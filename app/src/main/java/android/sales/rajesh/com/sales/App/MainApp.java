package android.sales.rajesh.com.sales.App;

import android.app.Activity;
import android.app.Application;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.sales.rajesh.com.sales.Database.DatabaseManager;
import android.sales.rajesh.com.sales.Model.Merchant;
import android.sales.rajesh.com.sales.Utils.GeneralSettings;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.util.Properties;
import java.util.Vector;

/**
 * Created by Karthik on 1/31/17.
 */

public class MainApp extends Application{
    private static final String TAG = "MainApp";

    public SharedPreferences sharedData;
    public SharedPreferences.Editor SDeditor;


    public static final int CONNECTED 		=  1;
    public static final int DISCONNECTED  	= -1;
    public static final int CONNECTING 		=  0;
    public static final int INDETERMINANT 	= -2;


    public static int CONNECTIVITY_STATUS = INDETERMINANT;

    private Vector<Merchant> merchantsVector = new Vector<Merchant>();

    private String sessionId = "";
    private String rosterVersion = "";

    public Activity currentActivity;

    DatabaseManager databaseManager;

    public Properties globals;

    public Context getContext() {

        return getApplicationContext();
    }


    public Context context;

    public static boolean isAppRegistered = false;


    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG,"Application Oncreate Called");
        databaseManager = DatabaseManager.getInstance(getApplicationContext());
    }

    public MainApp() {
        Log.d(TAG, "ctor start");
    }

    public void messages(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void showConnectivityStatus( String msg ) {
        Toast toast = Toast.makeText( this, msg, Toast.LENGTH_LONG );
        toast.setGravity( Gravity.TOP | Gravity.RIGHT , 12, 40 );
        toast.show();
    }

    public void showConnectivityStatusShort( String msg ) {
        Toast toast = Toast.makeText( this, msg, Toast.LENGTH_SHORT );
        toast.setGravity( Gravity.TOP | Gravity.RIGHT , 12, 40 );
        toast.show();
    }

    public void log(String tag, String msg) {
        if (GeneralSettings.getInstance().isDevMode()) {
            Log.d(tag, msg);
        }
    }

    public synchronized void setSessionId( final String sessionId ) {
        this.sessionId = sessionId ;
    }

    public synchronized void setRosterVersion( final String rosterVersion ) {
        this.rosterVersion = rosterVersion;
    }

    public synchronized void setMerchants( Vector<Merchant> merchants  ) {
        if ( merchants == null )
            return;
        this.merchantsVector.removeAllElements();
        this.merchantsVector.addAll( merchants );
    }

    public Vector<Merchant> getMerchants() {
        return this.merchantsVector;
    }

}
