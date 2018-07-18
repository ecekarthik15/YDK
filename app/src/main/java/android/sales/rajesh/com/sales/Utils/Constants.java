package android.sales.rajesh.com.sales.Utils;

import android.location.Location;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Karthik on 1/31/17.
 */

public class Constants {
    public static final String TAG = "Constants";

    public static final int SPLASH_DISPLAY_LENGTH = 2000;

    public static String SERVER_URL = null;


    public static String NO_ERROR = "No error";
    public static final String CONNECTION_TIMED_OUT_ERROR = "Connection timed out";

    public static final int ALERT_WITH_OK_BUTTON 				= 0;
    public static final int ALERT_WITH_CLOSE_BUTTON 			= 1;
    public static final int SHOW_TOAST 							= 2;
    public static final int DISMISS_DIALOG 						= 3;
    public static final int SHOW_CONNECTIVITY_TOAST 			= 4;
    public static final int SHOW_NO_CONNECTIVITY_WITH_EXIT 		= 5;

    public static final float UPDATE_TIME_VARIATION_FACTOR 		= 0.19000f;



    public static String USER_NAME = "UserName";

    public static String XML_CONTENT_TYPE = "application/xml; charset=utf-8";
    public static String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded; charset=utf-8";
    public static String ACCEPT_ENCODING = "gzip";

    public static final String GETTING_LOGIN_REQUEST = "Sign In";

    public static final String GETTING_MERCHANT_REQUEST = "Getting Customers";

    public static final String OFFLINE_MODE = "offline mode";

    public static final String VISITED_MERCHANT_REQUEST = "Visited Merchant";


    public static final String API_KEY = "A56R887L0";




    public static final String REGISTERED = "REGISTERED";

//http://www.ydkoffice.com/mobile/ValidateEmployee?key=A56R887L0&Passcode=78906
    public static final String SALES_SERVER_URL = "http://www.ydkoffice.com/mobile";

    public static final String SALES_LOGIN_URL = "/ValidateEmployee?key="+API_KEY+"&PassCode=";

    public static final String SALES_MERCHANTS_URL = "/GetDetails?key="+API_KEY+"&EmpId=";

    public static final String SUBMIT_BILLS_URL = "/GetDetails?key="+API_KEY+"&EmpId=";

    public static final String VISITED_URL = "/SaveCustomerVisit?key="+API_KEY+"&CustomerVisit=";

    public static final String DEFAULT_LOGIN_ERROR = "Login failed";

    public static final String CHOOSE_CITY_HEADER = "Choose City";
    public static final String CHOOSE_DISTRICT_HEADER = "Choose District";

    public static final String CHOOSE_PRINTER_HEADER = "Choose Printer";






    public static final int SHOW_SPLASH 		= 	-2;




    public static final boolean DEBUG = true;

    public static String getLoginUrl(String passcode) {

        String url = SALES_SERVER_URL+SALES_LOGIN_URL+passcode;

        Log.e(TAG,"getLoginUrl : "+url);

        return url;

    }


    public static String getMerchantsUrl(int employeeId) {

        String url = SALES_SERVER_URL+SALES_MERCHANTS_URL+employeeId;

        return url;

    }

    public static String getVisitedURL(Location location, int empId ,int merchantId) {

        DateFormat df = DateFormat.getTimeInstance();
        df.setTimeZone(TimeZone.getTimeZone("gmt"));
        String gmtTime = df.format(new Date());
        String jsonString = "";

//        jsonString = "\\{\"cid\":"+merchantId+"," +
//                "\"eid\":" + empId + ","+
//                "\"la\":" + location.getLatitude() + ","+
//                "\"lo\":" + location.getLongitude() + ","+
//                "\"DateTime\":" + gmtTime +
//    "\\}";

//        String url = SALES_SERVER_URL+VISITED_URL+jsonString;


        try {
            jsonString = new JSONObject()
                    .put("cid", merchantId+"")
                    .put("eid", empId+"")
                    .put("la", location.getLatitude()+"")
                    .put("lo", location.getLongitude()+"")
                    .put("DateTime", gmtTime).toString();
        }catch (JSONException e){
            e.printStackTrace();
        }

//        String message = object.toString();
        jsonString = jsonString.replace("\"{", "{");
        jsonString = jsonString.replace("}\"", "}");
        jsonString = jsonString.replace("\\", "");

        try {
            jsonString = URLEncoder.encode(jsonString, "utf-8");
        }catch (Exception e){
            e.printStackTrace();
        }

        String url = SALES_SERVER_URL+VISITED_URL+jsonString;



        Log.e("Visited","Visited URL : "+url);

        return url;

    }
}
