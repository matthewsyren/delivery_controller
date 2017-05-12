package a15008377.opsc7311_assign2_15008377;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Matthew Syrén on 2017/05/12.
 */

public class DBAdapter {
    //Database Declarations
    static final String DATABASE_NAME = "Deliveries.db";
    static final int DATABASE_VERSION = 1;

    //Client Table Declarations
    static final String KEY_ROWID = "_id";
    static final String KEY_CLIENT_NAME = "client_name";
    static final String KEY_CLIENT_EMAIL = "client_email";
    static final String KEY_CLIENT_ADDRESS = "client_address";
    static final String KEY_CLIENT_LATITUDE = "client_latitude";
    static final String KEY_CLIENT_LONGITUDE = "client_longitude";
    static final String DATABASE_CLIENT_TABLE = "clients";
    static final String DATABASE_CLiENT_CREATE = "create table clients (" + KEY_ROWID + " integer primary key autoincrement, " + KEY_CLIENT_NAME + " text not null, " + KEY_CLIENT_EMAIL + " text not null, " + KEY_CLIENT_ADDRESS + " text not null, " + KEY_CLIENT_LATITUDE + " decimal(9, 6) not null, " + KEY_CLIENT_LONGITUDE + " decimal(9,6) not null);";

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
                db.execSQL(DATABASE_CLiENT_CREATE);
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
            onCreate(db);
        }
    }

    //Method inserts a record to the appropriate table
    public long insertClient(String clientName, String clientEmail, String clientAddress, double clientLatitude, double clientLongitude) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CLIENT_NAME, clientName);
        contentValues.put(KEY_CLIENT_EMAIL, clientEmail);
        contentValues.put(KEY_CLIENT_ADDRESS, clientAddress);
        contentValues.put(KEY_CLIENT_LATITUDE, clientLatitude);
        contentValues.put(KEY_CLIENT_LONGITUDE, clientLongitude);

        return sqLiteDatabase.insert(DATABASE_CLIENT_TABLE, null, contentValues);
    }

    //<ethod deletes a record from the appropriate table
    public boolean deleteRecord(long rowId, String tableName) {
        return sqLiteDatabase.delete(tableName, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //Method retrieves all records from the appropriate table
    public Cursor getAllClients() {
        return sqLiteDatabase.query(DATABASE_CLIENT_TABLE, new String[] {KEY_CLIENT_NAME, KEY_CLIENT_EMAIL, KEY_CLIENT_ADDRESS, KEY_CLIENT_LATITUDE, KEY_CLIENT_LONGITUDE}, null, null, null, null, null);
    }

    //Method retrieves a specific record from the appropriate database
    public Cursor getRecord(long rowId, String tableName, String[] columns) throws SQLException {
        Cursor cursor = sqLiteDatabase.query(true, tableName, columns, KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    //Method updates a specific record in the appropriate table
    public boolean updateRecord(long rowId, ContentValues contentValues, String tableName) {
        return sqLiteDatabase.update(tableName, contentValues, KEY_ROWID + "=" + rowId, null) > 0;
    }
}