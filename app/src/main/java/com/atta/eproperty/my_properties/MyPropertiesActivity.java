package com.atta.eproperty.my_properties;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.atta.eproperty.R;
import com.atta.eproperty.model.PropertiesAdapter;
import com.atta.eproperty.model.Property;
import com.atta.eproperty.model.SessionManager;
import com.atta.eproperty.new_property.NewPropertyActivity;

import java.util.ArrayList;

public class MyPropertiesActivity extends AppCompatActivity implements MyPropertiesContract.View, View.OnClickListener {


    MyPropertiesPresenter myPropertiesPresenter;

    RecyclerView recyclerView;

    SessionManager sessionManager;

    TextView infoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_properties);


        myPropertiesPresenter = new MyPropertiesPresenter(this, this);

        sessionManager = new SessionManager(this);

        recyclerView = findViewById(R.id.my_prop_recycler);

        infoTextView = findViewById(R.id.my_prop_info_tv);

        infoTextView.setOnClickListener(this);

        myPropertiesPresenter.getMyProperties(sessionManager.getUserId());

        final SwipeRefreshLayout mySwipeRefreshLayout = findViewById(R.id.my_prop_refresh);

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        myPropertiesPresenter.getMyProperties(sessionManager.getUserId());
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }

    @Override
    public void showError(String error) {

        Toast.makeText(this,error,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showRecyclerView(ArrayList<Property> properties) {

        recyclerView.setVisibility(View.VISIBLE);
        infoTextView.setVisibility(View.GONE);
        PropertiesAdapter myAdapter = new PropertiesAdapter(this, properties);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void updateText() {
        recyclerView.setVisibility(View.GONE);
        infoTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v == infoTextView){
            Intent intent = new Intent(MyPropertiesActivity.this, NewPropertyActivity.class);
            startActivity(intent);
        }
    }
}
