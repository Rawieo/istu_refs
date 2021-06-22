(ns srv.handler.sign-in
  (:require [srv.view :as view]
            [hiccup.core :as hiccup]
            [ring.util.response :as response]))

(defn handle
  [req]
  (case (:request-method req)

    :get
    (-> (response/response (hiccup/html (view/sign-in-form)))
        (response/content-type "text/html"))

    :post
    (-> (response/redirect "/")
        (response/set-cookie "auth" (get-in req [:params "name"])))))