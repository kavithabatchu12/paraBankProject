Para Bank E2E Test Automation Framework
This project contains a complete end-to-end (E2E) test automation framework for the Para Bank application. It is built using Java and features a hybrid testing approach:

UI Testing: Uses Playwright for browser automation to simulate user interactions.

API Testing: Uses REST-assured to make direct calls to the application's backend services.

Test Management: Uses TestNG as the testing framework to structure and run the tests.

Project Management: Uses Maven for dependency management and building the project.

✅ Scenarios Covered
This framework validates the following user and system flows:

UI Scenarios
Navigate & Register: Navigates to the Para Bank application and creates a new, unique user in each test run.

Login & Navigation: Confirms that the newly created user can log in and that the main navigation menu is visible and working.

Create Account: Creates a new Savings account and captures the new account number.

Validate Account Overview: Verifies that the new account appears correctly on the "Accounts Overview" page with the expected balance.

Transfer Funds: Performs a fund transfer from the new account to another existing account.

Pay Bill: Uses the new account to pay a bill to a predefined payee.

API Scenarios
Find Transaction: After the UI flow is complete, it uses an API endpoint to find the specific bill payment transaction created in the UI test.

Validate Details: Validates the details (type, amount, description) of the transaction in the JSON response to ensure data integrity.

🚀 Getting Started
Prerequisites
Before you begin, ensure you have the following installed on your system:

Java Development Kit (JDK): Version 11 or higher.

Apache Maven: To manage project dependencies and run the tests.

Visual Studio Code: Recommended code editor.

Extension Pack for Java (in VS Code): The official Microsoft extension for Java development support.

🛠️ Setup Instructions
Clone or Download the Project: Get the project files onto your local machine.

Open in VS Code: Open the root folder of the project (the one containing pom.xml) in Visual Studio Code.

Install Dependencies: VS Code, with the Java extensions, should automatically detect the pom.xml file and start downloading the required libraries (dependencies). If this doesn't happen, you can trigger it manually:

Right-click the pom.xml file in the Explorer.

Select "Update Maven Project".

▶️ How to Run Tests
You can run the full test suite in two ways:

1. From Visual Studio Code (Recommended)
Open the tests/ParaBankTests.java file.

Click the "Run Test" button that appears above the public class ParaBankTests line.

VS Code will execute the tests using TestNG, and you can monitor the progress and see the results in the "Test Explorer" panel.

2. From the Command Line
Open the integrated terminal in VS Code (View > Terminal or `Ctrl+``).

Make sure you are in the root directory of the project (e.g., java-playwright-framework).

Run the following Maven command:

mvn clean test

📊 Viewing Test Results
After a test run is complete, you can view the results in the Surefire HTML report.

Navigate to the target/surefire-reports directory within your project.

Find the file named emailable-report.html.

Right-click the file and select "Reveal in File Explorer" (or your OS equivalent).

Open the emailable-report.html file in any web browser to see a detailed summary of the test execution, including which tests passed, failed, and how long they took.
