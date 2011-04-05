resty rails generators
==========

these generators add GWT support to an existing rails application. for the communication between rails and GWT JSON is used.

on GWT side http://restygwt.fusesource.org/documentation/restygwt-user-guide.html is used for the un/marshalling of JSOn data and rails needed some monkey patches to make things compatible - this gem provides these patches.

the development flow tries to follow the rails way as close as possible - judge yourself.

there are two approaches and a matter of taste. the jruby way or the maven way.
but in any case you need java installed either for jruby or maven.

the jruby way
---------

requirement for this is jruby and then install (unless you have it already)

`jruby -S gem install ruby-maven`

with this you can create a new rails application like this

`rmvn rails new my_app`

go into the created application (or any existing rails app)

`cd my_app`

add into the __Gemfile__

`gem 'resty-generators'`

to setup resty you need to choose a java package name where to locate the GWT code (_com.example_)

`rmvn rails generate resty:setup com.example`

this is already a GWT application with EntryPoint and could be started but does not do much.

now you can scaffold a model (currently two steps are required for this)

`rmvn rails generate scaffold user name:string --migrate -t test_unit`

`rmvn rails generate resty:scaffold user name:string`

the later creates a rails like structure within the GWT java package:

>     src/main/java/com/example/client/
    |-- controllers
    |   `-- UsersController.java
    |-- models
    |   `-- User.java
    `-- Simple.java


finally you can run the application ut it does only print out 'hello world'. the resty part of the scaffold just creates the rails side code and the models on the GWT side - **NO VIEWS**

before running the application you need to migrate the database

`rmvn rake db:migrate`

and run it with the GWT development shell

`rmvn gwt:run`

to run the application with default webrick you need first to compile the GWT part

`rmvn gwt:compile`

the compiler output you will find in __public/my_app__ and then start the webrick. the start url is http://localhost:3000/my_app.html

`rmvn rails server`

the maven way
----------

maven3 is required to run these maven plugins below. the steps are the same as above but the commands are called slightly different:

`mvn de.saumya.mojo:rails3-maven-plugin:0.26.0:new -Dargs=my_app`

`mvn rails3:generate -Dargs="resty:setup com.example"`

`mvn rails3:generate -Dargs="scaffold user name:string --migrate -t test_unit"`

`mvn rails3:generate -Dargs="resty:scaffold user name:string"`

`mvn rails3:rake -Dargs="db:migrate"`

`mvn gwt:run`

`mvn gwt:compile`

`mvn rails3:server`

the moment you change something in the _Gemfile_ or _Gemfile.lock_ or _Mavenfile_ you need recreate the pom.xml with

`mvn rails3:pom -Dpom.force`

the maven ruby DSL for the _Mavenfile_
----------

the _Mavenfile_ allows you to (re)-configure parts of the maven pom. the pom will be "generated on the fly" when you run `rmvn`. but with proper maven you do it yourself - see above.

more about that DSL and the connection with gemspec files and _Gemfile_ can be found in https://github.com/sonatype/polyglot-maven/tree/master/pmaven-jruby

shortcomings
---------

the rails generators lost their defaults :-(, like you need to add _--migrate_ or _-t test\_unit_ to your scaffold command.

ruby-maven might not support all possible configurations within the Gemfile, dito the maven DSL is not complete yet (from the maven point of view).
