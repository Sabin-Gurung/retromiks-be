(ns user
  (:require [integrant.repl :as ig-repl]
            [retromiks.system :refer [system-config]]
            )
  )

(ig-repl/set-prep!
  (fn []
    (-> system-config
        (assoc-in [:app/config :profile] :dev)
        ;(assoc-in [:app/config :profile] :prod)
        )
    )
  )

(defn go [] (ig-repl/go))
(defn halt [] (ig-repl/halt))
(defn reset [] (ig-repl/reset))
(defn reset-all [] (ig-repl/reset-all))

(comment
  (go)
  (halt)
  (reset)
  (reset-all)
  )
