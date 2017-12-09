(ns hermes.route-fns.modify-tags
  (:require [hermes.route-fns.create-tag :refer :all]
            [hermes.db.utils :as dbutil]
            [hermes.db.core :as qu]
            [ring.util.http-response :as resp]))

(defn update-tag-resp
  [userinfo easyid newname]
  (let [userid (:id userinfo)
        tag (dbutil/get-db-tag userid easyid nil)]
    (if (empty? tag)
      (create-tag userid easyid newname)
      (resp/ok (-> (qu/update-tag! {:userid userid
                                    :easyid easyid
                                    :name newname})
                   (dbutil/dbtag->user))))))

(defn delete-tag
  [userid easyid]
  (let [n (qu/delete-tag! {:userid userid
                           :easyid easyid})]
    (resp/ok {:message "Tag deleted successfully"})
    (resp/internal-server-error
      {:error "Something bad happenend and the tag was not deleted"})))

(defn delete-tag-resp
  [userinfo easyid]
  (let [userid (:id userinfo)
        tag (dbutil/get-db-tag userid easyid nil)]
    (if (empty? tag)
      (resp/not-found {:error "Tag with the provided id not found"})
      (delete-tag userid easyid))))