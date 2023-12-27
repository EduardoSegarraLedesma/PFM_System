package com.pfm.pfm_system.controllers;

import Data.Company;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


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
        //sendUserId(userId, "/setUser");
    }

    public List<Company> getCompanies() {
        String restPoint = "/companies";
        Type CompaniesList = new TypeToken<ArrayList<Company>>() {
        }.getType();
        /*String companies = new RestTemplate().getForObject(
                api + restPoint,
                String.class);*/

        //For testing until Microservice Deployment
        String companies = "[{companyCode=TCEHY, marketCap=$357.50B, stockPrice=$37.73, lastDayVariation=3.45%, companyName=Tencent}," +
                " {companyCode=600519.SS, marketCap=$293.16B, stockPrice=$233.37, lastDayVariation=-0.18%, companyName=KweichowMoutai}," +
                " {companyCode=1398.HK, marketCap=$226.55B, stockPrice=$0.48, lastDayVariation=1.63%, companyName=ICBC}," +
                " {companyCode=BABA, marketCap=$193.17B, stockPrice=$75.85, lastDayVariation=0.76%, companyName=Alibaba}," +
                " {companyCode=PDD, marketCap=$192.93B, stockPrice=$145.22, lastDayVariation=0.51%, companyName=Pinduoduo}]";
        return new Gson().fromJson(companies, CompaniesList);

    }

    public void deleteUser(String userId) {
        sendUserId(userId, "/deleteUser");
    }


    // ----------------- SUPPORT METHODS ----------------- //

    private String sendUserId(String userId, String restPoint) {
        return new RestTemplate().postForObject(
                api + restPoint,
                userId,
                String.class);
    }


}
