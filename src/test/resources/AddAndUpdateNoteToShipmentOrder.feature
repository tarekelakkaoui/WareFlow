Feature: Add/Update Note (f10)
As staff, I want to add and update a shipment order to a order in the system.

  Background: 
    Given the following employees exist in the system (f10)
      | username | password | name  | phoneNumber   |
      | jeff     | pass1    | Jeff  | (555)555-5555 |
      | smith    | pass2    | Smith | (555)555-5555 |
    Given the following manager exists in the system (f10)
      | username | password |
      | manager  | manager  |
    Given the following asset types exist in the system (f10)
      | name        | expectedLifeSpanInDays |
      | notfount tv |                   1800 |
      | bed         |                   5000 |
    Given the following assets exist in the system (f10)
      | containerNumber | type        | addedOnDate | areaNumber | slotNumber |
      |               1 | notfount tv |  2022-03-20 |          9 |         23 |
      |               2 | bed         |  2010-01-30 |         10 |         35 |
    Given the following orders exist in the system (f10)
      | id | orderPlacer | placedOnDate | description                   | containerNumber | quantity |
      |  1 | manager     |   2023-07-20 | This is a dummy description 1 |               2 |        8 |
      |  2 | smith       |   2023-07-10 | This is a dummy description 2 |               1 |       10 |
    Given the following notes exist in the system (f10)
      | noteTaker | orderId | date       | description                              |
      | jeff      |       2 | 2023-09-01 | This is a dummy note 1 for a order       |
      | smith     |       1 | 2023-09-10 | This is another dummy note 2 for a order |

  Scenario Outline: Successfully add a note to a shipment order to the system by an employee
    When staff with username "<noteTaker>" attempts to add a new note with date "<date>" and description "<description>" to an existing shipment order "<orderId>" (f10)
    Then the number of notes in the system shall be "3" (f10)
    Then the number of notes for order "<orderId>" in the system shall be "<numberOfNotes>" (f10)
    Then the note number "<noteIndex>" for order <orderId> with noteTaker "<noteTaker>", date "<date>", and description "<description>" shall exist in the system (f10)

    Examples: 
      | orderId | noteTaker | date       | description                   | noteIndex | numberOfNotes |
      |       1 | jeff      | 2023-09-23 | This is a dummy description 1 |         1 |             2 |
      |       1 | manager   | 2023-10-05 | This is a dummy description 2 |         1 |             2 |

  Scenario Outline: Unsuccessfully add a shipment order to the system.
    When staff with username "<noteTaker>" attempts to add a new note with date "<date>" and description "<description>" to an existing shipment order "<orderId>" (f10)
    Then the number of notes in the system shall be "2" (f10)
    Then the following notes shall exist in the system (f10)
      | noteTaker | orderId | date       | description                              |
      | jeff      |       2 | 2023-09-01 | This is a dummy note 1 for a order       |
      | smith     |       1 | 2023-09-10 | This is another dummy note 2 for a order |
    Then the system shall raise the error "<error>" (f10)

    Examples: 
      | orderId | noteTaker | date       | description                   | error                             |
      |       1 | abdul     | 2023-09-23 | This is a dummy description 1 | Staff does not exist              |
      |       3 | manager   | 2023-10-05 | This is a dummy description 2 | Order does not exist              |
      |       1 | manager   | 2023-10-05 |                               | Order description cannot be empty |

  Scenario Outline: Successfully update a shipment order
    When the manger attempts to update note number "<noteIndex>" for shipment order "<orderId>" with note taker "<newNoteTaker>", date "<newDate>", and description "<newDescription>" (f10)
    Then the number of notes in the system shall be "2" (f10)
    Then the number of notes for order "<orderId>" in the system shall be "<numberOfNotes>" (f10)
    Then the note number "<noteIndex>" for order <orderId> with noteTaker "<newNoteTaker>", date "<newDate>", and description "<newDescription>" shall exist in the system (f10)

    Examples: 
      | orderId | newNoteTaker | newDate    | newDescription                | noteIndex | numberOfNotes |
      |       1 | jeff         | 2023-09-23 | This is a dummy description 1 |         0 |             1 |
      |       1 | manager      | 2023-10-05 | This is a dummy description 2 |         0 |             1 |

  Scenario Outline: Unsuccessfully update a note with invalid information
    When the manger attempts to update note number "<noteIndex>" for shipment order "<orderId>" with note taker "<newNoteTaker>", date "<newDate>", and description "<newDescription>" (f10)
    Then the number of notes in the system shall be "2" (f10)
    Then the following notes shall exist in the system (f10)
      | noteTaker | orderId | date       | description                              |
      | jeff      |       2 | 2023-09-01 | This is a dummy note 1 for a order       |
      | smith     |       1 | 2023-09-10 | This is another dummy note 2 for a order |
    Then the system shall raise the error "<error>" (f10)

    Examples: 
      | orderId | newNoteTaker | newDate    | newDescription                | noteIndex | error                             |
      |       1 | abdul        | 2023-09-23 | This is a dummy description 1 |         0 | Staff does not exist              |
      |       3 | manager      | 2023-10-05 | This is a dummy description 2 |         0 | Order does not exist              |
      |       1 | manager      | 2023-10-05 | This is a dummy description 2 |         1 | Note does not exist               |
      |       1 | manager      | 2023-10-05 |                               |         0 | Order description cannot be empty |
