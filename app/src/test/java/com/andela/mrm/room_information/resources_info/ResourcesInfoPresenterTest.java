package com.andela.mrm.room_information.resources_info;

import com.andela.mrm.fragment.Room;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

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
     * Fetch room details calls expected dependency methods.
     */
    @Test
    public void fetchRoomDetailsCallsExpectedDependencyMethods() {
        mPresenter.fetchRoomDetails();
        verify(mView).showLoadingIndicator(true);
        verify(mData).loadRoom(mCallbackArgumentCaptor.capture());
    }

    /**
     * Fetch room details calls expected view methods with failed request.
     */
    @Test
    public void fetchRoomDetailsCallsExpectedViewMethodsWithFailedRequest() {
        // mocks
        String dataLoadFailedMessage = "something went wrong";
        Exception dataLoadFailedException = new Exception(dataLoadFailedMessage);

        mPresenter.fetchRoomDetails();

        verify(mData).loadRoom(mCallbackArgumentCaptor.capture());
        mCallbackArgumentCaptor.getValue().onDataLoadFailed(dataLoadFailedException);
        verify(mView).showLoadingIndicator(false);
        verify(mView).showErrorMessage(dataLoadFailedMessage);
    }

    /**
     * Fetch room details calls expected view methods with successful request.
     */
    @Test
    public void fetchRoomDetailsCallsExpectedViewMethodsWithSuccessfulRequest() {
        // mock
        Room room = null;
        mPresenter.fetchRoomDetails();

        verify(mData).loadRoom(mCallbackArgumentCaptor.capture());
        mCallbackArgumentCaptor.getValue().onDataLoadSuccess(room);
        verify(mView).showLoadingIndicator(false);
        verify(mView).showRoomInfo(room);
    }
}
