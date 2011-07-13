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

with this you can create a new rails application like this

`rmvn rails new my_app`

go into the created application (or any existing rails app)

`cd my_app`

add into the __Gemfile__

`gem 'resty-generators'`

to setup resty you need to choose a java package name where to locate the GWT code (_com.example_)

`rmvn rails generate resty:setup com.example`

with this you have already a GWT application with EntryPoint and could be started but does not do much. the class layout look as such:

>     src/main/java/com/example
      ├── client
      │   ├── managed
      │   │   ├── ActivityFactory.java
      │   │   ├── ActivityPlaceActivityMapper.java
      │   │   ├── ActivityPlace.java
      │   │   ├── MyAppModule.java
      │   │   └── MyAppPlaceHistoryMapper.java
      │   └── MyApp.java
      └── my_app.gwt.xml

which is basically a GIN based setup. the managed classes will be modified by the scaffold generators. the other files are one generated and can be changed as needed. the __MyAppModule.java__ can be changed with care (only the __super.configure();__ and __new GinFactoryModuleBuilder()__should stay as it is).

now you can scaffold a model

`rmvn rails generate scaffold user name:string`

the later creates a rails like structure within the GWT _client_ package. see also the EntryPoint _MyApp.java_:

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

the code points which plugs all these into the application are:

* __src/main/java/com/example/client/managed/MyAppPlaceHistoryMapper.java__ which gets the history tokenizer for the new resource
* __src/main/java/com/example/client/managed/ActivityFactory.java__ which is parametrizied gin-factory. this allows the UserActivity to be managed by GIN
* __src/main/java/com/example/client/managed/MyAppModule.java__ gets a provider to make the UsersRestService a singleton and adds the UserActivity to the ActivityFactory

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

but also the json or xml variants (replace json with xml):

* `http://localhost:8888/users.json` the collection
* `http://localhost:8888/users/<id>.json` to view user with id <id>

the GWT application uses following url pattern:

* `http://localhost:8888/my_app.html#users` the collection (not implemented yet)
* `http://localhost:8888/my_app.html#users:new` to create a new user
* `http://localhost:8888/my_app.html#users:<id>` to view user with id <id>
* `http://localhost:8888/my_app.html#users:<id>/edit` to edit user with id <id>

(hopefully the `#users:` will be soon `#users/` to follow the rails pattern more closely).

you will find these common url pattern also in the path annotation of the rest-service __src/main/java/com/example/client/restservices/UsersRestService.java__. but since the default GWT baseurl is `http://localhost:8888/my_app` that is why the restservice uses following urls:

* `http://localhost:8888/my_app/users`
* `http://localhost:8888/my_app/users/<id>`

with a json payload - rails negotiates on the *HTTP\_ACCEPT* header field. there is Rack layer which removes the __my\_app__ from the url and add it to any *Location* header in the response.

both *PUT* and *POST* send the new or changed resource back the GWT client, so caching can work in restful manner as such:

* POST will create a new resource and the result will be cached using the __Location__ header as key for the cache
* GET uses the url as cache key to retrieve the cached data
* PUT uses the url as cache key to either store the result or when the response has a status __CONFLICT__ it will delete the cache entry to allow the next get to retrieve the updated data
* DELETE uses the url as cache key to delete the cache entry

the __CONFLICT__ status belongs to an optimistic persistence/transaction which can be scaffolded as well by add **--optimistic** to options (and **--timestamps**):

`rmvn rails generate scaffold user name:string --optimistic`

which uses the __updated\_at__ attribute of the model to decide whether the data is still up to date or was already modified.

### webrick server ###

to run the application with default webrick you need first to compile the GWT part

`rmvn gwt:compile`

the compiler will output the GWT app in __public/my_app__ and then start the webrick. the start url is [http://localhost:3000/my_app.html](http://localhost:3000/my_app.html)

`rmvn rails server`

such a setup also works with MRI and can be deploy on [heroku](http://heroku.com) ! no need for jruby for production unless you start using java on the ruby side of things, i.e. within the rails application.

what's next
----------

* collections
* error handling - i.e. validation errors (server side)
* selenium tests
* all possible types

some little things would be

* move from `#users:<id>/edit` to `#users/<id>/edit` history tokens
* Restservices come as singleton when used the @Singleton annotation
* little left side menu with a link for each model
* GWT editors to prepare for client side validation

## Note on Patches/Pull Requests

* Fork the project.
* Make your feature addition or bug fix.
* Add tests for it. This is important so I don't break it in a future version unintentionally.
* Commit, do not mess with version, or history. (if you want to have your own version, that is fine but bump version in a commit by itself I can ignore when I pull)
* Send me a pull request.

the maven way (maybe outdated and incomplete)
----------

maven3 is required to run these maven plugins below. the steps are the same as above but the commands are slightly different:

`mvn de.saumya.mojo:rails3-maven-plugin:0.26.0:new -Dargs=my_app`

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
