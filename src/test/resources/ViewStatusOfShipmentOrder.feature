Feature: View status of all shipment orders (f14)
As manager, I want to review all shipment orders in the system.

  Background: 
    Given the following employees exist in the system (f14)
      | username | password | name  | phoneNumber   |
      | jeff     | pass1    | Jeff  | (555)555-5555 |
      | smith    | pass2    | Smith | (555)555-5555 |
    Given the following manager exists in the system (f14)
      | username | password |
      | manager  | manager  |
    Given the following item types exist in the system (f14)
      | name        | expectedLifeSpanInDays |
      | notfound tv |                   1800 |
      | bed         |                   5000 |
    Given the following containers exist in the system (f14)
      | containerNumber | type        | addedOnDate | areaNumber | slotNumber |
      |               1 | notfound tv |  2022-03-20 |          9 |         23 |
      |               2 | bed         |  2010-01-30 |         10 |         35 |
      |               3 | bed         |  2010-01-30 |          1 |         35 |
    Given the following orders exist in the system (f14)
      | id | orderPlacer | placedOnDate | description                   | containerNumber | quantity |
      |  1 | manager     |   2023-07-20 | This is a dummy description 1 |               2 |       10 |
      |  2 | smith       |   2023-07-10 | This is a dummy description 2 |               1 |        5 |
      |  3 | manager     |   2023-07-20 | It is noisy                   |                 |        0 |
    Given the following notes exist in the system (f14)
      | noteTaker | orderId | date       | description                              |
      | jeff      |       2 | 2023-09-01 | This is a dummy note 1 for a order       |
      | smith     |       1 | 2023-09-10 | This is another dummy note 2 for a order |
      | manager   |       1 | 2023-09-23 | This is a dummy description 1            |

  Scenario: Successfully view status of all shipment orders
    When the manager attempts to view all shipment orders in the system (f14)
    Then the following shipment orders shall be presented (f14)
      | id | orderPlacer | placedOnDate | description                   | itemName    | expectedLifeSpanInDays | addedOnDate | areaNumber | slotNumber | quantity |
      |  1 | manager     |   2023-07-20 | This is a dummy description 1 | bed         |                   5000 |  2010-01-30 |         10 |         35 |       10 |
      |  2 | smith       |   2023-07-10 | This is a dummy description 2 | notfound tv |                   1800 |  2022-03-20 |          9 |         23 |        5 |
      |  3 | manager     |   2023-07-20 | It is noisy                   |             |                        |             |            |            |        0 |
    Then the order with id "1" shall have the following notes (f14)
      | noteTaker | date       | description                              |
      | smith     | 2023-09-10 | This is another dummy note 2 for a order |
      | manager   | 2023-09-23 | This is a dummy description 1            |
    Then the order with id "2" shall have the following notes (f14)
      | noteTaker | date       | description                        |
      | jeff      | 2023-09-01 | This is a dummy note 1 for a order |
    Then the order with id "3" shall have no notes (f14)
