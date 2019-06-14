package com.atta.eproperty.search_fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.atta.eproperty.R;
import com.atta.eproperty.model.PropertiesAdapter;
import com.atta.eproperty.model.Property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.apptik.widget.MultiSlider;

public class SearchFragment extends Fragment implements SearchContract.View, View.OnClickListener, AdapterView.OnItemSelectedListener {


    SearchPresenter searchPresenter;

    RecyclerView recyclerView;

    ImageView filterImage;

    TextView priceRangeTxt, areaRangeTxt;

    List<String> types, categories, cities, districts;

    Spinner typeSpinner, categorySpinner, citySpinner, districtSpinner;

    ArrayAdapter<String> typeAdapter, categoryAdapter, cityAdapter, districtAdapter;

    String category, type, city, district;

    Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        searchPresenter = new SearchPresenter(this, getContext());
        //return rootView;
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        recyclerView = view.findViewById(R.id.recycler);
        filterImage = view.findViewById(R.id.filter);
        filterImage.setOnClickListener(this);

        searchPresenter.getProperties();

        final SwipeRefreshLayout mySwipeRefreshLayout = view.findViewById(R.id.swiperefresh);

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        searchPresenter.getProperties();
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

        if (dialog != null){
            dialog.dismiss();
        }

        PropertiesAdapter myAdapter = new PropertiesAdapter(getContext(), properties);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onClick(View v) {

        dialog = new Dialog(getContext());

        dialog.setContentView(R.layout.filter_popup);

        Button filterBtn;

        priceRangeTxt = dialog.findViewById(R.id.price_range_txt);
        areaRangeTxt = dialog.findViewById(R.id.area_range_txt);

        MultiSlider priceMultiSlider = dialog.findViewById(R.id.price_range_slider);
        MultiSlider areaMultiSlider = dialog.findViewById(R.id.area_range_slider);

        final String[] maxPriceValue = new String[1];
        final String[] minPriceValue = new String[1];

        minPriceValue[0] = String.valueOf(2000);
        maxPriceValue[0] = String.valueOf(5000000);


        final String[] maxAreaValue = new String[1];
        final String[] minAreaValue = new String[1];

        minAreaValue[0] = String.valueOf(50);
        maxAreaValue[0] = String.valueOf(500);

        priceMultiSlider.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider,
                                       MultiSlider.Thumb thumb,
                                       int thumbIndex,
                                       int value)
            {
                if (thumbIndex == 0) {
                    setPriceRange(maxPriceValue[0], String.valueOf(value));
                    minPriceValue[0] = String.valueOf(value);
                } else {
                    setPriceRange(String.valueOf(value), minPriceValue[0]);
                    maxPriceValue[0] = String.valueOf(value);
                }
            }
        });


        areaMultiSlider.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider,
                                       MultiSlider.Thumb thumb,
                                       int thumbIndex,
                                       int value)
            {
                if (thumbIndex == 0) {
                    setAreaRange(maxAreaValue[0], String.valueOf(value));
                    minAreaValue[0] = String.valueOf(value);
                } else {
                    setAreaRange(String.valueOf(value), minAreaValue[0]);
                    maxAreaValue[0] = String.valueOf(value);
                }
            }
        });

        categorySpinner = dialog.findViewById(R.id.category_spinner);
        typeSpinner = dialog.findViewById(R.id.type_spinner);
        citySpinner = dialog.findViewById(R.id.city_spinner);
        districtSpinner = dialog.findViewById(R.id.district_spinner);

        initiateArrays();

        categoryAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(this);

        cityAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setOnItemSelectedListener(this);


        typeAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        typeSpinner.setOnItemSelectedListener(this);


        districtAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, districts);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(districtAdapter);
        districtSpinner.setOnItemSelectedListener(this);

        filterBtn = dialog.findViewById(R.id.filter_btn);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPresenter.filterProperties(Integer.valueOf(minPriceValue[0]), Integer.valueOf(maxPriceValue[0]), Integer.valueOf(minAreaValue[0]), Integer.valueOf(maxAreaValue[0]), district,
                        city, type, category);
                /*searchPresenter.filterProperties(500000, 5000000, 50, 500, district,
                        city, type, category);*/
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    private void setPriceRange(String maxValue, String minValue) {

        priceRangeTxt.setVisibility(View.VISIBLE);
        if (minValue.equals("2000")){
            priceRangeTxt.setText("up to" + maxValue + "EGP");
        }else {
            priceRangeTxt.setText(minValue + " - " + maxValue + "EGP");
        }

    }


    private void setAreaRange(String maxValue, String minValue) {

        areaRangeTxt.setVisibility(View.VISIBLE);
        if (minValue.equals("50")){
            areaRangeTxt.setText("up to" + maxValue + Html.fromHtml(" m<sup>2</sup>"));
        }else {
            areaRangeTxt.setText(minValue + " - " + maxValue + Html.fromHtml(" m<sup>2</sup>"));
        }

    }

    @Override
    public void initiateArrays() {

        String[] typesArray = getResources().getStringArray(R.array.types2);

        types = Arrays.asList(typesArray);


        String[] categoriesArray = {"Select category", "For Rent", "For Sale"};

        categories = Arrays.asList(categoriesArray);



        String[] districtsArray = {"Select district", "Nasr city", "6 of October"};

        districts = Arrays.asList(districtsArray);

        String[] citiesArray = getResources().getStringArray(R.array.cities);

        cities = Arrays.asList(citiesArray);
    }

    @Override
    public void dismissDialog() {

        if (dialog != null){
            dialog.dismiss();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == categorySpinner.getId()){
            if (i != 0){

                category = categories.get(i);


            }else{
                category = "%";
            }

        }else if (adapterView.getId() == citySpinner.getId()){
            if (i != 0){

                city = cities.get(i);

            }else{
                city = "%";
            }

        }else if (adapterView.getId() == typeSpinner.getId()){
            if (i != 0){

                type = types.get(i);

            }else{
                type = "%";
            }

        }else if (adapterView.getId() == districtSpinner.getId()){
            if (i != 0){

                district = districts.get(i);

            }else{
                district = "%";
            }

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        category = "%";
        city = "%";
        type = "%";
        district = "%";
    }


}
