package api.domains.simulator.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class SimulatorRequest {

    @JsonProperty("action")
    private String action;

    @JsonProperty("delay")
    private int delay;

    @JsonProperty("fieldName")
    private String fieldName;

    @JsonProperty("httpSatusCode")
    private int httpSatusCode;

    @JsonProperty("httpStatusCode")
    private int httpStatusCode;

    @JsonProperty("id")
    private String id;

    @JsonProperty("responseCode")
    private String responseCode;
}
