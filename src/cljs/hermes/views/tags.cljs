(ns hermes.views.tags
  (:require [hermes.views.common :as common]))

(defn tags-page
  "Tags page"
  []
  [:div.tags-page
   [common/appbar]
   [common/nav-drawer]
   [:div.tags-list {:style {:margin "24px 48px"}}]])

