package com.parabank.pages;

import com.microsoft.playwright.Page;
import java.util.Map;

public class RegisterPage extends BasePage {
    public RegisterPage(Page page) { super(page); }

    public void registerUser(Map<String, String> user) {
        page.locator("#customer\\.firstName").fill(user.get("firstName"));
        page.locator("#customer\\.lastName").fill(user.get("lastName"));
        page.locator("#customer\\.address\\.street").fill(user.get("address"));
        page.locator("#customer\\.address\\.city").fill(user.get("city"));
        page.locator("#customer\\.address\\.state").fill(user.get("state"));
        page.locator("#customer\\.address\\.zipCode").fill(user.get("zipCode"));
        page.locator("#customer\\.phoneNumber").fill(user.get("phone"));
        page.locator("#customer\\.ssn").fill(user.get("ssn"));
        page.locator("#customer\\.username").fill(user.get("username"));
        page.locator("#customer\\.password").fill(user.get("password"));
        page.locator("#repeatedPassword").fill(user.get("password"));
        page.locator("input[value='Register']").click();
    }
}
