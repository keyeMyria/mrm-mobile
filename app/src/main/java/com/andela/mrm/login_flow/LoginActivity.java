package com.andela.mrm.login_flow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.andela.mrm.Injection;
import com.andela.mrm.R;
import com.andela.mrm.room_setup.country.CountryActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

/**
 * LoginActivity class.
 */
public class LoginActivity extends AppCompatActivity implements LoginButtonFragment.Callbacks,
        LoginContract.View {
    private static final int REQUEST_CODE_SIGN_IN = 2002;

    private LoginContract.Presenter mPresenter;
    private FragmentManager manager;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPresenter = new LoginPresenter(this, Injection
                .provideGoogleSignInWrapperUtil(getApplicationContext()));
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    /**
     * Inflates the login button fragment.
     */
    private void inflateButtons() {
        LoginButtonFragment buttonFragment = new LoginButtonFragment();
        OnBoardingFragment onBoardingFragment = new OnBoardingFragment();
        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.frame_first, buttonFragment);
        transaction.add(R.id.frame_second, onBoardingFragment);
        transaction.commit();
    }

    @Override
    public void onClickLoginButton() {
        mPresenter.login();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            mPresenter.accountChosen(data);
        }
    }


    @Override
    public void showChooseAccountDialog() {
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, REQUEST_CODE_SIGN_IN);
    }

    @Override
    public void startNextActivity() {
        startActivity(CountryActivity.newIntent(this));
    }

    @Override
    public void showLoginButton() {
        inflateButtons();
    }
}
