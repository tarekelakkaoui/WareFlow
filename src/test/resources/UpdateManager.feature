Feature: Update Manager Password (f1)
As a manager, I wish to update my password

  Background: 
    Given the following manager exists in the system (f1)
      | username | password |
      | manager  | manager  |

  Scenario Outline: A manager updates their password successfully
    When a manager with "<username>" attempts to update their password to "<newPassword>" (f1)
    Then the manager account information will be updated and is now "<username>" and "<newPassword>" (f1)
    Then the number of managers in the system shall be "1" (f1)

    Examples: 
      | username | newPassword |
      | manager  | P!p1        |
      | manager  | p#2P        |
      | manager  | $aA3        |

  Scenario Outline: A manager updates their password unsuccessfully
    When a manager with "<username>" attempts to update their password to "<newPassword>" (f1)
    Then the following "<error>" shall be raised (f1)
    Then the manager account information will not be updated and will keep "<username>" and "manager" (f1)
    Then the number of managers in the system shall be "1" (f1)

    Examples: 
      | username | newPassword | error                                          |
      | manager  | P!p         | Password must be at least four characters long |
      | manager  | p2P2        | Password must contain one character out of !#$ |
      | manager  |             | Password cannot be empty                       |
      | manager  | !2P2        | Password must contain one lower-case character |
      | manager  | !2p2        | Password must contain one upper-case character |
