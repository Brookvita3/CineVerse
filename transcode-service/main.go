package main

import (
	"log"
	"transcode-service/config"
	"transcode-service/internal/consumer"
)

func main() {
	cfg := config.Load()

	err := consumer.StartRabbitMQListener(cfg)
	if err != nil {
		log.Fatalf("Error in start transcode server: %s", err)
	}
}
