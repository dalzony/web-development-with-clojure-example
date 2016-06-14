(ns ring-app.core
  (:require [ring.adapter.jetty :as jetty]
            [compojure.core :as compojure]
            [ring.util.http-response :as response]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.format :refer [wrap-restful-format]]))

(defn response-handler [request]
  (response/ok
   (str "<html><body> your IP is:"
        (:remote-addr request)
        "</body></html>")))

(defn display-profile [id]
  )

(defn display-settings [id]
  )

(defn change-password [id])

(compojure/defroutes user-routes
  (compojure/GET "/user/:id/profile" [id] (display-profile id))
  (compojure/GET "/user/:id/settings" [id] (display-settings id))
  (compojure/GET "/user/:id/change-password" [id] (change-password id)))

(compojure/defroutes handler
  (compojure/GET "/" request response-handler)
  (compojure/GET "/:id" [id] (str "<p>the id is: " id "</p>"))
  (compojure/POST "/json" [id] (response/ok {:result id})))

(defn wrap-nocache [handler]
  (fn [request]
    (-> request
        handler
        (assoc-in [:headers "Pragma"] "no-cache"))))

(defn wrap-formats [handler]
  (wrap-restful-format
   handler
   {:formats [:json-kw :transit-json :transit-msgpack]}))

(defn -main []
  (jetty/run-jetty
   (-> #'handler wrap-nocache wrap-reload wrap-formats)
   {:port 3000
    :join? false}))














