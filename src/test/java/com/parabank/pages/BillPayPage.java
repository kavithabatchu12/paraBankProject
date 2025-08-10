package com.parabank.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.SelectOption;
import java.util.Map;
import java.util.function.Predicate;

public class BillPayPage extends BasePage {
    public BillPayPage(Page page) { super(page); }

    public void payBill(Map<String, String> payee, String amount, String fromAccountId) {
        // Wait for the form to be ready
        page.waitForLoadState(LoadState.NETWORKIDLE);

        page.locator("input[name='payee.name']").fill(payee.get("name"));
        page.locator("input[name='payee.address.street']").fill(payee.get("address"));
        page.locator("input[name='payee.address.city']").fill(payee.get("city"));
        page.locator("input[name='payee.address.state']").fill(payee.get("state"));
        page.locator("input[name='payee.address.zipCode']").fill(payee.get("zipCode"));
        page.locator("input[name='payee.phoneNumber']").fill(payee.get("phone"));
        page.locator("input[name='payee.accountNumber']").fill(payee.get("account"));
        page.locator("input[name='verifyAccount']").fill(payee.get("account"));
        page.locator("input[name='amount']").fill(amount);
        page.locator("select[name='fromAccountId']").selectOption(new SelectOption().setValue(fromAccountId));

        // This is the most robust locator, targeting the button by its accessibility role and name.
        Runnable clickAction = () -> page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Send Payment")).click();

        // We wait for the network request to confirm the payment was sent.
        Predicate<Response> responsePredicate = response ->
           response.url().contains("billpay") && response.status() == 200;

       page.waitForResponse(responsePredicate, clickAction);
    }
}
