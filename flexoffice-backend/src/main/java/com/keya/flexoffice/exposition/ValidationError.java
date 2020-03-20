package com.keya.flexoffice.exposition;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationError {
    private String code;
    private String message;
}
