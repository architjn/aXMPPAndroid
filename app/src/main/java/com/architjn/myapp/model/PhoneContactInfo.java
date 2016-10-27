package com.architjn.myapp.model;

/**
 * Created by architjn on 10/27/2016.
 */
public class PhoneContactInfo {
    private int phoneContactID;
    private String contactName;
    private String contactNumber;

    public void setPhoneContactID(int phoneContactID) {
        this.phoneContactID = phoneContactID;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public int getPhoneContactID() {
        return phoneContactID;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }
}
