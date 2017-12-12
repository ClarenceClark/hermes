(ns hermes.views.core
  (:require [cljsjs.material-ui]
            [cljs-react-material-ui.core :as mui]
            [cljs-react-material-ui.reagent :as ui]
            [re-frame.core :as rf]
            [hermes.views.common :as common]
            [hermes.views.notifs :as notifs]
            [hermes.views.tags :as tags]
            [hermes.views.settings :as settings]
            [hermes.views.login :as login]
            [reagent.core :as r]
            [cljs-react-material-ui.icons :as ic]))

(def pages
  {:home login/login-page
   :notifications notifs/notifs-page
   :tags tags/tags-page
   :settings settings/settings-page
   :login login/login-page})

(defn main-all []
  [ui/mui-theme-provider {:mui-theme (mui/get-mui-theme)}
   [:div.application
    [(pages @(rf/subscribe [:ui.current-page]))]]])