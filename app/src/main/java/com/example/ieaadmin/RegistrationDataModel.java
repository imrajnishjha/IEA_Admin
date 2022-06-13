package com.example.ieaadmin;

public class RegistrationDataModel {
    String membershipType,turnover,payProofUrl,email,amountLeft,department;

    RegistrationDataModel(){

    }

    public RegistrationDataModel(String membershipType, String turnover, String payProofUrl, String email, String amountLeft, String department) {
        this.membershipType = membershipType;
        this.turnover = turnover;
        this.payProofUrl = payProofUrl;
        this.email = email;
        this.amountLeft = amountLeft;
        this.department = department;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public String getTurnover() {
        return turnover;
    }

    public void setTurnover(String turnover) {
        this.turnover = turnover;
    }

    public String getPayProofUrl() {
        return payProofUrl;
    }

    public void setPayProofUrl(String payProofUrl) {
        this.payProofUrl = payProofUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAmountLeft() {
        return amountLeft;
    }

    public void setAmountLeft(String amountLeft) {
        this.amountLeft = amountLeft;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}