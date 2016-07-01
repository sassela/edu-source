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
                              (fn [e]
                                (dispatch [:items/search [@input]])))}
         "SEARCH"]]])))


(defn page []
  (let []
    [:div [:h1 "Home Page"]
     [:p "FIXME"]
     [:button {:on-click #(dispatch [:print-db])} "DB"]
     [:button {:on-click #(dispatch [:items/get-all])} "ALL ITEMS"]
     [search]
     [:a {:href "#/about"} "about page"]
     ]))
