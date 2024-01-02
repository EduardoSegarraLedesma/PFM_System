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
        ResponseEntity<String> response = PostStringForBalance(restPoint, userId);
        balance = response.getBody();
    }

    public void addBalance(String userId, Float money) {
        String restPoint = "/addBalance/{id}/{money}";
        ResponseEntity<String> response = GetBalanceForEntity(restPoint, userId, money);
        balance = response.getBody();
    }

    public String getBalance() {
        return balance;
    }

    @GetMapping("/getCompanies")
    public List<Company> getCompanies() {
        String restPoint = "/companies";
        ResponseEntity<String> companies = GetStringForEntity(restPoint);
        Type CompaniesList = new TypeToken<ArrayList<Company>>() {
        }.getType();
        return new Gson().fromJson(companies.getBody(), CompaniesList);
    }

    public List<ComparePurchase> getPurchases(String id) {
        String restPoint = "/purchases/{id}";
        ResponseEntity<String> purchases = GetStringForId(restPoint, id);
        Type PurchasesList = new TypeToken<ArrayList<ComparePurchase>>() {
        }.getType();
        return new Gson().fromJson(purchases.getBody(), PurchasesList);
    }

    public ResponseEntity<String> buyShares(String id, String symbol, int amount) {
        String restPoint = "/buyStock/{purchase}";
        Purchase purchase = new Purchase(id, symbol, amount);
        ResponseEntity<String> response = PostPurchaseForBalance(restPoint, purchase);
        balance = response.getBody();
        return response;
    }

    public void sellShares(String Id, String Symbol, int Quantity) {
        String restPoint = "/sellStock/{sell}";
        Sell sell = new Sell(Id, Symbol, Quantity);
        ResponseEntity<String> response = PostSellForBalance(restPoint, sell);
        balance = response.getBody();
    }

    public void deleteUser(String userId) {
        String restPoint = "/deleteUser/{userId}";
        ResponseEntity<String> response = PostIdForAnswer(restPoint, userId);
    }

    // ----------------- SUPPORT METHODS ----------------- //

    private ResponseEntity<String> PostIdForAnswer(String restPoint, String id) {
        return new RestTemplate().getForEntity(api + restPoint, String.class, id);
    }

    private ResponseEntity<String> PostStringForBalance(String restPoint, String obj) {
        return new RestTemplate().getForEntity(api + restPoint, String.class, obj);
    }

    private ResponseEntity<String> PostPurchaseForBalance(String restPoint, Purchase obj) {
        return new RestTemplate().getForEntity(api + restPoint, String.class, obj);
    }

    private ResponseEntity<String> PostSellForBalance(String restPoint, Sell obj) {
        return new RestTemplate().getForEntity(api + restPoint, String.class, obj);
    }

    private ResponseEntity<String> GetBalanceForEntity(String restPoint, String id, Float money) {
        return new RestTemplate().getForEntity(api + restPoint, String.class, id, money);
    }

    private ResponseEntity<String> GetStringForEntity(String restPoint) {
        return new RestTemplate().getForEntity(api + restPoint, String.class);
    }

    private ResponseEntity<String> GetStringForId(String restPoint, String id) {
        return new RestTemplate().getForEntity(api + restPoint, String.class, id);
    }

}