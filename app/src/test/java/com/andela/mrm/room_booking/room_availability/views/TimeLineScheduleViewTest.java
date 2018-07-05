package com.andela.mrm.room_booking.room_availability.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.andela.mrm.R;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Created by chike on 04/07/2018.
 */
public class TimeLineScheduleViewTest {
    @Mock
    private Context context;
    @Mock
    private AttributeSet attributes;
    @Mock
    private TypedArray typedArray;


    /**
     * @throws Exception -
     */
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        initMockAttributes(context, typedArray);
    }


    /**
     * @param context -
     * @param typedArray -
     */
    private void initMockAttributes(Context context, TypedArray typedArray) {
        when(context.obtainStyledAttributes(attributes, R.styleable.TimeLineScheduleView))
                .thenReturn(typedArray);
        when(typedArray.getInt(eq(R.styleable.TimeLineScheduleView_canvasHeight), anyInt()))
                .thenReturn(200);

        when(typedArray.getFloat(eq(R.styleable.TimeLineScheduleView_sizeOfText), anyFloat()))
                .thenReturn(25F);
        when(typedArray.getFloat(eq(R.styleable.TimeLineScheduleView_strokeWidth), anyFloat()))
                .thenReturn(2F);
        when(typedArray
                .getFloat(eq(R.styleable.TimeLineScheduleView_lengthOfSingleBar), anyFloat()))
                .thenReturn(170F);
        when(typedArray.getFloat(eq(R.styleable.TimeLineScheduleView_textStartYPoint), anyFloat()))
                .thenReturn(120F);
        when(typedArray
                .getFloat(eq(R.styleable.TimeLineScheduleView_rectHeightAboveLine), anyFloat()))
                .thenReturn(4F);
    }

}
