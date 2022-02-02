(ns app.static
  (:require [hiccup.core :as hc]
            [org.httpkit.sni-client :as sni-client]
            [org.httpkit.client :as http]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.walk :as walk]))

(alter-var-root #'org.httpkit.client/*default-client* (fn [_] sni-client/default-client))

(defn save-remote-image [path url]
  (let [file-name (-> url (str/split #"\/") last)
        path* (str path "/static/" file-name)
        relative-path (str "./static/" file-name)]
    (io/make-parents path*)
    (with-open [in (io/input-stream (:body @(http/get url)))
                out (io/output-stream path*)]
      (io/copy in out))
    relative-path))

(defn pull-remote-resources [path config]
  (walk/postwalk
   (fn [el]
     (if (and (string? el)
              (and
               (str/includes? el "http")
               (or
                (str/includes? el ".png")
                (str/includes? el ".jpg")
                (str/includes? el ".jpeg"))))
       (save-remote-image path el)
       el))
   config))
