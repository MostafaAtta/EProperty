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

    TextView priceTv, detailsTv, addressTv, essentialsTv, lifestyleTv, areaTv, creationTimeTv, desTv;

    RecyclerView amenitiesRecyclerView;

    ImageView backBtn, favBtn;

    LinearLayout locationLinearLayout;

    String schoolUrl, hospitalUrl, restaurantsUrl, cinemaUrl, atmUrl, cafeUrl, pharmacyUrl, parkUrl, shoppingMallUrl, supermarketUrl;

    boolean isFavorite;

    int PROXIMITY_RADIUS = 500;

    PropertyDetailsPresenter propertyDetailsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);


        setDialog();

        propertyDetailsPresenter = new PropertyDetailsPresenter(this, this, progressDialog);

        sessionManager = new SessionManager(this);

        property = (Property) getIntent().getSerializableExtra("property");

        propertyDetailsPresenter.checkIfFav(property.getPropertyId(), sessionManager.getUserId());

        initiateViews();

        setViews();

        setUrls();

        String[] essentialsUrl = {schoolUrl, hospitalUrl, pharmacyUrl, atmUrl, supermarketUrl};

        String[] essentialsPlace = {"School", "Hospital", "Pharmacy", "ATM", "Supermarket"};

        String[] lifestyleUrl = {restaurantsUrl, cafeUrl, cinemaUrl, parkUrl, shoppingMallUrl};

        String[] lifestylePlace = {"Restaurant", "Cafe", "Cinema", "Park", "Shopping Mall"};

        propertyDetailsPresenter.requestPlaces(essentialsUrl,"essentials", essentialsPlace);

        propertyDetailsPresenter.requestPlaces(lifestyleUrl, "lifestyle", lifestylePlace);

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

            progressDialog.show();
            if (isFavorite){

                propertyDetailsPresenter.removeFromFav(property.getPropertyId(), sessionManager.getUserId());
            }else {

                propertyDetailsPresenter.addToFav(property.getPropertyId(), sessionManager.getUserId());
            }
        }else if (v == floatingActionButton){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + property.getOwnerPhone()));
            startActivity(intent);
        }
    }

    @Override
    public String getUrl(double latitude, double longitude, String nearbyPlace)
    {

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location=").append(latitude).append(",").append(longitude);
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
    public void initiateViews() {

        floatingActionButton = findViewById(R.id.floating);
        viewPager = findViewById(R.id.slider);
        priceTv = findViewById(R.id.price);
        detailsTv = findViewById(R.id.det);
        addressTv = findViewById(R.id.add);
        essentialsTv = findViewById(R.id.essentials);
        lifestyleTv = findViewById(R.id.lifestyle);
        backBtn = findViewById(R.id.btn_back);
        backBtn.setOnClickListener(this);
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


        imageUrls = property.getImagesUrls();

        String[] finalUrls = new String[imageUrls.length];

        for (int i=0; i < imageUrls.length; i++){
            finalUrls[i] = APIUrl.Images_BASE_URL + "images/" + imageUrls[i];
        }


        ViewPagerAdapter adapter = new ViewPagerAdapter(this, finalUrls);
        viewPager.setAdapter(adapter);

        floatingActionButton.setOnClickListener(this);

        NumberFormat format = NumberFormat.getNumberInstance();
        int price = property.getPrice();
        String priceString = format.format(price) + " EGP";
        priceTv.setText(priceString);
        String area = "Area: " + String.valueOf(property.getArea()) + " " + Html.fromHtml("(m<sup>2</sup>)");
        areaTv.setText(area);
        long timeInMilliseconds = Long.parseLong(property.getCreationTime());
        Date dateObject = new Date(timeInMilliseconds*1000L);
        String creationTime = "Creation Time: " + formatDateTime(dateObject);
        creationTimeTv.setText(creationTime);
        String desc = property.getDescription();
        desTv.setText(desc);
        String details = property.getType() + ", " + String.valueOf(property.getRooms()) + " Beds, " +
                String.valueOf(property.getBaths()) + " Baths, ";
        detailsTv.setText(details);
        String address = property.getCity() + ", " + property.getDistrict();
        addressTv.setText(address);

        String[] amenitiesArray = property.getAmenitiesArray();

        amenitiesRecyclerView = findViewById(R.id.grid);
        amenitiesRecyclerView.setAdapter(new AmenitiesAdapter(amenitiesArray));
        amenitiesRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));


    }


    @Override
    public void setDialog() {

        if(progressDialog != null){
            progressDialog.dismiss();
        }
        progressDialog = new ProgressDialog(PropertyDetailsActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Adding your Favorites...");
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

        schoolUrl = getUrl(property.getLatitude(), property.getLongitude(), "school");
        hospitalUrl = getUrl(property.getLatitude(), property.getLongitude(), "hospital");
        pharmacyUrl = getUrl(property.getLatitude(), property.getLongitude(), "pharmacy");
        atmUrl = getUrl(property.getLatitude(), property.getLongitude(), "atm");
        supermarketUrl = getUrl(property.getLatitude(), property.getLongitude(), "supermarket");

        restaurantsUrl = getUrl(property.getLatitude(), property.getLongitude(), "restaurant");
        cinemaUrl = getUrl(property.getLatitude(), property.getLongitude(), "movie_theater");
        cafeUrl = getUrl(property.getLatitude(), property.getLongitude(), "cafe");
        parkUrl = getUrl(property.getLatitude(), property.getLongitude(), "park");
        shoppingMallUrl = getUrl(property.getLatitude(), property.getLongitude(), "shopping_mall");

    }


    @Override
    public String formatDateTime(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy h:mm a");
        return dateFormat.format(dateObject);
    }
}
