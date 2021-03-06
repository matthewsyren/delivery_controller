/**
 * Author: Matthew Syrén
 *
 * Date:   19 May 2017
 *
 * Description: Class provides a template for a DeliveryItem object
 */

package a15008377.opsc7311_assign2_15008377;

import java.io.Serializable;
@SuppressWarnings("WeakerAccess")
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
