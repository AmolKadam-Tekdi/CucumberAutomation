Feature: Login functionality test

  @SmokeTest
  Scenario: User logs into the system successfully
    Given the user is on the login page
    When the user enters the username "tekdi_admin"
    And the user enters the password "4he!KY3#cIPE"
    And the user clicks on the login button
    Then the user should see " Academic Year" on the home page.