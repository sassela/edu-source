(ns ers.subscriptions
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]
            [re-frame.db]))

(register-sub :items/list-items (fn [db _] (reaction (:items/list-items @db))))
(register-sub :items/recommended-items (fn [db _] (reaction (:items/recommended-items @db))))
(register-sub :items/detail (fn [db _] (reaction (:items/detail @db))))
(register-sub :input/search (fn [db _] (reaction (:input/search @db))))
(register-sub :panel (fn [db _] (reaction (:panel @db))))
(register-sub :page (fn [db _] (reaction (:page @db))))
(register-sub :page/id (fn [db _] (reaction (:page/id @db))))
(register-sub :user (fn [db _] (reaction (:user @db))))
(register-sub :user/profile (fn [db _] (reaction (get-in @db [:user :profile]))))
(register-sub :user/selected-items (fn [db _] (reaction (get-in @db [:user :selected-item-ids]))))