package com.pfm.pfm_system.controllers;

import Data.Transaction.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class TransactionController {

    private static String api = null;

    private static TransactionController instance = null;

    private TransactionController() {
        api = ".......";

    }

    public static TransactionController getInstance() {
        if (instance == null)
            instance = new TransactionController();
        return instance;
    }

    public void saveTransaction(String userID, String transactionType, String subcategory, String timestamp, String amount, String description) {
        Transaction transaction = new Transaction(userID, transactionType, subcategory, timestamp, amount, description);
        String restPoint = "/api/transactions";
        new RestTemplate().postForEntity(
                api + restPoint,
                transaction, ResponseEntity.class
                );
    }

    public void getTransaction() {
        String restPoint = "";

    }

    public void deleteTransaction(String transactionID) {
        String restPoint = "/api/transactions/{transactionID}";

    }

    public void getAnalytics() {
        String restPoint = "/api/analytics/income";
    }


}
