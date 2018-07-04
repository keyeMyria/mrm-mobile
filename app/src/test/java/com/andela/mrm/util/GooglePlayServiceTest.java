package com.andela.mrm.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static com.andela.mrm.util.GooglePlayService.REQUEST_GOOGLE_PLAY_SERVICES;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * The Google play service test.
 */
public class GooglePlayServiceTest {

    private GooglePlayService mGooglePlayService;

    @Mock
    private GoogleApiAvailability mGoogleApiAvailability;


    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        initMocks(this);
        mGooglePlayService = new GooglePlayService(mGoogleApiAvailability);
    }

    /**
     * Is google play services available returns true.
     */
    @Test
    public void isGooglePlayServicesAvailable_returnsTrue() {
        Context context = any(Context.class);
        when(mGoogleApiAvailability.isGooglePlayServicesAvailable(context))
                .thenReturn(ConnectionResult.SUCCESS);

        assertTrue(mGooglePlayService.isGooglePlayServicesAvailable(context));
    }


    /**
     * Acquire google play services calls show error dialog.
     */
    @Test
    public void acquireGooglePlayServices_callsShowErrorDialog() {
        Context context = mock(Context.class);
        Activity activity = mock(Activity.class);
        Dialog dialog = mock(Dialog.class);
        int connectionStatusCode = 1;
        boolean isUserResolvableError = true;

        GooglePlayService googlePlayService = spy(mGooglePlayService);

        // stubs
        when(mGoogleApiAvailability.isGooglePlayServicesAvailable(context))
                .thenReturn(connectionStatusCode);
        when(mGoogleApiAvailability.isUserResolvableError(connectionStatusCode))
                .thenReturn(isUserResolvableError);
        when(mGoogleApiAvailability
                .getErrorDialog(activity, connectionStatusCode, REQUEST_GOOGLE_PLAY_SERVICES))
                .thenReturn(dialog);

        googlePlayService.acquireGooglePlayServices(context, activity);
        verify(googlePlayService).
                showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode, activity);
    }
}
