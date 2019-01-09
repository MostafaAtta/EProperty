package com.atta.eproperty.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.atta.eproperty.fragments.AddFragment;
import com.atta.eproperty.fragments.FavoritesFragment;
import com.atta.eproperty.fragments.ProfileFragment;
import com.atta.eproperty.R;
import com.atta.eproperty.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity implements MainContract.View, BottomNavigationView.OnNavigationItemSelectedListener {

    private TextView mTextMessage;

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
                    mTextMessage.setText(R.string.title_profile);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //loading the default fragment
        loadFragment(new SearchFragment());

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
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
                fragment = new AddFragment();
                break;

            case R.id.navigation_profile:
                fragment = new ProfileFragment();
                break;

        }

        return loadFragment(fragment);
    }
}
