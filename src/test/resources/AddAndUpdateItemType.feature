Feature: Add/Update Item Type (f6)
As the manager, I want to add and update an item type in the system.

  Background: 
    Given the following item types exist in the system (f6)
      | name        | expectedLifeSpanInDays |
      | NotFount TV |                   1800 |
      | Fake Laptop |                    700 |
      | Bed         |                   5000 |

  Scenario Outline: Successfully add an item type
    When the manager attempts to add a new item type to the system with name "<name>" and expected life span of "<expectedLifeSpanInDays>" days (f6)
    Then the number of item types in the system shall be "4" (f6)
    Then the item type with name "<name>" and expected life span of "<expectedLifeSpanInDays>" days shall exist in the system (f6)

    Examples: 
      | name      | expectedLifeSpanInDays |
      | Fridge    |                   5000 |
      | Microwave |                   2000 |

  Scenario Outline: Unsuccessfully add an item type with invalid information
    When the manager attempts to add a new item type to the system with name "<name>" and expected life span of "<expectedLifeSpanInDays>" days (f6)
    Then the number of item types in the system shall be "3" (f6)
    Then the following item types shall exist in the system (f6)
      | name        | expectedLifeSpanInDays |
      | NotFount TV |                   1800 |
      | Fake Laptop |                    700 |
      | Bed         |                   5000 |
    Then the system shall raise the error "<error>" (f6)

    Examples: 
      | name        | expectedLifeSpanInDays | error                                              |
      | television  |                      0 | The expected life span must be greater than 0 days |
      | television  |                   -180 | The expected life span must be greater than 0 days |
      |             |                    180 | The name must not be empty                         |
      | NotFount TV |                     60 | The item type already exists                       |
      | Bed         |                     96 | The item type already exists                       |

  Scenario Outline: Successfully update all information for an item type
    When the manager attempts to update an item type in the system with name "<oldName>" to have name "<newName>" and expected life span of "<newExpectedLifeSpanInDays>" days (f6)
    Then the number of item types in the system shall be "3" (f6)
    Then the item type with name "<newName>" and expected life span of "<newExpectedLifeSpanInDays>" days shall exist in the system (f6)
    Then the item type with name "<oldName>" and expected life span of "<oldExpectedLifeSpanInDays>" days shall not exist in the system (f6)

    Examples: 
      | oldName     | oldExpectedLifeSpanInDays | newName     | newExpectedLifeSpanInDays |
      | NotFount TV |                      1800 | NotFount TV |                      1900 |
      | Fake Laptop |                       700 | closet      |                      2500 |
      | Bed         |                      5000 | pot         |                       600 |

  Scenario Outline: Unsuccessfully update an item type with invalid information
    When the manager attempts to update an item type in the system with name "NotFount TV" to have name "<newName>" and expected life span of "<newExpectedLifeSpanInDays>" days (f6)
    Then the number of item types in the system shall be "3" (f6)
    Then the following item types shall exist in the system (f6)
      | name        | expectedLifeSpanInDays |
      | NotFount TV |                   1800 |
      | Fake Laptop |                    700 |
      | Bed         |                   5000 |
    Then the system shall raise the error "<error>" (f6)

    Examples: 
      | newName           | newExpectedLifeSpanInDays | error                                              |
      | table NotFount TV |                         0 | The expected life span must be greater than 0 days |
      | table NotFount TV |                       -30 | The expected life span must be greater than 0 days |
      |                   |                        30 | The name must not be empty                         |
      | Fake Laptop       |                       240 | The item type already exists                       |

  Scenario Outline: Unsuccessfully update an item type that does not exist in the system
    When the manager attempts to update an item type in the system with name "table NotFount TV" to have name "<newName>" and expected life span of "<newExpectedLifeSpanInDays>" days (f6)
    Then the number of item types in the system shall be "3" (f6)
    Then the following item types shall exist in the system (f6)
      | name        | expectedLifeSpanInDays |
      | NotFount TV |                   1800 |
      | Fake Laptop |                    700 |
      | Bed         |                   5000 |
    Then the system shall raise the error "The item type does not exist" (f6)

    Examples: 
      | newName | newExpectedLifeSpanInDays |
      | desk    |                      6000 |
