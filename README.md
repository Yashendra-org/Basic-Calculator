# 🧮 Animated Calculator — Java Swing

A clean, modern calculator desktop app built in **Java Swing**, featuring custom-painted UI components and smooth animations — built as a minor academic project.

> No external libraries. No frameworks. Just core Java (JDK 8+).

---

## ✨ Features

- **Standard arithmetic operations** — addition, subtraction, multiplication, division, percentage, sign toggle
- **Animated UI**
  - Smooth window fade-in on launch
  - Animated color transitions on button hover
  - "Pulse" press animation on click
  - Scale/pop animation when a result is displayed
  - Shake animation on error (e.g. divide by zero)
- **Custom rounded, flat-style UI** — buttons and display are hand-painted with `Graphics2D` instead of using default Swing styling
- **Zero dependencies** — runs with a plain JDK, no Maven/Gradle required

---

## 🖥️ Demo

<!-- Add a screenshot or GIF here once you have one. Example: -->
<!-- ![Calculator demo](assets/demo.gif) -->

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
git clone https://github.com/<your-username>/animated-calculator.git
cd animated-calculator/src

# Compile
javac Calculator.java

# Run
java Calculator
```

---

## 🧠 What I Learned / Implementation Notes

- Implementing **frame-based animation** in Swing using `javax.swing.Timer` instead of threads, to keep all UI updates safely on the Event Dispatch Thread (EDT)
- Custom component rendering by overriding `paintComponent()` with `Graphics2D` for rounded buttons, anti-aliasing, and interpolated color transitions
- Structuring a simple state machine for calculator logic (current value, pending operator, new-number flag)
- Designing a minimal, modern dark-themed UI from scratch without an external design library

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

Built as a minor project to practice Java GUI development, custom rendering, and animation fundamentals.

Feel free to fork, star ⭐, or open issues with suggestions!
