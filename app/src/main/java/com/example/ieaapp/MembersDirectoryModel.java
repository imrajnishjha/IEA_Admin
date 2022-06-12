package com.example.ieaapp;

public class MembersDirectoryModel {

    String companyName, fullname, purl, department;

    MembersDirectoryModel(){

    }

    public MembersDirectoryModel(String companyName, String fullname, String purl, String department) {
        this.companyName = companyName;
        this.fullname = fullname;
        this.purl = purl;
        this.department = department;
    }



    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
