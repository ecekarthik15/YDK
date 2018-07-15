package android.sales.rajesh.com.sales.Utils;

import android.util.Log;

/**
 * Created by Karthik on 1/31/17.
 */

public class Logger {
    private static final String TAG = "Logger";

    private static Logger instance = null;

    private StringBuffer text = null;

    private String requestUrl;
    private String errorMessage;


    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public void setErrorMessage(String errMessage) {
        this.errorMessage = errMessage;
    }

    public String getErrorMessage() {
        if (errorMessage == null) {
            return "No Error catched.";
        }
        return errorMessage;
    }

    /**
     * TODO set to false for production
     */
    private boolean loggingEnabled = true;

    /**
     * no public constructor, singleton
     */
    private Logger() {
        Log.d(TAG,"ctor start");
        text = new StringBuffer();
    }

    /**
     * fetch singleton
     *
     * @return singleton instance
     */
    public static Logger getInstance() {

        if (instance == null) {
            instance = new Logger();
        }

        return instance;
    }

    /**
     * fetch for visual display
     *
     * @return the collected text
     */
    public String getText() {
        return text.toString();
    }

    /**
     * appends the line of text to internal buffer
     *
     * @param line
     *            text to append
     */
    public void log(String line) {

        if (loggingEnabled) {
            text.append(line);
        }

        System.out.println(line);
    }
}
