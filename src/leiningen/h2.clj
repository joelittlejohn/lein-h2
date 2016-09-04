(ns leiningen.h2
  (:require [clojure.string :as str]
            [leiningen.core.main :as main]))

(def ^:private config-keys
  {:web              "Start the web server with the H2 Console (default false)"
   :web-allow-others "Allow other computers to connect (default false)"
   :web-daemon       "Use a daemon thread (default false)"
   :web-port         "The port (default: 8082)"
   :web-ssl          "Use encrypted (HTTPS) connections (default false)"
   :browser          "Start a browser connecting to the web server (default false)"
   :tcp              "Start the TCP server (default true)"
   :tcp-allow-others "Allow other computers to connect (default false)"
   :tcp-daemon       "Use a daemon thread (default false)"
   :tcp-port         "The port (default: 9092, will use a random port if this one is taken)"
   :tcp-ssl          "Use encrypted (SSL) connections (default false)"
   :pg               "Start the PG server (default false)"
   :pg-allow-others  "Allow other computers to connect (default false)"
   :pg-daemon        "Use a daemon thread (default false)"
   :pg-port          "The port (default: 5435, will use a random port if this one is taken)"
   :properties       "Server properties (default: ~, disable: null)"
   :base-dir         "The base directory for H2 databases"
   :if-exists        "Only existing databases may be opened (default false)"
   :trace            "Print additional trace information (default false)"
   :key              "Allow mapping of a database name to another e.g. {\"foo\" \"bar\"}"})

(def default-config
  {:tcp true})

(defn- flag?
  [v]
  (or (true? v) (false? v)))

(defn- key?
  [k]
  (= :key k))

(defn- kebab->camel
  [s]
  (str/replace s #"-." #(-> % second str str/upper-case)))

(defn- uppercase-ssl
  [s]
  (str/replace s "ssl" "SSL"))

(defn convert-key
  [keys]
  (reduce-kv #(conj %1 "-key" (name %2) (name %3)) [] keys))

(defn- convert-config-value
  [[k v]]
  (let [arg-name (str "-" (-> (name k)
                              uppercase-ssl
                              kebab->camel))]
    (if (flag? v)
      (if v
        [arg-name]
        [])
      (if (key? k)
        (convert-key v)
        [arg-name (str v)]))))

(defn convert-config
  [config]
  (reduce #(apply conj %1 (convert-config-value %2)) [] config))

(defn config
  [project]
  (-> default-config
      (merge (:h2 project))
      convert-config))

(defn check-config-keys
  [config]
  (when-let [bad-keys (seq (remove config-keys (keys config)))]
    (throw (IllegalArgumentException. (str "Unsupported key(s) for lein-h2: " (str/join ", " bad-keys))))))

(defn ^:no-project-needed h2
  "Start an H2 server (tcp mode), run the given task, then stop the
  server. See http://www.h2database.com/html/tutorial.html#using_server"
  [project & args]
  (let [server (org.h2.tools.Server.)]
    (check-config-keys (:h2 project))
    (.runTool server (into-array String (config project)))
    (Thread/sleep 1000) ;; give h2 services a little time to start
    (try
      (if (seq args)
        (main/apply-task (first args) project (rest args))
        (.join (Thread/currentThread)))
      (finally
        (.shutdown server)))))
