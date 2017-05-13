package a15008377.opsc7311_assign2_15008377;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class UpdateClientActivity extends AppCompatActivity implements IAPIConnectionResponse {
    Client client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_client);

        Bundle bundle = getIntent().getExtras();
        Client client = (Client) bundle.getSerializable("clientObject");
        displayData(client);
    }

    //Method pre-populates the TextViews on this Activity with the data from the Client item that was clicked on in the previous Activity and sent through the bundle
    public void displayData(Client client){
        EditText txtClientID = (EditText) findViewById(R.id.text_client_id);
        EditText txtClientName = (EditText) findViewById(R.id.text_client_name);
        EditText txtClientEmail = (EditText) findViewById(R.id.text_client_email);
        EditText txtClientAddress = (EditText) findViewById(R.id.text_client_address);

        txtClientID.setText(client.getClientID());
        txtClientName.setText(client.getClientName());
        txtClientEmail.setText(client.getClientEmail());
        txtClientAddress.setText(client.getClientAddress());
    }

    //Method updates the information for the current Client item
    public void updateClientOnClick(View view){
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
            if(client.validateClient(this)){
                APIConnection api = new APIConnection();
                api.delegate = this;
                api.execute("http://maps.google.com/maps/api/geocode/json?address=" + client.getClientAddress());
                DBAdapter dbAdapter = new DBAdapter(this);
                dbAdapter.open();
                dbAdapter.updateClient(client.getClientID(), client.getClientName(), client.getClientEmail(), client.getClientAddress(), client.getClientLatitude(), client.getClientLongitude());
            }
        }
        catch(Exception exc){

        }
    }

    //Method deletes the Client from the database
    public void deleteClientOnClick(View view){
        try{
            EditText txtClientID = (EditText) findViewById(R.id.text_client_id);
            String clientID = txtClientID.getText().toString();

            DBAdapter dbAdapter = new DBAdapter(this);
            dbAdapter.open();
            if(dbAdapter.deleteClient(clientID)){
                Toast.makeText(getApplicationContext(), "Client deleted successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(UpdateClientActivity.this, ClientControlActivity.class);
                startActivity(intent);
            }
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
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
                if(dbAdapter.updateClient(client.getClientID(), client.getClientName(), client.getClientEmail(), client.getClientAddress(), client.getClientLatitude(), client.getClientLongitude())){
                    Toast.makeText(getApplicationContext(), "Client successfully updated", Toast.LENGTH_LONG).show();
                }
            }
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
