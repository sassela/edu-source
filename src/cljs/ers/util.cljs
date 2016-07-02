(ns ers.util
  (:require [ers.schema :as schema]
            [schema.core :as s :include-macros true]))

(defn compose-query
  [params]
  (->>
    params
    (map (fn [[k v]] (str (name k) "=" "\"" v "\"")))
    (interpose "&")
    (apply str)))


(defn event-handler
  [f]
  (fn [e]
    (apply f [e])
    (.stopPropagation e)
    (.preventDefault e)))


(s/defn clean-keyword :- s/Keyword
  [s :- s/Str]
  (-> s (clojure.string/replace #"dc." "") (clojure.string/replace "." "/") (keyword)))


(def features [:subject/jacs3
               :subject/jacs3code
               :audience
               :jmd/community
               :contributor/author
               :language/iso])

(s/defn profile :- schema/Profile
  [metadata :- [schema/KeyValuePair]]
  (->>
    metadata
    ;; FIXME
    (map #(hash-map (clean-keyword (:key %)) (:value %)))
    (filter #(some (fn [feature] (= (ffirst %) feature)) features))
    (into #{})))