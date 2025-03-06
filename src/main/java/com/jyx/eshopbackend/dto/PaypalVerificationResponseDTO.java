package com.jyx.eshopbackend.dto;

public class PaypalVerificationResponseDTO {
    private String id;
    private String intent;
    private String status;
    private PaymentSource paymentSource;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PaymentSource getPaymentSource() {
        return paymentSource;
    }

    public void setPaymentSource(PaymentSource paymentSource) {
        this.paymentSource = paymentSource;
    }

    public static class PaymentSource {
        private Paypal paypal;

        public Paypal getPaypal() {
            return paypal;
        }

        public void setPaypal(Paypal paypal) {
            this.paypal = paypal;
        }
    }

    public static class Paypal {
        private String emailAddress;
        private String accountId;
        private String accountStatus;
        private Name name;
        private Address address;

        public String getEmailAddress() {
            return emailAddress;
        }

        public void setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
        }

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getAccountStatus() {
            return accountStatus;
        }

        public void setAccountStatus(String accountStatus) {
            this.accountStatus = accountStatus;
        }

        public Name getName() {
            return name;
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }
    }

    public static class Name {
        private String givenName;
        private String surname;

        public String getGivenName() {
            return givenName;
        }

        public void setGivenName(String givenName) {
            this.givenName = givenName;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }
    }

    public static class Address {
        private String addressLine1;
        private String addressLine2;
        private String adminArea2;
        private String adminArea1;
        private String postalCode;
        private String countryCode;

        public String getAddressLine1() {
            return addressLine1;
        }

        public void setAddressLine1(String addressLine1) {
            this.addressLine1 = addressLine1;
        }

        public String getAddressLine2() {
            return addressLine2;
        }

        public void setAddressLine2(String addressLine2) {
            this.addressLine2 = addressLine2;
        }

        public String getAdminArea2() {
            return adminArea2;
        }

        public void setAdminArea2(String adminArea2) {
            this.adminArea2 = adminArea2;
        }

        public String getAdminArea1() {
            return adminArea1;
        }

        public void setAdminArea1(String adminArea1) {
            this.adminArea1 = adminArea1;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }
    }
}
