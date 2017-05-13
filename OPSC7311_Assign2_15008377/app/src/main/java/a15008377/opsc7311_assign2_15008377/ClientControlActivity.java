package a15008377.opsc7311_assign2_15008377;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ClientControlActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_control);

        //Sets the NavigationDrawer for the Activity and sets the selected item in the NavigationDrawer to Home
        super.onCreateDrawer();
        super.setSelectedNavItem(R.id.nav_client_control);

        displayClients();
    }

    //Method populates the ListView with the client data stored in the database
    public void displayClients(){
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

            //Sets the ListViewAdapter for list_view_clients
            ClientReportListViewAdapter adapter = new ClientReportListViewAdapter(this, lstClients);
            ListView listView = (ListView) findViewById(R.id.list_view_clients);
            listView.setAdapter(adapter);

            //Sets an OnItemClickListener on the ListView, which will take the user to the UpdateClientActivity, where the user will be able to update information about the Clients
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                    Intent intent = new Intent(ClientControlActivity.this, UpdateClientActivity.class);
                    intent.putExtra("clientObject", lstClients.get(pos));
                    startActivity(intent);
                }
            });
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method takes the user to the AddClientActivity
    public void addClientOnClick(View view){
        Intent intent = new Intent(ClientControlActivity.this, AddClientActivity.class);
        startActivity(intent);
    }
}