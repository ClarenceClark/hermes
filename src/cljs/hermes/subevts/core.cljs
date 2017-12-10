(ns hermes.subevts.core
  (:require [re-frame.core :as rf]
            [hermes.db :as db]
            [hermes.subevts.utils :as utils]
            [hermes.subevts.ui]))

(rf/reg-event-db
  :init-db
  (fn [db _]
    db/default-db))

; Docs
(utils/reg-subevt-pair :docs :set-docs [:docs])

; Username and apikey
(utils/reg-subevt-pair :login.username :login.set-username [:login :username])
(utils/reg-subevt-pair :login.apikey :login.set-apikey [:login :apikey])