(ns ers.core
  (:require-macros [secretary.core :refer [defroute]])
  (:import goog.History)
  (:require [ers.handlers]
            [ers.subscriptions]
            [ers.util :as util]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :as reagent]
            [secretary.core :as secretary]))

(defonce debug?
  ^boolean js/goog.DEBUG)

(defn dev-setup []
  (when debug?
    (enable-console-print!)
    (println "dev mode")))


;; Routes

(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(defn app-routes []
  (secretary/set-config! :prefix "#")

  (defroute "/" []
    (dispatch [:page/set :home]))

  (defroute "/about" []
    (dispatch [:page/set :about]))

  ;; add routes here


  (hook-browser-navigation!))


;; Pages

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

(defn home []
  (let []
    [:div [:h1 "Home Page"]
     [:p "FIXME"]
     [:button {:on-click #(dispatch [:print-db])} "DB"]
     [:button {:on-click #(dispatch [:items/get-all])} "ALL ITEMS"]
     [search]
     [:a {:href "#/about"} "about page"]
     ]))

(defn about []
  [:div [:h1 "About Page"]
   [:a {:href "#/"} "home page"]])


;; Initialize App

(defmulti page identity)
(defmethod page :home [] home)
(defmethod page :about [] about)
(defmethod page :default [] (fn [_] [:div]))

(defn current-page []
  (let [page-key (subscribe [:page])]
    [(page @page-key)]))


(defn reload []
  (reagent/render [current-page]
                  (.getElementById js/document "app")))

(defn ^:export main []
  (dev-setup)
  (app-routes)
  (dispatch [:init])
  (reload))
