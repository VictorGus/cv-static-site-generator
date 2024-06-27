(ns app.static
  (:require [hiccup.core :as hc]
            [app.html    :as cv]
            [org.httpkit.sni-client :as sni-client]
            [org.httpkit.client :as http]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.walk :as walk]))

(alter-var-root #'org.httpkit.client/*default-client* (fn [_] sni-client/default-client))

(defmacro get-static []
  (let [r (str/join
           " "
           (reduce (fn [acc f]
                     (if (.isDirectory f)
                       (into acc (map (fn [el]
                                        (str (.getName f) "/" (.getName el)))
                                      (filter #(not (.isDirectory %)) (file-seq f))))
                       (if (not (some
                                 (fn [el]
                                   (re-find (re-pattern (.getName f)) el))
                                 acc))
                         (conj acc (.getName f))
                         acc)))
                   []
                   (->> (str (System/getProperty "user.dir") "/resources" "/public")
                        io/file
                        file-seq
                        (filter #(not (= (.getName %) "public"))))))]
    `~r))

(defn save-public-files [path]
  (let [items (str/split (get-static) #" ")]
    (println items)
    (io/make-parents (format "%s/public/%s" path (first items)))

    (doseq [item items]
      (with-open [in (io/input-stream
                      (io/resource (str "public/" item)))
                  out (io/output-stream (str path "/public/" item))]
        (io/copy in out)))))

(defn save-remote-image [path url]
  (let [file-name (-> url (str/split #"\/") last)
        path* (str path "/static/" file-name)
        relative-path (str "./static/" file-name)]
    (io/make-parents path*)
    (with-open [in (io/input-stream (:body @(http/get url)))
                out (io/output-stream path*)]
      (io/copy in out))
    relative-path))

(defn pull-remote-resources [{:keys [output-path] :as config}]
  (walk/postwalk
   (fn [el]
     (if (and (string? el)
              (and
               (str/includes? el "http")
               (or
                (str/includes? el ".png")
                (str/includes? el ".jpg")
                (str/includes? el ".jpeg"))))
       (save-remote-image output-path el)
       el))
   config))

(defn spit-cv-site [{:keys [output-path] :as config}]
  (let [config* (pull-remote-resources config)
        _ (save-public-files output-path)]
    (spit
     (str output-path "/index.html")
     (hc/html (cv/cv-as-hiccup config*)))))

(comment

  (spit-cv-site {:output-path "/home/viktor/Pet-Projects/cv-static-site-generator/output"
                 :language :russian
                 :about  {:photo "/home/viktor/Pet-Projects/cv-static-site-generator/resources/public/photo/photo_2022-10-24_10-09-49.jpg"
                          :text "A Data Engineer passionate about Data Science 📊. I like automating things, building pipelines, exploring scalability problems, improving efficiency and performance tuning. I’m a strong advocate for 📜 open source, ☁️ Cloud computing, 🚀 DevOps, 🆕 Innovation and Automation"
                          :name  "Viktor Gusakov"
                          :education [{:university "Saint Petersburg Electrotechnical University \"LETI\""
                                       :faculty    "Faculty of Computer Science and Technology (Bachelor)"
                                       :department ""
                                       :graduation "2019"}
                                      {:university "Saint Petersburg Electrotechnical University \"LETI\""
                                       :faculty    "Faculty of Computer Science and Technology (Master)"
                                       :graduation "2021"}]
                          :interests ["foo bar"]
                          :contacts [{:name "GitHub" :type "github" :url "https://github.com/VictorGus"}
                                     {:name "retaow@gmail.com" :type "e-mail"}
                                     {:name "VictorGus" :type "telegram"}
                                     {:name "Habr Career" :type "habr" :url "https://career.habr.com/victor-gusakov"}
                                     {:name "Medium" :type "medium" :url "https://medium.com/@VictorGus"}]}
                 :publications [{:description "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Pharetra vel turpis nunc eget lorem dolor sed viverra ipsum"
                                 :name "Foo bar"
                                 :url "https://google.com"
                                 :photo "https://www.dankultura.org/wp-content/uploads/2015/10/Article-Writing-Can-be-a-Sure-Wager.jpg"}
                                {:description "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Pharetra vel turpis nunc eget lorem dolor sed viverra ipsum"
                                 :name "Foo bar"
                                 :url "https://google.com"
                                 :photo "https://www.dankultura.org/wp-content/uploads/2015/10/Article-Writing-Can-be-a-Sure-Wager.jpg"}
                                {:description "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Pharetra vel turpis nunc eget lorem dolor sed viverra ipsum"
                                 :name "Foo bar"
                                 :url "https://google.com"
                                 :photo "https://www.dankultura.org/wp-content/uploads/2015/10/Article-Writing-Can-be-a-Sure-Wager.jpg"}]
                 :projects [{:description "Утилита для настройки работы кулера"
                             :technologies ["C" "GTK" "Linux"]
                             :highlights ["Реализован GUI с помощью библиотеки GTK"
                                          "Управление кулером осуществляется с помощью системных вызовов Linux(Raspberry Pi OS)"
                                          "Может работать в фоновом режиме"]
                             :name "Система управления внешним охлаждением CPU для Raspberry Pi"
                             :url "https://github.com/VictorGus/cv-static-site-generator"
                             :photo "https://cdn-reichelt.de/bilder/web/xxl_ws/A300/RPI_CASE_STACKP4_01.png"}
                            ]
                 :accomplishments [{:title "Foo bar"
                                    :subtitle "Foo"
                                    :date "Feb 21"
                                    :link "http://localhost:3333"
                                    :text "Foo bar barr get test brr"}
                                   {:title "Foo bar"
                                    :subtitle "Foo"
                                    :date "Feb 21"
                                    :link "http://localhost:3333"
                                    :text "Foo bar barr get test brr"}]
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
                 :skills [{:name "Go"
                           :photo "./public/logos/golang-icon.svg"}
                          {:name "Clojure"
                           :photo "./public/logos/clojure-icon.svg"}
                          {:name "C/C++"
                           :photo "./public/logos/c-seeklogo.com.svg"}
                          {:name "PostgreSQL"
                           :photo "./public/logos/postgresql-icon.svg"}
                          {:name "Docker"
                           :photo "./public/logos/docker-icon.svg"}
                          {:name "Kubernetes"
                           :photo "./public/logos/kubernetes-icon.svg"}
                          {:name "GTK"
                           :photo "./public/logos/gtk_icon.png"}
                          {:name "QT"
                           :photo "./public/logos/qt_icon.svg"}
                          {:name "AWS"
                           :photo "./public/logos/amazon_aws-icon.svg"}
                          {:name "FreeRTOS"
                           :photo "./public/logos/freertos_icon.svg"}
                          {:name "Git"
                           :photo "./public/logos/git-scm-icon.svg"}
                          {:name "Linux"
                           :photo "./public/logos/linux-icon.svg"}
                          {:name "STM32"
                           :photo "./public/logos/stm32_icon.png"}]})

  )
