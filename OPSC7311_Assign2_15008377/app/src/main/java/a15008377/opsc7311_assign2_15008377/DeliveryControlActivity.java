package a15008377.opsc7311_assign2_15008377;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class DeliveryControlActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_control);

        //Sets the NavigationDrawer for the Activity and sets the selected item in the NavigationDrawer to Home
        super.onCreateDrawer();
        super.setSelectedNavItem(R.id.nav_delivery_control);

        populateViews();
    }

    @Override
    public void onResume(){
        super.onResume();
        populateViews();
    }

    //Method populates the views that are displayed on this Activity
    public void populateViews(){
        populateReport();
    }

    //Method populates the ListView on this Activity
    public void populateReport(){
        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        Cursor deliveryCursor  = dbAdapter.getAllDeliveries();
        final ArrayList<Delivery> lstDeliveries = new ArrayList<>();

        //Loops through Cursor and adds each Delivery item to the lstDeliveries ArrayList
        if(deliveryCursor.moveToFirst()){
            do{
                Cursor deliveryItems = dbAdapter.getDeliveryItems(deliveryCursor.getString(0));
                ArrayList<DeliveryItem> lstDeliveryItems = new ArrayList<>();
                if(deliveryItems.moveToFirst()){
                    do{
                        DeliveryItem deliveryItem = new DeliveryItem(deliveryItems.getString(0), deliveryItems.getInt(1));
                        lstDeliveryItems.add(deliveryItem);
                    }while(deliveryItems.moveToNext());

                    Delivery delivery = new Delivery(deliveryCursor.getString(0),deliveryCursor.getString(1), deliveryCursor.getString(2), deliveryCursor.getInt(3), lstDeliveryItems);
                    lstDeliveries.add(delivery);
                }
            }while(deliveryCursor.moveToNext());

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
        }
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
