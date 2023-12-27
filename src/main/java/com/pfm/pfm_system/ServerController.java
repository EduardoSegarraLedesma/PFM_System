package com.pfm.pfm_system;

import Data.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;

@Controller
public class ServerController {
    private final PersistenceController db = PersistenceController.getInstance();


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
        model.addAttribute("user",db.getUser());
        return "investmentMain.html";
    }

    // ------------------------  Microservice ------------------------ //

}