package api.domains.reserve_and_transact.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

//@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ReserveAndTransactRequest {

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("accountIdentifier")
    private String accountIdentifier;

    @JsonProperty("authCode")
    private String authCode;

    @JsonProperty("clientTxnRef")
    private String clientTxnRef;

    @JsonProperty("channelSessionId")
    private String channelSessionId;

    @JsonProperty("clientId")
    private String clientId;

    @JsonProperty("fundingSourceId")
    private String fundingSourceId;

    @JsonProperty("productId")
    private String productId;

    @JsonProperty("amount")
    private String amount;

    @JsonProperty("purchaseAmount")
    private String purchaseAmount;

    @JsonProperty("feeAmount")
    private String feeAmount;

    @JsonProperty("currencyCode")
    private String currencyCode;

    @JsonProperty("channelId")
    private String channelId;

    @JsonProperty("channelName")
    private String channelName;

    @JsonProperty("sourceIdentifier")
    private String sourceIdentifier;

    @JsonProperty("targetIdentifier")
    private String targetIdentifier;

    @JsonProperty("reserveFundsTxnRef")
    private String reserveFundsTxnRef;

    @JsonProperty("additionalData")
    private Map<String,Object> additionalData;

}
