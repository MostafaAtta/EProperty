package com.atta.eproperty.model;

public class Property {


    private int propertyId, price, rooms, baths, area;
    private String imageUrl, address, district, city, governance, type, description, ownerPhone;
    private float latitude, longitude;

    public Property(int propertyId, int price, int rooms, int baths, int area, String imageUrl, String address, String district, String city, String governance, String type, String description, String ownerPhone, long latitude, long longitude) {
        this.propertyId = propertyId;
        this.price = price;
        this.rooms = rooms;
        this.baths = baths;
        this.area = area;
        this.imageUrl = imageUrl;
        this.address = address;
        this.district = district;
        this.city = city;
        this.governance = governance;
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

    public String getGovernance() {
        return governance;
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

    public int getDishId() {
        return propertyId;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
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
}
