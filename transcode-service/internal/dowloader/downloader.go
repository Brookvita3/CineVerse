package downloader

import (
	"context"
	"fmt"

	"github.com/minio/minio-go/v7"
	"github.com/minio/minio-go/v7/pkg/credentials"
	"transcode-service/config"
)

func DownloadFromMinio(objectName, localPath string, cfg config.Config) error {
	minioClient, err := minio.New(cfg.MinioURL, &minio.Options{
		Creds:  credentials.NewStaticV4(cfg.MinioUser, cfg.MinioPass, ""),
		Secure: false,
	})
	if err != nil {
		return err
	}

	err = minioClient.FGetObject(context.Background(), cfg.MinioBucketDownload, objectName, localPath, minio.GetObjectOptions{})
	if err != nil {
		return fmt.Errorf("failed to download object: %w", err)
	}
	return nil
}
