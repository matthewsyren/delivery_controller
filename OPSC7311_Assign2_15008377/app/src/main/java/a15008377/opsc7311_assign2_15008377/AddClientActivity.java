package a15008377.opsc7311_assign2_15008377;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class AddClientActivity extends AppCompatActivity implements IAPIConnectionResponse{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
    }

    //Method stores the entered data for the client in the database
    public void addClientOnClick(View view) {
        EditText txtClientName = (EditText) findViewById(R.id.text_client_name);
        EditText txtClientEmail = (EditText) findViewById(R.id.text_client_email);
        EditText txtClientAddress = (EditText) findViewById(R.id.text_client_address);

        String clientName = txtClientName.getText().toString();
        String clientEmail = txtClientEmail.getText().toString();
        String clientAddress = txtClientAddress.getText().toString();
        Client client = new Client(clientName, clientEmail, clientAddress);

        //Calls the Google Maps API to determine whether the user has entered a valid address
        if(client.validateClient(this)){
            APIConnection api = new APIConnection();
            api.delegate = this;
            api.execute("http://maps.google.com/maps/api/geocode/json?address=" + client.getClientAddress());
        }
    }

    //Method reads the data returned from the Google Maps API and determines whether the user has entered a valid address
    @Override
    public void getJsonResponse(String response) {
        try{
            if(response != null) {
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.getString("status").equals("OK")){
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    JSONObject location = jsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                    Toast.makeText(this, location.getString("lat") + " " + location.getString("lng"), Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Google Maps was unable to locate the address you typed in, please ensure that the address you have typed in is correct", Toast.LENGTH_LONG).show();
                }
            }
        }
        catch(Exception jse){
            Toast.makeText(getApplicationContext(), jse.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
