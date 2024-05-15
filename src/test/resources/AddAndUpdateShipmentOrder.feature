Feature: Add/Update Shipment Orders (f12)
As a order placer, I want to add and update a shipment order in the system.

  Background: 
    Given the following employees exist in the system (f12)
      | username | password | name  | phoneNumber   |
      | jeff     | pass1    | Jeff  | (555)555-5555 |
      | smith    | pass2    | Smith | (555)555-5555 |
    Given the following manager exists in the system (f12)
      | username | password |
      | manager  | manager  |
    Given the following clients exist in the system (f12)
      | username | password | name | phoneNumber   | address          |
      | jeff112  | pass1    | Jeff | (555)555-5555 | 123 NotFound Ave |
      | john112  | pass2    | John | (444)444-4444 | 333 NotFound Ave |
    Given the following item types exist in the system (f12)
      | name        | expectedLifeSpanInDays |
      | notfound tv |                   1800 |
      | bed         |                   5000 |
    Given the following containers exist in the system (f12)
      | containerNumber | type        | addedOnDate | areaNumber | slotNumber |
      |               1 | notfound tv |  2022-03-20 |          9 |         23 |
      |               2 | bed         |  2010-01-30 |         10 |         35 |
    Given the following orders exist in the system (f12)
      | id | orderPlacer | placedOnDate | description                   | containerNumber | quantity |
      |  1 | manager     |   2023-07-20 | This is a dummy description 1 |               2 |       12 |
      |  2 | smith       |   2023-07-10 | This is a dummy description 2 |               1 |       10 |

  Scenario Outline: Successfully add a shipment order to the system by a client, employee, or manager
    When the user with username "<orderPlacer>" attempts to add a new shipment order to the system with id "<id>", date "<placedOnDate>", description "<description>", quantity "<quantity>", and container number "<containerNumber>" (f12)
    Then the number of orders in the system shall be "3" (f12)
    Then the order raised by "<orderPlacer>" and with id "<id>", date "<placedOnDate>", description "<description>", quantity "<quantity>", and container number "<containerNumber>" shall exist in the system (f12)

    Examples: 
      | id | orderPlacer | placedOnDate | description                   | containerNumber | quantity |
      |  3 | john112     |   2023-09-23 | This is a dummy description 3 |               2 |       10 |
      |  4 | smith       |   2023-10-05 | This is a dummy description 2 |               1 |        8 |
      |  3 | manager     |   2023-09-23 | This is a dummy description 1 |               1 |        6 |

  Scenario Outline: Successfully add a shipment order without an container to the system by a client, employee, or manager
    When the user with username "<orderPlacer>" attempts to add a new shipment order to the system with id "<id>", date "<placedOnDate>", description "<description>", and quantity "<quantity>" but no container number (f12)
    Then the number of orders in the system shall be "3" (f12)
    Then the order raised by "<orderPlacer>" and with id "<id>", date "<placedOnDate>", description "<description>", and quantity "<quantity>" but no container shall exist in the system (f12)

    Examples: 
      | id | orderPlacer | placedOnDate | description               | quantity |
      |  3 | john112     |   2023-09-23 | it is noisy               |        0 |
      |  4 | smith       |   2023-10-05 | it smells                 |        0 |
      |  3 | manager     |   2023-09-23 | it smells and it is noisy |        0 |

  Scenario Outline: Unsuccessfully add a shipment order to the system
    When the user with username "<orderPlacer>" attempts to add a new shipment order to the system with id "<id>", date "<placedOnDate>", description "<description>", quantity "<quantity>", and container number "<containerNumber>" (f12)
    Then the number of orders in the system shall be "2" (f12)
    Then the following orders shall exist in the system (f12)
      | id | orderPlacer | placedOnDate | description                   | containerNumber | quantity |
      |  1 | manager     |   2023-07-20 | This is a dummy description 1 |               2 |       12 |
      |  2 | smith       |   2023-07-10 | This is a dummy description 2 |               1 |       10 |
    Then the system shall raise the error "<error>" (f12)

    Examples: 
      | id | orderPlacer | placedOnDate | description                   | containerNumber | quantity | error                                                            |
      |  2 | smith       |   2023-09-23 | This is a dummy description 1 |               2 |        2 | Order id already exists                                          |
      |  3 | manager     |   2023-09-23 | This is a dummy description 1 |               3 |        3 | The container does not exist                                     |
      |  3 | none        |   2023-09-23 | This is a dummy description 1 |               1 |       10 | The order placer does not exist                                  |
      |  3 | smith       |   2023-09-23 |                               |               1 |       10 | Order description cannot be empty                                |
      |  3 | smith       |   2023-09-23 | This is a dummy description 1 |               1 |        0 | Order quantity must be larger than 0 when container is specified |
      |  3 | smith       |   2023-09-23 | This is a dummy description 1 |                 |       10 | Order quantity must 0 when container is not specified            |

  Scenario Outline: Successfully update a shipment order
    When the manager attempts to update the shipment order with id "<id>" to order placer "<newOrderPlacer>", date "<newPlacedOnDate>", description "<newDescription>", quantity "<newQuantity>", and container number "<newContainerNumber>" (f12)
    Then the number of orders in the system shall be "2" (f12)
    Then the order raised by "<newOrderPlacer>" and with id "<id>", date "<newPlacedOnDate>", description "<newDescription>", quantity "<newQuantity>", and container number "<newContainerNumber>" shall exist in the system (f12)

    Examples: 
      | id | newOrderPlacer | newPlacedOnDate | newDescription                | newContainerNumber | newQuantity |
      |  1 | smith          |      2023-08-20 | bed needs to be fixed         |                  1 |          10 |
      |  2 | manager        |      2023-08-10 | notfound tv needs to be fixed |                  2 |           9 |

  Scenario Outline: Successfully remove an container from a shipment order
    When the manager attempts to update the shipment order with id "<id>" to order placer "<newOrderPlacer>", date "<newPlacedOnDate>", description "<newDescription>", and quantity "<newQuantity>" but no container number (f12)
    Then the number of orders in the system shall be "2" (f12)
    Then the order raised by "<newOrderPlacer>" and with id "<id>", date "<newPlacedOnDate>", description "<newDescription>", and quantity "<newQuantity>" but no container shall exist in the system (f12)

    Examples: 
      | id | newOrderPlacer | newPlacedOnDate | newDescription | newQuantity |
      |  1 | smith          |      2023-08-20 | it is noisy    |           0 |
      |  2 | manager        |      2023-08-10 | it smells      |           0 |

  Scenario Outline: Unsuccessfully update a shipment order with invalid information
    When the manager attempts to update the shipment order with id "<id>" to order placer "<newOrderPlacer>", date "<newPlacedOnDate>", description "<newDescription>", quantity "<newQuantity>", and container number "<newContainerNumber>" (f12)
    Then the number of orders in the system shall be "2" (f12)
    Then the following orders shall exist in the system (f12)
      | id | orderPlacer | placedOnDate | description                   | containerNumber | quantity |
      |  1 | manager     |   2023-07-20 | This is a dummy description 1 |               2 |       12 |
      |  2 | smith       |   2023-07-10 | This is a dummy description 2 |               1 |       10 |
    Then the system shall raise the error "<error>" (f12)

    Examples: 
      | id | newOrderPlacer | newPlacedOnDate | newDescription                | newContainerNumber | newQuantity | error                                                            |
      |  1 | manager        |      2023-09-23 | This is a dummy description 1 |                  3 |          10 | The container does not exist                                     |
      |  1 | none           |      2023-09-23 | This is a dummy description 1 |                  1 |          10 | The order placer does not exist                                  |
      |  1 | smith          |      2023-09-23 |                               |                  1 |          10 | Order description cannot be empty                                |
      |  1 | smith          |      2023-09-23 | This is a dummy description 1 |                  1 |           0 | Order quantity must be larger than 0 when container is specified |
      |  1 | smith          |      2023-09-23 | This is a dummy description 1 |                    |          10 | Order quantity must 0 when container is not specified            |
