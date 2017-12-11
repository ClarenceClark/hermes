(ns hermes.subevts.notifs
  (:require [hermes.subevts.utils :as utils]
            [re-frame.core :as rf]
            [clojure.set :as set]))

(utils/reg-keyword-diff-sub :notifs.map :notifs)

(def notif-int [(rf/path :notifs) rf/trim-v])

(rf/reg-sub
  :notifs.all
  :<- [:notifs.map]
  (fn [notif-map sub dyn]
    (vals notif-map)))

(rf/reg-sub
  :notifs.to-show
  :<- [:notifs.all]
  :<- [:ui.filter-tags]
  (fn [[notifs-all filter-tags] sub dyn]
    (let [filters (set filter-tags)
          filter-fn #(< 0 (count (set/intersection (:tags %1) filters)))
          ; Select only notifs where its tags and the filters intersect
          filtered (filter filter-fn notifs-all)
          ;_ (println filtered)
          sorted (sort-by :id > filtered)]
      sorted)))


(rf/reg-event-db
  :notifs.ins-db
  [notif-int]
  (fn [notif-map [id title content time tags]]
    (assoc notif-map id {:id id
                         :title title
                         :content content
                         :time time
                         :tags tags})))