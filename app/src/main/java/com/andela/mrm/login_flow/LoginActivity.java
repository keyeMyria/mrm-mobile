package com.andela.mrm.login_flow;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.andela.mrm.R;

/**
 * LoginActivity class.
 */
public class LoginActivity extends AppCompatActivity {
    FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inflateButtons();
    }

    /**
     * Inflates the login button fragment.
     */
    private void inflateButtons() {
        LoginButtonFragment buttonFragment = new LoginButtonFragment();
        OnBoardingFragment onBoardingFragment = new OnBoardingFragment();
        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.frame_first, buttonFragment, "button_frag");
        transaction.add(R.id.frame_second, onBoardingFragment, "on_board_frag");
        transaction.commit();
    }
}
