(ns ers.pages.home
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [ers.util :as util]
            [re-frame.core :refer [dispatch subscribe]]))


(defn recommend-button
  []
  (let [items                     (subscribe [:items/list-items])
        user-profile              (subscribe [:user/profile])
        recommendation-available? (reaction (seq @user-profile))]
    (fn []
        [:div
         [:button.btn.btn-primary {:on-click (util/event-handler
                                               (fn [e]
                                                 (dispatch [:items/update-scores @user-profile @items])
                                                 (dispatch [:items/remove-from-recommendations])
                                                 (dispatch [:page/change-panel :recommendations])))}
          "VIEW RECOMMENDATIONS"]]
      )))


(defn search []
  (when-let [input (subscribe [:input/search])]
    (fn []
      [:div.row
       [:div.col-lg-6
        [:div.input-group
         [:input.form-control
          {:on-change   #(dispatch [:input/update-search (-> % .-target .-value)])
           :type        "text"
           :value       @input
           :placeholder "search"}]
         [:span.input-group-btn
          [:button.btn.btn-primary
           {:type     "submit"
            :on-click (util/event-handler
                        (fn [e]
                          (dispatch [:page/change-panel :search])
                          (dispatch [:items/search @input])))}
           "SEARCH"]]]]])))


(defn item-info
  [title k]
  (when k
    [:div title (if (coll? k) (clojure.string/join ", " k) k)]))


(defn item-component
  [{:keys [name clean-metadata id] :as item}]
  (let [{:keys [description relation subject publisher jmd/community]} clean-metadata]
    [:div.panel.panel-default
     [:div.panel-heading [:h3 name]]
     [:div.panel-body
      [item-info "Publisher: " publisher]
      [:div (str (util/truncate description 500) "...")]
      [item-info "Subject: " subject]
      [item-info "Level: " community]
      [item-info "Content type: " relation]
      [:a {:href (str "http://find.jorum.ac.uk/resources/" id) :target "_blank"} "Resource page"]
      [:div
       [:button.btn.btn-success.pull-right
        {:on-click (util/event-handler (fn [e]
                                         (dispatch [:user/update-selected-items item])
                                         (dispatch [:user/update-profile item])))}
        "ADD TO PROFILE"]]]]))

(defn recommended-item-component
  [{:keys [name score clean-metadata id] :as item}]
  (let [{:keys [description]} clean-metadata]
    [:div.panel.panel-default
     [:div.panel-heading [:h3 name]]
     [:div.panel-body
      [:h4 [item-info "Profile similarity: " (cljs.pprint/cl-format nil "~,2f" score)]]
      ;[explanation item]
      [:div (str (util/truncate description 500) "...")]

      [:a {:href (str "http://find.jorum.ac.uk/resources/" id) :target "_blank"} "Resource page"]
      [:div
       [:button.btn.btn-success.pull-right
        {:on-click (util/event-handler (fn [e]
                                         (dispatch [:user/update-selected-items item])
                                         (dispatch [:user/update-profile item])))}
        "ADD TO PROFILE"]]]]))


(defn item-list
  []
  (let [items (subscribe [:items/list-items])]
    (fn []
      [:div
       [:div (str (count @items) " results:")]
       (into [:div] (map item-component @items))])))


(defn recommended-item-list
  []
  (let [items               (subscribe [:items/recommended-items])
        user-selected-items (subscribe [:user/selected-items]) ;; TODO remove from list
        items-by-score      (reaction (sort-by :score > @items))]
    (fn []
      [:div
       [:h3 "Recommended items"]
       [:div (str (count @items) " results:")]
       (if (empty? @items)
         [:div "You don't have any recommendations yet. Try adding some items to your profile"]
         (into [:div] (map recommended-item-component @items-by-score)))])))


(defn page []
  (let [current-panel (subscribe [:panel])]
    (fn []
      [:div
       [:nav
        [:ul.nav.nav-pills.pull-right
         [:li.active [:a {:href "#"} "HOME"]]
         [:li [:a {:href "#/profile"
                   :style {:color "white"}} "PROFILE"]]]]
       [:div.jumbotron
        {:style {:background-image "url(images/0SP70JWWOK.jpg)"}}
        [:div.container
         [:h1 {:style {:color "white"}}
          "eduSource"]
         [:p.lead {:style {:color "white"}}
          "Educational content recommendations for higher and further education"]
         [search]]]
       [:div.container
        (case @current-panel
          :recommendations [recommended-item-list]
          [:div
           [:h3 "Search Results:"]
           [recommend-button]
           [item-list]])]])))
