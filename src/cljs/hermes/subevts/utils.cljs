(ns hermes.subevts.utils
  (:require [re-frame.core :as rf]))

(defn reg-assoc-evt
  "Registers an event with name `evtname` that assocs the provided value to
   the path `path`."
  [evtname path]
  (rf/reg-event-db
    evtname
    (fn [db [ev newval]]
      (assoc-in db path newval))))

(defn reg-keyword-diff-sub
  "Same as `reg-keyword-sub`, except that key1 is the sub name and key2
   is the name of the db field"
  [sub-name dbkey]
  (rf/reg-sub
    sub-name
    (fn [db _]
      (dbkey db))))

(defn reg-keyword-sub
  "For the subscriptions that are a field in the database with the
   same name as the sub"
  [sub-name]
  (reg-keyword-diff-sub sub-name sub-name))

(defn reg-nested-sub
  "Register a subscription that maps the access-key to the given path"
  [sub-name path]
  (rf/reg-sub sub-name
              (fn [db dyn]
                (get-in db path))))

(defn reg-subevt-pair
  "Registers a pair of subs and event that puts/sets a value in the db"
  [subname evname dbpath]
  (do
    (rf/reg-event-db evname
                     (fn [db [_ newval]]
                       (assoc-in db dbpath newval)))
    (rf/reg-sub subname
                (fn [db dyn]
                  (get-in db dbpath)))))

(defn reg-bool-toggle-evt
  "Registers a toggle event for db boolean value"
  [evname dbpath]
  (rf/reg-event-db
    evname
    (fn [db _]
      (let [oldval (get-in db dbpath)]
        (assoc-in db dbpath (not oldval))))))