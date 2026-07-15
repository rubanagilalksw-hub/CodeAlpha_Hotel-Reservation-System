package hotel.dao;
import hotel.model.*;
import java.io.*;
import java.util.*;

public class DataManager {
    private static DataManager instance;
    private List<Room> rooms=new ArrayList<>();
    private List<Customer> customers=new ArrayList<>();
    private List<Booking> bookings=new ArrayList<>();
    private List<Payment> payments=new ArrayList<>();
    private static final String DIR="hotel_data";
    private static final String RF=DIR+"/rooms.txt",CF=DIR+"/customers.txt",BF=DIR+"/bookings.txt",PF=DIR+"/payments.txt";

    private DataManager(){new File(DIR).mkdirs();loadAll();if(rooms.isEmpty())initSample();}
    public static DataManager getInstance(){if(instance==null)instance=new DataManager();return instance;}

    private void initSample(){
        rooms.add(new Room(101,"Standard",5000,2,"Wi-Fi, AC, TV"));
        rooms.add(new Room(102,"Deluxe",8000,3,"Wi-Fi, AC, TV, Mini Bar"));
        rooms.add(new Room(103,"Deluxe",8000,3,"Wi-Fi, AC, TV, Mini Bar"));
        rooms.add(new Room(201,"Suite",15000,4,"Wi-Fi, AC, TV, Mini Bar, Balcony"));
        rooms.add(new Room(202,"Suite",15000,4,"Wi-Fi, AC, TV, Mini Bar, Balcony"));
        rooms.add(new Room(203,"Standard",5000,2,"Wi-Fi, AC, TV"));
        rooms.get(1).setStatus("Booked");rooms.get(3).setStatus("Booked");
        customers.add(new Customer("C001","Ali Raza","0300-1234567","ali@gmail.com"));
        customers.add(new Customer("C002","Sana Khan","0311-2345678","sana@gmail.com"));
        customers.add(new Customer("C003","Usman Ahmed","0321-3456789","usman@gmail.com"));
        customers.add(new Customer("C004","Hamza Ali","0331-4567890","hamza@gmail.com"));
        customers.add(new Customer("C005","Ayesha Noor","0346-5678901","ayesha@gmail.com"));
        customers.get(0).setTotalBookings(2);customers.get(1).setTotalBookings(1);
        customers.get(2).setTotalBookings(2);customers.get(3).setTotalBookings(1);customers.get(4).setTotalBookings(1);
        Booking b1=new Booking("B001","Ali Raza","0300-1234567",101,"Standard","10-07-2026","12-07-2026",2,10000);
        b1.setPaymentMethod("Cash");b1.setPaymentStatus("Paid");b1.setBookingStatus("Confirmed");
        Booking b2=new Booking("B002","Sana Khan","0311-2345678",102,"Deluxe","10-07-2026","12-07-2026",2,16000);
        b2.setPaymentMethod("Credit Card");b2.setPaymentStatus("Paid");b2.setBookingStatus("Confirmed");
        Booking b3=new Booking("B003","Usman Ahmed","0321-3456789",103,"Deluxe","11-07-2026","13-07-2026",2,16000);
        b3.setPaymentMethod("Easypaisa");b3.setPaymentStatus("Paid");b3.setBookingStatus("Cancelled");
        Booking b4=new Booking("B004","Hamza Ali","0331-4567890",201,"Suite","11-07-2026","12-07-2026",1,15000);
        b4.setPaymentMethod("Cash");b4.setPaymentStatus("Paid");b4.setBookingStatus("Confirmed");
        Booking b5=new Booking("B005","Ayesha Noor","0346-5678901",202,"Suite","12-07-2026","14-07-2026",2,30000);
        b5.setPaymentMethod("Bank Transfer");b5.setPaymentStatus("Paid");b5.setBookingStatus("Confirmed");
        bookings.addAll(Arrays.asList(b1,b2,b3,b4,b5));
        payments.add(new Payment("P001","B001","Ali Raza",10000,"Cash","09-07-2026"));
        payments.add(new Payment("P002","B002","Sana Khan",16000,"Credit Card","09-07-2026"));
        payments.add(new Payment("P003","B003","Usman Ahmed",16000,"Easypaisa","12-07-2026"));
        payments.add(new Payment("P004","B004","Hamza Ali",15000,"JazzCash","10-07-2026"));
        payments.add(new Payment("P005","B005","Ayesha Noor",30000,"Bank Transfer","11-07-2026"));
        saveAll();
    }
    public List<Room> getRooms(){return rooms;}
    public void addRoom(Room r){rooms.add(r);saveRooms();}
    public void updateRoom(Room r){for(int i=0;i<rooms.size();i++)if(rooms.get(i).getRoomNo()==r.getRoomNo()){rooms.set(i,r);break;}saveRooms();}
    public void deleteRoom(int no){rooms.removeIf(r->r.getRoomNo()==no);saveRooms();}
    public Room findRoom(int no){return rooms.stream().filter(r->r.getRoomNo()==no).findFirst().orElse(null);}
    public int getTotalRooms(){return rooms.size();}
    public long getAvailableRooms(){return rooms.stream().filter(r->"Available".equals(r.getStatus())).count();}
    public long getTodayBookings(){
        java.time.LocalDate t=java.time.LocalDate.now();
        String s=String.format("%02d-%02d-%d",t.getDayOfMonth(),t.getMonthValue(),t.getYear());
        return bookings.stream().filter(b->b.getCheckIn().equals(s)).count();
    }
    public List<Customer> getCustomers(){return customers;}
    public void addCustomer(Customer c){customers.add(c);saveCustomers();}
    public void updateCustomer(Customer c){for(int i=0;i<customers.size();i++)if(customers.get(i).getCustomerId().equals(c.getCustomerId())){customers.set(i,c);break;}saveCustomers();}
    public void deleteCustomer(String id){customers.removeIf(c->c.getCustomerId().equals(id));saveCustomers();}
    public Customer findCustomerByName(String n){return customers.stream().filter(c->c.getName().equalsIgnoreCase(n)).findFirst().orElse(null);}
    public String nextCustomerId(){int m=0;for(Customer c:customers){try{int n=Integer.parseInt(c.getCustomerId().substring(1));if(n>m)m=n;}catch(Exception e){}}return String.format("C%03d",m+1);}
    public List<Booking> getBookings(){return bookings;}
    public void addBooking(Booking b){bookings.add(b);saveBookings();}
    public void updateBooking(Booking b){for(int i=0;i<bookings.size();i++)if(bookings.get(i).getBookingId().equals(b.getBookingId())){bookings.set(i,b);break;}saveBookings();}
    public Booking findBooking(String id){return bookings.stream().filter(b->b.getBookingId().equals(id)).findFirst().orElse(null);}
    public String nextBookingId(){int m=0;for(Booking b:bookings){try{int n=Integer.parseInt(b.getBookingId().substring(1));if(n>m)m=n;}catch(Exception e){}}return String.format("B%03d",m+1);}
    public int getTotalBookings(){return bookings.size();}
    public List<Payment> getPayments(){return payments;}
    public void addPayment(Payment p){payments.add(p);savePayments();}
    public String nextPaymentId(){int m=0;for(Payment p:payments){try{int n=Integer.parseInt(p.getPaymentId().substring(1));if(n>m)m=n;}catch(Exception e){}}return String.format("P%03d",m+1);}
    public double getTotalRevenue(){return payments.stream().filter(p->"Paid".equals(p.getStatus())).mapToDouble(Payment::getAmount).sum();}
    private void saveRooms(){try(PrintWriter pw=new PrintWriter(new FileWriter(RF))){for(Room r:rooms)pw.println(r);}catch(Exception e){e.printStackTrace();}}
    private void saveCustomers(){try(PrintWriter pw=new PrintWriter(new FileWriter(CF))){for(Customer c:customers)pw.println(c);}catch(Exception e){e.printStackTrace();}}
    private void saveBookings(){try(PrintWriter pw=new PrintWriter(new FileWriter(BF))){for(Booking b:bookings)pw.println(b);}catch(Exception e){e.printStackTrace();}}
    private void savePayments(){try(PrintWriter pw=new PrintWriter(new FileWriter(PF))){for(Payment p:payments)pw.println(p);}catch(Exception e){e.printStackTrace();}}
    private void saveAll(){saveRooms();saveCustomers();saveBookings();savePayments();}
    private void loadAll(){
        try(BufferedReader br=new BufferedReader(new FileReader(RF))){String l;while((l=br.readLine())!=null){String[]p=l.split("\\|",6);if(p.length>=6){Room r=new Room(Integer.parseInt(p[0]),p[1],Double.parseDouble(p[2]),Integer.parseInt(p[3]),p[4]);r.setStatus(p[5]);rooms.add(r);}}}catch(Exception ignored){}
        try(BufferedReader br=new BufferedReader(new FileReader(CF))){String l;while((l=br.readLine())!=null){String[]p=l.split(",",5);if(p.length>=5){Customer c=new Customer(p[0],p[1],p[2],p[3]);c.setTotalBookings(Integer.parseInt(p[4]));customers.add(c);}}}catch(Exception ignored){}
        try(BufferedReader br=new BufferedReader(new FileReader(BF))){String l;while((l=br.readLine())!=null){String[]p=l.split("\\|",12);if(p.length>=12){Booking b=new Booking(p[0],p[1],p[2],Integer.parseInt(p[3]),p[4],p[5],p[6],Integer.parseInt(p[7]),Double.parseDouble(p[8]));b.setPaymentMethod(p[9]);b.setPaymentStatus(p[10]);b.setBookingStatus(p[11]);bookings.add(b);}}}catch(Exception ignored){}
        try(BufferedReader br=new BufferedReader(new FileReader(PF))){String l;while((l=br.readLine())!=null){String[]p=l.split("\\|",7);if(p.length>=7){Payment pay=new Payment(p[0],p[1],p[2],Double.parseDouble(p[3]),p[4],p[5]);pay.setStatus(p[6]);payments.add(pay);}}}catch(Exception ignored){}
    }
}
