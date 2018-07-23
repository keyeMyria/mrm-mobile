package com.andela.mrm.login_flow;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginPresenterTest {

    @Mock
    private LoginContract.View mView;

    @Mock
    private LoginContract.GoogleSignInWrapperUtil mSignInWrapperUtil;

    private LoginPresenter mLoginPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mLoginPresenter = new LoginPresenter(mView, mSignInWrapperUtil);
    }

    /**
     * Start calls view show login button when getSignedInAccount returns null.
     */
    @Test
    public void start_callsViewShowLoginButton() {
        when(mSignInWrapperUtil.getSignedInAccount()).thenReturn(null);
        mLoginPresenter.start();
        verify(mView).showLoginButton();
    }

    /**
     * Start calls view show login button when getSignedInAccount returns an account
     */
    @Test
    public void start_callsViewShowNextActivity() {
        GoogleSignInAccount googleSignInAccount = mock(GoogleSignInAccount.class);
        when(mSignInWrapperUtil.getSignedInAccount()).thenReturn(googleSignInAccount);
        mLoginPresenter.start();
        verify(mView).startNextActivity();
    }

    @Test
    public void login() {
        mLoginPresenter.login();
        verify(mView).showChooseAccountDialog();
    }

    /**
     * Account chosen does not start next activity when getResult throws an exception.
     *
     * @throws Throwable the throwable
     */
    @Test
    public void accountChosen_doesNotStartNextActivity() throws Throwable {
        Intent intent = mock(Intent.class);
        Task task = mock(Task.class);
        ApiException mock = mock(ApiException.class);

        when(mSignInWrapperUtil.getSignedInAccountFromIntent(intent)).thenReturn(task);
        when(task.getResult(ApiException.class)).thenThrow(mock);

        mLoginPresenter.accountChosen(intent);

        verify(mView, never()).startNextActivity();
    }

    @Test
    public void accountChosen_callsStartNextActivity() throws Throwable {
        Intent intent = mock(Intent.class);
        Task task = mock(Task.class);

        when(mSignInWrapperUtil.getSignedInAccountFromIntent(intent)).thenReturn(task);
        when(task.getResult(ApiException.class)).thenReturn(any());

        mLoginPresenter.accountChosen(intent);

        verify(mView).startNextActivity();
    }
}