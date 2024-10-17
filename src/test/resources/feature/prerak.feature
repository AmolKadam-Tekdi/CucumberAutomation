Feature: Learners Table
  As an Admin user System should Allow user to View Learners table, View learners details, And Edit the user details

  Background:
    Given the user is on the login page
    When the user enters the username "tekdi_admin"
    And the user enters the password "4he!KY3#cIPE"
    And the user clicks on the login button
    When the user selects "April24-Oct-25(Bihar)" as academic year
    And User select "BIHAR" as State
    When the user clicks on the continue button

  Scenario: when user clicks on the Preraks button the system should display the Preraks information
    When The user clicks on the Preraks button
    Then The system should display the Preraks page
