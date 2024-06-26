(ns app.pdf
  (:require [clojure.string :as str]
            [clj-pdf.core :as pdf]))

(def titles
  {:skills          {:russian "Навыки"
                      :english "Skills"}
   :education       {:russian "Образование"
                     :english "Educaition"}
   :faculty         {:russian "Факультет"
                     :english "Faculty"}
   :department      {:russian "Кафедра"
                     :english "Department"}
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
   :phone           {:russian "Телефон"
                     :english "Phone"}
   }
  )

(def styles {})

(defn description-section
  [{{:keys [text name photo contacts interests]} :about language :language :as config}]
  [:paragraph 
   [:paragraph
    [:chunk {:size 18} name]
    [:chunk {:x 220 :y -120 }
     [:image   {:width 140
                :height 160
                ;; :align :right
                :pad-left   100
                :pad-right  50} photo]]
    [:paragraph
     [:spacer]
     [:paragraph
      [:chunk (str "Email:  " (->> contacts
                                   (filter (comp #{"e-mail"} :type))
                                   first
                                   :name))]]
     [:paragraph
      [:chunk (str (get (:phone titles) language) ":  " (->> contacts
                                                             (filter (comp #{"phone"} :type))
                                                             first
                                                             :name))]]
     ]
    ]]
  )

(defn skills-section [{skills :skills language :language :as config}]
  [:paragraph 
   [:paragraph
    [:heading {:style {:size 15}} 
     (get (:skills titles) language)]
    ;; [:paragraph ""]
    [:paragraph
     (into
      [:table {:width 65 :border false :cell-border false}]
      (for [el (partition 3 (mapv :name skills))]
        (for [inner-el el]
          [:chunk (str "• " inner-el)])
        )
      )
     #_[:list {:symbol "• "}
      (for [skill skills]
        [:chunk {:size 11} (str (:name skill) ";")])]]
    ]]
  )

(defn experience-section [{experience :experience language :language :as config}]
  [:paragraph 
   [:paragraph
    [:heading {:style {:size 15}} 
     (get (:experience titles) language)]
    [:paragraph ""]
    (for [{:keys [company position period
                  highlights technologies]} experience]
      [:paragraph 
       [:chunk {:style :bold :size 15} "• "]
       [:paragraph 
        [:chunk {:style :bold
                 :size 11}
         (:name company)]]
       [:paragraph
        [:chunk {:style :bold :size 18} "  "]
        [:chunk {:color [128 128 128] :style :bold} period]
        [:chunk {:color [128 128 128] :style :bold} "   •   "]
        [:chunk {:color [128 128 128] :style :bold} (:location company)]
        ]
       [:paragraph 
        [:chunk {:style :bold :size 18} "  "]
        [:chunk {:size 11} position]]
       [:paragraph
        [:chunk {:style :bold :size 10} (str "    " (get (:highlights titles) language) ":")]]
       [:paragraph
        [:list {:symbol "     -  "}
         (for [highlight highlights]
           [:chunk {:size 10} (str highlight ";")])]
        [:chunk {:style :bold :size 10} (str "    " (get (:technologies titles) language) ": ")]
        [:chunk {:size 10} (str/join ", " technologies)]]
       #_[:paragraph 
        [:chunk {:style :bold :size 18} "  "]
        [:chunk {:size 10} department]]

       ;; [:paragraph ""]
       ])
    ]]
  )

(defn pet-projects-section [{projects :projects language :language :as config}]
  [:paragraph 
   [:paragraph
    [:heading {:style {:size 15}} 
     (get (:projects titles) language)]
    [:paragraph ""]
    (for [{:keys [name description url highlights technologies]} projects]
      [:paragraph
       [:chunk {:style :bold :size 15} "• "]
       [:paragraph
        [:anchor {:target url}
         [:chunk {:style :bold
                  :size 11}
          name]]]
       [:paragraph {:keep-together true :indent 15 :first-line-indent 15}
        [:chunk {:style :bold :size 18} "  "]
        [:chunk {:size 10} description]]
       [:paragraph
        [:list {:symbol "     -  "}
         (for [highlight highlights]
           [:chunk {:size 10} (str highlight ";")])]
        [:chunk {:style :bold :size 10} (str "   " (get (:technologies titles) language) ": ")]
        [:chunk {:size 10} (str/join ", " technologies)]]
       ])
    ]]
  )

(defn education-section [{{:keys [education]} :about language :language :as config}]
  [:paragraph 
   [:paragraph
    [:heading {:style {:size 15}} 
     (get (:education titles) language)]
    [:paragraph ""]
    (for [{:keys [university faculty start
                  graduation department]} education]
      [:paragraph 
       [:chunk {:style :bold :size 15} "• "]
       [:paragraph 
        [:chunk {:style :bold
                 :size 11}
         university]]
       [:paragraph
        [:chunk {:style :bold :size 18} "  "]
        [:chunk {:color [128 128 128] :style :bold} (str start " - " graduation)]
        ]
       [:paragraph 
        [:chunk {:style :bold :size 18} "  "]
        [:chunk {:size 10} faculty]]
       [:paragraph 
        [:chunk {:style :bold :size 18} "  "]
        [:chunk {:size 10} department]]

       ;; [:paragraph ""]
       ])
    ]]
  )

(defn save-cv-document [config path]
  (pdf/pdf
   [{:font {:encoding :unicode
            :ttf-name "fonts/arialuni.ttf"}}
    (description-section config)
    (skills-section config)
    (experience-section config)
    (education-section config)
    (pet-projects-section config)
    ]
   path)
  )

(comment

  (clojure.edn/read-string (slurp "src/app/core.clj"))

  (save-cv-document {:language :russian
                     :skills  [{:name "C/C++"
                                :photo "./public/logos/c-seeklogo.com.svg"}
                               {:name "Linux"
                                :photo "./public/logos/linux-icon.svg"}
                               {:name "STM32"}
                               {:name "FreeRTOS"}
                               {:name "QT"}
                               {:name "GTK"}
                               {:name "Clojure"
                                :photo "./public/logos/clojure-icon.svg"}
                               {:name "PostgreSQL"
                                :photo "./public/logos/postgresql-icon.svg"}
                               {:name "Docker"
                                :photo "./public/logos/docker-icon.svg"}
                               {:name "Kubernetes"
                                :photo "./public/logos/kubernetes-icon.svg"}
                               {:name "AWS"
                                :photo "./public/logos/amazon_aws-icon.svg"}
                               {:name "Git"
                                :photo "./public/logos/git-scm-icon.svg"}
                               ]
                     :projects   [{:description "Утилита для настройки работы кулера"
                                   :technologies ["C" "GTK" "Linux"]
                                   :highlights ["Реализан GUI с помощью библиотеки GTK"
                                                "Управление кулером осуществляется с помощью системных вызовов Linux(Raspberry Pi OS)"
                                                "Может работать в фоновом режиме"]
                                   :name "Система управления внешним охлаждением CPU для Raspberry Pi"
                                   :url "https://github.com/VictorGus/cv-static-site-generator"
                                   :photo "https://vivaldi.com/wp-content/uploads/The_Pomodoro_timer_in_Vivaldi_browser-980x551.png"}]
                     :experience [{:company {:name "ООО «ХЭЛС САМУРАИ» (Health Samurai)"
                                             :location "РФ, Санкт-Петербург"}
                                   :position "Cтажер"
                                   :period "Январь 2019 - Август 2019"
                                   :highlights ["Разработка и поддержка open source системы профилирования медицинских данных"
                                                "Разработка модуля валидации медицинских данных"]
                                   :technologies ["Clojure" "PostgreSQL" "FHIR" "Git"]}
                                  {:company {:name "ООО «ХЭЛС САМУРАИ» (Health Samurai)"
                                             :location "РФ, Санкт-Петербург"}
                                   :position "Инженер-программист"
                                   :period "C августа 2019 года"
                                   :highlights ["Разработка и поддержка региональной медицинской информационной системы (РМИС) для Чувашской республики"
                                                "Разработка системы преобразования данных (ETL)"
                                                "Разработка, проектирование и поддержка системы интеграции и агрегации данных из различных МИС"
                                                "Поддержка портала для персональных электронных карт здоровья (PHR), работающего по технологии «Smart on FHIR»"
                                                ]
                                   :technologies ["Clojure" "AWS" "PostgreSQL" "Kubernetes" "Linux" "FHIR" "Git"]}]
                     :about {:text "fpokfo xoxo"
                             :contacts [{:name "retaow@gmail.com" :type "e-mail"}
                                        {:name "+7(951)660-13-75" :type "phone"}]
                             :education [{:university "Санкт-Петербургский государственный электротехнический университет «ЛЭТИ»"
                                          :faculty    "Факультет компьютерных технологий и информатики (Бакалавр)"
                                          :department "Информатика и вычислительная техника"
                                          :start "2015"
                                          :graduation "2019"}
                                         {:university "Санкт-Петербургский государственный электротехнический университет «ЛЭТИ»"
                                          :faculty    "Факультет компьютерных технологий и информатики (Магистр)"
                                          :department "Информатика и вычислительная техника"
                                          :start "2019"
                                          :graduation "2021"}]
                             :photo "/home/viktor/Pictures/paulpot.jpg"
                             :name "Viktor Gusakov"}} "SAMPLE.pdf")

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

  (def skills [{:name "Clojure"
                :photo "./public/logos/clojure-icon.svg"}
               {:name "C/C++"
                :photo "./public/logos/c-seeklogo.com.svg"}
               {:name "PostgreSQL"
                :photo "./public/logos/postgresql-icon.svg"}
               {:name "Docker"
                :photo "./public/logos/docker-icon.svg"}
               {:name "Kubernetes"
                :photo "./public/logos/kubernetes-icon.svg"}
               {:name "AWS"
                :photo "./public/logos/amazon_aws-icon.svg"}
               {:name "Git"
                :photo "./public/logos/git-scm-icon.svg"}
               {:name "Linux"
                :photo "./public/logos/linux-icon.svg"}
               {:name "STM32"}
               {:name "FreeRTOS"}
               {:name "QT"}
               {:name "QT"}
               {:name "QT"}
               {:name "QT"}
               {:name "QT"}
               {:name "QT"}
               {:name "QT"}
               {:name "QT"}
               ])

  (partition 4 1 (mapv :name skills))

  (pdf/pdf
   [{}
    [:chunk {:size 20} "Viktor Gusakov"]
    (into
     [:table {:width 50 :border false :cell-border false}]
     (for [el (partition 3 (mapv :name skills))]
       (for [inner-el el]
         [:chunk (str "• " inner-el)])
       )
     )
    ]
   "SAMPLE.pdf")


  )
