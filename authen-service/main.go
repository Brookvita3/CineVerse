package main

import (
	"authen-service/config"
	"authen-service/internal/jwks"
	"authen-service/router"
	"log"
)

func main() {
	cfg := config.Load()

	jwks.Init(cfg.JWKSURL)

	r := router.SetupRouter(cfg)

	log.Println("Listening on {}", cfg.Port)
	r.Run(cfg.Port)
}
