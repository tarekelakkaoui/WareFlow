Feature: Add/Update Item Container (f8)
As the manager, I want to add and update an item container in the system.

  Background: 
    Given the following item types exist in the system (f8)
      | name        | expectedLifeSpanInDays |
      | NotFound TV |                   1800 |
      | Bed         |                   5000 |
    Given the following containers exist in the system (f8)
      | containerNumber | type        | addedOnDate | areaNumber | slotNumber |
      |               1 | NotFound TV |  2022-03-20 |          9 |         23 |
      |               2 | Bed         |  2010-01-30 |         10 |         35 |

  Scenario Outline: Successfully add an item container
    When the manager attempts to add a new item container to the system with container number "<containerNumber>", type "<type>", added date "<addedOnDate>", area number "<areaNumber>", and slot number "<slotNumber>" (f8)
    Then the number of containers in the system shall be "3" (f8)
    Then the item container "<type>" with container number "<containerNumber>", added date "<addedOnDate>", area number "<areaNumber>", and slot number "<slotNumber>" shall exist in the system (f8)

    Examples: 
      | containerNumber | type        | addedOnDate | areaNumber | slotNumber |
      |               3 | Bed         |  2020-06-23 |          2 |         32 |
      |               3 | NotFound TV |  2012-01-01 |          3 |         78 |
      |               3 | NotFound TV |  2012-01-01 |          0 |          4 |

  Scenario Outline: Unsuccessfully add an item container with a type that does not exist
    When the manager attempts to add a new item container to the system with container number "<containerNumber>", type "<type>", added date "<addedOnDate>", area number "<areaNumber>", and slot number "<slotNumber>" (f8)
    Then the number of containers in the system shall be "2" (f8)
    Then the following containers shall exist in the system (f8)
      | containerNumber | type        | addedOnDate | areaNumber | slotNumber |
      |               1 | NotFound TV |  2022-03-20 |          9 |         23 |
      |               2 | Bed         |  2010-01-30 |         10 |         35 |
    Then the error "<error>" shall be raised (f8)

    Examples: 
      | containerNumber | type       | addedOnDate | areaNumber | slotNumber | error                        |
      |               3 | television |  2012-01-01 |          5 |         43 | The item type does not exist |

  Scenario Outline: Unsuccessfully add an item container with invalid information
    When the manager attempts to add a new item container to the system with container number "<containerNumber>", type "<type>", added date "<addedOnDate>", area number "<areaNumber>", and slot number "<slotNumber>" (f8)
    Then the number of containers in the system shall be "2" (f8)
    Then the following containers shall exist in the system (f8)
      | containerNumber | type        | addedOnDate | areaNumber | slotNumber |
      |               1 | NotFound TV |  2022-03-20 |          9 |         23 |
      |               2 | Bed         |  2010-01-30 |         10 |         35 |
    Then the error "<error>" shall be raised (f8)

    Examples: 
      | containerNumber | type        | addedOnDate | areaNumber | slotNumber | error                                         |
      |               0 | NotFound TV |  2012-01-01 |          1 |         13 | The container number shall not be less than 1 |
      |               3 | NotFound TV |  2012-01-01 |         -1 |         13 | The area number shall not be less than 0      |
      |               3 | Bed         |  2012-01-01 |          7 |         -2 | The slot number shall not be less than 0      |

  Scenario Outline: Successfully update all information for an item container
    When the manager attempts to update container number "<containerNumber>" in the system with type "<newType>", addedOnDate "<newAddedOnDate>", areaNumber "<newAreaNumber>", and slotNumber "<newSlotNumber>" (f8)
    Then the number of containers in the system shall be "2" (f8)
    Then the item container "<newType>" with container number "<containerNumber>", added date "<newAddedOnDate>", area number "<newAreaNumber>", and slot number "<newSlotNumber>" shall exist in the system (f8)

    Examples: 
      | containerNumber | newType     | newAddedOnDate | newAreaNumber | newSlotNumber |
      |               1 | Bed         |     2022-03-21 |            12 |            10 |
      |               2 | NotFound TV |     2010-01-29 |             9 |             3 |

  Scenario Outline: Unsuccessfully update an item container with a type that does not exist
    When the manager attempts to update container number "<containerNumber>" in the system with type "<newType>", addedOnDate "<newAddedOnDate>", areaNumber "<newAreaNumber>", and slotNumber "<newSlotNumber>" (f8)
    Then the number of containers in the system shall be "2" (f8)
    Then the following containers shall exist in the system (f8)
      | containerNumber | type        | addedOnDate | areaNumber | slotNumber |
      |               1 | NotFound TV |  2022-03-20 |          9 |         23 |
      |               2 | Bed         |  2010-01-30 |         10 |         35 |
    Then the error "<error>" shall be raised (f8)

    Examples: 
      | containerNumber | newType | newAddedOnDate | newAreaNumber | newSlotNumber | error                        |
      |               1 | couch   |     1999-04-20 |            12 |            10 | The item type does not exist |

  Scenario Outline: Unsuccessfully update an item container with invalid information
    When the manager attempts to update container number "<containerNumber>" in the system with type "<newType>", addedOnDate "<newAddedOnDate>", areaNumber "<newAreaNumber>", and slotNumber "<newSlotNumber>" (f8)
    Then the number of containers in the system shall be "2" (f8)
    Then the following containers shall exist in the system (f8)
      | containerNumber | type        | addedOnDate | areaNumber | slotNumber |
      |               1 | NotFound TV |  2022-03-20 |          9 |         23 |
      |               2 | Bed         |  2010-01-30 |         10 |         35 |
    Then the error "<error>" shall be raised (f8)

    Examples: 
      | containerNumber | newType     | newAddedOnDate | newAreaNumber | newSlotNumber | error                                    |
      |               2 | NotFound TV |     2012-01-01 |            -1 |            13 | The area number shall not be less than 0 |
      |               1 | Bed         |     2012-01-01 |             7 |            -2 | The slot number shall not be less than 0 |
