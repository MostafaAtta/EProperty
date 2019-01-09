package com.atta.eproperty.fragments;

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
import android.widget.Toast;

import com.atta.eproperty.R;
import com.atta.eproperty.model.PropertiesAdapter;
import com.atta.eproperty.model.Property;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements FragmentsContract.View{


    FragmentsPresenter fragmentsPresenter;

    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        fragmentsPresenter = new FragmentsPresenter(this, getContext());
        //return rootView;
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);


        fragmentsPresenter.getProperties(recyclerView);

        final SwipeRefreshLayout mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        fragmentsPresenter.getProperties(recyclerView);
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );


        return view;
    }

    @Override
    public void showError(String error) {

        Toast.makeText(getContext(),error,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showRecyclerView(ArrayList<Property> properties) {

        PropertiesAdapter myAdapter = new PropertiesAdapter(getContext(), properties, this, fragmentsPresenter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(myAdapter);
    }
}
