(ns srv.view)

(defn sign-in-form
  []
  [:form {:method :POST}
   [:input {:type :password :name "name"}]
   [:input {:type :submit}]])