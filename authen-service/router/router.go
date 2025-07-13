// router/router.go
package router

import (
	"authen-service/config"
	"authen-service/internal/handler"

	"github.com/gin-gonic/gin"
)

func SetupRouter(cfg *config.Config) *gin.Engine {
	r := gin.Default()

	h := &handler.Handler{
		Config: cfg,
	}

	r.POST("/login", h.Login)
	r.GET("/authen", h.Authen)

	return r
}
