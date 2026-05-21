# Contributing to Auto Unstack

Thank you for your interest in contributing to Auto Unstack! This document outlines how to contribute to the project.

## Code of Conduct

Be respectful and professional. We aim to maintain a welcoming, inclusive community.

## How to Contribute

### Reporting Issues

Found a bug? Have a feature suggestion? [Open an issue on GitHub](https://github.com/yourusername/auto-unstack/issues).

When reporting a bug, include:
- Device model and Android version
- Reproduction steps
- Expected vs. actual behavior
- Logcat output if applicable

### Submitting Changes

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Make your changes
4. Test thoroughly on at least one Samsung device
5. Commit with clear messages: `git commit -m "Fix: description"`
6. Push to your fork: `git push origin feature/your-feature`
7. Open a Pull Request with a clear description

### Code Style

- Follow Kotlin naming conventions
- Use 4 spaces for indentation
- Add comments for non-obvious logic
- Keep functions focused and testable
- Maintain backward compatibility where possible

### Testing

- Test on real Samsung devices if possible
- Test with various notification types
- Verify battery impact with Android Studio Profiler
- Check logcat for warnings/errors

### Commit Messages

Format: `<type>: <subject>`

Types:
- `fix:` — Bug fixes
- `feat:` — New features
- `refactor:` — Code refactoring
- `docs:` — Documentation updates
- `test:` — Test additions

Example: `fix: prevent double-click on same notification`

## Development Setup

1. Clone the repository
2. Open in Android Studio Koala or later
3. Sync Gradle files
4. Build and run on a Samsung device

## Questions?

Open an issue or discussion on GitHub.

---

Thank you for contributing! 🎉
