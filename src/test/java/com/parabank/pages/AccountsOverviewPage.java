package com.parabank.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.util.Map;

public class AccountsOverviewPage extends BasePage {
    public AccountsOverviewPage(Page page)
     { 
        super(page); 
    }

    public Map<String, String> getAccountDetails(String accountId) {
        Locator accountRow = page.locator("tr:has-text('" + accountId + "')");
        String balance = accountRow.locator("td").nth(1).innerText();
        String availableAmount = accountRow.locator("td").nth(2).innerText();
        return Map.of("balance", balance, "availableAmount", availableAmount);
    }

    public String getFirstAccountId() {
        return page.locator("#accountTable tbody tr:first-child td:first-child a").innerText();
    }
}
