package com.andela.mrm.room_information.resources_info;

import android.content.Context;

import com.andela.mrm.RoomQuery;
import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    private ResourcesInfoContract.Data.Callback mCallback;
    @Mock
    private Response<RoomQuery.Data> mDataResponse;
    @Captor
    private ArgumentCaptor<ApolloCall.Callback<RoomQuery.Data>> mCallbackArgumentCaptor;

    private ResourcesInfoRepository mRepository;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mRepository = new ResourcesInfoRepository(mContext, mQuery);
    }

    /**
     * Load room calls callback data load success with room data when server returns with success.
     */
    @Test
    public void loadRoom_callsCallbackDataLoadSuccessWithRoomData() {
        RoomQuery.GetRoomById.Fragments fragments =
                mock(RoomQuery.GetRoomById.Fragments.class);
        // stubs
        when(mQuery.clone()).thenReturn(mQuery);
        when(mDataResponse.data()).thenReturn(mData);
        when(mDataResponse.hasErrors()).thenReturn(false);
        when(mData.getRoomById()).thenReturn(mGetRoomById);
        when(mGetRoomById.fragments()).thenReturn(fragments);

        mRepository.loadRoom(mCallback);

        // calls enqueue with the Apollo Callback Argument
        verify(mQuery).enqueue(mCallbackArgumentCaptor.capture());

        // when the Callback returns response with no errors and not null data
        mCallbackArgumentCaptor.getValue().onResponse(mDataResponse);

        verify(mCallback).onDataLoadSuccess(mData.getRoomById().fragments().room());

    }

    /**
     * Load room calls callback data load failed with null data.
     */
    @Test
    public void loadRoom_callsCallbackDataLoadFailedWithNullData() {
        // stubs
        when(mQuery.clone()).thenReturn(mQuery);
        when(mDataResponse.hasErrors()).thenReturn(false);
        when(mDataResponse.data()).thenReturn(null);

        mRepository.loadRoom(mCallback);

        // calls enqueue with the Apollo Callback Argument
        verify(mQuery).enqueue(mCallbackArgumentCaptor.capture());

        // when the Callback returns response with no errors but null data
        mCallbackArgumentCaptor.getValue().onResponse(mDataResponse);

        // Callback is called with an exception
        verify(mCallback).onDataLoadFailed();
    }

    /**
     * Load room calls callback data load failed with response error.
     */
    @Test
    public void loadRoom_callsCallbackDataLoadFailedWithResponseError() {
        // stubs
        when(mQuery.clone()).thenReturn(mQuery);
        when(mDataResponse.hasErrors()).thenReturn(true);
        when(mDataResponse.data()).thenReturn(mData);

        mRepository.loadRoom(mCallback);

        // calls enqueue with the Apollo Callback Argument
        verify(mQuery).enqueue(mCallbackArgumentCaptor.capture());

        // when the Callback returns response with errors
        mCallbackArgumentCaptor.getValue().onResponse(mDataResponse);

        // Callback is called with DataLoadFailed
        verify(mCallback).onDataLoadFailed();
    }

    /**
     * Load room calls callback data load failed with apollo exception.
     */
    @Test
    public void loadRoom_callsCallbackDataLoadFailedWithApolloException() {
        // stubs
        when(mQuery.clone()).thenReturn(mQuery);

        mRepository.loadRoom(mCallback);

        // calls enqueue with the Apollo Callback Argument
        verify(mQuery).enqueue(mCallbackArgumentCaptor.capture());

        // when the Callback returns response with errors
        mCallbackArgumentCaptor.getValue().onFailure(mock(ApolloException.class));

        // Callback is called with an exception
        verify(mCallback).onDataLoadFailed();
    }
}
