package android.sales.rajesh.com.sales.Core;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.sales.rajesh.com.sales.App.MainApp;
import android.sales.rajesh.com.sales.Utils.Constants;
import android.sales.rajesh.com.salesapp.R;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Karthik on 1/31/17.
 */

public abstract class CoreActivity extends AppCompatActivity {

    private static final String TAG = "CoreActivity";
    public static final int ERROR_STATUS = 0;
    public static final int SUCCESS_STATUS = 1;
    public static final int LOAD_PRODUCT_DETAILS = 3;


    public static final int RESULT_CODE_FINISH_ACTIVITY = 1;


    public MainApp app;

    public static final String MYACC_LOGIN = "my_account_login";

    String fromGroActivity = "";

    public abstract void loadNext(int code);

    public abstract void loadPrev();

    protected Bundle bundle;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "key pressed.");

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            loadPrev();
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate() with savedInstanceState");

        app = (MainApp) getApplication();
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // startTimerShedule(1);

        if (this.getIntent().getExtras() != null) {
            bundle = this.getIntent().getExtras();
        } else {
            bundle = null;
        }

    }

    /**
     * sends analytics data when the app gets closed.
     */

    /**
     * start new activity in ActivityGroup which corresponds to TabIndex
     *
     * @param tabIndex
     * @param destClass
     * @param b
     *
     */

    /**
     * To show the Alert message
     *
     * @param title
     * @param message
     */
    protected void showAlertMessage(String title, String message, int type) {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        switch (type) {
            case Constants.ALERT_WITH_CLOSE_BUTTON:
                alertDialog.setButton("Close",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                System.exit(0);
                                return;
                            }
                        });
                break;
            case Constants.ALERT_WITH_OK_BUTTON:
                alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                break;
            default:
                break;
        }

        alertDialog.show();
    }

    public void showMessage(String message){

        Message msg = new Message();
        msg.obj = message;

        showMessageHandler.sendMessage(msg);

    }

    private Handler showMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String message = (String) msg.obj;
            Toast.makeText(CoreActivity.this, message, Toast.LENGTH_SHORT).show();
            super.handleMessage(msg);

        }
    };

    @Override
    public void startActivity(Intent intent) {

        startActivityForResult(intent,1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_CODE_FINISH_ACTIVITY && !isRootActivity()){

            finish();

        }
    }

    public boolean isRootActivity(){

        return false;

    }

    AlertDialog alertDialog = null;

    /**
     * minimize the App
     */
    protected void minimizeApp() {
        showAlertMessage("SalesApp","Do you really want to exit now?",Constants.ALERT_WITH_CLOSE_BUTTON);
        return;

    }



    /**
     *Display virtual(soft) keypad on screen
     */
    protected void showVirtualKeypad() {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                InputMethodManager m = (InputMethodManager) CoreActivity.this
                        .getSystemService(Context.INPUT_METHOD_SERVICE);

                if (m != null) {
                    m.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
                }
            }

        }, 200);

    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        killCustomProgressDialog();
    }

    private void killCustomProgressDialog() {

        if (alertDialog != null) {
            alertDialog.dismiss();
        }

    }


    protected boolean getConnectivityStatus() {
//
//
//        ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//        boolean connected = false;
//        if (connectivity != null) {
//            NetworkInfo info = connectivity.getActiveNetworkInfo();
//            if (info != null) {
//                connected = true;
//            }
//        }
        return isNetworkAvailable() ;

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
