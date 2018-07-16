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
import com.andela.mrm.room_availability.FreeBusy;

import java.util.List;

/**
 * TimeLineScheduleView Custom Class.
 */
public class TimeLineScheduleView extends View {
    private static final float DIFF_TEXT_STARTY_SHAPE_STARTY = 35F;

    private Paint mLinePaint;
    private Paint mRectPaint;
    private TextPaint mTextPaint;

    private float mHourScaleXLength;
    private float textSize;
    private float strokeWidth;
    private int canvasHeight;
    private float textStartYPoint;
    private float shapeStartYPoint;
    private float mRectHeight;

    @Nullable
    private List<FreeBusy> mFreeBusyList;
    private int mTotalHours;
    private int mStartHour;

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
     * Computes the total number of hours to be drawn by this view.
     *
     * @param freeBusyList List of data to compute hours from
     * @return total number of hours from freeBusy list
     */
    private static int getTotalFreeBusyHours(@Nullable List<FreeBusy> freeBusyList) {
        int totalFreeBusyDuration = 0;
        if (freeBusyList != null) {
            for (FreeBusy freeBusy : freeBusyList) {
                totalFreeBusyDuration += freeBusy.getDuration();
            }
        }
        return (int) Math.ceil(totalFreeBusyDuration / 60.0);
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
        TypedArray ta = getContext()
                .obtainStyledAttributes(attrs, R.styleable.TimeLineScheduleView);
        try {
            canvasHeight = ta.getInt(R.styleable.TimeLineScheduleView_canvasHeight, 200);
            textSize = ta.getFloat(R.styleable.TimeLineScheduleView_sizeOfText, 25F);
            strokeWidth = ta.getFloat(R.styleable.TimeLineScheduleView_strokeWidth, 2F);
            mHourScaleXLength = ta.
                    getFloat(R.styleable.TimeLineScheduleView_lengthOfSingleBar, 170F);

            shapeStartYPoint = ta.getFloat(R.styleable.TimeLineScheduleView_shapeStartYPoint, 85F);
            textStartYPoint = ta.getFloat(R.styleable.TimeLineScheduleView_textStartYPoint,
                    shapeStartYPoint + DIFF_TEXT_STARTY_SHAPE_STARTY);
            mRectHeight = ta.getFloat(R.styleable.TimeLineScheduleView_rectHeightAboveLine, 5F);
        } finally {
            ta.recycle();
        }

        initializeThinLinePaint();
        initializeRectangularPaint();
        initializeTextViewPaint();
    }

    /**
     * Initializes the paint used in drawing texts in the view.
     */
    private void initializeTextViewPaint() {
        Typeface dinpro = Typeface
                .createFromAsset(getContext().getAssets(), "font/dinpro.otf");

        mTextPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTypeface(dinpro);
    }

    /**
     * Initializes the paint used in drawing rectangle shapes in the view.
     */
    private void initializeRectangularPaint() {
        mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * Initializes paint used in drawing thin lines.
     */
    private void initializeThinLinePaint() {
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(strokeWidth);
        mLinePaint.setColor(getResources().getColor(R.color.lineGray));
    }

    /**
     * Populates the data used in drawing the timeline.
     *
     * @param freeBusyList - array containing list of {@link FreeBusy}
     * @param startHour    - hour to start drawing the TimeLine view from
     */
    public void setTimeLineData(List<FreeBusy> freeBusyList, int startHour) {
        mFreeBusyList = freeBusyList;
        mStartHour = startHour;
        mTotalHours = getTotalFreeBusyHours(mFreeBusyList);

        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mFreeBusyList != null) {
            drawTimeTexts(canvas, mTotalHours, mStartHour);
            drawTimeLineShapes(canvas, mFreeBusyList);
        }
    }

    /**
     * Draws the time/period texts starting from the start hour.
     *
     * @param canvas     canvas instance to be drawn on.
     * @param totalHours total number of time periods to be drawn - in hours
     * @param startHour  reference time
     */
    private void drawTimeTexts(Canvas canvas, int totalHours, int startHour) {
        final int noon = 12;

        for (int i = 0; i < totalHours; i++) {
            float nextStartX = mHourScaleXLength * i;
            int nextHour = startHour + i;
            String timeText;
            if (nextHour < noon) {
                timeText = nextHour + " am";
            } else if (nextHour == noon) {
                timeText = nextHour + " noon";
            } else {
                timeText = (nextHour - noon) + " pm";
            }
            canvas.drawText(timeText, nextStartX, textStartYPoint, mTextPaint);
        }
    }

    /**
     * Draws the shapes progressively that each represent an availability status within a time
     * range.
     *
     * @param canvas       canvas instance to be drawn on
     * @param freeBusyList List of {@link FreeBusy}
     */
    private void drawTimeLineShapes(Canvas canvas, List<FreeBusy> freeBusyList) {
        float startX = 0;
        for (int i = 0; i < freeBusyList.size(); i++) {
            FreeBusy freeBusy = freeBusyList.get(i);
            int length = (int) ((freeBusy.getDuration() * mHourScaleXLength) / 60);
            switch (freeBusy.getStatus()) {
                case BUSY:
                    drawRect(canvas, startX, length, Color.WHITE);
                    break;
                case BUSY_CURRENT:
                    drawRect(canvas, startX, length, Color.GREEN);
                    break;
                case BUSY_CURRENT_CHECKED_IN:
                    drawRect(canvas, startX, length, Color.RED);
                    break;
                case FREE:
                    drawLine(canvas, startX, length);
                    break;
                default:
                    break;
            }
            startX += length;
        }
    }

    /**
     * Draws a single rect specified by the parameters.
     *
     * @param canvas Canvas
     * @param startX Starting point on the x-axis
     * @param length Length of the rectangle
     * @param color  Paint color
     */
    private void drawRect(Canvas canvas, float startX, float length, int color) {
        mRectPaint.setColor(color);
        final float top = shapeStartYPoint + mRectHeight;
        final float bottom = shapeStartYPoint - mRectHeight;
        final float right = startX + length;

        canvas.drawRect(startX, top, right, bottom, mRectPaint);
    }

    /**
     * Draws a single line on the canvas specified by the parameters.
     *
     * @param canvas Canvas
     * @param startX starting point on the x-axis
     * @param length length of the line
     */
    private void drawLine(Canvas canvas, float startX, float length) {
        float stopX = startX + length;
        canvas.drawLine(startX, shapeStartYPoint, stopX, shapeStartYPoint, mLinePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize((int) (mTotalHours * mHourScaleXLength));
        setMeasuredDimension(width, canvasHeight);
    }
}
