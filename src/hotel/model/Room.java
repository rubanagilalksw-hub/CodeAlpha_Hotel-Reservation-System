package hotel.model;
import java.io.Serializable;
public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    private int roomNo;
    private String roomType;
    private double pricePerNight;
    private int capacity;
    private String features;
    private String status;
    public Room(int roomNo,String roomType,double pricePerNight,int capacity,String features){
        this.roomNo=roomNo;this.roomType=roomType;this.pricePerNight=pricePerNight;
        this.capacity=capacity;this.features=features;this.status="Available";
    }
    public int getRoomNo(){return roomNo;}public void setRoomNo(int v){roomNo=v;}
    public String getRoomType(){return roomType;}public void setRoomType(String v){roomType=v;}
    public double getPricePerNight(){return pricePerNight;}public void setPricePerNight(double v){pricePerNight=v;}
    public int getCapacity(){return capacity;}public void setCapacity(int v){capacity=v;}
    public String getFeatures(){return features;}public void setFeatures(String v){features=v;}
    public String getStatus(){return status;}public void setStatus(String v){status=v;}
    public String toString(){return roomNo+"|"+roomType+"|"+pricePerNight+"|"+capacity+"|"+features+"|"+status;}
}
