Feature: Delete Employee (f4)
As an employee, I wish to delete my employee account

  Background: 
    Given the following employees exist in the system (f4)
      | username | password | name | phoneNumber   |
      | jeff     | pass1    | Jeff | (555)555-5555 |
      | john     | pass2    | John | (444)444-4444 |
    Given the following manager exists in the system (f4)
      | username | password |
      | manager  | manager  |

  Scenario Outline: Successfully deleting a employee
    When the employee attempts to delete their own account linked to the "<username>" (f4)
    Then the employee account linked to "<username>" shall not exist in the system (f4)
    Then the number of employees in the system shall be "<numberOfEmployees>" (f4)

    Examples: 
      | username | numberOfEmployees |
      | jeff     |                 1 |
      | john     |                 1 |
      | kyle     |                 2 |
      | paul     |                 2 |

  Scenario: Successfully deleting an employee that does not exist but manager exists
    When the employee attempts to delete their own account linked to the "manager" (f4)
    Then the employee account linked to "manager" shall not exist in the system (f4)
    Then the manager account linked to "manager" shall exist in the system (f4)
    Then the number of employees in the system shall be "2" (f4)
