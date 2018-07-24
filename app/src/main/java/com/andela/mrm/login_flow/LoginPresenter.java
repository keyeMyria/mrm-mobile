package com.andela.mrm.login_flow;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

/**
 * The Login presenter class.
 */
class LoginPresenter implements LoginContract.Presenter {
    private final LoginContract.View mView;
    private final LoginContract.GoogleSignInWrapperUtil mSignInWrapperUtil;

    /**
     * Instantiates a new Login presenter.
     *
     * @param view the view
     * @param util the util
     */
    LoginPresenter(LoginContract.View view, LoginContract.GoogleSignInWrapperUtil util) {
        mView = view;
        mSignInWrapperUtil = util;
    }

    @Override
    public void start() {
        GoogleSignInAccount signedInAccount = mSignInWrapperUtil.getSignedInAccount();
        if (signedInAccount == null) {
            mView.showLoginButton();
            return;
        }
        mView.startNextActivity();
    }

    @Override
    public void login() {
        mView.showChooseAccountDialog();
    }

    @Override
    public void accountChosen(Intent intent) {
        Task<GoogleSignInAccount> signedInAccountFromIntent =
                mSignInWrapperUtil.getSignedInAccountFromIntent(intent);
        try {
            signedInAccountFromIntent.getResult(ApiException.class);
        } catch (ApiException e) {
            return;
        }
        mView.startNextActivity();
    }
}
