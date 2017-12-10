(ns hermes.views.notifs
  (:require [hermes.views.common :as common]
            [cljs-react-material-ui.reagent :as ui]
            [re-frame.core :as rf]))

(defn notifs-elem
  [{:keys [id title content time tags]}]
  [ui/list-item
   [:div title]])

(defn notifs-page
  "The main notifications page"
  []
  (.log js/console (str @(rf/subscribe [:notifs.map])))
  [:div.notifs-page
   [common/appbar]
   [common/nav-drawer]
   [ui/list
    (for [notif @(rf/subscribe [:notifs.all])]
      ^{:key (:id notif)}
      [notifs-elem notif])]])