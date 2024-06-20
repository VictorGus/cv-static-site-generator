(ns app.html
  (:require [hiccup.core :as hc]
            [garden.core :as gc]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(defn stringify-styles [v]
  (gc/css v))

(def styles
  [:body {:font-family "Lato,sans-serif;"}
   [:.navbar-brand  {:font-weight "650"
                     :font-size   "24px"}]
   [:.contact       {:font-size "21px"
                     :font-weight "700"
                     :text-align "center"}]
   [:.social        {:text-decoration "none"
                     :color "black"}
    [:&:hover {:color "red"}]]
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
   [:.about-text    {:font-size "20px"}]
   [:.card          {:padding "1.25rem"}]
   [:.card-text     {:padding "1rem 0"}]
   [:.card-link     {:color "#fc6f5c"
                     :text-decoration "none"
                     :font-size "20px"}]
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
                              :top "15px"}]
   [:.project-link   {:min-height "100px"}
    [:.project-image {:max-width "100%"
                      :height "auto"}]]
   [:.link           {:text-decoration "none"
                      :font-weight "700"
                      :text-transform "uppercase"
                      :color "#000"}]
   [:.card-description {:font-size "20px"}]
   ])

(def titles
  {:skills          {:russian "Навыки"
                      :english "Skills"}
   :education       {:russian "Образование"
                     :english "Educaition"}
   :interests       {:russian "Интересы"
                     :english "Interests"}
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
   :accomplishments {:russian "Достижения"
                     :english "Accomplishments"}
   :resume          {:russian "Резюме"
                     :english "Resume"}
   }
  )

(defn skills-section [{:keys [skills language] :as config}]
  [:div.container
   [:div.row.pb-5
    [:div.col-xs-1 {:align "center"}
     [:h1.skills-header (get (:skills titles) language)]]]
   [:div.row
    (for [{:keys [photo name]} skills]
      [:div.col-lg-3.col-sm-6.mb-4
       [:div.position-relative.py-2.w-shadow.text-center
        [:embed {:src photo
                 :style (cond
                          (#{"FreeRTOS"} name)
                          "width: 164px;"

                          (#{"QT"} name)
                          "width: 94px;"

                          :other
                          "width: 64px;"
                          )}]
        [:h3 name]]])]])

(defn contact-logos [{:keys [type] :as contacts}]
  (case type
    "github"
    [:i.fab.fa-github      {:style "font-size: 30px;"}]

    "e-mail"
    [:i.fas.fa-envelope    {:style "font-size: 30px;"}]

    "telegram"
    [:i.fab.fa-telegram    {:style "font-size: 30px;"}]

    "habr"
    [:i.fa.fa-h-square     {:style "font-size: 30px;"}]

    "medium"
    [:i.fab.fa-medium      {:style "font-size: 30px;"}]

    "twitter"
    [:i.fab.fa-twitter     {:style "font-size: 30px;"}]

    "linked-in"
    [:i.fab.fa-linkedin-in {:style "font-size: 30px;"}]

    [:i.fas.fa-question-circle {:style "font-size: 30px;"}]))

(defn about-section [{{:keys [photo name text education interests contacts]} :about language :language :as config}]
  [:section.about-section.pb-0 {:id "about"}
   [:div.container.pb-5
    [:div.row
     [:div.col-10.mx-auto.col-md-6.order-md-2
      [:img.img-fluid.photo {:src photo}]]
     [:div.col-md-6.order-md-1.text-center.text-md-left.align-self-center
      [:div
       [:h2.iam-header (str (get (:iam titles) language) " ") [:strong name]]
       [:p.about-text text]]
      [:div.row.justify-content-center.pt-3
       (for [{:keys [type name url] :as c} contacts]
         [:div.col-sm-4
          [:div.contact
           (contact-logos c)
           (if url
             [:p
              [:a.social {:href url :target "blank"} name]]
             [:p.social name])]])]]]]
   [:section.inner-area.grey-area
    [:div.container
     [:div.row
      [:div.col-md-7
       [:h3 (get (:education titles) language)]
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
       [:h3 (get (:interests titles) language)]
       [:ul
        (for [el interests]
          [:li.about-el el])]]]]]])

(defn first? [idx]
  (= idx 0))

(defn last? [idx l]
  (= idx (- l 1)))

(defn experience-section [{:keys [experience language] :as config}]
  [:div.col-lg-8
   (map-indexed
    (fn [idx {{:keys [name location]} :company
              position :position
              period :period
              highlights :highlights
              techs :technologies :as el}]
      (let [wired-techs (str/join ", " techs)]
        [:div.row
         (when (not= 1 (count experience))
           [:div.col-auto.text-center.flex-column.d-none.d-sm-flex
            [:div.row.h-50
             [(if (first? idx) :div.col :div.col.exp-connector)]
             [:div.col]]
            [:div.m-2
             [:span.exp-badge.exp-border.badge
              "''"]]
            [:div.row.h-50
             [(if (not (last? idx (count experience))) :div.col.exp-connector :div.col)]
             [:div.col]]])
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
             [:p.highlight (str (get (:highlights titles) language) ": ")]
             [:ul
              (for [el highlights]
                [:li el])]
             [:p (format "%s: %s" (get (:technologies titles) language) wired-techs)]
             ]]
           ]]]))
    experience)])

(defn accomplishments-section [{:keys [accomplishments] :as config}]
  [:div.row
   (for [{:keys [title subtitle link date text]} accomplishments]
     [:div.col-lg-6.col-md-6.mb-4
      [:div.card.rounded-0.border-0.w-shadow
       [:div.card-body
        [:h4.main-title.card-title.text-muted.my-0 title]
        [:div.text-muted.exp-meta
         subtitle
         [:span.exp-divider]
         [:span.exp-location date]]
        [:div.card-text
         text]
        [:a.card-link {:target "_blank" :href link}
         "See certificate"]]]])])

(defn projects-section [{projects :projects :as config}]
  [:div.row.justify-content-center
   (for [{:keys [name url photo description]} projects]
     [:div.col-md-4.mb-4
      [:div.card.w-shadow.p-0
       [:a.project-link {:href url}
        [:img.project-image {:src photo}]]

       [:div.card-body.project-body
        [:h4
         [:a.link {:href url} name]]
        [:div.card-description
         [:p description]]]]])])

(defn publications-section [{publications :publications :as config}]
  [:div.row.justify-content-center
   (for [{:keys [name url photo description]} publications]
     [:div.col-md-4.mb-4
      [:div.card.w-shadow.p-0
       [:a.project-link {:href url}
        [:img.project-image {:src photo}]]

       [:div.card-body.project-body
        [:h4
         [:a.link {:href url} name]]
        [:div.card-description
         [:p description]]]]])])

(defn cv-as-hiccup [{:keys [language] :as config}]
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
          [:span (get (:home titles) language)]]]
        [:li.nav-item
         [:a.nav-link {:href "#skills"
                       :data-target "#skills"}
          [:span (get (:skills titles) language)]]]
        [:li.nav-item
         [:a.nav-link {:href "#experience"
                       :data-target "#experience"}
          [:span (get (:experience titles) language)]]]
        [:li.nav-item
         [:a.nav-link {:data-target "#accomplishments"
                       :href "#accomplishments"}
          [:span (get (:accomplishments titles) language)]]]
        [:li.nav-item
         [:a.nav-link {:href "#projects"
                       :data-target "#projects"}
          [:span (get (:projects titles) language)]]]
        [:li.nav-item
         [:a.nav-link {:href "#publications"
                       :data-target "#publications"}
          [:span (get (:publications titles) language)]]]
        [:li.nav-item
         [:a.nav-link
          [:span (get (:resume titles) language)]]]]]]]
    (about-section config)
    [:section.about-section {:id "skills"}
     (skills-section config)]
    [:section.about-section.grey-area {:id "experience"}
     [:div.container
      [:div.row.pb-4
       [:div.col-lg-4
        [:h1.skills-header
         (get (:experience titles) language)]]
       (experience-section config)]]]
    [:section.about-section {:id "accomplishments"}
     [:div.container
      [:div.text-center.pb-5
       [:h1.skills-header (get (:accomplishments titles) language)]]
      (accomplishments-section config)]]
    [:section.about-section.grey-area {:id "projects"}
     [:div.container
      [:div.text-center.pb-5
       [:h1.skills-header (get (:projects titles) language)]]
      (projects-section config)]]
    [:section.about-section {:id "publications"}
     [:div.container
      [:div.text-center.pb-5
       [:h1.skills-header (get (:publications titles) language)]]
      (publications-section config)]]
    ]])
