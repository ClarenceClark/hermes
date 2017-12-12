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

(defn tags-icon-select [icon get-ev set-ev]
  [ui/icon-menu
   {:icon-button-element
    (r/as-element [ui/icon-button icon])
    :multiple true
    :value (clj->js @(rf/subscribe [get-ev]))
    :touch-tap-close-delay 0
    :on-change #(rf/dispatch [set-ev (js->clj %2)])}

   (for [tag @(rf/subscribe [:tags.all])]
     ^{:key (:id tag)}
     [ui/menu-item {:primary-text (:name tag)
                    :value (:id tag)}])])

(defn notifs-elem
  [{:keys [id title content time tags] :as tag}]
  [ui/list-item {:on-click #(rf/dispatch [:ui.set-active-notif tag])}
   [:div.notif__elem.noselect
    [:span.notif__time
     (fmt/unparse time-fmt time)]
    ": "
    [:span.notif__title title]]])

(defn notif-detail-dialog
  "Dialog showing notif details"
  []
  (let [active-notif (rf/subscribe [:ui.active-notif])
        close-notif-fn #(rf/dispatch [:ui.set-active-notif :none])]
    [ui/dialog
     {:actions (r/as-element [ui/flat-button {:label "Close"
                                              :on-click close-notif-fn}])
      :title "Notification Details"
      :modal false
      :open (not= :none @active-notif)
      :on-request-close close-notif-fn}
     [cm/disp-text-field "Title" (:title @active-notif) true]
     [:br]
     [cm/disp-text-field "Time"
      (if (not= @active-notif :none)
        (fmt/unparse time-fmt (or (:time @active-notif) (time/now)))
        "None")]
     [:br]
     [cm/disp-text-field "Tags" (clj->js (:tags @active-notif))]
     [:br]
     [cm/disp-text-field "Content" (:content @active-notif) true]]))

(defn- notif-create-dialog []
  (let [ras r/as-element
        cancel-fn #(rf/dispatch [:ui.create-notif.cancel])
        send-fn #(rf/dispatch [:notifs.send])]
    [ui/dialog
     {:actions (cm/dialog-confirm-cancel cancel-fn send-fn)
      :title "Send New Notification"
      :modal true
      :on-request-close cancel-fn
      :open @(rf/subscribe [:ui.create-notif.show?])}
     [ui/text-field {:floating-label-text "Title"
                     :full-width true
                     :on-change (cm/text-cev-fn :ui.create-notif.set-title)
                     :value @(rf/subscribe [:ui.create-notif.title])}]
     [:br]
     [ui/text-field {:floating-label-text "Content"
                     :full-width true
                     :multi-line true
                     :on-change (cm/text-cev-fn :ui.create-notif.set-content)
                     :value @(rf/subscribe [:ui.create-notif.content])}]
     [:br]
     [tags-icon-select (ic/action-loyalty)
      :ui.create-notif.tags :ui.create-notif.set-tags]]))

(defn- notifs-toolbar []
  [ui/toolbar
   [ui/toolbar-title {:text "Notifications"}]
   [ui/toolbar-group {:last-child true}

    ; Tags filter menu
    [tags-icon-select (ic/content-filter-list)
     :ui.filter-tags :ui.set-filter-tags]

    [ui/raised-button {:label "New Notif"
                       :on-click
                       #(rf/dispatch [:ui.create-notif.set-show? true])}]]])

(defn notifs-page
  "The main notifications page"
  []
  (let [filter-tags (r/atom #js[1 2])]
    [:div.notifs-page.page
     [cm/appbar]
     [cm/nav-drawer]

     ; Toolbar for name, tag filter, and creation button
     [notifs-toolbar]

     ; List of all notifs
     [ui/list {:height "100%"
               :class-name "notifs__list"}
      (for [notif @(rf/subscribe [:notifs.to-show])]
        ^{:key (:id notif)}
        [notifs-elem notif])]

     [notif-create-dialog]
     [notif-detail-dialog]]))