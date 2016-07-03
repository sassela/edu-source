(ns ers.pages.item
  (:require [ers.util :as util]
            [re-frame.core :refer [dispatch subscribe]]))


(defn item-info
  [title k]
  (when k
    [:div title (if (coll? k) (clojure.string/join ", " k) k)]))


(defn page
  []
  (let [id (subscribe [:page/id])]
    [:div (str "WHOOOO " @id)]
    #_(let [{:keys [description relation subject publisher]} clean-metadata]
        [:div
         [:h3 name]
         [item-info "Profile similarity: " (cljs.pprint/cl-format nil "~,2f" score)]
         [item-info "Publisher: " publisher]
         [:div (str (util/truncate description 500) "...")]
         [item-info "Subject: " subject]
         [item-info "Content type: " relation]
         [:button
          {:on-click (util/event-handler (fn [e] (dispatch [:user/update-profile item])))}
          "ADD"]
         [:a {:href (str "#/item/" id)} [:button "VIEW MORE"]]])))
