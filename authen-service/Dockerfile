# --- Build stage ---
FROM golang:1.23 AS builder

WORKDIR /app
COPY . .
RUN go mod download
RUN CGO_ENABLED=0 GOOS=linux go build -o streaming-service .

# --- Run stage ---
FROM gcr.io/distroless/static

COPY --from=builder /app/streaming-service /
EXPOSE 9090
ENTRYPOINT ["/authen-service"]