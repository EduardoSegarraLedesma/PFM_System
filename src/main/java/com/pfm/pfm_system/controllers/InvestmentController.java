package com.pfm.pfm_system.controllers;

import Data.Investment.Company;
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
        String restPoint = "/setUser";
        ResponseEntity<String> response = PostIdForEntity(restPoint, userId);
    }

    public Float addBalance(String userId, Float money) {
        String restPoint = "/addBalance/{id}/{money}";
        ResponseEntity<Float> response = GetBalanceForEntity(restPoint, userId, money);
        return response.getBody();
    }

    @GetMapping("/getCompanies")
    public List<Company> getCompanies() {
        String restPoint = "/companies";
        ResponseEntity<String> companies = GetStringForEntity(restPoint);
        //For testing until Microservice Deployment
       /* String companies = "[{companyCode=TCEHY, marketCap=$357.50B, stockPrice=$37.73, lastDayVariation=3.45%, companyName=Tencent}," +
                " {companyCode=600519.SS, marketCap=$293.16B, stockPrice=$233.37, lastDayVariation=-0.18%, companyName=KweichowMoutai}," +
                " {companyCode=1398.HK, marketCap=$226.55B, stockPrice=$0.48, lastDayVariation=1.63%, companyName=ICBC}," +
                " {companyCode=BABA, marketCap=$193.17B, stockPrice=$75.85, lastDayVariation=0.76%, companyName=Alibaba}," +
                " {companyCode=PDD, marketCap=$192.93B, stockPrice=$145.22, lastDayVariation=0.51%, companyName=Pinduoduo}]";
        */
        Type CompaniesList = new TypeToken<ArrayList<Company>>() {
        }.getType();
        return new Gson().fromJson(companies.getBody(), CompaniesList);
    }

    public void buyShares(String Id, String Symbol, int Quantity) {
        String restPoint = "/buyShares";
        Purchase purchase = new Purchase(Id, Symbol, Quantity);
        ResponseEntity<String> response = PostObjectForEntity(restPoint, purchase);
    }

    public void sellShares(String Id, String Symbol, int Quantity) {
        String restPoint = "/sellShares";
        Sell sell = new Sell(Id, Symbol, Quantity);
        ResponseEntity<String> response = PostObjectForEntity(restPoint, sell);
    }


    public void deleteUser(String userId) {
        String restPoint = "/deleteUser";
        ResponseEntity<String> response = PostIdForEntity(restPoint, userId);
    }

    // ----------------- SUPPORT METHODS ----------------- //

    private ResponseEntity<String> PostIdForEntity(String restPoint, String id) {
        return new RestTemplate().postForEntity(api + restPoint, id, String.class);
    }

    private ResponseEntity<String> PostObjectForEntity(String restPoint, Object obj) {
        return new RestTemplate().postForEntity(api + restPoint, obj, String.class);
    }


    private ResponseEntity<String> PostPurchaseForEntity(String restPoint, Purchase purchase) {
        return new RestTemplate().postForEntity(api + restPoint, purchase, String.class);
    }

    private ResponseEntity<String> PostSellForEntity(String restPoint, Sell sell) {
        return new RestTemplate().postForEntity(api + restPoint, sell, String.class);
    }

    private ResponseEntity<Float> GetBalanceForEntity(String restPoint, String id, Float money) {
        return new RestTemplate().getForEntity(api + restPoint, Float.class, id, money);
    }

    private ResponseEntity<String> GetStringForEntity(String restPoint) {
        return new RestTemplate().getForEntity(api + restPoint, String.class);
    }

}
