(ns clj-strava.api-tests
  (:require [clj-strava.api :refer :all])
  (:use midje.sweet))

(fact "Url building"
      (replace-keywords "/v3/activities/:id" {:id 123}) => "/v3/activities/123"
      (replace-keywords
       "/v3/:id1/foobar/:id2/quux" {:id1 6 :id2 7})    => "/v3/6/foobar/7/quux")
