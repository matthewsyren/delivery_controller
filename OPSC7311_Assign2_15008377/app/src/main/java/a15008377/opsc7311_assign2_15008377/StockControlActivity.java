package a15008377.opsc7311_assign2_15008377;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class StockControlActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_control);

        //Sets the NavigationDrawer for the Activity and sets the selected item in the NavigationDrawer to Home
        super.onCreateDrawer();
        super.setSelectedNavItem(R.id.nav_stock_control);

        //Method populates the Stock report
        displayStock();
    }

    //Method populates the list_view_available_stock ListView
    public void displayStock(){
        try{
            //Sets the custom adapter for the ListView to display the Stock data
            final ArrayList<Stock> lstStock = Stock.readStockItems(this);
            StockReportListViewAdapter adapter = new StockReportListViewAdapter(this, lstStock);
            ListView listView = (ListView) findViewById(R.id.list_view_available_stock);
            listView.setAdapter(adapter);

            //Sets an OnItemClickListener on the ListView, which will take the user to the StockActivity, where the user will be able to update the Stock's information
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                    Intent intent = new Intent(StockControlActivity.this, StockActivity.class);
                    intent.putExtra("action", "update");
                    intent.putExtra("stockObject", lstStock.get(pos));
                    startActivity(intent);
                }
            });
        }
        catch(IOException ioe){
            Toast.makeText(getBaseContext(), ioe.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method calls the StockActivity
    public void addStockOnClick(View view){
        Intent intent = new Intent(StockControlActivity.this, StockActivity.class);
        intent.putExtra("action", "add");
        startActivity(intent);
    }
}