package com.pfm.pfm_system.controllers;

import Data.Budget.FinancialGoal;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FinancialGoalsController {

    private static String api = null;
    private List<FinancialGoal> fglist = null;
    private static FinancialGoalsController instance = null;

    private FinancialGoalsController() {
        api = "https://goals-microservice.happywave-288968fb.eastasia.azurecontainerapps.io";
    }

    public static FinancialGoalsController getInstance() {
        if (instance == null)
            instance = new FinancialGoalsController();
        return instance;
    }

    public ResponseEntity<String> createGoal( String userId, String description,
                                             BigDecimal targetAmount, BigDecimal currentAmount,
                                             String startDate, String endDate) {
        String restPoint = "/insertGoal/{goal}";
        FinancialGoal fgoal = new FinancialGoal(1, userId, description, targetAmount,
                currentAmount, startDate, endDate);
        return GetStringForString(restPoint, new Gson().toJson(fgoal));
    }

    public ResponseEntity<String> updateGoal(Integer goalId, String userId, String description,
                                             BigDecimal targetAmount, BigDecimal currentAmount,
                                             String startDate, String endDate) {
        String restPoint = "/editGoal/{goal}";
        FinancialGoal fgoal = new FinancialGoal(goalId, userId, description, targetAmount, currentAmount, startDate, endDate);
        return GetStringForString(restPoint, new Gson().toJson(fgoal));
    }

    public ResponseEntity<String> deleteGoal(Integer goalId) {
        String restPoint = "/deleteGoal/{goal}";
        return GetStringForString(restPoint, new Gson().toJson(searchGoalById(goalId)));
    }

    public void deleteAll(String userId) {
        String restPoint = "/deleteAll/{Id}";
        GetStringForString(restPoint, userId);
    }

    public List<FinancialGoal> obtainGoals(String userId) {
        String restPoint = "/getGoals/{userId}";
        Type GoalsList = new TypeToken<ArrayList<FinancialGoal>>() {
        }.getType();
        ResponseEntity<String> response = GetStringForString(restPoint, userId);
        fglist = new Gson().fromJson(response.getBody(), GoalsList);
        return fglist;
    }

    public FinancialGoal searchGoalById(int goalId) {
        for (FinancialGoal goal : fglist) {
            if (goal.getGoalId() == goalId) {
                return goal;
            }
        }
        return null;
    }

    // ----------------- SUPPORT METHODS ----------------- //

    private ResponseEntity<String> GetStringForString(String restPoint, String obj) {
        return new RestTemplate().getForEntity(api + restPoint, String.class, obj);
    }

}
