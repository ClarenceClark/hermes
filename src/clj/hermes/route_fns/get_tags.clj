(ns hermes.route-fns.get-tags
  (:require [ring.util.http-response :as resp]
            [hermes.db.core :as qu]))

(defn get-all-tags-resp
  "Get all tags for the user"
  [userinfo]
  (resp/ok (qu/get-all-tags-for-user {:userid (:id userinfo)})))

(defn get-tag-by-id-resp
  [userinfo easyid]
  (let [tag (qu/get-tag-by-id {:userid (:id userinfo)
                               :easyid easyid})]
    (if (empty? tag)
      (resp/not-found {:error "Tag not found"})
      (resp/ok tag))))
