(ns hermes.views.tags
  (:require [hermes.views.common :as common]
            [cljs-react-material-ui.reagent :as ui]
            [cljs-react-material-ui.icons :as ic]
            [re-frame.core :as rf]))

(defn tag-elem [tag]
  [ui/table-row
   [ui/table-row-column (:id tag)]
   [ui/table-row-column
    {:style {:width "100%"}}
    (:name tag)]
   [ui/table-row-column (ic/editor-mode-edit)]])

(defn tags-page
  "Tags page"
  []
  [:div.tags-page.page
   [common/appbar]
   [common/nav-drawer]
   [:div.page__container
    [ui/paper {:z-depth 2
               :class-name "page__fl-paper"}
     [:div.page__content
      [:div.page__label "Tags"]
      [ui/icon-button {:class-name "add-tag"}
       (ic/content-add)]
      [ui/table {:selectable false
                 :multi-selectable false
                 :fixed-header false
                 :show-row-hover true
                 :style {:table-layout "auto"
                         :width "auto"}}
       [ui/table-header {:display-select-all false
                         :adjust-for-checkbox false}
        [ui/table-header-column "Tag ID"]
        [ui/table-header-column "Tag Name"]
        [ui/table-header-column "Edit"]]
       [ui/table-body {:display-row-checkbox false}
        (for [tag @(rf/subscribe [:tags.all])]
          ^{:key (:id tag)}
          [tag-elem tag])]]]]]])