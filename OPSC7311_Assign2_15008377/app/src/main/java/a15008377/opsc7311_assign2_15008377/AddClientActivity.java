package a15008377.opsc7311_assign2_15008377;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class AddClientActivity extends AppCompatActivity implements IAPIConnectionResponse{
    Client client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
    }

    //Method stores the entered data for the client in the database
    public void addClientOnClick(View view) {
        try{
            EditText txtClientID = (EditText) findViewById(R.id.text_client_id);
            EditText txtClientName = (EditText) findViewById(R.id.text_client_name);
            EditText txtClientEmail = (EditText) findViewById(R.id.text_client_email);
            EditText txtClientAddress = (EditText) findViewById(R.id.text_client_address);

            String clientID = txtClientID.getText().toString();
            String clientName = txtClientName.getText().toString();
            String clientEmail = txtClientEmail.getText().toString();
            String clientAddress = txtClientAddress.getText().toString();
            client = new Client(clientID, clientName, clientEmail, clientAddress);

            //Calls the Google Maps API to determine whether the user has entered a valid address
            if(client.validateClient(this) && !client.checkClientID(this)){
                APIConnection api = new APIConnection();
                api.delegate = this;
                api.execute("http://maps.google.com/maps/api/geocode/json?address=" + client.getClientAddress());
            }
        }
        catch (IOException ioe){

        }
    }

    //Method reads the data returned from the Google Maps API (the coordinates if the address entered by the user) and determines whether the user has entered a valid address
    @Override
    public void getJsonResponse(String response) {
        try{
            JSONObject location = Client.getAddressCoordinates(response, this);
            if(location != null){
                client.setClientLatitude(location.getDouble("lat"));
                client.setClientLongitude(location.getDouble("lng"));

                Toast.makeText(this, "Lat: " + client.getClientLatitude() + "\nLon: " + client.getClientLongitude(), Toast.LENGTH_LONG).show();

                DBAdapter dbAdapter = new DBAdapter(this);
                dbAdapter.open();
                if(dbAdapter.insertClient(client.getClientID(), client.getClientName(), client.getClientEmail(), client.getClientAddress(), client.getClientLatitude(), client.getClientLongitude()) >= 0){
                    Toast.makeText(getApplicationContext(), "Client successfully added", Toast.LENGTH_LONG).show();
                }
            }
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}