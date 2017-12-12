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
        :tags #{1 2}}}))

(def default-db
  {:login {:username "testing1"
           :apikey "847aa22d8737c024bbee7df1e8d47004e19380b5392eea9083176f87b69bf2cdc9d87e7492c7dce553a2a9fac86ae3bfbf79e40454e5ee228346c9d28c22e88c"}
   :ui {:login-error ""
        :snackbar-msg ""
        :filter-tags []
        :active-notif :none
        :current-page :home
        :navdrawer-open? false
        :tag-add {:show? false
                  :id ""
                  :name ""}
        :tag-ed {:active-id :none
                 :name ""}
        :create-notif {:show? false
                       :title ""
                       :content ""
                       :tags #{}}}
   :docs {:docs ""}
   :tags {}
   :notifs {}})