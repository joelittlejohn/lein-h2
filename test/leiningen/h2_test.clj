(ns leiningen.h2-test
  (:require [leiningen.h2 :as h2]
            [clojure.test :refer :all]))

(deftest convert-config-creates-h2-compatible-args
  (testing "booleans become single flags"
    (is (= ["-tcp" "-web"]
           (h2/convert-config {:tcp true
                               :web true
                               :pg false}))))

  (testing "kebab case is converted to lower camel case"
    (is (= ["-webAllowOthers" "-baseDir" "~" "-tcpPort" "9999"]
           (h2/convert-config {:web-allow-others true
                               :base-dir "~"
                               :tcp-port 9999}))))

  (testing "ssl is uppercased"
    (is (= ["-tcpSSL"]
           (h2/convert-config {:tcp-ssl true}))))

  (testing "key becomes repeating -key <to> <from>"
    (is (= ["-key" "foo" "bar" "-key" "baz" "qux"]
           (h2/convert-config {:key {:foo :bar
                                     :baz :qux}})))))

(deftest default-config-starts-tcp-server
  (is (= ["-tcp"] (h2/config nil))))

(deftest h2-task-succeeds
  (testing "with empty config"
    (h2/h2 {} "do"))

  (testing "with all config args set"
    (h2/h2 {:h2 {:web true :web-allow-others true :web-daemon true :web-port 57983 :web-ssl false :browser false
                 :tcp true :tcp-allow-others true :tcp-daemon true :tcp-port 57984 :tcp-ssl false
                 :pg  true :pg-allow-others  true :pg-daemon  true :pg-port  57985
                 :properties "~"
                 :base-dir "~"
                 :if-exists true
                 :trace false
                 :key {"foo" "bar"
                       :baz :qux}}}
           "do")))

(deftest h2-task-checks-config-for-invalid-keys
  (is (thrown? IllegalArgumentException (h2/h2 {:h2 {:foo :bar}} "do"))))
