package com.mospaze.werehouse.model;

import lombok.*;

import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Builder
public class UploadCsvModel {
    private List<Object> skipped;
    private Integer success;
}
