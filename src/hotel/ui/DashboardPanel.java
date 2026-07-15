package hotel.ui;

import hotel.dao.DataManager;
import hotel.model.Booking;
import hotel.util.Theme;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class DashboardPanel extends JPanel {
    private MainFrame mf;
    private DataManager dm = DataManager.getInstance();
    private JLabel lTotalRooms, lAvailable, lTodayBook, lTotalBook;
    private DefaultTableModel tableModel;

    // Gradient colors exactly like image 3
    private static final Color[] CARD_FROM = {
        new Color(124, 58, 237),   // purple
        new Color(16, 185, 129),   // green
        new Color(245, 158, 11),   // orange
        new Color(59, 130, 246)    // blue
    };
    private static final Color[] CARD_TO = {
        new Color(99, 102, 241),   // indigo
        new Color(52, 211, 153),   // light green
        new Color(251, 191, 36),   // yellow
        new Color(96, 165, 250)    // light blue
    };

    public DashboardPanel(MainFrame mf) {
        this.mf = mf;
        setBackground(Theme.BG_CONTENT);
        setLayout(new BorderLayout());
        buildUI();
    }

    private void buildUI() {
        JPanel wrap = new JPanel();
        wrap.setBackground(Theme.BG_CONTENT);
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        // ── PAGE TITLE ────────────────────────────────────────────
        JLabel pageTitle = new JLabel("Dashboard Overview");
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        pageTitle.setForeground(new Color(15, 23, 42));
        pageTitle.setAlignmentX(LEFT_ALIGNMENT);
        wrap.add(pageTitle);

        JLabel pageSub = new JLabel("Welcome back, Admin! Here's what's happening today.");
        pageSub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        pageSub.setForeground(Theme.TEXT_GRAY);
        pageSub.setAlignmentX(LEFT_ALIGNMENT);
        wrap.add(pageSub);
        wrap.add(Box.createVerticalStrut(22));

        // ── GRADIENT STAT CARDS (like image 3) ───────────────────
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 14, 0));
        statsRow.setOpaque(false);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 115));
        statsRow.setAlignmentX(LEFT_ALIGNMENT);

        lTotalRooms = addGradCard(statsRow, "Total Rooms",      String.valueOf(dm.getTotalRooms()),      "All registered rooms",  0, "\uD83D\uDECF");
        lAvailable  = addGradCard(statsRow, "Available Rooms",  String.valueOf(dm.getAvailableRooms()),  "Ready for booking",      1, "\u2713");
        lTodayBook  = addGradCard(statsRow, "Today's Bookings", String.valueOf(dm.getTodayBookings()),   "Check-ins today",       2, "\uD83D\uDCC5");
        lTotalBook  = addGradCard(statsRow, "Total Bookings",   String.valueOf(dm.getTotalBookings()),   "All time bookings",     3, "\uD83D\uDCCB");
        wrap.add(statsRow);
        wrap.add(Box.createVerticalStrut(26));

        // ── QUICK ACTIONS (colored buttons like image 3) ──────────
        JLabel qlTitle = new JLabel("Quick Actions");
        qlTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        qlTitle.setForeground(new Color(15, 23, 42));
        qlTitle.setAlignmentX(LEFT_ALIGNMENT);
        wrap.add(qlTitle);
        wrap.add(Box.createVerticalStrut(12));

        JPanel linksRow = new JPanel(new GridLayout(1, 6, 10, 0));
        linksRow.setOpaque(false);
        linksRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        linksRow.setAlignmentX(LEFT_ALIGNMENT);

        addActionBtn(linksRow, "+ Add Room",    new Color(124,58,237), new Color(99,102,241),  () -> mf.navigateTo("rooms"));
        addActionBtn(linksRow, "Book Room",     new Color(16,185,129), new Color(52,211,153),  () -> openSearch());
        addActionBtn(linksRow, "Reservations",  new Color(245,158,11), new Color(251,191,36),  () -> mf.navigateTo("reservations"));
        addActionBtn(linksRow, "Payments",      new Color(239,68,68),  new Color(252,165,165), () -> mf.navigateTo("payments"));
        addActionBtn(linksRow, "Customers",     new Color(59,130,246), new Color(96,165,250),  () -> mf.navigateTo("customers"));
        addActionBtn(linksRow, "Reports",       new Color(20,184,166), new Color(94,234,212),  () -> mf.navigateTo("reports"));
        wrap.add(linksRow);
        wrap.add(Box.createVerticalStrut(26));

        // ── RECENT BOOKINGS ──────────────────────────────────────
        JPanel recHeader = new JPanel(new BorderLayout());
        recHeader.setOpaque(false);
        recHeader.setAlignmentX(LEFT_ALIGNMENT);
        recHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        JLabel recTitle = new JLabel("Recent Bookings");
        recTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        recTitle.setForeground(new Color(15, 23, 42));
        JButton viewAll = new JButton("View All \u2192");
        viewAll.setFont(new Font("SansSerif", Font.PLAIN, 12));
        viewAll.setForeground(Theme.PRIMARY);
        viewAll.setBorderPainted(false);
        viewAll.setFocusPainted(false);
        viewAll.setContentAreaFilled(false);
        viewAll.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        viewAll.addActionListener(e -> mf.navigateTo("reservations"));
        recHeader.add(recTitle, BorderLayout.WEST);
        recHeader.add(viewAll, BorderLayout.EAST);
        wrap.add(recHeader);
        wrap.add(Box.createVerticalStrut(10));

        // Table
        String[] cols = {"Booking ID","Customer","Room No","Room Type","Check-In","Check-Out","Amount","Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        Theme.styleTable(table);
        table.getColumnModel().getColumn(7).setCellRenderer((t,v,sel,foc,r,c) -> {
            JLabel l = Theme.statusLabel(v == null ? "" : v.toString());
            l.setHorizontalAlignment(SwingConstants.CENTER);
            return l;
        });

        // Wrap table in white card
        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        tableCard.add(Theme.darkScroll(table), BorderLayout.CENTER);
        tableCard.setAlignmentX(LEFT_ALIGNMENT);
        tableCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        wrap.add(tableCard);

        JScrollPane outerSp = new JScrollPane(wrap);
        outerSp.setBorder(null);
        outerSp.setBackground(Theme.BG_CONTENT);
        outerSp.getViewport().setBackground(Theme.BG_CONTENT);
        outerSp.getVerticalScrollBar().setUnitIncrement(14);
        add(outerSp, BorderLayout.CENTER);
        loadTable();
    }

    // ── GRADIENT STAT CARD (exactly like image 3) ─────────────────
    private JLabel addGradCard(JPanel parent, String label, String value, String sub, int colorIdx, String icon) {
        Color from = CARD_FROM[colorIdx];
        Color to   = CARD_TO[colorIdx];

        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Gradient background
                GradientPaint gp = new GradientPaint(0, 0, from, getWidth(), getHeight(), to);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                // Subtle decorative circle on right
                g2.setColor(new Color(255,255,255,20));
                g2.fillOval(getWidth()-70, -20, 100, 100);
                g2.setColor(new Color(255,255,255,12));
                g2.fillOval(getWidth()-40, getHeight()-40, 80, 80);
                g2.dispose();
            }
        };
        card.setLayout(new BorderLayout(10, 0));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 16));

        // Left: text info
        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label.toUpperCase());
        lbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        lbl.setForeground(new Color(255,255,255,200));

        JLabel val = new JLabel(value);
        val.setFont(new Font("SansSerif", Font.BOLD, 32));
        val.setForeground(Color.WHITE);

        JLabel sublbl = new JLabel(sub);
        sublbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
        sublbl.setForeground(new Color(255,255,255,160));

        info.add(lbl);
        info.add(Box.createVerticalStrut(2));
        info.add(val);
        info.add(Box.createVerticalStrut(2));
        info.add(sublbl);
        card.add(info, BorderLayout.WEST);

        // Right: icon
        JLabel iconLbl = new JLabel(icon, SwingConstants.CENTER);
        iconLbl.setFont(new Font("SansSerif", Font.PLAIN, 28));
        iconLbl.setForeground(new Color(255,255,255,100));
        iconLbl.setPreferredSize(new Dimension(50, 50));
        card.add(iconLbl, BorderLayout.EAST);

        parent.add(card);
        return val;
    }

    // ── COLORED ACTION BUTTON (like image 3) ──────────────────────
    private void addActionBtn(JPanel parent, String label, Color from, Color to, Runnable action) {
        JButton btn = new JButton(label) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp;
                if (getModel().isRollover()) {
                    gp = new GradientPaint(0,0,from.darker(),getWidth(),0,to.darker());
                } else {
                    gp = new GradientPaint(0,0,from,getWidth(),0,to);
                }
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                // arrow on right
                g2.setColor(new Color(255,255,255,120));
                g2.setFont(new Font("SansSerif",Font.BOLD,12));
                FontMetrics fm = g2.getFontMetrics();
                String arrow = ">";
                g2.drawString(arrow, getWidth()-fm.stringWidth(arrow)-10, getHeight()/2+fm.getAscent()/3);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 0));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> action.run());
        parent.add(btn);
    }

    private void openSearch() {
        new SearchRoomsDialog(mf, null).setVisible(true);
        refresh();
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        var list = dm.getBookings();
        int from = Math.max(0, list.size() - 5);
        for (int i = list.size()-1; i >= from; i--) {
            Booking b = list.get(i);
            tableModel.addRow(new Object[]{
                b.getBookingId(), b.getCustomerName(), b.getRoomNo(), b.getRoomType(),
                b.getCheckIn(), b.getCheckOut(), (int)b.getTotalAmount()+" PKR", b.getBookingStatus()
            });
        }
    }

    public void refresh() {
        lTotalRooms.setText(String.valueOf(dm.getTotalRooms()));
        lAvailable.setText(String.valueOf(dm.getAvailableRooms()));
        lTodayBook.setText(String.valueOf(dm.getTodayBookings()));
        lTotalBook.setText(String.valueOf(dm.getTotalBookings()));
        loadTable();
    }
}
