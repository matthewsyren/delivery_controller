package a15008377.opsc7311_assign2_15008377;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Matthew Syrén on 2017/05/13.
 */

public class Delivery {
    //Declarations
    private String deliveryID;
    private String deliveryClientID;
    private String deliveryDate;
    private int deliveryComplete;
    private ArrayList<DeliveryItem> lstDeliveryItems;

    //Constructor
    public Delivery(String deliveryID, String deliveryClientID, String deliveryDate, int deliveryComplete, ArrayList<DeliveryItem> lstDeliveryItems) {
        this.deliveryID = deliveryID;
        this.deliveryClientID = deliveryClientID;
        this.deliveryDate = deliveryDate;
        this.deliveryComplete = deliveryComplete;
        this.lstDeliveryItems = lstDeliveryItems;
    }

    //Getter methods
    public String getDeliveryID() {
        return deliveryID;
    }

    public String getDeliveryClientID() {
        return deliveryClientID;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public int getDeliveryComplete() {
        return deliveryComplete;
    }

    public ArrayList<DeliveryItem> getLstDeliveryItems() {
        return lstDeliveryItems;
    }

    //Method ensures that the Client has valid values for all of its fields
    public boolean validateDelivery(Context context){
        boolean validStock = false;

        //If statements check numerous validation criteria for the Stock object.
        if(deliveryID.length() == 0){
            displayMessage("Please enter a Delivery ID", context);
        }
        else if(deliveryClientID.length() == 0){
            displayMessage("Please enter a Delivery Client", context);
        }
        else if(deliveryDate.length() == 0){
            displayMessage("Please enter a Delivery Date", context);
        }
        else if(lstDeliveryItems.size() == 0){
            displayMessage("Please add at least one item to your delivery", context);
        }
        else{
            validStock = true;
        }
        return validStock;
    }

    //Method checks if the entered Client ID has already been taken. The method returns true if it has been taken, and false if it hasn't been taken
    public boolean checkDeliveryID(Context context) throws IOException {
        boolean deliveryIDTaken = false;
        DBAdapter dbAdapter = new DBAdapter(context);
        dbAdapter.open();
        Cursor cursor = dbAdapter.getDelivery(deliveryID);
        if(cursor.moveToFirst()){
            deliveryIDTaken = true;
            displayMessage("Delivery ID is taken, please choose another one", context);
        }
        return deliveryIDTaken;
    }


    //Method displays a Toast message with the message that is passed in as a parameter
    private void displayMessage(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
