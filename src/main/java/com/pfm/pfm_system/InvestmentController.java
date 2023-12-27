package com.pfm.pfm_system;

import org.springframework.boot.json.GsonJsonParser;
import org.springframework.web.client.RestTemplate;

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
        sendUserId(userId,"/setUser");
    }

    public List<Object> getCompanies() {
        String restPoint = "/companies";
        String companies = new RestTemplate().getForObject(
                api + restPoint,
                String.class);
        //For testing until Microservice Deployment
        companies = "[{companyCode=TCEHY, marketCap=$357.50 B, stockPrice=$37.73, lastDayVariation=3,45%, companyName=Tencent}, {companyCode=600519.SS, marketCap=$293.16 B, stockPrice=$233.37, lastDayVariation=-0,18%, companyName=Kweichow Moutai}, {companyCode=1398.HK, marketCap=$226.55 B, stockPrice=$0.48, lastDayVariation=1,63%, companyName=ICBC}, {companyCode=BABA, marketCap=$193.17 B, stockPrice=$75.85, lastDayVariation=0,76%, companyName=Alibaba}, {companyCode=PDD, marketCap=$192.93 B, stockPrice=$145.22, lastDayVariation=0,51%, companyName=Pinduoduo}, {companyCode=0941.HK, marketCap=$178.97 B, stockPrice=$8.12, lastDayVariation=2,75%}]";
        return new GsonJsonParser().parseList(companies);
    }

    public void deleteUser(String userId) {
        sendUserId(userId,"/deleteUser");
    }


    // ----------------- SUPPORT METHODS ----------------- //

    private void sendUserId(String userId, String restPoint){
        String user = new RestTemplate().postForObject(
                api + restPoint,
                userId,
                String.class);
    }


}
