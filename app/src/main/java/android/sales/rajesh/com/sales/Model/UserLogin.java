package android.sales.rajesh.com.sales.Model;

import android.database.Cursor;
import android.icu.text.DateFormat;
import android.icu.util.TimeZone;
import android.sales.rajesh.com.sales.Database.DatabaseManager;
import android.sales.rajesh.com.sales.Utils.Utility;
import android.util.Log;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by Karthik on 2/21/17.
 */

public class UserLogin implements Serializable,Comparable<UserLogin> {

    private static final String TAG = UserLogin.class.getSimpleName();

    //CREATE TABLE "UserLogin" ( `userId` INTEGER NOT NULL, `loginDttm` TEXT NOT NULL )
    private Integer userId;
    private String loginDttm;

    public UserLogin(){


        userId = 0;

        String dateAndtime = Utility.getDateAndTime();

        loginDttm = dateAndtime;

    }


    public UserLogin(Integer userId, String loginDttm) {
        this.userId = userId;
        this.loginDttm = loginDttm;
    }



    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLoginDttm() {
        return loginDttm;
    }

    public void setLoginDttm(String loginDttm) {
        this.loginDttm = loginDttm;
    }

    public static String TABLE_NAME = "USERLOGIN";
    public static final String COLUMN_USERLOGIN_USERID = "userId";
    public static final String COLUMN_USERLOGIN_LOGINDTTM = "loginDttm";

    private static String[] keyNames = {
            COLUMN_USERLOGIN_USERID,
            COLUMN_USERLOGIN_LOGINDTTM
    };

    //CREATE TABLE "UserLogin" ( `userId` INTEGER NOT NULL, `loginDttm` TEXT NOT NULL )



    public static final String CREATE_USERLOGIN_TABLE =
            "CREATE TABLE " + TABLE_NAME
                    + " ( "
                    + COLUMN_USERLOGIN_USERID + " INTEGER, "
                    + COLUMN_USERLOGIN_LOGINDTTM + " TEXT )";

    public static final String DROP_USERLOGIN_TABLE =
            "DROP TABLE [ IF EXISTS ] " + TABLE_NAME;


    public void persistData(){

        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put(COLUMN_USERLOGIN_USERID, userId);
        map.put(COLUMN_USERLOGIN_LOGINDTTM, loginDttm);


        DatabaseManager.getInstance(null).insert(TABLE_NAME, map);


    }

    public static UserLogin getLastLoggedInUser(){

        String lastLoginUser = "SELECT * FROM "+TABLE_NAME+" ORDER BY "+COLUMN_USERLOGIN_LOGINDTTM+" DESC LIMIT 1";


        DatabaseManager.getInstance(null).open();
        Cursor cursor = DatabaseManager.getInstance(null).executeRawQuery(lastLoginUser);

        if(cursor == null || cursor.getCount() < 1){
            Log.i(TAG, "Cursor count is null");
            return null;
        }

        UserLogin userLogin = null;

        for (int j = 0; j < cursor.getCount(); j++) {

            cursor.moveToPosition(j);

            userLogin = new UserLogin(cursor.getInt(0), cursor.getString(1));

        }

        return userLogin;


    }

    private void deleteData(int userId){

        DatabaseManager.getInstance(null).open();

        String whereCondn = COLUMN_USERLOGIN_USERID+" = '"+userId+"'";

        DatabaseManager.getInstance(null).deleteRows(TABLE_NAME, whereCondn);



    }


    @Override
    public int compareTo(UserLogin o) {
        return 0;
    }
}
