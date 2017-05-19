package a15008377.opsc7311_assign2_15008377;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.database.Cursor;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.Calendar;

public class HomeActivity extends BaseActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Sets the NavigationDrawer for the Activity and sets the selected item in the NavigationDrawer to Home
        super.onCreateDrawer();
        super.setSelectedNavItem(R.id.nav_home);

        //Sets up the Map for this Activity
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //Displays Markers on a Map, with each Marker representing the destination of a Delivery that needs to be made
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        Cursor deliveryCursor = dbAdapter.getAllDeliveries();
        final ArrayList<LatLng> lstMarkers = new ArrayList<>();
        ArrayList<String> lstMarkerTitles = new ArrayList<>();

        //Displays Markers for Deliveries if the user has created any Deliveries
        if(deliveryCursor.moveToFirst()){
            final LatLngBounds.Builder builder = new LatLngBounds.Builder();

            do{
                String clientID = deliveryCursor.getString(1);
                Client client = dbAdapter.getClient(clientID);
                Calendar calendar = Calendar.getInstance();
                String currentDate = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
                if(client != null && deliveryCursor.getInt(3) == 0 && deliveryCursor.getString(2).equals(currentDate)){
                    //Creates new Marker and title for Marker, and adds them to appropriate ArrayLists
                    LatLng clientLocation = new LatLng(client.getClientLatitude(), client.getClientLongitude());
                    lstMarkers.add(clientLocation);
                    lstMarkerTitles.add("Delivery: " + deliveryCursor.getString(0) + "     Client Name: " + client.getClientName());
                    builder.include(clientLocation);
                }
            }while(deliveryCursor.moveToNext());

            //Displays the markers and sets their titles
            for(int i = 0; i < lstMarkers.size(); i++){
                googleMap.addMarker(new MarkerOptions().position(lstMarkers.get(i)).title(lstMarkerTitles.get(i)));
            }

            //Animates camera to zoom in on Markers once the map has been loaded
            final RelativeLayout layout = (RelativeLayout) findViewById(R.id.content_home);
            layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    try{
                        if(lstMarkers.size() != 0){
                            LatLngBounds bounds = builder.build();
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 250));
                        }
                    }
                    catch(IllegalStateException ise){
                        Toast.makeText(getApplicationContext(), ise.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), "You have no deliveries for today... If you would like to add a delivery, go to the Delivery Control page", Toast.LENGTH_LONG).show();
        }
        dbAdapter.close();
    }
}