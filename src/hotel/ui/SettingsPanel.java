package hotel.ui;

import hotel.dao.AppConfig;
import hotel.dao.DataManager;
import hotel.model.*;
import hotel.util.Theme;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SettingsPanel extends JPanel {
    private MainFrame mf;
    private DataManager dm = DataManager.getInstance();
    private AppConfig cfg  = AppConfig.getInstance();

    // Hotel Info fields
    private JTextField hotelNameField, hotelPhoneField, hotelEmailField, hotelAddressField;
    // Preferences
    private JComboBox<String> currencyCombo, langCombo;
    private JCheckBox autoBackupCb, notifyCb, soundCb;
    // Login credentials
    private JTextField usernameField;
    private JPasswordField oldPassField, newPassField, confirmPassField;

    public SettingsPanel(MainFrame mf) {
        this.mf = mf;
        setBackground(Theme.BG_CONTENT);
        setLayout(new BorderLayout());
        buildUI();
    }

    private void buildUI() {
        JPanel wrap = new JPanel(new BorderLayout(0, 0));
        wrap.setBackground(Theme.BG_CONTENT);
        wrap.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        // ── TITLE ─────────────────────────────────────────────────
        JLabel title = new JLabel("\u2699  Settings");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(Theme.TEXT_WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));
        wrap.add(title, BorderLayout.NORTH);

        // Two-column layout
        JPanel grid = new JPanel(new GridLayout(1, 2, 16, 0));
        grid.setOpaque(false);

        // ── LEFT COLUMN ───────────────────────────────────────────
        JPanel leftCol = new JPanel();
        leftCol.setLayout(new BoxLayout(leftCol, BoxLayout.Y_AXIS));
        leftCol.setOpaque(false);

        // ── Card 1: Hotel Information ────────────────────────────
        JPanel hotelCard = makeCard("\uD83C\uDFE8  Hotel Information");
        hotelNameField    = addField(hotelCard, "Hotel Name",    cfg.getHotelName());
        hotelPhoneField   = addField(hotelCard, "Phone Number",  cfg.getHotelPhone());
        hotelEmailField   = addField(hotelCard, "Email",         cfg.getHotelEmail());
        hotelAddressField = addField(hotelCard, "Address",       cfg.getHotelAddress());
        hotelCard.add(Box.createVerticalStrut(6));

        JButton saveHotel = Theme.goldButton("Save Changes");
        saveHotel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        saveHotel.setAlignmentX(LEFT_ALIGNMENT);
        saveHotel.addActionListener(e -> {
            cfg.set("hotel.name",    hotelNameField.getText().trim());
            cfg.set("hotel.phone",   hotelPhoneField.getText().trim());
            cfg.set("hotel.email",   hotelEmailField.getText().trim());
            cfg.set("hotel.address", hotelAddressField.getText().trim());
            cfg.save();
            JOptionPane.showMessageDialog(mf,
                "✅ Hotel information saved successfully!", "Saved",
                JOptionPane.INFORMATION_MESSAGE);
        });
        hotelCard.add(saveHotel);
        leftCol.add(hotelCard);
        leftCol.add(Box.createVerticalStrut(14));

        // ── Card 2: System Preferences ───────────────────────────
        JPanel prefCard = makeCard("\u2699  System Preferences");

        JLabel cl = Theme.grayLabel("Currency");
        cl.setAlignmentX(LEFT_ALIGNMENT); prefCard.add(cl);
        prefCard.add(Box.createVerticalStrut(4));
        currencyCombo = Theme.styledCombo(new String[]{
            "PKR - Pakistani Rupee","USD - US Dollar","EUR - Euro","GBP - British Pound"});
        currencyCombo.setSelectedItem(cfg.getCurrency());
        currencyCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        currencyCombo.setAlignmentX(LEFT_ALIGNMENT);
        prefCard.add(currencyCombo); prefCard.add(Box.createVerticalStrut(10));

        JLabel ll = Theme.grayLabel("Language");
        ll.setAlignmentX(LEFT_ALIGNMENT); prefCard.add(ll);
        prefCard.add(Box.createVerticalStrut(4));
        langCombo = Theme.styledCombo(new String[]{"English","Urdu","Arabic"});
        langCombo.setSelectedItem(cfg.getLanguage());
        langCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        langCombo.setAlignmentX(LEFT_ALIGNMENT);
        prefCard.add(langCombo); prefCard.add(Box.createVerticalStrut(12));

        autoBackupCb = makeCheckBox("Auto Backup Data Daily",   cfg.isAutoBackup());
        notifyCb     = makeCheckBox("Enable Booking Notifications", cfg.isNotify());
        soundCb      = makeCheckBox("Enable Sound Alerts",       cfg.isSound());
        prefCard.add(autoBackupCb); prefCard.add(Box.createVerticalStrut(4));
        prefCard.add(notifyCb);     prefCard.add(Box.createVerticalStrut(4));
        prefCard.add(soundCb);      prefCard.add(Box.createVerticalStrut(10));

        JButton savePref = Theme.goldButton("Save Preferences");
        savePref.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        savePref.setAlignmentX(LEFT_ALIGNMENT);
        savePref.addActionListener(e -> {
            cfg.set("currency",    (String) currencyCombo.getSelectedItem());
            cfg.set("language",    (String) langCombo.getSelectedItem());
            cfg.set("auto.backup", autoBackupCb.isSelected() ? "true" : "false");
            cfg.set("notify",      notifyCb.isSelected()     ? "true" : "false");
            cfg.set("sound",       soundCb.isSelected()      ? "true" : "false");
            cfg.save();
            JOptionPane.showMessageDialog(mf,
                "✅ Preferences saved successfully!", "Saved",
                JOptionPane.INFORMATION_MESSAGE);
        });
        prefCard.add(savePref);
        leftCol.add(prefCard);

        grid.add(leftCol);

        // ── RIGHT COLUMN ──────────────────────────────────────────
        JPanel rightCol = new JPanel();
        rightCol.setLayout(new BoxLayout(rightCol, BoxLayout.Y_AXIS));
        rightCol.setOpaque(false);

        // ── Card 3: Change Login Credentials ─────────────────────
        JPanel passCard = makeCard("\uD83D\uDD12  Change Login Credentials");
        usernameField    = addField(passCard, "Username", cfg.getAdminUsername());
        oldPassField     = addPassField(passCard, "Current Password");
        newPassField     = addPassField(passCard, "New Password (leave blank to keep same)");
        confirmPassField = addPassField(passCard, "Confirm New Password");
        passCard.add(Box.createVerticalStrut(6));

        JButton changePass = Theme.goldButton("Update Credentials");
        changePass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        changePass.setAlignmentX(LEFT_ALIGNMENT);
        changePass.addActionListener(e -> doChangePassword());
        passCard.add(changePass);
        rightCol.add(passCard);
        rightCol.add(Box.createVerticalStrut(14));

        // ── Card 4: Data Management ───────────────────────────────
        JPanel dataCard = makeCard("\uD83D\uDDC2  Data Management");

        // Stats row
        JPanel statsRow = new JPanel(new GridLayout(1, 3, 8, 0));
        statsRow.setOpaque(false);
        statsRow.setAlignmentX(LEFT_ALIGNMENT);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        addMiniStat(statsRow, "Rooms",      String.valueOf(dm.getTotalRooms()),    Theme.BLUE_STAT);
        addMiniStat(statsRow, "Bookings",   String.valueOf(dm.getTotalBookings()), Theme.GREEN_STAT);
        addMiniStat(statsRow, "Customers",  String.valueOf(dm.getCustomers().size()), Theme.GOLD);
        dataCard.add(statsRow);
        dataCard.add(Box.createVerticalStrut(12));

        // Backup row
        JLabel backLbl = Theme.grayLabel("Backup & Restore");
        backLbl.setAlignmentX(LEFT_ALIGNMENT); dataCard.add(backLbl);
        dataCard.add(Box.createVerticalStrut(6));

        JPanel backRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        backRow.setOpaque(false); backRow.setAlignmentX(LEFT_ALIGNMENT);
        JButton backupBtn = Theme.goldButton("\uD83D\uDCBE  Backup Data");
        backupBtn.setPreferredSize(new Dimension(130, 33));
        backupBtn.addActionListener(e -> doBackup());
        JButton restoreBtn = Theme.outlineButton("\uD83D\uDD04  Restore");
        restoreBtn.setPreferredSize(new Dimension(100, 33));
        restoreBtn.addActionListener(e -> doRestore());
        backRow.add(backupBtn); backRow.add(restoreBtn);
        dataCard.add(backRow);
        dataCard.add(Box.createVerticalStrut(12));

        JSeparator sep = new JSeparator();
        sep.setForeground(Theme.BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(LEFT_ALIGNMENT);
        dataCard.add(sep);
        dataCard.add(Box.createVerticalStrut(10));

        // Clear data
        JLabel clearLbl = Theme.grayLabel("Danger Zone");
        clearLbl.setForeground(Theme.RED);
        clearLbl.setAlignmentX(LEFT_ALIGNMENT); dataCard.add(clearLbl);
        dataCard.add(Box.createVerticalStrut(4));
        JLabel warnLbl = new JLabel("<html><font color='#888888' size='2'>Warning: This action is permanent and cannot be undone.</font></html>");
        warnLbl.setAlignmentX(LEFT_ALIGNMENT); dataCard.add(warnLbl);
        dataCard.add(Box.createVerticalStrut(8));

        JButton clearBtn = makeRedButton("\uD83D\uDDD1  Clear All Bookings & Payments");
        clearBtn.addActionListener(e -> doClearData());
        dataCard.add(clearBtn);
        rightCol.add(dataCard);
        rightCol.add(Box.createVerticalStrut(14));

        // ── Card 5: About ─────────────────────────────────────────
        JPanel aboutCard = makeCard("\u2139  About System");
        addInfoRow(aboutCard, "System :",     "Hotel Reservation System");
        addInfoRow(aboutCard, "Version :",    "v1.0.0");
        addInfoRow(aboutCard, "Platform :",   "Java Swing (OOP)");
        addInfoRow(aboutCard, "Storage :",    "File I/O (hotel_data/)");
        addInfoRow(aboutCard, "Login :",      "Username: " + cfg.getAdminUsername());
        rightCol.add(aboutCard);

        grid.add(rightCol);
        wrap.add(grid, BorderLayout.CENTER);

        JScrollPane sp = new JScrollPane(wrap);
        sp.setBorder(null);
        sp.setBackground(Theme.BG_CONTENT);
        sp.getViewport().setBackground(Theme.BG_CONTENT);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        add(sp, BorderLayout.CENTER);
    }

    // ── ACTIONS ───────────────────────────────────────────────────

    private void doChangePassword() {
        String newUser = usernameField.getText().trim();
        String oldP = new String(oldPassField.getPassword()).trim();
        String newP = new String(newPassField.getPassword()).trim();
        String cnfP = new String(confirmPassField.getPassword()).trim();

        if (!oldP.equals(cfg.getAdminPassword())) {
            JOptionPane.showMessageDialog(mf,
                "❌ Current password is incorrect!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (newUser.isEmpty()) {
            JOptionPane.showMessageDialog(mf,
                "❌ Username cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean changingPassword = !newP.isEmpty() || !cnfP.isEmpty();
        if (changingPassword) {
            if (newP.length() < 4) {
                JOptionPane.showMessageDialog(mf,
                    "❌ Password must be at least 4 characters!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!newP.equals(cnfP)) {
                JOptionPane.showMessageDialog(mf,
                    "❌ New passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        // Save new username persistently
        cfg.setAdminUsername(newUser);
        // Save new password persistently, only if the user chose to change it
        if (changingPassword) cfg.setAdminPassword(newP);

        JOptionPane.showMessageDialog(mf,
            "✅ Login credentials updated successfully!\nUsername: " + newUser +
            (changingPassword ? "\nPassword changed." : "\nPassword unchanged."),
            "Success", JOptionPane.INFORMATION_MESSAGE);
        oldPassField.setText("");
        newPassField.setText("");
        confirmPassField.setText("");
    }

    private void doBackup() {
        File src = new File("hotel_data");
        if (!src.exists() || src.listFiles() == null || src.listFiles().length == 0) {
            JOptionPane.showMessageDialog(mf, "No data found to backup.", "Info",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            String ts  = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());
            File dst   = new File("hotel_backup_" + ts);
            dst.mkdirs();
            for (File f : src.listFiles()) {
                Files.copy(f.toPath(), new File(dst, f.getName()).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
            }
            JOptionPane.showMessageDialog(mf,
                "✅ Backup created!\nFolder: " + dst.getAbsolutePath(),
                "Backup Complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mf,
                "❌ Backup error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doRestore() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setDialogTitle("Select Backup Folder to Restore");
        int result = fc.showOpenDialog(mf);
        if (result != JFileChooser.APPROVE_OPTION) return;
        File backupDir = fc.getSelectedFile();
        File[] files = backupDir.listFiles();
        if (files == null || files.length == 0) {
            JOptionPane.showMessageDialog(mf, "Selected folder is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int r = JOptionPane.showConfirmDialog(mf,
            "Restore from: " + backupDir.getName() + "?\nThis will overwrite current data!",
            "Confirm Restore", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (r != JOptionPane.YES_OPTION) return;
        try {
            File dst = new File("hotel_data");
            dst.mkdirs();
            for (File f : files) {
                Files.copy(f.toPath(), new File(dst, f.getName()).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
            }
            JOptionPane.showMessageDialog(mf,
                "✅ Restore complete! Please restart the application.",
                "Restored", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mf,
                "❌ Restore error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doClearData() {
        int r = JOptionPane.showConfirmDialog(mf,
            "⚠ This will permanently delete ALL bookings and payments!\n" +
            "Rooms and Customers will be kept.\n\nAre you sure?",
            "Confirm Clear", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (r != JOptionPane.YES_OPTION) return;

        // Actually clear bookings and payments from DataManager
        dm.getBookings().clear();
        dm.getPayments().clear();
        // Reset all rooms to Available
        for (Room room : dm.getRooms()) room.setStatus("Available");
        // Reset customer booking counts
        for (hotel.model.Customer c : dm.getCustomers()) c.setTotalBookings(0);

        // Force save via reflection trick - call through DataManager
        try {
            // Delete the files directly
            new File("hotel_data/bookings.txt").delete();
            new File("hotel_data/payments.txt").delete();
            // Re-save rooms and customers
            PrintWriter pw1 = new PrintWriter(new FileWriter("hotel_data/rooms.txt"));
            for (Room room : dm.getRooms()) pw1.println(room); pw1.close();
            PrintWriter pw2 = new PrintWriter(new FileWriter("hotel_data/customers.txt"));
            for (hotel.model.Customer c : dm.getCustomers()) pw2.println(c); pw2.close();
        } catch (Exception ex) { ex.printStackTrace(); }

        JOptionPane.showMessageDialog(mf,
            "✅ All bookings and payments cleared!\nRooms reset to Available.",
            "Cleared", JOptionPane.INFORMATION_MESSAGE);
        mf.navigateTo("dashboard");
    }

    // ── HELPERS ───────────────────────────────────────────────────

    private JPanel makeCard(String title) {
        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(Theme.BORDER);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        card.setAlignmentX(LEFT_ALIGNMENT);

        JLabel t = new JLabel(title);
        t.setFont(new Font("SansSerif", Font.BOLD, 13));
        t.setForeground(Theme.GOLD);
        t.setAlignmentX(LEFT_ALIGNMENT);
        card.add(t);
        card.add(Box.createVerticalStrut(8));
        JSeparator sep = new JSeparator();
        sep.setForeground(Theme.BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(LEFT_ALIGNMENT);
        card.add(sep);
        card.add(Box.createVerticalStrut(10));
        return card;
    }

    private JTextField addField(JPanel card, String label, String val) {
        JLabel l = Theme.grayLabel(label);
        l.setAlignmentX(LEFT_ALIGNMENT); card.add(l);
        card.add(Box.createVerticalStrut(4));
        JTextField f = Theme.styledField(label);
        f.setText(val);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        f.setAlignmentX(LEFT_ALIGNMENT);
        card.add(f); card.add(Box.createVerticalStrut(8));
        return f;
    }

    private JPasswordField addPassField(JPanel card, String label) {
        JLabel l = Theme.grayLabel(label);
        l.setAlignmentX(LEFT_ALIGNMENT); card.add(l);
        card.add(Box.createVerticalStrut(4));
        JPasswordField f = Theme.styledPassword();
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        f.setAlignmentX(LEFT_ALIGNMENT);
        card.add(f); card.add(Box.createVerticalStrut(8));
        return f;
    }

    private JCheckBox makeCheckBox(String label, boolean selected) {
        JCheckBox cb = new JCheckBox(label);
        cb.setSelected(selected);
        cb.setFont(Theme.FONT_SMALL); cb.setForeground(Theme.TEXT_GRAY);
        cb.setOpaque(false); cb.setFocusPainted(false);
        cb.setAlignmentX(LEFT_ALIGNMENT);
        return cb;
    }

    private void addInfoRow(JPanel card, String label, String value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        row.setOpaque(false); row.setAlignmentX(LEFT_ALIGNMENT);
        JLabel l = Theme.grayLabel(label);
        l.setPreferredSize(new Dimension(80, 18));
        JLabel v = new JLabel(value); v.setFont(Theme.FONT_SMALL); v.setForeground(Theme.TEXT_WHITE);
        row.add(l); row.add(v); card.add(row);
    }

    private void addMiniStat(JPanel p, String label, String value, Color color) {
        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.BG_CONTENT);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),6,6);
                g2.setColor(color); g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,6,6);
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        JLabel vl = new JLabel(value, SwingConstants.CENTER);
        vl.setFont(new Font("SansSerif",Font.BOLD,18)); vl.setForeground(color);
        vl.setAlignmentX(CENTER_ALIGNMENT);
        JLabel ll = new JLabel(label, SwingConstants.CENTER);
        ll.setFont(Theme.FONT_TINY); ll.setForeground(Theme.TEXT_GRAY);
        ll.setAlignmentX(CENTER_ALIGNMENT);
        card.add(vl); card.add(ll);
        p.add(card);
    }

    private JButton makeRedButton(String text) {
        JButton btn = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(180,30,40) : Theme.RED);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),6,6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif",Font.BOLD,12));
        btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false); btn.setOpaque(false);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public void refresh() {
        // Reload config values into fields when panel is shown
        if (hotelNameField != null) {
            hotelNameField.setText(cfg.getHotelName());
            hotelPhoneField.setText(cfg.getHotelPhone());
            hotelEmailField.setText(cfg.getHotelEmail());
            hotelAddressField.setText(cfg.getHotelAddress());
            currencyCombo.setSelectedItem(cfg.getCurrency());
            langCombo.setSelectedItem(cfg.getLanguage());
            autoBackupCb.setSelected(cfg.isAutoBackup());
            notifyCb.setSelected(cfg.isNotify());
            soundCb.setSelected(cfg.isSound());
        }
        if (usernameField != null) {
            usernameField.setText(cfg.getAdminUsername());
        }
    }
}
