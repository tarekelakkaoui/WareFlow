Feature: Process shipment orders
As manager, I want to process shipment orders to keep their status up-to-date.

  Background: 
    Given the following employees exist in the system
      | username | password | name  | phoneNumber   |
      | jeff     | pass1    | Jeff  | (555)555-5555 |
      | smith    | pass2    | Smith | (555)555-5555 |
    Given the following manager exists in the system
      | username | password |
      | manager  | manager  |
    Given the following items exist in the system
      | name        | expectedLifeSpanInDays |
      | NotFound TV |                   1800 |
      | Bed         |                   5000 |
    Given the following containers exist in the system
      | containerNumber | type        | purchaseDate | areaNumber | slotNumber |
      |               1 | NotFound TV |   2022-03-20 |          9 |         23 |
      |               2 | Bed         |   2010-01-30 |         10 |         35 |
      |               3 | Bed         |   2010-01-30 |          1 |         35 |
    Given the following orders exist in the system
      | id | orderPlacer | placedOnDate | description               | containerNumber | status | quantity |
      |  1 | manager     |   2023-07-20 | Some TVs for shipment     |               2 | Open   |        5 |
      |  2 | jeff        |   2023-07-10 | 10 beds packaged together |               1 | Open   |       10 |

  Scenario Outline: Successfully assign a shipment order
    When the manager attempts to assign the order "<orderId>" to "<employeeUsername>" with estimated time "<timeEstimate>", priority "<priority>", and requires approval "<requiresApproval>"
    Then the order "<orderId>" shall be marked as "Assigned"
    Then the order "<orderId>" shall be assigned to "<employeeUsername>"
    Then the order "<orderId>" shall have estimated time "<timeEstimate>", priority "<priority>", and requires approval "<requiresApproval>"

    Examples: 
      | orderId | employeeUsername | timeEstimate     | priority | requiresApproval |
      |       1 | smith            | ThreeToSevenDays | Low      | false            |
      |       2 | jeff             | OneToThreeDays   | Urgent   | true             |

  Scenario Outline: Unsuccesfully assign a shipment order due to non-existing employee
    When the manager attempts to assign the order "<orderId>" to "<employeeUsername>" with estimated time "<timeEstimate>", priority "<priority>", and requires approval "<requiresApproval>"
    Then the order "<orderId>" shall be marked as "Open"
    Then the system shall raise the error "<error>"

    Examples: 
      | orderId | employeeUsername | timeEstimate     | priority | requiresApproval | error                           |
      |       1 | not_exist        | ThreeToSevenDays | Low      | false            | Staff to assign does not exist. |

  Scenario Outline: Unsuccesfully assign a shipment order due to wrong input
    When the manager attempts to assign the order "<orderId>" to "smith" with estimated time "ThreeToSevenDays", priority "Low", and requires approval "false"
    Then the order "<orderId>" shall not exist in the system
    Then the number of orders in the system shall be "2"
    Then the system shall raise the error "<error>"

    Examples: 
      | orderId | error                          |
      |       3 | shipment order does not exist. |
      |       4 | shipment order does not exist. |

  Scenario Outline: Unsuccesfully assign a shipment order due to wrong state
    Given order "1" is marked as "<state>"
    When the manager attempts to assign the order "1" to "smith" with estimated time "ThreeToSevenDays", priority "Low", and requires approval "false"
    Then the order "1" shall be marked as "<state>"
    Then the system shall raise the error "<error>"

    Examples: 
      | state      | error                                                |
      | Assigned   | The shipment order is already assigned.              |
      | Completed  | Cannot assign a shipment order which is completed.   |
      | Closed     | Cannot assign a shipment order which is closed.      |
      | InProgress | Cannot assign a shipment order which is in progress. |

  Scenario Outline: Successfully start an assigned shipment order
    Given order "<orderId>" is marked as "Assigned"
    When the warehouse staff attempts to start the order "<orderId>"
    Then the order "<orderId>" shall be marked as "InProgress"

    Examples: 
      | orderId |
      |       1 |
      |       2 |

  Scenario Outline: Unsuccesfully start a shipment order due to wrong input
    When the warehouse staff attempts to start the order "<orderId>"
    Then the order "<orderId>" shall not exist in the system
    Then the number of orders in the system shall be "2"
    Then the system shall raise the error "<error>"

    Examples: 
      | orderId | error                          |
      |       3 | shipment order does not exist. |
      |       4 | shipment order does not exist. |

  Scenario Outline: Unsuccesfully start a shipment order due to wrong state
    Given order "1" is marked as "<state>"
    When the warehouse staff attempts to start the order "1"
    Then the order "1" shall be marked as "<state>"
    Then the system shall raise the error "<error>"

    Examples: 
      | state      | error                                             |
      | Open       | Cannot start a shipment order which is open.      |
      | Completed  | Cannot start a shipment order which is completed. |
      | Closed     | Cannot start a shipment order which is closed.    |
      | InProgress | The shipment order is already in progress.        |

  Scenario Outline: Successfully complete a shipment order
    Given order "<orderId>" is marked as "InProgress" with requires approval "<requiresApproval>"
    When the warehouse staff attempts to complete the order "<orderId>"
    Then the order "<orderId>" shall be marked as "<state>"

    Examples: 
      | orderId | requiresApproval | state     |
      |       1 | false            | Closed    |
      |       2 | true             | Completed |
      |       1 | true             | Completed |
      |       2 | false            | Closed    |

  Scenario Outline: Unsuccesfully complete a shipment order due to wrong input
    When the warehouse staff attempts to complete the order "<orderId>"
    Then the order "<orderId>" shall not exist in the system
    Then the number of orders in the system shall be "2"
    Then the system shall raise the error "<error>"

    Examples: 
      | orderId | error                          |
      |       3 | shipment order does not exist. |
      |       4 | shipment order does not exist. |

  Scenario Outline: Unsuccesfully complete a shipment order due to wrong state
    Given order "1" is marked as "<state>"
    When the warehouse staff attempts to complete the order "1"
    Then the order "1" shall be marked as "<state>"
    Then the system shall raise the error "<error>"

    Examples: 
      | state     | error                                               |
      | Open      | Cannot complete a shipment order which is open.     |
      | Assigned  | Cannot complete a shipment order which is assigned. |
      | Closed    | The shipment order is already closed.               |
      | Completed | The shipment order is already completed.            |

  Scenario Outline: Successfully disapprove a shipment order
    Given order "<orderId>" is marked as "Completed"
    When the manager attempts to disapprove the order "<orderId>" on date "2023-10-15" and with reason "Disapprove! Redo!"
    Then the order "<orderId>" shall be marked as "InProgress"
    Then the order with id "<orderId>" shall have the following notes
      | noteTaker | addedOnDate | description       |
      | manager   |  2023-10-15 | Disapprove! Redo! |

    Examples: 
      | orderId |
      |       1 |
      |       2 |

  Scenario Outline: Unsuccesfully disapprove a shipment order due to wrong input
    When the manager attempts to disapprove the order "<orderId>" on date "2023-10-15" and with reason "Disapprove! Redo!"
    Then the order "<orderId>" shall not exist in the system
    Then the number of orders in the system shall be "2"
    Then the system shall raise the error "<error>"

    Examples: 
      | orderId | error                          |
      |       3 | shipment order does not exist. |
      |       4 | shipment order does not exist. |

  Scenario Outline: Unsuccesfully disapprove a shipment order due to wrong state
    Given order "1" is marked as "<state>"
    When the manager attempts to disapprove the order "1" on date "2023-10-15" and with reason "Disapprove! Redo!"
    Then the order "1" shall be marked as "<state>"
    Then the system shall raise the error "<error>"

    Examples: 
      | state      | error                                                    |
      | Open       | Cannot disapprove a shipment order which is open.        |
      | Assigned   | Cannot disapprove a shipment order which is assigned.    |
      | Closed     | Cannot disapprove a shipment order which is closed.      |
      | InProgress | Cannot disapprove a shipment order which is in progress. |

  Scenario Outline: Successfully approve a shipment order
    Given order "<orderId>" is marked as "Completed"
    When the manager attempts to approve the order "<orderId>"
    Then the order "<orderId>" shall be marked as "Closed"

    Examples: 
      | orderId |
      |       1 |
      |       2 |

  Scenario Outline: Unsuccesfully approve a shipment order due to wrong input
    When the manager attempts to approve the order "<orderId>"
    Then the order "<orderId>" shall not exist in the system
    Then the number of orders in the system shall be "2"
    Then the system shall raise the error "<error>"

    Examples: 
      | orderId | error                          |
      |       3 | shipment order does not exist. |
      |       4 | shipment order does not exist. |

  Scenario Outline: Unsuccesfully approve a shipment order due to wrong state
    Given order "1" is marked as "<state>"
    When the manager attempts to approve the order "1"
    Then the order "1" shall be marked as "<state>"
    Then the system shall raise the error "<error>"

    Examples: 
      | state      | error                                                 |
      | Open       | Cannot approve a shipment order which is open.        |
      | Assigned   | Cannot approve a shipment order which is assigned.    |
      | Closed     | The shipment order is already closed.                 |
      | InProgress | Cannot approve a shipment order which is in progress. |
