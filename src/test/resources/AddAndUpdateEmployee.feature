Feature: Add/Update Employee (f2)
As an employee, I wish to register and update an employee account in the system

  Background: 
    Given the following employees exist in the system (f2)
      | username | password | name | phoneNumber   |
      | jeff99   | pass1    | Jeff | (555)555-5555 |
      | john     | pass2    | John | (444)444-4444 |
    Given the following manager exists in the system (f2)
      | username | password |
      | manager  | manager  |

  Scenario Outline: An employee registers successfully
    When a new employee attempts to register with "<username>", "<password>", "<name>", and "<phoneNumber>" (f2)
    Then a new employee account shall exist with "<username>", "<password>", "<name>", and "<phoneNumber>" (f2)
    Then the number of employees in the system shall be "3" (f2)

    Examples: 
      | username | password | name | phoneNumber   |
      | lisa     | pass4    | Lisa | (888)888-8888 |
      | liam     | pass5    | Liam | (777)777-7777 |
      | owen     | pass10   |      | (888)888-5555 |
      | noah     | pass11   | Noah |               |

  Scenario Outline: An employee registers unsuccessfully
    When a new employee attempts to register with "<username>", "<password>", "<name>", and "<phoneNumber>" (f2)
    Then the following "<error>" shall be raised (f2)
    Then the number of employees in the system shall be "2" (f2)
    Then the following employees shall exist in the system (f2)
      | username | password | name | phoneNumber   |
      | jeff99   | pass1    | Jeff | (555)555-5555 |
      | john     | pass2    | John | (444)444-4444 |

    Examples: 
      | username    | password | name | phoneNumber   | error                                          |
      | manager     | pass1    | Paul | (111)111-1111 | Username cannot be manager                     |
      | jeff99      | pass2    | Jeff | (111)777-7777 | Username already linked to an employee account |
      | bart ap com | pass3    | Bart | (444)666-6666 | Invalid username                               |
      | kyle@ap.    | pass5    | Kyle | (666)777-6666 | Invalid username                               |
      | greg.ap@com | pass6    | Greg | (777)888-7777 | Invalid username                               |
      | karl@.com   | pass8    | Karl | (111)777-6661 | Invalid username                               |
      |             | pass9    | Vino | (777)888-5555 | Username cannot be empty                       |
      | luke        |          | Luke | (999)888-5555 | Password cannot be empty                       |

  Scenario Outline: An employee updates their info successfully
    When the employee with "<username>" attempts to update their account information to "<newPassword>", "<newName>", and "<newPhoneNumber>" (f2)
    Then their employee account information will be updated and is now "<username>", "<newPassword>", "<newName>", and "<newPhoneNumber>" (f2)
    Then the number of employees in the system shall be "2" (f2)

    Examples: 
      | username | newPassword | newName | newPhoneNumber |
      | jeff99   | pass5       | Jake    | (111)111-1111  |
      | john     | pass6       | Johnny  | (111)777-7777  |
      | john     | pass2       |         | (444)444-7777  |
      | john     | pass2       | Jon     |                |

  Scenario Outline: An employee updates their info unsuccessfully
    When the employee with "<username>" attempts to update their account information to "<newPassword>", "<newName>", and "<newPhoneNumber>" (f2)
    Then the following "<error>" shall be raised (f2)
    Then the number of employees in the system shall be "2" (f2)
    Then the following employees shall exist in the system (f2)
      | username | password | name | phoneNumber   |
      | jeff99   | pass1    | Jeff | (555)555-5555 |
      | john     | pass2    | John | (444)444-4444 |

    Examples: 
      | username | password | name | phoneNumber   | newPassword | newName | newPhoneNumber | error                    |
      | jeff99   | pass1    | Jeff | (555)555-5555 |             | Jeff    | (555)666-5555  | Password cannot be empty |
