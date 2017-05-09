package a15008377.opsc7311_assign2_15008377;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Matthew Syrén on 2017/05/09.
 */

public class Stock {
    //Declarations
    private String stockID;
    private String stockDescription;
    private int stockQuantity;

    //Constructor
    public Stock(String stockID, String stockDescription, int stockQuantity){
        this.stockID = stockID;
        this.stockDescription = stockDescription;
        this.stockQuantity = stockQuantity;
    }

    //Accessor methods
    public String getStockID() {
        return stockID;
    }

    public String getStockDescription() {
        return stockDescription;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    //Method returns true if the Stock object has valid values for all of its attributes, otherwise it returns false
    public boolean validateStock(Context context) throws IOException{
        boolean validStock = false;

        //If statements check numerous validation criteria for the Stock object.
        if(stockID.length() == 0){
            displayMessage("Please enter a Stock ID", context);
        }
        else if(stockID.contains("|")){
            displayMessage("Please remove all | symbols from the Stock ID", context);
        }
        else if(stockDescription.length() == 0){
            displayMessage("Please enter a Stock Description", context);
        }
        else if(stockDescription.contains("|")){
            displayMessage("Please remove all | symbols from the Stock Description", context);
        }
        else if(stockQuantity < 0){
            displayMessage("Please enter a Stock Quantity that is more than or equal to 0", context);
        }
        else if(checkStockID(context)){
            displayMessage(stockID + " has already been taken by another stock item, please choose another Stock ID", context);
        }
        else{
            validStock = true;
            displayMessage("Stock is valid", context);
        }
        return validStock;
    }

    //Method checks if the entered Stock ID has already been taken. The method returns true if it has been taken, and false if it hasn't been taken
    public boolean checkStockID(Context context) throws IOException{
        boolean stockIDTaken = false;
        String line;
        FileInputStream fileInputStream = context.openFileInput("Stock.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

        while((line = bufferedReader.readLine()) != null && !stockIDTaken){
            String[] part = line.split("\\|");
            if(part[0].equals(stockID)){
                stockIDTaken = true;
            }
        }

        return stockIDTaken;
    }

    public String readStockItems(Context context) throws IOException{
        BufferedReader bufferedReader = null;
        String textFileContent = "";
        String line;

        FileInputStream fileInputStream = context.openFileInput("Stock.txt");
        bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

        while((line = bufferedReader.readLine()) != null){
            textFileContent += line + "\n";
        }
        return textFileContent;
    }

    //Method displays a Toast message with the message that is passed in as a parameter
    private void displayMessage(String message, Context context){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
