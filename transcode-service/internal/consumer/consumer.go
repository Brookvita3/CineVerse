package consumer

import (
	"encoding/json"
	"fmt"
	"log"

	"transcode-service/config"
	"transcode-service/internal/transcode"

	amqp "github.com/rabbitmq/amqp091-go"
)

type TranscodeEvent struct {
	EventName string `json:"EventName"`
	Key       string `json:"Key"`
	Records   []struct {
		S3 struct {
			Bucket struct {
				Name string `json:"name"`
			} `json:"bucket"`
			Object struct {
				Key         string `json:"key"`
				Size        int64  `json:"size"`
				ContentType string `json:"contentType"`
			} `json:"object"`
		} `json:"s3"`
	} `json:"Records"`
}

func StartRabbitMQListener(cfg config.Config) error {

	conn, err := amqp.Dial(cfg.RabbitURL)
	if err != nil {
		return fmt.Errorf("failed to connect to RabbitMQ: %w", err)
	}

	ch, err := conn.Channel()
	if err != nil {
		return fmt.Errorf("failed to open channel: %w", err)
	}

	_, err = ch.QueueDeclare(
		cfg.QueueName, // name
		true,          // durable
		false,         // autoDelete
		false,         // exclusive
		false,         // noWait
		nil,           // arguments
	)
	if err != nil {
		return fmt.Errorf("failed to declare queue: %w", err)
	}

	err = ch.QueueBind(
		cfg.QueueName,  // queue name
		"transcode",    // routing key
		"upload-event", // exchange name
		false,          // noWait
		nil,            // args
	)
	if err != nil {
		return fmt.Errorf("failed to bind queue: %w", err)
	}

	msgs, err := ch.Consume(
		cfg.QueueName, "transcode-service", true, false, false, false, nil,
	)
	if err != nil {
		return fmt.Errorf("failed to register consumer: %w", err)
	}

	go func() {
		for msg := range msgs {

			var event TranscodeEvent
			if err := json.Unmarshal(msg.Body, &event); err != nil {
				log.Printf("Failed to parse MinIO event: %v\n", err)
				continue
			}
			filename := event.Records[0].S3.Object.Key
			log.Println("Receive new file", filename)

			go transcode.Process(filename, cfg)

		}
	}()

	log.Println("Listening event from RabbitMQ...")
	select {}
}
