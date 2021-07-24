(ns srv3.main
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.util.response :as response]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.reload :refer [wrap-reload]]
            [hiccup.core :as hiccup]
            [next.jdbc :as jdbc]
            [srv3.view :refer [index-view]]
            [srv3.data :refer [init-db! post-entry list-entries list-entries-paged]]))

(def page-size 5)

(def db-spec {:dbtype "h2:mem"})

(defmacro try?
  [a b]
  `(try ~a
        (catch Throwable _# ~b)))

(defn handle-post
  [{:keys [db] :as req}]
  (post-entry db (get-in req [:params "data"]))
  (response/redirect "/"))

(defn handle-get
  [{:keys [params db] :as req}]
  (let [page (try? (Integer/parseInt (get params "page")) 0)]
    (-> (list-entries-paged db {:limit page-size :offset (* page page-size)})
        index-view
        hiccup/html
        response/response
        (response/content-type "text/html"))))

(defn handler
  [{:keys [uri request-method] :as req}]
  (if (= "/" uri)
    (case request-method
      :post (handle-post req)
      :get (handle-get req))
    (response/not-found "not found")))

(defn wrap-db
  [app db options]
  (when (:init options)
    (init-db! db))
  #(app (assoc % :db db)))

(defn -main
  [& args]
  (with-open [db (jdbc/get-connection db-spec)]
    (-> (wrap-reload #'handler)
        wrap-params
        (wrap-db db {:init true})
        (run-jetty {:port 9500}))))
