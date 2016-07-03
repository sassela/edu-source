(ns ers.pages.home
  (:require [ers.util :as util]
            [re-frame.core :refer [dispatch subscribe]]))


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
  [{:keys [name score clean-metadata id] :as item}]
  (let [{:keys [description relation subject publisher jmd/community]} clean-metadata]
    [:div
     [:h3 name]
     [item-info "Profile similarity: " (cljs.pprint/cl-format nil "~,2f" score)]
     [item-info "Publisher: " publisher]
     [:div (str (util/truncate description 500) "...")]
     [item-info "Subject: " subject]
     [item-info "Level: " community]
     [item-info "Content type: " relation]
     [:button
      {:on-click (util/event-handler (fn [e] (dispatch [:user/update-profile item])))}
      "ADD"]
     [:a {:href (str "http://find.jorum.ac.uk/resources/" id) :target "_blank"} [:button "VISIT JORUM RESOURCE PAGE"]]]))


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
