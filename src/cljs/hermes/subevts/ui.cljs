(ns hermes.subevts.ui
  (:require [re-frame.core :as rf]
            [hermes.subevts.utils :as evutil]))

(evutil/reg-subevt-pairs
  [[:ui.navdrawer-open? :ui.set-navdrawer-open? [:ui :navdrawer-open?]]
   [:ui.current-page :ui.set-current-page [:ui :current-page]]
   [:ui.filter-tags :ui.set-filter-tags [:ui :filter-tags]]

   ; Notif detail
   [:ui.active-notif :ui.set-active-notif [:ui :active-notif]]

   ; Create notifs
   [:ui.create-notif.show? :ui.create-notif.set-show?
    [:ui :create-notif :show?]]
   [:ui.create-notif.title :ui.create-notif.set-title
    [:ui :create-notif :title]]
   [:ui.create-notif.content :ui.create-notif.set-content
    [:ui :create-notif :content]]
   [:ui.create-notif.tags :ui.create-notif.set-tags
    [:ui :create-notif :tags]]

   ; Add tags
   [:ui.tag-add.show? :ui.tag-add.set-show [:ui :tag-add :show?]]
   [:ui.tag-add.id :ui.tag-add.set-id [:ui :tag-add :id]]
   [:ui.tag-add.name :ui.tag-add.set-name [:ui :tag-add :name]]

   ; Edit tags
   [:ui.tag-ed.active-id :ui.tag-ed.set-active-id [:ui :tag-ed :active-id]]
   [:ui.tag-ed.name :ui.tag-ed.set-name [:ui :tag-ed :name]]])

(evutil/reg-bool-toggle-evt :ui.toggle-navdrawer-open? [:ui :navdrawer-open?])

(rf/reg-event-fx
  :ui.create-notif.cancel
  (fn [cofx _]
    {:dispatch-n [[:ui.create-notif.set-tags []]
                  [:ui.create-notif.set-title ""]
                  [:ui.create-notif.set-content ""]
                  [:ui.create-notif.set-show? false]]}))

(rf/reg-sub
  :ui.tag-add.id-error
  :<- [:ui.tag-add.id]
  (fn [id _ _]
    (cond
      (js/isNaN id) "Tag ID must be a number"
      (empty? id) "Tag ID must not be empty"
      :else nil)))

(rf/reg-sub
  :ui.tag-add.name-error
  :<- [:ui.tag-add.name]
  (fn [name _ _]
    (cond
      (empty? name) "Tag Name must not be empty"
      :else nil)))

(rf/reg-sub
  :ui.tag-ed.name-err
  :<- [:ui.tag-ed.name]
  (fn [name _ _]
    (cond
      (empty? name) "Tag Name must not be empty"
      :else nil)))