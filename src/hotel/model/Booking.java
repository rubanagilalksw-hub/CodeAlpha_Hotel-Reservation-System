package hotel.model;
import java.io.Serializable;
public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;
    private String bookingId,customerName,customerPhone,roomType,checkIn,checkOut,paymentMethod,paymentStatus,bookingStatus;
    private int roomNo,totalNights;
    private double totalAmount;
    public Booking(String bookingId,String customerName,String customerPhone,int roomNo,String roomType,String checkIn,String checkOut,int totalNights,double totalAmount){
        this.bookingId=bookingId;this.customerName=customerName;this.customerPhone=customerPhone;
        this.roomNo=roomNo;this.roomType=roomType;this.checkIn=checkIn;this.checkOut=checkOut;
        this.totalNights=totalNights;this.totalAmount=totalAmount;
        this.paymentMethod="Pending";this.paymentStatus="Pending";this.bookingStatus="Confirmed";
    }
    public String getBookingId(){return bookingId;}public void setBookingId(String v){bookingId=v;}
    public String getCustomerName(){return customerName;}public void setCustomerName(String v){customerName=v;}
    public String getCustomerPhone(){return customerPhone;}public void setCustomerPhone(String v){customerPhone=v;}
    public int getRoomNo(){return roomNo;}public void setRoomNo(int v){roomNo=v;}
    public String getRoomType(){return roomType;}public void setRoomType(String v){roomType=v;}
    public String getCheckIn(){return checkIn;}public void setCheckIn(String v){checkIn=v;}
    public String getCheckOut(){return checkOut;}public void setCheckOut(String v){checkOut=v;}
    public int getTotalNights(){return totalNights;}public void setTotalNights(int v){totalNights=v;}
    public double getTotalAmount(){return totalAmount;}public void setTotalAmount(double v){totalAmount=v;}
    public String getPaymentMethod(){return paymentMethod;}public void setPaymentMethod(String v){paymentMethod=v;}
    public String getPaymentStatus(){return paymentStatus;}public void setPaymentStatus(String v){paymentStatus=v;}
    public String getBookingStatus(){return bookingStatus;}public void setBookingStatus(String v){bookingStatus=v;}
    public String toString(){return bookingId+"|"+customerName+"|"+customerPhone+"|"+roomNo+"|"+roomType+"|"+checkIn+"|"+checkOut+"|"+totalNights+"|"+totalAmount+"|"+paymentMethod+"|"+paymentStatus+"|"+bookingStatus;}
}
