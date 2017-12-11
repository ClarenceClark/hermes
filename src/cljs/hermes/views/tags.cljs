(ns hermes.views.tags
  (:require [hermes.views.common :as common]
            [cljs-react-material-ui.core :as uic]
            [cljs-react-material-ui.reagent :as ui]
            [cljs-react-material-ui.icons :as ic]
            [re-frame.core :as rf]
            [reagent.core :as r]))

(defn tag-elem [tag]
  [ui/table-row
   [ui/table-row-column (:id tag)]
   [ui/table-row-column
    {:style {:width "100%"}} ; Expand to fill available space
    (:name tag)]
   [ui/table-row-column (ic/editor-mode-edit)]])

(defn edit-tag-dialog [tag edit?]
  (let [newname (r/atom "")
        cancel-fn
        #(rf/dispatch [:ui.set-tag-add-edit-state :none])
        confirm-fn
        #(rf/dispatch [:tags.edit-tag (:id @tag) newname])]
    [ui/dialog
     {:modal true
      :open (not= :none [:ui.tag-add-edit-state])
      :actions
      [[uic/flat-button
        {:label "Cancel"
         :primary false
         :on-click cancel-fn}]
       [uic/flat-button
        {:label "Confirm"
         :primary true
         :on-click confirm-fn}]]}
     [:div "hello"]]))

(defn tags-page
  "Tags page"
  []
  [:div.tags-page.page

   ; Appbar and nav
   [common/appbar]
   [common/nav-drawer]

   ; Actual page
   [:div.page__container
    [ui/paper {:z-depth 1
               :class-name "page__fl-paper"}
     [:div.page__content

      ; Label and add tag btn
      [:div.page__head
       [:div.page__label "Tags"]
       [ui/raised-button
        {:label "Add tag"
         :class-name "page__add-btn"
         :primary true
         :icon (ic/content-add)}]]

      ; Table showing all tags
      [ui/table {:selectable false
                 :multi-selectable false
                 :fixed-header false
                 :show-row-hover true
                 :style {:table-layout "auto"
                         :width "auto"}}

       ; Table header
       [ui/table-header {:display-select-all false
                         :adjust-for-checkbox false}
        [ui/table-header-column "Tag ID"]
        [ui/table-header-column "Tag Name"]
        [ui/table-header-column "Edit"]]

       ; Table body
       [ui/table-body {:display-row-checkbox false}
        (for [tag @(rf/subscribe [:tags.all])]
          ^{:key (:id tag)}
          [tag-elem tag])]]]]]])