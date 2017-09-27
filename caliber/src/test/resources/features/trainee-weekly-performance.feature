# feature
@trainee-weekly-performance
Feature: View Trainee's weekly progress and compare to the Batch
  As a user
  I can see weekly progress for each trainee
  to note when his/her performance improves or declines
  and to compare performance to the batch

  #background
  Background: Logged in
    Given that I am logged in as a User
    And on the Reports page

    Scenario:
    And I select a year
    And I have chosen "Patrick Walsh - 2/14/17" as a batch
    When I select "Patrick Muldoon" as a trainee
    Then I can compare the Trainee performance to the batch performance
