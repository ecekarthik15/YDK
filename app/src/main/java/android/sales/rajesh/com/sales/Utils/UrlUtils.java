package android.sales.rajesh.com.sales.Utils;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Karthik on 1/31/17.
 */

public class UrlUtils {
    private static final String TAG = "URLUtils";

    private static HashMap<String, String> property = new HashMap<String, String>();

    public static HashMap<String, String> getDefaultUrlProperties() {

        property.clear();
        property.put("Accept-Encoding", Constants.ACCEPT_ENCODING);

        Log.d(TAG, "getDefaultUrlProperties()");

        return property;
    }

    public static HashMap<String, String> gethttpFormPostProperties() {
        property.clear();
        property.put("Accept-Encoding", Constants.ACCEPT_ENCODING);
        property.put("Content-type", Constants.FORM_CONTENT_TYPE);

        return property;
    }

    public static HashMap<String, String> gethttpXmlPostProperties() {
        property.clear();
        property.put("Content-type", Constants.XML_CONTENT_TYPE);

        return property;
    }


    public static String constructMerchantsUrl() {

        String message = "https://kkannan.rscdevtb3.com/ws/v4/prodea/subscriber/network";

        return message;
    }


    private static String removeUnwantedPhoneCharacters(String number){

        number = number.replace("-","");
        number = number.replace("(","");
        number = number.replace(")","");

        return number;
    }


    private static String removeUnwantedStrings(String message) {

        message = message.replace("\"[", "[");
        message = message.replace("]\"", "]");
        message = message.replace("\\", "");
        message = message.replace("]\",[", "],[");
        message = message.replace("]\"]", "]]");

        return message;

    }


    private static String removeCaps(String str) {

        str = str.replace("^", "");
        str = str.replace("%", "");


        return str;

    }

}
