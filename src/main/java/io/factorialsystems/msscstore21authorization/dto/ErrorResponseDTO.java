package io.factorialsystems.msscstore21authorization.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDTO {
    private String path;
    private HttpStatus status;
    private Instant dateTime;
    private String message;
}
