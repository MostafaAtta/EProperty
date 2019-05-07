package com.atta.eproperty.property_datils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atta.eproperty.R;
import com.atta.eproperty.main.MainActivity;
import com.atta.eproperty.map.ShowMapsActivity;
import com.atta.eproperty.model.APIUrl;
import com.atta.eproperty.model.AmenitiesAdapter;
import com.atta.eproperty.model.Property;
import com.atta.eproperty.model.SessionManager;
import com.atta.eproperty.model.ViewPagerAdapter;
import com.atta.eproperty.new_property.NewPropertyActivity;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PropertyDetailsActivity extends AppCompatActivity implements View.OnClickListener, PropertyDetailsContract.View {

    ProgressDialog progressDialog;

    Property property;

    SessionManager sessionManager;

    private String[] imageUrls;

    ViewPager viewPager;

    FloatingActionButton floatingActionButton;

    TextView priceTv, detailsTv, addressTv, essentialsTv, lifestyleTv, areaTv, creationTimeTv, desTv, avgPrice, category;

    RecyclerView amenitiesRecyclerView;

    ImageView backBtn, favBtn, editBtn;

    LinearLayout locationLinearLayout;

    String schoolUrl, hospitalUrl, restaurantsUrl, cinemaUrl, atmUrl, cafeUrl, pharmacyUrl, parkUrl, shoppingMallUrl, supermarketUrl;

    boolean isFavorite;


    int saleAvgPrice;
    int rentAvgPrice;
    int essentialsResults;
    int lifestyleResults;
    int PROXIMITY_RADIUS = 500;

    double essentialsFactor, lifestyleFactor;


    int favId;

    PropertyDetailsPresenter propertyDetailsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);


        setDialog();

        propertyDetailsPresenter = new PropertyDetailsPresenter(this, this, progressDialog);

        sessionManager = new SessionManager(this);

        property = (Property) getIntent().getSerializableExtra("property");

        propertyDetailsPresenter.checkIfFav(property.getId(), sessionManager.getUserId());

        initiateViews();

        setViews();

        setUrls();

        String[] essentialsUrl = {schoolUrl, hospitalUrl, pharmacyUrl, atmUrl, supermarketUrl};

        String[] essentialsPlace = {"School", "Hospital", "Pharmacy", "ATM", "Supermarket"};

        String[] lifestyleUrl = {restaurantsUrl, cafeUrl, cinemaUrl, parkUrl, shoppingMallUrl};

        String[] lifestylePlace = {"Restaurant", "Cafe", "Cinema", "Park", "Shopping Mall"};

        propertyDetailsPresenter.requestPlaces(essentialsUrl, lifestyleUrl, essentialsPlace, lifestylePlace,  property.getDistrict(), property.getType(), property.getCategory() );

        //propertyDetailsPresenter.requestPlaces(lifestyleUrl, "lifestyle", lifestylePlace);

    }


    @Override
    public void onClick(View v) {
        if (v == locationLinearLayout){

            Intent intent = new Intent(PropertyDetailsActivity.this, ShowMapsActivity.class);
            intent.putExtra("latitude", property.getLatitude());
            intent.putExtra("longitude", property.getLongitude());
            intent.putExtra("type", property.getType());
            startActivity(intent);
        }else if (v == backBtn){
            Intent intent = new Intent(PropertyDetailsActivity.this, MainActivity.class);
            startActivity(intent);
        }else if (v == favBtn){


            if (sessionManager.isLoggedIn()){

                if (isFavorite){

                    progressDialog.setMessage("Removing from Favorites...");
                    propertyDetailsPresenter.removeFromFav(favId);
                }else {

                    progressDialog.setMessage("Adding to your Favorites...");
                    propertyDetailsPresenter.addToFav(property.getId(), sessionManager.getUserId());
                }
                progressDialog.show();
            }else showMessage("you need to login first");
        }else if (v == floatingActionButton){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + property.getOwnerPhone()));
            startActivity(intent);
        }else if (v == editBtn){
            Intent intent = new Intent(PropertyDetailsActivity.this, NewPropertyActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public String getUrl(String nearbyPlace)
    {

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location=").append(property.getLatitude()).append(",").append(property.getLongitude());
        googlePlaceUrl.append("&radius=").append(PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type=").append(nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyBdDl7JNwsC7AuxuDh-ZvwL2pWpW8uBr2E");

        Log.d("MapsActivity", "url = "+googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }

    @Override
    public void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void changeFavIcon(boolean isFav) {

        isFavorite = isFav;
        if (isFav){

            favBtn.setImageResource(R.drawable.star_fill);
        }else {

            favBtn.setImageResource(R.drawable.star);
        }
    }

    @Override
    public void setFavId(int id) {
        favId = id;
    }

    @Override
    public void initiateViews() {

        floatingActionButton = findViewById(R.id.floating);
        viewPager = findViewById(R.id.slider);
        priceTv = findViewById(R.id.price);
        detailsTv = findViewById(R.id.det);
        addressTv = findViewById(R.id.add);
        avgPrice = findViewById(R.id.avg_price);
        category = findViewById(R.id.category);
        essentialsTv = findViewById(R.id.essentials);
        lifestyleTv = findViewById(R.id.lifestyle);
        backBtn = findViewById(R.id.btn_back);
        backBtn.setOnClickListener(this);
        editBtn = findViewById(R.id.edit);
        int userId = sessionManager.getUserId();
        int propUserId = property.getUserId();
        if (userId == propUserId){
            editBtn.setVisibility(View.VISIBLE);
        }
        editBtn.setOnClickListener(this);
        favBtn = findViewById(R.id.fav);
        favBtn.setOnClickListener(this);
        areaTv = findViewById(R.id.area);
        creationTimeTv = findViewById(R.id.creation_time);
        desTv = findViewById(R.id.description);
        amenitiesRecyclerView = findViewById(R.id.grid);
        locationLinearLayout =findViewById(R.id.linear_group_location);
        locationLinearLayout.setOnClickListener(this);
    }

    @Override
    public void setViews() {
        String[] finalUrls;

        if (property.getImagesUrls() != null){

            imageUrls = property.getImagesUrls();


            finalUrls = new String[imageUrls.length];

            for (int i=0; i < imageUrls.length; i++){
                finalUrls[i] = APIUrl.Images_BASE_URL + imageUrls[i];
            }
        }else {
            finalUrls = new String[1];
            finalUrls[0] = APIUrl.Images_BASE_URL + "property_tmp.png";
        }




        ViewPagerAdapter adapter = new ViewPagerAdapter(this, finalUrls);
        viewPager.setAdapter(adapter);

        floatingActionButton.setOnClickListener(this);

        NumberFormat format = NumberFormat.getNumberInstance();
        int price = property.getPrice();
        String priceString = format.format(price) + " EGP";
        String apartmentCategory = " Category: " + property.getCategory();
        priceTv.setText(priceString);
        category.setText(apartmentCategory);
        String area = "Area: " + String.valueOf(property.getArea()) + " " + Html.fromHtml("(m<sup>2</sup>)");
        areaTv.setText(area);
        //long timeInMilliseconds = Long.parseLong(property.getCreationTime());
        //Date dateObject = new Date(timeInMilliseconds*1000L);
        String creationTime = "Creation Time: " + property.getCreationTime();
        creationTimeTv.setText(creationTime);
        String desc = property.getDescription();
        desTv.setText(desc);
        switch (property.getType()){
            case "Land":
            case "Garage":
                String details1 = property.getType();
                detailsTv.setText(details1);
                break;

            case "Building":
                String details2 = property.getType() + ", " + property.getLevels() + " Levels, ";
                detailsTv.setText(details2);
                break;
            default:
                String details3 = property.getType() + ", " + String.valueOf(property.getRooms()) + " Beds, " +
                        String.valueOf(property.getBaths()) + " Baths, ";
                detailsTv.setText(details3);
                break;
        }

        String address = property.getCity() + ", " + property.getDistrict();
        addressTv.setText(address);

        switch (property.getType()){
            case "Land":
            case "Garage":
            case "Building":
                amenitiesRecyclerView.setVisibility(View.GONE);
                findViewById(R.id.amenities_view).setVisibility(View.GONE);
                findViewById(R.id.amenities_text).setVisibility(View.GONE);
                break;
            default:
                String[] amenitiesArray = property.getAmenitiesArray();

                amenitiesRecyclerView.setVisibility(View.VISIBLE);
                amenitiesRecyclerView = findViewById(R.id.grid);
                amenitiesRecyclerView.setAdapter(new AmenitiesAdapter(amenitiesArray));
                amenitiesRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                break;
        }



    }


    @Override
    public void setDialog() {

        if(progressDialog != null){
            progressDialog.dismiss();
        }
        progressDialog = new ProgressDialog(PropertyDetailsActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
    }

    @Override
    public void setViews(String result, String type) {

        switch (type){
            case "essentials":

                essentialsTv.setText(result);
                break;
            case "lifestyle":

                lifestyleTv.setText(result);
                break;

            default:
                break;
        }
    }



    @Override
    public void setUrls() {

        schoolUrl = getUrl("school");
        hospitalUrl = getUrl("hospital");
        pharmacyUrl = getUrl("pharmacy");
        atmUrl = getUrl( "atm");
        supermarketUrl = getUrl("supermarket");

        restaurantsUrl = getUrl( "restaurant");
        cinemaUrl = getUrl( "movie_theater");
        cafeUrl = getUrl("cafe");
        parkUrl = getUrl( "park");
        shoppingMallUrl = getUrl( "shopping_mall");

    }


    @Override
    public String formatDateTime(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy h:mm a");
        return dateFormat.format(dateObject);
    }

    @Override
    public void setSaleAvgPrice(int price) {

        saleAvgPrice = (int) (price * (essentialsFactor + lifestyleFactor));

        Toast.makeText(this, String.valueOf(saleAvgPrice), Toast.LENGTH_SHORT).show();

        String priceAvgString = "Average Price: " + saleAvgPrice + " EGP per meter";

        avgPrice.setText(priceAvgString);



    }

    @Override
    public void setRentAvgPrice(int price) {

        rentAvgPrice = (int) (price  * (essentialsFactor + lifestyleFactor));

        Toast.makeText(this, String.valueOf(rentAvgPrice), Toast.LENGTH_SHORT).show();

        String priceAvgString = "Average Price: " + rentAvgPrice + " EGP per meter";

        avgPrice.setText(priceAvgString);

    }


    @Override
    public void setLifestyleAvg(int length) {

        lifestyleResults = length;

        if (lifestyleResults >= 15){

            lifestyleFactor = 0.4;

        }else if (lifestyleResults >= 10){

            lifestyleFactor = 0.35;

        }else if (lifestyleResults >= 5){

            lifestyleFactor = 0.3;

        }else {

            lifestyleFactor = 0.25;

        }
    }

    @Override
    public void setEssentialsAvg(int length) {

        essentialsResults = length;
        if (essentialsResults >= 30){

            essentialsFactor = 0.8;

        }else if (essentialsResults >= 20){

            essentialsFactor = 0.7;

        }else if (essentialsResults >= 10){

            essentialsFactor = 0.6;

        }else {

            essentialsFactor = 0.55;

        }
    }

}
