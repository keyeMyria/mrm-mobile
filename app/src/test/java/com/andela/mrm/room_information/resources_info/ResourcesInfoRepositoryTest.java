package com.andela.mrm.room_information.resources_info;

import android.content.Context;

import com.andela.mrm.RoomQuery;
import com.andela.mrm.fragment.Room;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloQueryCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The type Resources info repository test.
 */
public class ResourcesInfoRepositoryTest {

    @Mock
    private RoomQuery.Data mData;
    @Mock
    private RoomQuery.GetRoomById mGetRoomById;
    @Mock
    private Context mContext;
    @Mock
    private ApolloCall<RoomQuery.Data> mQuery;
    @Mock
    private ResourcesInfoContract.Data.Callback<Room> mCallback;
    @Mock
    private Response<RoomQuery.Data> mDataResponse;
    @Mock
    private ApolloClient mApolloClient;
    @Mock
    private ApolloException mException;
    @Captor
    private ArgumentCaptor<ApolloCall.Callback<RoomQuery.Data>> mCallbackArgumentCaptor;

    private ResourcesInfoRepository mRepository;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mRepository = new ResourcesInfoRepository(mApolloClient);
    }

    /**
     * Load room calls callback data load success with room data when server returns with success.
     */
    @Test
    public void loadRoom_callsCallbackDataLoadSuccessWithRoomData() {
        RoomQuery.GetRoomById.Fragments fragments =
                mock(RoomQuery.GetRoomById.Fragments.class);
//      stubs
        when(mDataResponse.data()).thenReturn(mData);
        when(mDataResponse.hasErrors()).thenReturn(false);
        when(mData.getRoomById()).thenReturn(mGetRoomById);
        when(mGetRoomById.fragments()).thenReturn(fragments);

        ApolloQueryCall queryCall = mock(ApolloQueryCall.class);
        when(mApolloClient.query(any(RoomQuery.class))).thenReturn(queryCall);
        mRepository.loadRoom(mCallback, eq(1));

        // calls enqueue with the Apollo Callback Argument
        verify(queryCall).enqueue(mCallbackArgumentCaptor.capture());

        // when the Callback returns response with no errors and not null data
        mCallbackArgumentCaptor.getValue().onResponse(mDataResponse);

        verify(mCallback).onDataLoadSuccess(mData.getRoomById().fragments().room());
    }

    /**
     * Load room calls callback data load failed with apollo exception.
     */
    @Test
    public void loadRoom_callsCallbackDataLoadFailedWithApolloException() {
        // stubs
        when(mQuery.clone()).thenReturn(mQuery);
        ApolloQueryCall queryCall = mock(ApolloQueryCall.class);
        when(mApolloClient.query(any(RoomQuery.class))).thenReturn(queryCall);
        mRepository.loadRoom(mCallback, eq(1));

        verify(queryCall).enqueue(mCallbackArgumentCaptor.capture());

        // when the Callback returns response with errors
        mCallbackArgumentCaptor.getValue().onFailure(mock(ApolloException.class));

        // Callback is called with an exception
        verify(mCallback).onDataLoadFailed();
    }
}
