(ns ers.util)

(defn compose-query
  [params]
  (->>
    params
    (map (fn [[k v]] (str (name k) "=" "\"" v "\"")))
    (interpose "&")
    (apply str)))
