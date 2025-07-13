package config

import (
	"github.com/joho/godotenv"
	"log"
	"os"
)

type Config struct {
	Port          string
	KeycloakURL   string
	KeycloakRealm string
	ClientID      string
	ClientSecret  string
	JWKSURL       string
}

func Load() *Config {
	err := godotenv.Load()
	if err != nil {
		log.Println("No .env file found, fallback to system env")
	}

	cfg := &Config{
		Port:          os.Getenv("PORT"),
		KeycloakURL:   os.Getenv("KEYCLOAK_BASE_URL"),
		KeycloakRealm: os.Getenv("KEYCLOAK_REALM"),
		ClientID:      os.Getenv("KEYCLOAK_CLIENT_ID"),
		ClientSecret:  os.Getenv("KEYCLOAK_CLIENT_SECRET"),
		JWKSURL:       os.Getenv("JWKS_URL"),
	}

	return cfg
}
