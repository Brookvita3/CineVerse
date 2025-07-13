package jwks

import (
	"context"
	"errors"
	"log"

	"github.com/MicahParks/keyfunc/v3"
	"github.com/golang-jwt/jwt/v5"
)

type ResourceRole struct {
	Roles []string `json:"roles"`
}

type CustomClaims struct {
	PreferredUsername string                  `json:"preferred_username"`
	ResourceAccess    map[string]ResourceRole `json:"resource_access"`
	jwt.RegisteredClaims
}

var jwks keyfunc.Keyfunc

func Init(jwksURL string) {
	if jwksURL == "" {
		log.Fatal("JWKS URL is empty")
	}
	ctx := context.Background()

	var err error
	jwks, err = keyfunc.NewDefaultCtx(ctx, []string{jwksURL})
	if err != nil {
		log.Fatalf("Failed to initialize JWKS: %v", err)
	}
	log.Println("JWKS initialized")
}

func VerifyJWT(tokenStr string) (*CustomClaims, error) {
	if jwks == nil {
		return nil, errors.New("JWKS not initialized")
	}

	token, err := jwt.ParseWithClaims(tokenStr, &CustomClaims{}, jwks.Keyfunc)
	if err != nil {
		return nil, err
	}

	claims, ok := token.Claims.(*CustomClaims)
	if ok && token.Valid {
		return claims, nil
	}

	return nil, jwt.ErrTokenInvalidClaims
}
