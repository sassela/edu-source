(ns ers.pages.user-profile
  (:require [ers.util :as util]
            [re-frame.core :refer [dispatch subscribe]]))


(defn profile-feature
  [item]
  (let [item-entry (first item)]
    [:div (str (name (key item-entry)) ": " (val item-entry))
     [:button
      {:on-click (util/event-handler (fn [e] (dispatch [:user/remove-from-profile item])))}
      "NOPE"]]))


(defn page
  []
  (let [features (subscribe [:user/profile])]
    (fn []
      [:div
       [:h1 "Profile"]
       [:div "It looks as though you're interested in: "]
       (into [:div]
         (map profile-feature @features))])))




