(ns ers.core
  (:require [ers.handlers]
            [ers.pages.about :as about]
            [ers.pages.home :as home]
            [ers.pages.item :as item]
            [ers.pages.user-profile :as profile]
            [ers.routes :as routes]
            [ers.subscriptions]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :as reagent]))

(defonce debug?
  ^boolean js/goog.DEBUG)


(defn dev-setup []
  (when debug?
    (enable-console-print!)
    (println "dev mode")))


;; Initialize App

(defmulti page identity)
(defmethod page :home [] home/page)
(defmethod page :about [] about/page)
(defmethod page :profile [] profile/page)
(defmethod page :default [] (fn [_] [:div]))


(defn current-page []
  (let [page-key (subscribe [:page])]
    [(page @page-key)]))


(defn reload []
  (reagent/render [current-page]
                  (.getElementById js/document "app")))


(defn ^:export main []
  (dev-setup)
  (dispatch [:init])
  (routes/app-routes)
  (reload))
