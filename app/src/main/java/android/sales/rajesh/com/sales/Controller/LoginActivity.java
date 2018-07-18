package android.sales.rajesh.com.sales.Controller;

import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.sales.rajesh.com.sales.Core.WebCallableCoreActivity;
import android.sales.rajesh.com.sales.Model.UserLocation;
import android.sales.rajesh.com.sales.Model.UserLogin;
import android.sales.rajesh.com.sales.Model.Users;
import android.sales.rajesh.com.sales.Parser.LoginParser;
import android.sales.rajesh.com.sales.Result.LoginParseResult;
import android.sales.rajesh.com.sales.Result.ParseResult;
import android.sales.rajesh.com.sales.Utils.Constants;
import android.sales.rajesh.com.sales.Utils.LocationHelper;
import android.sales.rajesh.com.sales.Utils.Utility;
import android.sales.rajesh.com.salesapp.R;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends WebCallableCoreActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    protected static final int LOAD_MERCHANTSLIST_ACTIVITY = 1;
    protected static final int SHOW_LOGIN_ERROR = 2;

    private Button submitBtn;

    private TextView passcodeHintTV;
    private EditText passwordET;

    private String msgFromServer;

    private String passCode;

    Users users;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);


        TextView titleView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        View homeBt = (View) toolbar.findViewById(R.id.toolbar_home_BT);
        View rightBt = (View) toolbar.findViewById(R.id.toolbar_right_action_Bt);

        rightBt.setVisibility(View.GONE);
        homeBt.setVisibility(View.GONE);


        titleView.setText(R.string.app_name);

        submitBtn = (Button) findViewById(R.id.login_submit_Btn);
        passcodeHintTV = (TextView) findViewById(R.id.login_username_ET);
        passwordET = (EditText) findViewById(R.id.login_password_ET);



//        passwordET.setText("saasf");

//        loginValidationMessage();

        submitBtn.setOnClickListener(this);

    }




    private void loginValidationMessage(){

        String passcodeStr = passwordET.getText().toString();

        this.passCode = passcodeStr;

        if(passcodeStr != null && passcodeStr.length() > 0){
            makeWebRequest(this, Constants.getLoginUrl(passcodeStr),
                    new LoginParser(this), Constants.GETTING_LOGIN_REQUEST,
                    false, "", false, false);
        }else{
            showAlertMessage("SalesApp","Please enter your Passcode", Constants.ALERT_WITH_OK_BUTTON);
        }

    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_MERCHANTSLIST_ACTIVITY:
                    // finish();
                    storeLocationOnsuccess();

                    Intent gotoMerchantActivity = new Intent(LoginActivity.this,
                            MerchantsListActivity.class);
                    gotoMerchantActivity.putExtra("user", users);

                    startActivity(gotoMerchantActivity);

                    break;

                case SHOW_LOGIN_ERROR:
                    // finish();
                    showAlertMessage("SalesApp","Please enter a valid Passcode", Constants.ALERT_WITH_OK_BUTTON);

                    break;
            }

        }
    };

    //Loads home activity
    public void loadNext(int code) {
        switch (code) {
            case LOAD_MERCHANTSLIST_ACTIVITY:
                //finish();
                handler.sendEmptyMessage(LOAD_MERCHANTSLIST_ACTIVITY);
                break;
        }
    }


    @Override
    public void loadPrev() {
        minimizeApp();

    }


    //onClick listener for Submit button
    @Override
    public void onClick(View v) {

        if (v.getId() == submitBtn.getId()) {

            loginValidationMessage();

        }
    }

    @Override
    protected int useResponseData(ParseResult result, String identifier) {


        LoginParseResult loginResult = (LoginParseResult) result;

        this.users = loginResult.getUsers();

        stopLoading();


        if(this.users != null){

            if(this.users.isAuthorized() == true) {

                finish();

                this.users.setPassCode(this.passCode);
                this.users.persistData();

                UserLogin userLogin = new UserLogin();
                userLogin.setUserId(this.users.getUserId());

                userLogin.persistData();



                return LOAD_MERCHANTSLIST_ACTIVITY;
            }else {

                handler.sendEmptyMessage(SHOW_LOGIN_ERROR);
            }

        }

        stopLoading();



        return -1;



    }

    void storeLocationOnsuccess(){

        LocationHelper locationHelper = new LocationHelper();

        locationHelper.getLocation(this, new LocationHelper.LocationResult() {
            @Override
            public void gotLocation(Location location) {

                Location currentLocation = location;

//                UserLocation loginLocation = new UserLocation();
//
//                loginLocation.setUserId(users.getUserId());
//                loginLocation.setLatitude(currentLocation.getLatitude()+"");
//                loginLocation.setLongitude(currentLocation.getLongitude()+"");
//                loginLocation.setRecordedAt(Utility.getDateAndTime());
//                loginLocation.setDisplayName(UserLocation.USER_LOCATION_DISPLAY_NAME_LOGGEDIN);

//                loginLocation.persistData();




            }
        },this);

    }

    @Override
    protected void handleTransportException(Exception ex) {

    }


}
