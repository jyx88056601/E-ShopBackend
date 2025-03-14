package com.jyx.eshopbackend.dto;

public class CreatingAddressRequestDTO {
    private String firstName;
    private String lastName;
    private String country;
    private String province;
    private String city;
    private String houseNumber;
    private String unitNumber;
    private String postalCode;
    private String streetName;
    private String buildingNumber;
    private String community;
    private String district;

    public CreatingAddressRequestDTO() {
    }

    public CreatingAddressRequestDTO(String firstName, String lastName, String country, String province, String city,
                                     String houseNumber, String unitNumber, String postalCode, String streetName,
                                     String buildingNumber, String community, String district) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.province = province;
        this.city = city;
        this.houseNumber = houseNumber;
        this.unitNumber = unitNumber;
        this.postalCode = postalCode;
        this.streetName = streetName;
        this.buildingNumber = buildingNumber;
        this.community = community;
        this.district = district;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public String toString() {
        return "CreatingAddressRequestDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", unitNumber='" + unitNumber + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", streetName='" + streetName + '\'' +
                ", buildingNumber='" + buildingNumber + '\'' +
                ", community='" + community + '\'' +
                ", district='" + district + '\'' +
                '}';
    }
}
