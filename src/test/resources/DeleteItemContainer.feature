Feature: Delete Item Containers (f9)
As the manager, I want to delete an item container in the system so that it is no longer available.

  Background: 
    Given the following item types exist in the system (f9)
      | name        | expectedLifeSpanInDays |
      | NotFound TV |                   1800 |
      | Bed         |                   5000 |
    Given the following containers exist in the system (f9)
      | containerNumber | type        | addedOnDate | areaNumber | slotNumber |
      |               1 | NotFound TV |  2022-03-20 |          9 |         23 |
      |               2 | Bed         |  2010-01-30 |         10 |         35 |
      |               3 | NotFound TV |  2007-12-27 |         99 |         99 |

  Scenario Outline: Successfully delete an container
    When the manager attempts to delete the container with number "2" (f9)
    Then the number of containers in the system shall be "2" (f9)
    Then the following containers shall exist in the system (f9)
      | containerNumber | type        | addedOnDate | areaNumber | slotNumber |
      |               1 | NotFound TV |  2022-03-20 |          9 |         23 |
      |               3 | NotFound TV |  2007-12-27 |         99 |         99 |

  Scenario Outline: Successfully delete an container that does not exist in the system
    When the manager attempts to delete the container with number "4" (f9)
    Then the number of containers in the system shall be "3" (f9)
    Then the following containers shall exist in the system (f9)
      | containerNumber | type        | addedOnDate | areaNumber | slotNumber |
      |               1 | NotFound TV |  2022-03-20 |          9 |         23 |
      |               2 | Bed         |  2010-01-30 |         10 |         35 |
      |               3 | NotFound TV |  2007-12-27 |         99 |         99 |
