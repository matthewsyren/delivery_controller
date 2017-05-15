package a15008377.opsc7311_assign2_15008377;

import java.io.Serializable;

/**
 * Created by Matthew Syrén on 2017/05/14.
 */

public class DeliveryItem implements Serializable{
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

    //Setter method
    public void setDeliveryItemQuantity(int deliveryItemQuantity) {
        this.deliveryItemQuantity = deliveryItemQuantity;
    }
}
