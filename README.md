# clj-strava

A library that provides functions for consuming the Strava API V3.

Currently only auth and the GET methods are available.  Not all resources have been tested.
The library is currently in development and provided functions may change at any time.

## Usage

Leiningen dependency:

    [ekimber/clj-strava "0.1.0"]

Place your secret in a `.lein-env` file `{:strava-secret "your secret"}` in the project root,
or supply an environment variable `STRAVA_SECRET`.

NS require :

    (:require [clj-strava.api :as strava)

To acquire a token, obtain a code as described here: http://strava.github.io/api/v3/oauth/

Then we can swap the code for an access token:

    (strava/swap-tokens code)

Pass in the access token to the API functions.  They require a map of URL replacements where appropriate
and additional query params can be passed in an optional map.  Keywordized JSON is returned on an async channel.

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


## License

Copyright © 2014 Edward Kimber

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
