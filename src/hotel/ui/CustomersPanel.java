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

public class CustomersPanel extends JPanel {
    private MainFrame mf;
    private DataManager dm=DataManager.getInstance();
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;

    public CustomersPanel(MainFrame mf){
        this.mf=mf;
        setBackground(Theme.BG_CONTENT);
        setLayout(new BorderLayout());
        buildUI();
    }

    private void buildUI(){
        JPanel content=new JPanel(new BorderLayout());
        content.setBackground(Theme.BG_CONTENT);
        content.setBorder(BorderFactory.createEmptyBorder(22,22,22,22));

        JPanel titleRow=new JPanel();
        titleRow.setLayout(new BoxLayout(titleRow,BoxLayout.Y_AXIS));
        titleRow.setOpaque(false);
        titleRow.setBorder(BorderFactory.createEmptyBorder(0,0,14,0));
        JLabel pt=new JLabel("Customer Management");
        pt.setFont(new Font("SansSerif",Font.BOLD,20)); pt.setForeground(new Color(15,23,42));
        JLabel ps=new JLabel("View and manage all registered customers");
        ps.setFont(Theme.FONT_SMALL); ps.setForeground(Theme.TEXT_GRAY);
        titleRow.add(pt); titleRow.add(ps);
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
        searchField=Theme.styledField("Search by name, phone or email...");
        searchField.setPreferredSize(new Dimension(300,38));
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
            public void insertUpdate(javax.swing.event.DocumentEvent e){filter();}
            public void removeUpdate(javax.swing.event.DocumentEvent e){filter();}
            public void changedUpdate(javax.swing.event.DocumentEvent e){filter();}
        });
        top.add(searchField,BorderLayout.WEST);
        JButton addBtn=Theme.goldButton("+ Add Customer");
        addBtn.setPreferredSize(new Dimension(130,38));
        addBtn.addActionListener(e->showDialog(null));
        top.add(addBtn,BorderLayout.EAST);
        card.add(top,BorderLayout.NORTH);

        String[] cols={"Customer ID","Name","Phone","Email","Total Bookings","Action"};
        tableModel=new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        table=new JTable(tableModel);
        Theme.styleTable(table);
        table.setRowHeight(42);

        table.getColumnModel().getColumn(5).setCellRenderer((t,v,sel,foc,r,c)->{
            JPanel p=new JPanel(new FlowLayout(FlowLayout.CENTER,5,4));
            p.setBackground(r%2==0?Theme.BG_TABLE_ODD:Theme.BG_TABLE_EVEN);
            p.add(styledTblBtn("Edit",  new Color(239,246,255), Theme.PRIMARY));
            p.add(styledTblBtn("Delete",new Color(254,226,226), Theme.RED));
            p.add(styledTblBtn("Book",  new Color(220,252,231), Theme.GREEN));
            return p;
        });
        table.getColumnModel().getColumn(5).setPreferredWidth(165);
        table.getColumnModel().getColumn(5).setMaxWidth(175);

        table.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                int row=table.rowAtPoint(e.getPoint());
                int col=table.columnAtPoint(e.getPoint());
                if(col==5&&row>=0){
                    Rectangle cell=table.getCellRect(row,col,false);
                    int x=e.getX()-cell.x;
                    String cid=(String)tableModel.getValueAt(row,0);
                    String cname=(String)tableModel.getValueAt(row,1);
                    Customer c=findById(cid);
                    if(x<62)      { if(c!=null) showDialog(c); }
                    else if(x<122){ deleteCustomer(cid); }
                    else          { new SearchRoomsDialog(mf,cname).setVisible(true);refresh(); }
                }
            }
        });

        card.add(Theme.darkScroll(table),BorderLayout.CENTER);
        JLabel fl=new JLabel("  Showing "+dm.getCustomers().size()+" customers");
        fl.setFont(Theme.FONT_SMALL); fl.setForeground(Theme.TEXT_GRAY);
        fl.setBorder(BorderFactory.createEmptyBorder(8,0,8,0));
        card.add(fl,BorderLayout.SOUTH);

        content.add(card,BorderLayout.CENTER);
        add(content,BorderLayout.CENTER);
        loadTable(dm.getCustomers());
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
        btn.setPreferredSize(new Dimension(50,26));
        return btn;
    }

    private void loadTable(List<Customer> list){
        tableModel.setRowCount(0);
        for(Customer c:list)
            tableModel.addRow(new Object[]{c.getCustomerId(),c.getName(),c.getPhone(),c.getEmail(),c.getTotalBookings(),"actions"});
    }

    private void filter(){
        String q=searchField.getText().trim().toLowerCase();
        loadTable(q.isEmpty()?dm.getCustomers():dm.getCustomers().stream()
            .filter(c->c.getName().toLowerCase().contains(q)||c.getPhone().contains(q)||c.getEmail().toLowerCase().contains(q))
            .collect(Collectors.toList()));
    }

    private Customer findById(String id){return dm.getCustomers().stream().filter(c->c.getCustomerId().equals(id)).findFirst().orElse(null);}

    private void showDialog(Customer existing){
        JDialog dlg=new JDialog(mf,existing==null?"Add Customer":"Edit Customer",true);
        dlg.setSize(460,460); dlg.setMinimumSize(new Dimension(420,400));
        dlg.setResizable(true); dlg.setLocationRelativeTo(mf);

        JPanel root=new JPanel(new BorderLayout()); root.setBackground(Color.WHITE);
        JPanel header=new JPanel(new BorderLayout());
        header.setBackground(new Color(248,250,252));
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0,Theme.BORDER),
            BorderFactory.createEmptyBorder(14,22,14,22)
        ));
        JLabel ht=new JLabel(existing==null?"\uD83D\uDC64  Add New Customer":"\uD83D\uDC64  Edit Customer");
        ht.setFont(new Font("SansSerif",Font.BOLD,15)); ht.setForeground(new Color(15,23,42));
        header.add(ht,BorderLayout.WEST); root.add(header,BorderLayout.NORTH);

        JPanel form=new JPanel(); form.setBackground(Color.WHITE);
        form.setLayout(new BoxLayout(form,BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(20,22,20,22));

        JTextField nf=ff(form,"Full Name",   existing==null?"":existing.getName());
        JTextField pf=ff(form,"Phone Number",existing==null?"":existing.getPhone());
        JTextField ef=ff(form,"Email Address",existing==null?"":existing.getEmail());

        JScrollPane formScroll=new JScrollPane(form);
        formScroll.setBorder(BorderFactory.createEmptyBorder());
        formScroll.getVerticalScrollBar().setUnitIncrement(16);
        formScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        root.add(formScroll,BorderLayout.CENTER);

        JPanel footer=new JPanel(new FlowLayout(FlowLayout.RIGHT,10,12));
        footer.setBackground(new Color(248,250,252));
        footer.setBorder(BorderFactory.createMatteBorder(1,0,0,0,Theme.BORDER));
        JButton cancel=Theme.outlineButton("Cancel"); cancel.setPreferredSize(new Dimension(90,38));
        JButton save=Theme.goldButton(existing==null?"Add Customer":"Update"); save.setPreferredSize(new Dimension(130,38));
        footer.add(cancel); footer.add(save); root.add(footer,BorderLayout.SOUTH);

        dlg.setContentPane(root);
        cancel.addActionListener(e->dlg.dispose());
        save.addActionListener(e->{
            String name=nf.getText().trim(),phone=pf.getText().trim(),email=ef.getText().trim();
            if(name.isEmpty()||phone.isEmpty()){JOptionPane.showMessageDialog(dlg,"Name and phone required.");return;}
            if(existing==null) dm.addCustomer(new Customer(dm.nextCustomerId(),name,phone,email));
            else{existing.setName(name);existing.setPhone(phone);existing.setEmail(email);dm.updateCustomer(existing);}
            refresh(); dlg.dispose();
        });
        dlg.setVisible(true);
    }

    private JTextField ff(JPanel p,String label,String val){
        JLabel l=new JLabel(label); l.setFont(Theme.FONT_BOLD); l.setForeground(Theme.TEXT_DARK);
        l.setAlignmentX(LEFT_ALIGNMENT); p.add(l); p.add(Box.createVerticalStrut(5));
        JTextField f=Theme.styledField(label); f.setText(val);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE,40)); f.setAlignmentX(LEFT_ALIGNMENT);
        p.add(f); p.add(Box.createVerticalStrut(12));
        return f;
    }

    private void deleteCustomer(String cid){
        int r=JOptionPane.showConfirmDialog(mf,"Delete customer "+cid+"?","Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        if(r==JOptionPane.YES_OPTION){dm.deleteCustomer(cid);refresh();}
    }
    public void refresh(){loadTable(dm.getCustomers());}
}