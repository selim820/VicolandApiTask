Feature: Basic Api Functionalities

  @wip
  Scenario: User flow
    When I create new user with given data: name as "Tim",gender as "Male",email as "tim2@gmail.com",status as "Active"
    Then Status code should be 201
    And Content-type should be "application/json"
    And Response should match with following inputs: name with "Tim",gender with "Male",email with "tim2@gmail.com",status with "Active"
    And Response Name should be "Tim"
    When I rename the user as "joey"
    Then Status code should be 200
    And Response Name should be "joey"
    And Response Gender should be "Male"
    When I create a post comment for current user with following data: title as "Hey", body as "Have a nice day"
    Then Status code should be 200
    And Comment Response should match with following data: title as "Hey", body as "Have a nice day"
    And Comment Response id and User id should match
    When I delete the new user
    Then Status code should be 204
    And Deleted data should not exist













