package com.atta.eproperty.login;

public interface LoginContract {

    interface View{

        void initiateViews();

        void showError(String error);

        void showViewError(String view, String error);

        void showMessage();

        void navigateToMain();

        void navigateToRegister();

        void setDialog();

        void SkipToMain();

        void dismissProgressDialog();
    }

    interface Presenter{

        void login(String email, String password);

        boolean validate(String email, String password);
    }
}
