import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;
import java.util.Map;

/**
 * Animated Calculator - Minor Project
 * Features:
 *  - Smooth fade-in window animation
 *  - Button hover & press color transition animations
 *  - Animated "shake" effect on error (e.g. divide by zero)
 *  - Animated slide/scale effect when result is displayed
 *  - Rounded, modern flat-UI buttons
 */
public class Calculator extends JFrame {

    private final JTextField display;
    private double currentValue = 0;
    private String pendingOperator = "";
    private boolean startNewNumber = true;

    // Colors
    private static final Color BG_DARK      = new Color(28, 28, 35);
    private static final Color DISPLAY_BG   = new Color(20, 20, 26);
    private static final Color NUM_BTN      = new Color(54, 54, 64);
    private static final Color NUM_BTN_HOV  = new Color(74, 74, 88);
    private static final Color OP_BTN       = new Color(255, 149, 0);
    private static final Color OP_BTN_HOV   = new Color(255, 178, 64);
    private static final Color FUN_BTN      = new Color(90, 90, 105);
    private static final Color FUN_BTN_HOV  = new Color(115, 115, 132);
    private static final Color TEXT_WHITE   = new Color(240, 240, 245);

    public Calculator() {
        setTitle("Animated Calculator - Minor Project");
        setSize(380, 560);
        setMinimumSize(new Dimension(340, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout(12, 12));
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));

        // ---------- Display ----------
        display = new JTextField("0");
        display.setEditable(false);
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setFont(new Font("Segoe UI", Font.BOLD, 48));
        display.setForeground(TEXT_WHITE);
        display.setBackground(DISPLAY_BG);
        display.setBorder(new EmptyBorder(20, 20, 20, 20));
        display.setOpaque(true);

        JPanel displayWrap = new RoundedPanel(24, DISPLAY_BG);
        displayWrap.setLayout(new BorderLayout());
        displayWrap.add(display, BorderLayout.CENTER);
        displayWrap.setPreferredSize(new Dimension(100, 110));

        add(displayWrap, BorderLayout.NORTH);

        // ---------- Buttons ----------
        JPanel grid = new JPanel(new GridLayout(5, 4, 12, 12));
        grid.setOpaque(false);

        String[][] layout = {
            {"C", "⌫", "%", "÷"},
            {"7", "8", "9", "×"},
            {"4", "5", "6", "−"},
            {"1", "2", "3", "+"},
            {"±", "0", ".", "="}
        };

        for (String[] row : layout) {
            for (String label : row) {
                grid.add(createAnimatedButton(label));
            }
        }

        add(grid, BorderLayout.CENTER);

        // ---------- Fade-in animation on launch ----------
        // Note: Frame.setOpacity() requires an UNDECORATED window on some
        // Windows JDKs and throws IllegalComponentStateException otherwise,
        // so we skip whole-window opacity and rely on the button/text
        // animations below for visual polish instead.
    }

    // ---------- Animated rounded button factory ----------
    private JButton createAnimatedButton(String label) {
        boolean isOperator = "÷×−+=".contains(label) && label.length() == 1;
        boolean isFunction = label.equals("C") || label.equals("⌫") || label.equals("%") || label.equals("±");

        final Color base = isOperator ? OP_BTN : isFunction ? FUN_BTN : NUM_BTN;
        final Color hover = isOperator ? OP_BTN_HOV : isFunction ? FUN_BTN_HOV : NUM_BTN_HOV;

        AnimatedButton btn = new AnimatedButton(label, base, hover);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 22));
        btn.setForeground(TEXT_WHITE);

        btn.addActionListener(e -> {
            btn.playClickPulse();
            handleInput(label);
        });

        return btn;
    }

    // ---------- Calculator logic ----------
    private void handleInput(String key) {
        switch (key) {
            case "C":
                currentValue = 0;
                pendingOperator = "";
                startNewNumber = true;
                animateText("0");
                break;
            case "⌫":
                String cur = display.getText();
                if (cur.length() > 1 && !startNewNumber) {
                    display.setText(cur.substring(0, cur.length() - 1));
                } else {
                    display.setText("0");
                    startNewNumber = true;
                }
                break;
            case "±":
                toggleSign();
                break;
            case "%":
                try {
                    double v = Double.parseDouble(display.getText());
                    animateText(formatNumber(v / 100));
                    startNewNumber = true;
                } catch (NumberFormatException ignored) {}
                break;
            case "÷": case "×": case "−": case "+":
                try {
                    currentValue = Double.parseDouble(display.getText());
                } catch (NumberFormatException ignored) {}
                pendingOperator = key;
                startNewNumber = true;
                break;
            case "=":
                compute();
                break;
            case ".":
                if (startNewNumber) {
                    display.setText("0.");
                    startNewNumber = false;
                } else if (!display.getText().contains(".")) {
                    display.setText(display.getText() + ".");
                }
                break;
            default: // digits
                if (startNewNumber || display.getText().equals("0")) {
                    display.setText(key);
                    startNewNumber = false;
                } else {
                    display.setText(display.getText() + key);
                }
        }
    }

    private void toggleSign() {
        String t = display.getText();
        if (t.equals("0")) return;
        if (t.startsWith("-")) {
            display.setText(t.substring(1));
        } else {
            display.setText("-" + t);
        }
    }

    private void compute() {
        if (pendingOperator.isEmpty()) return;
        double second;
        try {
            second = Double.parseDouble(display.getText());
        } catch (NumberFormatException ex) {
            return;
        }
        double result = 0;
        boolean error = false;

        switch (pendingOperator) {
            case "÷":
                if (second == 0) {
                    error = true;
                } else {
                    result = currentValue / second;
                }
                break;
            case "×": result = currentValue * second; break;
            case "−": result = currentValue - second; break;
            case "+": result = currentValue + second; break;
        }

        if (error) {
            display.setText("Error");
            shakeWindow();
        } else {
            animateText(formatNumber(result));
        }

        pendingOperator = "";
        startNewNumber = true;
    }

    private String formatNumber(double d) {
        if (d == (long) d) {
            return String.valueOf((long) d);
        }
        return String.valueOf(d);
    }

    // Animated text reveal (slight scale-pop) for results
    private void animateText(String finalText) {
        display.setText(finalText);
        Font original = display.getFont();
        Timer t = new Timer(15, null);
        t.addActionListener(new ActionListener() {
            int step = 0;
            public void actionPerformed(ActionEvent e) {
                step++;
                float scale = 1f + (float) (0.15 * Math.sin(Math.PI * step / 8));
                display.setFont(original.deriveFont(original.getSize2D() * (step <= 8 ? scale : 1f)));
                if (step >= 8) {
                    display.setFont(original);
                    t.stop();
                }
            }
        });
        t.start();
    }

    // Shake animation for errors (e.g., divide by zero)
    private void shakeWindow() {
        Point original = getLocation();
        Timer t = new Timer(20, null);
        t.addActionListener(new ActionListener() {
            int step = 0;
            final int[] offsets = {8, -8, 6, -6, 4, -4, 2, -2, 0};
            public void actionPerformed(ActionEvent e) {
                if (step >= offsets.length) {
                    setLocation(original);
                    t.stop();
                    return;
                }
                setLocation(original.x + offsets[step], original.y);
                step++;
            }
        });
        t.start();
    }

    // ---------- Rounded panel helper ----------
    static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color color;
        RoundedPanel(int radius, Color color) {
            this.radius = radius;
            this.color = color;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ---------- Animated rounded button with hover/press transitions ----------
    static class AnimatedButton extends JButton {
        private Color baseColor;
        private Color hoverColor;
        private Color currentColor;
        private float scale = 1f;
        private Timer hoverTimer;
        private Timer pulseTimer;

        AnimatedButton(String text, Color base, Color hover) {
            super(text);
            this.baseColor = base;
            this.hoverColor = hover;
            this.currentColor = base;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    animateColor(hoverColor);
                }
                public void mouseExited(MouseEvent e) {
                    animateColor(baseColor);
                }
            });
        }

        private void animateColor(Color target) {
            if (hoverTimer != null && hoverTimer.isRunning()) hoverTimer.stop();
            Color start = currentColor;
            hoverTimer = new Timer(12, null);
            hoverTimer.addActionListener(new ActionListener() {
                int step = 0;
                final int total = 10;
                public void actionPerformed(ActionEvent e) {
                    step++;
                    float ratio = (float) step / total;
                    int r = (int) (start.getRed()   + (target.getRed()   - start.getRed())   * ratio);
                    int g = (int) (start.getGreen() + (target.getGreen() - start.getGreen()) * ratio);
                    int b = (int) (start.getBlue()  + (target.getBlue()  - start.getBlue())  * ratio);
                    currentColor = new Color(r, g, b);
                    repaint();
                    if (step >= total) hoverTimer.stop();
                }
            });
            hoverTimer.start();
        }

        // Pulse/scale animation played on click
        void playClickPulse() {
            if (pulseTimer != null && pulseTimer.isRunning()) pulseTimer.stop();
            pulseTimer = new Timer(10, null);
            pulseTimer.addActionListener(new ActionListener() {
                int step = 0;
                final int total = 10;
                public void actionPerformed(ActionEvent e) {
                    step++;
                    // dip down to 0.9 then back to 1.0
                    double t = (double) step / total;
                    scale = (float) (0.9 + 0.1 * Math.sin(Math.PI * t / 2 + Math.PI / 2) * -1 + 0.1);
                    scale = (float) (1.0 - 0.1 * Math.sin(Math.PI * t));
                    repaint();
                    if (step >= total) {
                        scale = 1f;
                        repaint();
                        pulseTimer.stop();
                    }
                }
            });
            pulseTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            double sw = w * scale;
            double sh = h * scale;
            double ox = (w - sw) / 2;
            double oy = (h - sh) / 2;

            g2.setColor(currentColor);
            g2.fill(new RoundRectangle2D.Double(ox, oy, sw, sh, 20, 20));

            g2.setColor(getForeground());
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            String text = getText();
            int tx = (w - fm.stringWidth(text)) / 2;
            int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(text, tx, ty);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            Calculator calc = new Calculator();
            calc.setVisible(true);
        });
    }
}