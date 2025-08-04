package uploader

import (
	"context"
	"fmt"
	"os"
	"path/filepath"

	"github.com/minio/minio-go/v7"
	"github.com/minio/minio-go/v7/pkg/credentials"
	"transcode-service/config"
	"transcode-service/internal/publisher"
)

func UploadFolder(localDir, remotePrefix string, cfg config.Config) {
	client, err := minio.New(cfg.MinioURL, &minio.Options{
		Creds:  credentials.NewStaticV4(cfg.MinioUser, cfg.MinioPass, ""),
		Secure: false,
	})
	if err != nil {
		fmt.Println("Can't create MinIO client:", err)
		return
	}

	err = filepath.Walk(localDir, func(path string, info os.FileInfo, err error) error {
		if err != nil || info.IsDir() {
			return err
		}
		file, err := os.Open(path)
		if err != nil {
			return err
		}
		defer file.Close()

		relPath, _ := filepath.Rel(localDir, path)
		objectName := filepath.Join(remotePrefix, relPath)

		_, err = client.PutObject(context.Background(), cfg.MinioBucketUpload, objectName, file, info.Size(), minio.PutObjectOptions{})
		if err != nil {
			fmt.Println("Upload failed:", objectName, err)
		} else {
			fmt.Println("Upload successfully:", objectName)
		}
		return nil
	})

	folderName := filepath.Base(localDir)
	err = publisher.PublishUploadDone(cfg, folderName)

	if err != nil {
		fmt.Println("Upload folder failed:", err)
	}
}
