(ns hermes.subevts.notifs
  (:require [hermes.subevts.utils :as utils]
            [re-frame.core :as rf]
            [clojure.set :as set]
            [hermes.subevts.network :as nt]))

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
  :notifs.ins-one
  [notif-int]
  (fn [notif-map [id title content time tags]]
    (assoc notif-map id {:id id
                         :title title
                         :content content
                         :time time
                         :tags tags})))

(rf/reg-event-db
  :notifs.ins-multi
  [notif-int]
  (fn [notif-map [notifs]]
    (let [tuples (map (fn [n] [(:id n) n] notifs))
          new (into {} tuples)]
      (merge notif-map new))))

(rf/reg-event-fx
  :notifs.send
  (fn [cofx _]
    (let [db (:db cofx)
          notif (-> db
                    (get-in [:ui :create-notif])
                    (select-keys [:title :content :tags]))]
      {:dispatch [:ui.set-sending-notif? true]
       :http-xhrio
       (nt/merge-defaults db
         {:method :get
          :uri (str nt/api-base "/notifications")
          :params notif
          :on-success [:notifs.send-success]
          :on-failure [:notifs.send-fail]})})))

(rf/reg-event-fx
  :notifs.send-success
  (fn [cofx [_ notif]]
    {:dispatch-n [[:notifs.ins-one notif]
                  [:ui.set-sending-notif? false]]}))

(rf/reg-event-fx
  :notifs.send-fail
  (fn [_ _]
    {:dispatch-n [[:ui.set-sending-notif? false]
                  [:notifs.]]}))

(rf/reg-event-db
  :notifs.fetch-success
  (fn [cofx [_ notifs]]
    {:dispatch-n [[:notifs.ins-multi notifs]
                  [:ui.set-fetching-notifs? false]]}))