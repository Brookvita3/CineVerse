package com.cineverse.movie_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MinioUploadDTO {

    @JsonProperty("Records")
    private List<Record> Records;

    @Data
    public static class Record {
        private String eventName;
        private String eventTime;
        private S3 s3;

        @Data
        public static class S3 {
            private Bucket bucket;
            private ObjectData object;

            @Data
            public static class Bucket {
                private String name;
            }

            @Data
            public static class ObjectData {
                private String key;
                private long size;
                private String contentType;
            }
        }
    }
}

