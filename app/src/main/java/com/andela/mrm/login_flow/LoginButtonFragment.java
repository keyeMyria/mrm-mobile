package com.andela.mrm.login_flow;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.andela.mrm.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A {@link Fragment} subclass.
 */
public class LoginButtonFragment extends Fragment {

    /**
     * The M image button.
     */
    @BindView(R.id.button_login)
    ImageButton mImageButton;

    private Callbacks mCallbacks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login_button, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    /**
     * On click login button.
     */
    @OnClick(R.id.button_login)
    void onClickLoginButton() {
        mCallbacks.onClickLoginButton();
    }

    /**
     * Callbacks for communicating with the activity.
     */
    interface Callbacks {
        /**
         * On click login button.
         */
        void onClickLoginButton();
    }

}
