(ns hermes.views.nav
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as rf]))

(defn nav-drawer
  "The main nav drawer, linking to notifs, tags, and settings"
  []
  [:div.nav-drawer
   [ui/drawer {:open @(rf/subscribe [:navdrawer-open?])}
              :docked false
    [:div "abc"]
    [:div "def"]]])