package com.andela.mrm.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * The type Google play service.
 */
public class GooglePlayService {

    /**
     * The Request google play services.
     */
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private final GoogleApiAvailability mGoogleApiAvailability;

    /**
     * Instantiates a new Google play service.
     *
     * @param googleApiAvailability the google api availability
     */
    public GooglePlayService(GoogleApiAvailability googleApiAvailability) {
        mGoogleApiAvailability = googleApiAvailability;
    }

    /**
     * Is google play services available boolean.
     *
     * @param context the context
     * @return the boolean
     */
    public boolean isGooglePlayServicesAvailable(Context context) {
        final int connectionStatusCode =
                mGoogleApiAvailability.isGooglePlayServicesAvailable(context);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Show google play services availability error dialog.
     *
     * @param connectionStatusCode the connection status code
     * @param activity             the activity
     */
    public void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode, Activity activity) {
        Dialog dialog = mGoogleApiAvailability.getErrorDialog(
                activity,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * Acquire google play services.
     *
     * @param context  the context
     * @param activity the activity
     */
    public void acquireGooglePlayServices(Context context, Activity activity) {
        final int connectionStatusCode =
                mGoogleApiAvailability.isGooglePlayServicesAvailable(context);
        if (mGoogleApiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode, activity);
        }
    }
}
