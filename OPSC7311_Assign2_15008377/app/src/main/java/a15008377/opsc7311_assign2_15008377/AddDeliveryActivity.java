package a15008377.opsc7311_assign2_15008377;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddDeliveryActivity extends AppCompatActivity {
    ArrayList<DeliveryItem> lstDeliveryItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery);

        //Method displays the clients in the spinner_delivery_client Spinner
        displayClients();
        displayItems();
        lstDeliveryItems = new ArrayList<>();
    }

    //Method populates the spinner_delivery_client with all available clients
    public void displayClients(){
        try{
            DBAdapter dbAdapter = new DBAdapter(this);
            dbAdapter.open();
            Spinner spinner = (Spinner) findViewById(R.id.spinner_delivery_client);
            ArrayAdapter<String> adapter;
            List<String> lstClients = new ArrayList<>();

            Cursor cursor = dbAdapter.getAllClients();
            if(cursor.moveToFirst()){
                do{
                    lstClients.add(cursor.getString(0) + " - " + cursor.getString(1));
                }while(cursor.moveToNext());
            }
            adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_row, lstClients);
            spinner.setAdapter(adapter);
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method populates the spinner_delivery_items with items from the Stock text file
    public void displayItems(){
        try{
            Spinner spinner = (Spinner) findViewById(R.id.spinner_delivery_items);
            ArrayAdapter<String> adapter;
            ArrayList<Stock> lstStock = Stock.readStockItems(this);
            ArrayList<String> lstItems = new ArrayList<>();

            for(int i = 0; i < lstStock.size(); i++){
                lstItems.add(lstStock.get(i).getStockID() + " - " + lstStock.get(i).getStockDescription());
            }
            adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_row, lstItems);
            spinner.setAdapter(adapter);
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
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
        datePickerDialog.show();
    }

    //Method adds the delivery details to the database
    public void addDeliveryOnClick(View view){
        try{
            EditText txtDeliveryID = (EditText) findViewById(R.id.text_delivery_id);
            TextView txtDeliveryDate = (TextView) findViewById(R.id.text_delivery_date);
            Spinner spinner = (Spinner) findViewById(R.id.spinner_delivery_client);

            //Assigns values to the Delivery object
            String deliveryID = txtDeliveryID.getText().toString();
            String deliveryDate = txtDeliveryDate.getText().toString();
            String clientID = spinner.getSelectedItem().toString();
            clientID = clientID.substring(0, clientID.indexOf(" "));
            Delivery delivery = new Delivery(deliveryID, clientID, deliveryDate, 0, new ArrayList<DeliveryItem>());

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

    //Method adds the a DeliveryItem to the list_view_delivery_items ListView
    public void addDeliveryItemOnClick(View view){
        try{
            Spinner spinner = (Spinner) findViewById(R.id.spinner_delivery_items);
            EditText txtQuantity = (EditText) findViewById(R.id.text_delivery_item_quantity);

            //Creates a new DeliveryItem object and adds it to the lstDeliveryItems ArrayList
            String deliveryItemID = spinner.getSelectedItem().toString();
            int deliveryItemQuantity = Integer.parseInt(txtQuantity.getText().toString());
            deliveryItemID = deliveryItemID.substring(0, deliveryItemID.indexOf(" "));
            DeliveryItem deliveryItem = new DeliveryItem(deliveryItemID, deliveryItemQuantity);
            lstDeliveryItems.add(deliveryItem);

            //Sets the ListViewAdapter for list_view_delivery_items
            DeliveryItemListViewAdapter adapter = new DeliveryItemListViewAdapter(this, lstDeliveryItems);
            ListView listView = (ListView) findViewById(R.id.list_view_delivery_items);
            listView.setAdapter(adapter);
        }
        catch(NumberFormatException nfe){
            Toast.makeText(this, "Please enter a whole number for the Delivery Item Quantity", Toast.LENGTH_SHORT).show();
        }
        catch(Exception exc){
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}