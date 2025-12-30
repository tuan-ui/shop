package com.cms.dto.response;

import java.util.List;
import java.util.UUID;

import lombok.*;

@Data
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ErrorListResponse {
    private List<ErrorResponse> errors;
    private int total;
    private Boolean hasError;

    @Data
    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    public static class ErrorResponse {
        private UUID id;
        private String code;
        private String name;
        private String errorMessage;
    }
}
