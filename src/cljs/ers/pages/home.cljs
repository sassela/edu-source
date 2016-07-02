(ns ers.pages.home
  (:require [ers.subscriptions]
            [ers.util :as util]
            [re-frame.core :refer [dispatch subscribe]]))


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


(defn item-component
  [{:keys [name link] :as item}]
  [:div
   [:h3 name]
   [:button
    {:on-click (util/event-handler
                 (fn [e] (prn "YAY! " link)))}
    "DOWNLOAD"]])


(defn item-list
  []
  (let [items (subscribe [:items/list-items])]
    (fn []
      [:div
       [:h2 (str (count @items) " results:")]
       (into [:div] (map item-component @items))])))


(defn page []
  (let []
    [:div [:h1 "Home Page"]
     [:p "FIXME"]
     [:button {:on-click #(dispatch [:print-db])} "DB"]
     [:button {:on-click #(dispatch [:items/get-all])} "ALL ITEMS"]
     [search]
     [item-list]
     [:a {:href "#/about"} "about page"]
     ]))
