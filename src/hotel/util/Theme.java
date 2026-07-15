package hotel.util;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.geom.*;

public class Theme {
    // Backgrounds
    public static final Color BG_MAIN       = new Color(245, 247, 250);
    public static final Color BG_TOPBAR     = new Color(255, 255, 255);
    public static final Color BG_SIDEBAR    = new Color(30,  41,  59);
    public static final Color BG_CONTENT    = new Color(245, 247, 250);
    public static final Color BG_CARD       = new Color(255, 255, 255);
    public static final Color BG_TABLE_ODD  = new Color(255, 255, 255);
    public static final Color BG_TABLE_EVEN = new Color(248, 250, 252);
    public static final Color BG_INPUT      = new Color(255, 255, 255);
    public static final Color BG_DIALOG     = new Color(245, 247, 250);

    public static final Color SIDEBAR_TEXT        = new Color(148, 163, 184);
    public static final Color SIDEBAR_TEXT_ACTIVE = new Color(255, 255, 255);
    public static final Color SIDEBAR_ACTIVE_BG   = new Color(59, 130, 246, 40);
    public static final Color SIDEBAR_ACCENT      = new Color(59, 130, 246);

    public static final Color PRIMARY       = new Color(59,  130, 246);
    public static final Color PRIMARY_DARK  = new Color(37,  99,  235);
    public static final Color PRIMARY_LIGHT = new Color(96,  165, 250);

    public static final Color GOLD          = new Color(245, 158,  11);
    public static final Color GOLD_HOVER    = new Color(217, 119,   6);
    public static final Color GOLD_DARK     = new Color(180,  90,   0);
    public static final Color GOLD_TEXT     = new Color(120,  53,  15);

    public static final Color TEXT_WHITE    = new Color( 15,  23,  42);
    public static final Color TEXT_GRAY     = new Color(100, 116, 139);
    public static final Color TEXT_DARK     = new Color( 51,  65,  85);
    public static final Color TEXT_LIGHT    = new Color(148, 163, 184);

    public static final Color BORDER        = new Color(226, 232, 240);
    public static final Color BORDER_LIGHT  = new Color(241, 245, 249);

    public static final Color GREEN         = new Color( 22, 163,  74);
    public static final Color GREEN_BG      = new Color(220, 252, 231);
    public static final Color RED           = new Color(220,  38,  38);
    public static final Color RED_BG        = new Color(254, 226, 226);
    public static final Color ORANGE        = new Color(234,  88,  12);

    public static final Color BLUE_STAT     = new Color( 59, 130, 246);
    public static final Color GREEN_STAT    = new Color( 22, 163,  74);
    public static final Color ORANGE_STAT   = new Color(234,  88,  12);
    public static final Color PURPLE_STAT   = new Color(147,  51, 234);

    public static final Font FONT_TITLE  = new Font("SansSerif", Font.BOLD,  16);
    public static final Font FONT_HEADER = new Font("SansSerif", Font.BOLD,  14);
    public static final Font FONT_BODY   = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONT_SMALL  = new Font("SansSerif", Font.PLAIN, 12);
    public static final Font FONT_BOLD   = new Font("SansSerif", Font.BOLD,  13);
    public static final Font FONT_TINY   = new Font("SansSerif", Font.PLAIN, 11);

    // ── GOLD BUTTON ──────────────────────────────────────────────
    public static JButton goldButton(String text) {
        JButton btn = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isPressed()  ? GOLD_DARK
                        : getModel().isRollover() ? GOLD_HOVER : GOLD;
                GradientPaint gp = new GradientPaint(0,0,c.brighter(),0,getHeight(),c);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(new Color(255,255,255,40));
                g2.fillRoundRect(0, 0, getWidth(), getHeight()/2, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 36));
        return btn;
    }

    // ── OUTLINE BUTTON ───────────────────────────────────────────
    public static JButton outlineButton(String text) {
        JButton btn = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(241,245,249) : BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(TEXT_DARK);
        btn.setFont(FONT_BODY);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 36));
        return btn;
    }

    // ── TEXT FIELD ───────────────────────────────────────────────
    public static JTextField styledField(String placeholder) {
        JTextField f = new JTextField();
        f.setBackground(BG_INPUT);
        f.setForeground(TEXT_WHITE);
        f.setCaretColor(PRIMARY);
        f.setFont(FONT_BODY);
        f.setBorder(BorderFactory.createCompoundBorder(
            new RoundBorder(BORDER, 1, 8),
            BorderFactory.createEmptyBorder(7, 12, 7, 12)
        ));
        f.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                f.setBorder(BorderFactory.createCompoundBorder(
                    new RoundBorder(PRIMARY, 2, 8),
                    BorderFactory.createEmptyBorder(6, 11, 6, 11)));
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                f.setBorder(BorderFactory.createCompoundBorder(
                    new RoundBorder(BORDER, 1, 8),
                    BorderFactory.createEmptyBorder(7, 12, 7, 12)));
            }
        });
        return f;
    }

    // ── PASSWORD FIELD ───────────────────────────────────────────
    public static JPasswordField styledPassword() {
        JPasswordField f = new JPasswordField();
        f.setBackground(BG_INPUT);
        f.setForeground(TEXT_WHITE);
        f.setCaretColor(PRIMARY);
        f.setFont(FONT_BODY);
        f.setBorder(BorderFactory.createCompoundBorder(
            new RoundBorder(BORDER, 1, 8),
            BorderFactory.createEmptyBorder(7, 12, 7, 12)
        ));
        f.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                f.setBorder(BorderFactory.createCompoundBorder(
                    new RoundBorder(PRIMARY, 2, 8),
                    BorderFactory.createEmptyBorder(6, 11, 6, 11)));
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                f.setBorder(BorderFactory.createCompoundBorder(
                    new RoundBorder(BORDER, 1, 8),
                    BorderFactory.createEmptyBorder(7, 12, 7, 12)));
            }
        });
        return f;
    }

    // ── COMBO BOX ────────────────────────────────────────────────
    public static JComboBox<String> styledCombo(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setBackground(BG_INPUT);
        cb.setForeground(TEXT_WHITE);
        cb.setFont(FONT_BODY);
        cb.setBorder(new RoundBorder(BORDER, 1, 8));
        cb.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> l,Object v,int i,boolean sel,boolean foc) {
                super.getListCellRendererComponent(l,v,i,sel,foc);
                setBackground(sel ? new Color(239,246,255) : BG_INPUT);
                setForeground(sel ? PRIMARY : TEXT_WHITE);
                setBorder(BorderFactory.createEmptyBorder(5,12,5,12));
                return this;
            }
        });
        return cb;
    }

    // ── TABLE ────────────────────────────────────────────────────
    public static void styleTable(JTable table) {
        table.setBackground(BG_CARD);
        table.setForeground(TEXT_WHITE);
        table.setFont(FONT_SMALL);
        table.setRowHeight(38);
        table.setGridColor(new Color(241,245,249));
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0,0));
        table.setSelectionBackground(new Color(239,246,255));
        table.setSelectionForeground(PRIMARY_DARK);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(248,250,252));
        header.setForeground(TEXT_GRAY);
        header.setFont(new Font("SansSerif",Font.BOLD,11));
        header.setPreferredSize(new Dimension(0,40));
        header.setBorder(BorderFactory.createMatteBorder(0,0,2,0,BORDER));
        header.setReorderingAllowed(false);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t,Object v,boolean sel,boolean foc,int r,int c) {
                super.getTableCellRendererComponent(t,v,sel,foc,r,c);
                if (sel) { setBackground(new Color(239,246,255)); setForeground(PRIMARY_DARK); }
                else { setBackground(r%2==0 ? BG_TABLE_ODD : BG_TABLE_EVEN); setForeground(TEXT_WHITE); }
                setBorder(BorderFactory.createEmptyBorder(0,14,0,14));
                setFont(FONT_SMALL);
                return this;
            }
        });
    }

    // ── SCROLL PANE ──────────────────────────────────────────────
    public static JScrollPane darkScroll(Component c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBackground(BG_CARD);
        sp.getViewport().setBackground(BG_CARD);
        sp.setBorder(BorderFactory.createLineBorder(BORDER,1));
        sp.getVerticalScrollBar().setBackground(BG_MAIN);
        sp.getHorizontalScrollBar().setBackground(BG_MAIN);
        return sp;
    }

    // ── STATUS BADGE ─────────────────────────────────────────────
    public static JLabel statusLabel(String status) {
        Color fg, bg;
        switch (status == null ? "" : status) {
            case "Available": case "Confirmed": case "Paid":
                fg=new Color(21,128,61); bg=new Color(220,252,231); break;
            case "Booked": case "Cancelled": case "Failed":
                fg=new Color(185,28,28); bg=new Color(254,226,226); break;
            default:
                fg=new Color(154,52,18); bg=new Color(255,237,213); break;
        }
        final Color ff=fg, fb=bg;
        JLabel l = new JLabel(status, SwingConstants.CENTER) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(fb);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),12,12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        l.setForeground(ff);
        l.setFont(new Font("SansSerif",Font.BOLD,11));
        l.setOpaque(false);
        l.setPreferredSize(new Dimension(82,24));
        return l;
    }

    public static Color getStatusColor(String s) {
        if (s==null) return RED;
        switch(s) {
            case "Available": case "Confirmed": case "Paid": return GREEN;
            case "Booked": case "Cancelled": case "Failed": return RED;
            default: return ORANGE;
        }
    }

    // ── SIDEBAR BUTTON ───────────────────────────────────────────
    public static JButton sidebarButton(String icon, String text, boolean active) {
        JButton btn = new JButton() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                boolean isActive = Boolean.TRUE.equals(getClientProperty("active"));
                if (isActive) {
                    g2.setColor(new Color(59,130,246,35));
                    g2.fillRoundRect(4,2,getWidth()-8,getHeight()-4,8,8);
                    g2.setColor(SIDEBAR_ACCENT);
                    g2.setStroke(new BasicStroke(3,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
                    g2.drawLine(2,8,2,getHeight()-8);
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(255,255,255,10));
                    g2.fillRoundRect(4,2,getWidth()-8,getHeight()-4,8,8);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.putClientProperty("active", active);
        btn.setText(icon + "   " + text);
        btn.setFont(new Font("SansSerif", active ? Font.BOLD : Font.PLAIN, 13));
        btn.setForeground(active ? Color.WHITE : SIDEBAR_TEXT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setBorder(BorderFactory.createEmptyBorder(11,18,11,10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ── CARD PANEL ───────────────────────────────────────────────
    public static JPanel cardPanel() {
        JPanel p = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0,0,0,8));
                g2.fillRoundRect(2,3,getWidth()-2,getHeight()-2,12,12);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0,0,getWidth()-2,getHeight()-2,12,12);
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0,0,getWidth()-3,getHeight()-3,12,12);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        return p;
    }

    public static JLabel titleLabel(String text) {
        JLabel l=new JLabel(text); l.setFont(FONT_TITLE); l.setForeground(TEXT_WHITE); return l;
    }
    public static JLabel grayLabel(String text) {
        JLabel l=new JLabel(text); l.setFont(FONT_SMALL); l.setForeground(TEXT_GRAY); return l;
    }

    // ── ROUND BORDER ─────────────────────────────────────────────
    public static class RoundBorder extends AbstractBorder {
        private Color c; private int t,r;
        public RoundBorder(Color c,int t,int r){this.c=c;this.t=t;this.r=r;}
        public void paintBorder(Component comp,Graphics g,int x,int y,int w,int h){
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c); g2.setStroke(new BasicStroke(t));
            g2.drawRoundRect(x,y,w-1,h-1,r,r); g2.dispose();
        }
        public Insets getBorderInsets(Component c){return new Insets(t,t,t,t);}
        public Insets getBorderInsets(Component c,Insets i){i.set(t,t,t,t);return i;}
    }
}
