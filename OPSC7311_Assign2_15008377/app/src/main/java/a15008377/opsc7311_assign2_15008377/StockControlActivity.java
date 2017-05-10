package a15008377.opsc7311_assign2_15008377;

import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class StockControlActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_control);

        //Sets the NavigationDrawer for the Activity and sets the selected item in the NavigationDrawer to Home
        super.onCreateDrawer();
        super.setSelectedNavItem(R.id.nav_stock_control);

        //Sets the custom adapter for the ListView to display the Show data
        try{
            final ArrayList<Stock> lstStock = Stock.readStockItems(this);
            StockReportListViewAdapter adapter = new StockReportListViewAdapter(this, lstStock);
            ListView listView = (ListView) findViewById(R.id.list_view_available_stock);
            listView.setAdapter(adapter);

            //Sets an OnItemClickListener on the ListView, which will take the user to the SpecificShowActivity, where the user will be shown more information on the show that they clicked on
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                    Intent intent = new Intent(StockControlActivity.this, UpdateStockActivity.class);
                    intent.putExtra("stockObject", lstStock.get(pos));
                    startActivity(intent);
                }
            });
        }
        catch(IOException ioe){

        }
    }

    //Method calls the AddStockActivity
    public void addStockOnClick(View view){
        Intent intent = new Intent(StockControlActivity.this, AddStockActivity.class);
        startActivity(intent);
    }




}
