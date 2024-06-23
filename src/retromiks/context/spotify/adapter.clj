(ns retromiks.context.spotify.adapter
  (:require [retromiks.context.spotify.core :as c]
            [jsonista.core :as j]
            [ring.util.response :as resp])
  )

(defn api-routes []
  ["/api" {:tags #{"spotify"}}

   ["/playlist/:id" {:get {:summary    "Get the playlist"
                            :parameters {:path [:map [:id string?]]}
                            :responses  {200 {:body map?}}
                            :handler    (fn [{{{id :id} :path} :parameters} ]
                                          (resp/response (c/playlist id))
                                          #_(resp/not-found {:message "resound not found"})
                                          )
                            }
                      }]
   ])


(comment
  (user/halt)
  (user/go)
  (user/reset-all)
  (m/list-all)
  (c/search "d7a50419-392b-43d2-bd73-6efd9920652b")
  (c/search "d7a50419-392b-43d2-bd73-6efd9920652b")

  )
