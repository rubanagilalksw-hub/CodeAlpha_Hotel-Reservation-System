package hotel.ui;
import hotel.dao.DataManager;
import hotel.model.Room;
import hotel.util.Theme;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.stream.Collectors;

public class SearchRoomsDialog extends JDialog {
    private MainFrame mf;
    private DataManager dm=DataManager.getInstance();
    private JComboBox<String> typeCombo;
    private JTextField checkInField,checkOutField,guestsField;
    private DefaultTableModel resultsModel;
    private JTable resultsTable;
    private String presetCustomer;

    public SearchRoomsDialog(MainFrame mf,String presetCustomer){
        super(mf,"Search & Book Rooms",true);
        this.mf=mf; this.presetCustomer=presetCustomer;
        setSize(900,560); setLocationRelativeTo(mf);
        buildUI(); doSearch();
    }

    private void buildUI(){
        JPanel root=new JPanel(new BorderLayout());
        root.setBackground(Theme.BG_CONTENT);
        root.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        setContentPane(root);

        // Header
        JPanel header=new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0,Theme.BORDER),
            BorderFactory.createEmptyBorder(14,20,14,20)
        ));
        JLabel ht=new JLabel("\uD83D\uDD0D  Search Available Rooms");
        ht.setFont(new Font("SansSerif",Font.BOLD,16)); ht.setForeground(new Color(15,23,42));
        header.add(ht,BorderLayout.WEST);
        JButton closeBtn=Theme.outlineButton("✕ Close");
        closeBtn.setPreferredSize(new Dimension(90,34));
        closeBtn.addActionListener(e->dispose());
        header.add(closeBtn,BorderLayout.EAST);
        root.add(header,BorderLayout.NORTH);

        JPanel body=new JPanel();
        body.setBackground(Theme.BG_CONTENT);
        body.setLayout(new BoxLayout(body,BoxLayout.Y_AXIS));
        body.setBorder(BorderFactory.createEmptyBorder(16,20,16,20));

        // Filter card
        JPanel filterCard=new JPanel(){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE); g2.fillRoundRect(0,0,getWidth()-2,getHeight()-2,10,10);
                g2.setColor(Theme.BORDER); g2.drawRoundRect(0,0,getWidth()-3,getHeight()-3,10,10);
                g2.dispose();
            }
        };
        filterCard.setLayout(new GridBagLayout()); filterCard.setOpaque(false);
        filterCard.setBorder(BorderFactory.createEmptyBorder(14,16,14,16));
        filterCard.setMaximumSize(new Dimension(Integer.MAX_VALUE,105));
        filterCard.setAlignmentX(LEFT_ALIGNMENT);

        GridBagConstraints gc=new GridBagConstraints();
        gc.insets=new Insets(3,6,3,6); gc.fill=GridBagConstraints.BOTH; gc.weighty=1;
        gc.gridy=0; gc.weightx=1;
        gc.gridx=0; filterCard.add(grayLbl("Room Type"),gc);
        gc.gridx=1; filterCard.add(grayLbl("Check-In Date"),gc);
        gc.gridx=2; filterCard.add(grayLbl("Check-out Date"),gc);
        gc.gridx=3; filterCard.add(grayLbl("Guests"),gc);
        gc.gridx=4; gc.weightx=0.5; filterCard.add(new JLabel(),gc);

        gc.gridy=1; gc.weightx=1;
        typeCombo=Theme.styledCombo(new String[]{"All Types","Standard","Deluxe","Suite"});
        gc.gridx=0; filterCard.add(typeCombo,gc);
        checkInField=Theme.styledField("dd-mm-yyyy"); gc.gridx=1; filterCard.add(checkInField,gc);
        checkOutField=Theme.styledField("dd-mm-yyyy"); gc.gridx=2; filterCard.add(checkOutField,gc);
        guestsField=Theme.styledField("1"); gc.gridx=3; filterCard.add(guestsField,gc);
        JButton searchBtn=Theme.goldButton("Search"); gc.gridx=4; gc.weightx=0.5; filterCard.add(searchBtn,gc);

        body.add(filterCard); body.add(Box.createVerticalStrut(16));

        JLabel availLbl=new JLabel("Available Rooms");
        availLbl.setFont(new Font("SansSerif",Font.BOLD,14)); availLbl.setForeground(new Color(15,23,42));
        availLbl.setAlignmentX(LEFT_ALIGNMENT);
        body.add(availLbl); body.add(Box.createVerticalStrut(8));

        String[] cols={"Room No.","Room Type","Price / Night","Capacity","Features","Action"};
        resultsModel=new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        resultsTable=new JTable(resultsModel);
        Theme.styleTable(resultsTable); resultsTable.setRowHeight(40);

        resultsTable.getColumnModel().getColumn(5).setCellRenderer((t,v,sel,foc,r,c)->{
            JButton btn=new JButton("Book Now"){
                protected void paintComponent(Graphics g){
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                    GradientPaint gp=new GradientPaint(0,0,Theme.GOLD,0,getHeight(),Theme.GOLD_HOVER);
                    g2.setPaint(gp); g2.fillRoundRect(0,0,getWidth(),getHeight(),6,6);
                    g2.dispose(); super.paintComponent(g);
                }
            };
            btn.setForeground(Color.WHITE); btn.setFont(new Font("SansSerif",Font.BOLD,12));
            btn.setBorderPainted(false); btn.setFocusPainted(false); btn.setContentAreaFilled(false);
            btn.setPreferredSize(new Dimension(90,28));
            return btn;
        });
        resultsTable.getColumnModel().getColumn(5).setPreferredWidth(100);

        resultsTable.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                int row=resultsTable.rowAtPoint(e.getPoint());
                int col=resultsTable.columnAtPoint(e.getPoint());
                if(col==5&&row>=0){
                    int roomNo=(int)resultsModel.getValueAt(row,0);
                    openBookDialog(roomNo);
                }
            }
        });

        JScrollPane sp=Theme.darkScroll(resultsTable);
        sp.setAlignmentX(LEFT_ALIGNMENT); sp.setPreferredSize(new Dimension(Integer.MAX_VALUE,280));
        body.add(sp);

        root.add(body,BorderLayout.CENTER);
        searchBtn.addActionListener(e->doSearch());
    }

    private JLabel grayLbl(String t){JLabel l=new JLabel(t);l.setFont(Theme.FONT_SMALL);l.setForeground(Theme.TEXT_GRAY);return l;}

    private void doSearch(){
        String type=(String)typeCombo.getSelectedItem();
        resultsModel.setRowCount(0);
        List<Room> avail=dm.getRooms().stream()
            .filter(r->"Available".equals(r.getStatus()))
            .filter(r->"All Types".equals(type)||r.getRoomType().equals(type))
            .collect(Collectors.toList());
        for(Room r:avail)
            resultsModel.addRow(new Object[]{r.getRoomNo(),r.getRoomType(),
                String.format("%,.0f PKR",r.getPricePerNight()),r.getCapacity()+" Guests",r.getFeatures(),"Book Now"});
    }

    private void openBookDialog(int roomNo){
        Room room=dm.findRoom(roomNo);
        if(room==null) return;
        BookRoomDialog dlg=new BookRoomDialog(mf,room,checkInField.getText(),checkOutField.getText(),presetCustomer);
        dlg.setVisible(true);
        if(dlg.isBooked()) doSearch();
    }
}
