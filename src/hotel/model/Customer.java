package hotel.model;
import java.io.Serializable;
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    private String customerId,name,phone,email;
    private int totalBookings;
    public Customer(String customerId,String name,String phone,String email){
        this.customerId=customerId;this.name=name;this.phone=phone;this.email=email;this.totalBookings=0;
    }
    public String getCustomerId(){return customerId;}public void setCustomerId(String v){customerId=v;}
    public String getName(){return name;}public void setName(String v){name=v;}
    public String getPhone(){return phone;}public void setPhone(String v){phone=v;}
    public String getEmail(){return email;}public void setEmail(String v){email=v;}
    public int getTotalBookings(){return totalBookings;}public void setTotalBookings(int v){totalBookings=v;}
    public void incrementBookings(){totalBookings++;}
    public String toString(){return customerId+","+name+","+phone+","+email+","+totalBookings;}
}
