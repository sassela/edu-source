(ns ers.pages.home
  (:require [ers.subscriptions]
            [ers.util :as util]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.ratom :refer-macros [reaction]]))


(defn recommend-button
  []
  (let [items        (subscribe [:items/list-items])
        user-profile (subscribe [:user/profile])]
    (fn []
      [:button {:on-click (util/event-handler
                            (fn [e]
                              (dispatch [:items/update-scores @user-profile @items])))}
       "SCOREMEBABY"])))


(defn search []
  (when-let [input (subscribe [:input/search])]
    (fn []
      [:form
       [:formgroup
        [:input {:on-change #(dispatch [:input/update-search (-> % .-target .-value)])
                 :value     @input
                 :placeholder "search"}]
        [:button {:on-click (util/event-handler
                              (fn [e] (dispatch [:items/search @input])))}
         "SEARCH"]]])))


(defn item-info
  [title k]
  (when k
    [:div title (if (coll? k) (clojure.string/join ", " k) k)]))


(defn item-component
  [{:keys [name score clean-metadata] :as item}]
  (let [{:keys [description relation subject publisher]} clean-metadata]
    [:div
     [:h3 name]
     [item-info "Profile similarity: " (cljs.pprint/cl-format nil "~,2f" score)]
     [item-info "Publisher: " publisher]
     [:div (str (util/truncate description 500) "...")]
     [item-info "Subject: " subject]
     [item-info "Content type: " relation]
     [:button
      {:on-click (util/event-handler (fn [e] (dispatch [:user/update-profile item])))}
      "ADD"]]))


(defn item-list
  []
  (let [items (subscribe [:items/list-items])]
    (fn []
      [:div
       [:h2 (str (count @items) " results:")]
       (into [:div] (map item-component (sort-by :score > @items)))])))


(defn page []
  (let []
    [:div [:h1 "Home Page"]
     [:p "FIXME"]
     [:button {:on-click (util/event-handler (fn [e] (dispatch [:print-db])))} "DB"]
     [search]
     [recommend-button]
     [item-list]
     [:a {:href "#/about"} "about page"]
     ]))
