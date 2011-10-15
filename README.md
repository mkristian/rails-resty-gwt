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

with this you can create a new rails application like this (rails-3.1 needs more some more work to get it working with jruby). to setup resty you need to choose a java package name where to locate the GWT code (_com.example_)

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

## compile to javascript ##

which is not needed for development. to compile the java code as follows.

`gwt compile`

the first compile does the java to classfile compilation which is needed for the second step to compile the java to javascript

## scaffold a resource ##

now you can scaffold a model

`rails generate scaffold user name:string`

this creates a rails like structure within the GWT _client_ package:

>     src/main/java/com/example/client
    ├── activities
    │   └── UserActivity.java
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

the code points which plugs in all these classes into the application are:

* __src/main/java/com/example/client/managed/MyAppPlaceHistoryMapper.java__ which gets the history tokenizer registered for the new resource
* __src/main/java/com/example/client/managed/ActivityFactory.java__ which is a parametrizied gin-factory. this allows the UserActivity to be managed by GIN
* __src/main/java/com/example/client/managed/MyAppModule.java__ gets a provider to make the UsersRestService a singleton and adds the UserActivity to the ActivityFactory

all other GIN bindings are done via annotations.

before running the application you need to migrate the database so the new table is in place

`rake db:migrate`

## start the gwt development shell ##

and start the GWT development shell

`gwt run`

now the application has the usual rails specific views (for our users):

* `http://localhost:8888/users` the collection
* `http://localhost:8888/users/new` to create a new user
* `http://localhost:8888/users/<id>` to view user with id <id>
* `http://localhost:8888/users/<id>/edit` to edit user with id <id>

but also the json or xml variants (replace json with xml resp.):

* `http://localhost:8888/users.json` the collection
* `http://localhost:8888/users/<id>.json` to view user with id <id>

the GWT application uses following url pattern:

* `http://localhost:8888/MyApp.html?gwt.codesvr=127.0.0.1:9997#users` the collection (not implemented yet)
* `http://localhost:8888/MyApp.html?gwt.codesvr=127.0.0.1:9997#users/new` to create a new user
* `http://localhost:8888/MyApp.html?gwt.codesvr=127.0.0.1:9997#users/<id>` to view user with id <id>
* `http://localhost:8888/MyApp.html?gwt.codesvr=127.0.0.1:9997#users/<id>/edit` to edit user with id <id>

you will find these common url pattern also in the path annotation of the rest-service __src/main/java/com/example/client/restservices/UsersRestService.java__. the default baseurl for the restservices is `http://localhost:8888/` so the restservices use the following urls:

* `http://localhost:8888/users`
* `http://localhost:8888/users/<id>`

with a json payload - rails negotiates the response format on the given *HTTP\_ACCEPT* header field.

both *PUT* and *POST* send the new or changed resource back the GWT client, so caching can work on the client in a restful manner as such:

* POST will create a new resource and the result will be cached using the __Location__ header as key for the cache
* GET uses the url as cache key to retrieve the cached data
* PUT uses the url as cache key to either store the result or when the response has a status __CONFLICT__ it will delete the cache entry to allow the next get to retrieve the updated data
* DELETE uses the url as cache key to delete the cache entry

the __CONFLICT__ status belongs to an optimistic persistence/transaction which can be scaffolded by adding **--optimistic** to the options (which also needs the **--timestamps** which is rails default):

`rails generate scaffold user name:string --optimistic`

that optimistic persistence uses the __updated\_at__ attribute of the model to decide whether the data is still up to date or was already modified.

### webrick server ###

to run the application with default webrick you need first to compile the GWT part

`gwt compile`

the compiler will output the GWT app in __public/MyApp__ and then start the webrick.

`rails server`

the start url is [http://localhost:3000/MyApp.html](http://localhost:3000/MyApp.html).

such a setup also works with MRI and can be deploy on [heroku](http://heroku.com) ! no need for jruby for production unless you start using java on the ruby side of things, i.e. within the rails application.

*note* from rails-3.0.10 onward __rails new__ generates a jruby only Gemfile. with that you need to adjust your Gemfile so it will work for both MRI and JRuby.

# adding a menu entry for each scaffolded resource #

when creating the application add __--menu__ switch to the commandline

`gwt new my_app com.example --menu`

or rerun the resty:setup generator with that extra switch

`rails generate resty:setup com.example --menu`

with this you have already a GWT application with EntryPoint and could be started but does not do much. the class layout look as such:

>     src/main/java/com/example
    ├── client
    │   ├── ActivityPlaceActivityMapper.java
    │   ├── ActivityPlace.java
    │   ├── managed
    │   │   ├── ActivityFactory.java
    │   │   ├── MyAppMenuPanel.java
    │   │   ├── MyAppModule.java
    │   │   └── MyAppPlaceHistoryMapper.java
    │   └── MyAppEntryPoint.java
    └── MyApp.gwt.xml

which basically adds a __MyAppMenuPanel.java__ to the application. with each scaffolded resource there will be a new button in that menu.

`rails generate scaffold user name:string`

# login session and authorization #

this part is a bit invasiv, so have a look and see if it fits and suits your needs.

when creating the application add a __--session__ switch to the commandline

`gwt new my_app com.example --session`

or rerun the resty:setup generator with that extra switch inside an existing application

`rails generate resty:setup com.example --session`

>     src/main/java/com/example/
    ├── client
    │   ├── activities
    │   │   └── LoginActivity.java
    │   ├── ActivityPlaceActivityMapper.java
    │   ├── ActivityPlace.java
    │   ├── BreadCrumbsPanel.java
    │   ├── managed
    │   │   ├── ActivityFactory.java
    │   │   ├── MyAppModule.java
    │   │   └── MyAppPlaceHistoryMapper.java
    │   ├── models
    │   │   └── User.java
    │   ├── MyAppEntryPoint.java
    │   ├── places
    │   │   └── LoginPlace.java
    │   ├── restservices
    │   │   └── SessionRestService.java
    │   ├── SessionActivityPlaceActivityMapper.java
    │   └── views
    │       ├── LoginViewImpl.java
    │       └── LoginView.ui.xml
    └── MyApp.gwt.xml

this comes with session handle and authorization on both the server and the client side. of course you could add the menu switch here, too.

now a new scaffolded resource is protected by user authentication:

`rails generate scaffold account name:string`

`rmvn rake db:migrate`

`gwt run`

the authentication is fake and that part is hardcoded in __app/models/users.rb#authentication__ - 'name of group' == username, password == 'behappy'. per default users belonging to the 'root' group have full access, i.e. username == 'root' gives you such a root user.

the permissions get declared in __app/guards/accounts_guard.yml__ which is basically a list of **allowed groups** for each action on the accounts_controller.

now go to

`http://127.0.0.1:8888/myApp.html?gwt.codesvr=127.0.0.1:9997#accounts/new`

the idle session timeout is 15 minutes and can be configured in __config/application.rb__ by adding

`config.idle_session_timeout = 30 # minutes`

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
