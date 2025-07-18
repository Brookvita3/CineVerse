package config

import (
	"log"
	"os"

	"github.com/joho/godotenv"
)

type Config struct {
	RabbitURL           string
	QueueName           string
	MinioURL            string
	MinioUser           string
	MinioPass           string
	MinioBucketDownload string
	MinioBucketUpload   string
}

func Load() Config {
	err := godotenv.Load()
	if err != nil {
		log.Println("No .env file found, fallback to system env")
	}

	return Config{
		RabbitURL:           os.Getenv("RABBIT_URL"),
		QueueName:           os.Getenv("RABBIT_QUEUE"),
		MinioURL:            os.Getenv("MINIO_URL"),
		MinioUser:           os.Getenv("MINIO_USER"),
		MinioPass:           os.Getenv("MINIO_PASS"),
		MinioBucketDownload: os.Getenv("MINIO_BUCKET_DOWNLOAD"),
		MinioBucketUpload:   os.Getenv("MINIO_BUCKET_UPLOAD"),
	}
}
