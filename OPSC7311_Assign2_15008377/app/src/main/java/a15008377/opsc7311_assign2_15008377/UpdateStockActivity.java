package a15008377.opsc7311_assign2_15008377;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
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
                Toast.makeText(this, "Stock item updated successfully", Toast.LENGTH_LONG).show();
            }
        }
        catch(NumberFormatException nfe){
            Toast.makeText(getApplicationContext(), "Please enter a whole number for the Stock Quantity", Toast.LENGTH_LONG).show();
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    //Method removes a Stock item from the Stock.txt text file
    public void deleteStockOnClick(View view) throws IOException{
        try{
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Are you sure you would like to delete this item?");

            //Creates OnClickListener for the Dialog message
            DialogInterface.OnClickListener dialogOnClickListener = new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int button) {
                    try{
                        switch(button){
                            //Checks if the username is valid (length > 0 and every character is an alphabetic character)
                            case AlertDialog.BUTTON_POSITIVE:
                                deleteItem();
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), "Deletion cancelled", Toast.LENGTH_LONG).show();
                        }
                    }
                    catch(IOException ioe){
                        ioe.printStackTrace();
                    }
                }
            };

            //Assigns button and OnClickListener for the AlertDialog and displays the AlertDialog
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", dialogOnClickListener);
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", dialogOnClickListener);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
        catch(Exception exc){
            exc.printStackTrace();
        }
    }

    public void deleteItem() throws IOException{
        EditText txtStockID = (EditText) findViewById(R.id.text_stock_id);
        String stockID = txtStockID.getText().toString();
        boolean foundStockID = false;

        ArrayList<Stock> lstStock = Stock.readStockItems(this);
        for(int i = 0; i < lstStock.size() && !foundStockID; i++) {
            if(lstStock.get(i).getStockID().equals(stockID)){
                lstStock.remove(i);
                i--;
                foundStockID = true;
            }
        }
        rewriteFile(lstStock);
        Toast.makeText(getApplicationContext(), "Stock item successfully deleted", Toast.LENGTH_LONG).show();
    }

    //Method deletes the contents of the Stock.txt file and rewrites its content (used once a Stock item has been updated or deleted)
    public void rewriteFile(ArrayList<Stock> lstStock) throws IOException{
        //Clears contents of file
        File file = new File(getFilesDir(), "Stock.txt");
        PrintWriter writer = new PrintWriter(file);
        writer.print("");
        writer.close();

        //Writes updated data to the Stock.txt text file
        FileOutputStream fileOutputStream = openFileOutput(file.getName(), MODE_APPEND);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        for(int i = 0; i < lstStock.size(); i++){
            outputStreamWriter.write(lstStock.get(i).getStockID() + "|" + lstStock.get(i).getStockDescription() + "|" + lstStock.get(i).getStockQuantity() + "\n");
        }
        outputStreamWriter.close();
    }
}