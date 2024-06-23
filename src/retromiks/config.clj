(ns retromiks.config)

(declare config)

(defn set-config [conf]
  (def config conf)
  conf)

(defn unset-config []
  (def config nil))


(comment
  (-> config
    )
  )
