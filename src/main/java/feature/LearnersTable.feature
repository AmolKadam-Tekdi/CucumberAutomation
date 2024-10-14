@SmokeTest
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

    Scenario: When user clicks on the Learners button, systmes should display the learners table to the user
      When User clicks on the Learners button
      Then System should display the learner users table to the user

      Scenario: Veriy if user able to views the users informarion by clicking on the veiw button for the particular learner user
        When User clicks on the Learners button
        When user clicks on the Veiw button for the first user

