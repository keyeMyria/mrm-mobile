package com.andela.mrm.room_information.resources_info;

import com.andela.mrm.R;
import com.andela.mrm.fragment.Room;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Room Resources Info presenter test class.
 */
public class ResourcesInfoPresenterTest {

    @Mock
    private ResourcesInfoContract.View mView;

    @Mock
    private ResourcesInfoContract.Data mData;

    @Captor
    private ArgumentCaptor<ResourcesInfoContract.Data.Callback> mCallbackArgumentCaptor;

    private ResourcesInfoPresenter mPresenter;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mPresenter = new ResourcesInfoPresenter(mView, mData);
    }

    /**
     * Tear down.
     */
    @After
    public void tearDown() {
    }

    /**
     * Fetch room details calls expected view methods with failed request.
     */
    @Test
    public void fetchRoomDetails_callsViewShowErrorMessage_withDataLoadFailedDueToServerError() {
        when(mView.isNetworkAvailable()).thenReturn(true);

        mPresenter.fetchRoomDetails();

        verify(mData).loadRoom(mCallbackArgumentCaptor.capture());
        mCallbackArgumentCaptor.getValue().onDataLoadFailed();

        verify(mView).showLoadingIndicator(false);
        verify(mView).showErrorMessage(R.string.error_data_fetch_message);
    }

    /**
     * Fetch room details calls expected view methods with failed request.
     */
    @Test
    public void fetchRoomDetails_callsViewShowErrorMessage_withDataLoadFailedDueToNetworkError() {
        when(mView.isNetworkAvailable()).thenReturn(false);

        mPresenter.fetchRoomDetails();

        verify(mData).loadRoom(mCallbackArgumentCaptor.capture());
        mCallbackArgumentCaptor.getValue().onDataLoadFailed();

        verify(mView).showLoadingIndicator(false);
        verify(mView).showErrorMessage(R.string.error_internet_connection);
    }

    /**
     * Fetch room details calls expected view methods with successful request.
     */
    @Test
    public void fetchRoomDetails_callsExpectedViewMethodsWithSuccessfulRequest() {
        // mock
        Room room = null;
        mPresenter.fetchRoomDetails();

        verify(mData).loadRoom(mCallbackArgumentCaptor.capture());
        mCallbackArgumentCaptor.getValue().onDataLoadSuccess(room);
        verify(mView).showLoadingIndicator(false);
        verify(mView).showRoomInfo(room);
    }
}
