(ns ers.util
  (:require [ers.schema :as schema]
            [schema.core :as s :include-macros true]))


(defn event-handler
  [f]
  (fn [e]
    (apply f [e])
    (.stopPropagation e)
    (.preventDefault e)))


(defn truncate
  [s n]
  (clojure.string/join (take n s)))


(s/defn clean-keyword :- s/Keyword
  [s :- s/Str]
  (-> s (clojure.string/replace #"dc." "") (clojure.string/replace "." "/") (keyword)))


(s/defn transform-metadata-one :- schema/TransformedMetadata
  [item :- schema/ListItem]
  (->>
    (:metadata item)
    (map #(hash-map (clean-keyword (:key %)) (:value %)))
    (apply merge-with (comp flatten list))
    (assoc item :clean-metadata)))


(s/defn transform-metadata :- [schema/TransformedMetadata]
  [items :- [schema/ListItem]]
  (map #(transform-metadata-one %) items))


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