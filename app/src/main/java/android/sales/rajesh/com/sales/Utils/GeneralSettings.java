package android.sales.rajesh.com.sales.Utils;

import android.location.Location;
import android.sales.rajesh.com.sales.View.OnUpdateViewListener;
import android.util.Log;

/**
 * Created by Karthik on 1/31/17.
 */

public class GeneralSettings {

    private static final String TAG = "GeneralSettings";

    private static GeneralSettings instance = null;

    private boolean isMockMode = false;
    private boolean isDevMode = true;
    private String deviceId = "";
    private String carrier = "";
    private String deviceSoftwareVersion = "";
    private String deviceManufacturer = "";
    private String deviceModel = "";
    private boolean isStartActivity;
    private int webReqThreadIdNumber = 0;
    private boolean isAppUpgradeChecked = false;
    private String sessionID = null;
    private String version = null;
    private String mobileNumber = "";
    private String timerValue = "";
    private String availability = "";
    private String callStatus = "";
    private String networkStatus = "";
    private String updateTime = "";
    private Location currentGPSLocation = null;

    // use these update listener to update view for lazy data load
    private OnUpdateViewListener contactControllerOnUpdateViewListener = null;

    int areaOfInterest = -1;
    String topic;
    String topicTitle;
    String firstName;
    String secondName;
    String zipCode;
    String phone;
    String loginError;
    float dip;

	public String getDeviceManufacturer() {
		return deviceManufacturer;
	}

	public void setDeviceManufacturer(String deviceManufacturer) {
		this.deviceManufacturer = deviceManufacturer;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

    public float getDip() {
        return dip;
    }

    public void setDip(float dip) {
        this.dip = dip;
    }

    public String getLoginError() {
        return loginError;
    }

    public void setCurrentGPSLocation(Location currentLocation) {
        this.currentGPSLocation = currentLocation;
    }

    public Location getCurrentGPSLocation() {
        return currentGPSLocation;
    }

    public void setLoginError(String loginError) {
        this.loginError = loginError;
    }

    private GeneralSettings() {
        Log.d(TAG, "ctor start");
    }

    public static GeneralSettings getInstance() {

        if (instance == null) {
            synchronized (GeneralSettings.class) {
                if (instance == null) {
                    instance = new GeneralSettings();
                }
            }
        }
        return instance;
    }

	public void setDeviceId(String devId) {
		this.deviceId = devId;
	}

	public String getDeviceId() {
		return deviceId;
	}


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isStartActivity() {
        return isStartActivity;
    }

    public void setStartActivity(boolean isStartActivity) {
        this.isStartActivity = isStartActivity;
    }

    public static String getRoundedDoubleValue(double d) {
        String str = String.valueOf(d);
        int pos = str.lastIndexOf(".");
        if (pos == -1)
            return str;
        else
            return str.substring(0, pos + 2);
    }

    public boolean isMockMode() {
        return isMockMode;
    }

	public void setDeviceSoftwareVersion(String deviceSoftwareVersion) {
		this.deviceSoftwareVersion = deviceSoftwareVersion;
	}

	public String getDeviceSoftwareVersion() {
		return deviceSoftwareVersion;
	}

    public void setWebReqThreadIdNumber(int webReqThreadIdNumber) {
        this.webReqThreadIdNumber = webReqThreadIdNumber;
    }

    public OnUpdateViewListener getContactOnUpdateViewListener() {
        return contactControllerOnUpdateViewListener;
    }

    // Allows the user to set an Listener and react to the event
    public void setContactOnUpdateViewListener(
            OnUpdateViewListener updateViewListener) {
        this.contactControllerOnUpdateViewListener = updateViewListener;
    }

    public int getWebReqThreadIdNumber() {
        return webReqThreadIdNumber;
    }

    public void incrementWebReqThreadIdNumber() {
        this.webReqThreadIdNumber++;
    }

    public void setDevMode(boolean isDevMode) {
        this.isDevMode = isDevMode;
    }

    public boolean isDevMode() {
        return isDevMode;
    }

    public void setAppUpgradeChecked(boolean isAppUpgradeChecked) {
        this.isAppUpgradeChecked = isAppUpgradeChecked;
    }

    public boolean isAppUpgradeChecked() {
        return isAppUpgradeChecked;
    }

    public void setSessionID(String sessionId) {
        this.sessionID = sessionId;
    }

    public String getSessionID() {
        return sessionID;
    }
    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        if(version == null){
            version = "0";
        }
        return version;
    }
    /**
     * Check whether the server is accessible or not
     *
     * @return True if server has accessible
     */
//    public static boolean isServerAccessable() {
//        Log.d(TAG, "isServerAccessable()");
//        HttpClient client = new DefaultHttpClient();
//        HttpGet request = new HttpGet("http://www.google.com");
//        try {
//            HttpResponse response = client.execute(request);
//            // Check if server response is valid
//            StatusLine status = response.getStatusLine();
//            Log.d(TAG, "ServerAccessable status code[" + status.getStatusCode()
//                    + "]");
//            if (status.getStatusCode() == HttpStatus.SC_OK) {
//                return true;
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "isServerAccessable - connection exception", e);
//            return false;
//        }
//    }

    public static void hitURL(final String url) {
//        new Thread() {
//            @Override
//            public void run() {
//                HttpURLConnection news = null;
//                InputStream is = null;
//                Vector vector = new Vector();
//                Log.i(TAG, "Thread Started");
//                try {
//                    // URL urls = new
//                    // URL("http://multibriefs.com/briefs/ifma/IFMA.xml");
//                    URL urls = new URL(url);
//                    // Tuttle, ND
//                    Log.i(TAG, "Url constructed");
//                    news = (HttpURLConnection) urls.openConnection();
//                    Log.i(TAG, "HttpUrlConnection done");
//                    int responsecode = news.getResponseCode();
//                    System.out.println("responsecode = " + responsecode);
//                    is = news.getInputStream();
//
//                    // System.out.println(convertStreamToString(is));
//                    // parseInputStream(is);
//                    // pd.dismiss();
//
//                } catch (Exception e) {
//                    System.out.println(e);
//                }
//            }
//        }.start();

    }
}
