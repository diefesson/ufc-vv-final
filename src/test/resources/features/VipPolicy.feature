Feature: vip policy
    The company follows a policy for adding and removing passengers from a flight, depending on passenger type

  Scenario Outline: flight with regular passengers
    Given there is a flight having number "<flightNumber>" with passengers defined in the corresponding file
    When we have regular passengers
    Then you can remove them from the flight
    And add them to another flight

    Examples: 
      | flightNumber |
      | AA1234       |
      | AA1235       |
      | AA1236       |

  Scenario Outline: flight with VIP passengers
    Given there is a flight having number "<flightNumber>" with passengers defined in the corresponding file
    When we have vip passengers
    Then you cannot remove them from the flight

    Examples: 
      | flightNumber |
      | AA1234       |
      | AA1235       |
      | AA1236       |
