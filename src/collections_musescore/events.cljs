(ns collections-musescore.events
  (:require [collections-musescore.db :as db]
            [collections-musescore.events-util :as util]
            [re-frame.core :refer [inject-cofx reg-event-db reg-event-fx]]))

(reg-event-fx
 :initialise-db
 [(inject-cofx :local-store-collections)
  util/check-spec]
 db/initialize-db)
