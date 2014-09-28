(ns clj-strava.client
  (:require [org.httpkit.client :as http]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [clojure.core.async :refer [chan go >!]]))

(defn xform [x]
  (-> x
      :body
      json/read-str
      clojure.walk/keywordize-keys))

(defn http-get [url opts result]
  (log/info "getting: " url opts)
  (http/get url opts #(go (>! result %))))

(defn json-get [url opts]
  (let [result (chan 1 (map xform))]
    (http-get url opts result)
    result))
