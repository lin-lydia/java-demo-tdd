package com.example;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class CalculatorTest {

    private CloseableHttpClient client;
    private CloseableHttpResponse response;
    private StatusLine statusLine;
    private HttpEntity entity;
    private InputStream stream;

    @BeforeEach
    public void setUp() {
        client = mock(CloseableHttpClient.class);
        response = mock(CloseableHttpResponse.class);
        statusLine = mock(StatusLine.class);
        entity = mock(HttpEntity.class);

        stream = new ByteArrayInputStream("{\"time\": {\"updated\": \"Oct 15, 2020 22:55:00 UTC\",\"updatedISO\": \"2020-10-15T22:55:00+00:00\",\"updateduk\": \"Oct 15, 2020 at 23:55 BST\"},\"chartName\": \"Bitcoin\",\"bpi\": {\"USD\": {\"code\": \"USD\",\"symbol\": \"&#36;\",\"rate\": \"11,486.5341\",\"description\": \"United States Dollar\",\"rate_float\": 11486.5341},\"GBP\": {\"code\": \"GBP\",\"symbol\": \"&pound;\",\"rate\": \"8,900.8693\",\"description\": \"British Pound Sterling\",\"rate_float\": 8900.8693},\"EUR\": {\"code\": \"EUR\",\"symbol\": \"&euro;\",\"rate\": \"9,809.3278\",\"description\": \"Euro\",\"rate_float\": 9809.3278}}}".getBytes());
    } 

    @Test
    void add_TwoNumbers_GivesCorrectResult() {
        // Arrange
        var num1 = 3.5;
        var num2 = 2.0;
        var expectedValue = 5.5;

        // Act
        var sum = Calculator.Add(num1, num2);

        //Assert
        assertEquals(expectedValue, sum);        
    }

    @Test
    void getExchangeRate_USD_ReturnsUSDExchangeRate() throws IOException{
        //arrange
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
       

        //act
        Calculator calculator = new Calculator(client);
        var actual = calculator.getExchangeRate(Calculator.Currency.USD);

        //assert
        double expected = 11486.5341;
        assertEquals(expected, actual);
    }

    @Test
    public void getExchangeRate_GBP_ReturnsGBPExchangeRate() throws IOException{
        //arrange
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity); 
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
       

    //act
    Calculator calculator = new Calculator(client);
    var actual = calculator.getExchangeRate(Calculator.Currency.GBP);

        //assert
        double expected = 8900.8693;
        assertEquals(expected, actual);
    }

    @Test
    public void getExchangeRate_EUR_ReturnsEURExchangeRate() throws IOException {
        //arrange
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
       

    //act
    Calculator calculator = new Calculator(client);
    var actual = calculator.getExchangeRate(Calculator.Currency.EUR);

        //assert
        double expected =  9809.3278;
        assertEquals(expected, actual);
    }  
    
    @Test
    public void getExchangeRate_ExecuteThrowsIOException_ReturnsCorrectErrorCode() throws IOException {
        //arrange
        when(client.execute(any(HttpGet.class))).thenThrow(IOException.class);
    
    //act
    Calculator calculator = new Calculator(client);
    var actual = calculator.getExchangeRate(Calculator.Currency.USD);
    
        //assert
        double expected = -1;
        assertEquals(expected, actual);
    }

    @Test
    public void getExchangeRate_ClosingResponseThrowsIOException_ReturnsCorrectErrorCode() throws IOException {
        //arrange
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);

        doThrow(IOException.class).when(response).close();

    //act
    Calculator calculator = new Calculator(client);
    var actual = calculator.getExchangeRate(Calculator.Currency.USD);
    
        //assert
        double expected = -1;
        assertEquals(expected, actual);
    }
    
    @Test
    public void convertBitcoins_1BitCoinToUSD_ReturnsUSDDollars() throws IOException {
        //arrange
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
       

    //act
    Calculator calculator = new Calculator(client);
    var actual = calculator.convertBitcoins(Calculator.Currency.USD, 1);
    
        //assert
        double expected = 11486.5341;
        assertEquals(expected, actual);
    }
    
    @Test
    public void convertBitcoins_2BitCoinToUSD_ReturnsUSDDollars() throws IOException {
       //arrange
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
       

    //act
    Calculator calculator = new Calculator(client);
    var actual = calculator.convertBitcoins(Calculator.Currency.USD, 2);
    
        //assert
        double expected = 22973.0682;
        assertEquals(expected, actual);
    }

    @Test
    public void convertBitcoins_1BitCoinToGBP_ReturnsGBPDollars() throws IOException {
        //arrange
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
       

    //act
    Calculator calculator = new Calculator(client);
    var actual = calculator.convertBitcoins(Calculator.Currency.GBP, 1);
    
        //assert
        double expected = 8900.8693;
        assertEquals(expected, actual);
    }
    
    @Test
    public void convertBitcoins_2BitCoinToGBP_ReturnsGBPDollars() throws IOException {
        //arrange
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
       

    //act
    Calculator calculator = new Calculator(client);
    var actual = calculator.convertBitcoins(Calculator.Currency.GBP, 2);
    
        //assert
        double expected = 17801.7386;
        assertEquals(expected, actual);
    }

    @Test
    public void convertBitcoins_1BitCoinToEUR_ReturnsEURDollars() throws IOException {
        //arrange
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
       

    //act
    Calculator calculator = new Calculator(client);
    var actual = calculator.convertBitcoins(Calculator.Currency.EUR, 1);
    
        //assert
        double expected = 9809.3278;
        assertEquals(expected, actual);
    }
    
    @Test
    public void convertBitcoins_2BitCoinToEUR_ReturnsEURDollars() throws IOException {
        //arrange
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(stream);
        when(client.execute(any(HttpGet.class))).thenReturn(response);
       

    //act
    Calculator calculator = new Calculator(client);
    var actual = calculator.convertBitcoins(Calculator.Currency.EUR, 2);
    
        //assert
        double expected = 19618.6556;
        assertEquals(expected, actual);
    }

    @Test
    public void testExceptionThrown() {
        //arrange
        //act
        Calculator calculator = new Calculator(client);
  
        //assert
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            calculator.convertBitcoins(Calculator.Currency.EUR, -1);
        });
        Assertions.assertEquals("Number of coins must not be less than zero", exception.getMessage());
    }

}