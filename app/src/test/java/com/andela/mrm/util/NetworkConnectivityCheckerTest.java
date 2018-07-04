package com.andela.mrm.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * The Network connectivity checker test.
 */
public class NetworkConnectivityCheckerTest {

    private Context mContext;
    private NetworkInfo mNetworkInfo;
    private ConnectivityManager mConnectivityManager;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        mContext = Mockito.mock(Context.class);
        mNetworkInfo = Mockito.mock(NetworkInfo.class);
        mConnectivityManager = Mockito.mock(ConnectivityManager.class);

    }

    /**
     * Is device online returns false with unavailable network.
     */
    @Test
    public void isDeviceOnline_ReturnsFalseWithUnavailableNetwork() {
        when(mContext.getSystemService(Context.CONNECTIVITY_SERVICE))
                .thenReturn(mConnectivityManager);
        when(mConnectivityManager.getActiveNetworkInfo()).thenReturn(null);

        assertFalse(NetworkConnectivityChecker.isDeviceOnline(mContext));
    }

    /**
     * Is device online returns false with available network but device is not connected.
     */
    @Test
    public void isDeviceOnline_ReturnsFalseWithAvailableNetworkButDeviceIsNotConnected() {
        when(mContext.getSystemService(Context.CONNECTIVITY_SERVICE))
                .thenReturn(mConnectivityManager);
        when(mConnectivityManager.getActiveNetworkInfo()).thenReturn(mNetworkInfo);
        when(mNetworkInfo.isConnected()).thenReturn(false);

        assertFalse(NetworkConnectivityChecker.isDeviceOnline(mContext));
    }

    /**
     * Is device online returns true with available network and connected device.
     */
    @Test
    public void isDeviceOnline_ReturnsTrueWithAvailableNetworkAndConnectedDevice() {
        when(mContext.getSystemService(Context.CONNECTIVITY_SERVICE))
                .thenReturn(mConnectivityManager);
        when(mConnectivityManager.getActiveNetworkInfo()).thenReturn(mNetworkInfo);
        when(mNetworkInfo.isConnected()).thenReturn(true);

        assertTrue(NetworkConnectivityChecker.isDeviceOnline(mContext));
    }
}
