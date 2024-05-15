Feature: View all shipment orders including status
As manager, I want to review all shipment orders in the system with their status.

  Background: 
    Given the following employees exist in the system
      | username | password | name  | phoneNumber   |
      | jeff     | pass1    | Jeff  | (555)555-5555 |
      | smith    | pass2    | Smith | (555)555-5555 |
    Given the following manager exists in the system
      | username | password |
      | manager  | manager  |
    Given the following items exist in the system
      | name        | expectedLifeSpan |
      | NotFound TV |             1800 |
      | Bed         |             5000 |
    Given the following containers exist in the system
      | containerNumber | type        | purchaseDate | areaNumber | slotNumber |
      |               1 | NotFound TV |   2022-03-20 |          9 |         23 |
      |               2 | Bed         |   2010-01-30 |         10 |         35 |
      |               3 | Bed         |   2010-01-30 |          1 |         35 |
    Given the following orders exist in the system
      | id | orderPlacer | placedOnDate | description                         | containerNumber | quantity | status     | processedBy | timeToResolve    | priority | approvalRequired |
      |  1 | manager     |   2023-07-20 | The Bed needs replacement shipments |               2 |       10 | Assigned   | smith       | LessThanADay     | Low      | true             |
      |  2 | smith       |   2023-07-10 | Some more TVs are needed            |               1 |        5 | InProgress | smith       | OneToThreeDays   | Low      | true             |
      |  3 | manager     |   2023-07-20 | We need some more TVs               |               1 |       30 | Open       |             |                  |          |                  |
      |  4 | smith       |   2023-10-15 | The NotFound TV sells great         |               1 |       20 | Completed  | jeff        | OneToThreeWeeks  | Normal   | true             |
      |  5 | smith       |   2023-10-16 | The Bed needs replacement           |               3 |        7 | Closed     | jeff        | ThreeOrMoreWeeks | Urgent   | false            |
    Given the following notes exist in the system
      | noteTaker | orderId | addedOnDate | description                        |
      | jeff      |       2 |  2023-09-01 | TVs are currently out of stock     |
      | smith     |       1 |  2023-09-10 | Beds will arrive next week         |
      | manager   |       1 |  2023-09-23 | TVs restocked, continuing progress |
      | manager   |       4 |  2023-10-17 | The shipment needs further check   |
      | manager   |       5 |  2023-10-18 | The Bed shipment is now shipped    |

  Scenario: Successfully view status of all shipment orders
    When the manager attempts to view all shipment orders in the system
    Then the following shipment orders shall be presented
      | id | orderPlacer | placedOnDate | description                         | assetName   | expectLifeSpan | purchaseDate | areaNumber | slotNumber | quantity | status     | processedBy | timeToResolve    | priority | approvalRequired |
      |  1 | manager     |   2023-07-20 | The Bed needs replacement shipments | Bed         |           5000 |   2010-01-30 |         10 |         35 |       10 | Assigned   | smith       | LessThanADay     | Low      | true             |
      |  2 | smith       |   2023-07-10 | Some more TVs are needed            | NotFound TV |           1800 |   2022-03-20 |          9 |         23 |        5 | InProgress | smith       | OneToThreeDays   | Low      | true             |
      |  3 | manager     |   2023-07-20 | We need some more TVs               | NotFound TV |           1800 |   2022-03-20 |          9 |         23 |       30 | Open       |             |                  |          |                  |
      |  4 | smith       |   2023-10-15 | The NotFound TV sells great         | NotFound TV |           1800 |   2022-03-20 |          9 |         23 |       20 | Completed  | jeff        | OneToThreeWeeks  | Normal   | true             |
      |  5 | smith       |   2023-10-16 | The Bed needs replacement           | Bed         |           5000 |   2010-01-30 |          1 |         35 |        7 | Closed     | jeff        | ThreeOrMoreWeeks | Urgent   | false            |
    Then the order with id "1" shall have the following notes
      | noteTaker | addedOnDate | description                        |
      | smith     |  2023-09-10 | Beds will arrive next week         |
      | manager   |  2023-09-23 | TVs restocked, continuing progress |
    Then the order with id "2" shall have the following notes
      | noteTaker | addedOnDate | description                    |
      | jeff      |  2023-09-01 | TVs are currently out of stock |
    Then the order with id "3" shall have no notes
    Then the order with id "4" shall have the following notes
      | noteTaker | addedOnDate | description                      |
      | manager   |  2023-10-17 | The shipment needs further check |
    Then the order with id "5" shall have the following notes
      | noteTaker | addedOnDate | description                     |
      | manager   |  2023-10-18 | The Bed shipment is now shipped |
