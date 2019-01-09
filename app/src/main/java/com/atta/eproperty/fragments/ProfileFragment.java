package com.atta.eproperty.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.atta.eproperty.R;
import com.atta.eproperty.register.RegisterActivity;
import com.atta.eproperty.login.LoginActivity;
import com.atta.eproperty.model.SessionManager;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    SessionManager session;

    Button loginBtn, registerBtn, logoutBtn;

    LinearLayout linearLayoutLoggedIn, linearLayoutLoggedOut;

    AppCompatTextView nameLetter, userName, userEmail, phoneText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        //return rootView;
        View view =  inflater.inflate(R.layout.fragment_profile, null);

        loginBtn = view.findViewById(R.id.login_btn);
        registerBtn = view.findViewById(R.id.register_btn);
        logoutBtn = view.findViewById(R.id.logout_btn);
        linearLayoutLoggedIn = view.findViewById(R.id.linear_group_logged_in);
        linearLayoutLoggedOut = view.findViewById(R.id.linear_group_logged_out);
        phoneText = view.findViewById(R.id.phone);
        nameLetter = view.findViewById(R.id.name_letter);
        userName = view.findViewById(R.id.user_name);
        userEmail = view.findViewById(R.id.user_email);


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
        }
    }
}
