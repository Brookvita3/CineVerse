package publisher

import (
	"encoding/json"
	"fmt"
	"log"

	"transcode-service/config"

	amqp "github.com/rabbitmq/amqp091-go"
)

type UploadDoneEvent struct {
	Title string `json:"title"`
}

func PublishUploadDone(cfg config.Config, folderName string) error {
	conn, err := amqp.Dial(cfg.RabbitURL)
	if err != nil {
		return fmt.Errorf("failed to connect to RabbitMQ: %w", err)
	}
	defer conn.Close()

	ch, err := conn.Channel()
	if err != nil {
		return fmt.Errorf("failed to open channel: %w", err)
	}
	defer ch.Close()

	event := UploadDoneEvent{
		Title: folderName,
	}
	body, err := json.Marshal(event)
	if err != nil {
		return fmt.Errorf("failed to marshal event: %w", err)
	}

	err = ch.Publish(
		"upload-event", // default exchange
		"minio.upload", // routing key (queue name)
		false,          // mandatory
		false,          // immediate
		amqp.Publishing{
			ContentType: "application/json",
			Body:        body,
		},
	)
	if err != nil {
		return fmt.Errorf("failed to publish message: %w", err)
	}

	log.Println("Published upload-done message for:", folderName)
	return nil
}
