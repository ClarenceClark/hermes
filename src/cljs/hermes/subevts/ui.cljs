(ns hermes.subevts.ui
  (:require [re-frame.core :as rf]
            [hermes.subevts.utils :as evutil]))

(evutil/reg-subevt-pair
  :ui.navdrawer-open? :ui.set-navdrawer-open? [:ui :navdrawer-open?])
(evutil/reg-bool-toggle-evt :ui.toggle-navdrawer-open? [:ui :navdrawer-open?])

(evutil/reg-subevt-pair
  :ui.current-page :ui.set-current-page
  [:ui :current-page])

(evutil/reg-subevt-pair
  :ui.filter-tags :ui.set-filter-tags
  [:ui :filter-tags])

(evutil/reg-subevt-pair
  :ui.create-notif.show? :ui.create-notif.set-show?
  [:ui :create-notif :show?])

(evutil/reg-subevt-pair
  :ui.create-notif.title :ui.create-notif.set-title
  [:ui :create-notif :title])

(evutil/reg-subevt-pair
  :ui.create-notif.content :ui.create-notif.set-content
  [:ui :create-notif :content])

(evutil/reg-subevt-pair
  :ui.create-notif.tags :ui.create-notif.set-tags
  [:ui :create-notif :tags])

(evutil/reg-subevt-pair
  :ui.active-notif :ui.set-active-notif
  [:ui :active-notif])

(rf/reg-event-fx
  :ui.create-notif.cancel
  (fn [cofx _]
    {:dispatch-n [[:ui.create-notif.set-tags []]
                  [:ui.create-notif.set-title ""]
                  [:ui.create-notif.set-content ""]
                  [:ui.create-notif.set-show? false]]}))