package a15008377.opsc7311_assign2_15008377;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DeliveryControlActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_control);

        //Sets the NavigationDrawer for the Activity and sets the selected item in the NavigationDrawer to Home
        super.onCreateDrawer();
        super.setSelectedNavItem(R.id.nav_delivery_control);

        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        Cursor cursor = dbAdapter.getAllDeliveries();
        if(cursor.moveToFirst()){
            do{
                //Toast.makeText(this, "ID: " + cursor.getString(0) + "\nClient: " + cursor.getString(1) + "\nDate: " +cursor.getString(2) + "\nCompleted: " + cursor.getInt(3), Toast.LENGTH_LONG).show();
            }while(cursor.moveToNext());
        }
    }

    //Method takes the user to the AddDeliveryActivity
    public void addDeliveryOnClick(View view){
        try{
            Intent intent = new Intent(DeliveryControlActivity.this, AddDeliveryActivity.class);
            startActivity(intent);
        }
        catch(Exception exc){

        }
    }
}
