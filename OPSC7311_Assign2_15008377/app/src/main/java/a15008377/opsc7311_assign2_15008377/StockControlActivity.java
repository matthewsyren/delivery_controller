/**
 * Author: Matthew Syrén
 *
 * Date:   19 May 2017
 *
 * Description: Class displays a report of all Stock items in the Stock.txt text file
 */

package a15008377.opsc7311_assign2_15008377;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
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

        //Sets the onKeyListener for the text_search_client, which will perform a search when the enter key is pressed
        final EditText txtSearchStock = (EditText) findViewById(R.id.text_search_stock);
        txtSearchStock.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    String searchTerm = txtSearchStock.getText().toString();
                    searchStock(searchTerm);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    Toast.makeText(getApplicationContext(), "Search complete!", Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });

        //Method populates the Stock report
        populateViews();
    }

    @Override
    public void onResume(){
        super.onResume();
        populateViews();
    }

    //Method fetches all Stock items that match the search and sends them to the displayStock method
    public void searchStock(String searchTerm){
        try{
            ArrayList<Stock> lstStock = Stock.readStockItems(this);
            for(int i = 0; i < lstStock.size(); i++){
                if(!lstStock.get(i).getStockID().contains(searchTerm)){
                    lstStock.remove(i);
                    i--;
                }
            }

            displayStock(lstStock);
        }
        catch(IOException ioe){
            Toast.makeText(getApplicationContext(), "There are currently no Stock items added", Toast.LENGTH_LONG).show();
        }
        catch(Exception exc){
            Toast.makeText(getBaseContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method populates the views that are displayed on this Activity
    public void populateViews(){
        try{
            ArrayList<Stock> lstStock = Stock.readStockItems(this);
            displayStock(lstStock);
        }
        catch(IOException ioe){
            Toast.makeText(getApplicationContext(), "There are currently no Stock items added", Toast.LENGTH_LONG).show();
        }
    }

    //Method populates the list_view_available_stock ListView
    public void displayStock(final ArrayList<Stock> lstStock){
        try{
            //Sets the custom adapter for the ListView to display the Stock data
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
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method calls the StockActivity
    public void addStockOnClick(View view){
        Intent intent = new Intent(StockControlActivity.this, StockActivity.class);
        intent.putExtra("action", "add");
        startActivity(intent);
    }
}