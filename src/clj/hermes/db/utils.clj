(ns hermes.db.utils
  (:require [hermes.db.core :as qu]
            [clj-time.format :as fmt]))

(def iso8601 (fmt/formatters :basic-date-time))

(defn dbtag->user
  "Transforms a tag returned by the db into one that the server returns."
  [{:keys [userid easyid name]}]
  {:id easyid
   :name name})

(defn dbnotif->user
  "Transform an internal notif, as returned by the db, to a user-facing one."
  [{:keys [id userid title content sendtime] :as dbnotif}]
  (let [base {:id id
              :title title
              :content content
              :time (fmt/unparse iso8601 sendtime)}]
    (if (contains? dbnotif :tags)
      (-> base
          (assoc :tags (:tags dbnotif)))
      (let [tags (qu/get-tags-for-notif {:notifid id})
            easyids (map :easyid tags)]
        (-> base
            (assoc :tags easyids))))))

(defn dbtaglist->user
  "Transforms a list of internal tags to user-facing versions"
  [dbtags]
  (map dbtag->user dbtags))

(defn dbnotiflist->user
  "Transforms a list of internal notifs to the user-facing version"
  [dbnotifs]
  (map dbnotif->user dbnotifs))

(defn get-db-tag
  "Convenience function for getting a tag out of the db"
  [userid easyid name]
  (if easyid
    (qu/get-tag-by-id {:userid userid
                       :easyid easyid})
    (qu/get-tag-by-name {:userid userid
                         :naem name})))