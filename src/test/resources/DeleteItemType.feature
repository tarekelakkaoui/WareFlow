Feature: Delete Item Type (f7)
As the manager, I want to delete an existing item type in the system so that it is no longer available.

  Background: 
    Given the following item types exist in the system (f7)
      | name        | expectedLifeSpanInDays |
      | NotFound TV |                   1800 |
      | Pillow      |                    700 |
      | Bed         |                   5000 |

  Scenario: Successfully delete an item type
    When the manager attempts to delete an item type in the system with name "NotFound TV" (f7)
    Then the number of item types in the system shall be "2" (f7)
    Then the following item types shall exist in the system (f7)
      | name   | expectedLifeSpanInDays |
      | Pillow |                    700 |
      | Bed    |                   5000 |

  Scenario Outline: Successfully delete an item type that does not exist in the system
    When the manager attempts to delete an item type in the system with name "<name>" (f7)
    Then the number of item types in the system shall be "3" (f7)
    Then the following item types shall exist in the system (f7)
      | name        | expectedLifeSpanInDays |
      | NotFound TV |                   1800 |
      | Pillow      |                    700 |
      | Bed         |                   5000 |

    Examples: 
      | name     |
      | Found TV |
      | desk     |
