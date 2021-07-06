(ns srv2.view
  (:require [hiccup.util :refer [escape-html]]))

(defn data-view-form
  [data]
  [:div
   [:p data]
   [:p (escape-html data)]
   [:form {:method :POST}
    [:textarea {:name "data"}]
    [:br]
    [:input {:type :submit}]]])