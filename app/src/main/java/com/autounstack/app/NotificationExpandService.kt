package com.autounstack.app

import android.accessibilityservice.AccessibilityService
import android.graphics.Rect
import android.os.SystemClock
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class NotificationExpandService : AccessibilityService() {
    private val TAG = "NotificationExpandService"
    private var lastGlobalClickTime = 0L
    private val GLOBAL_CLICK_COOLDOWN_MS = 450L
    private lateinit var preferencesManager: PreferencesManager

    override fun onServiceConnected() {
        super.onServiceConnected()
        preferencesManager = PreferencesManager(this)
        Log.i(TAG, "Accessibility service connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (!preferencesManager.isServiceEnabled()) {
            Log.d(TAG, "Service disabled; ignoring event")
            return
        }

        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED &&
            event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return
        }

        val now = SystemClock.uptimeMillis()
        if (now - lastGlobalClickTime < GLOBAL_CLICK_COOLDOWN_MS) {
            Log.d(TAG, "Global cooldown active")
            return
        }

        val root = rootInActiveWindow
        if (root == null) {
            Log.d(TAG, "No active window root available for event type ${event.eventType}")
            return
        }

        // Only handle events coming from SystemUI
        val evPkg = event.packageName?.toString()
        if (evPkg == null || evPkg != "com.android.systemui") {
            Log.d(TAG, "Ignoring event from package=$evPkg")
            return
        }

        val screenBounds = Rect()
        root.getBoundsInScreen(screenBounds)
        val screenWidth = screenBounds.width().takeIf { it > 0 } ?: resources.displayMetrics.widthPixels

        Log.d(TAG, "SystemUI event; scanning entire node tree; eventType=${event.eventType}, screenWidth=$screenWidth")

        scanNodeRecursive(root, screenWidth)
    }

    override fun onInterrupt() {
        Log.d(TAG, "Accessibility service interrupted")
    }

    private fun scanNodeRecursive(node: AccessibilityNodeInfo, screenWidth: Int) {
        processNode(node, screenWidth)

        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            scanNodeRecursive(child, screenWidth)
            child.recycle()
        }
    }


    private fun processNode(node: AccessibilityNodeInfo, screenWidth: Int) {
        val text = node.text?.toString()
        if (!isNumericBadgeText(text)) {
            return
        }

        if (!node.isVisibleToUser) {
            Log.d(TAG, "Skipping numeric node because it is not visible: text=$text")
            return
        }

        val bounds = Rect()
        node.getBoundsInScreen(bounds)
        if (!isNearRightSide(bounds, screenWidth)) {
            Log.d(TAG, "Skipping numeric node because it is not near right side: text=$text bounds=$bounds")
            return
        }

        Log.d(TAG, "Detected numeric badge node: text=$text bounds=$bounds")

        val clickableParent = findClickableParent(node)
        if (clickableParent == null) {
            Log.d(TAG, "No clickable parent found for numeric badge: text=$text bounds=$bounds")
            return
        }

        Log.d(TAG, "Attempting click on clickable parent for numeric badge: text=$text")
        val clicked = clickableParent.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        if (clicked) {
            lastGlobalClickTime = SystemClock.uptimeMillis()
            Log.d(TAG, "Click performed")
        } else {
            Log.d(TAG, "Click failed for numeric badge: text=$text")
        }
    }

    private fun isNumericBadgeText(text: String?): Boolean {
        if (text.isNullOrBlank()) {
            return false
        }
        val trimmed = text.trim()
        val isNumeric = trimmed.matches(Regex("^[0-9]+$"))
        Log.d(TAG, "Numeric badge text check: text=\"$trimmed\" isNumeric=$isNumeric")
        return isNumeric
    }

    private fun isNearRightSide(bounds: Rect, screenWidth: Int): Boolean {
        if (bounds.isEmpty) {
            return false
        }
        val threshold = (screenWidth * 0.75f).toInt()
        return bounds.right >= threshold
    }

    private fun findClickableParent(node: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        var current: AccessibilityNodeInfo? = node
        while (current != null) {
            if (current.isClickable) {
                Log.d(TAG, "Found clickable parent: class=${current.className} clickable=true")
                return current
            }
            current = current.parent
        }
        return null
    }
}
