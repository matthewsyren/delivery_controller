package a15008377.opsc7311_assign2_15008377;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class AddStockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);
    }

    public void addStock(View view) {
        try{
            EditText txtStockID = (EditText) findViewById(R.id.text_stock_id);
            EditText txtStockDescription = (EditText) findViewById(R.id.text_stock_description);
            EditText txtStockQuantity = (EditText) findViewById(R.id.text_stock_quantity);

            String stockID = txtStockID.getText().toString();
            String stockDescription = txtStockDescription.getText().toString();
            int stockQuantity = Integer.parseInt(txtStockQuantity.getText().toString());

            Stock stock = new Stock(stockID, stockDescription, stockQuantity);
            if(stock.validateStock(this)){
                writeToFile(stockID, stockDescription, stockQuantity);
            }
        }
        catch(NumberFormatException nfe){
            Toast.makeText(getApplicationContext(), "Please enter a whole number for the Stock Quantity", Toast.LENGTH_LONG).show();
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    //Method writes the stock information to the Stock.txt file
    public void writeToFile(String stockID, String stockDescription, int stockQuantity) throws IOException{
        File file = new File(getFilesDir(), "Stock.txt");
        FileOutputStream fileOutputStream = openFileOutput(file.getName(), MODE_APPEND);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        outputStreamWriter.write(stockID + "|" + stockDescription + "|" + stockQuantity + "\n");
        outputStreamWriter.close();
        Toast.makeText(this, "Stock item added successfully", Toast.LENGTH_LONG).show();
    }
}