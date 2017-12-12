(ns hermes.views.common
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as rf]
    [reagent.core :as r]))

(defn- naventry-container
  [path]
  [:a {:href path}])

(defn naventry
  [curpage icon name path page]
  [ui/list-item {:left-icon (icon)
                 :href path
                 ; Close nav when a path is clicked
                 ; Secretary will route to the correct page, we don't
                 ; need to handle that here
                 :on-click #(rf/dispatch [:ui.toggle-navdrawer-open?])}
   name])

(defn appbar
  "Appbar for the app. Does not have theming."
  [args]
  (let [default {:title "Hermes"
                 :on-left-icon-button-touch-tap
                 #(rf/dispatch [:ui.toggle-navdrawer-open?])}]
    [ui/app-bar (merge args default)]))

(defn nav-drawer
  "The main nav drawer, linking to notifs, tags, and settings"
  []
  (let [curpage (rf/subscribe [:ui.current-page])
        open? (rf/subscribe [:ui.navdrawer-open?])]
    [:div.nav-drawer.noselect
     [ui/drawer {:open @open?
                 :on-request-change #(rf/dispatch [:ui.set-navdrawer-open? %])
                 :docked false}
      [appbar]
      [naventry @curpage ic/action-toc
       "Notifications" "/#/notifications" :notifications]
      [naventry @curpage ic/action-loyalty
       "Tags" "/#/tags" :tags]
      [naventry @curpage ic/action-settings
       "Settings " "/#/settings" :settings]]]))

(defn snackbar []
  [ui/snackbar
   {:open (not (empty? @(rf/subscribe [:ui.snackbar-msg])))
    :auto-hide-duration 3000
    :on-request-close #(rf/dispatch [:ui.set-snackbar-msg ""])
    :message @(rf/subscribe [:ui.snackbar-msg])}])

(defn text-cev-fn [event]
  #(rf/dispatch [event (.-value (.-target %))]))

(defn text-catom-fn [atom]
  #(reset! atom (.-value (.-target %))))

(defn dialog-confirm-cancel [cancel-fn confirm-fn]
  [(r/as-element [ui/flat-button {:label "Cancel"
                                  :on-click cancel-fn}])
   (r/as-element [ui/flat-button {:label "Confirm"
                                  :primary true
                                  :on-click confirm-fn}])])

(defn disp-text-field [label value full-width?]
  [ui/text-field {:floating-label-text label
                  :full-width full-width?
                  :value value}])