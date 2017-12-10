(ns hermes.subevts.ui
  (:require [re-frame.core :as rf]
            [hermes.subevts.utils :as evutil]))

(evutil/reg-subevt-pair
  :ui.navdrawer-open? :ui.set-navdrawer-open? [:ui :navdrawer-open?])
(evutil/reg-bool-toggle-evt :ui.toggle-navdrawer-open? [:ui :navdrawer-open?])

(evutil/reg-subevt-pair
  :ui.current-page :ui.set-current-page [:ui :current-page])