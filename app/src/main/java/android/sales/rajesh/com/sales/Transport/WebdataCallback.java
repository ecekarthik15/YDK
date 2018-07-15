package android.sales.rajesh.com.sales.Transport;

/**
 * Created by Karthik on 1/31/17.
 */

public interface WebdataCallback {
    /**
     * This method will be called by the webRequestThread with the resultData,
     * identifier, and error message if any. All the classes which implement the
     * WebDataCallback should implement this method to get the resultant data of
     * the webRequest.
     *
     * @param obj
     *            - Object with the resultant data of the web request
     * @param identifier
     *            - String identifier to identify the web request. This will be
     *            useful in cases when a class make more than one webRequest at
     *            a time
     * @param exception
     *            - exception returned by the webRequestthread if some error
     *            occurred during web request. If not, it will be null.
     * @param isBackgroundThread
     *            - true if it's background web request
     * @param isNotifyError
     * 			- notify the error message to corresponding activity if the web request fails.
     */
    public void callBack(Object obj, String identifier, Exception exception, boolean isBackgroundThread, boolean isNotifyError);

    /**
     * This method is called when a the web Request thread is completed with or without success.
     * Method is invoked when the thread ends.
     *
     * @param isBackgroundThread
     * 			 - true if it is a background web request
     */
    public void webRequestThreadCompleted(boolean isBackgroundThread);
}
