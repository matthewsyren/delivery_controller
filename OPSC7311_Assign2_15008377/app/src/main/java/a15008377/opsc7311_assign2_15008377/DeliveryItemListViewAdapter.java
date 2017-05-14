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

public class DeliveryItemListViewAdapter extends ArrayAdapter{
    //Declarations
    Context context;
    TextView deliveryItemID;
    TextView deliveryItemQuantity;
    ArrayList<DeliveryItem> lstDeliveryItems;

    //Constructor
    public DeliveryItemListViewAdapter(Context context, ArrayList<DeliveryItem> lstDeliveryItems)
    {
        super(context, R.layout.list_view_row_delivery_item_report, lstDeliveryItems);
        this.context = context;
        this.lstDeliveryItems = lstDeliveryItems;
    }

    //Method populates the appropriate Views with the appropriate data (stored in the shows ArrayList)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        //Inflates the list_row view for the ListView
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.list_view_row_delivery_item_report, parent, false);

        //Component assignments
        deliveryItemID = (TextView) convertView.findViewById(R.id.text_delivery_item_id);
        deliveryItemQuantity = (TextView) convertView.findViewById(R.id.text_delivery_item_quantity);

        //Displays the data in the appropriate Views
        deliveryItemID.setText("ID: " + lstDeliveryItems.get(position).getDeliveryStockID());
        deliveryItemQuantity.setText("Quantity: " + lstDeliveryItems.get(position).getDeliveryItemQuantity());
        return convertView;
    }
}
