(ns hermes.views.core
  (:require [re-frame.core :as rf]))

(def pages
  {:home "nada"
   :notifications "temp notifs"
   :tags "temp tags"
   :settings "temp settings"
   :login "temp login"})

(defn main-all []
  [:div.main
   ;[nav/nav-drawer]
   [:div (pages @(rf/subscribe [:page]))]])