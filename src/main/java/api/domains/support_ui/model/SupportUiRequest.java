package api.domains.support_ui.model;

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

public class SupportUiRequest {

    @JsonProperty("raasTxnRef")
    private String raasTxnRef;

}
