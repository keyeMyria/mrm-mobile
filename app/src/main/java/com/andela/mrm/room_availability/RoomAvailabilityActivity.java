package com.andela.mrm.room_availability;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;

import com.andela.mrm.Injection;
import com.andela.mrm.R;
import com.andela.mrm.data.CalendarDataRepository;
import com.andela.mrm.find_rooms.FindRoomActivity;
import com.andela.mrm.room_events.EventScheduleActivity;
import com.andela.mrm.room_information.RoomInformationActivity;
import com.andela.mrm.service.ApiService;
import com.andela.mrm.util.NetworkConnectivityChecker;
import com.andela.mrm.widget.TimeLineScheduleView;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.FreeBusyRequest;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.andela.mrm.util.DateTimeUtils.getHour;
import static com.andela.mrm.util.DateTimeUtils.getTime;

/**
 * The type Room availability activity.
 *
 */
public class RoomAvailabilityActivity extends AppCompatActivity implements
        CountDownTimerFragment.IOnTextChangeListener, MeetingRoomDetailFragment.IOnStartCountDown {

    private static final DateTime DAILY_START_TIME = getTime(8, 0, 0);
    private static final DateTime DAILY_END_TIME = getTime(22, 0, 0);

    public List<Event> items;

    @BindView(R.id.layout_find_room)
    LinearLayout mFindRoomBtn;
    @BindView(R.id.layout_schedule)
    LinearLayout mRoomScheduleBtn;
    @BindView(R.id.layout_room_info)
    LinearLayout mRoomInformationBtn;
    @BindView(R.id.view_time_line_strip)
    TimeLineScheduleView mTimeLineScheduleView;
    @BindView(R.id.layout_room_availability_parent)
    ConstraintLayout mRoomAvailabilityRootView;

    @OnClick(R.id.layout_find_room)
    void onClickFindRoomBtn() {
        startActivity(FindRoomActivity.newIntent(this));
    }

    @OnClick(R.id.layout_schedule)
    void onClickScheduleBtn() {
        startActivity(EventScheduleActivity.newIntent(this));
    }

    @OnClick(R.id.layout_room_info)
    void onClickRoomRoomInfoBtn() {
        startActivity(RoomInformationActivity.newIntent(this, 3));
    }

    private FragmentManager mFragmentManager;

    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_availability);
        ButterKnife.bind(this);

        setUpFragments();

        mDisposable = getCalendarFreeBusySchedule();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    /**
     * Sets up inflatable fragments(countdown timer and details fragments).
     */
    private void setUpFragments() {
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .add(R.id.frame_room_availability_details, new MeetingRoomDetailFragment())
                .add(R.id.frame_room_availability_countdown_timer, new CountDownTimerFragment())
                .commit();
    }

    /**
     * Retrieves the FreeBusy Schedule for the current room.
     *
     * @return Disposable
     */
    @NonNull
    private Disposable getCalendarFreeBusySchedule() {
        // TODO: this should be passed in dynamically or retrieved from SharedPreferences
        String calendarId = "andela.com_3236383234313334373439@resource.calendar.google.com";

        Calendar calendarService = ApiService.getCalendarService(
                Injection.provideGoogleSignInWrapperUtil(this)
                        .getSignedInAccount().getAccount(), this);
        FreeBusyRequest request = CalendarDataRepository.buildRoomFreeBusyRequest(calendarId,
                DAILY_START_TIME, DAILY_END_TIME);

        return new CalendarDataRepository(calendarService)
                .getRoomFreeBusySchedule(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::createFreeBusyPeriods,
                        this::handleCalendarDataFetchError);
    }

    /**
     * Error Handler for getCalendarFreeBusySchedule.
     * <p>
     * TODO: belongs to the presenter
     * NOTE: Error handling here has been made generic to prevent the app from crashing.
     * Different errors might occur, but this will be streamlined once the SignIn functionality
     * has been implemented
     *
     * @param exception Exception object
     */
    private void handleCalendarDataFetchError(Throwable exception) {
        Log.e(getClass().getSimpleName(), "CalendarDataFetchError", exception);
        if (!isDeviceOnline()) {
            Snackbar.make(mRoomAvailabilityRootView,
                    R.string.error_internet_connection, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.snackbar_retry_text, v -> getCalendarFreeBusySchedule())
                    .show();
        } else {
            Snackbar.make(mRoomAvailabilityRootView,
                    R.string.error_data_fetch_message, Snackbar.LENGTH_INDEFINITE)
                    .show();
        }
    }

    /**
     * Response handler for getCalendarFreeBusySchedule.
     * <p>
     * TODO: belongs to the presenter
     *
     * @param freeBusyList List of TimePeriods returned from the call.
     */
    private void createFreeBusyPeriods(List<FreeBusy> freeBusyList) {
        mTimeLineScheduleView.setTimeLineData(freeBusyList, getHour(DAILY_START_TIME));
    }

    @Override
    public void onTimeChange(int minutes) {
        MeetingRoomDetailFragment meetingRoomDetailFragment = (MeetingRoomDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.frame_room_availability_details);
        if (meetingRoomDetailFragment != null) {
            meetingRoomDetailFragment.updateMinute(minutes);
        }
    }

    @Override
    public void onCountDownComplete() {
        MeetingRoomDetailFragment meetingRoomDetailFragment = (MeetingRoomDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.frame_room_availability_details);
        if (meetingRoomDetailFragment != null) {
            meetingRoomDetailFragment.displayCheckInScreen();
        }
    }

    @Override
    public void startCountDown(long timeInMilliSeconds) {
        CountDownTimerFragment countDownTimerFragment = (CountDownTimerFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.frame_room_availability_countdown_timer);
        if (countDownTimerFragment != null) {
            countDownTimerFragment.stopTimer();
            countDownTimerFragment.startTimer(timeInMilliSeconds);
            countDownTimerFragment.setTimeRemainingText("Time Remaining");
        }
    }

    /**
     * On meeting ended.
     */
    @Override
    public void onMeetingEnded() {
        CountDownTimerFragment countDownTimerFragment = (CountDownTimerFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.frame_room_availability_countdown_timer);
        if (countDownTimerFragment != null) {
            countDownTimerFragment.setTimeRemainingText("Time Till Next Meeting");
            countDownTimerFragment.stopTimer();
            countDownTimerFragment.startTimer(8000);
        }
    }

    /**
     * Checks whether the device currently has a network connection.
     *
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        return NetworkConnectivityChecker.isDeviceOnline(getApplicationContext());
    }
}
