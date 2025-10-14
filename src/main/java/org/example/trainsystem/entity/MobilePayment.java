package org.example.trainsystem.entity;

public class MobilePayment implements PaymentStrategy {
    private String mobileNumber;

    public MobilePayment(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Override
    public String pay(double amount) {

        return "Paid LKR" + amount + " using Mobile Payment from number: " + mobileNumber;
    }

}
