package a15008377.opsc7311_assign2_15008377;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class CompletedDeliveryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_delivery);

        //Sets the NavigationDrawer for the Activity and sets the selected item in the NavigationDrawer to Home
        super.onCreateDrawer();
        super.setSelectedNavItem(R.id.nav_completed_deliveries);

        //Sets the onKeyListener for the text_search_client, which will perform a search when the enter key is pressed
        final EditText txtSearchDelivery = (EditText) findViewById(R.id.text_search_delivery);
        txtSearchDelivery.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    String searchTerm = txtSearchDelivery.getText().toString();
                    searchDeliveries(searchTerm);
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    Toast.makeText(getApplicationContext(), "Search complete!", Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });
        populateViews();
    }

    //Method populates the views that are displayed on this Activity
    public void populateViews(){
        getCompleteDeliveries();
    }

    //Method fetches the Deliveries that match the search result and send them to the displayDeliveries method
    public void searchDeliveries(String searchTerm){
        try{
            ArrayList<Delivery> lstSearchResults = Delivery.searchDeliveries(searchTerm, this, 1 );
            displayDeliveries(lstSearchResults);
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    //Method fetches all Deliveries and sends them to the displayDeliveries method
    public void getCompleteDeliveries(){
        try{
            ArrayList<Delivery> lstDeliveries = Delivery.getDeliveries(this, 1);
            displayDeliveries(lstDeliveries);
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    //Method populates the ListView on this Activity
    public void displayDeliveries(final ArrayList<Delivery> lstDeliveries){
        try{
            if(lstDeliveries.size() > 0){
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
                final ListView listView = (ListView) findViewById(R.id.list_view_completed_deliveries);
                listView.setAdapter(adapter);

                dbAdapter.close();
            }
            else{
                Toast.makeText(getApplicationContext(), "There are no currently no Deliveries that are completed", Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}