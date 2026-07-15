package hotel.model;
import java.io.Serializable;
public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;
    private String paymentId,bookingId,customerName,method,paymentDate,status;
    private double amount;
    public Payment(String paymentId,String bookingId,String customerName,double amount,String method,String paymentDate){
        this.paymentId=paymentId;this.bookingId=bookingId;this.customerName=customerName;
        this.amount=amount;this.method=method;this.paymentDate=paymentDate;this.status="Paid";
    }
    public String getPaymentId(){return paymentId;}public void setPaymentId(String v){paymentId=v;}
    public String getBookingId(){return bookingId;}public void setBookingId(String v){bookingId=v;}
    public String getCustomerName(){return customerName;}public void setCustomerName(String v){customerName=v;}
    public double getAmount(){return amount;}public void setAmount(double v){amount=v;}
    public String getMethod(){return method;}public void setMethod(String v){method=v;}
    public String getPaymentDate(){return paymentDate;}public void setPaymentDate(String v){paymentDate=v;}
    public String getStatus(){return status;}public void setStatus(String v){status=v;}
    public String toString(){return paymentId+"|"+bookingId+"|"+customerName+"|"+amount+"|"+method+"|"+paymentDate+"|"+status;}
}
