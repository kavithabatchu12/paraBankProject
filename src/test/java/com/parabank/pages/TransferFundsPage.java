package com.parabank.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.SelectOption;
import java.util.function.Predicate;

public class TransferFundsPage extends BasePage {
    public TransferFundsPage(Page page) { super(page); }

    public void transfer(String amount, String fromAccountId, String toAccountId) {
        page.locator("#amount").fill(amount);
        page.locator("#fromAccountId").selectOption(new SelectOption().setValue(fromAccountId));
        page.locator("#toAccountId").selectOption(new SelectOption().setValue(toAccountId));

        // This is the fix: We wait for the fund transfer network request to complete
        // before moving on, which makes the test much more reliable.
        Runnable clickAction = () -> page.locator("input[type='submit'][value='Transfer']").click();

        Predicate<Response> responsePredicate = response ->
            response.url().contains("transfer") && response.status() == 200;

        page.waitForResponse(responsePredicate, clickAction);
    }
}
