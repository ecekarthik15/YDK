package android.sales.rajesh.com.sales.App;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.sales.rajesh.com.sales.Controller.LoginActivity;
import android.sales.rajesh.com.sales.Core.CoreActivity;
import android.sales.rajesh.com.sales.Utils.Constants;
import android.sales.rajesh.com.salesapp.R;

/**
 * Created by Karthik on 2/15/17.
 */

public class SplashActivity extends CoreActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_activity);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent loginIntent = new Intent(SplashActivity.this,LoginActivity.class);
                SplashActivity.this.startActivity(loginIntent);
                SplashActivity.this.finish();
            }
        }, Constants.SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public void loadNext(int code) {

    }

    @Override
    public void loadPrev() {

    }
}
