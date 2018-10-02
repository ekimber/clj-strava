# clj-strava

A library that provides functions for consuming the Strava API V3.

Currently only auth and the GET methods are available.  Not all resources have been tested.
The library is currently in development and provided functions may change at any time.


# Requirements

* [Clojure 1.7 - 1.9](https://clojure.org/community/downloads) `brew install clojure`
* [core.async](https://github.com/clojure/core.async) last tested against 0.4.474

## Usage

Leiningen dependency:

    [ekimber/clj-strava "0.1.1"]

Place your secret in a `.lein-env` file `{:strava-secret "your secret" :strava-id 1234}` in the project root,
or supply environment variables `STRAVA_SECRET` and `STRAVA_ID`.

You also want to collect your public access token which you can add to .lein-env or simply store as a def within your clj file.

NS require :

    (:require [clj-strava.api :as strava]
              [clojure.core.async :refer [<!!]])

To acquire a token, obtain a code as described here: http://strava.github.io/api/v3/oauth/

The code is obtained from the response during [Strava's OAuth2 authentication](https://developers.strava.com/docs/authentication/) process. This is a good [write up about how to get this code](https://yizeng.me/2017/01/11/get-a-strava-api-access-token-with-write-permission/).

## Get the Authorization Code

In your frontend ClojureScript view, you need create a link to the authorization page hosted on strava.com:

```
(def client-id "your-client-id")
(def redirect-uri "http://localhost:3000/api/strava/token")
(def response-type "code")
(def scope "view_private,write")
(def approval-prompt "force")
(def strava-authorize-url
  (str "https://www.strava.com/oauth/authorize?"
       "client_id=" client-id "&"
       "response_type=" response-type "&"
       "redirect_uri=" redirect-uri "&"
       "approval_prompt=" approval-prompt "&"
       "scope=" scope))

[:a {:href strava-authorize-url} "Authorize Strava"]
```

This will produce the following url:
```
https://www.strava.com/oauth/authorize?client_id=28964&response_type=code&redirect_uri=http://localhost:3000/api/strava/token&approval_prompt=force&scope=view_private,write
```

After the user visits this page and accepts the authorization, it will redirect them back to your app.

Then, from your Clojure web server, you need to handle this redirect to your  "/api/strava/token" route and update your user record adding the code so that they don't have to authorize again.

An example compojure GET:
```
(GET "/api/strava/token" request
"handle the strava request, grab the code and store it with the user who granted access then redirect back to your home page"
  (update-user-with-code (get-in request [:params :code]))
  (redirect "/"))
```

## Get Activities

Once you have this code, you can then swap the code for an access token.

Pass in the access token to the API functions.  They require a map of URL replacements where appropriate and additional query params can be passed in an optional map.  Keywordized JSON is returned on an core.async channel.

Along with several ring and compojure libraries, your server will also include:

* [clj-strava.api :as strava]
* [clojure.core.async :refer [<!!]]
* [cheshire.core :as cheshire]

```
(def public-strava-token
    (saved-user :strava-public-token))

(defn get-activities []
  "this GET route handler will return your json activities from Strava"
  (fn []
    (let [access-token (strava/access-token public-strava-token)]
      (<!! (strava/activities access-token {"per_page 5"})))))
```

In order to return the activities as json, you can call get-activities from your compojure GET route and wrap it in cheshire.

```
(GET "/api/strava/activities/get" []
  { :status 200
    :content-type "application/json; charset=UTF-8"
    :body (cheshire/generate-string (get-activities))})
```

Example: list activities

    (<!! (strava/activities access-token {"per_page 5"}))

Example: list athlete's KOMs

    (<!! (strava/athlete-koms access-token {:id 490421} {"per_page" 5}))

## Available API functions

     activities "/v3/athlete/activities"
     activity "/v3/activities/:id" :id
     activity-streams "/v3/activities/:id/streams/:types" :id :types
     activity-comments "/v3/activities/:id/comments" :id
     activity-kudos "/v3/activities/:id/kudos" :id
     activity-zones "/v3/activities/:id/zones" :id

     athlete "/v3/athlete"
     athlete-by-id "/v3/athletes/:id" :id
     friends "/v3/athlete/friends"
     athlete-friends "/v3/athletes/:id/friends" :id
     followers "/v3/athlete/followers"
     athlete-followers "/v3/athletes/:id/followers" :id
     both-following "/v3/athletes/:id/both-following" :id
     athlete-koms "/v3/athletes/:id/koms" :id
     athlete-clubs "/v3/athlete/clubs"

     clubs "/v3/clubs/:id" :id
     club-members "/v3/clubs/:id/members" :id
     club-activities "/v3/clubs/:id/activities" :id

     gear "/v3/gear/:id" :id

     segments "/v3/segments/:id" :id
     segment-efforts "/v3/segments/:id/all_efforts" :id
     leaderboards "v3/segments/:id/leaderboard" :id
     starred-segments "v3/athlete/:id/segments/starred" :id
     explore-segments "/v3/segments/explore"
     all-efforts "/v3/segments/:id/all_efforts" :id
     segment-streams "v3/segments/:id/streams/:types" :id :types
     effort-streams "/v3/segment_efforts/:id/streams/:types" :id :types

     uploads "/v3/uploads"

## Github Projects using clj-strava

[clojure-shed lesson 3](https://github.com/headwinds/clojure-shed)

## License

Copyright © 2014 Edward Kimber

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
