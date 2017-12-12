(ns hermes.subevts.login
  (:require [re-frame.core :as rf]
            [hermes.subevts.network :as nt]
            [hermes.subevts.utils :as utils]
            [secretary.core :as secretary]))

(utils/reg-subevt-pair :login.username :login.set-username [:login :username])
(utils/reg-subevt-pair :login.apikey :login.set-apikey [:login :apikey])

(rf/reg-event-fx
  :login
  (fn [{:keys [db]} [_ user pass]]
    {:dispatch [:ui.set-snackbar-msg "Logging in ..."]
     :http-xhrio
     (nt/merge-defaults db
       {:method :get
        :uri (str nt/api-base "/user/" user)
        :headers {"Authorization" (nt/get-auth-from-info user pass)}
        :on-success [:login.success]
        :on-failure [:login.fail]})}))

(rf/reg-event-fx
  :signup
  (fn [{:keys [db]} [_ user pass]]
    {:dispatch [:ui.set-snackbar-msg "Signing up"]
     :http-xhrio
     (nt/merge-defaults db
        {:method :post
         :uri (str nt/api-base "/user")
         :headers {"Authorization" (nt/get-auth-from-info user pass)}
         :on-success [:login.success]
         :on-failure [:login.fail]})}))

(rf/reg-event-fx
  :login.success
  (fn [_ [_ {:keys [email apikey]}]]
    {:dispatch-n [[:ui.set-snackbar-msg "Login success!"]
                  [:login.set-username email]
                  [:login.set-apikey apikey]
                  [:notifs.retrieve]
                  [:tags.retrieve]]
     :navigate "/notifications"}))

(rf/reg-event-fx
  :login.fail
  (fn [_ _]
    {:dispatch-n [[:ui.set-snackbar-msg "Login failed!"]
                  [:ui.set-login-error "Invalid username/password"]]}))

(rf/reg-fx
  :navigate
  (fn [path]
    (secretary/dispatch! path)))