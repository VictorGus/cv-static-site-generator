(ns app.core
  (:require [clojure.string :as str]
            [cli-matic.core :as cli])
  (:gen-class))


(def configuration
  {:app         {:command     "cv-static"
                 :description "CV static site generator"
                 :version     "0.0.1"}
   :commands    [{:command     "rest"
                  :description "upload scraped data by url"
                  :opts [{:option "url" :short "u" :as "url of a service" :type :string}
                         {:option "token" :short "t" :as "bearer token"  :type :string}
                         {:option "endpoint" :short "e" :as "bulk upload endpoint" :type :string}
                         {:option "datamap" :short "f" :as "yaml file with urls" :type :yamlfile}]
                  :runs  (fn [])}
                 {:command     "straight"
                  :description "upload scraped data via straight connection to db"
                  :opts [{:option "datamap" :short "f" :as "yaml file with urls" :type :yamlfile}]
                  :runs  (fn [])}]})

(defn -main [& args]
  (cli/run-cmd args configuration))

(comment


  )
