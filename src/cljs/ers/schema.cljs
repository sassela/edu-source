(ns ers.schema
  (:require [schema.core :as s :include-macros true]))


(def TransformedMetadata
  {s/Keyword (s/conditional coll? [s/Str] :else s/Str)})

(def Profile
  {s/Keyword [s/Any]})


(def KeyValuePair
  {:key   s/Str
   :value s/Str})


(def ListItem
  {:name                                  s/Str
   :id                                    s/Num
   :type                                  s/Str
   :metadata                              [KeyValuePair]
   :clean-metadata                        TransformedMetadata
   :expand                                [s/Str]
   :link                                  s/Str
   :lastModified                          s/Str
   :withdrawn                             s/Str
   :handle                                s/Str
   (s/optional-key :score)                s/Num
   (s/optional-key :archived)             s/Str
   (s/optional-key :bitstreams)           s/Any
   (s/optional-key :parentCollection)     s/Any
   (s/optional-key :parentCollectionList) s/Any
   (s/optional-key :parentCommunityList)  s/Any})


(def db-schema
  {:items/list-items              [ListItem]
   :input/search                  s/Str
   :page                          s/Keyword
   (s/optional-key :page/id)      s/Str
   (s/optional-key :items/detail) ListItem
   (s/optional-key :user)         s/Any})