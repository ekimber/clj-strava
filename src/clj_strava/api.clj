(ns clj-strava.api
  (:require [clojure.string :as str]
            [org.httpkit.client :as http]
            [clojure.data.json :as json]
            [clj-strava.client :as client]
            [clojure.java.io :as io]
            [clojure.core.async :refer [chan go >!]]
            [environ.core :refer [env]])
  (:import [java.net URI URLEncoder]))

(def strava-url "https://www.strava.com")
(def endpoint (str strava-url "/api"))
(def secret (env :strava-secret))

(defn url-encode [s] (URLEncoder/encode (str s) "utf8"))
(defn auth-header [token] {:headers {"Authorization" (str "Bearer " token)}})

(defn swap-tokens [code]
  (:body
   @(http/post (str strava-url "/oauth/token")
               {:form-params
                {:client_id 2792
                 :client_secret (url-encode secret)
                 :code (url-encode code)}})))

(defn access-token [code]
  ((json/read-str (swap-tokens code)) "access_token"))

(defn exchange-tokens [code]
   {:access-token ((access-token code) "access_token")})

(defn replace-keywords
  "Replace url params like so:
  (replace-keywords /v3/activities/:id {:id 123}) -> /v3/activities/123"
  [url keymap]
  (if (empty? keymap)
    url
    (let [k (first (keys keymap))]
      (replace-keywords
       (str/replace url (re-pattern (str k)) (str (get keymap k)))
       (dissoc keymap k)))))

(defn url-builder
  ([url]
   (str endpoint url))
  ([url params]
   (url-builder (replace-keywords url params))))

;TODO could simplify this macro
(defmacro defapifn
  ([name url]
   `(defn ~name
      ([token# params#]
       (client/json-get
         (url-builder ~url)
         (merge {:query-params params#} (auth-header token#))))
       ([token#]
        (~name token# {}))))

   ([name url & url-param-names] ;TODO could validate url-param-names
    `(defn ~name
       ([token# url-params# params#]
        (client/json-get
         (url-builder ~url url-params#)
         (merge {:query-params params#} (auth-header token#))))
       ([token# url-params#]
        (~name token# url-params# {})))))

#_(defmacro tm [arg & ps]
  `(defn a ~(if ps '[arg f] '[arg]) f))

#_(macroexpand '(tm 1))

;; Strava API V3 resource definitions

(defapifn activities "/v3/athlete/activities")
(defapifn activity "/v3/activities/:id" :id)
(defapifn activity-streams "/v3/activities/:id/streams/:types" :id :types)
(defapifn activity-comments "/v3/activities/:id/comments" :id)
(defapifn activity-kudos "/v3/activities/:id/kudos" :id)
(defapifn activity-zones "/v3/activities/:id/zones" :id)

(defapifn athlete "/v3/athlete")
(defapifn athlete-by-id "/v3/athletes/:id" :id)
(defapifn friends "/v3/athlete/friends")
(defapifn athlete-friends "/v3/athletes/:id/friends" :id)
(defapifn followers "/v3/athlete/followers")
(defapifn athlete-followers "/v3/athletes/:id/followers" :id)
(defapifn both-following "/v3/athletes/:id/both-following" :id)
(defapifn athlete-koms "/v3/athletes/:id/koms" :id)
(defapifn athlete-clubs "/v3/athlete/clubs")

(defapifn clubs "/v3/clubs/:id" :id)
(defapifn club-members "/v3/clubs/:id/members" :id)
(defapifn club-activities "/v3/clubs/:id/activities" :id)

(defapifn gear "/v3/gear/:id" :id)

(defapifn segments "/v3/segments/:id" :id)
(defapifn segment-efforts "/v3/segments/:id/all_efforts" :id)
(defapifn leaderboards "v3/segments/:id/leaderboard" :id)
(defapifn starred-segments "v3/athlete/:id/segments/starred" :id)
(defapifn explore-segments "/v3/segments/explore")
(defapifn all-efforts "/v3/segments/:id/all_efforts" :id)
(defapifn segment-streams "v3/segments/:id/streams/:types" :id :types)
(defapifn effort-streams "/v3/segment_efforts/:id/streams/:types" :id :types)

(defapifn uploads "/v3/uploads")
