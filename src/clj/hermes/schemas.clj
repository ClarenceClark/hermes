(ns hermes.schemas
  (:require [schema.core :as s]))

(s/defschema Notif
  {:id s/Int
   :title s/Str
   :content s/Str
   :tags [s/Int]
   :time s/Str})

(s/defschema Tag
  {:id s/Int
   :name s/Str})

(s/defschema NotifIn
  {:title s/Str
   :content s/Str
   :tags [s/Int]})