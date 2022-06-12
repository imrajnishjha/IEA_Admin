package com.example.ieaapp;

public class RegistrationDataModel {
    String membershipType,turnover,payProofUrl,email;

    RegistrationDataModel(){

    }

    public RegistrationDataModel(String membershipType, String turnover, String payProofUrl,String email) {
        this.membershipType = membershipType;
        this.turnover = turnover;
        this.payProofUrl = payProofUrl;
        this.email=email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
