package android.sales.rajesh.com.sales.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.sales.rajesh.com.sales.Model.Bill;
import android.sales.rajesh.com.sales.Model.BillPayments;
import android.sales.rajesh.com.sales.Model.Merchant;
import android.sales.rajesh.com.sales.Model.UserLocation;
import android.sales.rajesh.com.sales.Model.UserLogin;
import android.sales.rajesh.com.sales.Model.Users;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Karthik on 2/2/17.
 */

public class DatabaseManager {

    private static final String TAG = "DatabaseManager";

    private static final String DATABASE_NAME = "SalesApp";

    private static final int DATABASE_VERSION = 3;

    private static DatabaseManager instance = null;

    private Context context;

    private DatabaseHelper DBHelper;

    public SQLiteDatabase sqlitedb;

    public DatabaseManager(Context ctx) {

        Log.d(TAG, "DatabaseManager");

        // Exists only to defeat instantiation.
        this.context = ctx;
        DBHelper = new DatabaseHelper(context, DATABASE_NAME, null,
                DATABASE_VERSION);
    }

    public static DatabaseManager getInstance(Context ctx) {

        if (instance == null && ctx != null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager(ctx);
                    instance.context = ctx;
                }
            }
        }
        return instance;
    }

    // ---opens the database---
    public DatabaseManager open() throws SQLException {
        sqlitedb = DBHelper.getWritableDatabase();
        return this;
    }

    // ---closes the database---
    public void close() {
        DBHelper.close();
    }

    // ---insert a record into the database---
    public long insert(String table, HashMap<String, Object> data) {
        System.out.println("insert operation");
        ContentValues args = new ContentValues();
        Iterator<?> dataIterator = data.keySet().iterator();
        while (dataIterator.hasNext()) {
            String key = (String) dataIterator.next();
            if(data.get(key) instanceof Integer){
                args.put(key,(Integer) data.get(key));
            }else if(data.get(key) instanceof Double){
                args.put(key,(Double) data.get(key));
            }else if(data.get(key) instanceof Long){
                args.put(key,(Long) data.get(key));
            }else {
                args.put(key,(String) data.get(key));
            }
        }
        return sqlitedb.insert(table, null, args);
    }

    // ---retrieves all from a table---
    public Cursor selectFieldsFrom(String table, String[] columns, String Where) {
        return sqlitedb.query(table, columns, Where, null, null, null, null);
    }

    public Cursor selectFieldsFrom(String table, String[] columns,
                                   String Where, String Order) {
        return sqlitedb.query(table, columns, Where, null, null, null, Order);
    }

    public Cursor executeRawQuery(String sql) {
        return sqlitedb.rawQuery(sql,null);
    }

    public boolean deleteByRowId(String tablename,String column,String rowId){
        return sqlitedb.delete(tablename, column +"=\""+rowId+"\"", null)>0;
    }
    public boolean deleteRows(String tablename,String whereConditon){
        return sqlitedb.delete(tablename, whereConditon, null) > 0;
    }

    public boolean updateByRowId(String tablename,String column,String rowId,ContentValues data){
        return sqlitedb.update(tablename, data, column+"=\""+rowId+"\"", null) > 0;
    }

    // ---deletes a particular record---
    public boolean deleteAll(String table) {
        System.out.println("Deleteing the table");
        return sqlitedb.delete(table, null, null) > 0;
    }

    // ---retrieves a particular row---
    public Cursor selectByRowID(String table, String[] columns, String column,
                                String rowId) throws SQLException {
        Cursor mCursor = sqlitedb.query(true, table, columns, column + "='"
                + rowId + "'", null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    // ---retrieves a particular row---
    public Cursor selectByQuery(String table, String[] columns, String query)
            throws SQLException {
        Cursor mCursor = sqlitedb.query(true, table, columns, query, null,
                null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }



    // ------------------------------------------------------------------------------------------------------------
    // / DB HELPER
    // ------------------------------------------------------------------------------------------------------------

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context, String databaseName,
                       SQLiteDatabase.CursorFactory factory, int databaseVersion) {
            super(context, databaseName, factory, databaseVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            createDB(db);

        }

        private void createDB(SQLiteDatabase db) {
            db.execSQL(Users.CREATE_USERS_TABLE);
            db.execSQL(Merchant.CREATE_MERCHANT_TABLE);
            db.execSQL(Bill.CREATE_BILLS_TABLE);
            db.execSQL(BillPayments.CREATE_BILLPAYMENTS_TABLE);
            db.execSQL(UserLogin.CREATE_USERLOGIN_TABLE);
            db.execSQL(UserLocation.CREATE_USERLOCATION_TABLE);
        }

        private void deleteDB(SQLiteDatabase db){
            db.execSQL(Users.DROP_USERS_TABLE);
            db.execSQL(Merchant.DROP_MERCHANT_TABLE);
            db.execSQL(Bill.DROP_BILLS_TABLE);
            db.execSQL(BillPayments.DROP_BILLPAYMENTS_TABLE);
            db.execSQL(UserLogin.DROP_USERLOGIN_TABLE);
            db.execSQL(UserLocation.DROP_USERLOCATION_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

//            deleteDB(db);
//            createDB(db);

            Log.i("Example",
                    "Upgrading database, this will drop tables and recreate.");

            Log
                    .w("TaskDBAdapter", "Upgrading from version " + oldVersion
                            + " to " + newVersion
                            + ", which will destroy all old data");

            onCreate(db);
        }
    }
}
