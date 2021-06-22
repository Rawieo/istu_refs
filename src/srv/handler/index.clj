(ns srv.handler.index
  (:require [ring.util.response :as response]))

(defn handle
  [req]
  (let [cookies (:cookies req)
        auth-cookie (get cookies "auth")]
    (if auth-cookie
      (response/response ":)")
      (response/redirect "/sign-in"))))