(ns hermes.subevts.tags
  (:require [hermes.subevts.utils :as util]
            [re-frame.core :as rf]))

(util/reg-keyword-diff-sub :tags.map :tags)

(rf/reg-sub
  :tags.all
  :<- [:tags.map]
  (fn [tags-map sub dyn]
    (vals tags-map)))

(rf/reg-event-db
  :tags.set-by-id
  [(rf/path :tags) rf/trim-v]
  (fn [tags-map [id name]]
    (assoc tags-map id name)))