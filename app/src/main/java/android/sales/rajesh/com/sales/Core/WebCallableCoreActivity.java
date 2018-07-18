package android.sales.rajesh.com.sales.Core;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.sales.rajesh.com.sales.Result.ParseResult;
import android.sales.rajesh.com.sales.Parser.JSONParser;
import android.sales.rajesh.com.sales.Transport.WebRequestThread;
import android.sales.rajesh.com.sales.Transport.WebdataCallback;
import android.sales.rajesh.com.sales.Utils.Constants;
import android.sales.rajesh.com.salesapp.R;
import android.util.Log;

import java.util.Vector;

/**
 * Created by Karthik on 1/31/17.
 */

public abstract class WebCallableCoreActivity extends CoreActivity implements WebdataCallback, DialogInterface.OnCancelListener {

    String errMsg;
    protected static ProgressDialog pd = null;
    int code = 0;
    WebRequestThread newRequest = null;
    private String TAG = "WebCallableCoreActivity";
    private static boolean isShowNetworkAvailabilityError = true;

    protected static final int SHOW_LOADING = 0;
    protected static final int STOP_LOADING = 1;



    protected Vector<WebRequestThread> webThreads = new Vector<WebRequestThread>();

    public abstract void loadNext(int code);

    public void init() {
        if ( pd != null ){
            loadingHandler.sendEmptyMessage(STOP_LOADING);
        }
        pd = new ProgressDialog(this);
        pd.setCancelable(true);
        pd.setOnCancelListener(this);
    }

    protected void makeWebRequest(Context context, String url,
                                  JSONParser parser, String identifier, boolean isNotifyError,
                                  String postString, boolean isPost, boolean background) {

        Log.d(TAG, " Request coming in ");

        CoreActivity coreActivity = (CoreActivity)context;

        if(((CoreActivity) context).getConnectivityStatus()) {
            if ( isShowNetworkAvailabilityError )
                ErrorHandler.sendEmptyMessage( Constants.SHOW_CONNECTIVITY_TOAST );
            isShowNetworkAvailabilityError = false;

        }
        else
            isShowNetworkAvailabilityError = true;

        WebRequestThread newWebRequest = new WebRequestThread(context, this,
                url, parser, identifier, isNotifyError, postString, isPost,
                null);

        webThreads.add(newWebRequest);

        if ( !background )
            init();

        if (!background && pd != null && !pd.isShowing()) {

            if(identifier.equals(Constants.VISITED_MERCHANT_REQUEST)){
                pd.setMessage("Submitting");
                loadingHandler.sendEmptyMessage(SHOW_LOADING);

            }else {
                pd.setMessage(identifier);
                loadingHandler.sendEmptyMessage(SHOW_LOADING);
            }

        }
        newWebRequest.start();
    }

    @Override
    public void callBack(Object obj, String identifier, Exception exception,
                         boolean isBackgroundThread, boolean isNotifyError) {

        Log.i(TAG, "call back called");
        // TODO Auto-generated method stub



        if (exception != null) {
            if (pd != null ) {
                Log.d( TAG, " Dialog is now dismissed ");
                ErrorHandler.sendEmptyMessage( Constants.DISMISS_DIALOG );
            }

            handleTransportException( exception);
            if (identifier.equals(Constants.GETTING_MERCHANT_REQUEST)) {
                errMsg = getString(R.string.no_connectivity_alert_msg);
                if (isShowNetworkAvailabilityError) {
                    ErrorHandler.sendEmptyMessage(Constants.SHOW_TOAST);
                    isShowNetworkAvailabilityError = false;
                }
                handler.sendEmptyMessage(Constants.ALERT_WITH_OK_BUTTON);
                code = useResponseData((ParseResult) obj,
                        Constants.OFFLINE_MODE);

                Log.i(TAG, "Offline contactResponse");
                // }
            } else if (isShowNetworkAvailabilityError) {
                Log.i(TAG, "NO response");
                isShowNetworkAvailabilityError = false;
                ErrorHandler.sendEmptyMessage(Constants.SHOW_TOAST);


            }
        } else {

            Log.i(TAG, "got response");
//            ErrorHandler.sendEmptyMessage(Constants.DISMISS_DIALOG);
            code = useResponseData((ParseResult) obj, identifier);
            handler.sendEmptyMessage(Constants.ALERT_WITH_OK_BUTTON);
        }
    }



    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            loadNext(code);
            super.handleMessage(msg);
        }
    };

    private Handler ErrorHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if ( pd != null && pd.isShowing()){
                loadingHandler.sendEmptyMessage(STOP_LOADING);
            }

            switch (msg.what) {
                case Constants.ALERT_WITH_OK_BUTTON: {
                    showAlertMessage(
                            getString(R.string.no_connectivity_alert_title),
                            errMsg, Constants.ALERT_WITH_CLOSE_BUTTON);
                    break;
                }
                case Constants.SHOW_TOAST: {
                    app.messages(getString(R.string.no_connectivity_alert_msg));
                    break;
                }
                case Constants.SHOW_CONNECTIVITY_TOAST: {
                    app.showConnectivityStatusShort( getString(R.string.connectivity_unavailable));
                    break;
                }
                default: {
                    break;
                }
            }
            super.handleMessage(msg);
        }
    };

    public void onCancel(android.content.DialogInterface arg0) {

        for (int i = 0; i < webThreads.size(); i++) {
            WebRequestThread webRequest = webThreads.get(i);

            if (!webRequest.isBackgroundThread()) {

                webRequest.setCancelled(true);
                if (webRequest.isNotifyError()) {
                    code = useResponseData(null, webRequest.getIdentifier());
                    handler.sendEmptyMessage(code);
                }
            }
        }
    };

    private Handler loadingHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "handler loadNext[" + msg.what + "]");

            switch (msg.what){
                case SHOW_LOADING:
                    pd.show();
                    break;

                case STOP_LOADING:
                    pd.dismiss();

                    break;

                default:
                    break;

            }
            super.handleMessage(msg);

        }
    };

    public void showLoading(){
        loadingHandler.sendEmptyMessage(SHOW_LOADING);
    }

    public void stopLoading(){
        loadingHandler.sendEmptyMessage(STOP_LOADING);


    }

    @Override
    public void webRequestThreadCompleted(boolean isBackgroundThread) {
        // TODO Auto-generated method stub

    }

    protected abstract int useResponseData(ParseResult result, String identifier);

    protected abstract void handleTransportException( Exception ex) ;


}
