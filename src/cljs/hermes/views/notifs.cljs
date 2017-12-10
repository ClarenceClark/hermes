(ns hermes.views.notifs
  (:require [hermes.views.common :as common]))

(defn notifs-page
  "The main notifications page"
  []
  [:div.notifs-page
   [common/appbar]
   [common/nav-drawer]])
