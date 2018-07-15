package android.sales.rajesh.com.sales.Parser;

import android.sales.rajesh.com.sales.Core.CoreActivity;
import android.sales.rajesh.com.sales.Model.Users;
import android.sales.rajesh.com.sales.Result.LoginParseResult;
import android.sales.rajesh.com.sales.Result.ParseResult;
import android.sales.rajesh.com.sales.Utils.Constants;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by Karthik on 2/11/17.
 */

public class LoginParser extends JSONParser {

    private String TAG = "LoginParser";

    LoginParseResult result;

    public LoginParser( CoreActivity activity ) {
        this.activity = activity;
    }


    @Override
    public void doParsing(String rawResult) {

        result = new LoginParseResult();

        if (rawResult != null) {
            JSONTokener tokener = new JSONTokener(rawResult);
            result.setRawResult(rawResult);


            try{
                JSONObject jsnObj = new JSONObject(tokener);

                Users users = new Users();

                if(jsnObj != null){


                    if(jsnObj.has("isauthorized")){
                        users.setAuthorized(jsnObj.getBoolean("isauthorized"));

                    }

                    users.setMsg(Constants.DEFAULT_LOGIN_ERROR);

                    if(jsnObj.has("msg")){
                        users.setMsg(jsnObj.getString("msg"));

                    }

                    if(jsnObj.has("eid")){
                        users.setUserId(jsnObj.getInt("eid"));

                    }

                    if(jsnObj.has("ename")){
                        users.setName(jsnObj.getString("ename"));

                    }

                }

                result.setUsers(users);


            }catch (JSONException exception){

                Log.d(TAG,"Parsing Error ==================== "+exception.getMessage());

            }

        }

    }

    @Override
    public ParseResult getparseResult() {
        return result;
    }
}
