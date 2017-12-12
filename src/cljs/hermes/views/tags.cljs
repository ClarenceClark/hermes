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
   [ui/table-row-column
    [ui/icon-button {:on-click #(rf/dispatch
                                  [:ui.tag-ed.set-active-id (:id tag)])}
     (ic/editor-mode-edit)]]])

(defn edit-tag-dialog []
  (let [aid (rf/subscribe [:ui.tag-ed.active-id])
        tname (rf/subscribe [:ui.tag-ed.name])
        cancel-fn #(rf/dispatch [:ui.tag-ed.set-active-id :none])
        confirm-fn #(rf/dispatch [:ui.edit-tag @aid @tname])]
    (fn []
      [ui/dialog
       {:modal true
        :title "Edit Tag"
        :open (not= :none @aid)
        :actions (common/dialog-confirm-cancel cancel-fn confirm-fn)}
       [common/disp-text-field "Tag ID" @aid false]
       [ui/text-field {:floating-label-text "Tag Name"
                       :full-width true
                       :on-change (common/text-cev-fn :ui.tag-ed.set-name)
                       :value @tname
                       :error-text @(rf/subscribe [:ui.tag-ed.name-err])}]])))

(defn add-tag-dialog []
  (let [tid (rf/subscribe [:ui.tag-add.id])
        tname (rf/subscribe [:ui.tag-add.name])
        id-err (rf/subscribe [:ui.tag-add.id-error])
        name-err (rf/subscribe [:ui.tag-add.name-error])
        cancel-fn  #(rf/dispatch [:ui.tag-add.set-show false])
        confirm-fn #(rf/dispatch [:tags.add-tag @tid @tname])]
    (fn []
      [ui/dialog
       {:modal true
        :title "Add New Tag"
        :open @(rf/subscribe [:ui.tag-add.show?])
        :actions (common/dialog-confirm-cancel cancel-fn confirm-fn)}
       [ui/text-field {:floating-label-text "Tag ID"
                       :on-change (common/text-cev-fn :ui.tag-add.set-id)
                       :value @tid
                       :error-text @id-err}]
       [:br]
       [ui/text-field {:floating-label-text "Tag Name"
                       :full-width true
                       :on-change (common/text-cev-fn :ui.tag-add.set-name)
                       :value @tname
                       :error-text @name-err}]])))

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
               :class-name "page__fl-paper"
               :style {:min-width "600px"
                       :max-width "720px"}}
     [:div.page__content

      [add-tag-dialog]
      [edit-tag-dialog]

      ; Label and add tag btn
      [:div.page__head
       [:div.page__label "Tags"]
       [ui/raised-button
        {:label "Add tag"
         :class-name "page__add-btn"
         :primary true
         :on-click #(rf/dispatch [:ui.tag-add.set-show true])
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