(ns srv.handler.sign-out
  (:require [ring.util.response :as response]))

(defn handle
  [_]
  (-> (response/redirect "/sign-in")
      (response/set-cookie "auth" "" {:max-age 1})))