package com.atta.eproperty.model;

import java.io.Serializable;
import java.util.Map;

public class Property implements Serializable {


    private Map<String, String> images, imagesNames, amenities;
    private String[] imagesUrls, amenitiesArray;
    private int userId, propertyId, price, rooms, baths, area;
    private String address, district, city, type, description, ownerPhone, creationTime;
    private float latitude, longitude;

    public Property(String[] imagesUrls, int userId, int propertyId,
                    int price, int rooms, int baths, int area, String address, String district, String city,
                    String[] amenitiesArray, String type, String description, String ownerPhone, String creationTime,
                    float latitude, float longitude) {
        this.imagesUrls = imagesUrls;
        this.userId = userId;
        this.propertyId = propertyId;
        this.price = price;
        this.rooms = rooms;
        this.baths = baths;
        this.area = area;
        this.address = address;
        this.district = district;
        this.city = city;
        this.amenitiesArray = amenitiesArray;
        this.type = type;
        this.description = description;
        this.ownerPhone = ownerPhone;
        this.creationTime = creationTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Property(Map<String, String> images, Map<String, String> imagesNames, int userId,
                    int price, int rooms, int baths, int area, String address, String district, String city,
                    Map<String, String> amenities, String type, String description, String ownerPhone, String creationTime,
                    float latitude, float longitude) {
        this.images = images;
        this.imagesNames = imagesNames;
        this.userId = userId;
        this.price = price;
        this.rooms = rooms;
        this.baths = baths;
        this.area = area;
        this.address = address;
        this.district = district;
        this.city = city;
        this.amenities = amenities;
        this.type = type;
        this.description = description;
        this.ownerPhone = ownerPhone;
        this.creationTime = creationTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Property(int propertyId, int price, int rooms, int baths, int area, String address, String district, String city, String[] amenitiesArray, String type, String description, String ownerPhone, long latitude, long longitude) {
        this.propertyId = propertyId;
        this.price = price;
        this.rooms = rooms;
        this.baths = baths;
        this.area = area;
        this.address = address;
        this.district = district;
        this.city = city;
        this.amenitiesArray = amenitiesArray;
        this.type = type;
        this.description = description;
        this.ownerPhone = ownerPhone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public int getBaths() {
        return baths;
    }

    public int getArea() {
        return area;
    }

    public String getCity() {
        return city;
    }

    public Map<String, String> getAmenities() {
        return amenities;
    }

    public String[] getAmenitiesArray() {
        return amenitiesArray;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public int getPrice() {
        return price;
    }

    public String getAddress() {
        return address;
    }

    public int getRooms() {
        return rooms;
    }

    public String getDistrict() {
        return district;
    }

    public Map<String, String> getImages() {
        return images;
    }

    public Map<String, String> getImagesNames() {
        return imagesNames;
    }

    public int getUserId() {
        return userId;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public String[] getImagesUrls() {
        return imagesUrls;
    }
}
