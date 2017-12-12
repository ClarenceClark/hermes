(ns hermes.subevts.notifs
  (:require [hermes.subevts.utils :as utils]
            [re-frame.core :as rf]
            [clojure.set :as set]
            [hermes.subevts.network :as nt]
            [cljs-time.format :as fmt]))

(utils/reg-keyword-diff-sub :notifs.map :notifs)

(def notif-int [(rf/path :notifs) rf/trim-v])

(def bsc-dt (fmt/formatters :basic-date-time))

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
  (fn [notif-map {:keys [id title content time tags]}]
    (assoc notif-map id {:id id
                         :title title
                         :content content
                         :time (fmt/parse bsc-dt time)
                         :tags tags})))

(rf/reg-event-db
  :notifs.ins-multi
  [notif-int]
  (fn [notif-map [notifs]]
    (let [timed (map (fn [n] (update-in n [:time] #(fmt/parse bsc-dt %))) notifs)
          ;_ (println timed)
          tuples (map (fn [n] [(:id n) n]) timed)
          new (into {} tuples)]
      (merge notif-map new))))

(rf/reg-event-fx
  :notifs.send
  (fn [cofx _]
    (let [db (:db cofx)
          notif (-> db
                    (get-in [:ui :create-notif])
                    (select-keys [:title :content :tags]))]
      {:dispatch [:ui.set-snackbar-msg "Sending notif ..."]
       :http-xhrio
       (nt/merge-defaults db
         {:method :post
          :uri (str nt/api-base "/notifications")
          :params notif
          :on-success [:notifs.send-success]
          :on-failure [:notifs.send-fail]})})))

(rf/reg-event-fx
  :notifs.send-success
  (fn [cofx [_ notif]]
    {:dispatch-n [[:notifs.ins-one notif]
                  [:ui.set-snackbar-msg "Notification sent successfully"]
                  [:ui.create-notif.set-show? false]
                  [:notifs.retrieve]]}))

(rf/reg-event-fx
  :notifs.send-fail
  (fn [cofx [_ fail]]
    {:dispatch-n [[:ui.set-snackbar-msg
                   (str "Notification failed to send" (:error fail))]]}))

(rf/reg-event-fx
  :notifs.retrieve
  (fn [{:keys [db]} _]
    {:dispatch-n [[:tags.retrieve]
                  [:ui.set-snackbar-msg "Retrieving notifs ..."]]
     :http-xhrio
     (nt/merge-defaults db
        {:method :get
         :uri (str nt/api-base "/notifications")
         :params {:after (or (apply max (vals (:notifs db))) 0)}
         :on-success [:notifs.fetch-success]
         :on-failure [:notifs.fetch-failure]})}))

(rf/reg-event-fx
  :notifs.fetch-success
  (fn [cofx [_ notifs]]
    {:dispatch-n [[:notifs.ins-multi notifs]
                  [:ui.set-snackbar-msg "Notifications retrieved successfully"]]}))

(rf/reg-event-fx
  :notifs.fetch-failure
  (fn [cofx [_ fail]]
    {:dispatch-n [[:ui.set-snackbar-msg
                   (str "Failed to retrieve notifications" (:error fail))]]}))
