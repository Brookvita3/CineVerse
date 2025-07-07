package handler

import (
	"authen-service/jwks"
	"net/http"
	"strings"
)

func AuthHandler(w http.ResponseWriter, r *http.Request) {

	// CORS headers
	w.Header().Set("Access-Control-Allow-Origin", "*")
	w.Header().Set("Access-Control-Allow-Headers", "Authorization, Content-Type")
	w.Header().Set("Access-Control-Allow-Methods", "GET, OPTIONS")

	if r.Method != http.MethodGet {
		http.Error(w, "Method Not Allowed", http.StatusMethodNotAllowed)
		return
	}

	authHeader := r.Header.Get("Authorization")
	if !strings.HasPrefix(authHeader, "Bearer ") {
		http.Error(w, "Unauthorized", http.StatusUnauthorized)
		return
	}
	tokenStr := strings.TrimPrefix(authHeader, "Bearer ")

	valid, err := jwks.VerifyJWT(tokenStr)
	if err != nil {
		http.Error(w, "Forbidden", http.StatusForbidden)
		return
	}
	if valid {
		w.WriteHeader(http.StatusOK)
	} else {
		http.Error(w, "Internal Server Error", http.StatusInternalServerError)
	}
}
