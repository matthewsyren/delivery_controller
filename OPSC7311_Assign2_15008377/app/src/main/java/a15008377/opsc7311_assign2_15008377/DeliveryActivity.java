/**
 * Author: Matthew Syrén
 *
 * Date:   19 May 2017
 *
 * Description: Class allows you to add and update Delivery information
 */

package a15008377.opsc7311_assign2_15008377;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DeliveryActivity extends AppCompatActivity {
    //Declarations
    ArrayList<DeliveryItem> lstDeliveryItems;
    String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery);

        //Determines whether the user is adding or updating, and calls the appropriate methods
        Bundle bundle = getIntent().getExtras();
        action = bundle.getString("action");
        Button button = (Button) findViewById(R.id.button_add_delivery);
        lstDeliveryItems = new ArrayList<>();

        if(action.equals("update")){
            EditText txtDeliveryID = (EditText) findViewById(R.id.text_delivery_id);
            txtDeliveryID.setEnabled(false);
            button.setText("Update Delivery");
            Delivery delivery = (Delivery) bundle.getSerializable("deliveryObject");
            displayDelivery(delivery);
            displayDeliveryItems(delivery.getDeliveryID());
        }
        else if(action.equals("add")){
            button.setText("Add Delivery");
            ListView listView = (ListView) findViewById(R.id.list_view_delivery_items);
            DeliveryItemListViewAdapter adapter = new DeliveryItemListViewAdapter(this, new ArrayList<DeliveryItem>());
            listView.setAdapter(adapter);
            adapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    displayItems();
                }
            });
        }

        //Method displays the clients in the spinner_delivery_client Spinner
        displayClients();
        displayItems();
    }

    //Method displays the Delivery object's values
    public void displayDelivery(Delivery delivery){
        EditText txtDeliveryID = (EditText) findViewById(R.id.text_delivery_id);
        TextView txtDeliveryDate = (TextView) findViewById(R.id.text_delivery_date);

        txtDeliveryID.setText(delivery.getDeliveryID());
        txtDeliveryDate.setText(delivery.getDeliveryDate());
        displayDeliveryItems(delivery.getDeliveryID());
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
            ArrayList<String> lstUsedStock = new ArrayList<>();

            //Removes the items that have already been added to the delivery from the Spinner (used for updates only)
            ListView listViewDeliveryItems = (ListView) findViewById(R.id.list_view_delivery_items);
            Adapter lstDeliveryItems =  listViewDeliveryItems.getAdapter();

            for(int i = 0; i < lstDeliveryItems.getCount(); i++){
                DeliveryItem item = (DeliveryItem) lstDeliveryItems.getItem(i);
                lstUsedStock.add(item.getDeliveryStockID());
            }

            //Adds stock items that haven't been used yet to the Spinner
            for(int i = 0; i < lstStock.size(); i++){
                if(!lstUsedStock.contains(lstStock.get(i).getStockID())){
                    lstItems.add(lstStock.get(i).getStockID() + " - " + lstStock.get(i).getStockDescription());
                }
            }

            //Sets the adapter for the spinner
            adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_row, lstItems);
            spinner.setAdapter(adapter);
        }
        catch(FileNotFoundException fnf){
            Toast.makeText(getApplicationContext(), "There is currently no Stock information on this app. Please go to Stock control to add Stock before trying to schedule a Delivery", Toast.LENGTH_LONG).show();
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method displays the Delivery's selected items in the list_view_delivery_items
    public void displayDeliveryItems(String deliveryID){
        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        Cursor cursor = dbAdapter.getDeliveryItems(deliveryID);
        ArrayList<DeliveryItem> lstDeliveryItems = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                DeliveryItem deliveryItem = new DeliveryItem(cursor.getString(0), cursor.getInt(1));
                lstDeliveryItems.add(deliveryItem);
            }while(cursor.moveToNext());

            //Adds items to the ListView
            DeliveryItemListViewAdapter deliveryItemListViewAdapter = new DeliveryItemListViewAdapter(this, lstDeliveryItems);
            ListView listView = (ListView) findViewById(R.id.list_view_delivery_items);
            listView.setAdapter(deliveryItemListViewAdapter);
            deliveryItemListViewAdapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    displayItems();
                }
            });
        }
        dbAdapter.close();
    }

    //Method displays a DatePickerDialog for the user to choose the delivery date
    public void chooseDateOnClick(View vie){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog (this, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                TextView txtDate = (TextView) findViewById(R.id.text_delivery_date);
                txtDate.setText((dayOfMonth + "/" + (month + 1) + "/" + year));
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
            Intent intent = null;

            //Assigns values to the Delivery object
            String deliveryID = txtDeliveryID.getText().toString();
            String deliveryDate = txtDeliveryDate.getText().toString();
            String clientID = spinner.getSelectedItem().toString();
            clientID = clientID.substring(0, clientID.indexOf(" "));
            ArrayList<DeliveryItem> lstDeliveryItems = getDeliveryItems();
            ArrayList<Stock> lstStock = new Stock().readStockItems(this);

            //Loops through available Stock to ensure that there is enough Stock available to cater for the Delivery
            boolean enoughStock = true;

            for(int i = 0; i < lstDeliveryItems.size(); i++){
                String deliveryStockID = lstDeliveryItems.get(i).getDeliveryStockID();
                int numberOfItems = lstDeliveryItems.get(i).getDeliveryItemQuantity();
                for(int j = 0; j < lstStock.size(); j++){
                    String stockID = lstStock.get(j).getStockID();
                    int availableStockQuantity = lstStock.get(j).getStockQuantity();

                    if(deliveryStockID.equals(stockID)){
                        if(numberOfItems > availableStockQuantity){
                            Toast.makeText(getApplicationContext(), "There are only " + availableStockQuantity + " item/s left of " + deliveryStockID + ". Please reduce the number of " + deliveryStockID + " items for this delivery", Toast.LENGTH_LONG).show();
                            enoughStock = false;
                        }
                        else{
                            if(action.equals("update")){
                                DBAdapter dbAdapter = new DBAdapter(this);
                                dbAdapter.open();
                                Cursor cursor = dbAdapter.getDeliveryItem(deliveryID, deliveryStockID);
                                if(cursor.moveToFirst()){
                                    int oldQuantity = cursor.getInt(1);
                                    availableStockQuantity += oldQuantity;
                                }
                            }
                            lstStock.get(j).setStockQuantity(availableStockQuantity - numberOfItems);
                        }
                    }
                }
            }

            //Writes the Delivery details to the database if there is enough Stock for the Delivery
            if(enoughStock){
                Delivery delivery = new Delivery(deliveryID, clientID, deliveryDate, 0, getDeliveryItems());
                DBAdapter dbAdapter = new DBAdapter(this);
                dbAdapter.open();

                //Writes the delivery details to the database if the information is valid
                if(delivery.validateDelivery(this)){
                    if(action.equals("add") && !delivery.checkDeliveryID(this)){
                        dbAdapter.insertDelivery(delivery);
                        Toast.makeText(getApplicationContext(), "Delivery successfully added", Toast.LENGTH_LONG).show();
                        dbAdapter.insertDeliveryItems(delivery.getDeliveryID(), delivery.getLstDeliveryItems());
                        intent = getIntent();
                        finish();
                    }
                    else if(action.equals("update")){
                        dbAdapter.updateDelivery(delivery);
                        dbAdapter.deleteDeliveryItems(delivery.getDeliveryID());
                        dbAdapter.insertDeliveryItems(delivery.getDeliveryID(), delivery.getLstDeliveryItems());
                        Toast.makeText(getApplicationContext(), "Updated delivery successfully", Toast.LENGTH_LONG).show();
                        intent = new Intent(DeliveryActivity.this, DeliveryControlActivity.class);
                    }
                    new Stock().rewriteFile(lstStock, this);
                    startActivity(intent);
                    dbAdapter.close();
                }
            }
        }
        catch(Exception exc){
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //Method reads the list_view_delivery_items ListView and returns an ArrayList containing all the DeliveryItem objects within it
    public ArrayList<DeliveryItem> getDeliveryItems() throws NullPointerException{
        ArrayList<DeliveryItem> lstDeliveryItems = new ArrayList<>();
        ListView listView = (ListView) findViewById(R.id.list_view_delivery_items);
        for(int i = 0; i < listView.getCount(); i++){
            lstDeliveryItems.add((DeliveryItem) listView.getItemAtPosition(i));
        }
        return lstDeliveryItems;
    }

    //Method adds the a DeliveryItem to the list_view_delivery_items ListView
    public void addDeliveryItemOnClick(View view){
        try{
            Spinner spinner = (Spinner) findViewById(R.id.spinner_delivery_items);
            EditText txtQuantity = (EditText) findViewById(R.id.text_delivery_item_quantity);
            ArrayAdapter<String> spinnerAdapter = (ArrayAdapter<String>) spinner.getAdapter();

            if(spinnerAdapter.getCount() == 0){
                Toast.makeText(getApplicationContext(), "All items of stock have been added to the delivery already. You can change their quantities by clicking the + and - buttons in the list", Toast.LENGTH_LONG).show();
            }
            else{
                //Creates a new DeliveryItem object and adds it to the lstDeliveryItems ArrayList
                String deliveryItemID = spinner.getSelectedItem().toString();
                int deliveryItemQuantity = Integer.parseInt(txtQuantity.getText().toString());
                deliveryItemID = deliveryItemID.substring(0, deliveryItemID.indexOf(" "));
                DeliveryItem deliveryItem = new DeliveryItem(deliveryItemID, deliveryItemQuantity);
                lstDeliveryItems.add(deliveryItem);
                spinnerAdapter.remove(spinner.getSelectedItem().toString());
                spinnerAdapter.notifyDataSetChanged();

                //Sets the ListViewAdapter for list_view_delivery_items
                ListView listView = (ListView) findViewById(R.id.list_view_delivery_items);
                DeliveryItemListViewAdapter deliveryItemListViewAdapter = (DeliveryItemListViewAdapter) listView.getAdapter();
                deliveryItemListViewAdapter.add(deliveryItem);
                listView.setAdapter(deliveryItemListViewAdapter);
            }
        }
        catch(NumberFormatException nfe){
            Toast.makeText(this, "Please enter a whole number for the Delivery Item Quantity", Toast.LENGTH_SHORT).show();
        }
        catch(Exception exc){
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}