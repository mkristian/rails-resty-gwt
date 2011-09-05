Feature: Generators for Resty

  Scenario: Create a rails application and adding 'resty-generators' gem will provide the resty generators
    Given I create new rails application with template "simple.template"
    And I execute "rails generate"
    Then the output should contain "resty:controller" and "resty:model" and "resty:scaffold" and "resty:setup"

    Given me an existing rails application "simple"
    And I execute "rails generate resty:setup"
    Then the output should contain "GWT_MODULE_NAME"
    
    Given me an existing rails application "simple"
    And I execute "rails generate resty:setup com.example"
    Then the output should contain "src/main/java/com/example/Simple.gwt.xml" and "Mavenfile" and "src/main/java/com/example/client/SimpleEntryPoint.java" and "public/Simple.html" and "public/stylesheets/simple.css"
    
    Given me an existing rails application "simple"
    And I execute "compile gwt:compile"
    Then the output should contain "Compilation succeeded"
    
    Given me an existing rails application "simple"
    And I execute "rails generate scaffold user name:string"
    Then the output should contain "src/main/java/com/example/client/models/User.java" and "src/main/java/com/example/client/restservices/UsersRestService.java"

    Given me an existing rails application "simple"
    And I execute "compile gwt:compile"
    Then the output should contain "Compilation succeeded"
    
    Given me an existing rails application "simple"
    And I execute "rake db:migrate test"
    Then the output should contain "1 tests, 1 assertions, 0 failures, 0 errors" and "7 tests, 10 assertions, 0 failures, 0 errors"
    
    Given me an existing rails application "simple"
    And I execute "rails generate scaffold project name:string children:number cars:fixnum age:integer weight:float --skip-timestamps"
    And I execute "compile gwt:compile"
    Then the output should contain "Compilation succeeded"
    
    Given me an existing rails application "simple"
    And I execute "rails generate scaffold accounts name:string --timestamps --optimistic"
    And I execute "compile gwt:compile"
    Then the output should contain "Compilation succeeded"
    
    Given me an existing rails application "simple"
    And I execute "rails generate model cars name:string"
    And I execute "compile gwt:compile"
    Then the output should contain "Compilation succeeded"
    
    

  Scenario: Create a gwt rails application with session and menu
    Given I create new rails application with template "complete.template" and "complete" tests
    And I execute "rails generate resty:setup com.example --session --menu"
   Then the output should contain "src/main/java/com/example/client/SessionActivityPlaceActivityMapper.java" and "src/main/java/com/example/client/managed/CompleteMenuPanel.java" and "src/main/java/com/example/client/activities/LoginActivity.java"

    Given me an existing rails application "complete"
    And I execute "compile gwt:compile"
    Then the output should contain "Compilation succeeded"
    
    Given me an existing rails application "complete"
    And I execute "rails generate scaffold accounts name:string"
    And I execute "compile gwt:compile"
    Then the output should contain "Compilation succeeded"
    
    Given me an existing rails application "complete"
    And I execute "rake db:migrate test"
    Then the output should contain "1 tests, 1 assertions, 0 failures, 0 errors" and "7 tests, 10 assertions, 0 failures, 0 errors"
 
    
