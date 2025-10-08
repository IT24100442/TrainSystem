package org.example.trainsystem.entity;

public class CreditCardPayment  implements  PaymentStrategy {
    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;

    public CreditCardPayment(String cardNumber, String cardHolderName, String expiryDate, String cvv) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    @Override
    public String pay(double amount) {

        return "Paid LKR" + amount + " using Credit Card ending with " + cardNumber.substring(cardNumber.length() - 4);
    }
}
