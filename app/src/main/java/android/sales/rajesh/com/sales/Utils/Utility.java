package android.sales.rajesh.com.sales.Utils;

import android.content.Context;
import android.sales.rajesh.com.salesapp.R;
import android.view.View;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Karthik on 2/12/17.
 */

public class Utility {



    public static String getFormattedCurrency(String amount){


        double totalAmt = Double.parseDouble(amount);

        String formatted = NumberFormat.getCurrencyInstance(new Locale("en", "in")).format((totalAmt));

        return  formatted;
    }


    public static String replaceCharecterAt(String sourceString ,int postion, char replaceChar){

        String inputString = sourceString;
        String outPutString = "";
        char[] inputStringChars = inputString.toCharArray();
        inputStringChars[postion] = replaceChar;
        outPutString = String.valueOf(inputStringChars);

        outPutString = outPutString.replace(" ","");

        return outPutString;
    }



    public static int countMatches(String input ,char match){

        int charCount = 0;
        for(int i =0 ; i<input.length(); i++){
            if(input.charAt(i) == match){
                charCount++;
            }
        }

        return charCount;

    }



    public static void setRoundedBg(View ctx){

        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            ctx.setBackgroundDrawable( ctx.getResources().getDrawable(R.drawable.rounded_bg) );
        } else {
            ctx.setBackground( ctx.getResources().getDrawable(R.drawable.rounded_bg));
        }
    }

    public static String getDateAndTime(){

        Date myDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(java.util.TimeZone.getDefault().getTimeZone("UTC"));
        calendar.setTime(myDate);
        Date time = calendar.getTime();
        SimpleDateFormat outputFmt = new SimpleDateFormat("MMM dd, yyy h:mm:ss a zz");
        String dateAsString = outputFmt.format(time);

        return dateAsString;
    }


    public static String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy");
        String dateStr = mdformat.format(calendar.getTime());

        return dateStr;
    }

    public static Long  getCurrentUTCTime(){


        Date myDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(java.util.TimeZone.getDefault().getTimeZone("UTC"));
        calendar.setTime(myDate);
        long time = calendar.getTimeInMillis();

        return time;

    }


    public static  String getDate(Long milliseconds ,String format){

        SimpleDateFormat formatter = new SimpleDateFormat(format);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        return formatter.format(calendar.getTime());

    }
}
