package com.atta.eproperty.favorites_fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.atta.eproperty.R;
import com.atta.eproperty.model.PropertiesAdapter;
import com.atta.eproperty.model.Property;
import com.atta.eproperty.model.SessionManager;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment implements FavoritesContract.View{


    FavoritesPresenter favoritesPresenter;

    RecyclerView recyclerView;

    SessionManager sessionManager;

    TextView infoTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        favoritesPresenter = new FavoritesPresenter(this, getContext());

        sessionManager = new SessionManager(getContext());

        //return rootView;
        View view = inflater.inflate(R.layout.fragment_favorites,container,false);

        recyclerView = view.findViewById(R.id.fav_recycler);

        infoTextView = view.findViewById(R.id.info_tv);

        if (sessionManager.isLoggedIn()){

            favoritesPresenter.getFavProperties(sessionManager.getUserId());

            final SwipeRefreshLayout mySwipeRefreshLayout = view.findViewById(R.id.fav_refresh);

            mySwipeRefreshLayout.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            favoritesPresenter.getFavProperties(sessionManager.getUserId());
                            mySwipeRefreshLayout.setRefreshing(false);
                        }
                    }
            );
        }else {
            infoTextView.setText("You need to login to see your favorites");
        }



        return view;
    }

    @Override
    public void showError(String error) {

        Toast.makeText(getContext(),error,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showRecyclerView(ArrayList<Property> properties) {

        recyclerView.setVisibility(View.VISIBLE);
        infoTextView.setVisibility(View.GONE);
        PropertiesAdapter myAdapter = new PropertiesAdapter(getContext(), properties);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void updateText() {
        recyclerView.setVisibility(View.GONE);
        infoTextView.setVisibility(View.VISIBLE);
        infoTextView.setText("No properties added to your favorites");
    }
}
