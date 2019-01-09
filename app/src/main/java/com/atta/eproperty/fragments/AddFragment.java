package com.atta.eproperty.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.atta.eproperty.R;
import com.atta.eproperty.model.SessionManager;

import java.util.Arrays;
import java.util.List;

public class AddFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    SessionManager session;

    EditText prceText, areaText;

    LinearLayout propertyGroup;

    Spinner typeSpinner;

    ArrayAdapter<String> typeAdapter;

    List<String> types;

    private int price, rooms, baths, area;
    private String imageUrl, address, district, city, governance, type, description, ownerPhone;
    private float latitude, longitude;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        //return rootView;
        View view = inflater.inflate(R.layout.fragment_add,container,false);

        propertyGroup = view.findViewById(R.id.property_group);

        // Session class instance
        session = new SessionManager(getContext());

        if (!session.isLoggedIn()){
            propertyGroup.setVisibility(View.GONE);
            showLoginPopup();
        }

        areaText = view.findViewById(R.id.area);
        typeSpinner = view.findViewById(R.id.type);

        areaText.setHint(Html.fromHtml("Area (m<sup>2</sup>)"));


        String[] typesArray = getResources().getStringArray(R.array.types);


        types = Arrays.asList(typesArray);


        typeAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        typeSpinner.setOnItemSelectedListener(this);

        return view;
    }

    private void showLoginPopup() {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
