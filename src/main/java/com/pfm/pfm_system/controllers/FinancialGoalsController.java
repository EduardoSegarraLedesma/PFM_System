package com.pfm.pfm_system.controllers;

import Data.Budget.FinancialGoal;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
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
        String restPoint = "/insertGoal/{goal}";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        FinancialGoal fgoal = new FinancialGoal(goalId, userId, description, targetAmount,
                currentAmount, dateFormat.format(startDate), dateFormat.format(endDate));
        GetString(restPoint, new Gson().toJson(fgoal));
    }

    public void updateGoal(Integer goalId, String userId, String description,
                           BigDecimal targetAmount, BigDecimal currentAmount,
                           Date startDate, Date endDate) {
        String restPoint = "/editGoal/{goal}";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        FinancialGoal fgoal = new FinancialGoal(goalId, userId, description, targetAmount, currentAmount, dateFormat.format(startDate), dateFormat.format(endDate));
        GetString(restPoint, new Gson().toJson(fgoal));
    }

    public void deleteGoal(Integer goalId, String userId) {
        String restPoint = "/deleteGoal/{Ids}";
        List<String> IdList = new LinkedList<>();
        IdList.add(goalId.toString());
        IdList.add(userId);
        GetString(restPoint, new Gson().toJson(IdList));
    }

    public void deleteAll(String userId) {
        String restPoint = "/deleteAll/{Id}";
        GetString(restPoint, userId);
    }

    public List<FinancialGoal> obtainGoals(String userId) {
        String restPoint = "/getGoals/{userId}";
        Type GoalsList = new TypeToken<ArrayList<FinancialGoal>>() {
        }.getType();
        ResponseEntity<String> response = GetStringForString(restPoint, userId);
        return new Gson().fromJson(response.getBody(), GoalsList);
    }

    // ----------------- SUPPORT METHODS ----------------- //

    private ResponseEntity<String> GetString(String restPoint, String obj) {
        return new RestTemplate().getForEntity(api + restPoint, String.class, obj);
    }

    private ResponseEntity<String> GetStringForString(String restPoint, String obj) {
        return new RestTemplate().getForEntity(api + restPoint, String.class, obj);
    }

}
