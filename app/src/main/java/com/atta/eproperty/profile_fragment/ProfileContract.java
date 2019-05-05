package com.atta.eproperty.profile_fragment;

public interface ProfileContract {

    interface View{

        void showMessage(String message);

        void showPasswordDialog();

        void showPhoneDialog();

        boolean validate(String oldPassword, String newPassword, String passwordConfirm);

        void dismissPasswordProgressDialog();

        void dismissPhoneProgressDialog();

        void setProgressDialog();

        void updatePhoneText();
    }

    interface Presenter{
        void resetPassword(int userId, String oldPassword, String password) ;

        void updatePhone(int userId, String phone) ;

    }
}
