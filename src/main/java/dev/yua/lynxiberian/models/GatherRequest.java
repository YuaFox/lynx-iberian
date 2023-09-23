package dev.yua.lynxiberian.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class GatherRequest {
    @Getter @Setter
    private Bucket bucket;
    @Getter @Setter
    private Filter filter;
}
