package handler

import (
	"encoding/json"
	"io"
	"log"
	"net/http"
	"net/url"
	"os"
)

type LoginRequest struct {
	Username string `json:"username" binding:"required"`
	Password string `json:"password" binding:"required"`
}

func LoginHandler(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "Method Not Allowed", http.StatusMethodNotAllowed)
		return
	}

	var loginReq LoginRequest
	if err := json.NewDecoder(r.Body).Decode(&loginReq); err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	clientID := os.Getenv("KEYCLOAK_CLIENT_ID")
	clientSecret := os.Getenv("KEYCLOAK_CLIENT_SECRET")
	realm := os.Getenv("KEYCLOAK_REALM")
	keycloakBaseURL := os.Getenv("KEYCLOAK_BASE_URL") // vd: http://localhost:8080

	form := url.Values{}
	form.Add("grant_type", "password")
	form.Add("client_id", clientID)
	form.Add("username", loginReq.Username)
	form.Add("password", loginReq.Password)
	if clientSecret != "" {
		form.Add("client_secret", clientSecret)
	}

	tokenURL := keycloakBaseURL + "/realms/" + realm + "/protocol/openid-connect/token"

	resp, err := http.PostForm(tokenURL, form)
	if err != nil {
		log.Printf("Failed to call Keycloak: %v", err)
		http.Error(w, "Failed to contact auth server", http.StatusInternalServerError)
		return
	}
	defer resp.Body.Close()

	body, _ := io.ReadAll(resp.Body)

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(resp.StatusCode)
	w.Write(body)
}
