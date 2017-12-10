(ns hermes.views.core
  (:require [cljsjs.material-ui]
            [cljs-react-material-ui.core :as mui]
            [cljs-react-material-ui.reagent :as ui]
            [re-frame.core :as rf]
            [hermes.views.common :as nav]
            [hermes.views.notifs :as notifs]
            [reagent.core :as r]
            [cljs-react-material-ui.icons :as ic]))

(def pages
  {:home notifs/notifs-page
   :notifications notifs/notifs-page
   :tags "temp tags"
   :settings "temp settings"
   :login "temp login"})

;(defn home-page []
;  [ui/mui-theme-provider
;   {:mui-theme (get-mui-theme
;                 {:palette {:text-color (color :green600)}})}
;   [:div
;    [ui/app-bar {:title "Title"
;                 :icon-element-right
;                 (r/as-element [ui/icon-button
;                                (ic/action-account-balance-wallet)])}]
;    [ui/paper
;     [:div "Hello"]
;     [ui/mui-theme-provider
;      {:mui-theme (get-mui-theme {:palette {:text-color (color :blue200)}})}
;      [ui/raised-button {:label "Blue button"}]]
;     (ic/action-home {:color (color :grey600)})
;     [ui/raised-button {:label        "Click me"
;                        :icon         (ic/social-group)
;                        :on-click     #(println "clicked")}]]]])

(defn main-all []
  [ui/mui-theme-provider {:mui-theme (mui/get-mui-theme)}
   [:div.application
    [notifs/notifs-page]]])