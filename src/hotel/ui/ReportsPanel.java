package hotel.ui;

import hotel.dao.DataManager;
import hotel.model.*;
import hotel.util.Theme;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ReportsPanel extends JPanel {
    private MainFrame mf;
    private DataManager dm = DataManager.getInstance();

    public ReportsPanel(MainFrame mf) {
        this.mf = mf;
        setBackground(Theme.BG_CONTENT);
        setLayout(new BorderLayout());
        buildUI();
    }

    private void buildUI() {
        JPanel wrap = new JPanel(new BorderLayout(0, 14));
        wrap.setBackground(Theme.BG_CONTENT);
        wrap.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        // Page title
        JPanel titleArea = new JPanel();
        titleArea.setLayout(new BoxLayout(titleArea, BoxLayout.Y_AXIS));
        titleArea.setOpaque(false);
        titleArea.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        JLabel pageTitle = new JLabel("Reports & Analytics");
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        pageTitle.setForeground(new Color(15, 23, 42));
        JLabel pageSub = new JLabel("View and download detailed reports for all hotel operations");
        pageSub.setFont(Theme.FONT_SMALL); pageSub.setForeground(Theme.TEXT_GRAY);
        titleArea.add(pageTitle); titleArea.add(pageSub);
        wrap.add(titleArea, BorderLayout.NORTH);

        JPanel centerAll = new JPanel();
        centerAll.setBackground(Theme.BG_CONTENT);
        centerAll.setLayout(new BoxLayout(centerAll, BoxLayout.Y_AXIS));

        // ── 4 REPORT CARDS ───────────────────────────────────────
        JPanel cardsRow = new JPanel(new GridLayout(1, 4, 14, 0));
        cardsRow.setOpaque(false);
        cardsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));
        cardsRow.setAlignmentX(LEFT_ALIGNMENT);

        addReportCard(cardsRow, "\uD83D\uDCB3", "Payments Report",
            "View all payments made\nin the system.",
            new Color(245, 158, 11), "payments_report");
        addReportCard(cardsRow, "\uD83D\uDECF", "Rooms Report",
            "View all rooms with\ntheir status details.",
            new Color(59, 130, 246), "rooms_report");
        addReportCard(cardsRow, "\uD83D\uDC64", "Customers Report",
            "View all customers and\ntheir booking history.",
            new Color(22, 163, 74), "customers_report");
        addReportCard(cardsRow, "\uD83D\uDCB0", "Revenue Report",
            "View total revenue and\nincome analytics.",
            new Color(147, 51, 234), "revenue_report");

        centerAll.add(cardsRow);
        centerAll.add(Box.createVerticalStrut(20));

        // ── SUMMARY STATS ────────────────────────────────────────
        JLabel sl = sectionLabel("Summary Statistics");
        centerAll.add(sl);
        centerAll.add(Box.createVerticalStrut(10));

        long confirmed = dm.getBookings().stream().filter(b -> "Confirmed".equals(b.getBookingStatus())).count();
        long cancelled = dm.getBookings().stream().filter(b -> "Cancelled".equals(b.getBookingStatus())).count();
        double avg = dm.getBookings().isEmpty() ? 0 :
            dm.getBookings().stream().mapToDouble(Booking::getTotalAmount).average().orElse(0);

        JPanel statsGrid = new JPanel(new GridLayout(2, 3, 12, 10));
        statsGrid.setOpaque(false);
        statsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        statsGrid.setAlignmentX(LEFT_ALIGNMENT);

        miniStat(statsGrid, "\uD83D\uDCCB", "Total Bookings",
            String.valueOf(dm.getTotalBookings()), Theme.BLUE_STAT);
        miniStat(statsGrid, "\u2705", "Confirmed",
            String.valueOf(confirmed), Theme.GREEN_STAT);
        miniStat(statsGrid, "\u274C", "Cancelled",
            String.valueOf(cancelled), Theme.RED);
        miniStat(statsGrid, "\uD83D\uDCB0", "Total Revenue",
            String.format("%,.0f PKR", dm.getTotalRevenue()), Theme.GOLD);
        miniStat(statsGrid, "\uD83D\uDCCA", "Avg. Booking",
            String.format("%,.0f PKR", avg), new Color(147, 51, 234));
        miniStat(statsGrid, "\uD83D\uDC65", "Customers",
            String.valueOf(dm.getCustomers().size()), new Color(20, 184, 166));

        centerAll.add(statsGrid);


        wrap.add(centerAll, BorderLayout.CENTER);
        add(wrap, BorderLayout.CENTER);
    }

    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 14));
        l.setForeground(new Color(15, 23, 42));
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    // ── REPORT CARD ──────────────────────────────────────────────
    private void addReportCard(JPanel parent, String icon, String title, String desc, Color accent, String type) {
        JPanel card = new JPanel() {
            boolean hover = false;
            { addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                public void mouseExited(MouseEvent e)  { hover = false; repaint(); }
            }); }
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 8));
                g2.fillRoundRect(2, 3, getWidth()-2, getHeight()-2, 12, 12);
                g2.setColor(hover ? new Color(accent.getRed(),accent.getGreen(),accent.getBlue(),8) : Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth()-2, getHeight()-2, 12, 12);
                g2.setColor(hover ? accent : Theme.BORDER);
                g2.setStroke(new BasicStroke(hover ? 1.5f : 1f));
                g2.drawRoundRect(0, 0, getWidth()-3, getHeight()-3, 12, 12);
                // Top accent bar
                g2.setColor(accent);
                g2.setStroke(new BasicStroke(3));
                g2.drawLine(16, 1, getWidth()-18, 1);
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(16, 14, 14, 14));

        // Icon circle
        JPanel iconCircle = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 20));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        iconCircle.setPreferredSize(new Dimension(46, 46));
        iconCircle.setMaximumSize(new Dimension(46, 46));
        iconCircle.setOpaque(false);
        iconCircle.setLayout(new GridBagLayout());
        JLabel il = new JLabel(icon);
        il.setFont(new Font("SansSerif", Font.PLAIN, 22));
        il.setForeground(accent);
        iconCircle.add(il);

        JPanel iconWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        iconWrap.setOpaque(false); iconWrap.setAlignmentX(LEFT_ALIGNMENT);
        iconWrap.add(iconCircle);

        JLabel tl = new JLabel(title);
        tl.setFont(new Font("SansSerif", Font.BOLD, 13));
        tl.setForeground(new Color(15, 23, 42));
        tl.setAlignmentX(LEFT_ALIGNMENT);

        String htmlDesc = "<html>" + desc.replace("\n", "<br>") + "</html>";
        JLabel dl = new JLabel(htmlDesc);
        dl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        dl.setForeground(Theme.TEXT_GRAY);
        dl.setAlignmentX(LEFT_ALIGNMENT);

        // View button
        JButton viewBtn = new JButton("View Report") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0,0,accent.brighter(),0,getHeight(),accent);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        viewBtn.setForeground(Color.WHITE);
        viewBtn.setFont(new Font("SansSerif", Font.BOLD, 11));
        viewBtn.setContentAreaFilled(false); viewBtn.setBorderPainted(false); viewBtn.setFocusPainted(false);
        viewBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        viewBtn.setAlignmentX(LEFT_ALIGNMENT);
        viewBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        viewBtn.addActionListener(e -> showReport(type, title, false));

        // Download button
        JButton dlBtn = new JButton("\u2B07 Download CSV") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover()
                    ? new Color(accent.getRed(),accent.getGreen(),accent.getBlue(),15)
                    : new Color(accent.getRed(),accent.getGreen(),accent.getBlue(),8));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.setColor(accent);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 6, 6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        dlBtn.setForeground(accent);
        dlBtn.setFont(new Font("SansSerif", Font.BOLD, 11));
        dlBtn.setContentAreaFilled(false); dlBtn.setBorderPainted(false); dlBtn.setFocusPainted(false);
        dlBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        dlBtn.setAlignmentX(LEFT_ALIGNMENT);
        dlBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        dlBtn.addActionListener(e -> showReport(type, title, true));

        card.add(iconWrap);
        card.add(Box.createVerticalStrut(8));
        card.add(tl);
        card.add(Box.createVerticalStrut(3));
        card.add(dl);
        card.add(Box.createVerticalGlue());
        card.add(viewBtn);
        card.add(Box.createVerticalStrut(5));
        card.add(dlBtn);

        parent.add(card);
    }

    // ── MINI STAT ────────────────────────────────────────────────
    private void miniStat(JPanel p, String icon, String label, String value, Color color) {
        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0,0,0,6));
                g2.fillRoundRect(2,3,getWidth()-2,getHeight()-2,8,8);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0,0,getWidth()-2,getHeight()-2,8,8);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0,0,getWidth()-3,getHeight()-3,8,8);
                g2.dispose();
            }
        };
        card.setLayout(new BorderLayout(10, 0));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        JPanel iconCircle = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),20));
                g2.fillOval(0,0,getWidth(),getHeight());
                g2.dispose();
            }
        };
        iconCircle.setOpaque(false);
        iconCircle.setPreferredSize(new Dimension(34, 34));
        iconCircle.setLayout(new GridBagLayout());
        JLabel il = new JLabel(icon); il.setFont(new Font("SansSerif",Font.PLAIN,16));
        iconCircle.add(il);
        card.add(iconCircle, BorderLayout.WEST);

        JPanel info = new JPanel(new GridLayout(2,1,0,2)); info.setOpaque(false);
        JLabel ll = new JLabel(label); ll.setFont(new Font("SansSerif",Font.PLAIN,10)); ll.setForeground(Theme.TEXT_GRAY);
        JLabel vl = new JLabel(value); vl.setFont(new Font("SansSerif",Font.BOLD,15)); vl.setForeground(color);
        info.add(ll); info.add(vl);
        card.add(info, BorderLayout.CENTER);
        p.add(card);
    }


    // ── SHOW REPORT DIALOG ───────────────────────────────────────
    private void showReport(String type, String title, boolean download) {
        DefaultTableModel model = buildModel(type);
        if (download) { saveCSV(model, title); return; }

        JDialog dlg = new JDialog(mf, title, true);
        dlg.setSize(820, 500); dlg.setLocationRelativeTo(mf);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG_CONTENT);

        // Dialog header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0,Theme.BORDER),
            BorderFactory.createEmptyBorder(14,20,14,20)
        ));
        JLabel ht = new JLabel("\uD83D\uDCCA  " + title);
        ht.setFont(new Font("SansSerif",Font.BOLD,15));
        ht.setForeground(new Color(15,23,42));

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));
        btns.setOpaque(false);

        // Download CSV button
        JButton dlBtn = new JButton("\u2B07 Download CSV") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover()?new Color(220,252,231):new Color(240,253,244));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),6,6);
                g2.setColor(Theme.GREEN); g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,6,6);
                g2.dispose(); super.paintComponent(g);
            }
        };
        dlBtn.setForeground(new Color(21,128,61));
        dlBtn.setFont(new Font("SansSerif",Font.BOLD,12));
        dlBtn.setContentAreaFilled(false); dlBtn.setBorderPainted(false); dlBtn.setFocusPainted(false);
        dlBtn.setPreferredSize(new Dimension(145,34));
        dlBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        dlBtn.addActionListener(e -> saveCSV(model, title));

        JButton closeBtn = Theme.outlineButton("Close");
        closeBtn.setPreferredSize(new Dimension(80,34));

        btns.add(dlBtn); btns.add(closeBtn);
        header.add(ht,BorderLayout.WEST); header.add(btns,BorderLayout.EAST);
        root.add(header,BorderLayout.NORTH);

        // Table inside white card
        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER,1),
            BorderFactory.createEmptyBorder(0,0,0,0)
        ));
        JTable table = new JTable(model);
        Theme.styleTable(table);
        table.setRowHeight(38);

        for (int c=0; c<model.getColumnCount(); c++) {
            if ("Status".equals(model.getColumnName(c))) {
                final int col=c;
                table.getColumnModel().getColumn(col).setCellRenderer((t,v,sel,foc,r,cc)->{
                    JLabel l=Theme.statusLabel(v==null?"":v.toString());
                    l.setHorizontalAlignment(SwingConstants.CENTER);
                    return l;
                });
            }
        }
        tableCard.add(Theme.darkScroll(table),BorderLayout.CENTER);
        root.add(tableCard,BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.setBackground(Theme.BG_CONTENT);
        JLabel countLbl = new JLabel("  " + model.getRowCount() + " records found");
        countLbl.setFont(Theme.FONT_SMALL); countLbl.setForeground(Theme.TEXT_GRAY);
        footer.add(countLbl);
        root.add(footer,BorderLayout.SOUTH);

        dlg.setContentPane(root);
        closeBtn.addActionListener(e -> dlg.dispose());
        dlg.setVisible(true);
    }

    // ── BUILD TABLE DATA ─────────────────────────────────────────
    private DefaultTableModel buildModel(String type) {
        DefaultTableModel m;
        switch (type) {
            case "payments_report":
                m = new DefaultTableModel(new String[]{"Payment ID","Booking ID","Customer","Amount","Method","Date","Status"},0){public boolean isCellEditable(int r,int c){return false;}};
                for (Payment p : dm.getPayments())
                    m.addRow(new Object[]{p.getPaymentId(),p.getBookingId(),p.getCustomerName(),
                        (int)p.getAmount()+" PKR",p.getMethod(),p.getPaymentDate(),p.getStatus()});
                break;
            case "rooms_report":
                m = new DefaultTableModel(new String[]{"Room No","Type","Price/Night","Capacity","Features","Status"},0){public boolean isCellEditable(int r,int c){return false;}};
                for (Room r : dm.getRooms())
                    m.addRow(new Object[]{r.getRoomNo(),r.getRoomType(),(int)r.getPricePerNight()+" PKR",r.getCapacity()+" Guests",r.getFeatures(),r.getStatus()});
                break;
            case "customers_report":
                m = new DefaultTableModel(new String[]{"ID","Name","Phone","Email","Total Bookings"},0){public boolean isCellEditable(int r,int c){return false;}};
                for (Customer c : dm.getCustomers())
                    m.addRow(new Object[]{c.getCustomerId(),c.getName(),c.getPhone(),c.getEmail(),c.getTotalBookings()});
                break;
            default:
                m = new DefaultTableModel(new String[]{"Booking ID","Customer","Room","Amount","Method","Check-In","Check-Out","Status"},0){public boolean isCellEditable(int r,int c){return false;}};
                for (Booking b : dm.getBookings())
                    m.addRow(new Object[]{b.getBookingId(),b.getCustomerName(),"#"+b.getRoomNo()+" "+b.getRoomType(),
                        (int)b.getTotalAmount()+" PKR",b.getPaymentMethod(),b.getCheckIn(),b.getCheckOut(),b.getBookingStatus()});
                break;
        }
        return m;
    }

    // ── SAVE CSV ─────────────────────────────────────────────────
    private void saveCSV(DefaultTableModel model, String reportTitle) {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File(reportTitle.replace(" ","_")+".csv"));
        fc.setDialogTitle("Save Report as CSV");
        if (fc.showSaveDialog(mf) != JFileChooser.APPROVE_OPTION) return;
        File file = fc.getSelectedFile();
        if (!file.getName().endsWith(".csv")) file = new File(file.getAbsolutePath()+".csv");
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            StringBuilder hdr = new StringBuilder();
            for (int c=0; c<model.getColumnCount(); c++) {
                if (c>0) hdr.append(",");
                hdr.append("\"").append(model.getColumnName(c)).append("\"");
            }
            pw.println(hdr);
            for (int r=0; r<model.getRowCount(); r++) {
                StringBuilder row = new StringBuilder();
                for (int c=0; c<model.getColumnCount(); c++) {
                    if (c>0) row.append(",");
                    Object val = model.getValueAt(r,c);
                    row.append("\"").append(val==null?"":val.toString()).append("\"");
                }
                pw.println(row);
            }
            JOptionPane.showMessageDialog(mf,
                "✅ Report saved successfully!\n"+file.getAbsolutePath(),
                "Download Complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mf,"❌ Error saving: "+ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refresh() { removeAll(); buildUI(); revalidate(); repaint(); }
}
