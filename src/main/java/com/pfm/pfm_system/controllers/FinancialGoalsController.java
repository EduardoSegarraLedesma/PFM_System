package com.pfm.pfm_system.controllers;

public class FinancialGoalsController {

    private static String api = null;

    private static FinancialGoalsController instance = null;

    private FinancialGoalsController() {
        api = "";
    }

    public static FinancialGoalsController getInstance() {
        if (instance == null)
            instance = new FinancialGoalsController();
        return instance;
    }

}
