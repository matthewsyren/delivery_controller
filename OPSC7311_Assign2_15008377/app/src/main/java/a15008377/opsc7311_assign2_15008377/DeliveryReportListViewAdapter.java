package a15008377.opsc7311_assign2_15008377;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Matthew Syrén on 2017/05/14.
 */

public class DeliveryReportListViewAdapter extends ArrayAdapter {
    //Declarations
    Context context;
    TextView deliveryID;
    TextView deliveryClientID;
    TextView deliveryDate;
    TextView deliveryComplete;
    TextView deliveryItems;

    ArrayList<Delivery> lstDeliveries;

    public DeliveryReportListViewAdapter(Context context, ArrayList<Delivery> lstDeliveries) {
        super(context, R.layout.list_view_row_delivery_report,lstDeliveries);
        this.context = context;
        this.lstDeliveries = lstDeliveries;
    }

    //Method populates the appropriate Views with the appropriate data (stored in the shows ArrayList)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        //Inflates the list_row view for the ListView
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.list_view_row_delivery_report, parent, false);

        //Component assignments
        deliveryID = (TextView) convertView.findViewById(R.id.text_delivery_id);
        deliveryClientID = (TextView) convertView.findViewById(R.id.text_delivery_client_id);
        deliveryDate = (TextView) convertView.findViewById(R.id.text_delivery_date);
        deliveryComplete = (TextView) convertView.findViewById(R.id.text_delivery_complete);
        deliveryItems = (TextView) convertView.findViewById(R.id.text_delivery_items);

        //Displays the data in the appropriate Views
        deliveryID.setText("Delivery ID: " + lstDeliveries.get(position).getDeliveryID());
        deliveryClientID.setText("Client ID: " + lstDeliveries.get(position).getDeliveryClientID());
        deliveryDate.setText("Delivery Date: " + lstDeliveries.get(position).getDeliveryDate());
        deliveryComplete.setText("Delivery Complete: " + lstDeliveries.get(position).getDeliveryComplete() + "\n\n");

        ArrayList<DeliveryItem> lstDeliveryItems = lstDeliveries.get(position).getLstDeliveryItems();
        String itemText = "Delivery Items: \n";
        for(int i = 0; i < lstDeliveryItems.size(); i++){
            itemText += "Item ID: " + lstDeliveryItems.get(i).getDeliveryStockID() + "\nQuantity: " + lstDeliveryItems.get(i).getDeliveryItemQuantity() + "\n\n";
        }
        deliveryItems.setText(itemText);

        return convertView;
    }
}