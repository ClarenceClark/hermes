(ns hermes.views.docs
  (:require [re-frame.core :as rf]
            [markdown.core :refer [md->html]]))

(defn docs-page []
  [:div.container
   (when-let [docs @(rf/subscribe [:docs])]
     [:div.row>div.col-sm-12
      [:div {:dangerouslySetInnerHTML
             {:__html (md->html docs)}}]])])