package a15008377.opsc7311_assign2_15008377;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class UpdateStockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_stock);

        Bundle bundle = getIntent().getExtras();
        Stock stock = (Stock) bundle.getSerializable("stockObject");
        displayData(stock);
    }

    //Method pre-populates the TextViews on this Activity with the data from the Stock item that was clicked on in the previous Activity and sent through the bundle
    public void displayData(Stock stock){
        EditText txtStockID = (EditText) findViewById(R.id.text_stock_id);
        EditText txtStockDescription = (EditText) findViewById(R.id.text_stock_description);
        EditText txtStockQuantity = (EditText) findViewById(R.id.text_stock_quantity);

        txtStockID.setText(stock.getStockID());
        txtStockDescription.setText(stock.getStockDescription());
        txtStockQuantity.setText(stock.getStockQuantity() + "");
    }

    //Method updates the Stock item's information in the Stock.txt text file
    public void updateStockOnClick(View view){
        try{
            EditText txtStockID = (EditText) findViewById(R.id.text_stock_id);
            EditText txtStockDescription = (EditText) findViewById(R.id.text_stock_description);
            EditText txtStockQuantity = (EditText) findViewById(R.id.text_stock_quantity);

            String stockID = txtStockID.getText().toString();
            String stockDescription = txtStockDescription.getText().toString();
            int stockQuantity = Integer.parseInt(txtStockQuantity.getText().toString());

            Stock stock = new Stock(stockID, stockDescription, stockQuantity);
            if(stock.validateStock(this)){
                //Fetches all Stock items from the Stock.txt text file
                ArrayList<Stock> lstStock = Stock.readStockItems(this);
                boolean stockUpdated = false;

                //Loops through the Stock items and updates the appropriate Stock item
                for(int i = 0; i < lstStock.size() && !stockUpdated; i++){
                    if(lstStock.get(i).getStockID().equals(stock.getStockID())){
                        lstStock.set(i, stock);
                        stockUpdated = true;
                    }
                }

                rewriteFile(lstStock);
            }
        }
        catch(NumberFormatException nfe){
            Toast.makeText(getApplicationContext(), "Please enter a whole number for the Stock Quantity", Toast.LENGTH_LONG).show();
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    //Method deletes the contents of the Stock.txt file and rewrites its content (used once a Stock item has been updated)
    public void rewriteFile(ArrayList<Stock> lstStock) throws IOException{
        //Clears contents of file


        File file = new File(getFilesDir(), "Stock.txt");
        PrintWriter writer = new PrintWriter(file);
        writer.print("");
        writer.close();

        FileOutputStream fileOutputStream = openFileOutput(file.getName(), MODE_APPEND);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        for(int i = 0; i < lstStock.size(); i++){
            outputStreamWriter.write(lstStock.get(i).getStockID() + "|" + lstStock.get(i).getStockDescription() + "|" + lstStock.get(i).getStockQuantity() + "\n");
        }
        outputStreamWriter.close();

        Toast.makeText(this, "Stock item updated successfully", Toast.LENGTH_LONG).show();
    }
}