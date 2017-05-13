package a15008377.opsc7311_assign2_15008377;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddDeliveryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery);

        //Method displays the clients in the spinner_delivery_client Spinner
        displayClients();
    }

    //Method populates the spinner_delivery_client with all available clients
    public void displayClients(){
        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        Spinner spinner = (Spinner) findViewById(R.id.spinner_delivery_client);
        ArrayAdapter<String> adapter;
        List<String> list = new ArrayList<>();

        Cursor cursor = dbAdapter.getAllClients();
        if(cursor.moveToFirst()){
            do{
                list.add(cursor.getString(0));
            }while(cursor.moveToNext());
        }
        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    //Method displays a DatePickerDialog for the user to choose the delivery date
    public void chooseDateOnClick(View vie){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog (this, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                TextView txtDate = (TextView) findViewById(R.id.text_delivery_date);
                txtDate.setText((dayOfMonth + "/" + (month + 1) + "/" + year));

                //Toast.makeText(getApplicationContext(), "Year: " + year + "\nMonth: " + month + "\nDay: " + dayOfMonth, Toast.LENGTH_LONG).show();
            }
        },calendar.get(Calendar.YEAR) , calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setTitle("Please enter delivery date");
        datePickerDialog.show();
    }

    //Method adds the delivery details to the database
    public void addDeliveryOnClick(View view){
        try{
            EditText txtDeliveryID = (EditText) findViewById(R.id.text_delivery_id);
            TextView txtDeliveryDate = (TextView) findViewById(R.id.text_delivery_date);
            Spinner spinner = (Spinner) findViewById(R.id.spinner_delivery_client);

            String deliveryID = txtDeliveryID.getText().toString();
            String deliveryDate = txtDeliveryDate.getText().toString();
            String clientID = spinner.getSelectedItem().toString();

            Delivery delivery = new Delivery(deliveryID, clientID, deliveryDate, 0);

            //Writes the delivery details to the database if the information is valid
            if(delivery.validateDelivery(this) && !delivery.checkDeliveryID(this)){
                DBAdapter dbAdapter = new DBAdapter(this);
                dbAdapter.open();
                dbAdapter.insertDelivery(delivery.getDeliveryID(), delivery.getDeliveryClientID(), delivery.getDeliveryDate(), delivery.getDeliveryComplete());
                Toast.makeText(getApplicationContext(), "Delivery successfully added", Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception exc){
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}