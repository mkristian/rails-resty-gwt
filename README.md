resty rails generators
==========

these generators add GWT support to an existing [rails](http://rubyonrails.org) application. JSON is used for the communication between rails and GWT.

on GWT side [restygwt](http://restygwt.fusesource.org/documentation/restygwt-user-guide.html) is used for the un/marshalling of the JSON payload and rails needed some tweaks to make things compatible - the 'resty-generators' gem provides these as initializer.

the development environment tries to follow the rails way as close as possible - judge yourself.

there are two approaches and they are a matter of taste. the jruby way or the maven way. but in any case you need java installed for either one. and to keep things simple I just discribe the jruby way. if you are interested in the maven commands they are printed by ruby-maven when executing it.

the glue between rails and GWT is the [ruby DSL for maven](https://github.com/sonatype/polyglot-maven/blob/master/pmaven-jruby/README.md). (thanx to [Sonatype](http://sonatype.org) for sponsoring the work on that DSL)

# get started #

requirement for this is ruby+rubygems or jruby and then install (unless you have it already)

`gem install ruby-maven`

## new rails application ##

with this you can create a new rails application like this (rails-3.1 is pending but might already work). to setup resty you need to choose a direcotry and a java package name where to locate the GWT code (_com.example_)

`gwt new my_app com.example`

go into the created application (or any existing rails app)

`cd myapp`

with this you have already a GWT application with EntryPoint and could be started but does not do much. the class layout look as such:

>     src/main/java/com/example
      ├── client
      │   ├── ActivityPlaceActivityMapper.java
      │   ├── ActivityPlace.java
      │   ├── managed
      │   │   ├── ActivityFactory.java
      │   │   ├── MyAppModule.java
      │   │   └── MyAppPlaceHistoryMapper.java
      │   └── MyAppEntryPoint.java
      └── MyApp.gwt.xml

which is a GIN based setup. the __managed__ classes will be modified by the scaffold generators. the other files are once generated and can be changed as needed. the __MyAppModule.java__ can be changed with care (only the __super.configure();__ and __new GinFactoryModuleBuilder()__should stay as it is). the EntryPoint of the application is __MyAppEntryPoint.java__.

## binstubs which use bundler and setup the classpath (if needed)

`rmvn bundle install`

will get all the gems and jars in place and setup binstubs as bundler would do without ruby-maven. when you set your environment **PATH** to

`export PATH=target/bin:$PATH`

then you have all the `rails`, `rake`, `rspec` commands installed by your gems with gems + jars in place.

## scaffold a resource ##

now you can scaffold a model

`rails generate scaffold user name:string`

this creates a rails like structure within the GWT _client_ package:

>     src/main/java/com/example/client
    ├── activities
    │   └── UserActivity.java
    ├── editors
    │   ├── UserEditor.java
    │   └── UserEditor.ui.xml
    ├── events
    │   ├── UserEventHandler.java
    │   └── UserEvent.java
    ├── models
    │   └── User.java
    ├── places
    │   ├── UserPlace.java
    │   └── UserPlaceTokenizer.java
    ├── restservices
    │   └── UsersRestService.java
    └── views
        ├── UserViewImpl.java
        ├── UserView.java
        └── UserView.ui.xml

before running the application you need to migrate the database so the new table is in place

`rake db:migrate`

## start the gwt development shell ##

and start the GWT development shell

`gwt run`

the GWT application uses following url pattern:

* `http://localhost:8888/MyApp.html?gwt.codesvr=127.0.0.1:9997#users` the collection (not implemented yet)
* `http://localhost:8888/MyApp.html?gwt.codesvr=127.0.0.1:9997#users/new` to create a new user
* `http://localhost:8888/MyApp.html?gwt.codesvr=127.0.0.1:9997#users/<id>` to view user with id <id>
* `http://localhost:8888/MyApp.html?gwt.codesvr=127.0.0.1:9997#users/<id>/edit` to edit user with id <id>

### webrick server ###

to run the application with default webrick you need first to compile the GWT part

`gwt compile`

the compiler will output the GWT app in __public/MyApp__ and then start the webrick.

`rails server`

the start url is [http://localhost:3000/MyApp.html](http://localhost:3000/MyApp.html).

such a setup also works with MRI and can be deploy on [heroku](http://heroku.com) ! no need for jruby for production unless you start using java on the ruby side of things, i.e. within the rails application.

*note* from rails-3.0.10 onward __rails new__ generates a jruby only Gemfile. with that you need to adjust your Gemfile so it will work for both MRI and JRuby.

what's next
----------

* error handling - i.e. validation errors (server side)
* selenium/cabybara tests
* hide buttons if the logged in user does not have the permissions to use it

some little things would be

* Restservices come as singleton when used the @Singleton annotation

## Note on Patches/Pull Requests

* Fork the project.
* Make your feature addition or bug fix.
* Add tests for it. This is important so I don't break it in a future version unintentionally.
* Commit, do not mess with version, or history. (if you want to have your own version, that is fine but bump version in a commit by itself I can ignore when I pull)
* Send me a pull request.

the maven ruby DSL for the _Mavenfile_
----------

the _Mavenfile_ allows you to (re)-configure parts of the maven pom. the pom will be "generated on the fly" when you run `rmvn`. but with proper maven you do it yourself - see above.

more about that DSL and the connection with gemspec files and _Gemfile_ can be found in [polyglot-maven (jruby)](https://github.com/sonatype/polyglot-maven/tree/master/pmaven-jruby) project.

shortcomings
---------

* ruby-maven might not support all possible configurations within the Gemfile and the maven DSL is not complete yet (from the maven point of view).
* and possible other things, but enjoy anyways ;-)
* binstub of `rspec` works with version 2.7.0.rc2 or later due the way it handled bundler before
