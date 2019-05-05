package com.atta.eproperty.main;

public interface MainContract {


    interface View{
        void setProgressDialog();

        void showLoginPopup();

        void showViewError(String view, String error);

        void showMessage(String message);

        void navigateToRegister();

        boolean validate(String email, String password);

        void dismissProgressDialog();
    }

    interface Presenter{



        void login(String email, String password);

    }
}
