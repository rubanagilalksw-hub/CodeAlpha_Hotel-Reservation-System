package hotel.ui;
import hotel.dao.DataManager;
import hotel.model.*;
import hotel.util.Theme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.*;
import java.time.temporal.ChronoUnit;

public class BookRoomDialog extends JDialog {
    private MainFrame mf; private Room room;
    private DataManager dm=DataManager.getInstance();
    private boolean booked=false;
    private JTextField custNameField,custPhoneField,checkInField,checkOutField;
    private JLabel nightsLbl,amountLbl;

    public BookRoomDialog(MainFrame mf,Room room,String checkIn,String checkOut,String preset){
        super(mf,"Book Room",true);
        this.mf=mf; this.room=room;
        setSize(840,620); setMinimumSize(new Dimension(760,520));
        setResizable(true); setLocationRelativeTo(mf);
        buildUI(checkIn,checkOut,preset);
    }

    private void buildUI(String checkIn,String checkOut,String preset){
        JPanel root=new JPanel(new BorderLayout());
        root.setBackground(Theme.BG_CONTENT);
        setContentPane(root);

        // Header
        JPanel header=new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0,Theme.BORDER),
            BorderFactory.createEmptyBorder(14,20,14,20)
        ));
        JLabel ht=new JLabel("\uD83D\uDECF  Book Room #"+room.getRoomNo()+" - "+room.getRoomType());
        ht.setFont(new Font("SansSerif",Font.BOLD,16)); ht.setForeground(new Color(15,23,42));
        JButton backBtn=Theme.outlineButton("← Back"); backBtn.setPreferredSize(new Dimension(90,34));
        backBtn.addActionListener(e->dispose());
        header.add(ht,BorderLayout.WEST); header.add(backBtn,BorderLayout.EAST);
        root.add(header,BorderLayout.NORTH);

        JPanel body=new JPanel(new GridLayout(1,2,16,0));
        body.setBackground(Theme.BG_CONTENT);
        body.setBorder(BorderFactory.createEmptyBorder(16,20,16,20));

        // LEFT - Room Details
        JPanel leftCard=whiteCard("Room Details");
        detailRow(leftCard,"Room No. :",   String.valueOf(room.getRoomNo()));
        detailRow(leftCard,"Room Type :",  room.getRoomType());
        detailRow(leftCard,"Price / Night :",String.format("%,.0f PKR",room.getPricePerNight()));
        detailRow(leftCard,"Capacity :",   room.getCapacity()+" Guests");
        detailRow(leftCard,"Status :",     room.getStatus());
        detailRow(leftCard,"Features :",   room.getFeatures());
        body.add(leftCard);

        // RIGHT - Booking Info
        JPanel rightCard=whiteCard("Booking Information");
        custNameField =inputRow(rightCard,"Customer Name",   preset!=null?preset:"");
        custPhoneField=inputRow(rightCard,"Phone Number",    "");

        JLabel cl1=new JLabel("Check-in Date"); cl1.setFont(Theme.FONT_BOLD); cl1.setForeground(Theme.TEXT_DARK);
        cl1.setAlignmentX(LEFT_ALIGNMENT); rightCard.add(cl1); rightCard.add(Box.createVerticalStrut(4));
        checkInField=Theme.styledField("dd-mm-yyyy");
        checkInField.setText(validDate(checkIn)?checkIn:todayStr());
        checkInField.setMaximumSize(new Dimension(Integer.MAX_VALUE,38)); checkInField.setAlignmentX(LEFT_ALIGNMENT);
        rightCard.add(checkInField); rightCard.add(Box.createVerticalStrut(10));

        JLabel cl2=new JLabel("Check-out Date"); cl2.setFont(Theme.FONT_BOLD); cl2.setForeground(Theme.TEXT_DARK);
        cl2.setAlignmentX(LEFT_ALIGNMENT); rightCard.add(cl2); rightCard.add(Box.createVerticalStrut(4));
        checkOutField=Theme.styledField("dd-mm-yyyy");
        checkOutField.setText(validDate(checkOut)?checkOut:tomorrowStr());
        checkOutField.setMaximumSize(new Dimension(Integer.MAX_VALUE,38)); checkOutField.setAlignmentX(LEFT_ALIGNMENT);
        rightCard.add(checkOutField); rightCard.add(Box.createVerticalStrut(12));

        // Summary box
        JPanel summaryBox=new JPanel(new GridLayout(2,1,0,4)){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(239,246,255)); g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8);
                g2.dispose();
            }
        };
        summaryBox.setOpaque(false); summaryBox.setAlignmentX(LEFT_ALIGNMENT);
        summaryBox.setMaximumSize(new Dimension(Integer.MAX_VALUE,56));
        summaryBox.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));

        JPanel nr=new JPanel(new FlowLayout(FlowLayout.LEFT,4,0)); nr.setOpaque(false);
        JLabel nl=new JLabel("Total Nights: "); nl.setFont(Theme.FONT_SMALL); nl.setForeground(Theme.TEXT_GRAY);
        nightsLbl=new JLabel("?"); nightsLbl.setFont(new Font("SansSerif",Font.BOLD,13)); nightsLbl.setForeground(Theme.PRIMARY);
        nr.add(nl); nr.add(nightsLbl);

        JPanel ar=new JPanel(new FlowLayout(FlowLayout.LEFT,4,0)); ar.setOpaque(false);
        JLabel al=new JLabel("Total Amount: "); al.setFont(Theme.FONT_SMALL); al.setForeground(Theme.TEXT_GRAY);
        amountLbl=new JLabel("-"); amountLbl.setFont(new Font("SansSerif",Font.BOLD,16)); amountLbl.setForeground(Theme.GOLD);
        ar.add(al); ar.add(amountLbl);

        summaryBox.add(nr); summaryBox.add(ar);
        rightCard.add(summaryBox); rightCard.add(Box.createVerticalStrut(14));

        JButton proceedBtn=new JButton("Proceed to Payment →"){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp=new GradientPaint(0,0,Theme.GOLD,getWidth(),0,Theme.GOLD_HOVER);
                g2.setPaint(gp); g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8);
                g2.dispose(); super.paintComponent(g);
            }
        };
        proceedBtn.setForeground(Color.WHITE); proceedBtn.setFont(new Font("SansSerif",Font.BOLD,13));
        proceedBtn.setContentAreaFilled(false); proceedBtn.setBorderPainted(false); proceedBtn.setFocusPainted(false);
        proceedBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE,42)); proceedBtn.setAlignmentX(LEFT_ALIGNMENT);
        proceedBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        rightCard.add(proceedBtn);
        body.add(rightCard);

        JScrollPane scroll=new JScrollPane(body);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        root.add(scroll,BorderLayout.CENTER);

        FocusAdapter calc=new FocusAdapter(){public void focusLost(FocusEvent e){calcNights();}};
        checkInField.addFocusListener(calc); checkOutField.addFocusListener(calc);
        calcNights();
        proceedBtn.addActionListener(e->doBook());
    }

    private JPanel whiteCard(String title){
        JPanel card=new JPanel(){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0,0,0,8)); g2.fillRoundRect(2,3,getWidth()-2,getHeight()-2,12,12);
                g2.setColor(Color.WHITE); g2.fillRoundRect(0,0,getWidth()-2,getHeight()-2,12,12);
                g2.setColor(Theme.BORDER); g2.drawRoundRect(0,0,getWidth()-3,getHeight()-3,12,12);
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card,BoxLayout.Y_AXIS)); card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
        JLabel t=new JLabel(title); t.setFont(new Font("SansSerif",Font.BOLD,14)); t.setForeground(new Color(15,23,42));
        t.setAlignmentX(LEFT_ALIGNMENT);
        JLabel divider=new JLabel(); divider.setPreferredSize(new Dimension(0,1));
        divider.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Theme.BORDER));
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE,1)); divider.setAlignmentX(LEFT_ALIGNMENT);
        card.add(t); card.add(Box.createVerticalStrut(8)); card.add(divider); card.add(Box.createVerticalStrut(12));
        return card;
    }

    private void detailRow(JPanel card,String label,String value){
        JPanel row=new JPanel(new FlowLayout(FlowLayout.LEFT,6,2));
        row.setOpaque(false); row.setAlignmentX(LEFT_ALIGNMENT);
        JLabel l=new JLabel(label); l.setFont(Theme.FONT_SMALL); l.setForeground(Theme.TEXT_GRAY);
        l.setPreferredSize(new Dimension(110,18));
        JLabel v=new JLabel(value); v.setFont(new Font("SansSerif",Font.BOLD,12)); v.setForeground(new Color(15,23,42));
        row.add(l); row.add(v); card.add(row);
    }

    private JTextField inputRow(JPanel card,String label,String defVal){
        JLabel l=new JLabel(label); l.setFont(Theme.FONT_BOLD); l.setForeground(Theme.TEXT_DARK);
        l.setAlignmentX(LEFT_ALIGNMENT); card.add(l); card.add(Box.createVerticalStrut(4));
        JTextField f=Theme.styledField(label); f.setText(defVal);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE,38)); f.setAlignmentX(LEFT_ALIGNMENT);
        card.add(f); card.add(Box.createVerticalStrut(10));
        return f;
    }

    private void calcNights(){
        try{
            DateTimeFormatter fmt=DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate in=LocalDate.parse(checkInField.getText().trim(),fmt);
            LocalDate out=LocalDate.parse(checkOutField.getText().trim(),fmt);
            long n=ChronoUnit.DAYS.between(in,out); if(n<1) n=1;
            nightsLbl.setText(String.valueOf(n));
            amountLbl.setText(String.format("%,.0f PKR",room.getPricePerNight()*n));
        }catch(Exception ex){nightsLbl.setText("?");amountLbl.setText("-");}
    }

    private void doBook(){
        String name=custNameField.getText().trim(),phone=custPhoneField.getText().trim();
        if(name.isEmpty()||phone.isEmpty()){JOptionPane.showMessageDialog(this,"Please fill all fields.","Error",JOptionPane.ERROR_MESSAGE);return;}
        int nights; try{nights=Integer.parseInt(nightsLbl.getText());}catch(Exception e){nights=1;}
        double amount=room.getPricePerNight()*nights;
        Booking booking=new Booking(dm.nextBookingId(),name,phone,room.getRoomNo(),room.getRoomType(),
            checkInField.getText().trim(),checkOutField.getText().trim(),nights,amount);
        PaymentDialog pd=new PaymentDialog(mf,booking,room);
        pd.setVisible(true);
        if(pd.isPaid()){booked=true;dispose();}
    }

    private boolean validDate(String s){if(s==null||s.isEmpty()||s.equals("dd-mm-yyyy"))return false;
        try{DateTimeFormatter.ofPattern("dd-MM-yyyy").parse(s);return true;}catch(Exception e){return false;}}
    private String todayStr(){LocalDate d=LocalDate.now();return String.format("%02d-%02d-%d",d.getDayOfMonth(),d.getMonthValue(),d.getYear());}
    private String tomorrowStr(){LocalDate d=LocalDate.now().plusDays(1);return String.format("%02d-%02d-%d",d.getDayOfMonth(),d.getMonthValue(),d.getYear());}
    public boolean isBooked(){return booked;}
}
