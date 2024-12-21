Feature: IECS Gym Ticket Price Calculation
  In order to ensure the user can get the correct ticket price,
  when the user selects the day, time, age, and membership status,
  the system should correctly determine the most favorable discount and display the price.

  Background:
    Given I have opened the "https://nlhsueh.github.io/iecs-gym/" webpage

  Scenario: Non-member, weekday before 7 AM, age 30
    Given I select the day "星期一"
    And I select the time "早場：早上七點以前"
    And I enter the age "30"
    And I select "不是" for membership
    When I click the "Calculate Price" button
    Then I should see the fee as "160.00" dollars
    # Base price 200, before 7 AM applies 20% off (200 * 0.8 = 160)

  Scenario: Member, weekend, age 65 (eligible for senior discount, but membership is more favorable)
    Given I select the day "星期六"
    And I select the time "正常時段"
    And I enter the age "65"
    And I select "是" for membership
    And I enter the membership code "IECS-99999"
    When I click the "Calculate Price" button
    Then I should see the fee as "125.00" dollars
    # Weekend price: 250
    # Member discount: 50% -> 250 * 0.5 = 125
    # Senior discount also possible, but membership is more favorable per given logic.

  Scenario: Invalid age input (2 years old)
    Given I select the day "星期三"
    And I select the time "正常時段"
    And I enter the age "2"
    And I select "不是" for membership
    When I click the "Calculate Price" button
    Then I should see an age error message "年齡應介於 3 與 75 之間"
    And the age field should be cleared

  Scenario: Non-member, weekend without membership discount, age below 12 (20% off)
    Given I select the day "星期日"
    And I select the time "正常時段"
    And I enter the age "10"
    And I select "不是" for membership
    When I click the "Calculate Price" button
    Then I should see the fee as "200.00" dollars
    # Weekend price: 250
    # Child discount: 20% off -> 250 * 0.8 = 200

  Scenario: Member with invalid membership code (not starting with "IECS-")
    Given I select the day "星期五"
    And I select the time "正常時段"
    And I enter the age "40"
    And I select "是" for membership
    And I enter the membership code "ABC-12345"
    When I click the "Calculate Price" button
    Then I should see a membership code error message "會員編號必須以 IECS- 開頭。"
    And the fee field should not be displayed

  Scenario: Reset the form and re-enter
    Given I select the day "星期一"
    And I select the time "早場：早上七點以前"
    And I enter the age "30"
    And I select "不是" for membership
    When I click the "Calculate Price" button
    Then I should see the fee as "160.00" dollars
    When I click the "Reset" button
    Then the day field should be reset to "星期一"
    And the age field should be cleared
    And the time field should be reset to "早場：早上七點以前"
    And the membership selection should be reset to "不是"
    And the result field should be hidden
