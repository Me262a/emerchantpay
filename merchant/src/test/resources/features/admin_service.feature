Feature: Importing Admins from CSV

  Scenario: Successful import of admins from CSV
    Given OAuthServer is working properly
    When I import admins from CSV
    Then the import is successful and returns a list of 2 admins

  Scenario: Exception occurs during import
    Given OAuthServer throws an exception
    When I import admins from CSV
    Then the import fails and an error is returned