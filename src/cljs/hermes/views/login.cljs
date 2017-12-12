(ns hermes.views.login
  (:require [cljs-react-material-ui.reagent :as ui]
            [reagent.core :as r]
            [hermes.views.common :as cm]
            [re-frame.core :as rf]))

(defn login-page []
  (let [email (r/atom "")
        password (r/atom "")
        login-error (rf/subscribe [:ui.login-error])]
    (fn []
      [:div.login.page
       [ui/paper {:z-depth 1
                  :class-name "login__container"}
        [ui/app-bar {:title "Login to Hermes"
                     :show-menu-icon-button false}]
        [:div.login__content
         [:div {:class (str "login__error " (when-not @login-error "hide"))}
          @login-error]
         [ui/text-field {:floating-label-text "Email"
                         :value @email
                         :on-change (cm/text-catom-fn email)}]
         [:br]
         [ui/text-field {:floating-label-text "Password"
                         :value @password
                         :on-change (cm/text-catom-fn password)}]
         [:br]]
        [ui/raised-button {:label "Login"
                           :class-name "login__button"
                           :primary true
                           :on-click #(rf/dispatch [:login @email @password])}]]])))