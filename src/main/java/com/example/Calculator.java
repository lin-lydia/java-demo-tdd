package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.text.ParseException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Calculator {

    public static double Add(double num1, double num2) {
        return num1 + num2;
    }
    
    private final String BITCOIN_CURRENTPRICE_URL = "https://api.coindesk.com/v1/bpi/currentprice.json";
    private final HttpGet httpget = new HttpGet(BITCOIN_CURRENTPRICE_URL);

    private CloseableHttpClient httpclient;

    public Calculator() {
        this.httpclient = HttpClients.createDefault();
    }

    public Calculator(CloseableHttpClient httpClient) {
        this.httpclient = httpClient;
    }
    public enum Currency {
        USD,
        GBP,
        EUR
    }

    public double getExchangeRate(Currency currency) {
        double rate = 0;

        try (CloseableHttpResponse response = this.httpclient.execute(httpget)) {
            switch (response.getStatusLine().getStatusCode()) {
                case 200:
                    InputStream inputStream = response.getEntity().getContent();
                    var json = new BufferedReader(new InputStreamReader(inputStream));

                    @SuppressWarnings("deprecation")
                    JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
                    String n = jsonObject.get("bpi").getAsJsonObject().get(currency.toString()).getAsJsonObject().get("rate").getAsString();
                    NumberFormat nf = NumberFormat.getInstance();
                    rate = nf.parse(n).doubleValue();

                    break;
                default:
                    rate = -1;
            }
        } catch (IOException | ParseException ex) {
            rate = -1;
        }

        return rate;
    }

    public double convertBitcoins(Currency currency, double coins) throws IllegalArgumentException {
        double dollars = 0;

        if(coins < 0) {
            throw new IllegalArgumentException("Number of coins must not be less than zero"); 
        }

        var exchangeRate = getExchangeRate(currency);

        if(exchangeRate >= 0) {
            dollars = exchangeRate * coins;
        } else {
            dollars = -1;
        }

        return dollars;
    }
    
}