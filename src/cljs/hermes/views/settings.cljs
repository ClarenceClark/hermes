(ns hermes.views.settings
  (:require [hermes.views.common :as common]
            [cljs-react-material-ui.reagent :as ui]
            [reagent.core :as r]))

(defn settings-page []
  [:div.settings-page.page
   [common/appbar]
   [common/nav-drawer]
   [:div.page__container
    [ui/paper {:z-depth 1
               :class-name "page__fl-paper"}
     [:div.page__content
      [:div.page__label "Settings"]
      [ui/list
       [ui/list-item {:right-toggle
                      (r/as-element [ui/toggle])}
        "No idea what to do with this"]]]]]])