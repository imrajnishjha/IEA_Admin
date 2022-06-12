package com.example.ieaapp;

public class memberApprovalModel {

    String imageUrl,fullname,department,CompanyName;

    memberApprovalModel(){

    }

    public memberApprovalModel(String imageUrl, String fullname, String department, String companyName) {
        this.imageUrl = imageUrl;
        this.fullname = fullname;
        this.department = department;
        CompanyName = companyName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }
}
