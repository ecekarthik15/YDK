package android.sales.rajesh.com.sales.Transport;

import android.content.Context;
import android.sales.rajesh.com.sales.Result.ParseResult;
import android.sales.rajesh.com.sales.Parser.JSONParser;
import android.sales.rajesh.com.sales.Utils.Constants;
import android.sales.rajesh.com.sales.Utils.GeneralSettings;
import android.sales.rajesh.com.sales.Utils.Logger;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Karthik on 1/31/17.
 */

public class WebRequestThread extends Thread{
    private static final String TAG = "WebRequestThread";

    private WebdataCallback callback = null;
    private String urlString = null;
    private JSONParser parser = null;
    private String identifier = null;
    private String errorMessage = Constants.NO_ERROR;
    private boolean isCancelled = false;
    private boolean isPost = false;
    private String postString = null;
    private Context context = null;
    private HashMap<String, String> properties = null;
    private boolean isExit = false;
    private Exception exception = null;
    /*
     * By Default AutoRetry is true. It can be turned-off in places like
     * Analytics call
     */
    private boolean shouldAutoRetry = true;

    private boolean shouldRetryOnTimeout = false;
    private static final int TIMEOUT_RETRY_COUNT = 2;

    private boolean shouldRetryOnSAXEx = false;
    private static final int SAX_EX_RETRY_COUNT = 4;

    private Integer threadId;
    // Background thread flag need to check before cancelling the progress
    // dialog
    private boolean isBackgroundThread = false;

    // Notify error message to activity if its true
    private boolean isNotifyError = false;

    HttpURLConnection http = null;

    URL url = null;

    // always verify the host - dont check for certificate
    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    public WebRequestThread(Context ctx, WebdataCallback callback, String url,
                            JSONParser parser, String identifier, boolean isNotifyError,
                            String postString, boolean isPost,
                            HashMap<String, String> properties) {
        this.context = ctx;
        this.callback = callback;
        this.urlString = url;
        this.parser = parser;

        this.identifier = identifier;

        this.isNotifyError = isNotifyError;

        this.postString = postString;
        this.isPost = isPost;
        this.properties = properties;
        this.threadId = GeneralSettings.getInstance().getWebReqThreadIdNumber();
    }

    public void run() {

        Logger.getInstance().setRequestUrl(urlString);
        InputStream is = null;
        ParseResult parseResult = null;
        Log.i("WebRequest", "URL :" + urlString);
        Log.i(TAG, "Web Request Thread," + this.identifier + " ["
                + this.threadId + "] started.");

        int retryCount = 0;

        // do {
        retryCount++;
        shouldRetryOnTimeout = false;

        try {

            Log.i("TIME", "Job starting time:"
                    + new Date(System.currentTimeMillis()));
            System.setProperty("http.keepAlive", "false");
            http = openConnection();
            http = setUpConnectionProperties(http);
            if (isPost) {
                http = setUpHttpPostConnectionProperties(http);
            }

            if (http == null) {
                Log.d(TAG, "note http is null");
                // no point in continuing this thread
                return;
            }

            http.setConnectTimeout(30000);
            http.setReadTimeout(30000);

            http.connect();

            is = http.getInputStream();
            int rCode = http.getResponseCode();

            Log.i(TAG, "Response Code : " + rCode);

            if (parser != null) {
                Log.i(TAG, "Web Request Thread," + this.identifier + "["
                        + this.threadId + "] got the response.");
                String rawResult = readInputStreamAsString(is);
                Log.i(TAG, "Output : " + rawResult);
                parser.doParsing(rawResult);
                parseResult = parser.getparseResult();


            } else {
                String rawResult = readInputStreamAsString(is);
                Log.d(TAG, "WebRequestThread:Parser is null with identifier : "
                        + identifier + " ,result: " + rawResult);
            }

        }

        catch (java.net.SocketException ex) {
            exception = ex;
            shouldRetryOnTimeout = true;
            ex.printStackTrace();
            Logger.getInstance().setErrorMessage(ex.getMessage());
            Log.e(TAG, "caught SocketException[" + ex.getMessage() + "]");
        }

        catch (java.net.SocketTimeoutException ex) {
            exception = ex;
            shouldRetryOnTimeout = true;
            ex.printStackTrace();
            Logger.getInstance().setErrorMessage(ex.getMessage());
            Log
                    .e(TAG, "caught SocketTimeoutException[" + ex.getMessage()
                            + "]");
        } catch (java.net.UnknownHostException ex) {
            exception = ex;
            ex.printStackTrace();
            Logger.getInstance().setErrorMessage(ex.getMessage());
            Log.e(TAG, "caught UnknownHostException[" + ex.getMessage() + "]");
        } catch (Exception e) {
            exception = e;
            e.printStackTrace();
            errorMessage = e.toString();
            Logger.getInstance().setErrorMessage(e.getMessage());
            Log.e(TAG, "Exception in WebRequest Thread:" + e.getMessage());

        } finally {

            try {
                if (is != null) {
                    is.close();
                }

                if (http != null) {
                    http.disconnect();
                }

                if (!isCancelled) {
                    callback.callBack(parseResult, identifier, exception,
                            isBackgroundThread, isNotifyError);
                    exception = null;
                } else {
                    Log.i(TAG, "Web Request Thread," + this.identifier + "["
                            + this.threadId + "] Cancelled.");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        // } while ((shouldAutoRetry && shouldRetryOnTimeout && (retryCount <=
        // TIMEOUT_RETRY_COUNT))
        // || (shouldAutoRetry && shouldRetryOnSAXEx && ((retryCount <=
        // SAX_EX_RETRY_COUNT))));

        callback.webRequestThreadCompleted(isBackgroundThread);
    }

    public static String readInputStreamAsString(InputStream in)
            throws IOException {

        BufferedInputStream bis = new BufferedInputStream(in);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result = bis.read();
        while (result != -1) {
            byte b = (byte) result;
            buf.write(b);
            result = bis.read();
        }
        return buf.toString();
    }

    private HttpURLConnection setUpHttpPostConnectionProperties(
            HttpURLConnection http) {

        // DataOutputStream dos = null;
        BufferedWriter bw = null;
        try {
            // connection.setReadTimeout(6000);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setUseCaches(false);

            http.setRequestMethod("POST");

            // Adding the content-length header to the request
            http.setRequestProperty("Content-Length", ""
                    + postString.getBytes().length);
            http.setRequestProperty("Content-Type", "text/plain");

            bw = new BufferedWriter(new OutputStreamWriter(http
                    .getOutputStream(), "UTF8"));

            bw.write(postString);
            bw.flush();

        } catch (Exception e) {

            e.printStackTrace();
            errorMessage = e.toString();
            Logger.getInstance().setErrorMessage(e.getMessage());
            Log.e(TAG, "Exception in WebRequest Thread [" + e.getMessage()
                    + "]");

        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return http;
    }

    private HttpURLConnection setUpConnectionProperties(HttpURLConnection http) {

        if (http == null) {
            Log.e(TAG, "setUpConnectionProperties: http is null");
            return null;
        }

        return http;
    }

    private HttpURLConnection openConnection() {
        try {
            url = new URL(urlString);
            if (url.getProtocol().toLowerCase().equals("https")) {
                trustEveryone();
                HttpsURLConnection https = (HttpsURLConnection) url
                        .openConnection();
                http = https;
            } else {
                http = (HttpURLConnection) url.openConnection();
            }

        } catch (IOException e) {
            e.printStackTrace();
            errorMessage = e.toString();
            Logger.getInstance().setErrorMessage(e.getMessage());
            System.out.println("Exception in WebRequest Thread:"
                    + e.getMessage());
        }
        return http;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    private void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(DO_NOT_VERIFY);
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[] { new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            } }, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context
                    .getSocketFactory());
        } catch (Exception e) { // should never happen
            e.printStackTrace();
        }
    }

    public String getIdentifier() {
        return identifier;
    }

    public Integer getThreadId() {
        return threadId;
    }

    public void setThreadId(Integer threadId) {
        this.threadId = threadId;
    }

    public void setBackgroundThread(boolean isBackgroundThread) {
        this.isBackgroundThread = isBackgroundThread;
    }

    public boolean isBackgroundThread() {
        return isBackgroundThread;
    }

    public boolean isNotifyError() {
        return isNotifyError;
    }

    public void setNotifyError(boolean isNotifyError) {
        this.isNotifyError = isNotifyError;
    }

    public boolean isShouldAutoRetry() {
        return shouldAutoRetry;
    }

    /*
     * By Default AutoRetry is true. It can be turned-off in places like
     * Analytics call
     */
    public void setShouldAutoRetry(boolean shouldAutoRetry) {
        this.shouldAutoRetry = shouldAutoRetry;
    }

}
