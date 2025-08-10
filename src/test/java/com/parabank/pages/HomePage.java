package com.parabank.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.testng.Assert;

public class HomePage extends BasePage {

    public HomePage(Page page) {
        super(page);
    }

    public void login(String username, String password) {
        page.locator("input[name='username']").fill(username);
        page.locator("input[name='password']").fill(password);
        page.locator("input[value='Log In']").click();
    }

    public void logout() {
        page.locator("text=Log Out").click();
    }

    public RegisterPage goToRegisterPage() {
        page.locator("text=Register").click();
        return new RegisterPage(page);
    }
    
    public OpenAccountPage goToOpenNewAccountPage() {
        page.locator("text=Open New Account").click();
        return new OpenAccountPage(page);
    }

    public AccountsOverviewPage goToAccountsOverviewPage() {
        page.locator("text=Accounts Overview").click();
        return new AccountsOverviewPage(page);
    }

    public TransferFundsPage goToTransferFundsPage() {
        page.locator("text=Transfer Funds").click();
        return new TransferFundsPage(page);
    }

    public BillPayPage goToBillPayPage() {
        page.locator("text=Bill Pay").click();
        return new BillPayPage(page);
    }

    public void verifyNavLinks() {
        // Corrected the locator to be specific to the header panel to avoid strict mode violation.
        Locator aboutUsLinkInHeader = page.locator("#headerPanel").getByText("About Us");
        Assert.assertTrue(aboutUsLinkInHeader.isVisible(), "About Us link in header is not visible.");
        // Add more assertions for other navigation links as needed.
    }
}
