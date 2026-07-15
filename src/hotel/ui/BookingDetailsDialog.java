package hotel.ui;
import hotel.model.Booking;
import hotel.util.Theme;
import javax.swing.*;
import java.awt.*;
import java.awt.print.*;

public class BookingDetailsDialog extends JDialog {
    private Booking b;
    public BookingDetailsDialog(JFrame parent,Booking b){
        super(parent,"Booking Details",true);
        this.b=b; setSize(720,500); setLocationRelativeTo(parent);
        buildUI();
    }

    private void buildUI(){
        JPanel root=new JPanel(new BorderLayout()); root.setBackground(Theme.BG_CONTENT);
        setContentPane(root);

        JPanel header=new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0,Theme.BORDER),
            BorderFactory.createEmptyBorder(14,20,14,20)
        ));
        JLabel ht=new JLabel("\uD83D\uDCCB  Booking Details - "+b.getBookingId());
        ht.setFont(new Font("SansSerif",Font.BOLD,16)); ht.setForeground(new Color(15,23,42));
        JPanel tr=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0)); tr.setOpaque(false);
        JButton backBtn=Theme.outlineButton("← Back"); backBtn.setPreferredSize(new Dimension(90,34));
        JButton printBtn=new JButton("\uD83D\uDDA8 Print Receipt"){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp=new GradientPaint(0,0,Theme.PRIMARY,getWidth(),0,Theme.PRIMARY_DARK);
                g2.setPaint(gp); g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8); g2.dispose(); super.paintComponent(g);
            }
        };
        printBtn.setForeground(Color.WHITE); printBtn.setFont(new Font("SansSerif",Font.BOLD,12));
        printBtn.setContentAreaFilled(false); printBtn.setBorderPainted(false); printBtn.setFocusPainted(false);
        printBtn.setPreferredSize(new Dimension(140,34)); printBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e->dispose()); printBtn.addActionListener(e->doPrint());
        tr.add(backBtn); tr.add(printBtn);
        header.add(ht,BorderLayout.WEST); header.add(tr,BorderLayout.EAST);
        root.add(header,BorderLayout.NORTH);

        JPanel body=new JPanel(new GridLayout(1,2,16,0));
        body.setBackground(Theme.BG_CONTENT);
        body.setBorder(BorderFactory.createEmptyBorder(16,20,16,20));

        // LEFT - Details
        JPanel left=new JPanel(){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0,0,0,8)); g2.fillRoundRect(2,3,getWidth()-2,getHeight()-2,12,12);
                g2.setColor(Color.WHITE); g2.fillRoundRect(0,0,getWidth()-2,getHeight()-2,12,12);
                g2.setColor(Theme.BORDER); g2.drawRoundRect(0,0,getWidth()-3,getHeight()-3,12,12);
                g2.dispose();
            }
        };
        left.setLayout(new BoxLayout(left,BoxLayout.Y_AXIS)); left.setOpaque(false);
        left.setBorder(BorderFactory.createEmptyBorder(16,18,16,18));
        JLabel lt=new JLabel("Booking Information"); lt.setFont(new Font("SansSerif",Font.BOLD,14)); lt.setForeground(new Color(15,23,42)); lt.setAlignmentX(LEFT_ALIGNMENT);
        JSeparator lsep=new JSeparator(); lsep.setForeground(Theme.BORDER); lsep.setMaximumSize(new Dimension(Integer.MAX_VALUE,1)); lsep.setAlignmentX(LEFT_ALIGNMENT);
        left.add(lt); left.add(Box.createVerticalStrut(8)); left.add(lsep); left.add(Box.createVerticalStrut(10));

        dr(left,"Booking ID :",    b.getBookingId());
        dr(left,"Customer Name :", b.getCustomerName());
        dr(left,"Phone Number :",  b.getCustomerPhone());
        dr(left,"Room No :",       "#"+b.getRoomNo()+" ("+b.getRoomType()+")");
        dr(left,"Check-In :",      b.getCheckIn());
        dr(left,"Check-Out :",     b.getCheckOut());
        dr(left,"Total Nights :",  String.valueOf(b.getTotalNights()));
        dr(left,"Total Amount :",  String.format("%,.0f PKR",b.getTotalAmount()));
        dr(left,"Payment Method :",b.getPaymentMethod());
        dr(left,"Payment Status :",b.getPaymentStatus());
        dr(left,"Booking Status :",b.getBookingStatus());
        body.add(left);

        // RIGHT - Success card
        JPanel right=new JPanel(){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0,0,0,8)); g2.fillRoundRect(2,3,getWidth()-2,getHeight()-2,12,12);
                GradientPaint gp=new GradientPaint(0,0,Color.WHITE,0,getHeight(),new Color(240,253,244));
                g2.setPaint(gp); g2.fillRoundRect(0,0,getWidth()-2,getHeight()-2,12,12);
                g2.setColor(Theme.BORDER); g2.drawRoundRect(0,0,getWidth()-3,getHeight()-3,12,12);
                g2.dispose();
            }
        };
        right.setLayout(new GridBagLayout()); right.setOpaque(false);
        JPanel inner=new JPanel(); inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner,BoxLayout.Y_AXIS));

        JPanel iconCircle=new JPanel(){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(220,252,231)); g2.fillOval(0,0,70,70); g2.dispose();
            }
        };
        iconCircle.setOpaque(false); iconCircle.setPreferredSize(new Dimension(70,70)); iconCircle.setMaximumSize(new Dimension(70,70));
        iconCircle.setLayout(new GridBagLayout());
        JLabel ck=new JLabel("\u2713"); ck.setFont(new Font("SansSerif",Font.BOLD,34)); ck.setForeground(Theme.GREEN);
        iconCircle.add(ck);
        JPanel iconWrap=new JPanel(new FlowLayout(FlowLayout.CENTER)); iconWrap.setOpaque(false); iconWrap.add(iconCircle); iconWrap.setAlignmentX(CENTER_ALIGNMENT);

        JLabel conf=new JLabel("Booking Confirmed!",SwingConstants.CENTER); conf.setFont(new Font("SansSerif",Font.BOLD,17)); conf.setForeground(new Color(21,128,61)); conf.setAlignmentX(CENTER_ALIGNMENT);
        JLabel thnk=new JLabel("Thank you, "+b.getCustomerName(),SwingConstants.CENTER); thnk.setFont(Theme.FONT_BODY); thnk.setForeground(Theme.TEXT_GRAY); thnk.setAlignmentX(CENTER_ALIGNMENT);
        JLabel msg=new JLabel("<html><center>Your booking has been<br>confirmed successfully.</center></html>",SwingConstants.CENTER); msg.setFont(Theme.FONT_SMALL); msg.setForeground(Theme.TEXT_GRAY); msg.setAlignmentX(CENTER_ALIGNMENT);

        JPanel idBox=new JPanel(){protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g.create();g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setColor(new Color(239,246,255));g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8);g2.dispose();}};
        idBox.setOpaque(false); idBox.setLayout(new FlowLayout(FlowLayout.CENTER,6,6)); idBox.setAlignmentX(CENTER_ALIGNMENT); idBox.setMaximumSize(new Dimension(200,36));
        JLabel idl=new JLabel("Booking ID: "+b.getBookingId()); idl.setFont(new Font("SansSerif",Font.BOLD,12)); idl.setForeground(Theme.PRIMARY);
        idBox.add(idl);

        inner.add(iconWrap); inner.add(Box.createVerticalStrut(10)); inner.add(conf); inner.add(Box.createVerticalStrut(6));
        inner.add(thnk); inner.add(Box.createVerticalStrut(6)); inner.add(msg); inner.add(Box.createVerticalStrut(12)); inner.add(idBox);
        right.add(inner);
        body.add(right);
        root.add(body,BorderLayout.CENTER);
    }

    private void dr(JPanel card,String label,String value){
        JPanel row=new JPanel(new FlowLayout(FlowLayout.LEFT,6,2)); row.setOpaque(false); row.setAlignmentX(LEFT_ALIGNMENT);
        JLabel l=new JLabel(label); l.setFont(Theme.FONT_SMALL); l.setForeground(Theme.TEXT_GRAY); l.setPreferredSize(new Dimension(145,18));
        JLabel v=new JLabel(value); v.setFont(new Font("SansSerif",Font.BOLD,12)); v.setForeground(new Color(15,23,42));
        row.add(l); row.add(v); card.add(row);
    }

    private void doPrint(){
        PrinterJob job=PrinterJob.getPrinterJob(); job.setJobName("Booking-"+b.getBookingId());
        job.setPrintable((g,pf,page)->{
            if(page>0) return Printable.NO_SUCH_PAGE;
            Graphics2D g2=(Graphics2D)g;
            g2.translate(pf.getImageableX(),pf.getImageableY());
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("SansSerif",Font.BOLD,16));
            g2.drawString("HOTEL RESERVATION SYSTEM",20,30);
            g2.setFont(new Font("SansSerif",Font.PLAIN,11));
            g2.drawString("Booking Receipt",20,48);
            g2.drawLine(20,55,400,55);
            int y=70;
            g2.setFont(new Font("SansSerif",Font.PLAIN,12));
            g2.drawString("Booking ID    : "+b.getBookingId(),20,y+=18);
            g2.drawString("Customer      : "+b.getCustomerName(),20,y+=18);
            g2.drawString("Phone         : "+b.getCustomerPhone(),20,y+=18);
            g2.drawString("Room          : #"+b.getRoomNo()+" ("+b.getRoomType()+")",20,y+=18);
            g2.drawString("Check-In      : "+b.getCheckIn(),20,y+=18);
            g2.drawString("Check-Out     : "+b.getCheckOut(),20,y+=18);
            g2.drawString("Nights        : "+b.getTotalNights(),20,y+=18);
            g2.drawString("Total Amount  : "+String.format("%,.0f PKR",b.getTotalAmount()),20,y+=18);
            g2.drawString("Payment       : "+b.getPaymentMethod(),20,y+=18);
            g2.drawString("Status        : "+b.getBookingStatus(),20,y+=18);
            g2.drawLine(20,y+10,400,y+10);
            g2.setFont(new Font("SansSerif",Font.ITALIC,10));
            g2.drawString("Thank you for choosing our hotel!",20,y+25);
            return Printable.PAGE_EXISTS;
        });
        if(job.printDialog()){try{job.print();}catch(Exception e){JOptionPane.showMessageDialog(this,"Print error: "+e.getMessage());}}
    }
}
