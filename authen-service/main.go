package main

import (
	"authen-service/handler"
	"log"
	"net/http"
	"os"

	"github.com/joho/godotenv"
)

func main() {
	err := godotenv.Load()
	if err != nil {
		log.Fatal("Error loading .env file")
	}

	http.HandleFunc("/auth", handler.AuthHandler)
	http.HandleFunc("/login", handler.LoginHandler)

	http.HandleFunc("/ping", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		_, _ = w.Write([]byte("pong"))
	})

	log.Println("Auth service listening on :9090...")
	log.Fatal(http.ListenAndServe(os.Getenv("PORT"), nil))
}
