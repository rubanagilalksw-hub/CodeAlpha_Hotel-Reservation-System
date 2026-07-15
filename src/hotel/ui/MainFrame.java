package hotel.ui;

import hotel.util.Theme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentArea;
    private JButton[] navBtns;
    private int activeIdx = 0;

    private static final String[] NAV_ICONS  = {"\uD83C\uDFE0","\uD83D\uDECF","\uD83D\uDCC5","\uD83D\uDC64","\uD83D\uDCB3","\uD83D\uDCCA","\u2699","\uD83D\uDEAA"};
    private static final String[] NAV_LABELS = {"Dashboard","Rooms","Reservations","Customers","Payments","Reports","Settings","Logout"};
    private static final String[] CARD_KEYS  = {"dashboard","rooms","reservations","customers","payments","reports","settings","logout"};

    // Accent colors per nav item
    private static final Color[] NAV_COLORS = {
        new Color(124, 58, 237),  // Dashboard  - purple
        new Color(59, 130, 246),  // Rooms      - blue
        new Color(245, 158, 11),  // Reservations - amber
        new Color(16, 185, 129),  // Customers  - emerald
        new Color(239, 68, 68),   // Payments   - red
        new Color(20, 184, 166),  // Reports    - teal
        new Color(148, 163, 184), // Settings   - gray
        new Color(239, 68, 68),   // Logout     - red
    };

    private DashboardPanel    dashPanel;
    private RoomsPanel        roomsPanel;
    private ReservationsPanel reservationsPanel;
    private CustomersPanel    customersPanel;
    private PaymentsPanel     paymentsPanel;
    private ReportsPanel      reportsPanel;
    private SettingsPanel     settingsPanel;

    public MainFrame() {
        setTitle("Hotel Reservation System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1300, 760);
        setMinimumSize(new Dimension(1100, 650));
        setLocationRelativeTo(null);
        buildUI();
        switchTo(0);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0,0));
        root.setBackground(Theme.BG_MAIN);
        setContentPane(root);

        // ── TOP BAR ──────────────────────────────────────────────
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setPreferredSize(new Dimension(0, 54));
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0,Theme.BORDER),
            BorderFactory.createEmptyBorder(0,20,0,20)
        ));
        JLabel logo = new JLabel("Hotel Reservation System");
        logo.setFont(new Font("SansSerif", Font.BOLD, 16));
        logo.setForeground(new Color(15,23,42));
        topBar.add(logo, BorderLayout.WEST);

        JPanel adminBadge = new JPanel(new FlowLayout(FlowLayout.LEFT,6,0)) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(239,246,255));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),20,20);
                g2.dispose();
            }
        };
        adminBadge.setOpaque(false);
        adminBadge.setBorder(BorderFactory.createEmptyBorder(5,12,5,14));
        JLabel dot = new JLabel("\u25CF");
        dot.setFont(new Font("SansSerif",Font.PLAIN,10));
        dot.setForeground(new Color(22,163,74));
        JLabel adminLbl = new JLabel("Welcome, Admin");
        adminLbl.setFont(new Font("SansSerif",Font.BOLD,13));
        adminLbl.setForeground(new Color(30,64,175));
        adminBadge.add(dot); adminBadge.add(adminLbl);
        JPanel rightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT,0,0));
        rightTop.setOpaque(false); rightTop.add(adminBadge);
        topBar.add(rightTop, BorderLayout.EAST);
        root.add(topBar, BorderLayout.NORTH);

        // ── VIP SIDEBAR ──────────────────────────────────────────
        JPanel sidebar = buildVIPSidebar();
        root.add(sidebar, BorderLayout.WEST);

        // ── CONTENT ──────────────────────────────────────────────
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(Theme.BG_CONTENT);

        dashPanel         = new DashboardPanel(this);
        roomsPanel        = new RoomsPanel(this);
        reservationsPanel = new ReservationsPanel(this);
        customersPanel    = new CustomersPanel(this);
        paymentsPanel     = new PaymentsPanel(this);
        reportsPanel      = new ReportsPanel(this);
        settingsPanel     = new SettingsPanel(this);

        contentArea.add(dashPanel,         "dashboard");
        contentArea.add(roomsPanel,        "rooms");
        contentArea.add(reservationsPanel, "reservations");
        contentArea.add(customersPanel,    "customers");
        contentArea.add(paymentsPanel,     "payments");
        contentArea.add(reportsPanel,      "reports");
        contentArea.add(settingsPanel,     "settings");
        root.add(contentArea, BorderLayout.CENTER);
    }

    private JPanel buildVIPSidebar() {
        JPanel sidebar = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Deep dark base
                g2.setColor(new Color(10, 15, 28));
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Subtle purple glow top-left
                RadialGradientPaint rp1 = new RadialGradientPaint(
                    new Point2D.Float(0, 0), getWidth()*1.2f,
                    new float[]{0f, 1f},
                    new Color[]{new Color(124,58,237,40), new Color(10,15,28,0)}
                );
                g2.setPaint(rp1);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Subtle blue glow bottom-right
                RadialGradientPaint rp2 = new RadialGradientPaint(
                    new Point2D.Float(getWidth(), getHeight()), getWidth()*1.5f,
                    new float[]{0f, 1f},
                    new Color[]{new Color(59,130,246,25), new Color(10,15,28,0)}
                );
                g2.setPaint(rp2);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Right border - subtle gradient line
                GradientPaint bp = new GradientPaint(
                    getWidth()-1, 0, new Color(124,58,237,60),
                    getWidth()-1, getHeight(), new Color(59,130,246,60)
                );
                g2.setPaint(bp);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawLine(getWidth()-1, 0, getWidth()-1, getHeight());
                g2.dispose();
            }
        };
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setOpaque(false);

        // ── LOGO SECTION ─────────────────────────────────────────
        JPanel logoSection = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Glass-like header bg
                g2.setColor(new Color(255,255,255,6));
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Bottom divider - gradient
                GradientPaint div = new GradientPaint(
                    20, getHeight()-1, new Color(124,58,237,120),
                    getWidth()-20, getHeight()-1, new Color(59,130,246,120)
                );
                g2.setPaint(div);
                g2.setStroke(new BasicStroke(1));
                g2.drawLine(16, getHeight()-1, getWidth()-16, getHeight()-1);
                g2.dispose();
            }
        };
        logoSection.setOpaque(false);
        logoSection.setLayout(new BorderLayout());
        logoSection.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
        logoSection.setPreferredSize(new Dimension(220, 72));
        logoSection.setBorder(BorderFactory.createEmptyBorder(0,16,0,16));

        // Hotel icon circle
        JPanel iconCircle = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Gradient circle
                GradientPaint gp = new GradientPaint(0,0,new Color(124,58,237),getWidth(),getHeight(),new Color(59,130,246));
                g2.setPaint(gp);
                g2.fillOval(0, 0, getWidth(), getHeight());
                // H letter
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 18));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("H", getWidth()/2 - fm.stringWidth("H")/2, getHeight()/2 + fm.getAscent()/3);
                g2.dispose();
            }
        };
        iconCircle.setOpaque(false);
        iconCircle.setPreferredSize(new Dimension(38, 38));

        JPanel logoTextPanel = new JPanel();
        logoTextPanel.setOpaque(false);
        logoTextPanel.setLayout(new BoxLayout(logoTextPanel, BoxLayout.Y_AXIS));
        JLabel logoTitle = new JLabel("HMS Admin");
        logoTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        logoTitle.setForeground(Color.WHITE);
        JLabel logoSub = new JLabel("Hotel Management");
        logoSub.setFont(new Font("SansSerif", Font.PLAIN, 10));
        logoSub.setForeground(new Color(148,163,184));
        logoTextPanel.add(logoTitle);
        logoTextPanel.add(logoSub);

        JPanel logoInner = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        logoInner.setOpaque(false);
        logoInner.add(iconCircle);
        logoInner.add(logoTextPanel);
        logoSection.add(logoInner, BorderLayout.CENTER);
        sidebar.add(logoSection);
        sidebar.add(Box.createVerticalStrut(10));

        // ── NAV SECTION LABEL ────────────────────────────────────
        JLabel navLabel = new JLabel("  NAVIGATION");
        navLabel.setFont(new Font("SansSerif", Font.BOLD, 9));
        navLabel.setForeground(new Color(71, 85, 105));
        navLabel.setBorder(BorderFactory.createEmptyBorder(8, 18, 4, 0));
        navLabel.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(navLabel);

        // ── NAV BUTTONS ──────────────────────────────────────────
        navBtns = new JButton[NAV_LABELS.length];
        for (int i = 0; i < NAV_LABELS.length; i++) {
            if (i == NAV_LABELS.length - 1) {
                sidebar.add(Box.createVerticalGlue());
                // Divider before logout
                JPanel divider = new JPanel() {
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2=(Graphics2D)g.create();
                        GradientPaint gp = new GradientPaint(
                            16, 0, new Color(255,255,255,0),
                            getWidth()/2, 0, new Color(255,255,255,30)
                        );
                        // Second half
                        g2.setPaint(gp); g2.fillRect(0,0,getWidth(),getHeight());
                        g2.dispose();
                    }
                };
                divider.setOpaque(false);
                divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
                divider.setPreferredSize(new Dimension(220, 1));
                sidebar.add(divider);
                sidebar.add(Box.createVerticalStrut(4));
            }
            final int idx = i;
            navBtns[i] = buildNavButton(i, i == 0);
            navBtns[i].addActionListener(e -> handleNav(idx));
            sidebar.add(navBtns[i]);
            if (i < NAV_LABELS.length - 2)
                sidebar.add(Box.createVerticalStrut(2));
        }
        sidebar.add(Box.createVerticalStrut(12));

        return sidebar;
    }

    private JButton buildNavButton(int idx, boolean active) {
        Color accentColor = NAV_COLORS[idx];

        JButton btn = new JButton() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean isActive = Boolean.TRUE.equals(getClientProperty("active"));
                boolean hover    = getModel().isRollover();

                if (isActive) {
                    GradientPaint gp = new GradientPaint(
                        0, 0, new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 50),
                        getWidth(), 0, new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 15)
                    );
                    g2.setPaint(gp);
                    g2.fillRoundRect(8, 2, getWidth()-16, getHeight()-4, 10, 10);
                    GradientPaint pill = new GradientPaint(0, 0, accentColor.brighter(), 0, getHeight(), accentColor);
                    g2.setPaint(pill);
                    g2.fillRoundRect(4, 8, 4, getHeight()-16, 4, 4);
                    g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 80));
                    g2.fillOval(getWidth()-16, getHeight()/2-4, 8, 8);
                } else if (hover) {
                    g2.setColor(new Color(255,255,255,8));
                    g2.fillRoundRect(8, 2, getWidth()-16, getHeight()-4, 10, 10);
                    g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 60));
                    g2.fillRoundRect(4, 10, 3, getHeight()-20, 3, 3);
                }

                // Icon background box
                if (isActive) {
                    GradientPaint iconBg = new GradientPaint(14, 0, accentColor.brighter(), 14+26, 26, accentColor);
                    g2.setPaint(iconBg);
                } else {
                    g2.setColor(new Color(255,255,255,10));
                }
                g2.fillRoundRect(14, getHeight()/2-13, 26, 26, 7, 7);

                g2.dispose();

                // Draw icon manually
                Graphics2D g3 = (Graphics2D) g.create();
                g3.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g3.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
                g3.setColor(Boolean.TRUE.equals(getClientProperty("active")) ? Color.WHITE : new Color(148,163,184));
                FontMetrics fm = g3.getFontMetrics();
                String ico = NAV_ICONS[idx];
                int iconX = 14 + (26 - fm.stringWidth(ico)) / 2;
                int iconY = getHeight()/2 + fm.getAscent()/2 - 2;
                g3.drawString(ico, iconX, iconY);

                // Draw label text
                g3.setFont(new Font("SansSerif", Boolean.TRUE.equals(getClientProperty("active")) ? Font.BOLD : Font.PLAIN, 13));
                g3.setColor(Boolean.TRUE.equals(getClientProperty("active")) ? Color.WHITE : new Color(148,163,184));
                fm = g3.getFontMetrics();
                int textY = getHeight()/2 + fm.getAscent()/2 - 2;
                g3.drawString(NAV_LABELS[idx], 50, textY);
                g3.dispose();
            }
        };

        btn.setText("");  // We draw everything manually
        btn.putClientProperty("active", active);
        btn.setFont(new Font("SansSerif", active ? Font.BOLD : Font.PLAIN, 13));
        btn.setForeground(active ? Color.WHITE : new Color(148, 163, 184));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        btn.setPreferredSize(new Dimension(220, 46));
        btn.setMaximumSize(new Dimension(220, 46));
        btn.setMinimumSize(new Dimension(220, 46));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

        private void handleNav(int idx) {
        if (idx == NAV_LABELS.length - 1) {
            int r = JOptionPane.showConfirmDialog(this, "Logout from system?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) { dispose(); new LoginPage().setVisible(true); }
            return;
        }
        switchTo(idx);
    }

    public void switchTo(int idx) {
        activeIdx = idx;
        for (int i = 0; i < navBtns.length; i++) {
            boolean a = (i == idx);
            navBtns[i].putClientProperty("active", a);
            navBtns[i].setFont(new Font("SansSerif", a ? Font.BOLD : Font.PLAIN, 13));
            navBtns[i].setForeground(a ? Color.WHITE : new Color(148,163,184));
            navBtns[i].repaint();
        }
        String key = CARD_KEYS[idx];
        if      ("dashboard".equals(key))    dashPanel.refresh();
        else if ("rooms".equals(key))        roomsPanel.refresh();
        else if ("reservations".equals(key)) reservationsPanel.refresh();
        else if ("customers".equals(key))    customersPanel.refresh();
        else if ("payments".equals(key))     paymentsPanel.refresh();
        else if ("reports".equals(key))      reportsPanel.refresh();
        else if ("settings".equals(key))     settingsPanel.refresh();
        cardLayout.show(contentArea, key);
    }

    public void navigateTo(String key) {
        for (int i=0; i<CARD_KEYS.length; i++)
            if (CARD_KEYS[i].equals(key)) { switchTo(i); return; }
    }

    public ReservationsPanel getReservationsPanel() { return reservationsPanel; }
    public PaymentsPanel     getPaymentsPanel()     { return paymentsPanel; }
    public RoomsPanel        getRoomsPanel()         { return roomsPanel; }
}
