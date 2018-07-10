package com.andela.mrm.room_information.resources_info;

import com.andela.mrm.GetRoomsInABlockQuery;
import com.andela.mrm.GetRoomsInABlockQuery.GetRoomsInABlock;
import com.andela.mrm.R;
import com.andela.mrm.fragment.Room;
import com.andela.mrm.room_information.resources_info.ResourcesInfoContract.Data.Callback;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
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
    private ArgumentCaptor<Callback<Room>> mCallbackArgumentCaptor;

    @Captor
    private ArgumentCaptor<Callback<GetRoomsInABlock>> mGetRoomsInABlockArgCaptor;

    private ResourcesInfoPresenter mPresenter;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mPresenter = new ResourcesInfoPresenter(mView, mData, 1);
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
        verify(mView).showLoadingIndicator(true);

        verify(mData).loadRoom(mCallbackArgumentCaptor.capture(), eq(1));
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

        verify(mData).loadRoom(mCallbackArgumentCaptor.capture(), eq(1));
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
        Room room = mock(Room.class);
        Room.Floor floor = mock(Room.Floor.class);
        Room.Block block = mock(Room.Block.class);
        GetRoomsInABlock getRoomsInABlock = mock(GetRoomsInABlock.class);

        when(room.floor()).thenReturn(floor);
        when(floor.block()).thenReturn(block);
        when(block.id()).thenReturn("1");
        ArrayList<GetRoomsInABlockQuery.Floor> floors = new ArrayList<>();
        when(getRoomsInABlock.floors()).thenReturn(floors);

        mPresenter.fetchRoomDetails();

        verify(mData).loadRoom(mCallbackArgumentCaptor.capture(), eq(1));
        mCallbackArgumentCaptor.getValue().onDataLoadSuccess(room);
        verify(mView).showLoadingIndicator(true);
        verify(mData).loadSimilarRooms(mGetRoomsInABlockArgCaptor.capture(), eq(1));

        mGetRoomsInABlockArgCaptor.getValue().onDataLoadSuccess(getRoomsInABlock);
        verify(mView).showLoadingIndicator(false);
        mView.showRoomInfo(room);
        mView.showSimilarRooms(new ArrayList<>());
    }
}
