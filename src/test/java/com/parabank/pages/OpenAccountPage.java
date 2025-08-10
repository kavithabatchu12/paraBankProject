package com.parabank.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.SelectOption;
import java.util.function.Predicate;

public class OpenAccountPage extends BasePage {
    public OpenAccountPage(Page page) { super(page); }

    public String openNewAccount(String accountType) { // "CHECKING" or "SAVINGS"
        // Select the type of account to open
        page.locator("#type").selectOption(new SelectOption().setLabel(accountType));

        // This is the fix: We must also select an account to transfer funds from.
        // We'll find the 'fromAccountId' dropdown and select the first option.
        Locator fromAccountDropdown = page.locator("#fromAccountId");
        String firstAccountId = fromAccountDropdown.locator("option").first().getAttribute("value");
        fromAccountDropdown.selectOption(new SelectOption().setValue(firstAccountId));
        
        // Define the action to be performed (the click).
        Runnable clickAction = () -> page.locator("input[type='button'][value='Open New Account']").click();

        // Define the condition to wait for in the response.
        Predicate<Response> responsePredicate = response ->
            response.url().contains("createAccount") && response.status() == 200;

        // Execute the action and wait for the response that matches the condition.
        page.waitForResponse(responsePredicate, clickAction);

        // Now that we've waited for the response, the new account ID will be on the page.
        return page.locator("#newAccountId").innerText();
    }
}
