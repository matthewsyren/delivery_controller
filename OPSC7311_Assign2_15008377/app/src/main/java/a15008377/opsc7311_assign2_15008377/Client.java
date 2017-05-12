package a15008377.opsc7311_assign2_15008377;

import android.content.Context;
import android.widget.Toast;

import java.io.Serializable;

/**
 * Created by Matthew Syrén on 2017/05/11.
 */

public class Client implements Serializable{
    //Declarations
    private String clientName;
    private String clientEmail;
    private String clientAddress;
    private double clientLatitude;
    private double clientLongitude;

    //Constructor
    public Client(String clientName, String clientEmail, String clientAddress) {
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.clientAddress = clientAddress;
    }

    //Getter methods
    public String getClientName() {
        return clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public double getClientLatitude() {
        return clientLatitude;
    }

    public double getClientLongitude() {
        return clientLongitude;
    }

    //Setter methods
    public void setClientLongitude(double clientLongitude) {
        this.clientLongitude = clientLongitude;
    }

    public void setClientLatitude(double clientLatitude) {
        this.clientLatitude = clientLatitude;
    }

    //Method ensures that the Client has valid values for all of its fields
    public boolean validateClient(Context context){
        boolean validStock = false;

        //If statements check numerous validation criteria for the Stock object.
        if(clientName.length() == 0){
            displayMessage("Please enter a Client Name", context);
        }
        else if(clientEmail.length() == 0){
            displayMessage("Please enter a Client Email", context);
        }
        else if(clientAddress.length() == 0){
            displayMessage("Please enter a Client Address", context);
        }
        else{
            validStock = true;
        }
        return validStock;
    }

    //Method displays a Toast message with the message that is passed in as a parameter
    private void displayMessage(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}