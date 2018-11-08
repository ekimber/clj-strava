(defproject ekimber/clj-strava "0.2.0-SNAPSHOT"
  :description "Strava API"
  :url "https://github.com/ekimber/clj-strava"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/core.async "0.4.474"]
                 [http-kit "2.2.0"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/tools.logging "0.2.6"]
                 [environ "1.1.0"]]
  :profiles {:dev {:dependencies [[midje "1.9.4"]]
                   :plugins [[lein-midje "3.1.3"]]}
             :1.7 {:dependencies [[org.clojure/clojure "1.7.0"]]}
             :1.8 {:dependencies [[org.clojure/clojure "1.8.0"]]}})
