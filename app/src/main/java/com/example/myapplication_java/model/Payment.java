package com.example.myapplication_java.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Payment {
    // Constants for payment types
    public static final String TYPE_CASH = "Cash";
    public static final String TYPE_BANK_TRANSFER = "Bank Transfer";
    public static final String TYPE_CREDIT_CARD = "Credit Card";

    private double amount;
    private String type;
    private String provider;
    private String reference;

    public Payment(double amount, String type) {
        this.amount = amount;
        this.type = type;
    }

    public Payment(double amount, String type, String provider, String reference) {
        this.amount = amount;
        this.type = type;
        this.provider = provider;
        this.reference = reference;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }


    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("amount", amount);
        json.put("type", type);
        if (provider != null) json.put("provider", provider);
        if (reference != null) json.put("reference", reference);
        return json;
    }

    public static Payment fromJson(JSONObject json) throws JSONException {
        double amount = json.getDouble("amount");
        String type = json.getString("type");

        if (json.has("provider") && json.has("reference")) {
            return new Payment(
                    amount,
                    type,
                    json.getString("provider"),
                    json.getString("reference")
            );
        } else {
            return new Payment(amount, type);
        }
    }
}
