package hotel.ui;
import hotel.dao.DataManager;
import hotel.model.*;
import hotel.util.Theme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;

public class PaymentDialog extends JDialog {
    private MainFrame mf; private Booking booking; private Room room;
    private DataManager dm=DataManager.getInstance();
    private boolean paid=false;
    private JRadioButton rbCash,rbCard,rbEasy,rbBank;
    private JPanel confirmPanel;

    public PaymentDialog(MainFrame mf,Booking booking,Room room){
        super(mf,"Payment",true);
        this.mf=mf;this.booking=booking;this.room=room;
        setSize(900,520); setLocationRelativeTo(mf);
        buildUI();
    }

    private void buildUI(){
        JPanel root=new JPanel(new BorderLayout());
        root.setBackground(Theme.BG_CONTENT);
        setContentPane(root);

        JPanel header=new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0,Theme.BORDER),
            BorderFactory.createEmptyBorder(14,20,14,20)
        ));
        JLabel ht=new JLabel("\uD83D\uDCB3  Payment - Booking "+booking.getBookingId());
        ht.setFont(new Font("SansSerif",Font.BOLD,16)); ht.setForeground(new Color(15,23,42));
        JButton backBtn=Theme.outlineButton("← Back"); backBtn.setPreferredSize(new Dimension(90,34));
        backBtn.addActionListener(e->dispose());
        header.add(ht,BorderLayout.WEST); header.add(backBtn,BorderLayout.EAST);
        root.add(header,BorderLayout.NORTH);

        JPanel body=new JPanel(new GridLayout(1,3,14,0));
        body.setBackground(Theme.BG_CONTENT);
        body.setBorder(BorderFactory.createEmptyBorder(16,20,16,20));

        // LEFT - Summary
        JPanel sumCard=whiteCard("Booking Summary");
        sumRow(sumCard,"Booking ID :",  booking.getBookingId());
        sumRow(sumCard,"Customer :",    booking.getCustomerName());
        sumRow(sumCard,"Room :",        "#"+booking.getRoomNo()+" ("+booking.getRoomType()+")");
        sumRow(sumCard,"Check-in :",    booking.getCheckIn());
        sumRow(sumCard,"Check-out :",   booking.getCheckOut());
        sumRow(sumCard,"Nights :",      String.valueOf(booking.getTotalNights()));
        sumCard.add(Box.createVerticalGlue());

        // Amount box
        JPanel amtBox=new JPanel(){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp=new GradientPaint(0,0,new Color(245,158,11),getWidth(),0,new Color(234,88,12));
                g2.setPaint(gp); g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8); g2.dispose();
            }
        };
        amtBox.setLayout(new BorderLayout(8,0)); amtBox.setOpaque(false);
        amtBox.setBorder(BorderFactory.createEmptyBorder(10,12,10,12));
        amtBox.setMaximumSize(new Dimension(Integer.MAX_VALUE,70)); amtBox.setAlignmentX(LEFT_ALIGNMENT);

        JPanel amtLeft=new JPanel(new GridLayout(2,1,0,2)); amtLeft.setOpaque(false);
        JLabel amtLbl=new JLabel("Total Amount"); amtLbl.setFont(new Font("SansSerif",Font.PLAIN,11)); amtLbl.setForeground(new Color(255,255,255,180));
        JLabel amtVal=new JLabel(String.format("%,.0f PKR",booking.getTotalAmount())); amtVal.setFont(new Font("SansSerif",Font.BOLD,18)); amtVal.setForeground(Color.WHITE);
        amtLeft.add(amtLbl); amtLeft.add(amtVal);
        amtBox.add(amtLeft,BorderLayout.WEST);

        JButton payNowBtn=new JButton("Pay Now"){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover()?new Color(255,255,255,50):new Color(255,255,255,35));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),6,6);
                g2.dispose(); super.paintComponent(g);
            }
        };
        payNowBtn.setForeground(Color.WHITE); payNowBtn.setFont(new Font("SansSerif",Font.BOLD,13));
        payNowBtn.setContentAreaFilled(false); payNowBtn.setBorderPainted(false); payNowBtn.setFocusPainted(false);
        payNowBtn.setPreferredSize(new Dimension(90,38)); payNowBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        amtBox.add(payNowBtn,BorderLayout.EAST);
        sumCard.add(amtBox);
        body.add(sumCard);

        // MIDDLE - Method
        JPanel methodCard=whiteCard("Select Payment Method");
        ButtonGroup bg=new ButtonGroup();
        rbCash=addRadio(methodCard,"\uD83D\uDCB5  Cash",bg);
        rbCard=addRadio(methodCard,"\uD83D\uDCB3  Credit / Debit Card",bg);
        rbEasy=addRadio(methodCard,"\uD83D\uDCF1  Easypaisa / JazzCash",bg);
        rbBank=addRadio(methodCard,"\uD83C\uDFE6  Bank Transfer",bg);
        rbCash.setSelected(true);
        methodCard.add(Box.createVerticalGlue());
        body.add(methodCard);

        // RIGHT - Confirm
        confirmPanel=whiteCard("");
        confirmPanel.setLayout(new GridBagLayout());
        JLabel waitLbl=new JLabel("<html><center><span style='color:#94a3b8;font-size:13px'>Select payment method<br>and click Pay Now</span></center></html>",SwingConstants.CENTER);
        confirmPanel.add(waitLbl);
        body.add(confirmPanel);

        root.add(body,BorderLayout.CENTER);
        payNowBtn.addActionListener(e->processPayment());
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
        if(!title.isEmpty()){
            JLabel t=new JLabel(title); t.setFont(new Font("SansSerif",Font.BOLD,14)); t.setForeground(new Color(15,23,42));
            t.setAlignmentX(LEFT_ALIGNMENT);
            JSeparator sep=new JSeparator(); sep.setForeground(Theme.BORDER);
            sep.setMaximumSize(new Dimension(Integer.MAX_VALUE,1)); sep.setAlignmentX(LEFT_ALIGNMENT);
            card.add(t); card.add(Box.createVerticalStrut(8)); card.add(sep); card.add(Box.createVerticalStrut(12));
        }
        return card;
    }

    private void sumRow(JPanel card,String label,String value){
        JPanel row=new JPanel(new FlowLayout(FlowLayout.LEFT,4,2)); row.setOpaque(false); row.setAlignmentX(LEFT_ALIGNMENT);
        JLabel l=new JLabel(label); l.setFont(Theme.FONT_SMALL); l.setForeground(Theme.TEXT_GRAY); l.setPreferredSize(new Dimension(90,18));
        JLabel v=new JLabel(value); v.setFont(new Font("SansSerif",Font.BOLD,12)); v.setForeground(new Color(15,23,42));
        row.add(l); row.add(v); card.add(row);
    }

    private JRadioButton addRadio(JPanel p,String label,ButtonGroup bg){
        JRadioButton rb=new JRadioButton(label);
        rb.setFont(Theme.FONT_BODY); rb.setForeground(new Color(51,65,85));
        rb.setOpaque(false); rb.setFocusPainted(false); rb.setAlignmentX(LEFT_ALIGNMENT);
        bg.add(rb); p.add(Box.createVerticalStrut(10)); p.add(rb);
        return rb;
    }

    private void processPayment(){
        String method="Cash";
        if(rbCard.isSelected()) method="Credit/Debit Card";
        else if(rbEasy.isSelected()) method="Easypaisa/JazzCash";
        else if(rbBank.isSelected()) method="Bank Transfer";

        booking.setPaymentMethod(method); booking.setPaymentStatus("Paid"); booking.setBookingStatus("Confirmed");
        dm.addBooking(booking);
        Room r=dm.findRoom(booking.getRoomNo());
        if(r!=null){r.setStatus("Booked");dm.updateRoom(r);}
        LocalDate today=LocalDate.now();
        String dateStr=String.format("%02d-%02d-%d",today.getDayOfMonth(),today.getMonthValue(),today.getYear());
        dm.addPayment(new Payment(dm.nextPaymentId(),booking.getBookingId(),booking.getCustomerName(),booking.getTotalAmount(),method,dateStr));
        Customer cust=dm.findCustomerByName(booking.getCustomerName());
        if(cust!=null){cust.incrementBookings();dm.updateCustomer(cust);}
        paid=true;

        // Show confirmation
        confirmPanel.removeAll(); confirmPanel.setLayout(new BorderLayout());
        JPanel inner=new JPanel(); inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner,BoxLayout.Y_AXIS));
        inner.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // Success icon
        JPanel iconCircle=new JPanel(){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(220,252,231)); g2.fillOval(0,0,60,60); g2.dispose();
            }
        };
        iconCircle.setOpaque(false); iconCircle.setPreferredSize(new Dimension(60,60));
        iconCircle.setMaximumSize(new Dimension(60,60)); iconCircle.setLayout(new GridBagLayout());
        JLabel ck=new JLabel("\u2713"); ck.setFont(new Font("SansSerif",Font.BOLD,28)); ck.setForeground(Theme.GREEN);
        iconCircle.add(ck);
        JPanel iconWrap=new JPanel(new FlowLayout(FlowLayout.CENTER));
        iconWrap.setOpaque(false); iconWrap.add(iconCircle); iconWrap.setAlignmentX(CENTER_ALIGNMENT);

        JLabel conf=new JLabel("Booking Confirmed!",SwingConstants.CENTER);
        conf.setFont(new Font("SansSerif",Font.BOLD,16)); conf.setForeground(new Color(21,128,61)); conf.setAlignmentX(CENTER_ALIGNMENT);
        JLabel thnk=new JLabel("Thank you, "+booking.getCustomerName(),SwingConstants.CENTER);
        thnk.setFont(Theme.FONT_BODY); thnk.setForeground(Theme.TEXT_GRAY); thnk.setAlignmentX(CENTER_ALIGNMENT);
        JLabel msg=new JLabel("<html><center>Your booking has been<br>confirmed successfully.</center></html>",SwingConstants.CENTER);
        msg.setFont(Theme.FONT_SMALL); msg.setForeground(Theme.TEXT_GRAY); msg.setAlignmentX(CENTER_ALIGNMENT);
        JLabel idl=new JLabel("ID: "+booking.getBookingId(),SwingConstants.CENTER);
        idl.setFont(new Font("SansSerif",Font.BOLD,12)); idl.setForeground(Theme.PRIMARY); idl.setAlignmentX(CENTER_ALIGNMENT);

        JButton viewBtn=Theme.outlineButton("View Details"); viewBtn.setAlignmentX(CENTER_ALIGNMENT);
        JButton doneBtn=Theme.goldButton("Done"); doneBtn.setAlignmentX(CENTER_ALIGNMENT);

        inner.add(Box.createVerticalStrut(8)); inner.add(iconWrap);
        inner.add(Box.createVerticalStrut(8)); inner.add(conf);
        inner.add(Box.createVerticalStrut(4)); inner.add(thnk);
        inner.add(Box.createVerticalStrut(4)); inner.add(msg);
        inner.add(Box.createVerticalStrut(6)); inner.add(idl);
        inner.add(Box.createVerticalStrut(12)); inner.add(viewBtn);
        inner.add(Box.createVerticalStrut(6)); inner.add(doneBtn);

        confirmPanel.add(inner,BorderLayout.CENTER);
        confirmPanel.revalidate(); confirmPanel.repaint();

        viewBtn.addActionListener(ev->{dispose();new BookingDetailsDialog(mf,booking).setVisible(true);});
        doneBtn.addActionListener(ev->dispose());
    }
    public boolean isPaid(){return paid;}
}
