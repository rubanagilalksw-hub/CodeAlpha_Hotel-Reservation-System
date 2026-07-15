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

public class ReservationsPanel extends JPanel {
    private MainFrame mf;
    private DataManager dm=DataManager.getInstance();
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;
    private JComboBox<String> statusFilter;

    public ReservationsPanel(MainFrame mf){
        this.mf=mf;
        setBackground(Theme.BG_CONTENT);
        setLayout(new BorderLayout());
        buildUI();
    }

    private void buildUI(){
        JPanel content=new JPanel(new BorderLayout());
        content.setBackground(Theme.BG_CONTENT);
        content.setBorder(BorderFactory.createEmptyBorder(22,22,22,22));

        // Page title
        JPanel titleRow=new JPanel();
        titleRow.setLayout(new BoxLayout(titleRow,BoxLayout.Y_AXIS));
        titleRow.setOpaque(false);
        titleRow.setBorder(BorderFactory.createEmptyBorder(0,0,14,0));
        JLabel pageTitle=new JLabel("Reservations");
        pageTitle.setFont(new Font("SansSerif",Font.BOLD,20));
        pageTitle.setForeground(new Color(15,23,42));
        JLabel pageSub=new JLabel("View and manage all hotel reservations");
        pageSub.setFont(Theme.FONT_SMALL); pageSub.setForeground(Theme.TEXT_GRAY);
        titleRow.add(pageTitle); titleRow.add(pageSub);
        content.add(titleRow,BorderLayout.NORTH);

        // White card
        JPanel card=new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Theme.BORDER,1));

        // Top bar
        JPanel top=new JPanel(new BorderLayout(10,0));
        top.setBackground(new Color(248,250,252));
        top.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0,Theme.BORDER),
            BorderFactory.createEmptyBorder(12,16,12,16)
        ));

        searchField=Theme.styledField("Search by booking ID or customer name...");
        searchField.setPreferredSize(new Dimension(310,38));
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
            public void insertUpdate(javax.swing.event.DocumentEvent e){filter();}
            public void removeUpdate(javax.swing.event.DocumentEvent e){filter();}
            public void changedUpdate(javax.swing.event.DocumentEvent e){filter();}
        });
        top.add(searchField,BorderLayout.WEST);

        JPanel rightTop=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));
        rightTop.setOpaque(false);
        statusFilter=Theme.styledCombo(new String[]{"All Status","Confirmed","Cancelled"});
        statusFilter.setPreferredSize(new Dimension(150,38));
        statusFilter.addActionListener(e->filter());
        rightTop.add(statusFilter);
        JButton bookBtn=Theme.goldButton("+ Book Room");
        bookBtn.setPreferredSize(new Dimension(120,38));
        bookBtn.addActionListener(e->{new SearchRoomsDialog(mf,null).setVisible(true);refresh();});
        rightTop.add(bookBtn);
        top.add(rightTop,BorderLayout.EAST);
        card.add(top,BorderLayout.NORTH);

        // Table
        String[] cols={"Booking ID","Customer","Room No","Room Type","Check-in","Check-out","Amount","Status","Action"};
        tableModel=new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        table=new JTable(tableModel);
        Theme.styleTable(table);
        table.setRowHeight(42);

        table.getColumnModel().getColumn(7).setCellRenderer((t,v,sel,foc,r,c)->{
            JLabel l=Theme.statusLabel(v==null?"":v.toString());
            l.setHorizontalAlignment(SwingConstants.CENTER);
            return l;
        });
        table.getColumnModel().getColumn(7).setPreferredWidth(100);

        table.getColumnModel().getColumn(8).setCellRenderer((t,v,sel,foc,r,c)->{
            JPanel p=new JPanel(new FlowLayout(FlowLayout.CENTER,5,4));
            p.setBackground(r%2==0?Theme.BG_TABLE_ODD:Theme.BG_TABLE_EVEN);
            JButton vBtn=styledTblBtn("View",new Color(239,246,255),Theme.PRIMARY);
            JButton cBtn=styledTblBtn("Cancel",new Color(254,226,226),Theme.RED);
            p.add(vBtn); p.add(cBtn);
            return p;
        });
        table.getColumnModel().getColumn(8).setPreferredWidth(130);
        table.getColumnModel().getColumn(8).setMaxWidth(140);

        table.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                int row=table.rowAtPoint(e.getPoint());
                int col=table.columnAtPoint(e.getPoint());
                if(col==8&&row>=0){
                    Rectangle cell=table.getCellRect(row,col,false);
                    int x=e.getX()-cell.x;
                    String bid=(String)tableModel.getValueAt(row,0);
                    if(x<68) viewBooking(bid);
                    else     cancelBooking(bid);
                }
            }
        });

        card.add(Theme.darkScroll(table),BorderLayout.CENTER);

        JLabel footLbl=new JLabel("  Showing "+dm.getTotalBookings()+" entries");
        footLbl.setFont(Theme.FONT_SMALL); footLbl.setForeground(Theme.TEXT_GRAY);
        footLbl.setBorder(BorderFactory.createEmptyBorder(8,0,8,0));
        card.add(footLbl,BorderLayout.SOUTH);

        content.add(card,BorderLayout.CENTER);
        add(content,BorderLayout.CENTER);
        loadTable(dm.getBookings());
    }

    private JButton styledTblBtn(String text,Color bg,Color fg){
        JButton btn=new JButton(text){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg); g2.fillRoundRect(0,0,getWidth(),getHeight(),6,6);
                g2.dispose(); super.paintComponent(g);
            }
        };
        btn.setForeground(fg); btn.setFont(new Font("SansSerif",Font.BOLD,11));
        btn.setBorderPainted(false); btn.setFocusPainted(false); btn.setContentAreaFilled(false);
        btn.setPreferredSize(new Dimension(56,26));
        return btn;
    }

    private void loadTable(List<Booking> list){
        tableModel.setRowCount(0);
        for(Booking b:list){
            tableModel.addRow(new Object[]{b.getBookingId(),b.getCustomerName(),b.getRoomNo(),
                b.getRoomType(),b.getCheckIn(),b.getCheckOut(),(int)b.getTotalAmount()+" PKR",
                b.getBookingStatus(),"actions"});
        }
    }

    private void filter(){
        String q=searchField.getText().trim().toLowerCase();
        String s=(String)statusFilter.getSelectedItem();
        loadTable(dm.getBookings().stream()
            .filter(b->q.isEmpty()||b.getBookingId().toLowerCase().contains(q)||b.getCustomerName().toLowerCase().contains(q))
            .filter(b->"All Status".equals(s)||b.getBookingStatus().equals(s))
            .collect(Collectors.toList()));
    }

    private void viewBooking(String bid){
        Booking b=dm.findBooking(bid);
        if(b!=null) new BookingDetailsDialog(mf,b).setVisible(true);
    }

    private void cancelBooking(String bid){
        Booking b=dm.findBooking(bid);
        if(b==null) return;
        if("Cancelled".equals(b.getBookingStatus())){JOptionPane.showMessageDialog(this,"Already cancelled.");return;}
        int r=JOptionPane.showConfirmDialog(this,"Cancel booking "+bid+"?","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        if(r==JOptionPane.YES_OPTION){
            b.setBookingStatus("Cancelled");dm.updateBooking(b);
            Room room=dm.findRoom(b.getRoomNo());
            if(room!=null){room.setStatus("Available");dm.updateRoom(room);}
            refresh();
        }
    }
    public void refresh(){loadTable(dm.getBookings());}
}
