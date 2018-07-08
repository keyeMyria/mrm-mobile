package com.andela.mrm.room_availability;

/**
 * Created by Fred Adewole on 16/05/2018.
 */

/**
 * The interface Make request task listener.
 */
public interface IGoogleCalenderCallListener {
    /**
     * On success.
     *
     * @param itemsInString the items in string
     */
    void onSuccess(String itemsInString);

    /**
     * On cancelled.
     *
     * @param mLastError the m last error
     */
    void onCancelled(Exception mLastError);
}

