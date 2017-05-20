/**
 * Author: Matthew Syrén
 *
 * Date:   19 May 2017
 *
 * Description: Class provides methods to manage the database
 */

package a15008377.opsc7311_assign2_15008377;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;


public class DBAdapter {
    //Database declarations
    private static final String DATABASE_NAME = "Deliveries.db";
    private static final int DATABASE_VERSION = 1;
    private static final String KEY_ROWID = "_id";
    private static final String DATABASE_CLIENT_TABLE = "clients";
    private static final String DATABASE_DELIVERY_TABLE = "deliveries";
    private static final String DATABASE_DELIVERY_ITEM_TABLE = "delivery_items";

    //Clients table declarations
    private static final String KEY_CLIENT_ID = "client_id";
    private static final String KEY_CLIENT_NAME = "client_name";
    private static final String KEY_CLIENT_EMAIL = "client_email";
    private static final String KEY_CLIENT_ADDRESS = "client_address";
    private static final String KEY_CLIENT_LATITUDE = "client_latitude";
    private static final String KEY_CLIENT_LONGITUDE = "client_longitude";
    private static final String DATABASE_CLIENT_CREATE = "create table " + DATABASE_CLIENT_TABLE + " (" + KEY_ROWID + " integer primary key autoincrement, " + KEY_CLIENT_ID + " text unique not null, " + KEY_CLIENT_NAME + " text not null, " + KEY_CLIENT_EMAIL + " text not null, " + KEY_CLIENT_ADDRESS + " text not null, " + KEY_CLIENT_LATITUDE + " decimal(10, 7) not null, " + KEY_CLIENT_LONGITUDE + " decimal(9,6) not null);";

    //Deliveries table declarations
    private static final String KEY_DELIVERY_ID = "delivery_id";
    private static final String KEY_DELIVERY_CLIENT_ID = "delivery_client_id";
    private static final String KEY_DELIVERY_DATE = "delivery_date";
    private static final String KEY_DELIVERY_COMPLETED = "delivery_completed";
    private static final String DATABASE_DELIVERY_CREATE = "create table " + DATABASE_DELIVERY_TABLE + "(" + KEY_ROWID + " integer primary key autoincrement, " + KEY_DELIVERY_ID + " text unique not null, " + KEY_DELIVERY_CLIENT_ID + " text not null, " + KEY_DELIVERY_DATE + " text not null, " + KEY_DELIVERY_COMPLETED + " int not null);";

    //Delivery_Items table declarations
    private static final String KEY_DELIVERY_ITEM_ID = "delivery_item_id";
    private static final String KEY_DELIVERY_ITEM_QUANTITY = "delivery_item_quantity";
    private static final String DATABASE_DELIVERY_ITEM_CREATE = "create table " + DATABASE_DELIVERY_ITEM_TABLE + " (" + KEY_ROWID + " integer primary key autoincrement, " + KEY_DELIVERY_ID + " text not null, " + KEY_DELIVERY_ITEM_ID + " text not null, " + KEY_DELIVERY_ITEM_QUANTITY + " int not null);";

    //Other Declarations
    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;

    //Constructor
    public DBAdapter(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    //Method opens a connection to the database
    public DBAdapter open() {
        sqLiteDatabase = dbHelper.getWritableDatabase();
        return this;
    }

    //Method closes the connection to the database
    public void close() {
        dbHelper.close();
    }

    //Class creates a custom version of SQLiteOpenHandler
    public class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        //Method creates the database tables
        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CLIENT_CREATE);
                db.execSQL(DATABASE_DELIVERY_CREATE);
                db.execSQL(DATABASE_DELIVERY_ITEM_CREATE);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //Method updates the version of the database
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w("DB", "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_CLIENT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_DELIVERY_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_DELIVERY_ITEM_TABLE);
            onCreate(db);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Clients table

    //Method inserts a record to the appropriate table
    public long insertClient(Client client) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CLIENT_ID, client.getClientID());
        contentValues.put(KEY_CLIENT_NAME, client.getClientName());
        contentValues.put(KEY_CLIENT_EMAIL, client.getClientEmail());
        contentValues.put(KEY_CLIENT_ADDRESS, client.getClientAddress());
        contentValues.put(KEY_CLIENT_LATITUDE, client.getClientLatitude());
        contentValues.put(KEY_CLIENT_LONGITUDE, client.getClientLongitude());

        return sqLiteDatabase.insert(DATABASE_CLIENT_TABLE, null, contentValues);
    }

    //Method deletes a record from the appropriate table
    public boolean deleteClient(String clientID) {
        return sqLiteDatabase.delete(DATABASE_CLIENT_TABLE, KEY_CLIENT_ID + "='" + clientID + "'", null) > 0;
    }

    //Method retrieves all records from the appropriate table
    public Cursor getAllClients() {
        return sqLiteDatabase.query(DATABASE_CLIENT_TABLE, new String[] {KEY_CLIENT_ID, KEY_CLIENT_NAME, KEY_CLIENT_EMAIL, KEY_CLIENT_ADDRESS, KEY_CLIENT_LATITUDE, KEY_CLIENT_LONGITUDE}, null, null, null, null, null);
    }

    //Method retrieves all records from the appropriate table
    public Cursor searchClients(String searchTerm) {
        return sqLiteDatabase.query(DATABASE_CLIENT_TABLE, new String[] {KEY_CLIENT_ID, KEY_CLIENT_NAME, KEY_CLIENT_EMAIL, KEY_CLIENT_ADDRESS, KEY_CLIENT_LATITUDE, KEY_CLIENT_LONGITUDE}, KEY_CLIENT_ID + " LIKE '%" + searchTerm + "%'", null, null, null, null);
    }

    //Method retrieves a specific record from the appropriate database
    public Client getClient(String clientID) {
        Cursor cursor = sqLiteDatabase.query(true, DATABASE_CLIENT_TABLE, new String[] {KEY_CLIENT_NAME, KEY_CLIENT_EMAIL, KEY_CLIENT_ADDRESS, KEY_CLIENT_LATITUDE, KEY_CLIENT_LONGITUDE}, KEY_CLIENT_ID + "='" + clientID + "'", null, null, null, null, null);
        Client client;
        if(cursor.moveToFirst()){
            client = new Client(clientID, cursor.getString(0), cursor.getString(1),cursor.getString(2),cursor.getDouble(3),cursor.getDouble(4));
        }
        else{
            client = null;
        }

        return client;
    }

    //Method updates a specific record in the appropriate table
    public boolean updateClient(String clientID, String clientName, String clientEmail, String clientAddress, double clientLatitude, double clientLongitude) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CLIENT_ID, clientID);
        contentValues.put(KEY_CLIENT_NAME, clientName);
        contentValues.put(KEY_CLIENT_EMAIL, clientEmail);
        contentValues.put(KEY_CLIENT_ADDRESS, clientAddress);
        contentValues.put(KEY_CLIENT_LATITUDE, clientLatitude);
        contentValues.put(KEY_CLIENT_LONGITUDE, clientLongitude);

        return sqLiteDatabase.update(DATABASE_CLIENT_TABLE, contentValues, KEY_CLIENT_ID + "='" + clientID + "'", null) > 0;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Deliveries table

    //Method inserts a record to the appropriate table
    public long insertDelivery(Delivery delivery) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DELIVERY_ID, delivery.getDeliveryID());
        contentValues.put(KEY_DELIVERY_CLIENT_ID, delivery.getDeliveryClientID());
        contentValues.put(KEY_DELIVERY_DATE, delivery.getDeliveryDate());
        contentValues.put(KEY_DELIVERY_COMPLETED, delivery.getDeliveryComplete());

        return sqLiteDatabase.insert(DATABASE_DELIVERY_TABLE, null, contentValues);
    }

    //Method deletes a record from the appropriate table
    public boolean deleteDelivery(String deliveryID) {
        return sqLiteDatabase.delete(DATABASE_DELIVERY_TABLE, KEY_DELIVERY_ID + "='" + deliveryID + "'", null) > 0;
    }

    //Method retrieves all records from the appropriate table
    public Cursor getAllDeliveries() {
        return sqLiteDatabase.query(DATABASE_DELIVERY_TABLE, new String[] {KEY_DELIVERY_ID, KEY_DELIVERY_CLIENT_ID, KEY_DELIVERY_DATE, KEY_DELIVERY_COMPLETED}, null, null, null, null, null);
    }

    //Method deletes a record from the appropriate table
    public boolean deleteClientDeliveries(String clientID) {
        return sqLiteDatabase.delete(DATABASE_DELIVERY_TABLE, KEY_DELIVERY_CLIENT_ID + "='" + clientID + "'", null) > 0;
    }

    //Method retrieves a specific record from the appropriate database
    public Cursor getDelivery(String deliveryID) {
        Cursor cursor = sqLiteDatabase.query(true, DATABASE_DELIVERY_TABLE, new String[] {KEY_DELIVERY_ID, KEY_DELIVERY_CLIENT_ID, KEY_DELIVERY_DATE, KEY_DELIVERY_COMPLETED}, KEY_DELIVERY_ID + "='" + deliveryID + "'", null, null, null, null, null);
        return cursor;
    }

    //Method retrieves a specific record from the appropriate database
    public Cursor searchDelivery(String searchTerm)  {
        Cursor cursor = sqLiteDatabase.query(true, DATABASE_DELIVERY_TABLE, new String[] {KEY_DELIVERY_ID, KEY_DELIVERY_CLIENT_ID, KEY_DELIVERY_DATE, KEY_DELIVERY_COMPLETED}, KEY_DELIVERY_ID + " LIKE '%" + searchTerm + "%'", null, null, null, null, null);
        return cursor;
    }

    //Method updates a specific record in the appropriate table
    public boolean updateDelivery(Delivery delivery) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DELIVERY_ID, delivery.getDeliveryID());
        contentValues.put(KEY_DELIVERY_CLIENT_ID, delivery.getDeliveryClientID());
        contentValues.put(KEY_DELIVERY_DATE, delivery.getDeliveryDate());
        contentValues.put(KEY_DELIVERY_COMPLETED, delivery.getDeliveryComplete());

        return sqLiteDatabase.update(DATABASE_DELIVERY_TABLE, contentValues, KEY_DELIVERY_ID + "='" + delivery.getDeliveryID() + "'", null) > 0;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Delivery items table

    //Method inserts a record to the appropriate table
    public int insertDeliveryItems(String deliveryID, ArrayList<DeliveryItem> lstDeliveryItems) {
        int itemsInserted = 0;

        for(int i = 0; i < lstDeliveryItems.size(); i++){
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_DELIVERY_ID, deliveryID);
            contentValues.put(KEY_DELIVERY_ITEM_ID, lstDeliveryItems.get(i).getDeliveryStockID());
            contentValues.put(KEY_DELIVERY_ITEM_QUANTITY, lstDeliveryItems.get(i).getDeliveryItemQuantity());
            itemsInserted += sqLiteDatabase.insert(DATABASE_DELIVERY_ITEM_TABLE, null, contentValues);
        }
        return itemsInserted;
    }

    //Method retrieves a specific record from the appropriate database
    public Cursor getDeliveryItems(String deliveryID)  {
        Cursor cursor = sqLiteDatabase.query(true, DATABASE_DELIVERY_ITEM_TABLE, new String[] {KEY_DELIVERY_ITEM_ID, KEY_DELIVERY_ITEM_QUANTITY}, KEY_DELIVERY_ID + "='" + deliveryID + "'", null, null, null, null, null);
        return cursor;
    }

    //Method retrieves a specific record from the appropriate database
    public Cursor getDeliveryItem(String deliveryID, String stockID)  {
        Cursor cursor = sqLiteDatabase.query(true, DATABASE_DELIVERY_ITEM_TABLE, new String[] {KEY_DELIVERY_ITEM_ID, KEY_DELIVERY_ITEM_QUANTITY}, KEY_DELIVERY_ID + "='" + deliveryID + "' AND " + KEY_DELIVERY_ITEM_ID + "='" + stockID + "'", null, null, null, null, null);
        return cursor;
    }

    //Method deletes all DeliveryItems associated with a Delivery
    public boolean deleteDeliveryItems(String deliveryID) {
        return sqLiteDatabase.delete(DATABASE_DELIVERY_ITEM_TABLE, KEY_DELIVERY_ID + "='" + deliveryID + "'", null) > 0;
    }

    //Method deletes all DeliveryItems associated with a Delivery
    public boolean deleteDeliveryItemsByStockID(String stockID) {
        return sqLiteDatabase.delete(DATABASE_DELIVERY_ITEM_TABLE, KEY_DELIVERY_ITEM_ID + "='" + stockID + "'", null) > 0;
    }
}