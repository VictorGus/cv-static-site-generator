(ns app.core
  (:require [clojure.string :as str]
            [clojure.edn    :as edn]
            [cli-matic.core :as cli]
            [app.static     :as static])
  (:gen-class))


(defn process-config [{:keys [config output] :as arg}]
  (let [parsed-config (cond-> config ;; add new options later
                        (str/includes? config ".edn")
                        (-> slurp edn/read-string))]
    (->> parsed-config (merge {:output-path output}) static/spit-cv-site)))

(def configuration
  {:app         {:command     "cv-static"
                 :description "CV static site generator"
                 :version     "0.0.1"}
   :commands    [{:command     "gen-static"
                  :description "upload scraped data via straight connection to db"
                  :opts [{:option "config" :short "c" :as "Path to configuration file" :type :string}
                         {:option "output" :short "o" :as "Path for static files"      :type :string}]
                  :runs process-config}]})

(defn -main [& args]
  (cli/run-cmd args configuration))

(comment


  )
