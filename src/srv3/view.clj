(ns srv3.view
  (:require [hiccup.util :refer [escape-html]]))

(defn index-view
  [data]
  [:html
   [:body
    [:pre (pr-str (type data))]
    [:ul
     (for [i data]
       [:li (escape-html i)])]
    [:form {:method :POST}
     [:textarea {:name "data"}]
     [:br]
     [:input {:type :submit}]]]])
