package com.andela.mrm.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.andela.mrm.R;
import com.andela.mrm.room_availability.TimeLine;

import java.util.ArrayList;
import java.util.List;

/**
 * TimeLineScheduleView Custom Class.
 */
public class TimeLineScheduleView extends View {
    Paint mLinePaint, mRectPaint;
    TextPaint mTextPaint;
    float lengthOfSingleTimeBar, textSize, strokeWidth;
    int numberOfSingleTimeBar = 14;
    int canvasHeight;
    float textStartYPoint, rectHeightAboveLine;

    // TimeLine Model
    List<TimeLine> mTimeLineArray;

    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public TimeLineScheduleView(Context context) {
        super(context);
        init(null);
    }

    /**
     * Constructor that is called when inflating a view from XML. This is called
     * when a view is being constructed from an XML file, supplying attributes
     * that were specified in the XML file. This version uses a default style of
     * 0, so the only attribute values applied are those in the Context's Theme
     * and the given AttributeSet.
     * <p>
     * <p>
     * The method onFinishInflate() will be called after all children have been
     * added.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     */
    public TimeLineScheduleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    /**
     * Perform inflation from XML and apply a class-specific base style from a
     * theme attribute. This constructor of View allows subclasses to use their
     * own base style when they are inflating.
     *
     * @param context      The Context the view is running in, through which it can
     *                     access the current theme, resources, etc.
     * @param attrs        The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *                     reference to a style resource that supplies default values for
     *                     the view. Can be 0 to not look for defaults.
     */
    public TimeLineScheduleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /**
     * Initializes Paint objects.
     *
     * @param attrs - custom attributes
     */
    private void init(@Nullable AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray ta = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.TimeLineScheduleView);
        try {
            canvasHeight = ta.getInt(R.styleable.TimeLineScheduleView_canvasHeight, 200);
            textSize = ta.getFloat(R.styleable.TimeLineScheduleView_sizeOfText, 25F);
            strokeWidth = ta.getFloat(R.styleable.TimeLineScheduleView_strokeWidth, 2F);
            lengthOfSingleTimeBar = ta.
                    getFloat(R.styleable.TimeLineScheduleView_lengthOfSingleBar, 170F);
            textStartYPoint = ta.getFloat(R.styleable.TimeLineScheduleView_textStartYPoint, 120F);
            rectHeightAboveLine = ta
                    .getFloat(R.styleable.TimeLineScheduleView_rectHeightAboveLine, 4F);
        } finally {
            ta.recycle();
        }
        mTimeLineArray = new ArrayList<>();
        populateArrayList(mTimeLineArray);
        // create thin line paint
        createThinLinePaint();
        // create rectangular time-bar paint
        createRectangularTimeBarPaint();
        // create text-view paint
        createTextViewPaint();
        Typeface dinpro = Typeface.createFromAsset(getContext().getAssets(), "font/dinpro.otf");
        mTextPaint.setTypeface(dinpro);
    }

    /**
     * Extracted Method to create Text View Paint.
     */
    public final void createTextViewPaint() {
        mTextPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setTextSize(textSize);
    }

    /**
     * Extracted Method to create the rectangular time bar.
     */
    public final void createRectangularTimeBarPaint() {
        mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * Extracted Method to create the thinLine paint.
     */
    public final void createThinLinePaint() {
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(strokeWidth);
        mLinePaint.setColor(getResources().getColor(R.color.lineGray));
    }

    /**
     * Populates an array list of schedule events.
     *
     * @param mTimeLineArray - array containing list of dummy schedule events
     */
    private void populateArrayList(List<TimeLine> mTimeLineArray) {
        // dummy meeting duration in minutes for both booked and available time slots, alternatively
        int[] timeDuration = {60, 90, 60, 45, 45, 15, 60, 75, 60, 150, 60, 40, 80};

        for (int i = 0; i < timeDuration.length; i++) {
            int timeBarColor = Color.WHITE;
            boolean isRoomAvailable = false;

            if (i == 2) {
                timeBarColor = Color.GREEN;
            }

            if ((i + 2) % 2 != 0) {
                isRoomAvailable = true;
            }

            mTimeLineArray.add(new TimeLine(timeDuration[i], timeBarColor, isRoomAvailable));
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float startXPoint = 0F;
        final float startEndYPoint = 85F;
        canvasDrawer(canvas, startXPoint);
        for (int i = 0; i <= (numberOfSingleTimeBar - 2); i++) {
            int meetingDuration = mTimeLineArray.get(i).getMeetingDuration();
            int timeBarColor = mTimeLineArray.get(i).getTimeBarColor();
            boolean isRoomAvailable = mTimeLineArray.get(i).isAvailable();
            float rectHeightBelowLine = rectHeightAboveLine;
            int lengthOfMeetingBar = (int) ((lengthOfSingleTimeBar * meetingDuration) / 60);
            final float endXPoint = startXPoint + lengthOfMeetingBar;
            mRectPaint.setColor(timeBarColor);
            if (i > 0) {
                if (!isRoomAvailable) {
                    canvas.drawRect(startXPoint, startEndYPoint - rectHeightAboveLine,
                            endXPoint, startEndYPoint + rectHeightBelowLine, mRectPaint);
                } else {
                    canvas.drawLine(startXPoint, startEndYPoint,
                            endXPoint, startEndYPoint, mLinePaint);
                }
            } else {
                canvas.drawRect(startXPoint, startEndYPoint - rectHeightAboveLine,
                        lengthOfMeetingBar, startEndYPoint + rectHeightBelowLine, mRectPaint);
            }
            startXPoint += lengthOfMeetingBar;
        }
    }

    /**
     * Method to deal with drawing the canvas.
     *
     * @param canvas      canvas instance to be drawn on
     * @param startXPoint floating point integer to represent the start point
     */
    public void canvasDrawer(Canvas canvas, float startXPoint) {
        float textStartXPoint;
        for (int i = 0; i < numberOfSingleTimeBar; i++) {
            String timePeriod = " am";
            String timeText = 8 + i + timePeriod;
            if (i == 0) {
                textStartXPoint = startXPoint;
            } else {
                textStartXPoint = lengthOfSingleTimeBar * i;
                if (i == 4) {
                    timePeriod = " noon";
                    timeText = 8 + i + timePeriod;
                } else if (i > 4) {
                    timePeriod = " pm";
                    timeText = (i - 4) + timePeriod;
                }
            }
            canvas.drawText(timeText, textStartXPoint, textStartYPoint, mTextPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Compute the width and height required to render the custom view

        int width = MeasureSpec.getSize((int) (lengthOfSingleTimeBar * numberOfSingleTimeBar));

        setMeasuredDimension(width, canvasHeight);
    }


}
