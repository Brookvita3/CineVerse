package handler

import (
	"authen-service/internal/jwks"
	"log"
	"net/http"
	"strings"

	"github.com/gin-gonic/gin"
)

func (h *Handler) Authen(c *gin.Context) {
	authHeader := c.GetHeader("Authorization")
	if !strings.HasPrefix(authHeader, "Bearer ") {
		c.JSON(http.StatusUnauthorized, gin.H{"error": "Missing or invalid Authorization header"})
		return
	}

	tokenStr := strings.TrimPrefix(authHeader, "Bearer ")
	claims, err := jwks.VerifyJWT(tokenStr)
	if err != nil {
		c.JSON(http.StatusForbidden, gin.H{"error": "Invalid token: " + err.Error()})
		return
	}

	roles := claims.ResourceAccess["movie-service"].Roles

	log.Printf("User %s has roles: %s", claims.PreferredUsername, strings.Join(roles, ","))
	c.Header("X-User", claims.PreferredUsername)
	c.Header("X-Role", strings.Join(roles, ","))
	c.Status(http.StatusOK)
}
