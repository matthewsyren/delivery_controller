package a15008377.opsc7311_assign2_15008377;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class DeliveryControlActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_control);

        //Sets the NavigationDrawer for the Activity and sets the selected item in the NavigationDrawer to Home
        super.onCreateDrawer();
        super.setSelectedNavItem(R.id.nav_delivery_control);

        //Sets the onKeyListener for the text_search_client, which will perform a search when the enter key is pressed
        final EditText txtSearchDelivery = (EditText) findViewById(R.id.text_search_delivery);
        txtSearchDelivery.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    String searchTerm = txtSearchDelivery.getText().toString();
                    searchDeliveries(searchTerm);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    Toast.makeText(getApplicationContext(), "Search complete!", Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });
        populateViews();
    }

    @Override
    public void onResume(){
        super.onResume();
        populateViews();
    }

    //Method populates the views that are displayed on this Activity
    public void populateViews(){
        getAllDeliveries();
    }

    //Method fetches the Deliveries that match the search result and sned them to the displayDeliveries method
    public void searchDeliveries(String searchTerm){
        try{
            DBAdapter dbAdapter = new DBAdapter(this);
            dbAdapter.open();
            Cursor cursor = dbAdapter.searchDelivery(searchTerm);
            ArrayList<Delivery> lstSearchResults = new ArrayList<>();
            ArrayList<DeliveryItem> lstDeliveryItems = new ArrayList<>();

            if(cursor.moveToFirst()){
                do{
                    Delivery delivery = new Delivery(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), lstDeliveryItems);
                    lstSearchResults.add(delivery);
                }while(cursor.moveToNext());
            }
            dbAdapter.close();
            displayDeliveries(lstSearchResults);
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    //Method fetches all Deliveries and sends them to the displayDeliveries method
    public void getAllDeliveries(){
        try{
            DBAdapter dbAdapter = new DBAdapter(this);
            dbAdapter.open();
            Cursor deliveryCursor  = dbAdapter.getAllDeliveries();
            final ArrayList<Delivery> lstDeliveries = new ArrayList<>();
            ArrayList<DeliveryItem> lstDeliveryItems = new ArrayList<>();

            if(deliveryCursor.moveToFirst()){
                do{
                    //Cursor deliveryItems = dbAdapter.getDeliveryItems(deliveryCursor.getString(0));
                    Delivery delivery = new Delivery(deliveryCursor.getString(0),deliveryCursor.getString(1), deliveryCursor.getString(2), deliveryCursor.getInt(3), lstDeliveryItems);
                    lstDeliveries.add(delivery);
                }while(deliveryCursor.moveToNext());
            }
            dbAdapter.close();
            displayDeliveries(lstDeliveries);
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    //Method populates the ListView on this Activity
    public void displayDeliveries(final ArrayList<Delivery> lstDeliveries){
        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        //Loops through Cursor and adds each Delivery item to the lstDeliveries ArrayList
        for(int i = 0; i < lstDeliveries.size(); i++) {
            Cursor deliveryItems = dbAdapter.getDeliveryItems(lstDeliveries.get(i).getDeliveryID());
            ArrayList<DeliveryItem> lstDeliveryItems = new ArrayList<>();
            if(deliveryItems.moveToFirst()){
                do{
                    DeliveryItem deliveryItem = new DeliveryItem(deliveryItems.getString(0), deliveryItems.getInt(1));
                    lstDeliveryItems.add(deliveryItem);
                }while(deliveryItems.moveToNext());
                lstDeliveries.get(i).setLstDeliveryItems(lstDeliveryItems);
            }
        }
        //Sets the Adapter for the list_view_deliveries ListView
        DeliveryReportListViewAdapter adapter = new DeliveryReportListViewAdapter(this, lstDeliveries);
        final ListView listView = (ListView) findViewById(R.id.list_view_deliveries);
        listView.setAdapter(adapter);

        //Sets OnItemClickListener, which will pass the information of the Delivery clicked to the DeliveryActivity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DeliveryControlActivity.this, DeliveryActivity.class);
                intent.putExtra("action", "update");
                intent.putExtra("deliveryObject", lstDeliveries.get(position));
                startActivity(intent);
            }
        });
        dbAdapter.close();
    }

    //Method takes the user to the DeliveryActivity
    public void addDeliveryOnClick(View view){
        try{
            Intent intent = new Intent(DeliveryControlActivity.this, DeliveryActivity.class);
            intent.putExtra("action", "add");
            startActivity(intent);
        }
        catch(Exception exc){

        }
    }
}
