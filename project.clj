(defproject lein-h2 "0.1.1-SNAPSHOT"
  :description "A lein plugin that runs an h2 instance in server mode"
  :url "http://github.com/joelittlejohn/lein-h2"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[com.h2database/h2 "1.4.192"]]
  :deploy-repositories [["releases" :clojars]]
  :eval-in-leiningen true)
