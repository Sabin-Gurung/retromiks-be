(ns retromiks.system
  (:require [integrant.core :as ig]
            [integrant.core :as i]
            [ring.adapter.jetty :as jetty]
            [retromiks.api :refer [create-handler]]
            [aero.core :as a]
            )
  )

(def system-config
  {
   ; :app/db {:app-config (i/ref :app/config)}
   :app/config {:profile :default
                :source "config/config.edn"}
   :app/handler {:app-config (i/ref :app/config)
                 ; :app-db (i/ref :app/db)
                 }
   }
  )



(defmethod i/init-key :app/handler [_ config]
  (let [port (-> config :app-config :port)]
    (println "api : Started " port)
    (jetty/run-jetty (create-handler) {:port port :join? false})))

(defmethod i/halt-key! :app/handler [_ server]
  (.stop server)
  (println "api : Ended"))

(defmethod i/init-key :app/config [_ {:keys [source profile]}]
  (let [config (a/read-config (clojure.java.io/resource source) {:profile profile})]
    (println (str "config : " source " loaded. profile " (:profile config)))
    config))

(defmethod i/halt-key! :app/config [_ config]
  (println config)
  (println "config : Ended")
  )

(comment
  (+ 1 2)
  (def system (i/init system-config))
  system
  (i/halt! system)
  )
