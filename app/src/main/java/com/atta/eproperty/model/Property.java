package com.atta.eproperty.model;

import java.io.Serializable;
import java.util.Map;

public class Property implements Serializable {


    private Map<String, String> images, imagesNames, amenities;
    private String[] imagesUrls, amenitiesArray;
    private int userId, id, price, rooms, baths, area;
    private String category, address, district, city, type, description, ownerPhone, creationTime, levels;
    private float latitude, longitude;

    public Property(String[] imagesUrls, int userId, int id,
                    int price, int rooms, int baths, String levels, int area, String address, String district, String city,
                    String[] amenitiesArray, String category, String type, String description, String ownerPhone, String creationTime,
                    float latitude, float longitude) {
        this.imagesUrls = imagesUrls;
        this.userId = userId;
        this.id = id;
        this.price = price;
        this.rooms = rooms;
        this.baths = baths;
        this.levels = levels;
        this.area = area;
        this.address = address;
        this.district = district;
        this.city = city;
        this.amenitiesArray = amenitiesArray;
        this.category = category;
        this.type = type;
        this.description = description;
        this.ownerPhone = ownerPhone;
        this.creationTime = creationTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Property(int id, String[] imagesUrls, int userId, int propertyId,
                    int price, int rooms, int baths, String levels, int area, String address, String district, String city,
                    String[] amenitiesArray, String category, String type, String description, String ownerPhone, String creationTime,
                    float latitude, float longitude) {
        this.imagesUrls = imagesUrls;
        this.userId = userId;
        this.id = propertyId;
        this.price = price;
        this.rooms = rooms;
        this.baths = baths;
        this.levels = levels;
        this.area = area;
        this.address = address;
        this.district = district;
        this.city = city;
        this.amenitiesArray = amenitiesArray;
        this.category = category;
        this.type = type;
        this.description = description;
        this.ownerPhone = ownerPhone;
        this.creationTime = creationTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Property(Map<String, String> images, Map<String, String> imagesNames, int userId,
                    int price, int rooms, int baths, String levels, int area, String address, String district, String city,
                    Map<String, String> amenities, String category, String type, String description, String ownerPhone, String creationTime,
                    float latitude, float longitude) {
        this.images = images;
        this.imagesNames = imagesNames;
        this.userId = userId;
        this.price = price;
        this.rooms = rooms;
        this.baths = baths;
        this.levels = levels;
        this.area = area;
        this.address = address;
        this.district = district;
        this.city = city;
        this.amenities = amenities;
        this.category = category;
        this.type = type;
        this.description = description;
        this.ownerPhone = ownerPhone;
        this.creationTime = creationTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public Property(int id, int price, int rooms, int baths, String levels, int area, String address, String district, String city, String[] amenitiesArray, String category, String type, String description, String ownerPhone, long latitude, long longitude) {
        this.id = id;
        this.price = price;
        this.rooms = rooms;
        this.baths = baths;
        this.levels = levels;
        this.area = area;
        this.address = address;
        this.district = district;
        this.city = city;
        this.amenitiesArray = amenitiesArray;
        this.category = category;
        this.type = type;
        this.description = description;
        this.ownerPhone = ownerPhone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
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

    public String getLevels() {
        return levels;
    }

    public String getCategory() {
        return category;
    }
}
