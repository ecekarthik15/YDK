package android.sales.rajesh.com.sales.Model;

import android.sales.rajesh.com.sales.Database.DatabaseManager;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Karthik on 2/21/17.
 */

public class UserLocation implements Serializable,Comparable<UserLocation> {


    private static final String TAG = UserLocation.class.getSimpleName();

    public static final String USER_LOCATION_DISPLAY_NAME_LOGGEDIN = "logged in";
    public static final String USER_LOCATION_DISPLAY_NAME_VISITED = "visited";




    private Integer userId;
    private String latitude;
    private String longitude;
    private String displayName;
    private String recordedAt;
    private Integer merchantId;


    public UserLocation() {

        this.userId = 0;
        this.latitude = "";
        this.displayName = "";
        this.longitude = "";
        this.recordedAt = "";
        this.merchantId = 0;

    }

    public UserLocation(Integer userId, String latitude, String displayName, String longitude, String recordedAt, Integer merchantId) {
        this.userId = userId;
        this.latitude = latitude;
        this.displayName = displayName;
        this.longitude = longitude;
        this.recordedAt = recordedAt;
        this.merchantId = merchantId;
    }



    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(String recordedAt) {
        this.recordedAt = recordedAt;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

//CREATE TABLE "UserLocation" ( `userId` INTEGER NOT NULL, `latLong` TEXT NOT NULL,
// `displayName` TEXT, `recordedAt` TEXT, `merchantId` INTEGER )


    public static String TABLE_NAME = "USERLOCATION";
    public static final String COLUMN_USERLOCATION_USERID = "userId";
    public static final String COLUMN_USERLOCATION_LATITUDE = "latitude";
    public static final String COLUMN_USERLOCATION_LONGITUDE = "longitude";
    public static final String COLUMN_USERLOCATION_DISPLAYNAME = "displayName";
    public static final String COLUMN_USERLOCATION_RECORDEDAT = "recordedAt";
    public static final String COLUMN_USERLOCATION_MERCHANTID = "merchantId";

    private static String[] keyNames = {
            COLUMN_USERLOCATION_USERID,
            COLUMN_USERLOCATION_LATITUDE,
            COLUMN_USERLOCATION_LONGITUDE,
            COLUMN_USERLOCATION_DISPLAYNAME,
            COLUMN_USERLOCATION_RECORDEDAT,
            COLUMN_USERLOCATION_MERCHANTID

    };

    public static final String CREATE_USERLOCATION_TABLE =
            "CREATE TABLE " + TABLE_NAME
                    + " ( "
                    + COLUMN_USERLOCATION_USERID + " INTEGER NOT NULL, "
                    + COLUMN_USERLOCATION_LATITUDE + " TEXT NOT NULL, "
                    + COLUMN_USERLOCATION_LONGITUDE + " TEXT NOT NULL, "
                    + COLUMN_USERLOCATION_DISPLAYNAME + " TEXT, "
                    + COLUMN_USERLOCATION_RECORDEDAT + " TEXT, "
                    + COLUMN_USERLOCATION_MERCHANTID + " TEXT )";

    public static final String DROP_USERLOCATION_TABLE =
            "DROP TABLE [ IF EXISTS ] " + TABLE_NAME;

    public void persistData(){

        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put(COLUMN_USERLOCATION_USERID, userId);
        map.put(COLUMN_USERLOCATION_LATITUDE, latitude);
        map.put(COLUMN_USERLOCATION_LONGITUDE, longitude);
        map.put(COLUMN_USERLOCATION_DISPLAYNAME, displayName);
        map.put(COLUMN_USERLOCATION_RECORDEDAT, recordedAt);
        map.put(COLUMN_USERLOCATION_MERCHANTID, merchantId);



        DatabaseManager.getInstance(null).insert(TABLE_NAME, map);


    }

    private void deleteData(int userId){

        DatabaseManager.getInstance(null).open();

        String whereCondn = COLUMN_USERLOCATION_USERID+" = '"+userId+"'";

        DatabaseManager.getInstance(null).deleteRows(TABLE_NAME, whereCondn);



    }





    @Override
    public int compareTo(UserLocation o) {
        return 0;
    }
}
