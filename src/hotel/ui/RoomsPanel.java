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

public class RoomsPanel extends JPanel {
    private MainFrame mf;
    private DataManager dm = DataManager.getInstance();
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;

    public RoomsPanel(MainFrame mf) {
        this.mf = mf;
        setBackground(Theme.BG_CONTENT);
        setLayout(new BorderLayout());
        buildUI();
    }

    private void buildUI() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Theme.BG_CONTENT);
        content.setBorder(BorderFactory.createEmptyBorder(22,22,22,22));

        // Page title
        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        titleRow.setBorder(BorderFactory.createEmptyBorder(0,0,4,0));
        JLabel pageTitle = new JLabel("Room Management");
        pageTitle.setFont(new Font("SansSerif",Font.BOLD,20));
        pageTitle.setForeground(new Color(15,23,42));
        JLabel pageSub = new JLabel("Manage all hotel rooms, types, pricing and availability");
        pageSub.setFont(Theme.FONT_SMALL); pageSub.setForeground(Theme.TEXT_GRAY);
        JPanel titleText = new JPanel(); titleText.setOpaque(false);
        titleText.setLayout(new BoxLayout(titleText,BoxLayout.Y_AXIS));
        titleText.add(pageTitle); titleText.add(pageSub);
        titleRow.add(titleText,BorderLayout.WEST);
        content.add(titleRow,BorderLayout.NORTH);

        // White card container
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER,1),
            BorderFactory.createEmptyBorder(16,16,16,16)
        ));

        // Top bar inside card
        JPanel top = new JPanel(new BorderLayout(10,0));
        top.setOpaque(false);
        top.setBorder(BorderFactory.createEmptyBorder(0,0,14,0));

        JPanel leftTop = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
        leftTop.setOpaque(false);
        JLabel searchIcon = new JLabel("  \uD83D\uDD0D  ");
        searchIcon.setForeground(Theme.TEXT_GRAY);
        searchField = Theme.styledField("Search rooms by number or type...");
        searchField.setPreferredSize(new Dimension(300,38));
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
            public void insertUpdate(javax.swing.event.DocumentEvent e){filter();}
            public void removeUpdate(javax.swing.event.DocumentEvent e){filter();}
            public void changedUpdate(javax.swing.event.DocumentEvent e){filter();}
        });
        leftTop.add(searchField);
        top.add(leftTop,BorderLayout.WEST);

        JButton addBtn = Theme.goldButton("+ Add Room");
        addBtn.setPreferredSize(new Dimension(120,38));
        addBtn.addActionListener(e->showRoomDialog(null));
        top.add(addBtn,BorderLayout.EAST);
        card.add(top,BorderLayout.NORTH);

        // Table
        String[] cols = {"Room No.","Room Type","Price / Night","Capacity","Status","Action"};
        tableModel = new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        table = new JTable(tableModel);
        Theme.styleTable(table);
        table.setRowHeight(42);

        // Status column
        table.getColumnModel().getColumn(4).setCellRenderer((t,v,sel,foc,r,c)->{
            JLabel l=Theme.statusLabel(v==null?"":v.toString());
            l.setHorizontalAlignment(SwingConstants.CENTER);
            return l;
        });
        table.getColumnModel().getColumn(4).setPreferredWidth(100);

        // Action column
        table.getColumnModel().getColumn(5).setCellRenderer((t,v,sel,foc,r,c)->{
            JPanel p=new JPanel(new FlowLayout(FlowLayout.CENTER,6,4));
            p.setBackground(r%2==0?Theme.BG_TABLE_ODD:Theme.BG_TABLE_EVEN);

            JButton editBtn = new JButton("Edit"){
                protected void paintComponent(Graphics g){
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(239,246,255));
                    g2.fillRoundRect(0,0,getWidth(),getHeight(),6,6);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            editBtn.setForeground(Theme.PRIMARY); editBtn.setFont(new Font("SansSerif",Font.BOLD,11));
            editBtn.setBorderPainted(false); editBtn.setFocusPainted(false); editBtn.setContentAreaFilled(false);
            editBtn.setPreferredSize(new Dimension(52,26));

            JButton delBtn = new JButton("Delete"){
                protected void paintComponent(Graphics g){
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(254,226,226));
                    g2.fillRoundRect(0,0,getWidth(),getHeight(),6,6);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            delBtn.setForeground(Theme.RED); delBtn.setFont(new Font("SansSerif",Font.BOLD,11));
            delBtn.setBorderPainted(false); delBtn.setFocusPainted(false); delBtn.setContentAreaFilled(false);
            delBtn.setPreferredSize(new Dimension(56,26));

            p.add(editBtn); p.add(delBtn);
            return p;
        });
        table.getColumnModel().getColumn(5).setPreferredWidth(130);
        table.getColumnModel().getColumn(5).setMaxWidth(140);

        table.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                int row=table.rowAtPoint(e.getPoint());
                int col=table.columnAtPoint(e.getPoint());
                if(col==5&&row>=0){
                    Rectangle cell=table.getCellRect(row,col,false);
                    int x=e.getX()-cell.x;
                    int roomNo=(int)tableModel.getValueAt(row,0);
                    if(x<68) showRoomDialog(dm.findRoom(roomNo));
                    else     confirmDelete(roomNo);
                }
            }
        });

        JScrollPane sp = Theme.darkScroll(table);
        card.add(sp,BorderLayout.CENTER);

        // Footer
        JLabel footLbl = new JLabel("Showing "+dm.getTotalRooms()+" entries");
        footLbl.setFont(Theme.FONT_SMALL); footLbl.setForeground(Theme.TEXT_GRAY);
        footLbl.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        card.add(footLbl,BorderLayout.SOUTH);

        content.add(card,BorderLayout.CENTER);
        add(content,BorderLayout.CENTER);
        loadTable(dm.getRooms());
    }

    private void loadTable(List<Room> list){
        tableModel.setRowCount(0);
        for(Room r:list){
            tableModel.addRow(new Object[]{r.getRoomNo(),r.getRoomType(),
                String.format("%,.0f PKR",r.getPricePerNight()),
                r.getCapacity()+" Guests",r.getStatus(),"actions"});
        }
    }

    private void filter(){
        String q=searchField.getText().trim().toLowerCase();
        if(q.isEmpty()){loadTable(dm.getRooms());return;}
        loadTable(dm.getRooms().stream()
            .filter(r->String.valueOf(r.getRoomNo()).contains(q)||r.getRoomType().toLowerCase().contains(q))
            .collect(Collectors.toList()));
    }

    private void showRoomDialog(Room existing){
        JDialog dlg=new JDialog(mf,existing==null?"Add New Room":"Edit Room",true);
        dlg.setSize(460,560); dlg.setLocationRelativeTo(mf);

        JPanel root=new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);

        // Dialog header
        JPanel header=new JPanel(new BorderLayout());
        header.setBackground(new Color(248,250,252));
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0,Theme.BORDER),
            BorderFactory.createEmptyBorder(16,22,16,22)
        ));
        JLabel hTitle=new JLabel(existing==null?"\uD83C\uDFE0  Add New Room":"\uD83C\uDFE0  Edit Room #"+existing.getRoomNo());
        hTitle.setFont(new Font("SansSerif",Font.BOLD,15)); hTitle.setForeground(new Color(15,23,42));
        header.add(hTitle,BorderLayout.WEST);
        root.add(header,BorderLayout.NORTH);

        JPanel form=new JPanel(); form.setBackground(Color.WHITE);
        form.setLayout(new BoxLayout(form,BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(20,22,20,22));

        JTextField noField  = addFormField(form,"Room Number",existing==null?"":String.valueOf(existing.getRoomNo()));
        JTextField capField = addFormField(form,"Capacity (Guests)",existing==null?"":String.valueOf(existing.getCapacity()));

        JLabel tl=new JLabel("Room Type"); tl.setFont(Theme.FONT_BOLD); tl.setForeground(Theme.TEXT_DARK);
        tl.setAlignmentX(LEFT_ALIGNMENT); form.add(tl); form.add(Box.createVerticalStrut(5));
        JComboBox<String> typeCombo=Theme.styledCombo(new String[]{"Standard","Deluxe","Suite"});
        if(existing!=null) typeCombo.setSelectedItem(existing.getRoomType());
        typeCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE,40)); typeCombo.setAlignmentX(LEFT_ALIGNMENT);
        form.add(typeCombo); form.add(Box.createVerticalStrut(12));

        JTextField featField  = addFormField(form,"Features / Amenities",existing==null?"":existing.getFeatures());
        JTextField priceField = addFormField(form,"Price Per Night (PKR)",existing==null?"":String.valueOf((int)existing.getPricePerNight()));
        if(existing!=null) noField.setEditable(false);

        root.add(form,BorderLayout.CENTER);

        // Footer buttons
        JPanel footer=new JPanel(new FlowLayout(FlowLayout.RIGHT,10,12));
        footer.setBackground(new Color(248,250,252));
        footer.setBorder(BorderFactory.createMatteBorder(1,0,0,0,Theme.BORDER));
        JButton cancelBtn=Theme.outlineButton("Cancel");
        JButton saveBtn=Theme.goldButton(existing==null?"Save Room":"Update Room");
        saveBtn.setPreferredSize(new Dimension(130,38));
        cancelBtn.setPreferredSize(new Dimension(90,38));
        footer.add(cancelBtn); footer.add(saveBtn);
        root.add(footer,BorderLayout.SOUTH);

        dlg.setContentPane(root);
        cancelBtn.addActionListener(e->dlg.dispose());
        saveBtn.addActionListener(e->{
            try{
                int no=Integer.parseInt(noField.getText().trim());
                int cap=Integer.parseInt(capField.getText().trim());
                String tp=(String)typeCombo.getSelectedItem();
                String ft=featField.getText().trim();
                double pr=Double.parseDouble(priceField.getText().trim());
                if(ft.isEmpty()){JOptionPane.showMessageDialog(dlg,"Features required");return;}
                if(existing==null){
                    if(dm.findRoom(no)!=null){JOptionPane.showMessageDialog(dlg,"Room already exists!");return;}
                    dm.addRoom(new Room(no,tp,pr,cap,ft));
                }else{
                    existing.setCapacity(cap);existing.setRoomType(tp);existing.setFeatures(ft);existing.setPricePerNight(pr);
                    dm.updateRoom(existing);
                }
                refresh();dlg.dispose();
            }catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(dlg,"Please enter valid numbers.","Error",JOptionPane.ERROR_MESSAGE);
            }
        });
        dlg.setVisible(true);
    }

    private JTextField addFormField(JPanel p,String label,String val){
        JLabel l=new JLabel(label); l.setFont(Theme.FONT_BOLD); l.setForeground(Theme.TEXT_DARK);
        l.setAlignmentX(LEFT_ALIGNMENT); p.add(l); p.add(Box.createVerticalStrut(5));
        JTextField f=Theme.styledField(label); f.setText(val);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE,40)); f.setAlignmentX(LEFT_ALIGNMENT);
        p.add(f); p.add(Box.createVerticalStrut(12));
        return f;
    }

    private void confirmDelete(int roomNo){
        int r=JOptionPane.showConfirmDialog(mf,"Delete Room #"+roomNo+"?","Confirm Delete",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        if(r==JOptionPane.YES_OPTION){dm.deleteRoom(roomNo);refresh();}
    }
    public void refresh(){loadTable(dm.getRooms());}
}
