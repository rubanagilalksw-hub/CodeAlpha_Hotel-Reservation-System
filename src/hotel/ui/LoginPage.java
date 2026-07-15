package hotel.ui;

import hotel.dao.AppConfig;
import hotel.util.Theme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class LoginPage extends JFrame {
    private JTextField userField;
    private JPasswordField passField;

    public LoginPage() {
        setTitle("Hotel Reservation System - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(980, 620);
        setLocationRelativeTo(null);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new GridLayout(1, 2, 0, 0));
        root.setBackground(Theme.BG_MAIN);
        setContentPane(root);

        // ── LEFT PANEL - Branding ─────────────────────────────────
        JPanel leftPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(15, 23, 42), 0, getHeight(), new Color(30, 58, 138));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 255, 255, 8));
                g2.fillOval(-60, -60, 300, 300);
                g2.fillOval(getWidth()-180, getHeight()-200, 320, 320);
                g2.setColor(new Color(245, 158, 11, 15));
                g2.fillOval(50, getHeight()/2, 200, 200);
                g2.dispose();
            }
        };
        leftPanel.setLayout(new GridBagLayout());

        JPanel brandContent = new JPanel();
        brandContent.setOpaque(false);
        brandContent.setLayout(new BoxLayout(brandContent, BoxLayout.Y_AXIS));
        brandContent.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        JPanel shieldPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = getWidth()/2, cy = getHeight()/2;
                g2.setColor(new Color(245, 158, 11, 30));
                g2.fillOval(cx-45, cy-45, 90, 90);
                g2.setColor(new Color(245, 158, 11, 180));
                g2.fillOval(cx-32, cy-32, 64, 64);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 32));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("H", cx - fm.stringWidth("H")/2, cy + fm.getAscent()/3);
                g2.dispose();
            }
        };
        shieldPanel.setOpaque(false);
        shieldPanel.setPreferredSize(new Dimension(260, 90));
        shieldPanel.setMaximumSize(new Dimension(260, 90));
        shieldPanel.setAlignmentX(CENTER_ALIGNMENT);
        brandContent.add(shieldPanel);
        brandContent.add(Box.createVerticalStrut(16));

        JLabel hl1 = new JLabel("HOTEL", SwingConstants.CENTER);
        hl1.setFont(new Font("SansSerif", Font.BOLD, 32));
        hl1.setForeground(new Color(245, 158, 11));
        hl1.setAlignmentX(CENTER_ALIGNMENT);
        brandContent.add(hl1);

        JLabel hl2 = new JLabel("RESERVATION SYSTEM", SwingConstants.CENTER);
        hl2.setFont(new Font("SansSerif", Font.BOLD, 14));
        hl2.setForeground(new Color(148, 163, 184));
        hl2.setAlignmentX(CENTER_ALIGNMENT);
        brandContent.add(hl2);
        brandContent.add(Box.createVerticalStrut(32));

        String[] features = {
            "\u2713  Smart Room Booking System",
            "\u2713  Real-time Availability Tracking",
            "\u2713  Secure Payment Processing",
            "\u2713  Complete Reservation Management",
            "\u2713  Detailed Reports & Analytics"
        };
        for (String feat : features) {
            JLabel fl = new JLabel(feat);
            fl.setFont(new Font("SansSerif", Font.PLAIN, 13));
            fl.setForeground(new Color(148, 163, 184));
            fl.setAlignmentX(LEFT_ALIGNMENT);
            fl.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
            brandContent.add(fl);
        }

        brandContent.add(Box.createVerticalStrut(28));
        JLabel version = new JLabel("v1.0.0  \u2022  Professional Edition");
        version.setFont(new Font("SansSerif", Font.PLAIN, 11));
        version.setForeground(new Color(71, 85, 105));
        version.setAlignmentX(LEFT_ALIGNMENT);
        brandContent.add(version);

        leftPanel.add(brandContent);
        root.add(leftPanel);

        // ── RIGHT PANEL - Login Form ──────────────────────────────
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setPreferredSize(new Dimension(380, 420));
        form.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JLabel welcomeLabel = new JLabel("Welcome Back!");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        welcomeLabel.setForeground(new Color(15, 23, 42));
        welcomeLabel.setAlignmentX(LEFT_ALIGNMENT);
        form.add(welcomeLabel);

        JLabel subLabel = new JLabel("Sign in to your admin account");
        subLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subLabel.setForeground(new Color(100, 116, 139));
        subLabel.setAlignmentX(LEFT_ALIGNMENT);
        form.add(subLabel);
        form.add(Box.createVerticalStrut(30));

        // Username
        JLabel ul = new JLabel("Username");
        ul.setFont(new Font("SansSerif", Font.BOLD, 13));
        ul.setForeground(new Color(51, 65, 85));
        ul.setAlignmentX(LEFT_ALIGNMENT);
        form.add(ul);
        form.add(Box.createVerticalStrut(6));
        userField = Theme.styledField("");
        userField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        userField.setAlignmentX(LEFT_ALIGNMENT);
        form.add(userField);
        form.add(Box.createVerticalStrut(18));

        // Password
        JLabel pl = new JLabel("Password");
        pl.setFont(new Font("SansSerif", Font.BOLD, 13));
        pl.setForeground(new Color(51, 65, 85));
        pl.setAlignmentX(LEFT_ALIGNMENT);
        form.add(pl);
        form.add(Box.createVerticalStrut(6));
        passField = Theme.styledPassword();
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        passField.setAlignmentX(LEFT_ALIGNMENT);
        form.add(passField);
        form.add(Box.createVerticalStrut(12));

        // Remember / forgot row
        JPanel remRow = new JPanel(new BorderLayout());
        remRow.setOpaque(false);
        remRow.setAlignmentX(LEFT_ALIGNMENT);
        remRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        JCheckBox rem = new JCheckBox("Remember me");
        rem.setFont(new Font("SansSerif", Font.PLAIN, 12));
        rem.setForeground(new Color(100, 116, 139));
        rem.setOpaque(false);
        rem.setFocusPainted(false);
        JButton forgot = new JButton("Forgot Password?");
        forgot.setFont(new Font("SansSerif", Font.PLAIN, 12));
        forgot.setForeground(Theme.PRIMARY);
        forgot.setBorderPainted(false);
        forgot.setFocusPainted(false);
        forgot.setContentAreaFilled(false);
        forgot.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        remRow.add(rem, BorderLayout.WEST);
        remRow.add(forgot, BorderLayout.EAST);
        form.add(remRow);
        form.add(Box.createVerticalStrut(24));

        // Login button
        JButton loginBtn = new JButton("Sign In") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isPressed() ? Theme.PRIMARY_DARK
                        : getModel().isRollover() ? new Color(37, 99, 235)
                        : Theme.PRIMARY;
                GradientPaint gp = new GradientPaint(0, 0, c.brighter(), 0, getHeight(), c);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        loginBtn.setContentAreaFilled(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setFocusPainted(false);
        loginBtn.setOpaque(false);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        loginBtn.setAlignmentX(LEFT_ALIGNMENT);
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        form.add(loginBtn);

        rightPanel.add(form);
        root.add(rightPanel);

        loginBtn.addActionListener(e -> doLogin());
        passField.addActionListener(e -> doLogin());
    }

    private void doLogin() {
        String u = userField.getText().trim();
        String p = new String(passField.getPassword()).trim();
        String savedUser = AppConfig.getInstance().getAdminUsername();
        String savedPass = AppConfig.getInstance().getAdminPassword();
        if (u.equalsIgnoreCase(savedUser) && p.equals(savedPass)) {
            dispose();
            SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
        } else if (u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials!", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
