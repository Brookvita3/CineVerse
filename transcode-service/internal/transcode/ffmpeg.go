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
	// B0: Tạo thư mục chứa file tải về
	rawMoviesDir := "/rawmovies"
	if err := os.MkdirAll(rawMoviesDir, 0755); err != nil {
		fmt.Println("❌ Không thể tạo thư mục rawmovies:", err)
		return
	}

	// B1: Tải file từ MinIO về local
	fmt.Println("⬇️ Tải file từ MinIO:", filename)
	localInputPath := filepath.Join(rawMoviesDir, filename)
	err := downloader.DownloadFromMinio(filename, localInputPath, cfg)
	if err != nil {
		fmt.Println("❌ Lỗi tải file:", err)
		return
	}

	// B2: Chuẩn bị đường dẫn output
	nameOnly := strings.TrimSuffix(filepath.Base(filename), filepath.Ext(filename))
	outputDir := filepath.Join("/tmp/hls", nameOnly)
	outputFile := filepath.Join(outputDir, "index.m3u8")

	// B3: Tạo thư mục output
	if err := os.MkdirAll(outputDir, 0755); err != nil {
		fmt.Println("❌ Không thể tạo thư mục output:", err)
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
	fmt.Println("🎞️ Bắt đầu transcode:", filename)
	if err := cmd.Run(); err != nil {
		fmt.Println("❌ Transcode lỗi:", err)
		return
	}
	fmt.Println("✅ Transcode hoàn tất:", outputFile)

	// B5: Upload kết quả
	uploader.UploadFolder(outputDir, "movies/"+nameOnly, cfg)
}
