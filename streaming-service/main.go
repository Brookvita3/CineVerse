// main.go
package main

import (
	"log"
	"streaming-service/controller"

	"github.com/gin-gonic/gin"
	"github.com/joho/godotenv"
)

func main() {

	err := godotenv.Load()
	if err != nil {
		log.Fatal("Error loading .env file")
	}

	router := gin.Default()

	router.POST("/signed-url/upload", controller.GetSignedUploadURL)

	router.Run(":7070")
}
