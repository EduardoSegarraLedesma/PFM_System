package com.pfm.pfm_system.controllers;

import Data.Investment.Company;
import Data.Investment.ComparePurchase;
import Data.Investment.Purchase;
import Data.Investment.Sell;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class InvestmentController {

    private static String api = null;

    private static InvestmentController instance = null;

    private InvestmentController() {
        api = "https://investment-microservice.happywave-288968fb.eastasia.azurecontainerapps.io";
    }

    public static InvestmentController getInstance() {
        if (instance == null)
            instance = new InvestmentController();
        return instance;
    }

    public String setUser(String userId) {
        String restPoint = "/setUser/{userId}";
        ResponseEntity<String> response = PostObjectForBalance(restPoint, userId);
        return response.getBody();
    }

    public String addBalance(String userId, Float money) {
        String restPoint = "/addBalance/{id}/{money}";
        ResponseEntity<String> response = GetBalanceForEntity(restPoint, userId, money);
        return response.getBody();
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
        ResponseEntity<List<ComparePurchase>> purchases = GetPurchasesForEntity(restPoint, id);
        return purchases.getBody();
    }

    @GetMapping("/buy-Stocks")
    public String buyShares(@RequestBody Map<String, Object> map) {
        String restPoint = "/buyStock/{purchase}";
        Purchase purchase = new Purchase(
                PersistenceController.getInstance().getUser().getPersonalID(),
                (String) map.get("symbol"),
                (Integer) map.get("amount"));
        ResponseEntity<String> response = PostObjectForBalance(restPoint, purchase);
        return response.getBody();
    }

    public String sellShares(String Id, String Symbol, int Quantity) {
        String restPoint = "/sellStock/{sell}";
        Sell sell = new Sell(Id, Symbol, Quantity);
        ResponseEntity<String> response = PostObjectForBalance(restPoint, sell);
        return response.getBody();
    }

    public void deleteUser(String userId) {
        String restPoint = "/deleteUser/{userId}";
        ResponseEntity<String> response = PostIdForAnswer(restPoint, userId);
    }

    // ----------------- SUPPORT METHODS ----------------- //

    private ResponseEntity<String> PostIdForAnswer(String restPoint, String id) {
        return new RestTemplate().getForEntity(api + restPoint, String.class, id);
    }

    private ResponseEntity<String> PostObjectForBalance(String restPoint, Object obj) {
        return new RestTemplate().getForEntity(api + restPoint, String.class, obj);
    }

    private ResponseEntity<String> GetBalanceForEntity(String restPoint, String id, Float money) {
        return new RestTemplate().getForEntity(api + restPoint, String.class, id, money);
    }

    private ResponseEntity<String> GetStringForEntity(String restPoint) {
        return new RestTemplate().getForEntity(api + restPoint, String.class);
    }

    private ResponseEntity<List<ComparePurchase>> GetPurchasesForEntity(String restPoint, String id) {
        Class<List<ComparePurchase>> PurchaseList = (Class<List<ComparePurchase>>) (Class<?>) List.class;
        return new RestTemplate().getForEntity(api + restPoint, PurchaseList, id);
    }

}