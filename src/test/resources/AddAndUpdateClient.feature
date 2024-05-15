Feature: Add/Update Client (f3)
As a client, I wish to register and update a client account in the system

  Background: 
    Given the following clients exist in the system (f3)
      | username | password | name | phoneNumber   | address                          |
      | jeff     | pass1    | Jeff | (555)555-5555 |   555 Fictional Lane, Storyville |
      | john     | pass2    | John | (444)444-4444 | 777 Mirage Avenue, Illusion City |
    Given the following manager exists in the system (f3)
      | username | password |
      | manager  | manager  |

  Scenario Outline: A client registers successfully
    When a new client attempts to register with "<username>", "<password>", "<name>", "<phoneNumber>", and "<address>" (f3)
    Then a new client account shall exist with "<username>", "<password>", "<name>", "<phoneNumber>", and "<address>" (f3)
    Then the number of clients in the system shall be "3" (f3)

    Examples: 
      | username | password | name | phoneNumber   | address                              |
      | lisa     | pass4    | Lisa | (888)888-8888 |            123 Fake Street, Faketown |
      | liam     | pass5    | Liam | (777)777-7777 | 404 Not Found Avenue, Imaginary City |
      | owen     | pass10   |      | (888)888-5555 |                                      |
      | noah     | pass11   | Noah |               |                                      |

  Scenario Outline: A client registers unsuccessfully
    When a new client attempts to register with "<username>", "<password>", "<name>", "<phoneNumber>", and "<address>" (f3)
    Then the following "<error>" shall be raised (f3)
    Then the number of clients in the system shall be "2" (f3)
    Then the following clients shall exist in the system (f3)
      | username | password | name | phoneNumber   | address                          |
      | jeff     | pass1    | Jeff | (555)555-5555 |   555 Fictional Lane, Storyville |
      | john     | pass2    | John | (444)444-4444 | 777 Mirage Avenue, Illusion City |

    Examples: 
      | username        | password | name | phoneNumber   | address | error                                       |
      | manager         | pass1    | Paul | (111)111-1111 |         | Username cannot be manager                  |
      | jeff            | pass2    | Jeff | (111)777-7777 |         | Username already linked to a client account |
      | bart @ ap.com   | pass3    | Bart | (444)666-6666 |         | Invalid username                            |
      | dony@gmail@.com | pass4    | Dony | (777)555-7777 |         | Invalid username                            |
      | kyle@gmail      | pass5    | Kyle | (666)777-6666 |         | Invalid username                            |
      | greg.ap@com     | pass6    | Greg | (777)888-7777 |         | Invalid username                            |
      | karl@.com       | pass8    | Karl | (111)777-6661 |         | Invalid username                            |
      |                 | pass9    | Vino | (777)888-5555 |         | Username cannot be empty                    |
      | luke            |          | Luke | (999)888-5555 |         | Password cannot be empty                    |

  Scenario Outline: A client updates their info successfully
    When the client with "<username>" attempts to update their account information to "<newPassword>", "<newName>", "<newPhoneNumber>", and "<newAddress>" (f3)
    Then their client account information will be updated and is now "<username>", "<newPassword>", "<newName>", "<newPhoneNumber>", and "<newAddress>" (f3)
    Then the number of clients in the system shall be "2" (f3)

    Examples: 
      | username | newPassword | newName | newPhoneNumber | newArress                        |
      | jeff     | pass5       | Jake    | (111)111-1111  |                                  |
      | john     | pass6       | Johnny  | (111)777-7777  | 777 Mirage Avenue, Illusion City |
      | john     | pass2       |         | (444)444-7777  |   555 Fictional Lane, Storyville |
      | john     | pass2       | Jon     |                |                                  |

  Scenario Outline: A client updates their info unsuccessfully
    When the client with "<username>" attempts to update their account information to "<newPassword>", "<newName>", "<newPhoneNumber>", and "<newAddress>" (f3)
    Then the following "<error>" shall be raised (f3)
    Then the number of clients in the system shall be "2" (f3)
    Then the following clients shall exist in the system (f3)
      | username | password | name | phoneNumber   | address                          |
      | jeff     | pass1    | Jeff | (555)555-5555 |   555 Fictional Lane, Storyville |
      | john     | pass2    | John | (444)444-4444 | 777 Mirage Avenue, Illusion City |

    Examples: 
      | username | password | name | phoneNumber   | newPassword | newName | newPhoneNumber | newArress                      | error                    |
      | jeff     | pass1    | Jeff | (555)555-5555 |             | Jeff    | (555)666-5555  | 555 Fictional Lane, Storyville | Password cannot be empty |
