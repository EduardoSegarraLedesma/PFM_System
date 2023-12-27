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
        String restPoint = "/setUser";
        String user = new RestTemplate().postForObject(
                api + restPoint,
                userId,
                String.class);
    }

    public List<Object> getCompanies() {
        String restPoint = "/companies";
        String companies = new RestTemplate().getForObject(
                api + restPoint,
                String.class);
        return new GsonJsonParser().parseList(companies);
    }

    public void deleteUser(String userId) {
        String restPoint = "/deleteUser";
        String user = new RestTemplate().postForObject(
                api + restPoint,
                userId,
                String.class);

    }


    // ----------------- SUPPORT METHODS ----------------- //

}
