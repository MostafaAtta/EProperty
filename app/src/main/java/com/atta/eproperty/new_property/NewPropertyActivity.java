package com.atta.eproperty.new_property;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    NewPropertyPresenter newPropertyPresenter;

    AppCompatEditText addressText, amenitiesText, priceText, roomsText, bathsText, areaText,
            descriptionText;

    LinearLayout propertyGroup;

    Spinner typeSpinner, categorySpinner, citySpinner;

    ArrayAdapter<String> typeAdapter, categoryAdapter, cityAdapter;

    List<String> types, categories, cities;

    ImageView addImages;

    Button confirmBtn;

    List<Bitmap> imagesBitmap;
    List<String> imagesString;
    List<String> imagesName;

    RecyclerView recyclerView;

    String[] movieListItems;

    boolean[] movieCheckedItems;

    ArrayList<Integer> moviesUserItems = new ArrayList<>();

    Map<String, String> selectedAmenities, imagesNames, images;


    private int userId, price, rooms, baths, area;
    private String imageUrl, address, district, city, category, type, description, ownerPhone;
    private float latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_property);


        // Session class instance
        session = new SessionManager(this);

        setDialog();

        newPropertyPresenter = new NewPropertyPresenter(this, this, progressDialog);

        initiateViews();

        initiateArrays();

        typeAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, types);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        typeSpinner.setOnItemSelectedListener(this);


        categoryAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(this);

        cityAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == typeSpinner.getId()){

            if (i != 0){
                type = types.get(i);
            }else {
                type = null;
            }
        }else if (adapterView.getId() == categorySpinner.getId()){
            if (i != 0){

                category = categories.get(i);

            }else{
                category = null;
            }

        }else if (adapterView.getId() == citySpinner.getId()){
            if (i != 0){

                city = cities.get(i);

            }else{
                city = null;
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
                .includeVideo(false) // Show video on image picker // single mode
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

    private Bitmap getBitmapFromPath(String filePath) {

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath,bmOptions);
        return bitmap;

    }

    public String getStringImage(Bitmap bm){
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,ba);
        byte[] imagebyte = ba.toByteArray();
        String encode = Base64.encodeToString(imagebyte,Base64.DEFAULT);
        return encode;
    }

    @Override
    public void initiateViews() {

        propertyGroup = findViewById(R.id.property_group);
        areaText = findViewById(R.id.area);
        areaText.setHint(Html.fromHtml("Area (m<sup>2</sup>)"));
        typeSpinner = findViewById(R.id.type);
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
        rooms = Integer.parseInt(roomsText.getText().toString());
        baths = Integer.parseInt(bathsText.getText().toString());
        description = descriptionText.getText().toString();

        Property property = new Property(images, imagesNames, userId, price, rooms, baths, area, address, district, city,
                selectedAmenities, type, description, ownerPhone, creationTime, latitude, longitude);

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

        movieListItems = getResources().getStringArray(R.array.amenities);

        movieCheckedItems = new boolean[movieListItems.length];


        String[] typesArray = getResources().getStringArray(R.array.types);

        types = Arrays.asList(typesArray);


        String[] categoriesArray = {"Select category", "For Rent", "For Sale"};

        categories = Arrays.asList(categoriesArray);

        String[] citiesArray = getResources().getStringArray(R.array.cities);

        cities = Arrays.asList(citiesArray);
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
    public void setMovies(String movies){

        String[] arrayString = movies.split(",");

        List<String> movieList = Arrays.asList(movieListItems);
        for (int i = 0; i < arrayString.length; i++) {
            int currentItem = movieList.indexOf(arrayString[i]);
            moviesUserItems.add(currentItem);

        }
        for (int j = 0; j < moviesUserItems.size(); j++) {
            movieCheckedItems[moviesUserItems.get(j)] = true;
        }
        amenitiesText.setText(movies);
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

        if (roomsText.getText().toString().isEmpty()) {
            showViewError("rooms","Enter Rooms number");
            valid = false;

        }else {
            showViewError("rooms",null);
        }

        if (bathsText.getText().toString().isEmpty()) {
            showViewError("baths","Enter Baths number");
            valid = false;

        }else {
            showViewError("baths",null);
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

        if (amenitiesText.getText().toString().isEmpty()) {
            showViewError("amenities","Enter Property Amenities");
            valid = false;

        }else {
            showViewError("amenities",null);
        }

        return valid;
    }

    @Override
    public void multiChoice(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(NewPropertyActivity.this);
        mBuilder.setTitle("Amenities");
        mBuilder.setMultiChoiceItems(movieListItems, movieCheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

                if(isChecked){
                    moviesUserItems.add(position);
                }else{
                    moviesUserItems.remove((Integer.valueOf(position)));
                }
            }
        });

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";
                for (int i = 0; i < moviesUserItems.size(); i++) {
                    item = item + movieListItems[moviesUserItems.get(i)];

                    selectedAmenities.put("amenities[" + i + "]", movieListItems[moviesUserItems.get(i)]);
                    if (i != moviesUserItems.size() - 1) {
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
                for (int i = 0; i < movieCheckedItems.length; i++) {
                    movieCheckedItems[i] = false;
                    moviesUserItems.clear();
                    amenitiesText.setHint("Amenities");
                    amenitiesText.setText(null);
                }
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }
}
