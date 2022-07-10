package com.example.ieaadmin;

public class MemberofMonthModel {
    String name,companyname,purl;

    public MemberofMonthModel() {
    }

    public MemberofMonthModel(String name, String companyname,String purl) {
        this.name = name;
        this.companyname = companyname;
        this.purl = purl;
    }

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }
}
