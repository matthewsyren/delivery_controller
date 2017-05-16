package a15008377.opsc7311_assign2_15008377;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Matthew Syrén on 2017/05/11.
 */

public class Client implements Serializable{
    //Declarations
    private String clientID;
    private String clientName;
    private String clientEmail;
    private String clientAddress;
    private double clientLatitude;
    private double clientLongitude;

    //Constructor
    public Client(String clientID, String clientName, String clientEmail, String clientAddress) {
        this.clientID = clientID;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.clientAddress = clientAddress;
    }

    public Client(String clientID, String clientName, String clientEmail, String clientAddress, double clientLatitude, double clientLongitude) {
        this(clientID, clientName, clientEmail, clientAddress);
        this.clientLatitude = clientLatitude;
        this.clientLongitude = clientLongitude;
    }

        //Getter methods
    public String getClientID() {
        return clientID;
    }

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
        if(clientID.length() == 0){
            displayMessage("Please enter a Client ID", context);
        }
        else if(clientID.contains(" ")){
            displayMessage("Please remove all spaces from the Client ID", context);
        }
        else if(clientName.length() == 0){
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

    //Method checks if the entered Client ID has already been taken. The method returns true if it has been taken, and false if it hasn't been taken
    public boolean checkClientID(Context context) throws IOException {
        boolean stockIDTaken = false;
        DBAdapter dbAdapter = new DBAdapter(context);
        dbAdapter.open();
        Client client = dbAdapter.getClient(clientID);
        if(client != null){
            stockIDTaken = true;
            displayMessage("Client ID is taken, please choose another one", context);
        }
        return stockIDTaken;
    }

    public static JSONObject getAddressCoordinates(String response, Context context) throws JSONException{
        JSONObject jsonObject = new JSONObject(response);
        if(jsonObject.getString("status").equals("OK")){
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            JSONObject location = jsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location");

            return location;
        }
        else{
            Toast.makeText(context, "Google Maps was unable to locate the address you typed in, please ensure that the address you have typed in is correct", Toast.LENGTH_LONG).show();
        }
        return null;
    }

    //Method displays a Toast message with the message that is passed in as a parameter
    private void displayMessage(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}