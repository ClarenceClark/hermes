(ns hermes.db
  (:require [cljs-time.core :as t]))

(defn generate-tags []
  (for [i (range 0 10)]
    {i {:id i
        :name (str "Tag #" i)}}))

(defn generate-notifs []
  (for [i (range 0 20)]
    {i {:id i
        :title (str "Notif #" i)
        :content (str "Lots of content here, notif#" i)
        :time (t/now)
        :tags [1 2]}}))

(def default-db
  {:login {:username ""
           :apikey ""}
   :ui {:active-notif :none
        :current-page :home
        :navdrawer-open? false
        :create-notif {:show? false
                       :title ""
                       :content ""
                       :tags #{}}}
   :docs {:docs ""}
   :tags (apply merge (generate-tags))
   :notifs (apply merge (generate-notifs))})