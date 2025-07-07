package jwks

import (
	"context"
	"log"
	"os"

	"github.com/MicahParks/keyfunc/v3"
	"github.com/golang-jwt/jwt/v5"
)

func VerifyJWT(tokenStr string) (bool, error) {

	ctx := context.Background()
	jwksURL := os.Getenv("JWKS_URL")
	if jwksURL == "" {
		log.Fatal("JWKS_URL env is not set or empty")
	}

	jwks, err := keyfunc.NewDefaultCtx(ctx, []string{jwksURL})
	if err != nil {
		log.Fatalf("Failed to create JWKS Keyfunc: %v", err)
	}

	token, err := jwt.Parse(tokenStr, jwks.Keyfunc)
	if err != nil {
		return false, err
	}
	return token.Valid, nil
}
