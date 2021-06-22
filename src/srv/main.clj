(ns srv.main
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.util.response :as response]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.cookies :refer [wrap-cookies]]
            [ring.middleware.reload :refer [wrap-reload]]
            [srv.handler.index :as handler.index]
            [srv.handler.sign-in :as handler.sign-in]
            [srv.handler.sign-out :as handler.sign-out]))

(defn handler-not-fount
  [_]
  (response/not-found "not found"))

(def routes
  {"/"         handler.index/handle
   "/sign-in"  handler.sign-in/handle
   "/sign-out" handler.sign-out/handle})

(defn handler
  [req]
  (apply (get routes (:uri req) handler-not-fount) [req]))

(def app
  (-> handler
      wrap-reload
      wrap-cookies
      wrap-params))

(defn -main
  [& args]
  (run-jetty #'app {:port 9500}))