package android.sales.rajesh.com.sales.Model;

import android.sales.rajesh.com.sales.Database.DatabaseManager;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Karthik on 2/11/17.
 */

public class Users implements Serializable,Comparable<Users> {


    private boolean IsAuthorized;
    private String msg;
    private Integer userId;
    private String passCode;
    private String name;




    public boolean isAuthorized() {
        return IsAuthorized;
    }

    public void setAuthorized(boolean authorized) {
        this.IsAuthorized = authorized;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPassCode() {
        return passCode;
    }

    public void setPassCode(String passCode) {
        this.passCode = passCode;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    @Override
    public int compareTo(Users o) {
        return 0;
    }



    public static String TABLE_NAME = "USERS";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USER_PASSCODE = "passcode";
    public static final String COLUMN_USER_NAME = "name";


    private static String[] keyNames = {
            COLUMN_USER_ID,
            COLUMN_USER_PASSCODE,
            COLUMN_USER_NAME
    };

    public static final String CREATE_USERS_TABLE =
            "CREATE TABLE " + TABLE_NAME
                    + " ( "
                    + COLUMN_USER_ID + " INTEGER NOT NULL, "
                    + COLUMN_USER_PASSCODE + " VARCHAR NOT NULL, "
//                    + COLUMN_USER_NAME + " VARCHAR NOT NULL, PRIMARY KEY('"+COLUMN_USER_ID+"') ) WITHOUT ROWID";

                    + COLUMN_USER_NAME + " VARCHAR NOT NULL, PRIMARY KEY('"+COLUMN_USER_ID+"') )";

    public static final String DROP_USERS_TABLE =
            "DROP TABLE [ IF EXISTS ] " + TABLE_NAME;


    public void persistData(){

        deleteData(userId);

        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put(COLUMN_USER_ID, userId);
        map.put(COLUMN_USER_PASSCODE, passCode);
        map.put(COLUMN_USER_NAME, name);


        DatabaseManager.getInstance(null).insert(TABLE_NAME, map);


    }

    private void deleteData(int userId){

        DatabaseManager.getInstance(null).open();

        String whereCondn = COLUMN_USER_ID+" = '"+userId+"'";

        DatabaseManager.getInstance(null).deleteRows(TABLE_NAME, whereCondn);



    }
}
