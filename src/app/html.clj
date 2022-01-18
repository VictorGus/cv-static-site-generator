(ns app.html
  (:require [hiccup.core :as hc]
            [garden.core :as gc]
            [clojure.string :as str]))

(defn stringify-styles [v]
  (gc/css v))

(def styles
  [:body {:font-family "Lato,sans-serif;"}
   [:.navbar-brand  {:font-weight "650"
                     :font-size   "24px"}]
   [:.navbar-shadow {:box-shadow "0 0.125rem 0.25rem 0 rgb(0 0 0 / 11%)"
                     :letter-spacing "1px"}]
   [:.highlight     {:font-style "Italic"}]
   [:.about-section {:padding-top "115px"
                     :padding-bottom "115px"}]
   [:.photo         {:border "10px solid #fff"
                     :box-shadow "rgba(0,0,0,.2)0 20px 30px 0"}]
   [:.w-shadow      {:box-shadow "0 .5rem 1rem rgba(0,0,0,.15)!important;"}]
   [:.faculty       {:margin-bottom "0rem"}]
   [:.exp-meta      {:font-size "1.0rem"}]
   [:.grey-area     {:background-color "#f7f7f7"}]
   [:.inner-area    {:padding "110px 0"}]
   [:.main-title    {:font-weight "700"}]
   [:.text-md-left  {:text-align "left!important"}]
   [:.about-el      {:font-size   "19px"
                     :margin-left "10px"}]
   [:.iam-header    {:font-size  "1.5rem"
                     :margin-top "1rem"}]
   [:h1             {:position "relative"
                     :font-family "Lato,sans-serif;"
                     :font-size "30px"
                     :font-weight "400"
                     :text-transform "uppercase"}]
   [:h3             {:font-family "Lato,sans-serif;"
                     :font-weight "400"}]
   [:.skills-header {:letter-spacing "4px"
                     :display "inline-block"}]
   [:.exp-location  {:font-size "16px"}]
   [:.exp-divider   {:padding-right ".45em"
                     :padding-left  ".45em"
                     :font-size "20px"}]
   [:.exp-border    {:border-color "#ff0a0a!important"
                     :border "1px solid"}]
   [:.badge         {:padding-right ".6em"
                     :padding-left  ".6em"
                     :border-radius "10rem"}]
   [:.exp-connector {:border-right "1px solid #ff0a0a!important"}]
   [".exp-divider::after"   {:content "'\\00B7'"}]
   [".skills-header::before" {:content "''"
                              :position "absolute"
                              :z-index 99
                              :background-color "#fc6f5c"
                              :width "6px"
                              :height "6px"
                              :border-radius "100%"
                              :left "-17px"
                              :top "15px"}]
   [".skills-header::after"  {:content "''"
                              :position "absolute"
                              :z-index 99
                              :background-color "#fc6f5c"
                              :width "6px"
                              :height "6px"
                              :border-radius "100%"
                              :right "-17px"
                              :top "15px"}]])

(defn skills-section [{:keys [skills] :as config}]
  [:div.container
   [:div.row.pb-5
    [:div.col-xs-1 {:align "center"}
     [:h1.skills-header "Skills"]]]
   [:div.row
    (for [{:keys [photo name]} skills]
      [:div.col-lg-3.col-sm-6.mb-4
       [:div.position-relative.py-2.w-shadow.text-center
        [:embed {:src photo
                 :style "width: 64px;"}]
        [:h3 name]]])]])

(defn about-section [{{:keys [photo name text education interests]} :about :as config}]
  [:section.about-section.pb-0 {:id "about"}
   [:div.container.pb-5
    [:div.row
     [:div.col-10.mx-auto.col-md-6.order-md-2
      [:img.img-fluid.photo {:src photo}]]
     [:div.col-md-6.order-md-1.text-center.text-md-left.align-self-center
      [:h2.iam-header (format "I am %s" name)]
      [:p text]]]]
   [:section.inner-area.grey-area
    [:div.container
     [:div.row
      [:div.col-md-7
       [:h3 "Education"]
       [:ul.ul-edu.fa-ul
        (for [{:keys [university faculty graduation]} education]
          [:li.about-el
           [:i.fa-li.fas.fa-graduation-cap]
           [:div.description
            [:p.faculty
             (format "%s, %s" faculty graduation)]
            [:p.text-muted
             university]]])]]
      [:div.col-md-5
       [:h3 "Interests"]
       [:ul
        (for [el interests]
          [:li.about-el el])]]]]]])

(defn experience-section [{:keys [experience] :as config}]
  [:div.col-lg-8
   (for [{{:keys [name location]} :company
          position :position
          period :period
          highlights :highlights
          techs :technologies} experience]
     (let [wired-techs (str/join ", " techs)]
       [:div.row
        [:div.col-auto.text-center.flex-column.d-none.d-sm-flex
         [:div.row.h-50
          [:div.col]
          [:div.col]]
         [:div.m-2
          [:span.exp-badge.exp-border.badge
           "''"]]
         [:div.row.h-50
          [:div.col.exp-connector]
          [:div.col]]]
        [:div.col.py-2
         [:div.card.w-shadow
          [:div.card-body
           [:h4.main-title.card-title.text-muted.mt-0.mb-2 position]
           [:h4.card-title.text-muted.my-0 name]
           [:div.text-muted.exp-meta
            period
            [:span.exp-divider]
            [:span.exp-location location]]
           [:div.card-text.pt-3
            [:p.highlight "Highlights: "]
            [:ul
             (for [el highlights]
               [:li el])
             [:p (format "Technologies: %s" wired-techs)]
             ]]]
          ]]]))])

(defn cv-as-hiccup [config]
  [:html
   [:head
    [:link {:href       "https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
            :rel        "stylesheet"
            :integrity  "sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3"
            :crossorigin "anonymous"}]
    [:link {:href        "https://use.fontawesome.com/releases/v5.6.0/css/all.css"
            :rel         "stylesheet"
            :integrity   "sha384-aOkxzJ5uQz7WBObEZcHvV5JvRW3TUc2rNPA7pe3AwnsUohiw1Vj2Rgx2KSOkF5+h"
            :crossorigin "anonymous"}]]
   [:body
    [:style (stringify-styles styles)]
    [:nav.navbar.navbar-light.fixed-top.navbar-expand-lg.navbar-shadow.bg-white
     [:div.container.d-flex
      [:div.mr-auto.p-2
       [:a.navbar-brand (get-in config [:about :name])]]
      [:div.p-2
       [:ul.navbar-nav.ml-auto
        [:li.nav-item
         [:a.nav-link {:href "#about"
                       :data-target "#about"}
          [:span "Home"]]]
        [:li.nav-item
         [:a.nav-link
          [:span "Publications"]]]
        [:li.nav-item
         [:a.nav-link {:href "#skills"
                       :data-target "#skills"}
          [:span "Skills"]]]
        [:li.nav-item
         [:a.nav-link {:href "#experience"
                       :data-target "#experience"}
          [:span "Experience"]]]
        [:li.nav-item
         [:a.nav-link
          [:span "Projects"]]]
        [:li.nav-item
         [:a.nav-link
          [:span "Contact"]]]
        [:li.nav-item
         [:a.nav-link
          [:span "Resume"]]]]]]]
    (about-section config)
    [:section.about-section {:id "skills"}
     (skills-section config)]
    [:section.about-section.grey-area {:id "experience"}
     [:div.container
      [:div.row.pb-4
       [:div.col-lg-4
        [:h1.skills-header
         "Experience"]]
       (experience-section config)]]]
    ]]
  )

(spit "/home/victor/Documents/Pet-Projects/cv/resources/index.html" (hc/html (cv-as-hiccup {:about  {:photo "https://thomasgeorgethomas.com/img/Profile_Picture.jpg"
                                                                                                     :text "A Data Engineer passionate about Data Science 📊. I like automating things, building pipelines, exploring scalability problems, improving efficiency and performance tuning. I’m a strong advocate for 📜 open source, ☁️ Cloud computing, 🚀 DevOps, 🆕 Innovation and Automation"
                                                                                                     :name  "Viktor Gusakov"
                                                                                                     :education [{:university "Leti"
                                                                                                                  :faculty    "Foo bar"
                                                                                                                  :graduation "2019"}
                                                                                                                 {:university "Leti"
                                                                                                                  :faculty    "Foo bar"
                                                                                                                  :location  ""
                                                                                                                  :graduation "2021"}]
                                                                                                     :interests ["foo bar"]}
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
                                                                                                      :photo "./public/logos/vim-icon.svg"}]})))

(spit
 "/home/victor/Documents/Pet-Projects/cv/resources/index.html"
 (hc/html
  [:html
   [:head
    [:link {:href       "https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
            :rel        "stylesheet"
            :integrity  "sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3"
            :crossorigin "anonymous"}]
    [:link {:href        "https://use.fontawesome.com/releases/v5.6.0/css/all.css"
            :rel         "stylesheet"
            :integrity   "sha384-aOkxzJ5uQz7WBObEZcHvV5JvRW3TUc2rNPA7pe3AwnsUohiw1Vj2Rgx2KSOkF5+h"
            :crossorigin "anonymous"}]]
   [:body
    [:style (stringify-styles styles)]
    [:nav.navbar.navbar-light.fixed-top.navbar-expand-lg.navbar-shadow.bg-white
     [:div.container.d-flex
      [:div.mr-auto.p-2
       [:a.navbar-brand "Viktor Gusakov"]]
      [:div.p-2
       [:ul.navbar-nav.ml-auto
        [:li.nav-item
         [:a.nav-link {:href "#about"
                       :data-target "#about"}
          [:span "Home"]]]
        [:li.nav-item
         [:a.nav-link
          [:span "Publications"]]]
        [:li.nav-item
         [:a.nav-link {:href "#skills"
                       :data-target "#skills"}
          [:span "Skills"]]]
        [:li.nav-item
         [:a.nav-link {:href "#experience"
                       :data-target "#experience"}
          [:span "Experience"]]]
        [:li.nav-item
         [:a.nav-link
          [:span "Projects"]]]
        [:li.nav-item
         [:a.nav-link
          [:span "Contact"]]]
        [:li.nav-item
         [:a.nav-link
          [:span "Resume"]]]]]]]
    [:section.about-section.pb-0 {:id "about"}
     [:div.container.pb-5
      [:div.row
       [:div.col-10.mx-auto.col-md-6.order-md-2
        [:img.img-fluid.photo {:src "https://thomasgeorgethomas.com/img/Profile_Picture.jpg"}]]
       [:div.col-md-6.order-md-1.text-center.text-md-left.align-self-center
        [:h2.iam-header "I am Viktor Gusakov"]
        [:p "A Data Engineer passionate about Data Science 📊. I like automating things, building pipelines, exploring scalability problems, improving efficiency and performance tuning. I’m a strong advocate for 📜 open source, ☁️ Cloud computing, 🚀 DevOps, 🆕 Innovation and Automation"]]]]
     [:section.inner-area.grey-area
      [:div.container
       [:div.row
        [:div.col-md-7
         [:h3 "Education"]
         [:ul.ul-edu.fa-ul
          [:li.about-el
           [:i.fa-li.fas.fa-graduation-cap]
           [:div.description
            [:p.faculty
             "M.S. Data Analytics Engineering, 2023"]
            [:p.text-muted
             "Northeastern University"]]]
          [:li.about-el
           [:i.fa-li.fas.fa-graduation-cap]
           [:div.description
            [:p.faculty
             "M.S. Data Analytics Engineering, 2023"]
            [:p.text-muted
             "Northeastern University"]]]]]
        [:div.col-md-5
         [:h3 "Interests"]
         [:ul
          [:li.about-el "Big Data Analytics"]
          [:li.about-el "Big Data Analytics"]
          [:li.about-el "Big Data Analytics"]
          [:li.about-el "Big Data Analytics"]
          [:li.about-el "Big Data Analytics"]]]]]]]
    [:section.about-section {:id "skills"}
     [:div.container
      [:div.row.pb-5
       [:div.col-xs-1 {:align "center"}
        [:h1.skills-header "Skills"]]]
      [:div.row
       [:div.col-lg-3.col-sm-6.mb-4
        [:div.position-relative.py-2.w-shadow.text-center
         [:embed {:src "/home/victor/Documents/Pet-Projects/cv/resources/public/logos/golang-icon.svg"}]
         [:h3 "Go"]]]
       [:div.col-lg-3.col-sm-6.mb-4
        [:div.position-relative.py-2.w-shadow.text-center
         [:embed {:src "/home/victor/Documents/Pet-Projects/cv/resources/public/logos/clojure-icon.svg"}]
         [:h3 "Clojure"]]]
       [:div.col-lg-3.col-sm-6.mb-4
        [:div.position-relative.py-2.w-shadow.text-center
         [:embed {:src "/home/victor/Documents/Pet-Projects/cv/resources/public/logos/c-seeklogo.com.svg"
                  :style "width: 64px;"}]
         [:h3 "C/C++"]]]
       [:div.col-lg-3.col-sm-6.mb-4
        [:div.position-relative.py-2.w-shadow.text-center
         [:embed {:src "/home/victor/Documents/Pet-Projects/cv/resources/public/logos/postgresql-icon.svg"}]
         [:h3 "PostgreSQL"]]]
       [:div.col-lg-3.col-sm-6.mb-4
        [:div.position-relative.py-2.w-shadow.text-center
         [:embed {:src "/home/victor/Documents/Pet-Projects/cv/resources/public/logos/docker-icon.svg"}]
         [:h3 "Docker"]]]
       [:div.col-lg-3.col-sm-6.mb-4
        [:div.position-relative.py-2.w-shadow.text-center
         [:embed {:src "/home/victor/Documents/Pet-Projects/cv/resources/public/logos/kubernetes-icon.svg"}]
         [:h3 "Kubernetes"]]]
       [:div.col-lg-3.col-sm-6.mb-4
        [:div.position-relative.py-2.w-shadow.text-center
         [:embed {:src "/home/victor/Documents/Pet-Projects/cv/resources/public/logos/elastic-icon.svg"}]
         [:h3 "Elasticsearch"]]]
       [:div.col-lg-3.col-sm-6.mb-4
        [:div.position-relative.py-2.w-shadow.text-center
         [:embed {:src "/home/victor/Documents/Pet-Projects/cv/resources/public/logos/amazon_aws-icon.svg"}]
         [:h3 "AWS"]]]
       [:div.col-lg-3.col-sm-6.mb-4
        [:div.position-relative.py-2.w-shadow.text-center
         [:embed {:src "/home/victor/Documents/Pet-Projects/cv/resources/public/logos/travis-ci-icon.svg"}]
         [:h3 "Travis CI"]]]
       [:div.col-lg-3.col-sm-6.mb-4
        [:div.position-relative.py-2.w-shadow.text-center
         [:embed {:src "/home/victor/Documents/Pet-Projects/cv/resources/public/logos/git-scm-icon.svg"}]
         [:h3 "Git"]]]
       [:div.col-lg-3.col-sm-6.mb-4
        [:div.position-relative.py-2.w-shadow.text-center
         [:embed {:src "/home/victor/Documents/Pet-Projects/cv/resources/public/logos/linux-icon.svg"}]
         [:h3 "Linux"]]]
       [:div.col-lg-3.col-sm-
        [:div.position-relative.py-2.w-shadow.text-center
         [:embed {:src "/home/victor/Documents/Pet-Projects/cv/resources/public/logos/vim-icon.svg"}]
         [:h3 "Vim"]]]
       ]]]
    [:section.about-section.grey-area {:id "experience"}
     [:div.container
      [:div.row.pb-4
       [:div.col-lg-4
        [:h1.skills-header
         "Experience"]]
       [:div.col-lg-8
        [:div.row
         [:div.col-auto.text-center.flex-column.d-none.d-sm-flex
          [:div.row.h-50
           [:div.col]
           [:div.col]]
          [:div.m-2
           [:span.exp-badge.exp-border.badge
            "''"]]
          [:div.row.h-50
           [:div.col.exp-connector]
           [:div.col]]]
         [:div.col.py-2
          [:div.card.w-shadow
           [:div.card-body
            [:h4.main-title.card-title.text-muted.mt-0.mb-2 "Blablabla"]
            [:h4.card-title.text-muted.my-0 "Blablabla"]
            [:div.text-muted.exp-meta
             "Oct 2020 – Aug 2021"
             [:span.exp-divider]
             [:span.exp-location "St. Petersburg, Russia"]]
            [:div.card-text.pt-3
             [:p.highlight "Highlights: "]
             [:ul
              [:li "Built data pipelines in AWS leveraging services S3, RDS, Athena, Step functions, and EMR."]
              [:li "Migrated 112 TB of data from the on-premises Hadoop cluster to AWS and Snowflake."]
              [:li "Innovated and automated post-migration validation reports in Spark Scala bringing down costs by 90% for 2 projects."]
              [:li "Innovated and automated post-migration validation reports in Spark Scala bringing down costs by 90% for 2 projects."]
              [:li "Innovated and automated post-migration validation reports in Spark Scala bringing down costs by 90% for 2 projects."]
              [:li "Innovated and automated post-migration validation reports in Spark Scala bringing down costs by 90% for 2 projects."]]
             [:p "Technologies: Spark, Scala, Hive, Hadoop, Snowflake, Git, Bitbucket, Maven"]
             ]]]
          ]]
        [:div.row
         [:div.col-auto.text-center.flex-column.d-none.d-sm-flex
          [:div.row.h-50
           [:div.col..exp-connector]
           [:div.col]]
          [:div.m-2
           [:span.exp-badge.exp-border.badge
            "''"]]]
         [:div.col.py-2
          [:div.card.w-shadow
           [:div.card-body
            [:h4.main-title.card-title.text-muted.mt-0.mb-2 "Blablabla"]
            [:h4.card-title.text-muted.my-0 "Blablabla"]
            [:div.text-muted.exp-meta
             "Oct 2020 – Aug 2021"
             [:span.exp-divider]
             [:span.exp-location "St. Petersburg, Russia"]]
            [:div.card-text.pt-3
             [:p.highlight "Highlights: "]
             [:ul
              [:li "Built data pipelines in AWS leveraging services S3, RDS, Athena, Step functions, and EMR."]
              [:li "Migrated 112 TB of data from the on-premises Hadoop cluster to AWS and Snowflake."]
              [:li "Innovated and automated post-migration validation reports in Spark Scala bringing down costs by 90% for 2 projects."]
              [:li "Innovated and automated post-migration validation reports in Spark Scala bringing down costs by 90% for 2 projects."]
              [:li "Innovated and automated post-migration validation reports in Spark Scala bringing down costs by 90% for 2 projects."]
              [:li "Innovated and automated post-migration validation reports in Spark Scala bringing down costs by 90% for 2 projects."]]
             [:p "Technologies: Spark, Scala, Hive, Hadoop, Snowflake, Git, Bitbucket, Maven"]
             ]]]
          ]]
        ]]]]
    ]]))

(comment

  {:about        {:photo ""
                  :name  ""
                  :text  ""
                  :education [{} {}]
                  :interests []}
   :experience   [{:companyName ""
                   :companyLocation ""
                   :period ""
                   :highlights [""]
                   :technologies [""]}]
   :projects     [{} {}]
   :publications [{}]
   :contact      {}}
 


  )
