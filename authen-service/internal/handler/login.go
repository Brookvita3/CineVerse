package handler

import (
	"io"
	"log"
	"net/http"
	"net/url"

	"github.com/gin-gonic/gin"
)

type LoginRequest struct {
	Username string `json:"username" binding:"required"`
	Password string `json:"password" binding:"required"`
}

func (h *Handler) Login(c *gin.Context) {
	var req LoginRequest
	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Invalid input"})
		return
	}

	form := url.Values{}
	form.Add("grant_type", "password")
	form.Add("client_id", h.Config.ClientID)
	form.Add("username", req.Username)
	form.Add("password", req.Password)
	if h.Config.ClientSecret != "" {
		form.Add("client_secret", h.Config.ClientSecret)
	}

	tokenURL := h.Config.KeycloakURL + "/realms/" + h.Config.KeycloakRealm + "/protocol/openid-connect/token"

	resp, err := http.PostForm(tokenURL, form)
	if err != nil {
		log.Printf("Failed to call Keycloak: %v", err)
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to contact auth server"})
		return
	}
	defer resp.Body.Close()

	body, _ := io.ReadAll(resp.Body)

	c.Data(resp.StatusCode, "application/json", body)
}
