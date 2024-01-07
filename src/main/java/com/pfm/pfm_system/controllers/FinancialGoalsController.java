package com.pfm.pfm_system.controllers;

import Data.Budget.FinancialGoal;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FinancialGoalsController {

    private static String api = null;

    private static FinancialGoalsController instance = null;

    private FinancialGoalsController() {
        api = "https://goals-microservice.happywave-288968fb.eastasia.azurecontainerapps.io";
    }

    public static FinancialGoalsController getInstance() {
        if (instance == null)
            instance = new FinancialGoalsController();
        return instance;
    }

    public void createGoal(Integer goalId, String userId, String description,
                           BigDecimal targetAmount, BigDecimal currentAmount,
                           Date startDate, Date endDate) {
        String restPoint = "";
        FinancialGoal fgoal = new FinancialGoal(goalId, userId, description, targetAmount, currentAmount, startDate, endDate);
        PostString(restPoint, new Gson().toJson(fgoal));
    }

    public void updateGoal(Integer goalId, String userId, String description,
                           BigDecimal targetAmount, BigDecimal currentAmount,
                           Date startDate, Date endDate) {
        String restPoint = "";
        FinancialGoal fgoal = new FinancialGoal(goalId, userId, description, targetAmount, currentAmount, startDate, endDate);
        PostString(restPoint, new Gson().toJson(fgoal));
    }

    public void deleteGoal(Integer goalId, String userId) {
        String restPoint = "";
        FinancialGoal fgoal = new FinancialGoal(goalId, userId, null, null, null, null, null);
        PostString(restPoint, new Gson().toJson(fgoal));
    }

    public List<FinancialGoal> obtainGoals(String userId) {
        String restPoint = "";
        Type GoalsList = new TypeToken<ArrayList<FinancialGoal>>() {
        }.getType();
        //ResponseEntity<String> response = GetStringForString(restPoint, userId);
        //Data for testing
        String response = "[\n" +
                "    {\n" +
                "        \"goalId\": 1,\n" +
                "        \"userId\": \"user123\",\n" +
                "        \"description\": \"Save for vacation\",\n" +
                "        \"targetAmount\": 10000.0,\n" +
                "        \"currentAmount\": 2500.0,\n" +
                "        \"startDate\": \"2024-01-07\",\n" +
                "        \"endDate\": \"2025-01-06\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"goalId\": 2,\n" +
                "        \"userId\": \"user456\",\n" +
                "        \"description\": \"Emergency fund\",\n" +
                "        \"targetAmount\": 5000.0,\n" +
                "        \"currentAmount\": 1200.0,\n" +
                "        \"startDate\": \"2024-02-01\",\n" +
                "        \"endDate\": \"2024-12-31\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"goalId\": 3,\n" +
                "        \"userId\": \"user789\",\n" +
                "        \"description\": \"Home renovation\",\n" +
                "        \"targetAmount\": 20000.0,\n" +
                "        \"currentAmount\": 8000.0,\n" +
                "        \"startDate\": \"2024-03-15\",\n" +
                "        \"endDate\": \"2025-03-14\"\n" +
                "    }\n" +
                "]\n";
        return new Gson().fromJson(response, GoalsList);
    }

    // ----------------- SUPPORT METHODS ----------------- //

    private ResponseEntity<String> PostString(String restPoint, String obj) {
        return new RestTemplate().postForEntity(api + restPoint, obj, String.class);
    }

    private ResponseEntity<String> GetStringForString(String restPoint, String obj) {
        return new RestTemplate().getForEntity(api + restPoint, String.class, obj);
    }

}
