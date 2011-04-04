Feature: Generators for Resty

  Scenario: Create a rails application and adding 'resty-generators' gem will provide the resty generators
    Given I create new rails application with template "simple.template"
    Then the output should contain "resty:controller" and "resty:model" and "resty:scaffold" and "resty:setup"
    Then the output should contain "GWT_MODULE_NAME"
    Then the output should contain "src/main/java/com/example/simple.gwt.xml" and " Mavenfile" and "src/main/java/com/example/client/Simple.java" and "config/initializers/resty.rb" and "public/simple.html"
    Then the output should contain "src/main/java/com/example/client/models/User.java" and "src/main/java/com/example/client/controllers/UsersController.java"
    Then the output should contain "Compilation succeeded"
    
