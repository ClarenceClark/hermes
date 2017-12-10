(ns hermes.views.common
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as rf]))

(defn naventry
  [curpage icon name path page]
  [ui/list-item {:left-icon (icon)
                 ;:disabled (= curpage page)
                 :on-click #(rf/dispatch [:ui.set-current-page page])}
   name])

(defn nav-drawer
  "The main nav drawer, linking to notifs, tags, and settings"
  []
  (let [curpage (rf/subscribe [:ui.current-page])
        open? (rf/subscribe [:ui.navdrawer-open?])]
    [:div.nav-drawer.noselect
     [ui/drawer {:open @open?
                 :on-request-change #(rf/dispatch [:ui.set-navdrawer-open? %])
                 :docked false}
      [ui/list-item {:disabled true
                     :class-name "nav-label"
                     :style {:font-size "28px"
                             :margin "36px 18px 24px"}}
       "Hermes"]
      [naventry @curpage ic/action-toc
       "Notifications" "/#/notifications" :notifications]
      [naventry @curpage ic/action-loyalty
       "Tags" "/#/tags" :tags]
      [naventry @curpage ic/action-settings
       "Settings " "/#/settings" :settings]]]))

(defn appbar
  "Appbar for the app. Does not hame theming."
  []
  [ui/app-bar {:title "Hermes"
               :on-left-icon-button-touch-tap
               #(rf/dispatch [:ui.toggle-navdrawer-open?])}])