(ns hermes.views.notifs
  (:require [hermes.views.common :as cm]
            [cljs-react-material-ui.reagent :as ui]
            [cljs-react-material-ui.core :as cui]
            [cljs-react-material-ui.icons :as ic]
            [re-frame.core :as rf]
            [cljs-time.format :as fmt]
            [reagent.core :as r]
            [cljs-time.core :as time]))

(def time-fmt (fmt/formatter "yyyy-MM-dd HH:MM:ss"))

(defn textfield-cfn [ev]
  #(rf/dispatch [ev (.-value (.-target %))]))

(defn notifs-elem
  [{:keys [id title content time tags]}]
  [ui/list-item
   [:div.notif__elem.noselect
    [:span.notif__time
     (fmt/unparse time-fmt time)]
    ": "
    [:span.notif__title title]]])

(defn disp-text-field [label value]
  [ui/text-field {:floating-label-text label
                  :full-width true
                  :value value}])

(defn notif-detail-dialog
  "Dialog showing notif details"
  []
  (let [active-notif (rf/subscribe [:ui.active-notif])
        close-notif-fn #(rf/dispatch [:ui.create-notif.cancel])]
    [ui/dialog
     {:actions (r/as-element [ui/flat-button {:label "Close"
                                              :on-click close-notif-fn}])
      :title "Notification Details"
      :modal false
      :open (not= :none @active-notif)
      :on-request-close close-notif-fn}
     [disp-text-field "Title" (:title @active-notif)]
     [disp-text-field "Time"
      (if (not= @active-notif :none)
        (fmt/unparse time-fmt (or (:time @active-notif) (time/now)))
        "None")]
     [disp-text-field "Tags" (clj->js (:tags @active-notif))]
     [disp-text-field "Content" (:content @active-notif)]]))

(defn- notif-create-dialog []
  (let [ras r/as-element
        cancel-fn #(rf/dispatch [:ui.create-notif.cancel])
        send-fn #(rf/dispatch [:notifs.send])]
    [ui/dialog
     {:actions (cm/dialog-confirm-cancel-actions cancel-fn send-fn)
      :title "Send New Notification"
      :modal true
      :on-request-close cancel-fn
      :open @(rf/subscribe [:ui.create-notif.show?])}
     [ui/text-field {:floating-label-text "Title"
                     :full-width true
                     :on-change (cm/text-cfn :ui.create-notif.set-title)
                     :value @(rf/subscribe [:ui.create-notif.title])}]
     [:br]
     [ui/text-field {:floating-label-text "Content"
                     :full-width true
                     :multi-line true
                     :on-change (cm/text-cfn :ui.create-notif.set-content)
                     :value @(rf/subscribe [:ui.create-notif.content])}]
     [:br]
     [cm/tags-icon-selet (ic/action-loyalty)
      :ui.create-notif.tags :ui.create-notif.set-tags]]))

(defn- notifs-toolbar []
  [ui/toolbar
   [ui/toolbar-title {:text "Notifications"}]
   [ui/toolbar-group {:last-child true}

    ; Tags filter menu
    [cm/tags-icon-selet (ic/content-filter-list) :ui.filter-tags :ui.set-filter-tags]

    [ui/raised-button {:label "New Notif"
                       :on-click
                       #(rf/dispatch [:ui.create-notif.set-show? true])}]]])

(defn notifs-page
  "The main notifications page"
  []
  (let [filter-tags (r/atom #js[1 2])]
    [:div.notifs-page
     [cm/appbar]
     [cm/nav-drawer]

     ; Toolbar for name, tag filter, and creation button
     [notifs-toolbar]

     ; List of all notifs
     [ui/list
      (for [notif @(rf/subscribe [:notifs.all])]
        ^{:key (:id notif)}
        [notifs-elem notif])]

     [notif-create-dialog]
     [notif-detail-dialog]]))