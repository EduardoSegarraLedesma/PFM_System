package com.pfm.pfm_system.controllers;

import Data.Investment.Sell;
import Data.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ServerController {
    private final PersistenceController db = PersistenceController.getInstance();
    private final InvestmentController inv = InvestmentController.getInstance();

    private final FinancialGoalsController fg = FinancialGoalsController.getInstance();

    // ------------------------ Home Page ------------------------ //

    @GetMapping("/")
    public String showIndexPage() {
        return "index.html";
    }

    @GetMapping("/loginPage")
    public String showLoginPage() {
        return "login.html";
    }

    @GetMapping("/createAccountPage")
    public String showCreateAccountPage() {
        return "createAccount.html";
    }

    @GetMapping("/recoverPasswordPage")
    public String showRecoverPasswordPage() {
        return "recoverPassword.html";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam("email") String email,
            @RequestParam("username") String username,
            @RequestParam("personalId") String personalId,
            @RequestParam("password") String password,
            @RequestParam(value = "phone", required = false) String phone,
            Model model) {
        try {
            User user = db.createAccount(email, username, personalId, password, phone);
            model.addAttribute("user", user);
            return "mainPage";
        } catch (SQLException e) {
            if (e.getErrorCode() == 2627)
                model.addAttribute("error",
                        "Email already in use");
            else
                model.addAttribute("error",
                        "Failed to create account, please try later");
            return "createAccount.html";
        }
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("email") String email,
                            @RequestParam("password") String password,
                            Model model) {
        try {
            User user = db.logIn(email, password);
            model.addAttribute("user", user);
            return "mainPage.html";
        } catch (SQLException e) {
            model.addAttribute("error",
                    "Wrong email or password");
            return "login.html";
        }
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam("email") String email,
                                @RequestParam("username") String username,
                                @RequestParam("personalId") String personalId,
                                Model model) {
        try {
            model.addAttribute("password",
                    "Your new Password is : "
                            + db.resetPassword(email, username, personalId)
                            + " - Please, change it as soon as possible");
        } catch (SQLException e) {
            model.addAttribute("error", "Invalid account credentials");
        }
        return "recoverPassword.html";
    }

    // ------------------------ Hub ------------------------ //
    @GetMapping("/mainPage")
    public String showMainPage(Model model) {
        model.addAttribute("user", db.getUser());
        return "mainPage.html";
    }

    @GetMapping("/configurationPage")
    public String showConfigurationPage(Model model) {
        model.addAttribute("user", db.getUser());
        return "configuration.html";
    }

    @GetMapping("/logout")
    public String logoutUser() {
        db.cleanUser();
        return showIndexPage();
    }

    @PostMapping("/editAccount")
    public String editUserData(@RequestParam(value = "email", required = false) String email,
                               @RequestParam(value = "username", required = false) String username,
                               @RequestParam(value = "password", required = false) String password,
                               @RequestParam(value = "phone", required = false) String phone,
                               Model model) {
        try {
            db.updateConfiguration(email, username, password, phone);
        } catch (SQLException e) {
            if (e.getErrorCode() == 2627)
                model.addAttribute("error",
                        "New email is already in use by other account");
            else
                model.addAttribute("error",
                        "Failed to save some changes, please try later");
        }
        return showConfigurationPage(model);
    }

    @GetMapping("/deleteAccount")
    public String deleteAccount(Model model) {
        try {
            inv.deleteUser(db.getUser().getPersonalID());
            fg.deleteAll(db.getUser().getPersonalID());
            db.deleteAccount();
            return showIndexPage();
        } catch (SQLException e) {
            model.addAttribute("error",
                    "Failed to delete account, please try later");
            return showConfigurationPage(model);
        }
    }

    // ------------------------ Investment Microservice ------------------------ //

    @GetMapping("/investment")
    public String showInvestmentPage(Model model) {
        model.addAttribute("userName", db.getUser().getUserName());
        inv.setUser(db.getUser().getPersonalID());
        model.addAttribute("balance", inv.getBalance());
        return "investment/investmentMain.html";
    }

    @GetMapping("/investmentWallet")
    public String showInvestmentWallet(Model model) {
        model.addAttribute("purchases", inv.getPurchases(db.getUser().getPersonalID()));
        model.addAttribute("balance", inv.getBalance());
        model.addAttribute("userName", db.getUser().getUserName());
        return "investment/wallet.html";
    }

    @PostMapping("/buyStocks")
    public String buyStocks(@RequestParam("ignoredName") String ignoredName,
                            @RequestParam("amount") int amount,
                            @RequestParam("companyCode") String companyCode,
                            Model model) {
        ResponseEntity<String> response = inv.buyShares(db.getUser().getPersonalID(), companyCode, amount);
        if (!response.getBody().contains("$"))
            model.addAttribute("error", "Not Enough Balance for the Operation");
        return showInvestmentPage(model);
    }

    @GetMapping("/addInvestmentMoney")
    public String addInvestmentMoney(@RequestParam(value = "moneyInput", required = false) Float money,
                                     Model model) {
        inv.addBalance(db.getUser().getPersonalID(), money);
        return showInvestmentWallet(model);
    }

    @PostMapping("/sellPurchases")
    public String sellStocks(@RequestParam List<String> selectedPurchases,
                             Model model) {
        List<Sell> toSell = new ArrayList<>();
        selectedPurchases.remove(0);
        for (String purchaseInfo : selectedPurchases) {
            String[] parts = purchaseInfo.split(",");
            String symbol = parts[0];
            int quantity = Integer.parseInt(parts[1]);
            String transactionDate = parts[2];
            toSell.add(new Sell(db.getUser().getPersonalID(), symbol, quantity, transactionDate));
        }
        inv.sellShares(toSell);
        return showInvestmentWallet(model);
    }

    // ------------------------  FinancialGoals Microservice ------------------------ //

    @GetMapping("/financialGoals")
    public String showFinancialGoalsPage(Model model) {
        model.addAttribute("userName", db.getUser().getUserName());
        model.addAttribute("goals", fg.obtainGoals(db.getUser().getPersonalID()));
        return "financialGoals/financialGoalsMain.html";
    }

    @PostMapping("/createGoal")
    public String createGoal(@RequestParam("description") String description,
                             @RequestParam("targetAmount") BigDecimal targetAmount,
                             @RequestParam("currentAmount") BigDecimal currentAmount,
                             @RequestParam("startDate") String startDate,
                             @RequestParam("endDate") String endDate,
                             Model model) {
        ResponseEntity<String> response = fg.createGoal(0, db.getUser().getPersonalID(), description,
                targetAmount, currentAmount, startDate, endDate);
        if (response.getStatusCode() == HttpStatus.BAD_REQUEST)
            model.addAttribute("error", "Unable to create goal, please try later");
        return showFinancialGoalsPage(model);
    }

    @GetMapping("/editGoal")
    public String showEditGoalPage(@RequestParam("goalId") int goalId,
                                   Model model) {
        model.addAttribute("goal", fg.searchGoalById(goalId));
        return "financialGoals/modifyGoal.html";
    }

    @PostMapping("/updateGoal")
    public String updateGoal(@RequestParam("goalId") int goalId,
                             @RequestParam("description") String description,
                             @RequestParam("targetAmount") BigDecimal targetAmount,
                             @RequestParam("currentAmount") BigDecimal currentAmount,
                             @RequestParam("startDate") String startDate,
                             @RequestParam("endDate") String endDate,
                             Model model) {
        ResponseEntity<String> response = fg.updateGoal(goalId, db.getUser().getPersonalID(), description,
                targetAmount, currentAmount, startDate, endDate);
        if (response.getStatusCode() == HttpStatus.BAD_REQUEST)
            model.addAttribute("error", "Unable to update goal, please try later");
        return showFinancialGoalsPage(model);
    }

    @PostMapping("/deleteGoal")
    public String deleteGoal(@RequestParam("goalId") int goalId,
                             Model model) {
        ResponseEntity<String> response = fg.deleteGoal(goalId);
        if (response.getStatusCode() == HttpStatus.BAD_REQUEST)
            model.addAttribute("error", "Unable to delete goal, please try later");
        return showFinancialGoalsPage(model);
    }

}