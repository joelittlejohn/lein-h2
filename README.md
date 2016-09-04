# lein-h2

A Leiningen plugin to start an [H2 server](http://www.h2database.com/html/tutorial.html#using_server) in tcp mode.

## Usage

Put `[lein-h2 "0.1.0"]` into the `:plugins` vector of your project.clj.

You can start the H2 server then do some other tasks, the H2 server will stop when the downstream tasks are complete:

    $ lein h2 test

If you want to start an H2 server and wait, then invoke the h2 task with no further args:

    $ lein h2

You can configure the H2 server using a map under the `:h2` key in your project.clj (you probably want to place this in your `dev` profile):

```clj
:h2
 {:web              true   ; Start the web server with the H2 Console (default false)
  :web-allow-others true   ; Allow other computers to connect (default false)
  :web-daemon       true   ; Use a daemon thread (default false)
  :web-port         9999   ; The port (default: 8082)
  :web-ssl          true   ; Use encrypted (HTTPS) connections (default false)
  :browser          true   ; Start a browser connecting to the web server (default false)
  :tcp              true   ; Start the TCP server (default true)
  :tcp-allow-others true   ; Allow other computers to connect (default false)
  :tcp-daemon       true   ; Use a daemon thread (default false)
  :tcp-port         true   ; The port (default: 9092, will use a random port if this one is taken)
  :tcp-ssl          true   ; Use encrypted (SSL) connections (default false)
  :pg               true   ; Start the PG server (default false)
  :pg-allow-others  true   ; Allow other computers to connect (default false)
  :pg-daemon        true   ; Use a daemon thread (default false)
  :pg-port          true   ; The port (default: 5435, will use a random port if this one is taken)
  :properties       "~"    ; Dir in which to place server properties file (default: none)
  :base-dir         "/tmp" ; The base directory for H2 databases
  :if-exists        true   ; Only existing databases may be opened (default false)
  :trace            true   ; Print additional trace information (default false)
  :key       {:foo :bar}   ; Allows mapping of one database name to another e.g. {:foo :bar}
  }
```

If you don't add any config map to your project.clj, you'll get a default config that looks like:

```clj
:h2 {:tcp true}
```

## License

Copyright © 2016 Joe Littlejohn

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
