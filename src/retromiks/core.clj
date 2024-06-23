  (ns retromiks.core
  (:require [clojure.string :as s]
            [retromiks.system :as sys]
            [integrant.core :as ig]
            [jsonista.core :as j])
  (:gen-class)
  )

(defn -main
  "launch the server"
  [& args]
  (let [profile (-> (first args)
                    (keyword)
                    #{:dev :prod}
                    (or :dev))]
    (-> sys/system-config
        (assoc-in [:app/config :profile] profile)
        (ig/init))))

(comment
  (def system (-main "dev"))
  sys/system-config
  (keyword ":hello")
  (#{:dev :prod} (keyword "test"))
  (s/upper-case "hello there")
  (j/write-value-as-string {:a "this is awesome"}))
