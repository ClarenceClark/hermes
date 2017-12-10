(ns hermes.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [secretary.core :as sc]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [ajax.core :refer [GET POST]]
            [hermes.ajax :refer [load-interceptors!]]
            [hermes.views.core :as views]
            [hermes.events])
  (:import goog.History))

(defn home-page []
  [:div.container
   (when-let [docs @(rf/subscribe [:docs])]
     [:div.row>div.col-sm-12
      [:div {:dangerouslySetInnerHTML
             {:__html (md->html docs)}}]])])

(def pages
  {:home #'home-page})

(defn page []
  [(pages @(rf/subscribe [:page]))])

;; -------------------------
;; Routes
(sc/set-config! :prefix "/#")

(sc/defroute "/" []
             (rf/dispatch [:set-active-page :home]))
(sc/defroute "/about" []
             (rf/dispatch [:set-active-page :about]))
(sc/defroute "/notifications" []
             (rf/dispatch [:set-active-page :notifications]))
(sc/defroute "/tags" []
             (rf/dispatch [:set-active-page :tags]))
(sc/defroute "/settings" []
             (rf/dispatch [:set-active-page :settings]))
(sc/defroute "/login" []
             (rf/dispatch [:set-active-page :login]))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
      HistoryEventType/NAVIGATE
      (fn [event]
        (sc/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn fetch-docs! []
  (GET "/docs" {:handler #(rf/dispatch [:set-docs %])}))

(defn mount-components []
  (rf/clear-subscription-cache!)
  (r/render [views/main-all]
            (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [:initialize-db])
  (load-interceptors!)
  (fetch-docs!)
  (hook-browser-navigation!)
  (mount-components))
