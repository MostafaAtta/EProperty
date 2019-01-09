package com.atta.eproperty.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.atta.eproperty.R;
import com.atta.eproperty.main.MainActivity;

public class RegisterActivity extends AppCompatActivity implements RegisterContract.View, View.OnClickListener {


    ProgressDialog progressDialog;

    // register button
    Button register;

    // National ID, password edit text
    EditText emailText, passwordText, nameText, phoneText, confirmPasswordText;

    private RegisterPresenter registerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        setDialog();

        registerPresenter = new RegisterPresenter(this, progressDialog, this);

        initiateViews();
    }


    private void initiateViews() {

        // National ID, Password input text
        emailText = (EditText)findViewById(R.id.email);
        passwordText = (EditText)findViewById(R.id.password);
        nameText = (EditText) findViewById(R.id.name);
        phoneText = (EditText) findViewById(R.id.phone);
        confirmPasswordText = (EditText) findViewById(R.id.password_confirm);


        // Register button
        register = (Button)findViewById(R.id.btnRegister);
        register.setOnClickListener(this);

    }

    @Override
    public void showMessage(String error) {

        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showViewError(String view, String error) {

        int id = getResources().getIdentifier(view, "id", this.getPackageName());
        EditText editText = (EditText)findViewById(id);
        editText.setError(error);
    }

    @Override
    public void navigateToMain() {

        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public void setDialog() {

        if(progressDialog != null){
            progressDialog.dismiss();
        }
        progressDialog = new ProgressDialog(RegisterActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating your profile...");
    }

    @Override
    public void onClick(View view) {
        if(view == register) {
            if (!registerPresenter.validate(nameText.getText().toString(), emailText.getText().toString(), passwordText.getText().toString(),
                    confirmPasswordText.getText().toString(), phoneText.getText().toString())) {
                register.setEnabled(true);
                return;
            }


            progressDialog.show();
            registerPresenter.register(nameText.getText().toString(), emailText.getText().toString(), passwordText.getText().toString(),
                    phoneText.getText().toString());
        }
    }
}
