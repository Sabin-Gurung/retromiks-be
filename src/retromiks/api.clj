(ns retromiks.api
  (:require [muuntaja.core :as m]
            [reitit.coercion.malli]
            [reitit.ring :as rr]
            [reitit.ring.coercion :as coercion]
            [reitit.ring.middleware.exception :as exception]
            [reitit.ring.middleware.multipart :as multipart]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.parameters :as parameters]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.cors :as cors]
            [retromiks.context.spotify.adapter :as i-ad]))

(defn- -swagger-routes []
  ["" {:no-doc true}
   ["/swagger.json" {:get (swagger/create-swagger-handler)}]
   ["/api-docs/*" {:get (swagger-ui/create-swagger-ui-handler {:url "/swagger.json"})}]])

(defn create-handler []
  (rr/ring-handler
   (rr/router
    [""
     (i-ad/api-routes)
     (-swagger-routes)]
    {:data {:muuntaja m/instance
            :coercion reitit.coercion.malli/coercion
            :middleware [swagger/swagger-feature
                         parameters/parameters-middleware
                         muuntaja/format-negotiate-middleware
                         muuntaja/format-response-middleware
                         exception/exception-middleware
                         muuntaja/format-request-middleware
                         coercion/coerce-response-middleware
                         coercion/coerce-request-middleware
                         multipart/multipart-middleware
                         [cors/wrap-cors
                          :access-control-allow-origin [#".*"]
                          :access-control-allow-methods [:get :put :post :delete :options]]]}})
   (rr/create-default-handler)))

(comment
  (def app (create-handler))
  (rr/create-default-handler)
  (user/go)
  (->
    (app {:request-method :get :uri "/api/hello"})
    ;:body
    ;slurp
    ;(j/read-value j/keyword-keys-object-mapper)
    )
  (def server
    (jetty/run-jetty app {:port 3000 :join? false}))
  (.stop server)
  (@server)
  (app {:request-method :get :uri "/api/index.html"})
  (app {:request-method :get :uri "/plus"})
  (app {:request-method :get :uri "/openapi.json"})
  (app {:request-method :post :uri "/api-docs/index.html"})
  (app {:request-method :get :uri "/hello"}))
