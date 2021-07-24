(ns srv3.data
  (:require [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]))

(defn init-db!
  [db]
  (jdbc/execute-one! db ["CREATE TABLE entries (
                            id int AUTO_INCREMENT PRIMARY KEY,
                            text VARCHAR(1024)
                          )"]))

(defn post-entry
  [db data]
  (jdbc/execute-one! db ["INSERT INTO entries (text) values (?)" data]))

(defn list-entries
  [db]
  (map :text (jdbc/execute! db ["SELECT (text) FROM entries"] {:builder-fn rs/as-unqualified-lower-maps})))

(defn list-entries-paged
  ([db options]
   (let [limit (get options :limit 100)
         offset (get options :offset 0)]
     (map :text (jdbc/execute! db ["SELECT (text) FROM entries ORDER BY id LIMIT ? OFFSET ?" limit offset]
                               {:builder-fn rs/as-unqualified-lower-maps}))))
  ([db]
   (list-entries-paged db nil)))
