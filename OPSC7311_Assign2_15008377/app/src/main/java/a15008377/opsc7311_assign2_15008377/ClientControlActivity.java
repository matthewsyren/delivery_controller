package a15008377.opsc7311_assign2_15008377;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

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
        DBAdapter db = new DBAdapter(this);
        db.open();
        Cursor cursor = db.getAllClients();
        ArrayList<Client> lstClients = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                Client client = new Client(cursor.getString(0), cursor.getString(1), cursor.getString(2));
                lstClients.add(client);
            }
            while(cursor.moveToNext());
        }
        ClientReportListViewAdapter adapter = new ClientReportListViewAdapter(this, lstClients);
        ListView listView = (ListView) findViewById(R.id.list_view_clients);
        listView.setAdapter(adapter);
        db.close();
    }

    //Method takes the user to the AddClientActivity
    public void addClientOnClick(View view){
        Intent intent = new Intent(ClientControlActivity.this, AddClientActivity.class);
        startActivity(intent);
    }

}
