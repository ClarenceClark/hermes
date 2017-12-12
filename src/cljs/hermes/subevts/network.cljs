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
    {:headers {"Authorization" (str "Basic " (get-auth-from-db db))}}
    default-xhr-opts
    req))