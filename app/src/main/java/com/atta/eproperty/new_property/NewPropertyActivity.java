package com.atta.eproperty.new_property;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.atta.eproperty.R;
import com.atta.eproperty.main.MainActivity;
import com.atta.eproperty.map.MapsActivity;
import com.atta.eproperty.model.ImagesAdapter;
import com.atta.eproperty.model.Property;
import com.atta.eproperty.model.SessionManager;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewPropertyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener,
        NewPropertyContract.View{

    SessionManager session;

    ProgressDialog progressDialog;

    RelativeLayout levelsRelativeLayout, amenitiesRelativeLayout;

    Dialog typeDialog;

    NewPropertyPresenter newPropertyPresenter;

    AppCompatEditText addressText, amenitiesText, priceText, roomsText, bathsText, areaText,
            descriptionText, typesIndicatorText, districtText;

    AppCompatTextView priceAvg;

    LinearLayout propertyGroup;

    Spinner typeSpinner, categorySpinner, citySpinner, levelsSpinner;

    ArrayAdapter<String> typeAdapter, categoryAdapter, cityAdapter, levelsAdapter;

    List<String> types, categories, cities, levels;

    ImageView addImages;

    Button confirmBtn;

    List<Bitmap> imagesBitmap;
    List<String> imagesString, imagesName;

    RecyclerView recyclerView;

    String[] amenityListItems;

    boolean[] amenityCheckedItems;

    ArrayList<Integer> amenitiesUserItems = new ArrayList<>();

    Map<String, String> selectedAmenities, imagesNames, images;


    private int userId, price, rooms, baths, area;
    private String imageUrl, address, district, city, category, type, description, ownerPhone, levelsValue;
    private float latitude, longitude;

    String schoolUrl, hospitalUrl, restaurantsUrl, cinemaUrl, atmUrl, cafeUrl, pharmacyUrl, parkUrl, shoppingMallUrl, supermarketUrl;


    int saleAvgPrice;
    int rentAvgPrice;
    int essentialsResults;
    int lifestyleResults;
    int PROXIMITY_RADIUS = 500;

    double essentialsFactor, lifestyleFactor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_property);

        essentialsResults = 0;

        lifestyleResults = 0;


        // Session class instance
        session = new SessionManager(this);

        setDialog();

        newPropertyPresenter = new NewPropertyPresenter(this, this, progressDialog);

        initiateViews();

        initiateArrays();

        showTypeDialog();

        categoryAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(this);

        cityAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setOnItemSelectedListener(this);


        levelsAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, levels);
        levelsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        levelsSpinner.setAdapter(levelsAdapter);
        levelsSpinner.setOnItemSelectedListener(this);
    }

    private void showTypeDialog() {

        final String[] typesArray = getResources().getStringArray(R.array.types);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please select the property type");
        builder.setItems(typesArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                type = typesArray[item];
                typesIndicatorText.setText(type);
                calculateAvgPrice();
                switch (type){
                    case "Apartment":
                    case "Villa":
                    case "Chalet":

                        roomsText.setVisibility(View.VISIBLE);
                        bathsText.setVisibility(View.VISIBLE);
                        levelsRelativeLayout.setVisibility(View.GONE);
                        amenitiesRelativeLayout.setVisibility(View.VISIBLE);
                        break;
                    case "Land":
                    case "Garage":

                        roomsText.setVisibility(View.GONE);
                        bathsText.setVisibility(View.GONE);
                        amenitiesRelativeLayout.setVisibility(View.GONE);
                        levelsRelativeLayout.setVisibility(View.GONE);
                        break;
                    case "Building":

                        roomsText.setVisibility(View.GONE);
                        bathsText.setVisibility(View.GONE);
                        levelsRelativeLayout.setVisibility(View.VISIBLE);
                        amenitiesRelativeLayout.setVisibility(View.GONE);
                        break;
                    default:

                        break;
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

        /*
        typeDialog = new Dialog(this);

        typeDialog.setCanceledOnTouchOutside(false);

        Button updateBtn;

        typeDialog.setContentView(R.layout.type_popup);

        updateBtn = typeDialog.findViewById(R.id.update_phone_btn);
        typeSpinner = typeDialog.findViewById(R.id.property_type);


        String[] typesArray = getResources().getStringArray(R.array.types);

        types = Arrays.asList(typesArray);


        typeAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        typeSpinner.setOnItemSelectedListener(this);


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typesIndicatorText.setText(type);
                switch (type){
                    case "Apartment":

                        levelsRelativeLayout.setVisibility(View.GONE);
                        break;
                    default:

                        break;
                }

                typeDialog.dismiss();
            }
        });




        typeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        typeDialog.show();

         */
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == categorySpinner.getId()){
            if (i != 0){

                category = categories.get(i);


                calculateAvgPrice();

            }else{
                category = null;
            }

        }else if (adapterView.getId() == citySpinner.getId()){
            if (i != 0){

                city = cities.get(i);

            }else{
                city = null;
            }

        }else if (adapterView.getId() == levelsSpinner.getId()){
            if (i != 0){

                levelsValue = levels.get(i);

            }else{
                levelsValue = "0";
            }

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void setDialog() {

        if(progressDialog != null){
            progressDialog.dismiss();
        }
        progressDialog = new ProgressDialog(NewPropertyActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Adding your Property...");
    }

    public void getImages(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), 3);
    }

    public void getImages2(){
        ImagePicker.create(this)
                .returnMode(ReturnMode.NONE) // set whether pick and / or camera action should return immediate result or not.
                .folderMode(true) // folder mode (false by default)
                .toolbarFolderTitle("Folder") // folder selection title
                .toolbarImageTitle("Tap to select") // image selection title
                .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
                .multi() // multi mode (default mode)
                .limit(8) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .enableLog(false) // disabling log
                .start();
    }

    @Override
    public void onClick(View view) {
        if (view == addressText){
            Intent intent = new Intent(NewPropertyActivity.this, MapsActivity.class);
            startActivityForResult(intent,1);
        }else if (view == addImages){
            getImages2();
        }else if (view == amenitiesText){
            multiChoice();
        }else if (view == confirmBtn){
            AddProperty();
        }else if (view == typesIndicatorText){
            showTypeDialog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                district = data.getStringExtra("district");
                address = data.getStringExtra("address");
                String lat = data.getStringExtra("latitude");
                latitude = Float.parseFloat(lat);
                String lon = data.getStringExtra("longitude");
                longitude = Float.parseFloat(lon);
                addressText.setText(data.getStringExtra("address"));
                districtText.setText(data.getStringExtra("district"));


                calculateAvgPrice();


            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }else if (requestCode == 3){

        }else if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            List<Image> imageList = ImagePicker.getImages(data);

            if (!imageList.isEmpty()|| imageList != null){


                if (imagesBitmap.size() == 8){
                    imagesBitmap.clear();
                    imagesName.clear();
                    images.clear();
                    imagesNames.clear();
                }
                for (int i = 0; i < imageList.size(); i++){
                    imagesBitmap.add(getBitmapFromPath(imageList.get(i).getPath()));
                    imagesName.add(imageList.get(i).getName());
                    getStringImage(getBitmapFromPath(imageList.get(i).getPath()));


                    images.put("images[" + i + "]", getStringImage(getBitmapFromPath(imageList.get(i).getPath())));

                    imagesNames.put("imagesName[" + i + "]", imageList.get(i).getName());
                }

                if (imagesBitmap.size() == 8){
                    addImages.setVisibility(View.GONE);
                }
                ImagesAdapter myAdapter = new ImagesAdapter(this, imagesBitmap);
                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(myAdapter);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void calculateAvgPrice() {
        setUrls();

        String[] essentialsUrl = {schoolUrl, hospitalUrl, pharmacyUrl, atmUrl, supermarketUrl};

        String[] lifestyleUrl = {restaurantsUrl, cafeUrl, cinemaUrl, parkUrl, shoppingMallUrl};

        newPropertyPresenter.requestPlaces(essentialsUrl, lifestyleUrl, districtText.getText().toString(), type, category);
    }


    @Override
    public String getUrl( String nearbyPlace)
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

    @Override
    public void setSaleAvgPrice(int price) {

        saleAvgPrice = (int) (price * (essentialsFactor + lifestyleFactor));

        Toast.makeText(this, String.valueOf(saleAvgPrice), Toast.LENGTH_SHORT).show();

        String priceAvgString = "The average sale Price for this location is " + saleAvgPrice + " EGP per meter";

        priceAvg.setText(priceAvgString);



    }

    @Override
    public void setRentAvgPrice(int price) {

        rentAvgPrice = (int) (price  * (essentialsFactor + lifestyleFactor));

        Toast.makeText(this, String.valueOf(rentAvgPrice), Toast.LENGTH_SHORT).show();

        String priceAvgString = "The average rent Price for this location is " + rentAvgPrice + " EGP per meter";

        priceAvg.setText(priceAvgString);

    }


    @Override
    public void setUrls() {

        schoolUrl = getUrl("school");
        hospitalUrl = getUrl("hospital");
        pharmacyUrl = getUrl("pharmacy");
        atmUrl = getUrl("atm");
        supermarketUrl = getUrl("supermarket");

        restaurantsUrl = getUrl("restaurant");
        cinemaUrl = getUrl("movie_theater");
        cafeUrl = getUrl("cafe");
        parkUrl = getUrl("park");
        shoppingMallUrl = getUrl("shopping_mall");

    }

    private Bitmap getBitmapFromPath(String filePath) {

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath,bmOptions);
        return bitmap;

    }

    public String getStringImage(Bitmap bm){
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,ba);
        byte[] imageByte = ba.toByteArray();
        String encode = Base64.encodeToString(imageByte,Base64.DEFAULT);
        return encode;
    }

    @Override
    public void initiateViews() {

        propertyGroup = findViewById(R.id.property_group);
        areaText = findViewById(R.id.area);
        areaText.setHint(Html.fromHtml("Area (m<sup>2</sup>)"));
        typesIndicatorText = findViewById(R.id.typeText);
        typesIndicatorText.setOnClickListener(this);
        levelsRelativeLayout = findViewById(R.id.levels_layout);
        amenitiesRelativeLayout = findViewById(R.id.amenities_layout);
        levelsSpinner = findViewById(R.id.levels);
        categorySpinner = findViewById(R.id.category);
        citySpinner = findViewById(R.id.city);
        addressText = findViewById(R.id.address);
        addressText.setOnClickListener(this);
        amenitiesText = findViewById(R.id.amenities);
        amenitiesText.setOnClickListener(this);
        confirmBtn = findViewById(R.id.confirm);
        confirmBtn.setOnClickListener(this);
        priceText = findViewById(R.id.price);
        roomsText = findViewById(R.id.rooms);
        bathsText = findViewById(R.id.baths);
        areaText = findViewById(R.id.area);
        descriptionText = findViewById(R.id.desc);
        districtText= findViewById(R.id.district);
        priceAvg = findViewById(R.id.price_avg);

        recyclerView = findViewById(R.id.images_recycler);

        addImages = findViewById(R.id.image);
        addImages.setOnClickListener(this);
    }

    @Override
    public void showViewError(String view, String error) {

        int id = getResources().getIdentifier(view, "id", this.getPackageName());
        EditText editText = (EditText)findViewById(id);
        editText.setError(error);
    }

    @Override
    public void AddProperty() {

        if (!validate()){
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        String creationTime = sdf.format(new Date());
        ownerPhone = session.getPhone();
        userId = session.getUserId();
        price = Integer.parseInt(priceText.getText().toString());
        area = Integer.parseInt(areaText.getText().toString());
        if (!roomsText.getText().toString().isEmpty()){

            rooms = Integer.parseInt(roomsText.getText().toString());
        }else {
            rooms = 0;
        }

        if (!bathsText.getText().toString().isEmpty()){

            baths = Integer.parseInt(bathsText.getText().toString());
        }else {
            baths = 0;
        }
        description = descriptionText.getText().toString();
        district = districtText.getText().toString();
        address = addressText.getText().toString();

        Property property = new Property(images, imagesNames, userId, price, rooms, baths, levelsValue, area, address, district, city,
                selectedAmenities, category, type, description, ownerPhone, creationTime, latitude, longitude);

        progressDialog.show();

        newPropertyPresenter.addProperty(property);
    }

    @Override
    public void initiateArrays() {

        imagesBitmap = new ArrayList<>();
        imagesName = new ArrayList<>();
        imagesString = new ArrayList<>();


        selectedAmenities = new HashMap<String, String>();
        imagesNames = new HashMap<String, String>();
        images = new HashMap<String, String>();

        amenityListItems = getResources().getStringArray(R.array.amenities);

        amenityCheckedItems = new boolean[amenityListItems.length];


        String[] typesArray = getResources().getStringArray(R.array.types);

        types = Arrays.asList(typesArray);


        String[] categoriesArray = {"Select category", "For Rent", "For Sale"};

        categories = Arrays.asList(categoriesArray);

        String[] citiesArray = getResources().getStringArray(R.array.cities);

        cities = Arrays.asList(citiesArray);

        String[] levelsArray = getResources().getStringArray(R.array.levels);

        levels = Arrays.asList(levelsArray);
    }

    @Override
    public void showMessage(String message) {

        Toast.makeText(NewPropertyActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToMain() {

        Intent intent = new Intent(NewPropertyActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void setAmenities(String amenities){

        String[] arrayString = amenities.split(",");

        List<String> amenitiesList = Arrays.asList(amenityListItems);
        for (int i = 0; i < arrayString.length; i++) {
            int currentItem = amenitiesList.indexOf(arrayString[i]);
            amenitiesUserItems.add(currentItem);

        }
        for (int j = 0; j < amenitiesUserItems.size(); j++) {
            amenityCheckedItems[amenitiesUserItems.get(j)] = true;
        }
        amenitiesText.setText(amenities);
    }

    @Override
    public boolean validate() {
        boolean valid = true;


        if (category == null) {

            showMessage("Select the Category");
            valid = false;
        }

        if (type == null) {

            showMessage("Select the Type");
            valid = false;
        }

        if (priceText.getText().toString().isEmpty()){
            showViewError("price","Enter Property Price");
            valid = false;

        }else {
            showViewError("price",null);
        }

        if (areaText.getText().toString().isEmpty()) {
            showViewError("area","Enter Property Area");
            valid = false;

        }else {
            showViewError("area",null);
        }


        if (descriptionText.getText().toString().isEmpty()) {
            showViewError("desc","Enter Property Description");
            valid = false;

        }else {
            showViewError("desc",null);
        }

        if (addressText.getText().toString().isEmpty()) {
            showViewError("address","Enter Property Address");
            valid = false;

        }else {
            showViewError("address",null);
        }

        if (city == null) {

            showMessage("Select the City");
            valid = false;
        }

        return valid;
    }

    @Override
    public void multiChoice(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(NewPropertyActivity.this);
        mBuilder.setTitle("Amenities");
        mBuilder.setMultiChoiceItems(amenityListItems, amenityCheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

                if(isChecked){
                    amenitiesUserItems.add(position);
                }else{
                    amenitiesUserItems.remove((Integer.valueOf(position)));
                }
            }
        });

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";
                for (int i = 0; i < amenitiesUserItems.size(); i++) {
                    item = item + amenityListItems[amenitiesUserItems.get(i)];

                    selectedAmenities.put("amenities[" + i + "]", amenityListItems[amenitiesUserItems.get(i)]);
                    if (i != amenitiesUserItems.size() - 1) {
                        item = item + ", ";
                    }
                }
                amenitiesText.setText(item);

            }
        });

        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i = 0; i < amenityCheckedItems.length; i++) {
                    amenityCheckedItems[i] = false;
                    amenitiesUserItems.clear();
                    amenitiesText.setHint("Amenities");
                    amenitiesText.setText(null);
                }
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }
}
