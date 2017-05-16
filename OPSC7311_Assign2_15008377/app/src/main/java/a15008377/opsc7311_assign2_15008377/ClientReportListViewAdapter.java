package a15008377.opsc7311_assign2_15008377;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Matthew Syrén on 2017/05/12.
 */

public class ClientReportListViewAdapter extends ArrayAdapter{
    //Declarations
    private Context context;
    private TextView clientID;
    private TextView clientName;
    private TextView clientEmail;
    private TextView clientAddress;
    private ArrayList<Client> lstClients;
    private ImageButton deleteClient;

    //Constructor
    public ClientReportListViewAdapter(Context context, ArrayList<Client> lstClients)
    {
        super(context, R.layout.list_view_row_client_report, lstClients);
        this.context = context;
        this.lstClients = lstClients;
    }

    //Method populates the appropriate Views with the appropriate data (stored in the shows ArrayList)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        //Inflates the list_row view for the ListView
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.list_view_row_client_report, parent, false);

        //Component assignments
        clientID = (TextView) convertView.findViewById(R.id.text_client_id);
        clientName = (TextView) convertView.findViewById(R.id.text_client_name);
        clientEmail = (TextView) convertView.findViewById(R.id.text_client_email);
        clientAddress = (TextView) convertView.findViewById(R.id.text_client_address);
        deleteClient = (ImageButton) convertView.findViewById(R.id.button_delete_client);

        //Displays the data in the appropriate Views
        clientID.setText("ID: " + lstClients.get(position).getClientID());
        clientName.setText("Name: " + lstClients.get(position).getClientName());
        clientEmail.setText("Email: " + lstClients.get(position).getClientEmail());
        clientAddress.setText("Address: " + lstClients.get(position).getClientAddress());

        //Sets OnClickListener for the button_delete_client Button
        deleteClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBAdapter dbAdapter = new DBAdapter(context);
                dbAdapter.open();
                dbAdapter.deleteClient(lstClients.get(position).getClientID());
                lstClients.remove(position);
                Toast.makeText(context, "Client successfully deleted", Toast.LENGTH_LONG).show();
                dbAdapter.close();
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
