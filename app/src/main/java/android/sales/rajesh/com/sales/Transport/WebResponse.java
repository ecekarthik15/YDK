package android.sales.rajesh.com.sales.Transport;

import android.sales.rajesh.com.sales.Model.Merchant;
import android.util.Log;

import java.util.Vector;

/**
 * Created by Karthik on 1/31/17.
 */

public class WebResponse {
    private static final String TAG = "WebResponse";

    private static WebResponse _instance = null;

    private Vector<Merchant> merchantsVector;

    private WebResponse() {

        Log.d(TAG, "ctor start");
    }

    public static WebResponse getInstance() {
        if (_instance == null) {
            synchronized (WebResponse.class) {
                if (_instance == null) {
                    _instance = new WebResponse();
                }
            }
        }
        return _instance;
    }

    public Vector<Merchant> getMerchantsVector() {

        return this.merchantsVector;
    }

    public void setMerchantsVector(Vector<Merchant> merchantsVector) {
        this.merchantsVector = merchantsVector;
    }


}
