(ns srv2.main
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.util.response :as response]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.cookies :refer [wrap-cookies]]
            [ring.middleware.reload :refer [wrap-reload]]
            [hiccup.core :as hiccup]
            [srv2.view :refer [data-view-form]]))

(defonce data (atom ""))

(defn handle-post
  [req]
  (reset! data (get-in req [:params "data"]))
  (response/redirect "/"))

(defn handle-get
  [req]
  (-> @data
      data-view-form
      hiccup/html
      response/response
      (response/content-type "text/html")))

(defn handler
  [req]
  (case (:request-method req)
    :post (handle-post req)
    :get (handle-get req)))

(def app
  (-> handler
      wrap-reload
      wrap-cookies
      wrap-params))

(defn -main
  [& args]
  (run-jetty #'app {:port 9500}))