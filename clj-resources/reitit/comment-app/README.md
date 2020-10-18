# reitit의 라우팅

	(def routes
	  [["/ping" {:name ::ping}]
	   ["/pong" ::pong]
	   ["/api {:a 1}
	    ["/users" ::users]
		["/users/:id" ::user-id]
		["/posts" ::posts]]])
		
	(def router
	  (ring/router routes))
	  
	(r/routes router)
	
	(r/match-by-path router "/ping")
	(r/match-by-path router "/api/users/1")
	(r/match-by-name router ::ping)

위 처럼 배열로 라우팅 할 수 있고, 
`::users` 처럼 라우팅 된 곳에 이름을 짖어할 수 있다.
패스나 이름 혹은 `{:a 1}` 처럼 임의로 만든 이름으로도 라우팅 된 곳을 찾을 수 있다.

## ring server 올리기

	(def routes
	  [["/ping" {:get (fn [req] {:status 200 :body "ok"})}]])
	  
	(def router
	  (ring/router routes))
	  
	(def app
	  (ring/ring-handler router))
	  
    (defn start []
	  (jetty/run-jetty #'app {:port 3000 :join? false})
	  (println "server running on 3000))

이렇게 만들면 아래처럼 실행 되는 것임

	(app {:request-method :get :url "/ping"})
	(start)
	
	
# 스웨거 붙이기
