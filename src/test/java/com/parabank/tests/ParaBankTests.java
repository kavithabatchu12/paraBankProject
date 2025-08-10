package com.parabank.tests;

import com.microsoft.playwright.*;
import com.parabank.pages.*;
import com.parabank.utils.ConfigReader;
import com.parabank.utils.TestUtils;
import io.restassured.filter.session.SessionFilter;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ParaBankTests {

    // Playwright objects
    private static Playwright playwright;
    private static Browser browser;
    private BrowserContext context;
    private Page page;

    // Configuration and Test Data objects
    private static ConfigReader configReader;
    private static String baseUrl;
    private static String password;
    // Username is static to be shared across tests
    private static final String username = TestUtils.generateRandomUsername();
    private static String customerId;
    private static String newAccountId;
    private static Map<String, String> user;
    private static Map<String, String> payee;


    @BeforeClass
    public static void setupClass() {
        configReader = new ConfigReader();
        baseUrl = configReader.getProperty("baseUrl");
        password = configReader.getProperty("password");

        user = Map.of(
            "firstName", configReader.getProperty("user.firstName"),
            "lastName", configReader.getProperty("user.lastName"),
            "address", configReader.getProperty("user.address"),
            "city", configReader.getProperty("user.city"),
            "state", configReader.getProperty("user.state"),
            "zipCode", configReader.getProperty("user.zipCode"),
            "phone", configReader.getProperty("user.phone"),
            "ssn", configReader.getProperty("user.ssn"),
            "username", username,
            "password", password
        );

        payee = Map.of(
            "name", configReader.getProperty("payee.name"),
            "address", configReader.getProperty("payee.address"),
            "city", configReader.getProperty("payee.city"),
            "state", configReader.getProperty("payee.state"),
            "zipCode", configReader.getProperty("payee.zipCode"),
            "phone", configReader.getProperty("payee.phone"),
            "account", configReader.getProperty("payee.account")
        );

        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @AfterClass
    public static void closeBrowser() {
        if (playwright != null) {
            playwright.close();
        }
    }

    @BeforeMethod
    public void setupTest() {
        context = browser.newContext();
        page = context.newPage();
        // Increased timeout to 60 seconds to prevent navigation errors
        page.navigate(baseUrl, new Page.NavigateOptions().setTimeout(60000));
    }

    @AfterMethod
    public void closeContext() {
        if (context != null) {
            context.close();
        }
    }

    @Test(priority = 1, description = "UI Scenarios 1-9: Full user flow")
    public void testFullUiFlow() {
        HomePage homePage = new HomePage(page);

        // Scenario 1 & 2: Navigate and create a new user
        RegisterPage registerPage = homePage.goToRegisterPage();
        registerPage.registerUser(user);
        Assert.assertTrue(page.locator("text=Welcome " + user.get("username")).isVisible(), "Assertion Failed: Registration failed or welcome message not found.");

        // Scenario 3 & 4: User is auto-logged in. Verify global navigation.
        homePage.verifyNavLinks();
        
        // Scenario 5: Create a Savings account and capture the account number
        OpenAccountPage openAccountPage = homePage.goToOpenNewAccountPage();
        newAccountId = openAccountPage.openNewAccount("SAVINGS");
        Assert.assertTrue(page.locator("h1.title:has-text('Account Opened!')").isVisible(), "Assertion Failed: 'Account Opened!' message not visible.");
        Assert.assertNotNull(newAccountId, "Assertion Failed: New account ID is null.");

        // Scenario 6: Validate Accounts Overview page
        AccountsOverviewPage accountsOverviewPage = homePage.goToAccountsOverviewPage();
        Map<String, String> accountDetails = accountsOverviewPage.getAccountDetails(newAccountId);
        Assert.assertEquals(accountDetails.get("balance"), "$100.00", "Assertion Failed: Balance on overview page is incorrect.");
        Assert.assertEquals(accountDetails.get("availableAmount"), "$100.00", "Assertion Failed: Available amount on overview page is incorrect.");

        // Scenario 7: Transfer funds
        String firstAccountId = accountsOverviewPage.getFirstAccountId();
        TransferFundsPage transferFundsPage = homePage.goToTransferFundsPage();
        transferFundsPage.transfer("50", newAccountId, firstAccountId);
        Assert.assertTrue(page.locator("h1:has-text('Transfer Complete!')").isVisible(), "Assertion Failed: Fund transfer did not complete.");

        // Scenario 8: Pay a bill
        BillPayPage billPayPage = homePage.goToBillPayPage();
        String amount = "25.00";
        billPayPage.payBill(payee, amount, newAccountId);
        Assert.assertTrue(page.locator("h1:has-text('Bill Payment Complete')").isVisible(), "Assertion Failed: Bill payment did not complete.");
        String expectedSuccessMessage = String.format("Bill payment to %s in the amount of $%s from account %s was successful.", payee.get("name"), amount, newAccountId);
        Assert.assertTrue(page.locator("text=" + expectedSuccessMessage).isVisible(), "Assertion Failed: Bill payment success message not found.");
    }

    @Test(priority = 2, description = "API Scenarios 1-2: Find and validate transaction", dependsOnMethods = {"testFullUiFlow"})
    public void testFindTransactionByApi() {
        // This test only runs if the UI flow test passes.
        String amount = "25.00"; // This is the amount from the bill pay step.

        SessionFilter sessionFilter = new SessionFilter();

        // 1. Log in via the web form to establish a session and get the cookie.
        given()
            .redirects().follow(false) // Allow the redirect without following it
            .baseUri(baseUrl)
            .param("username", username)
            .param("password", password)
            .filter(sessionFilter) // This will capture the JSESSIONID cookie
        .when()
            .post("/parabank/login.htm");

        // 2. Use the captured session to make the authenticated API call.
        Response transactionResponse = given()
            .baseUri(baseUrl)
            .filter(sessionFilter) // This re-uses the cookie from the login step
        .when()
            .get("/parabank/services_proxy/bank/accounts/{accountId}/transactions/amount/{amount}", newAccountId, amount)
        .then()
            .log().body() // This will print just the JSON response body to the console
            .extract().response(); // Extract the response to continue with assertions
        
        Assert.assertEquals(transactionResponse.statusCode(), 200, "Assertion Failed: Could not get transaction by amount from API.");
        
        List<Map<String, ?>> transactions = transactionResponse.jsonPath().getList("$");
        Assert.assertFalse(transactions.isEmpty(), "Assertion Failed: No transaction found for the specified amount.");

        Map<String, ?> billPayTransaction = transactions.get(0);
        Assert.assertNotNull(billPayTransaction, "Assertion Failed: Bill payment transaction is null.");
        Assert.assertEquals(billPayTransaction.get("type"), "Debit", "Assertion Failed: Transaction type is not Debit.");
        Assert.assertEquals(((Number)billPayTransaction.get("amount")).floatValue(), 25.00f, "Assertion Failed: Transaction amount is incorrect.");
        Assert.assertTrue(billPayTransaction.get("description").toString().contains(payee.get("name")), "Assertion Failed: Transaction description is incorrect.");
    }
}
