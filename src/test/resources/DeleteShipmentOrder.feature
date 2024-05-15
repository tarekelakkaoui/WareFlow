Feature: Delete Shipment Order (f13)
As a manager, I want to delete a shipment order so that it is no longer available.

  Background: 
    Given the following employees exist in the system (f13)
      | username | password | name  | phoneNumber   |
      | jeff     | pass1    | Jeff  | (555)555-5555 |
      | smith    | pass2    | Smith | (555)555-5555 |
    Given the following manager exists in the system (f13)
      | username | password |
      | manager  | manager  |
    Given the following item types exist in the system (f13)
      | name        | expectedLifeSpanInDays |
      | notfound tv |                   1800 |
      | bed         |                   5000 |
    Given the following containers exist in the system (f13)
      | containerNumber | type        | addedOnDate | areaNumber | slotNumber |
      |               1 | notfound tv |  2022-03-20 |          9 |         23 |
      |               2 | bed         |  2010-01-30 |         10 |         35 |
    Given the following orders exist in the system (f13)
      | id | orderPlacer | placedOnDate | description                   | containerNumber | quantity |
      |  1 | manager     |   2023-07-20 | This is a dummy description 1 |               2 |       10 |
      |  2 | smith       |   2023-07-10 | This is a dummy description 2 |               1 |        5 |

  Scenario: Successfully delete a shipment order
    When the manager attempts to delete the shipment order with id "2" (f13)
    Then the number of shipment orders in the system shall be "1" (f13)
    Then the following orders shall exist in the system (f13)
      | id | orderPlacer | placedOnDate | description                   | containerNumber | quantity |
      |  1 | manager     |   2023-07-20 | This is a dummy description 1 |               2 |       10 |

  Scenario: Successfully delete a shipment order that does not exist in the system
    When the manager attempts to delete the shipment order with id "3" (f13)
    Then the number of shipment orders in the system shall be "2" (f13)
    Then the following orders shall exist in the system (f13)
      | id | orderPlacer | placedOnDate | description                   | containerNumber | quantity |
      |  1 | manager     |   2023-07-20 | This is a dummy description 1 |               2 |       10 |
      |  2 | smith       |   2023-07-10 | This is a dummy description 2 |               1 |        5 |
