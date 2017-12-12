(ns hermes.subevts.network
  (:require [re-frame.core :as rf]
            [ajax.core :as ajax]))

(def api-base "http://localhost:3000/v1")
(def auth
  (js/btoa
    "testing1:847aa22d8737c024bbee7df1e8d47004e19380b5392eea9083176f87b69bf2cdc9d87e7492c7dce553a2a9fac86ae3bfbf79e40454e5ee228346c9d28c22e88c"))

(defn encode-login-info [username password]
  (js/btoa (str username ":" password)))

(defn get-auth-from-db [db]
  (let [username (get-in db [:login :username])
        apikey (get-in db [:login :apikey])]
    (encode-login-info username apikey)))

(def default-xhr-opts
  {:keywords? true
   :format (ajax/json-request-format)
   :response-format (ajax/json-response-format)})

(defn merge-defaults [db req]
  (merge
    {"Authorization" (str "Basic " (get-auth-from-db db))}
    default-xhr-opts
    req))

(rf/reg-event-fx
  :network.fetch-notifs
  (fn [cofx _]
    {:dispatch [:ui.set-fetching-notifs? true]
     :http-xhrio
     (merge default-xhr-opts
            {:method :get
             :uri (str api-base "/notifications")
             :params {:after 0}
             :on-success [:notifs.fetch-success]
             :on-failure [:notifs.fetch-failure]})}))

(rf/reg-event-fx
  :network.fetch-tags
  (fn [{:keys [db]} _]
    {:dispatch [:ui.set-fetching-tags? true]
     :http-xhrio
     (merge default-xhr-opts
            {:method :get
             :uri (str api-base "/tags")})}))


(rf/reg-event-fx
  :network.add-tag
  (fn [{:keys [db]} [_ id tagname]]
    {:dispatch [:ui.set-adding-tag? true]
     :http-xhrio
     (merge default-xhr-opts
            {:method :post
             :uri (str api-base "/tags/" id)
             :params {:name tagname}
             :on-success [:tags.add-success]
             :on-failure [:tags.add-fail]})}))

(rf/reg-event-fx
  :network.edit-tag
  (fn [_ [_ id tagname]]
    {:dispatch [:ui.set-editing-tag? true]
     :http-xhrio
     (merge default-xhr-opts
            {:method :put
             :uri (str api-base "/tags/" id)
             :params {:name tagname}
             :on-success [:tags.edit-success]
             :on-failure [:tags.edit-fail]})}))

(rf/reg-event-fx
  :login
  (fn [_ [_ email password]]
    {:dispatch [:ui.set-logging-in? true]
     :http-xhrio
     (merge default-xhr-opts
            {:method :get
             :uri {"Authorization"
                   (str "Basic " (encode-login-info email password))}
             :on-success [:login.succes]
             :on-fail [:login.fail]})}))