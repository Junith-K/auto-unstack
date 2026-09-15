package com.autounstack.app

import android.accessibilityservice.AccessibilityService
import android.app.KeyguardManager
import android.os.SystemClock
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class NotificationExpandService : AccessibilityService() {
    private val TAG = "NotificationExpandService"
    private val GROUP_CHILD_COUNT_ID = "android:id/group_child_count_number"
    private var lastGlobalClickTime = 0L
    private val GLOBAL_CLICK_COOLDOWN_MS = 450L
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var keyguardManager: KeyguardManager

    // After we expand stacks once for a shade open, ignore further events until the shade closes.
    // Prevents re-clicks during dismiss (which re-open the shade).
    private var hasExpandedThisShadeSession = false

    override fun onServiceConnected() {
        super.onServiceConnected()
        preferencesManager = PreferencesManager(this)
        keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
        Log.i(TAG, "Accessibility service connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (!preferencesManager.isServiceEnabled()) {
            Log.d(TAG, "Service disabled; ignoring event")
            return
        }

        // Lock screen shares SystemUI and shows numeric PIN / lock-screen notification counts.
        // Never click there — it can open the shade in a loop and block unlock.
        if (keyguardManager.isKeyguardLocked) {
            hasExpandedThisShadeSession = false
            Log.d(TAG, "Keyguard locked; ignoring event")
            return
        }

        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED &&
            event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return
        }

        val root = rootInActiveWindow
        if (root == null) {
            hasExpandedThisShadeSession = false
            Log.d(TAG, "No active window root; shade session reset")
            return
        }

        val rootPkg = root.packageName?.toString()
        // Shade is only "open" for us when SystemUI is the active window root.
        // When the user dismisses the shade, root becomes launcher/app → reset session.
        if (rootPkg != "com.android.systemui") {
            if (hasExpandedThisShadeSession) {
                Log.d(TAG, "Left SystemUI (root=$rootPkg); shade session reset")
            }
            hasExpandedThisShadeSession = false
            return
        }

        val evPkg = event.packageName?.toString()
        if (evPkg != null && evPkg != "com.android.systemui") {
            hasExpandedThisShadeSession = false
            Log.d(TAG, "Ignoring event from package=$evPkg; shade session reset")
            return
        }

        if (hasExpandedThisShadeSession) {
            Log.d(TAG, "Already expanded this shade session; ignoring")
            return
        }

        val now = SystemClock.uptimeMillis()
        if (now - lastGlobalClickTime < GLOBAL_CLICK_COOLDOWN_MS) {
            Log.d(TAG, "Global cooldown active")
            return
        }

        Log.d(TAG, "SystemUI event; scanning node tree; eventType=${event.eventType}")

        scanNodeRecursive(root)
    }

    override fun onInterrupt() {
        Log.d(TAG, "Accessibility service interrupted")
    }

    private fun scanNodeRecursive(node: AccessibilityNodeInfo) {
        processNode(node)

        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            scanNodeRecursive(child)
            child.recycle()
        }
    }

    private fun processNode(node: AccessibilityNodeInfo) {
        val text = node.text?.toString()
        if (node.viewIdResourceName != GROUP_CHILD_COUNT_ID) {
            return
        }

        if (!node.isVisibleToUser) {
            Log.d(TAG, "Skipping hidden group count node: text=$text")
            return
        }

        Log.d(TAG, "Detected grouped-notification count node: text=$text")

        val clickableParent = findClickableParent(node)
        if (clickableParent == null) {
            Log.d(TAG, "No clickable parent found for group count node: text=$text")
            return
        }

        Log.d(TAG, "Attempting click on clickable parent for numeric badge: text=$text")
        val clicked = clickableParent.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        if (clicked) {
            lastGlobalClickTime = SystemClock.uptimeMillis()
            hasExpandedThisShadeSession = true
            Log.d(TAG, "Click performed; shade session marked handled")
        } else {
            Log.d(TAG, "Click failed for numeric badge: text=$text")
        }
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
