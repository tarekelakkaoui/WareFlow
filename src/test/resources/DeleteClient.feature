Feature: Delete Client (f5)
As an client, I wish to delete my client account

  Background: 
    Given the following clients exist in the system (f5)
      | username | password | name | phoneNumber   | address      |
      | jeff     | pass1    | Jeff | (555)555-5555 | 1234 Main St |
      | john     | pass2    | John | (444)444-4444 | 5678 Main St |
    Given the following manager exists in the system (f5)
      | username | password |
      | manager  | manager  |

  Scenario Outline: Successfully deleting a client
    When the client attempts to delete their own account linked to the "<username>" (f5)
    Then the client account linked to "<username>" shall not exist in the system (f5)
    Then the number of clients in the system shall be "<numberOfClients>" (f5)

    Examples: 
      | username | numberOfClients |
      | jeff     |               1 |
      | john     |               1 |
      | kyle     |               2 |
      | paul     |               2 |

  Scenario: Successfully deleting a client that does not exist but manager exists
    When the client attempts to delete their own account linked to the "manager" (f5)
    Then the client account linked to "manager" shall not exist in the system (f5)
    Then the manager account linked to "manager" shall exist in the system (f5)
    Then the number of clients in the system shall be "2" (f5)
