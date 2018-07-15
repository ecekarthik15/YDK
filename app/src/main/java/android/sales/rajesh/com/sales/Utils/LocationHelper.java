package android.sales.rajesh.com.sales.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by Karthik on 2/12/17.
 */

public class LocationHelper {
    private static final String TAG = "LocationHelper";
    LocationManager locationManager;
    Context ctx;
    private LocationResult locationResult;
    boolean gpsEnabled = false;
    boolean networkEnabled = false;

    public boolean getLocation(Context context, LocationResult result) {
        locationResult = result;

        ctx = context;

        if (locationManager == null) {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }

        // check whether the location providers are enabled.
        // exceptions thrown if provider not enabled
        try {
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // don't start listeners if no provider is enabled
        if (!gpsEnabled && !networkEnabled) {
            Log.d(TAG, "location manager[GPS_PROVIDER & NETWORK_PROVIDER] are disabled.");
            return false;
        }

        // if location providers enabled, request location updates
        if (gpsEnabled) {
            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return false;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                    locationListenerGps);
            Log.d(TAG, "GPS_PROVIDER location update requested.");

        } else {
            Log.d(TAG, "location manager[GPS_PROVIDER] disabled.");
        }

        if (networkEnabled) {
            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return false;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                    locationListenerNetwork);
            Log.d(TAG, "NETWORK_PROVIDER location update requested.");

        } else {
            Log.d(TAG, "location manager[NETWORK_PROVIDER] disabled.");
        }

        getLastLocation();
        return true;
    }

    // remove the location listener once we got the location from GPS_PROVIDER
    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            Log.d(TAG, "got the location from GPS_PROVIDER");
            locationResult.gotLocation(location);
            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling

                return;
            }
            locationManager.removeUpdates(this);
            locationManager.removeUpdates(locationListenerNetwork);

        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extra) {
        }
    };

    // remove the location listener once we got the location from
    // NETWORK_PROVIDER
    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            Log.d(TAG, "got the location from NETWORK_PROVIDER");
            locationResult.gotLocation(location);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extra) {
        }

    };

    /**
     *
     */
    private void getLastLocation() {

        Location gpsLocation = null;
        Location networkLocation = null;

        if (gpsEnabled) {
            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling

                return;
            }
            gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (networkEnabled) {

            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling

                return;
            }
            networkLocation = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        // if there are both values use the latest one
        if (gpsLocation != null && networkLocation != null) {
            if (gpsLocation.getTime() > networkLocation.getTime()) {
                Log.d(TAG, "got the latest last known location from GPS_PROVIDER");
                locationResult.gotLocation(gpsLocation);
            } else {
                Log.d(TAG, "got the latest last known location from NETWORK_PROVIDER");
                locationResult.gotLocation(networkLocation);
            }

            return;
        }

        if (gpsLocation != null) {
            Log.d(TAG, "got the latest last known location from GPS_PROVIDER");
            locationResult.gotLocation(gpsLocation);
            return;
        }

        if (networkLocation != null) {
            Log.d(TAG, "got the latest last known location from NETWORK_PROVIDER");
            locationResult.gotLocation(networkLocation);
            return;
        }

        locationResult.gotLocation(null);
    }

    public static abstract class LocationResult {
        public abstract void gotLocation(Location location);
    }
}
