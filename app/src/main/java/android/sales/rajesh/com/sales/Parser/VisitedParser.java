package android.sales.rajesh.com.sales.Parser;

import android.sales.rajesh.com.sales.Core.CoreActivity;
import android.sales.rajesh.com.sales.Result.ParseResult;
import android.sales.rajesh.com.sales.Result.VisitedParseResult;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by Karthik on 7/17/18.
 */

public class VisitedParser extends JSONParser  {

    private String TAG = "LoginParser";

    VisitedParseResult result;
    public VisitedParser( CoreActivity activity ) {
        this.activity = activity;
    }

    @Override
    public void doParsing(String rawResult) {

        result = new VisitedParseResult();

        if (rawResult != null) {
            JSONTokener tokener = new JSONTokener(rawResult);
            result.setRawResult(rawResult);


            try {
                JSONObject jsnObj = new JSONObject(tokener);

                if (jsnObj != null) {


                    if (jsnObj.has("issuccess")) {
                        result.setSuccess(jsnObj.getBoolean("issuccess"));

                    }

                    if (jsnObj.has("msg")) {
                        result.setMessage(jsnObj.getString("msg"));

                    }


                }


            } catch (JSONException exception) {

                Log.d(TAG, "Parsing Error ==================== " + exception.getMessage());

            }


        }
    }

    @Override
    public ParseResult getparseResult() {
        return result;
    }
}
