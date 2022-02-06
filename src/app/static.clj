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

  (spit-cv-site {:output-path "/home/victor/Documents/Pet-Projects/cv/output"
                 :about  {:photo "https://thomasgeorgethomas.com/img/Profile_Picture.jpg"
                          :text "A Data Engineer passionate about Data Science 📊. I like automating things, building pipelines, exploring scalability problems, improving efficiency and performance tuning. I’m a strong advocate for 📜 open source, ☁️ Cloud computing, 🚀 DevOps, 🆕 Innovation and Automation"
                          :name  "Viktor Gusakov"
                          :education [{:university "Leti"
                                       :faculty    "Foo bar"
                                       :graduation "2019"}
                                      {:university "Leti"
                                       :faculty    "Foo bar"
                                       :location  ""
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
                 :projects [
                            {:description "Taking a look at data of 1.6 million twitter users and drawing useful insights while exploring interesting patterns. The techniques used include text mining, sentimental analysis, probability, time series analysis and Hierarchical clustering on text/words using R"
                             :name "Test foo"
                             :url "https://google.com"
                             :photo "https://vivaldi.com/wp-content/uploads/The_Pomodoro_timer_in_Vivaldi_browser-980x551.png"}
                            {:description "Taking a look at data of 1.6 million twitter users and drawing useful insights whil"
                             :name "Test foo"
                             :url "https://google.com"
                             :photo "https://vivaldi.com/wp-content/uploads/The_Pomodoro_timer_in_Vivaldi_browser-980x551.png"}
                            {:description "Taking a look at data of 1.6 million twitter users and drawing useful insights while exploring interesting patterns. The techniques used include text mining, sentimental analysis, probability, time series analysis and Hierarchical clustering on text/words using R"
                             :name "Test foo"
                             :url "https://google.com"
                             :photo "https://vivaldi.com/wp-content/uploads/The_Pomodoro_timer_in_Vivaldi_browser-980x551.png"}

                            {:description "Taking a look at data of 1.6 million twitter users and drawing useful insights while"
                             :name "Test foo foo"
                             :url "https://google.com"
                             :photo "https://vivaldi.com/wp-content/uploads/The_Pomodoro_timer_in_Vivaldi_browser-980x551.png"}]
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
                 :experience [{:company {:name "Blabla"
                                         :location "Russia, St. Petersburg"}
                               :position "Software Engineer"
                               :period "Aug 2019 - Now"
                               :highlights ["Fooo"]
                               :technologies ["AWS"]}
                              {:company {:name "Blabla"
                                         :location "Russia, St. Petersburg"}
                               :position "Software Engineer"
                               :period "Aug 2019 - Now"
                               :highlights ["Fooo"]
                               :technologies ["AWS"]}
                              {:company {:name "Blabla"
                                         :location "Russia, St. Petersburg"}
                               :position "Software Engineer"
                               :period "Aug 2019 - Now"
                               :highlights ["Fooo"]
                               :technologies ["AWS"]}]
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
                          {:name "Elasticsearch"
                           :photo "./public/logos/elastic-icon.svg"}
                          {:name "AWS"
                           :photo "./public/logos/amazon_aws-icon.svg"}
                          {:name "Travis CI"
                           :photo "./public/logos/travis-ci-icon.svg"}
                          {:name "Git"
                           :photo "./public/logos/git-scm-icon.svg"}
                          {:name "Linux"
                           :photo "./public/logos/linux-icon.svg"}
                          {:name "Vim"
                           :photo "./public/logos/vim-icon.svg"}]})



  )
