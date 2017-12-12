(ns hermes.subevts.tags
  (:require [hermes.subevts.utils :as util]
            [re-frame.core :as rf]
            [hermes.subevts.utils :as utils]
            [hermes.subevts.network :as nt]))

(util/reg-keyword-diff-sub :tags.map :tags)
(def tags-int [(rf/path :tags) rf/trim-v])

(rf/reg-sub
  :tags.all
  :<- [:tags.map]
  (fn [tags-map sub dyn]
    (vals tags-map)))

(rf/reg-event-db
  :tags.set-by-id
  [(rf/path :tags) rf/trim-v]
  (fn [tags-map [id name]]
    (assoc tags-map id name)))

(rf/reg-event-db
  :tags.ins-one
  [tags-int]
  (fn [tags-map [{:keys [id name] :as tag}]]
    (println id name)
    (assoc tags-map id tag)))

(rf/reg-event-db
  :tags.ins-multi
  [tags-int]
  (fn [tags-map [tags]]
    (let [tuples (map (fn [n] [(:id n) n]) tags)
          new (into {} tuples)]
      (merge tags-map new))))

(rf/reg-event-fx
  :tags.edit
  (fn [cofx _]
    (let [db (:db cofx)
          tag (-> db
                  (get-in [:ui :tag-ed])
                  (select-keys [:name :active-id]))
          _ (println tag)]
      {:dispatch [:ui.set-snackbar-msg "Editing tag ..."]
       :http-xhrio
       (nt/merge-defaults db
         {:method :put
          :uri (str nt/api-base "/tags/" (:active-id tag))
          :params (select-keys tag [:name])
          :on-success [:tags.edit-sucess]
          :on-failure [:tags.edit-fail]})})))

(rf/reg-event-fx
  :tags.new
  (fn [cofx _]
    (let [db (:db cofx)
          tag (-> db
                  (get-in [:ui :tag-add])
                  (select-keys [:name]))]
      {:dispatch [:ui.set-snackbar-msg "Adding new tag ..."]
       :http-xhrio
       (nt/merge-defaults db
          {:method :post
           :uri (str nt/api-base "/tags/" (:id tag))
           :params tag
           :on-success [:tags.new-success]
           :on-failure [:tags.new-fail]})})))

(rf/reg-event-fx
  :tags.retrieve
  (fn [{:keys [db]} _]
    (println "Retrieving tags")
    {:dispatch [:ui.set-snackbar-msg "Retrieving tags ..."]
     :http-xhrio
     (nt/merge-defaults db
        {:method :get
         :uri (str nt/api-base "/tags")
         :params {}
         :on-success [:tags.fetch-success]
         :on-failure [:tags.fetch-failure]})}))

(rf/reg-event-fx
  :tags.new-success
  (fn [cofx [_ notif]]
    {:dispatch-n [[:tags.ins-one notif]
                  [:ui.set-snackbar-msg "Tag created successfully"]
                  [:ui.tag-add.set-show false]
                  [:tags.retrieve]]}))

(rf/reg-event-fx
  :tags.new-fail
  (fn [cofx [_ fail]]
    {:dispatch-n [[:ui.set-snackbar-msg
                   (str "Tag failed to create" (:error fail))]]}))

(rf/reg-event-fx
  :tags.fetch-success
  (fn [cofx [_ notifs]]
    {:dispatch-n [[:tags.ins-multi notifs]
                  [:ui.set-snackbar-msg "Tags retrieved successfully"]]}))

(rf/reg-event-fx
  :tags.fetch-failure
  (fn [cofx [_ fail]]
    {:dispatch-n [[:ui.set-snackbar-msg
                   (str "Failed to retrieve tags" (:error fail))]]}))

(rf/reg-event-fx
  :tags.edit-sucess
  (fn [cofx [_ notif]]
    {:dispatch-n [[:tags.ins-one notif]
                  [:ui.tag-ed.set-active-id :none]
                  [:ui.set-snackbar-msg "Tag edited successfully"]]}))

(rf/reg-event-fx
  :tags.edit-fail
  (fn [cofx [_ fail]]
    {:dispatch-n [[:ui.set-snackbar-msg
                   (str "Failed to edit tag" (:error fail))]]}))