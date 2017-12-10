(ns hermes.subevts.notifs
  (:require [hermes.subevts.utils :as utils]
            [re-frame.core :as rf]))

(utils/reg-keyword-diff-sub :notifs.map :notifs)

(def notif-int [(rf/path :notifs) rf/trim-v])

(rf/reg-sub
  :notifs.all
  :<- [:notifs.map]
  (fn [notif-map sub dyn]
    (vals notif-map)))

(rf/reg-sub
  :notifs.to-show
  :<- [:notifs.map]
  :<- [:ui.tag-filter]
  :<- [:prefs.view-limit]
  (fn [[notifs-map filter limit] sub dyn]
    (let [filter-fn #(contains? (:tags %) filter)])))

(rf/reg-event-db
  :notifs.ins-db
  [notif-int]
  (fn [notif-map [id title content time tags]]
    (assoc notif-map id {:id id
                         :title title
                         :content content
                         :time time
                         :tags tags})))