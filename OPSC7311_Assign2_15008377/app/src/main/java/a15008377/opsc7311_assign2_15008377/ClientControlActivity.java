/**
 * Author: Matthew Syrén
 *
 * Date:   19 May 2017
 *
 * Description: Class shows a report of the Clients that are stored in the database
 */

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
import android.view.View.OnKeyListener;
import java.util.ArrayList;

public class ClientControlActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_client_control);

            //Sets the NavigationDrawer for the Activity and sets the selected item in the NavigationDrawer to Home
            super.onCreateDrawer();
            super.setSelectedNavItem(R.id.nav_client_control);

            //Sets the onKeyListener for the text_search_client, which will perform a search when the enter key is pressed
            final EditText txtSearchClient = (EditText) findViewById(R.id.text_search_client);
            txtSearchClient.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if(keyCode == KeyEvent.KEYCODE_ENTER){
                        String searchTerm = txtSearchClient.getText().toString();
                        searchClients(searchTerm);

                        //Hides keyboard one search is completed
                        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

                        //Displays message to user
                        Toast.makeText(getApplicationContext(), "Search complete!", Toast.LENGTH_LONG).show();
                        return true;
                    }
                    return false;
                }
            });

            //Populates the views that need to be displayed on the Activity
            populateViews();
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Repopulates the views when the Activity is resumed
    @Override
    public void onResume(){
        try{
            super.onResume();
            populateViews();
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method populates the views that need to be displayed on the Activity
    public void populateViews(){
        try{
            getAllClients();
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method fetches an ArrayList of the Clients that match the search term entered by the user
    public void searchClients(String searchTerm){
        try{
            DBAdapter dbAdapter = new DBAdapter(this);
            dbAdapter.open();
            Cursor cursor = dbAdapter.searchClients(searchTerm);
            ArrayList<Client> lstSearchResults = new ArrayList<>();

            if(cursor.moveToFirst()){
                do{
                    Client client = new Client(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                    lstSearchResults.add(client);
                }while(cursor.moveToNext());
            }
            dbAdapter.close();
            displayClients(lstSearchResults);
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Fetches all the Clients from the database and sends them to the displayClient method
    public void getAllClients(){
        try{
            DBAdapter db = new DBAdapter(this);
            db.open();
            Cursor cursor = db.getAllClients();
            final ArrayList<Client> lstClients = new ArrayList<>();

            //Loops through the cursor and adds each Client in the cursor to the lstClients ArrayList
            if(cursor.moveToFirst()){
                do{
                    Client client = new Client(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                    lstClients.add(client);
                }
                while(cursor.moveToNext());
            }
            db.close();

            //Displays error message if there are no Clients to display
            if(lstClients.size() > 0){
                displayClients(lstClients);
            }
            else{
                Toast.makeText(getApplicationContext(), "There are no currently no Clients added", Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method populates the ListView with the client data stored in the database
    public void displayClients(final ArrayList<Client> lstClients){
        try{
            if(lstClients.size() > 0){
                //Sets the ListViewAdapter for list_view_clients
                ClientReportListViewAdapter adapter = new ClientReportListViewAdapter(this, lstClients);
                ListView listView = (ListView) findViewById(R.id.list_view_clients);
                listView.setAdapter(adapter);

                //Sets an OnItemClickListener on the ListView, which will take the user to the ClientActivity, where the user will be able to update information about the Clients
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                        Intent intent = new Intent(ClientControlActivity.this, ClientActivity.class);
                        intent.putExtra("action", "update");
                        intent.putExtra("clientObject", lstClients.get(pos));
                        startActivity(intent);
                    }
                });
            }
            else{
                Toast.makeText(getApplicationContext(), "There are no currently no Clients added", Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method takes the user to the ClientActivity
    public void addClientOnClick(View view){
        try{
            Intent intent = new Intent(ClientControlActivity.this, ClientActivity.class);
            intent.putExtra("action", "add");
            startActivity(intent);
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}