package hotel.ui;

import hotel.dao.DataManager;
import hotel.model.*;
import hotel.util.Theme;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentsPanel extends JPanel {
    private MainFrame mf;
    private DataManager dm=DataManager.getInstance();
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;

    public PaymentsPanel(MainFrame mf){
        this.mf=mf;
        setBackground(Theme.BG_CONTENT);
        setLayout(new BorderLayout());
        buildUI();
    }

    private void buildUI(){
        JPanel content=new JPanel(new BorderLayout());
        content.setBackground(Theme.BG_CONTENT);
        content.setBorder(BorderFactory.createEmptyBorder(22,22,22,22));

        // Title row + revenue card
        JPanel titleRow=new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        titleRow.setBorder(BorderFactory.createEmptyBorder(0,0,14,0));

        JPanel titleText=new JPanel();
        titleText.setLayout(new BoxLayout(titleText,BoxLayout.Y_AXIS));
        titleText.setOpaque(false);
        JLabel pt=new JLabel("Payment Records");
        pt.setFont(new Font("SansSerif",Font.BOLD,20)); pt.setForeground(new Color(15,23,42));
        JLabel ps=new JLabel("Track all payment transactions");
        ps.setFont(Theme.FONT_SMALL); ps.setForeground(Theme.TEXT_GRAY);
        titleText.add(pt); titleText.add(ps);
        titleRow.add(titleText,BorderLayout.WEST);

        // Revenue summary card
        JPanel revCard=new JPanel(){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp=new GradientPaint(0,0,new Color(245,158,11),getWidth(),0,new Color(234,88,12));
                g2.setPaint(gp); g2.fillRoundRect(0,0,getWidth(),getHeight(),12,12);
                g2.dispose();
            }
        };
        revCard.setLayout(new BoxLayout(revCard,BoxLayout.Y_AXIS));
        revCard.setOpaque(false);
        revCard.setBorder(BorderFactory.createEmptyBorder(12,20,12,20));
        revCard.setPreferredSize(new Dimension(220,64));
        JLabel rl=new JLabel("Total Revenue");
        rl.setFont(new Font("SansSerif",Font.PLAIN,11)); rl.setForeground(new Color(255,255,255,180));
        rl.setAlignmentX(LEFT_ALIGNMENT);
        JLabel rv=new JLabel(String.format("%,.0f PKR",dm.getTotalRevenue()));
        rv.setFont(new Font("SansSerif",Font.BOLD,20)); rv.setForeground(Color.WHITE);
        rv.setAlignmentX(LEFT_ALIGNMENT);
        revCard.add(rl); revCard.add(rv);
        titleRow.add(revCard,BorderLayout.EAST);
        content.add(titleRow,BorderLayout.NORTH);

        JPanel card=new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Theme.BORDER,1));

        JPanel top=new JPanel(new BorderLayout(10,0));
        top.setBackground(new Color(248,250,252));
        top.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0,Theme.BORDER),
            BorderFactory.createEmptyBorder(12,16,12,16)
        ));
        searchField=Theme.styledField("Search by payment ID, booking ID or customer...");
        searchField.setPreferredSize(new Dimension(340,38));
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
            public void insertUpdate(javax.swing.event.DocumentEvent e){filter();}
            public void removeUpdate(javax.swing.event.DocumentEvent e){filter();}
            public void changedUpdate(javax.swing.event.DocumentEvent e){filter();}
        });
        top.add(searchField,BorderLayout.WEST);
        card.add(top,BorderLayout.NORTH);

        String[] cols={"Payment ID","Booking ID","Customer","Amount","Method","Payment Date","Status","Action"};
        tableModel=new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        table=new JTable(tableModel);
        Theme.styleTable(table);
        table.setRowHeight(42);

        table.getColumnModel().getColumn(6).setCellRenderer((t,v,sel,foc,r,c)->{
            JLabel l=Theme.statusLabel(v==null?"":v.toString());
            l.setHorizontalAlignment(SwingConstants.CENTER);
            return l;
        });
        table.getColumnModel().getColumn(6).setPreferredWidth(90);

        table.getColumnModel().getColumn(7).setCellRenderer((t,v,sel,foc,r,c)->{
            JPanel p=new JPanel(new FlowLayout(FlowLayout.CENTER,0,4));
            p.setBackground(r%2==0?Theme.BG_TABLE_ODD:Theme.BG_TABLE_EVEN);
            JButton vBtn=new JButton("View Details"){
                protected void paintComponent(Graphics g){
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(239,246,255)); g2.fillRoundRect(0,0,getWidth(),getHeight(),6,6);
                    g2.dispose(); super.paintComponent(g);
                }
            };
            vBtn.setForeground(Theme.PRIMARY); vBtn.setFont(new Font("SansSerif",Font.BOLD,11));
            vBtn.setBorderPainted(false); vBtn.setFocusPainted(false); vBtn.setContentAreaFilled(false);
            vBtn.setPreferredSize(new Dimension(88,26));
            p.add(vBtn); return p;
        });
        table.getColumnModel().getColumn(7).setPreferredWidth(100);
        table.getColumnModel().getColumn(7).setMaxWidth(110);

        table.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                int row=table.rowAtPoint(e.getPoint());
                int col=table.columnAtPoint(e.getPoint());
                if(col==7&&row>=0){
                    String bid=(String)tableModel.getValueAt(row,1);
                    Booking b=dm.findBooking(bid);
                    if(b!=null) new BookingDetailsDialog(mf,b).setVisible(true);
                }
            }
        });

        card.add(Theme.darkScroll(table),BorderLayout.CENTER);
        JLabel fl=new JLabel("  Showing "+dm.getPayments().size()+" payment records");
        fl.setFont(Theme.FONT_SMALL); fl.setForeground(Theme.TEXT_GRAY);
        fl.setBorder(BorderFactory.createEmptyBorder(8,0,8,0));
        card.add(fl,BorderLayout.SOUTH);

        content.add(card,BorderLayout.CENTER);
        add(content,BorderLayout.CENTER);
        loadTable(dm.getPayments());
    }

    private void loadTable(List<Payment> list){
        tableModel.setRowCount(0);
        for(Payment p:list)
            tableModel.addRow(new Object[]{p.getPaymentId(),p.getBookingId(),p.getCustomerName(),
                (int)p.getAmount()+" PKR",p.getMethod(),p.getPaymentDate(),p.getStatus(),"view"});
    }

    private void filter(){
        String q=searchField.getText().trim().toLowerCase();
        loadTable(q.isEmpty()?dm.getPayments():dm.getPayments().stream()
            .filter(p->p.getPaymentId().toLowerCase().contains(q)||p.getBookingId().toLowerCase().contains(q)||p.getCustomerName().toLowerCase().contains(q))
            .collect(Collectors.toList()));
    }
    public void refresh(){loadTable(dm.getPayments());}
}
