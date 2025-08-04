package transcode

import (
	"fmt"
	"os"
	"os/exec"
	"path/filepath"
	"strings"

	"transcode-service/config"
	"transcode-service/internal/dowloader"
	"transcode-service/internal/uploader"
)

func Process(filename string, cfg config.Config) {
	// B0: Create rawmovies folder
	rawMoviesDir := "/rawmovies"
	if err := os.MkdirAll(rawMoviesDir, 0755); err != nil {
		fmt.Println("Can't create rawmovies folder:", err)
		return
	}

	// B1: Download from MinIO
	fmt.Println("Download from MinIO: ", filename)
	localInputPath := filepath.Join(rawMoviesDir, filename)
	err := downloader.DownloadFromMinio(filename, localInputPath, cfg)
	if err != nil {
		fmt.Println("error while downloading from MinIO: ", err)
		return
	}

	// B2: Prepare output dir
	nameOnly := strings.TrimSuffix(filepath.Base(filename), filepath.Ext(filename))
	outputDir := filepath.Join("/tmp/hls", nameOnly)
	outputFile := filepath.Join(outputDir, "index.m3u8")
	if err := os.MkdirAll(outputDir, 0755); err != nil {
		fmt.Println("error while creating output directory: ", err)
		return
	}

	// B4: Transcode
	cmd := exec.Command("ffmpeg",
		"-i", localInputPath,
		"-profile:v", "baseline",
		"-level", "3.0",
		"-start_number", "0",
		"-hls_time", "30",
		"-hls_list_size", "0",
		"-f", "hls",
		outputFile,
	)
	cmd.Stdout = os.Stdout
	cmd.Stderr = os.Stderr
	fmt.Printf("Transcoding %s...", filename)
	if err := cmd.Run(); err != nil {
		fmt.Println("error while transcoding: ", err)
		return
	}
	fmt.Println("Transcoding done", outputFile)

	// B5: Upload result to MinIO
	uploader.UploadFolder(outputDir, "movies/"+nameOnly, cfg)

	// B6: Delet file local after process
	if err := os.Remove(localInputPath); err != nil {
		fmt.Println("Failed to delete original file:", err)
	} else {
		fmt.Println("Deleted original file:", localInputPath)
	}
}
