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
        String restPoint = "/insertGoal";
        FinancialGoal fgoal = new FinancialGoal(goalId, userId, description, targetAmount, currentAmount, startDate, endDate);
        PostString(restPoint, new Gson().toJson(fgoal));
    }

    public void updateGoal(Integer goalId, String userId, String description,
                           BigDecimal targetAmount, BigDecimal currentAmount,
                           Date startDate, Date endDate) {
        String restPoint = "/editGoal";
        FinancialGoal fgoal = new FinancialGoal(goalId, userId, description, targetAmount, currentAmount, startDate, endDate);
        PostString(restPoint, new Gson().toJson(fgoal));
    }

    public void deleteGoal(Integer goalId, String userId) {
        String restPoint = "/deleteGoal";
        List<String> IdList = new LinkedList<>();
        IdList.add(goalId.toString());
        IdList.add(userId);
        PostString(restPoint, new Gson().toJson(IdList));
    }

    public void deleteAll(String userId) {
        String restPoint = "/deleteAll";
        PostString(restPoint, userId);
    }

    public List<FinancialGoal> obtainGoals(String userId) {
        String restPoint = "/getGoals/{userId}";
        Type GoalsList = new TypeToken<ArrayList<FinancialGoal>>() {
        }.getType();
        ResponseEntity<String> response = GetStringForString(restPoint, userId);
        //Data for testing
        /*String response =
                        """
                        [
                            {
                                "goalId": 1,
                                "userId": "user123",
                                "description": "Save for vacation",
                                "targetAmount": 10000.0,
                                "currentAmount": 2500.0,
                                "startDate": "2024-01-07",
                                "endDate": "2025-01-06"
                            },
                            {
                                "goalId": 2,
                                "userId": "user456",
                                "description": "Emergency fund",
                                "targetAmount": 5000.0,
                                "currentAmount": 1200.0,
                                "startDate": "2024-02-01",
                                "endDate": "2024-12-31"
                            },
                            {
                                "goalId": 3,
                                "userId": "user789",
                                "description": "Home renovation",
                                "targetAmount": 20000.0,
                                "currentAmount": 8000.0,
                                "startDate": "2024-03-15",
                                "endDate": "2025-03-14"
                            }
                        ]
                        """;
         */
        return new Gson().fromJson(response.getBody(), GoalsList);
    }

    // ----------------- SUPPORT METHODS ----------------- //

    private ResponseEntity<String> PostString(String restPoint, String obj) {
        return new RestTemplate().postForEntity(api + restPoint, obj, String.class);
    }

    private ResponseEntity<String> GetStringForString(String restPoint, String obj) {
        return new RestTemplate().getForEntity(api + restPoint, String.class, obj);
    }

}
