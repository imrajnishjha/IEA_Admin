package com.example.ieaapp;

public class memberApprovalDetailModel {
    String companyName,department,email,fullname,phoneNo,purl;

    memberApprovalDetailModel(){

    }

    public memberApprovalDetailModel(String companyName, String department, String email, String fullname, String phoneNo, String purl) {
        this.companyName = companyName;
        this.department = department;
        this.email = email;
        this.fullname = fullname;
        this.phoneNo = phoneNo;
        this.purl = purl;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }
}
