package com.pfm.pfm_system.controllers;

import Data.Investment.Company;
import Data.Investment.ComparePurchase;
import Data.Investment.Purchase;
import Data.Investment.Sell;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RestController
public class InvestmentController {

    private static String api = null;

    private String balance = "0 $";

    private static InvestmentController instance = null;

    private InvestmentController() {
        api = "https://investment-microservice.happywave-288968fb.eastasia.azurecontainerapps.io";
    }

    public static InvestmentController getInstance() {
        if (instance == null)
            instance = new InvestmentController();
        return instance;
    }

    public void setUser(String userId) {
        String restPoint = "/setUser/{userId}";
        ResponseEntity<String> response = GetStringForString(restPoint, userId);
        balance = response.getBody();
    }

    public void addBalance(String userId, Float money) {
        String restPoint = "/addBalance/{id}/{money}";
        ResponseEntity<String> response = GetStringForStringFloat(restPoint, userId, money);
        balance = response.getBody();
    }

    public String getBalance() {
        return balance;
    }

    @GetMapping("/getCompanies")
    public List<Company> getCompanies() {
        String restPoint = "/companies";
        ResponseEntity<String> companies = GetString(restPoint);
        Type CompaniesList = new TypeToken<ArrayList<Company>>() {
        }.getType();
        return new Gson().fromJson(companies.getBody(), CompaniesList);
    }

    public List<ComparePurchase> getPurchases(String id) {
        String restPoint = "/purchases/{id}";
        ResponseEntity<String> purchases = GetStringForString(restPoint, id);
        Type PurchasesList = new TypeToken<ArrayList<ComparePurchase>>() {
        }.getType();
        return new Gson().fromJson(purchases.getBody(), PurchasesList);
    }

    public ResponseEntity<String> buyShares(String id, String symbol, int amount) {
        String restPoint = "/buyStock/{purchase}";
        Purchase purchase = new Purchase(id, symbol, amount);
        ResponseEntity<String> response = GetStringForString(restPoint, new Gson().toJson(purchase));
        balance = response.getBody();
        return response;
    }

    public void sellShares(String Id, String Symbol, int Quantity) {
        String restPoint = "/sellStock/{sell}";
        Sell sell = new Sell(Id, Symbol, Quantity);
        ResponseEntity<String> response = GetStringForString(restPoint, new Gson().toJson(sell));
        balance = response.getBody();
    }

    public void deleteUser(String userId) {
        String restPoint = "/deleteUser/{userId}";
        ResponseEntity<String> response = GetStringForString(restPoint, userId);
    }

    // ----------------- SUPPORT METHODS ----------------- //

    private ResponseEntity<String> GetString(String restPoint) {
        return new RestTemplate().getForEntity(api + restPoint, String.class);
    }

    private ResponseEntity<String> GetStringForString(String restPoint, String obj){
        return new RestTemplate().getForEntity(api + restPoint, String.class, obj);
    }

    private ResponseEntity<String> GetStringForStringFloat(String restPoint, String id, Float money) {
        return new RestTemplate().getForEntity(api + restPoint, String.class, id, money);
    }
}