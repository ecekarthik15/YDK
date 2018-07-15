package android.sales.rajesh.com.sales.Parser;

import android.sales.rajesh.com.sales.App.MainApp;
import android.sales.rajesh.com.sales.Core.CoreActivity;
import android.sales.rajesh.com.sales.Result.ParseResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Karthik on 1/31/17.
 */

public abstract  class JSONParser {
    protected MainApp appInstance ;

    protected CoreActivity activity ;

    public abstract void doParsing(String rawResult);

    public abstract ParseResult getparseResult();

    protected boolean processMerchants(final JSONObject jsnObj, ParseResult result )throws JSONException {
        boolean hasMerchants = false;

        return hasMerchants;
    }
}
