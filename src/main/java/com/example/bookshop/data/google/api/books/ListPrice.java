package com.example.bookshop.data.google.api.books;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ListPrice {
    @JsonProperty("amount")
    public double getAmount() {
        return this.amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    long amount;

    @JsonProperty("currencyCode")
    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    String currencyCode;

    @JsonProperty("amountInMicros")
    public long getAmountInMicros() {
        return this.amountInMicros;
    }

    public void setAmountInMicros(long amountInMicros) {
        this.amountInMicros = amountInMicros;
    }

    long amountInMicros;
}
