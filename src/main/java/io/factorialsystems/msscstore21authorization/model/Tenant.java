package io.factorialsystems.msscstore21authorization.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Tenant  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String description;
    private String secret;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private Boolean disabled;
}
