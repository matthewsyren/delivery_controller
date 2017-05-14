package a15008377.opsc7311_assign2_15008377;

/**
 * Created by Matthew Syrén on 2017/05/14.
 */

public class DeliveryItem {
    //Declarations
    private String deliveryStockID;
    private int deliveryItemQuantity;

    //Constructor
    public DeliveryItem(String deliveryStockID, int deliveryItemQuantity) {
        this.deliveryStockID = deliveryStockID;
        this.deliveryItemQuantity = deliveryItemQuantity;
    }

    //Getter methods
    public int getDeliveryItemQuantity() {
        return deliveryItemQuantity;
    }

    public String getDeliveryStockID() {
        return deliveryStockID;
    }

    public void setDeliveryItemQuantity(int deliveryItemQuantity) {
        this.deliveryItemQuantity = deliveryItemQuantity;
    }
}
