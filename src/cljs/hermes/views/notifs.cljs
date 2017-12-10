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
    [:span.notif__time (fmt/unparse time-fmt time)]
    ": "
    [:span.notif__title title]]])

(defn notif-detail-dialog
  "Dialog showing notif details"
  []
  (let [active-notif (rf/subscribe [:ui.active-notif])
        should-show? (rf/subscribe [:ui.show-notif-detail?])
        close-notif-fn #(rf/dispatch [:ui.set-show-notif-detail? false])]
    [ui/dialog
     {:actions (ui/flat-button
                 {:label "Close"
                  :on-click close-notif-fn})
      :title (:title @active-notif)
      :modal false
      :open @should-show?
      :on-request-close close-notif-fn}
     [:div.notif__content]]))

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
   ;[notif-detail-dialog]])