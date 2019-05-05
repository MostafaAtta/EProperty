package com.atta.eproperty.profile_fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.atta.eproperty.R;
import com.atta.eproperty.login.LoginActivity;
import com.atta.eproperty.model.SessionManager;
import com.atta.eproperty.my_properties.MyPropertiesActivity;
import com.atta.eproperty.register.RegisterActivity;

public class ProfileFragment extends Fragment implements View.OnClickListener, ProfileContract.View {

    SessionManager session;

    ProfilePresenter profilePresenter;

    Button loginBtn, registerBtn, logoutBtn, myAddsBtn, resetPasswordBtn, updatePhoneBtn;

    LinearLayout linearLayoutLoggedIn, linearLayoutLoggedOut;

    AppCompatTextView nameLetter, userName, userEmail, phoneText;

    Dialog passwordDialog, phoneDialog;

    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        //return rootView;
        View view =  inflater.inflate(R.layout.fragment_profile, null);

        profilePresenter = new ProfilePresenter(this, getContext());

        loginBtn = view.findViewById(R.id.login_btn);
        registerBtn = view.findViewById(R.id.register_btn);
        logoutBtn = view.findViewById(R.id.logout_btn);
        myAddsBtn = view.findViewById(R.id.my_adds_btn);
        resetPasswordBtn = view.findViewById(R.id.reset_password_btn);
        updatePhoneBtn = view.findViewById(R.id.update_phone_btn);
        linearLayoutLoggedIn = view.findViewById(R.id.linear_group_logged_in);
        linearLayoutLoggedOut = view.findViewById(R.id.linear_group_logged_out);
        phoneText = view.findViewById(R.id.phone);
        nameLetter = view.findViewById(R.id.name_letter);
        userName = view.findViewById(R.id.user_name);
        userEmail = view.findViewById(R.id.user_email);

        setProgressDialog();


        // Session class instance
        session = new SessionManager(getContext());

        if (!session.isLoggedIn()){
            linearLayoutLoggedIn.setVisibility(View.GONE);
            linearLayoutLoggedOut.setVisibility(View.VISIBLE);
            loginBtn.setOnClickListener(this);
            registerBtn.setOnClickListener(this);

        }else {

            linearLayoutLoggedIn.setVisibility(View.VISIBLE);
            linearLayoutLoggedOut.setVisibility(View.GONE);
            logoutBtn.setOnClickListener(this);
            myAddsBtn.setOnClickListener(this);
            resetPasswordBtn.setOnClickListener(this);
            updatePhoneBtn.setOnClickListener(this);


            String name = SessionManager.getInstance(getContext()).getUserName().toUpperCase();
            userName.setText(name);
            nameLetter.setText(name.substring(0,1));
            userEmail.setText(SessionManager.getInstance(getContext()).getEmail());
            phoneText.setText(SessionManager.getInstance(getContext()).getPhone());

        }

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == loginBtn){
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }else if (view == registerBtn){

            Intent intent = new Intent(getContext(), RegisterActivity.class);
            startActivity(intent);
        }else if (view == logoutBtn){

            session.logoutUser();
            linearLayoutLoggedIn.setVisibility(View.GONE);
            linearLayoutLoggedOut.setVisibility(View.VISIBLE);
            loginBtn.setOnClickListener(this);
            registerBtn.setOnClickListener(this);
        }else if (view == myAddsBtn){

            Intent intent = new Intent(getContext(), MyPropertiesActivity.class);
            startActivity(intent);
        }else if (view == resetPasswordBtn){

            showPasswordDialog();

        }else if (view == updatePhoneBtn){

            showPhoneDialog();
        }
    }

    @Override
    public void showMessage(String message) {

        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showPasswordDialog() {

        passwordDialog = new Dialog(getContext());


        Button resetBtn;

        passwordDialog.setContentView(R.layout.password_popup);

        resetBtn =passwordDialog.findViewById(R.id.btn_reset);
        final EditText oldPassword = passwordDialog.findViewById(R.id.old_password);
        final EditText newPassword = passwordDialog.findViewById(R.id.new_password);
        final EditText newPasswordConfirm = passwordDialog.findViewById(R.id.new_password_confirm);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validate(oldPassword.getText().toString(), newPassword.getText().toString(), newPasswordConfirm.getText().toString())) {

                    return;
                }

                progressDialog.setMessage("updating Password...");
                progressDialog.show();
                profilePresenter.resetPassword(session.getUserId(),oldPassword.getText().toString(), newPassword.getText().toString());

            }
        });




        passwordDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        passwordDialog.show();
    }

    @Override
    public void showPhoneDialog() {

        phoneDialog = new Dialog(getContext());


        Button updateBtn;

        phoneDialog.setContentView(R.layout.phone_popup);

        updateBtn = phoneDialog.findViewById(R.id.update_phone_btn);
        final EditText phone = phoneDialog.findViewById(R.id.update_phone);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String phoneNumber = phone.getText().toString().trim();
                if (phoneNumber.isEmpty() || phoneNumber.length()!= 11) {

                    showMessage("Enter a valid Phone number");
                    return;
                }

                progressDialog.setMessage("updating Phone...");
                progressDialog.show();
                profilePresenter.updatePhone(session.getUserId(), phoneNumber);

            }
        });




        phoneDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        phoneDialog.show();
    }


    @Override
    public boolean validate(String oldPassword, String newPassword, String passwordConfirm) {

        boolean valid = true;


        if (oldPassword.isEmpty() || oldPassword.length() < 4 || oldPassword.length() > 10) {
            showMessage("wrong old password, Passwords must be between 4 and 10 alphanumeric characters");
            valid = false;
        }else if (newPassword.isEmpty() || newPassword.length() < 4 || newPassword.length() > 10) {
            showMessage("wrong new password, Passwords  must be between 4 and 10 alphanumeric characters");
            valid = false;
        } else if (passwordConfirm.isEmpty() || passwordConfirm.length() < 4 || passwordConfirm.length() > 10 ) {

            showMessage("wrong confirm password, Passwords  must be between 4 and 10 alphanumeric characters");
            valid = false;
        } else if (!newPassword.equals(passwordConfirm)){

            showMessage("new password and confirm password not Matched");
            valid = false;
        }
        return valid;
    }
    @Override
    public void setProgressDialog() {

        if(progressDialog != null){
            progressDialog.dismiss();
        }
        progressDialog = new ProgressDialog(getContext(),R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
    }

    @Override
    public void dismissPasswordProgressDialog() {

        if(progressDialog != null || progressDialog.isShowing() ){
            progressDialog.dismiss();
        }
        if(passwordDialog != null || passwordDialog.isShowing() ){
            passwordDialog.dismiss();
        }
    }

    @Override
    public void dismissPhoneProgressDialog() {

        if(progressDialog != null || progressDialog.isShowing() ){
            progressDialog.dismiss();
        }
        if(phoneDialog != null || phoneDialog.isShowing() ){
            phoneDialog.dismiss();
        }
    }

    @Override
    public void updatePhoneText() {

        phoneText.setText(session.getPhone());
    }
}
