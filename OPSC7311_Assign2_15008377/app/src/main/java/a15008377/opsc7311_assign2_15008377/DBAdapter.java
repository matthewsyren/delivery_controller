package a15008377.opsc7311_assign2_15008377;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Matthew Syrén on 2017/05/12.
 */

public class DBAdapter {
    //Database declarations
    static final String DATABASE_NAME = "Deliveries.db";
    static final int DATABASE_VERSION = 1;
    static final String KEY_ROWID = "_id";
    static final String DATABASE_CLIENT_TABLE = "clients";
    static final String DATABASE_DELIVERY_TABLE = "deliveries";
    static final String DATABASE_DELIVERY_ITEM_TABLE = "delivery_items";

    //Client table declarations
    static final String KEY_CLIENT_ID = "client_id";
    static final String KEY_CLIENT_NAME = "client_name";
    static final String KEY_CLIENT_EMAIL = "client_email";
    static final String KEY_CLIENT_ADDRESS = "client_address";
    static final String KEY_CLIENT_LATITUDE = "client_latitude";
    static final String KEY_CLIENT_LONGITUDE = "client_longitude";
    static final String DATABASE_CLIENT_CREATE = "create table clients (" + KEY_ROWID + " integer primary key autoincrement, " + KEY_CLIENT_ID + " text unique not null, " + KEY_CLIENT_NAME + " text not null, " + KEY_CLIENT_EMAIL + " text not null, " + KEY_CLIENT_ADDRESS + " text not null, " + KEY_CLIENT_LATITUDE + " decimal(9, 6) not null, " + KEY_CLIENT_LONGITUDE + " decimal(9,6) not null);";

    //Delivery table declarations
    static final String KEY_DELIVERY_ID = "delivery_id";
    static final String KEY_DELIVERY_CLIENT_ID = "delivery_client_id";
    static final String KEY_DELIVERY_DATE = "delivery_date";
    static final String KEY_DELIVERY_COMPLETED = "delivery_completed";
    static final String DATABASE_DELIVERY_CREATE = "create table deliveries (" + KEY_ROWID + " integer primary key autoincrement, " + KEY_DELIVERY_ID + " text unique not null, " + KEY_DELIVERY_CLIENT_ID + " text not null, " + KEY_DELIVERY_DATE + " text not null, " + KEY_DELIVERY_COMPLETED + " int not null);";

    //Delivery_Item table declarations
    static final String KEY_DELIVERY_ITEM_ID = "delivery_item_id";
    static final String KEY_DELIVERY_ITEM_QUANTITY = "delivery_item_quantity";
    static final String DATABASE_DELIVERY_ITEM_CREATE = "create table delivery_items (" + KEY_ROWID + " integer primary key autoincrement, " + KEY_DELIVERY_ID + " text not null, " + KEY_DELIVERY_ITEM_ID + " text not null, " + KEY_DELIVERY_ITEM_QUANTITY + " int not null);";

    //Other Declarations
    final Context context;
    DatabaseHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;

    //Constructor
    public DBAdapter(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    //Method opens a connection to the database
    public DBAdapter open() throws SQLException {
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

    //Method inserts a record to the appropriate table
    public long insertDelivery(Delivery delivery) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DELIVERY_ID, delivery.getDeliveryID());
        contentValues.put(KEY_DELIVERY_CLIENT_ID, delivery.getDeliveryClientID());
        contentValues.put(KEY_DELIVERY_DATE, delivery.getDeliveryDate());
        contentValues.put(KEY_DELIVERY_COMPLETED, delivery.getDeliveryComplete());

        return sqLiteDatabase.insert(DATABASE_DELIVERY_TABLE, null, contentValues);
    }

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

    //Method deletes a record from the appropriate table
    public boolean deleteClient(String clientID) {
        return sqLiteDatabase.delete(DATABASE_CLIENT_TABLE, KEY_CLIENT_ID + "='" + clientID + "'", null) > 0;
    }

    //Method deletes a record from the appropriate table
    public boolean deleteDelivery(String deliveryID) {
        return sqLiteDatabase.delete(DATABASE_DELIVERY_TABLE, KEY_DELIVERY_ID + "='" + deliveryID + "'", null) > 0;
    }

    //Method retrieves all records from the appropriate table
    public Cursor getAllClients() {
        return sqLiteDatabase.query(DATABASE_CLIENT_TABLE, new String[] {KEY_CLIENT_ID, KEY_CLIENT_NAME, KEY_CLIENT_EMAIL, KEY_CLIENT_ADDRESS, KEY_CLIENT_LATITUDE, KEY_CLIENT_LONGITUDE}, null, null, null, null, null);
    }

    //Method retrieves all records from the appropriate table
    public Cursor getAllDeliveries() {
        return sqLiteDatabase.query(DATABASE_DELIVERY_TABLE, new String[] {KEY_DELIVERY_ID, KEY_DELIVERY_CLIENT_ID, KEY_DELIVERY_DATE, KEY_DELIVERY_COMPLETED}, null, null, null, null, null);
    }

    //Method retrieves a specific record from the appropriate database
    public Client getClient(String clientID) throws SQLException {
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

    //Method retrieves all records from the appropriate table
    public Cursor getAllDeliveryItems() {
        return sqLiteDatabase.query(DATABASE_DELIVERY_ITEM_TABLE, new String[] {KEY_DELIVERY_ID, KEY_DELIVERY_ITEM_ID, KEY_DELIVERY_ITEM_QUANTITY}, null, null, null, null, null);
    }

    //Method retrieves a specific record from the appropriate database
    public Cursor getDeliveryItems(String deliveryID) throws SQLException {
        Cursor cursor = sqLiteDatabase.query(true, DATABASE_DELIVERY_ITEM_TABLE, new String[] {KEY_DELIVERY_ITEM_ID, KEY_DELIVERY_ITEM_QUANTITY}, KEY_DELIVERY_ID + "='" + deliveryID + "'", null, null, null, null, null);
        return cursor;
    }

    //Method retrieves a specific record from the appropriate database
    public Cursor getDeliveryItem(String deliveryID, String stockID) throws SQLException {
        Cursor cursor = sqLiteDatabase.query(true, DATABASE_DELIVERY_ITEM_TABLE, new String[] {KEY_DELIVERY_ITEM_ID, KEY_DELIVERY_ITEM_QUANTITY}, KEY_DELIVERY_ID + "='" + deliveryID + "' AND " + KEY_DELIVERY_ITEM_ID + "='" + stockID + "'", null, null, null, null, null);
        return cursor;
    }

    //Method retrieves a specific record from the appropriate database
    public Cursor getDelivery(String deliveryID) throws SQLException {
        Cursor cursor = sqLiteDatabase.query(true, DATABASE_DELIVERY_TABLE, new String[] {KEY_DELIVERY_ID, KEY_DELIVERY_CLIENT_ID, KEY_DELIVERY_DATE, KEY_DELIVERY_COMPLETED}, KEY_DELIVERY_ID + "='" + deliveryID + "'", null, null, null, null, null);
        return cursor;
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

    //Method updates a specific record in the appropriate table
    public boolean updateDelivery(Delivery delivery) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DELIVERY_ID, delivery.getDeliveryID());
        contentValues.put(KEY_DELIVERY_CLIENT_ID, delivery.getDeliveryClientID());
        contentValues.put(KEY_DELIVERY_DATE, delivery.getDeliveryDate());
        contentValues.put(KEY_DELIVERY_COMPLETED, delivery.getDeliveryComplete());

        return sqLiteDatabase.update(DATABASE_DELIVERY_TABLE, contentValues, KEY_DELIVERY_ID + "='" + delivery.getDeliveryID() + "'", null) > 0;
    }

    //Method deletes all DeliveryItems associated with a Delivery
    //Method deletes a record from the appropriate table
    public boolean deleteDeliveryItems(String deliveryID) {
        return sqLiteDatabase.delete(DATABASE_DELIVERY_ITEM_TABLE, KEY_DELIVERY_ID + "='" + deliveryID + "'", null) > 0;
    }
}