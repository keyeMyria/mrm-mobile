package com.andela.mrm.login_flow;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

/**
 * The type Login contract.
 */
public class LoginContract {
    /**
     * The interface View.
     */
    interface View {
        /**
         * Show choose account dialog.
         */
        void showChooseAccountDialog();

        /**
         * Start next activity.
         */
        void startNextActivity();

        /**
         * Show login button.
         */
        void showLoginButton();
    }

    /**
     * The interface Presenter.
     */
    interface Presenter {
        /**
         * Start.
         */
        void start();

        /**
         * Login.
         */
        void login();

        /**
         * Account chosen.
         *
         * @param intent the intent
         */
        void accountChosen(Intent intent);
    }

    /**
     * A wrapper interface for the GoogleSignIn static methods.
     */
    public interface GoogleSignInWrapperUtil {
        /**
         * Gets signed in account.
         *
         * @return the signed in account
         */
        GoogleSignInAccount getSignedInAccount();

        /**
         * Gets signed in account from intent.
         *
         * @param intent the intent
         * @return the signed in account from intent
         */
        Task<GoogleSignInAccount> getSignedInAccountFromIntent(Intent intent);

    }
}
