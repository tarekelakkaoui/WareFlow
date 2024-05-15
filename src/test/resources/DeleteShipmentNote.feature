Feature: Delete Note (f11)
As a manager, I want to delete a shipment note for a order so that it is no longer available.

  Background: 
    Given the following employees exist in the system (f11)
      | username | password | name  | phoneNumber   |
      | jeff     | pass1    | Jeff  | (555)555-5555 |
      | smith    | pass2    | Smith | (555)555-5555 |
    Given the following manager exists in the system (f11)
      | username | password |
      | manager  | manager  |
    Given the following item types exist in the system (f11)
      | name        | expectedLifeSpanInDays |
      | notfound tv |                   1800 |
      | bed         |                   5000 |
    Given the following containers exist in the system (f11)
      | containerNumber | type        | addedOnDate | areaNumber | slotNumber |
      |               1 | notfound tv |  2022-03-20 |          9 |         23 |
      |               2 | bed         |  2010-01-30 |         10 |         35 |
    Given the following orders exist in the system (f11)
      | id | orderPlacer | placedOnDate | description                   | containerNumber | quantity |
      |  1 | manager     |   2023-07-20 | This is a dummy description 1 |               2 |        4 |
      |  2 | smith       |   2023-07-10 | This is a dummy description 2 |               1 |        9 |
    Given the following notes exist in the system (f11)
      | noteTaker | orderId | addedOnDate | description                              |
      | jeff      |       2 |  2023-09-01 | This is a dummy note 1 for a order       |
      | smith     |       1 |  2023-09-10 | This is another dummy note 2 for a order |
      | manager   |       1 |  2023-09-23 | This is a dummy description 1            |

  Scenario Outline: Successfully delete a note for a shipment order in the system
    When the manger attempts to delete note number "<noteIndex>" for shipment order "<orderId>" (f11)
    Then the number of notes in the system shall be "2" (f11)
    Then the number of notes for order "<orderId>" in the system shall be "<numberOfNotes>" (f11)
    Then the note number "<noteIndex>" for order <orderId> shall not exist in the system (f11)

    Examples: 
      | noteIndex | orderId | numberOfNotes |
      |         0 |       2 |             0 |
      |         0 |       1 |             1 |
      |         1 |       1 |             1 |

  Scenario Outline: Successfully delete a nonexisting note for a shipment order in the system
    When the manger attempts to delete note number "<noteIndex>" for shipment order "<orderId>" (f11)
    Then the number of notes in the system shall be "3" (f11)
    Then the number of notes for order "<orderId>" in the system shall be "<numberOfNotes>" (f11)
    Then the note number "<noteIndex>" for order <orderId> shall not exist in the system (f11)

    Examples: 
      | noteIndex | orderId | numberOfNotes |
      |         1 |       2 |             1 |
      |         2 |       1 |             2 |
      |         3 |       1 |             2 |

  Scenario Outline: Successfully delete a nonexisting note for a nonexisting shipment order in the system
    When the manger attempts to delete note number "<noteIndex>" for shipment order "<orderId>" (f11)
    Then the number of notes in the system shall be "3" (f11)

    Examples: 
      | noteIndex | orderId |
      |         0 |       3 |
