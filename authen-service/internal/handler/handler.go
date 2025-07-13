package handler

import (
	"authen-service/config"
)

type Handler struct {
	Config *config.Config
}

func NewHandler(cfg *config.Config) *Handler {
	return &Handler{Config: cfg}
}
