package com.example.tamsdineout;

public class UserDataClass {
    private String Name,PhoneNo,EmailId;


    UserDataClass(String userName, String userPhoneNo, String userEmail) {
        this.Name = userName;
        this.PhoneNo = userPhoneNo;
        this.EmailId = userEmail;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }
}
