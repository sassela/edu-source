(ns ers.pages.item
  (:require [ers.util :as util]
            [re-frame.core :refer [dispatch subscribe]]))


(defn item-info
  [title k]
  (when k
    [:div title (if (coll? k) (clojure.string/join ", " k) k)]))


(defn item-component
  [{:keys [name clean-metadata id] :as item}]
  (cljs.pprint/pprint (dissoc item :metadata))
  (let [{:keys [description relation subject publisher jmd/community subject/jacs3code]} clean-metadata]
    [:div
     [:h3 name]
     [item-info "Publisher: " publisher]
     [:div description]
     [item-info "Subject: " subject]
     [item-info "Level: " community]
     [item-info "Content type: " relation]
     [item-info "JACS3 Code: " jacs3code]
     [:button
      {:on-click (util/event-handler (fn [e] (dispatch [:user/update-profile item])))}
      "ADD"]
     [:a {:href (str "#/item/" id)} [:button "DOWNLOAD ALL"]]]))


(defn page
  []
  (let [item (subscribe [:items/detail])]
    (fn []
      [item-component @item])))
