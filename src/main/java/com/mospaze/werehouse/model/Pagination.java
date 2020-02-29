package com.mospaze.werehouse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Pagination {
    private Integer total;
    private Integer per_page;
    private Integer current_page;
    private Integer last_page;
    private Integer from;
    private Integer to;
}
