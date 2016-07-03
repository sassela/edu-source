(ns ers.pages.user-profile
  (:require [ers.util :as util]
            [re-frame.core :refer [dispatch subscribe]]))


(defn profile-feature
  [item]
  (let [item-entry (first item)]
    [:button.list-group-item
     {:on-click (util/event-handler (fn [e] (dispatch [:user/remove-from-profile item])))}
     (str (name (key item-entry)) ": " (val item-entry))]))


(defn page
  []
  (let [features (subscribe [:user/profile])]
    (fn []
      [:div
       [:nav
        [:ul.nav.nav-pills.pull-right
         [:li [:a {:href "#"} "HOME"]]
         [:li.active [:a {:href "#/profile"} "PROFILE"]]]]
       [:div.jumbotron
        {:style {:background-image "url(images/0SP70JWWOK.jpg)"}}
        [:div.container
         [:h1 {:style {:color "white"}}
          "profile"]]]
       [:div.container
        [:h4 "It looks as though you're interested in the following item features. Select a feature to remove it from your profile."]
        (into [:div.list-group]
          (map profile-feature @features))]])))




