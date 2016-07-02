(ns ers.recommender
  (:require [clojure.set :as set]))


(defn jaccard-index
  "
  CREDIT to https://github.com/aria42/infer/blob/master/src/infer/measures.clj
  http://en.wikipedia.org/wiki/Jaccard_index
  The Jaccard index, also known as the Jaccard similarity coefficient (originally coined coefficient de communauté by Paul Jaccard), is a statistic used for comparing the similarity and diversity of sample sets.
  The Jaccard coefficient measures similarity between sample sets, and is defined as the size of the intersection divided by the size of the union of the sample sets."
  [a b]
  (/ (count (set/intersection a b))
    (count (set/union a b))))
