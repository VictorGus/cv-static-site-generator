(ns app.pdf
  (:require [clojure.string :as str]
            [clj-pdf.core :as pdf]))

(def titles
  {:skills          {:russian "Навыки"
                      :english "Skills"}
   :education       {:russian "Образование"
                     :english "Educaition"}
   :iam             {:russian "Меня зовут "
                     :english "I am "}
   :home            {:russian "О себе"
                     :english "Home"}
   :publications    {:english "Publications"
                     :russian "Публикации"}
   :highlights      {:russian "Обязанности"
                     :english "Highlights"}
   :technologies    {:russian "Технологии"
                     :english "Technologies"}
   :projects        {:russian "Проекты"
                     :english "Projects"}
   :experience      {:russian "Опыт работы"
                     :english "Experience"}
   }
  )

(def styles {})

(defn description-section [{{:keys [text name contacts interests]} :about :as config}]
  [:paragraph
   [:chunk name]]
  )

(defn skills-section [config]
  )

(defn experience-section [config]
  )

(defn pet-projects-section [config]
  )

(defn education-section [config]
  )

(defn save-cv-document [config path]
  (pdf/pdf
   [{}
    (description-section config)
    ]
   path)
  )

(comment

  (save-cv-document {:about {:text "fpokfo xoxo"}} "SAMPLE.pdf")

  (pdf/pdf
   [{}
    [:chapter [:paragraph {:color [250 0 0]} "Chapter"]
     [:section "Section Title" "Some content"]
     [:section [:paragraph {:size 10} "Section Title"]
      [:paragraph "Some content"]
      [:paragraph "Some more content"]
      [:section {:color [100 200 50]} [:paragraph "Nested Section Title"]
       [:paragraph "nested section content"]]]]]
   "SAMPLE.pdf")


  (pdf/pdf
   [{}
    [:chunk {:size 20} "Viktor Gusakov"]
    []
    ]
   "SAMPLE.pdf")


  )
