resty rails generators
==========

these generators add GWT support to an existing [rails](http://rubyonrails.org) application. JSON is used for the communication between rails and GWT.

on GWT side [restygwt](http://restygwt.fusesource.org/documentation/restygwt-user-guide.html) is used for the un/marshalling of the JSON payload and rails needed some tweaks to make things compatible - the 'resty-generators' gem provides these as initializer.

the development environment tries to follow the rails way as close as possible - judge yourself.

there are two approaches and they are a matter of taste. the jruby way or the maven way. but in any case you need java installed for either one. and to keep things simple I just discribe the jruby way. if you are interested in the maven commands they are printed by ruby-maven when executing it.

the glue between rails and GWT is the [ruby DSL for maven](https://github.com/sonatype/polyglot-maven/blob/master/pmaven-jruby/README.md). (thanx to [Sonatype](http://sonatype.org) for sponsoring the work on that DSL)

the jruby way
---------

requirement for this is jruby and then install (unless you have it already)

`jruby -S gem install ruby-maven`

with this you can create a new rails application like this (use only letters as application name since no alphabetical has bug right now)

`rmvn rails new myapp`

go into the created application (or any existing rails app)

`cd myapp`

add into the __Gemfile__

`gem 'resty-generators'`

to setup resty you need to choose a java package name where to locate the GWT code (_com.example_)

`rmvn rails generate resty:setup com.example`

with this you have already a GWT application with EntryPoint and could be started but does not do much. the class layout look as such:

>     src/main/java/com/example
      ├── client
      │   ├── ActivityPlaceActivityMapper.java
      │   ├── ActivityPlace.java
      │   ├── managed
      │   │   ├── ActivityFactory.java
      │   │   ├── MyappModule.java
      │   │   └── MyappPlaceHistoryMapper.java
      │   └── MyappEntryPoint.java
      └── myapp.gwt.xml

which is a GIN based setup. the __managed__ classes will be modified by the scaffold generators. the other files are one generated and can be changed as needed. the __MyappModule.java__ can be changed with care (only the __super.configure();__ and __new GinFactoryModuleBuilder()__should stay as it is). see also the EntryPoint __MyappEntryPoint.java__.

you can always compile the java code as follows, the first compile does the java to classfile compilation which is needed for the second step to compile the java to javascript.

`rmvn compile gwt:compile`

now you can scaffold a model

`rmvn rails generate scaffold user name:string`

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

* __src/main/java/com/example/client/managed/MyappPlaceHistoryMapper.java__ which gets the history tokenizer for the new resource
* __src/main/java/com/example/client/managed/ActivityFactory.java__ which is parametrizied gin-factory. this allows the UserActivity to be managed by GIN
* __src/main/java/com/example/client/managed/MyappModule.java__ gets a provider to make the UsersRestService a singleton and adds the UserActivity to the ActivityFactory

all other GIN bindings are done via annotations.

before running the application you need to migrate the database

`rmvn rake db:migrate`

and start the GWT development shell

`rmvn gwt:run`

now the application has the usual rails specific views (for our users):


* `http://localhost:8888/users` the collection
* `http://localhost:8888/users/new` to create a new user
* `http://localhost:8888/users/<id>` to view user with id <id>
* `http://localhost:8888/users/<id>/edit` to edit user with id <id>

but also the json or xml variants (replace json with xml resp.):

* `http://localhost:8888/users.json` the collection
* `http://localhost:8888/users/<id>.json` to view user with id <id>

the GWT application uses following url pattern:

* `http://localhost:8888/myapp.html?gwt.codesvr=127.0.0.1:9997|#users` the collection (not implemented yet)
* `http://localhost:8888/myapp.html?gwt.codesvr=127.0.0.1:9997|#users:new` to create a new user
* `http://localhost:8888/myapp.html?gwt.codesvr=127.0.0.1:9997|#users:<id>` to view user with id <id>
* `http://localhost:8888/myapp.html?gwt.codesvr=127.0.0.1:9997|#users:<id>/edit` to edit user with id <id>

(hopefully the `#users:` will be soon `#users/` to follow the rails pattern more closely).

you will find these common url pattern also in the path annotation of the rest-service __src/main/java/com/example/client/restservices/UsersRestService.java__. the default baseurl for the restservices is `http://localhost:8888/` so the restservices use the following urls:

* `http://localhost:8888/users`
* `http://localhost:8888/users/<id>`

with a json payload - rails negotiates response format on the given *HTTP\_ACCEPT* header field.

both *PUT* and *POST* send the new or changed resource back the GWT client, so caching can work on the client in restful manner as such:

* POST will create a new resource and the result will be cached using the __Location__ header as key for the cache
* GET uses the url as cache key to retrieve the cached data
* PUT uses the url as cache key to either store the result or when the response has a status __CONFLICT__ it will delete the cache entry to allow the next get to retrieve the updated data
* DELETE uses the url as cache key to delete the cache entry

the __CONFLICT__ status belongs to an optimistic persistence/transaction which can be scaffolded as well by adding **--optimistic** to the options (which needs the **--timestamps** as well but that is rails default):

`rmvn rails generate scaffold user name:string --optimistic`

that optimistic persistence uses the __updated\_at__ attribute of the model to decide whether the data is still up to date or was already modified.

### webrick server ###

to run the application with default webrick you need first to compile the GWT part

`rmvn compile gwt:compile`

the compiler will output the GWT app in __public/myapp__ and then start the webrick.

`rmvn rails server`

the start url is [http://localhost:3000/myapp.html](http://localhost:3000/myapp.html).

such a setup also works with MRI and can be deploy on [heroku](http://heroku.com) ! no need for jruby for production unless you start using java on the ruby side of things, i.e. within the rails application.


# login session and authorization

this part is a bit invasiv, so have a look and see if it fits and suits.

with newly create rails application and added __resty-generators__ gem to the Gemfile you can create a GWT with login and authroization:

`rmvn rails generate resty:setup com.example --session`

>     src/main/java/com/example/
    ├── client
    │   ├── activities
    │   │   └── LoginActivity.java
    │   ├── ActivityPlaceActivityMapper.java
    │   ├── ActivityPlace.java
    │   ├── BreadCrumbsPanel.java
    │   ├── managed
    │   │   ├── ActivityFactory.java
    │   │   ├── MyappModule.java
    │   │   └── MyappPlaceHistoryMapper.java
    │   ├── models
    │   │   └── User.java
    │   ├── MyappEntryPoint.java
    │   ├── places
    │   │   └── LoginPlace.java
    │   ├── restservices
    │   │   └── SessionRestService.java
    │   ├── SessionActivityPlaceActivityMapper.java
    │   └── views
    │       ├── LoginViewImpl.java
    │       └── LoginView.ui.xml
    └── myapp.gwt.xml

this comes with session handle and authorization on both the server and the client side.

now a new scaffolded resource is protected by user authentication:

`rmvn rails generate scaffold account name:string`

`rmvn rake db:migrate`

`rmvn gwt:run`

the authentication is fake and that part is hardcoded in __app/models/users.rb#authentication__ - username == groupname, password == 'behappy'. per default users belonging to the 'root' group have full access, i.e. username == 'root' gives you such a root user.

the permissions get declared in __app/guards/accounts_guard.rb__ which is basically a list of **allowed groups** for each action on the accounts_controller.

now go to

`http://127.0.0.1:8888/myapp.html?gwt.codesvr=127.0.0.1:9997|#accounts:new`

the idle session timeout is 15 minutes and can be configured in __config/application.rb__ by adding

`config.idle_session_timeout = 30 # minutes`

what's next
----------

* collections
* error handling - i.e. validation errors (server side)
* selenium tests
* more types on GWT, i.e. dates
* hide buttons if the logged in user does not have the permissions to use it

some little things would be

* move from `#users:<id>/edit` to `#users/<id>/edit` history tokens
* Restservices come as singleton when used the @Singleton annotation
* little left side menu with a link for each model
* GWT editors (which will have client side validation in future(

## Note on Patches/Pull Requests

* Fork the project.
* Make your feature addition or bug fix.
* Add tests for it. This is important so I don't break it in a future version unintentionally.
* Commit, do not mess with version, or history. (if you want to have your own version, that is fine but bump version in a commit by itself I can ignore when I pull)
* Send me a pull request.

the maven way (maybe outdated and incomplete)
----------

maven3 is required to run these maven plugins below. the steps are the same as above but the commands are slightly different:

`mvn de.saumya.mojo:rails3-maven-plugin:0.26.0:new -Dargs=myapp`

`mvn rails3:generate -Dargs="resty:setup com.example"`

`mvn rails3:generate -Dargs="scaffold user name:string"`

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

more about that DSL and the connection with gemspec files and _Gemfile_ can be found in [polyglot-maven (jruby)](https://github.com/sonatype/polyglot-maven/tree/master/pmaven-jruby) project.

shortcomings
---------

* ruby-maven might not support all possible configurations within the Gemfile, dito the maven DSL is not complete yet (from the maven point of view).
* there is bug in formating timestamps which might prevent a model to be changed since the "conflict" check always delivers conflict.
* only String properties are implemented on the GWT side
* and possible other things, but enjoy anyways ;-)
