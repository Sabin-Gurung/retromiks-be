(ns retromiks.context.spotify.core
  (:require [clj-http.client :as client]
            [jsonista.core :as j]
            [retromiks.config :refer [config]]
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

(defn- get-playlist [playlist-id token]
  (-> (client/get (str "https://api.spotify.com/v1/playlists/" playlist-id)
                   {:headers {"Authorization" (str "Bearer " token)}}
                   )
      :body
      (j/read-value)
      ))

(defn playlist [playlist-id]
  (get-playlist playlist-id (get-token)))


(comment
  (def token (get-token))
  (get-playlist "07sZADfVm00IF6f355IAyI" (get-token))
  (playlist "07sZADfVm00IF6f355IAyI")
  conf/config

  (user/go)
  (user/reset)
  (get-secrets)

  (->
    config
    )
  )
