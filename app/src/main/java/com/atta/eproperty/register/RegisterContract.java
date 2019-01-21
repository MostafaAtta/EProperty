package com.atta.eproperty.register;

public interface RegisterContract {

    interface View{

        void showMessage(String error);

        void showViewError(String view, String error);

        void navigateToMain();

        void setDialog();
    }

    interface Presenter{

        void register(String name, String email, String password, String phone);

        boolean validate(String name, String email, String password, String passwordConfirm, String phone);
    }
}
