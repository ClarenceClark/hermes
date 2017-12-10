(ns hermes.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [secretary.core :as sc]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [ajax.core :refer [GET POST]]
            [re-frisk.core :as re-frisk]

            [hermes.ajax :refer [load-interceptors!]]
            [hermes.views.core :as views]

            ; Load subs and events
            [hermes.subevts.core]
            [hermes.subevts.notifs]
            [hermes.subevts.tags]
            [hermes.subevts.ui]
            [hermes.subevts.utils])

  (:import goog.History))

;; -------------------------
;; Routes
(sc/set-config! :prefix "/#")

(sc/defroute "/" []
             (rf/dispatch [:ui.set-current-page :home]))
(sc/defroute "/docs" []
             (rf/dispatch [:ui.set-current-page :docs]))
(sc/defroute "/about" []
             (rf/dispatch [:ui.set-current-page :about]))
(sc/defroute "/notifications" []
             (rf/dispatch [:ui.set-current-page :notifications]))
(sc/defroute "/tags" []
             (rf/dispatch [:ui.set-current-page :tags]))
(sc/defroute "/settings" []
             (rf/dispatch [:ui.set-current-page :settings]))
(sc/defroute "/login" []
             (rf/dispatch [:ui.set-current-page :login]))

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

(def debug? goog.DEBUG)

(defn init! []
  (rf/dispatch-sync [:init-db])
  (when debug?
    (enable-console-print!)
    (re-frisk/enable-re-frisk!))
  (load-interceptors!)
  (fetch-docs!)
  (hook-browser-navigation!)
  (mount-components))
