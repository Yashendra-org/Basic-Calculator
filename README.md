# 🧮 Calculator — Java Swing

A clean, modern calculator desktop app built in **Java Swing**, featuring custom-painted UI components and smooth animations — built as a minor academic project.

> No external libraries. No frameworks. Just core Java (JDK 8+).

---

## ✨ Features

- **Standard arithmetic operations** — addition, subtraction, multiplication, division, percentage, sign toggle
- **Animated UI**
  - Animated content fade-in on launch
  - Animated color transitions on button hover
  - "Pulse" press animation on click
  - Scale/pop animation when a result is displayed
  - Shake animation on error (e.g. divide by zero)
- **Custom rounded, flat-style UI** — buttons and display are hand-painted with `Graphics2D` instead of using default Swing styling
- **Zero dependencies** — runs with a plain JDK, no Maven/Gradle required
- **Cross-platform safe symbols** — uses plain ASCII operators (`/`, `x`, `-`, `+/-`, `<-`) instead of Unicode glyphs, so the UI renders correctly on every OS/terminal without font or encoding issues


---

## 🛠️ Tech Stack

| Component        | Details                          |
|-------------------|-----------------------------------|
| Language          | Java (JDK 8+)                    |
| UI Toolkit        | Java Swing                       |
| Animation         | `javax.swing.Timer` (frame-based) |
| Custom Rendering  | `Graphics2D`, `RoundRectangle2D` |

---

## 📂 Project Structure

```
animated-calculator/
├── src/
│   └── Calculator.java     # Main application source
├── README.md
├── LICENSE
└── .gitignore
```

---

## 🚀 Getting Started

### Prerequisites
- Java JDK 8 or higher installed ([download here](https://www.oracle.com/java/technologies/downloads/) or use OpenJDK)

### Run locally

```bash
# Clone the repository
git clone https://github.com/Yashendra-org/Basic-Calculator.git
cd Calculator/src

# Compile
javac Calculator.java

# Run
java Calculator
```

### Run in VS Code

1. Install the **"Extension Pack for Java"** (by Microsoft) from the Extensions tab.
2. Open the project folder in VS Code.
3. Open `src/Calculator.java`, then either:
   - Click the ▶️ **Run** button in the top-right corner, or
   - Right-click the file → **Run Java**, or
   - Use the integrated terminal and run the `javac` / `java` commands above manually.

---

## 🐞 Troubleshooting / Known Issues Fixed

This project went through a few real debugging fixes during development — documented here for reference:

| Issue | Cause | Fix |
|---|---|---|
| `IllegalComponentStateException: The frame is decorated` on launch | `Frame.setOpacity()` only works on **undecorated** windows on some Windows JDKs | Removed whole-window opacity fade-in; kept button/text animations instead |
| Operator symbols (`÷ × − ⌫ ±`) showing as `?` or boxes | Unicode characters in source file conflicting with default Windows file encoding (CP1252 vs UTF-8) | Replaced all Unicode operator symbols with plain ASCII equivalents (`/`, `x`, `-`, `+/-`, `<-`) so the file is pure ASCII and renders identically on any system |
| `git push` rejected with "fetch first" | Local repo history didn't match the remote (GitHub auto-created README/LICENSE on repo creation) | Cloned the remote repo fresh and added project files into it, instead of force-pushing over existing history |
| Compiled `.class` files getting committed to Git | `.gitignore` wasn't in place before the first `git add .` | Added `*.class` to `.gitignore` and untracked previously committed class files with `git rm --cached` |

If you hit the opacity crash or weird symbol rendering, make sure you're using the latest `Calculator.java` from this repo — both issues are already fixed in the current version.

---

## 🧠 What I Learned / Implementation Notes

- Implementing **frame-based animation** in Swing using `javax.swing.Timer` instead of threads, to keep all UI updates safely on the Event Dispatch Thread (EDT)
- Custom component rendering by overriding `paintComponent()` with `Graphics2D` for rounded buttons, anti-aliasing, and interpolated color transitions
- Structuring a simple state machine for calculator logic (current value, pending operator, new-number flag)
- Designing a minimal, modern dark-themed UI from scratch without an external design library
- Debugging platform-specific Swing quirks (e.g. `setOpacity` restrictions on decorated windows) and Windows file-encoding issues with Unicode source characters
- Practicing a real Git/GitHub workflow: resolving rejected pushes, merging unrelated histories, and keeping compiled artifacts out of version control via `.gitignore`

---

## 🗺️ Roadmap / Possible Improvements

- [ ] Keyboard input support
- [ ] Scientific calculator mode (sin, cos, log, etc.)
- [ ] Calculation history panel
- [ ] Light/dark theme toggle
- [ ] Unit tests for calculator logic

---

## 📄 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

---

## 🙋 About

Built as a minor project to practice Java GUI development, custom rendering, animation fundamentals, and a real-world Git/GitHub workflow.

Feel free to fork, star ⭐, or open issues with suggestions!