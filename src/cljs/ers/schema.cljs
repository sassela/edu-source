(ns ers.schema
  (:require [schema.core :as s :include-macros true]))


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
   :expand                                [s/Str]
   :link                                  s/Str
   :lastModified                          s/Str
   :withdrawn                             s/Str
   :handle                                s/Str
   (s/optional-key :archived)             s/Str
   (s/optional-key :bitstreams)           s/Any
   (s/optional-key :parentCollection)     s/Any
   (s/optional-key :parentCollectionList) s/Any
   (s/optional-key :parentCommunityList)  s/Any})


(def db-schema
  {:items/list-items [ListItem]
   :input/search s/Str
   :page s/Keyword})