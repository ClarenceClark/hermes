(ns hermes.views.notifs
  (:require [hermes.views.common :as common]
            [cljs-react-material-ui.reagent :as ui]
            [cljs-react-material-ui.core :as cui]
            [re-frame.core :as rf]
            [cljs-time.format :as fmt]))

(def time-fmt (fmt/formatter "yyyy-MM-dd HH:MM:ss"))

(defn notifs-elem
  [{:keys [id title content time tags]}]
  [ui/list-item
   [:div.notif__elem
    [ui/mui-theme-provider
     {:mui-theme (cui/get-mui-theme
                   {:palette {:text-color (cui/color :grey600)}})}
     [:span.notif__time (fmt/unparse time-fmt time)]]
    ": "
    [:span.notif__title title]]])

(defn notifs-page
  "The main notifications page"
  []
  [:div.notifs-page
   [common/appbar]
   [common/nav-drawer]
   [ui/list
    (for [notif @(rf/subscribe [:notifs.all])]
      ^{:key (:id notif)}
      [notifs-elem notif])]])