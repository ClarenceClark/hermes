(ns hermes.subevts.core
  (:require [re-frame.core :as rf]
            [hermes.db :as db]
            [hermes.subevts.utils :as utils]))

(rf/reg-event-db
  :init-db
  (fn [db _]
    db/default-db))

(rf/reg-event-fx
  :the-void
  (fn [_ _]
    {}))

(rf/reg-event-fx
  :fetch-data
  (fn [_ _]
    {:dispatch-n [[:tags.retrieve]
                  [:notifs.retrieve]]}))

; Docs
(utils/reg-subevt-pair :docs :set-docs [:docs :docs])