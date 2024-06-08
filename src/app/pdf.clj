(ns app.pdf
  (:require [clojure.string :as str]
            [clj-pdf.core :as pdf]))


(comment 
  (pdf/pdf
   [{}
    [:list {:roman true}
     [:chunk {:style :bold} "a bold item"]
     "another item"
     "yet another item"]
    [:phrase "some text"]
    [:phrase "some more text"]
    [:paragraph "yet more text"]]
   "doc.pdf")


  )
