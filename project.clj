(defproject ekimber/clj-strava "0.1.1"
  :description "Strava API"
  :url "https://github.com/ekimber/clj-strava"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0-alpha2"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [http-kit "2.1.16"]
                 [org.clojure/data.json "0.2.1"]
                 [org.clojure/tools.logging "0.2.6"]
                 [environ "1.0.0"]]
  :profiles {:dev {:dependencies [[midje "1.5.1"]]}})
