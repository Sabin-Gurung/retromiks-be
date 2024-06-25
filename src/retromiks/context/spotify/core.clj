(ns retromiks.context.spotify.core
  (:require [clj-http.client :as client]
            [jsonista.core :as j]
            [retromiks.config :refer [config]]
            [clojure.core.cache.wrapped :as cache]
            )
  )

(defn- get-secrets []
  [(get-in config [:spotify :client_id]) (get-in config [:spotify :client_secret])])

(defn- get-token []
  (-> (client/post "https://accounts.spotify.com/api/token"
                   {:basic-auth (get-secrets)
                    :headers {"Content-Type" "application/x-www-form-urlencoded"}
                    :form-params {:grant_type "client_credentials"}
                    }
                   )
      :body
      (j/read-value j/keyword-keys-object-mapper)
      :access_token
      ))

(def get-token-with-cache
  (let [c (cache/ttl-cache-factory {} :ttl 3300000)]
    (fn []
      (cache/lookup-or-miss c :token (fn [_] (get-token)))))
  )

(defn- get-playlist [playlist-id token]
  (-> (client/get (str "https://api.spotify.com/v1/playlists/" playlist-id)
                  {:headers {"Authorization" (str "Bearer " token)}}
                  )
      :body
      (j/read-value)
      ))

(defn playlist [playlist-id]
  (get-playlist playlist-id (get-token-with-cache)))


(comment
  (get-secrets)
  (def token (get-token))
  (get-playlist "07sZADfVm00IF6f355IAyI" (get-token))
  (playlist "07sZADfVm00IF6f355IAyI")
  conf/config
  config
  (get-token-with-cache)

  (user/go)
  (user/reset)
  (get-secrets)
  (get {:c 12} :c)
  (-> @c1
      (get :d)

      )

  (let [tk (cache/through-cache c1 :token (fn [_] (get-token)))]
    (println tk)
    )


  (cache/lookup c1 :d)
  (cache/lookup c1 :c)
  (-> @c1
      ; (cache/through-cache :c (constantly 13))
      ; (cache/through-cache :d (constantly 13))

      ; :token
      ; (get 2)
      ; :token
      )
  (get @c1 :token)
  (cache/miss c1 2 2)
  (cache/hit c1 :token)
  (cache/lookup c1 :token)

  (->
    config
    )
  )
