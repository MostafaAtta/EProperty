package com.atta.eproperty.main;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.atta.eproperty.QueryUtils;
import com.atta.eproperty.R;
import com.atta.eproperty.favorites_fragment.FavoritesFragment;
import com.atta.eproperty.model.SessionManager;
import com.atta.eproperty.new_property.NewPropertyActivity;
import com.atta.eproperty.profile_fragment.ProfileFragment;
import com.atta.eproperty.register.RegisterActivity;
import com.atta.eproperty.search_fragment.SearchFragment;

public class MainActivity extends AppCompatActivity implements MainContract.View, BottomNavigationView.OnNavigationItemSelectedListener{

    private TextView mTextMessage;

    ProgressDialog progressDialog;

    MainPresenter mainPresenter;

    Dialog loginDialog;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    mTextMessage.setText(R.string.title_search);
                    return true;
                case R.id.navigation_favorites:
                    mTextMessage.setText(R.string.title_saved);
                    return true;
                case R.id.navigation_profile:
                    if (!session.isLoggedIn()){
                        mTextMessage.setText(R.string.title_login);
                    }else{
                        mTextMessage.setText(R.string.title_profile);
                    }

                    return true;
            }
            return false;
        }
    };


    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Session class instance
        session = new SessionManager(this);

        mainPresenter = new MainPresenter(this,this);

        //loading the default fragment
        loadFragment(new SearchFragment());

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        setProgressDialog();
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.navigation_search:
                fragment = new SearchFragment();
                break;

            case R.id.navigation_favorites:
                fragment = new FavoritesFragment();
                break;

            case R.id.navigation_add:

                if (!session.isLoggedIn()){
                    showLoginPopup();
                }else {
                    Intent intent = new Intent(MainActivity.this, NewPropertyActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.navigation_profile:
                fragment = new ProfileFragment();
                break;

        }

        return loadFragment(fragment);
    }


    @Override
    public void showLoginPopup() {

        loginDialog = new Dialog(this);


        Button loginBtn;

        TextView registerBtn;

        loginDialog.setContentView(R.layout.login_popup);

        loginBtn =loginDialog.findViewById(R.id.btn_login);
        registerBtn = loginDialog.findViewById(R.id.btnRegisterScreen);
        final EditText email = loginDialog.findViewById(R.id.email);
        final EditText password = loginDialog.findViewById(R.id.password);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validate(email.getText().toString().trim(), password.getText().toString())) {
                    showMessage("Invalid Login details");
                    return;
                }

                progressDialog.show();
                mainPresenter.login(email.getText().toString(),password.getText().toString());

            }
        });


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                navigateToRegister();
            }
        });


        loginDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loginDialog.show();
    }

    @Override
    public boolean validate(String email, String password) {

        return QueryUtils.validate( email,  password,null, this, this);
    }

    @Override
    public void navigateToRegister() {

        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void setProgressDialog() {

        if(progressDialog != null){
            progressDialog.dismiss();
        }
        progressDialog = new ProgressDialog(MainActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
    }



    @Override
    public void showViewError(String view, String error) {

        int id = getResources().getIdentifier(view, "id", this.getPackageName());
        EditText editText = (EditText)findViewById(id);
        editText.setError(error);
    }

    @Override
    public void showMessage(String message) {

        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void dismissProgressDialog() {

        if(progressDialog != null || progressDialog.isShowing() ){
            progressDialog.dismiss();
        }if(loginDialog != null || loginDialog.isShowing() ){
            loginDialog.dismiss();
        }
    }


}
