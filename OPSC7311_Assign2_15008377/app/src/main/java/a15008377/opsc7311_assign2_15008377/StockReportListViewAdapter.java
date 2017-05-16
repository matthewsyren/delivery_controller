package a15008377.opsc7311_assign2_15008377;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListResourceBundle;

/**
 * Created by Matthew Syrén on 2017/05/10.
 */

public class StockReportListViewAdapter extends ArrayAdapter {
    //Declarations
    Context context;
    TextView stockID;
    TextView stockDescription;
    TextView latestQuantity;
    ArrayList<Stock> lstStock;
    ImageButton btnDeleteStock;

    //Constructor
    public StockReportListViewAdapter(Context context, ArrayList<Stock> stock)
    {
        super(context, R.layout.list_view_row_stock_report, stock);
        this.context = context;
        lstStock = stock;
    }

    //Method populates the appropriate Views with the appropriate data (stored in the shows ArrayList)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        //Inflates the list_row view for the ListView
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.list_view_row_stock_report, parent, false);

        //Component assignments
        stockID = (TextView) convertView.findViewById(R.id.text_stock_id);
        stockDescription = (TextView) convertView.findViewById(R.id.text_stock_description);
        latestQuantity = (TextView) convertView.findViewById(R.id.text_stock_quantity);
        btnDeleteStock = (ImageButton) convertView.findViewById(R.id.button_delete_stock);

        //Displays the data in the appropriate Views
        stockID.setText("ID: " + lstStock.get(position).getStockID());
        stockDescription.setText("Description: " + lstStock.get(position).getStockDescription());
        latestQuantity.setText("Quantity: " + lstStock.get(position).getStockQuantity());

        //Sets OnClickListener for the button_delete_stock Button
        btnDeleteStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    new Stock().deleteStockItem(lstStock.get(position).getStockID(), context);
                    lstStock.remove(position);
                    notifyDataSetChanged();
                }
                catch(IOException ioe){
                    Toast.makeText(context, ioe.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        return convertView;
    }
}
