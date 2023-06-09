package api.domains.transact.repo;

import api.clients.ReserveAndTransactClient;
import api.domains.transact.model.TransactRequest;
import api.enums.ChannelId;
import api.enums.ChannelName;
import api.enums.CurrencyCode;

import java.util.LinkedHashMap;
import java.util.Map;

import static util.DateProvider.getCurrentIsoDateTime;


public class TransactRequestRepo {
    //V4
    public static TransactRequest setUpTransactV4Data(String clientId, CurrencyCode currencyCode,
                                                      ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataWithCSIDEmpty(String clientId, CurrencyCode currencyCode,
                                                                   ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataWithTSEmpty(String clientId, CurrencyCode currencyCode,
                                                                 ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp("")
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataWithRFTREmpty(String clientId, CurrencyCode currencyCode,
                                                                   ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataWithRCIEmpty(String clientId, CurrencyCode currencyCode,
                                                                  ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId("")
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataWithFSEmpty(String clientId, CurrencyCode currencyCode,
                                                                 ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataWithPIDEmpty(String clientId, CurrencyCode currencyCode,
                                                                  ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId("")
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataWithPAEmpty(String clientId, CurrencyCode currencyCode,
                                                                 ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataWithFAEmpty(String clientId, CurrencyCode currencyCode,
                                                                 ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataWithCCEmpty(String clientId, CurrencyCode currencyCode,
                                                                 ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode("")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataWithCNEmpty(String clientId, CurrencyCode currencyCode,
                                                                 ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName("")
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataWithCIDEmpty(String clientId, CurrencyCode currencyCode,
                                                                  ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId("")
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataWithSIEmpty(String clientId, CurrencyCode currencyCode,
                                                                 ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataWithCTIEmpty(String clientId, CurrencyCode currencyCode,
                                                                  ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("")
                .build();
    }

    //Max Limit Methods
    //V4

    public static TransactRequest setUpTransactV4DataReserveFundTxnMaxLimit(String clientId, CurrencyCode currencyCode,
                                                      ChannelName channelName, ChannelId channelId, String productId, String reserveFundTxnRef) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef(reserveFundTxnRef)//"200831235610408740004"
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataAccIdentifierMaxLimit(String clientId, CurrencyCode currencyCode,
                                                                            ChannelName channelName, ChannelId channelId, String productId, String accIdentifier) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier(accIdentifier)
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataClientTxnRefMaxLimit(String clientId, CurrencyCode currencyCode,
                                                                           ChannelName channelName, ChannelId channelId, String productId, String clientTxnRef) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef(clientTxnRef)//"1598914290004"
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataChannelSessionIdMaxLimit(String clientId, CurrencyCode currencyCode,
                                                                          ChannelName channelName, ChannelId channelId, String productId, String channelSessionID) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId(channelSessionID)//"20200831230000004"
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataAuthCodeMaxLimit(String clientId, CurrencyCode currencyCode,
                                                                              ChannelName channelName, ChannelId channelId, String productId, String authCode) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .authCode(authCode)
                .build();
    }

    public static TransactRequest setUpTransactV4DataTimestampMaxLimit(String clientId, CurrencyCode currencyCode,
                                                                      ChannelName channelName, ChannelId channelId, String productId, String timeStamp) {
        return TransactRequest.builder()
                .timestamp(timeStamp)//getCurrentIsoDateTime()
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataFundingSourceIdMaxLimit(String clientId, CurrencyCode currencyCode,
                                                                       ChannelName channelName, ChannelId channelId, String productId, String fundingSourceId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId(fundingSourceId)//"3"
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataPurchaseAmountMaxLimit(String clientId, CurrencyCode currencyCode,
                                                                             ChannelName channelName, ChannelId channelId, String productId, String purchaseAmount) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount(purchaseAmount)//"10000"
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataFeeAmountMaxLimit(String clientId, CurrencyCode currencyCode,
                                                                            ChannelName channelName, ChannelId channelId, String productId, String feeAmount) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount(feeAmount)//"0"
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataSourceIdentifierMaxLimit(String clientId, CurrencyCode currencyCode,
                                                                       ChannelName channelName, ChannelId channelId, String productId, String sourceIdentifier) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier(sourceIdentifier)
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataTargetIdentifierMaxLimit(String clientId, CurrencyCode currencyCode,
                                                                       ChannelName channelName, ChannelId channelId, String productId, String targetIdentifier) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier(targetIdentifier)
                .build();
    }


    //V3
    public static TransactRequest setUpTransactV3Data(String clientId, ChannelName channelName,
                                                      ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier(null)
                .reserveFundsTxnRef("90b91ada-df60-4a03-b0c3-c6bbf8381d09-0003")
                .channelSessionId("channelSessionId-0003")
                .clientTxnRef("toxR9F-0003")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    //V3 Transact for Empty cases
    public static TransactRequest setUpTransactV3DataWithCSIDEmpty(String clientId,
                                                                   ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier(null)
                .clientTxnRef("1598914290004")
                .channelSessionId("")
                .reserveFundsTxnRef("90b91ada-df60-4a03-b0c3-c6bbf8381d09-0003")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV3DataWithTSEmpty(String clientId,
                                                                 ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp("")
                .accountIdentifier(null)
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV3DataWithRFTREmpty(String clientId,
                                                                   ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier(null)
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV3DataWithRCIEmpty(String clientId,
                                                                  ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier(null)
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId("")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV3DataWithPIDEmpty(String clientId, ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier(null)
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .productId("")
                .amount("10000")
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV3DataWithPAEmpty(String clientId,
                                                                 ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier(null)
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .productId(productId)
                .amount("")
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV3DataWithFAEmpty(String clientId,
                                                                 ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier(null)
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount("")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV3DataWithCNEmpty(String clientId,
                                                                 ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier(null)
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName("")
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV3DataWithCIDEmpty(String clientId,
                                                                  ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier(null)
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .channelId("")
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV3DataWithSIEmpty(String clientId,
                                                                 ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier(null)
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV3DataWithCTIEmpty(String clientId,
                                                                  ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier(null)
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("")
                .build();
    }
    // V3 Transact for Null cases
    public static TransactRequest SetupBodyForTransactV3WithChannelSessionIDNull(String clientId, ChannelName channelName,
                                                      ChannelId channelId, String productId, String CSI) {
        return TransactRequest.builder()
                .channelSessionId(CSI)
                .timestamp(getCurrentIsoDateTime())
                .reserveFundsTxnRef("90b91ada-df60-4a03-b0c3-c6bbf8381d09-0003")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest SetupBodyForTransactV3WitTimeStampIDNull(String clientId, ChannelName channelName,
                                                                                        ChannelId channelId, String productId, String TS) {
        return TransactRequest.builder()
                .channelSessionId("channelSessionId-0003")
                .timestamp(TS)
                .reserveFundsTxnRef("90b91ada-df60-4a03-b0c3-c6bbf8381d09-0003")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }
    public static TransactRequest SetupBodyForTransactV3WithreserveFundsTxnRefNull(String clientId, ChannelName channelName,
                                                                                        ChannelId channelId, String productId, String RFTR) {
        return TransactRequest.builder()
                .channelSessionId("channelSessionId-0003")
                .timestamp(getCurrentIsoDateTime())
                .reserveFundsTxnRef(RFTR)
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }
    public static TransactRequest SetupBodyForTransactV3WithclientIdNull(String clientId, ChannelName channelName,
                                                                                        ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .channelSessionId("channelSessionId-0003")
                .timestamp(getCurrentIsoDateTime())
                .reserveFundsTxnRef("90b91ada-df60-4a03-b0c3-c6bbf8381d09-0003")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }
    public static TransactRequest SetupBodyForTransactV3WithproductIdNull(String clientId, ChannelName channelName,
                                                                                        ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .channelSessionId("channelSessionId-0003")
                .timestamp(getCurrentIsoDateTime())
                .reserveFundsTxnRef("90b91ada-df60-4a03-b0c3-c6bbf8381d09-0003")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }
    public static TransactRequest SetupBodyForTransactV3WithpurchaseAmountNull(String clientId, ChannelName channelName,
                                                                                        ChannelId channelId, String productId, String PA) {
        return TransactRequest.builder()
                .channelSessionId("channelSessionId-0003")
                .timestamp(getCurrentIsoDateTime())
                .reserveFundsTxnRef("90b91ada-df60-4a03-b0c3-c6bbf8381d09-0003")
                .clientId(clientId)
                .productId(productId)
                .amount(PA)
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }
    public static TransactRequest SetupBodyForTransactV3WithfeeAmountNull(String clientId, ChannelName channelName,
                                                                                        ChannelId channelId, String productId, String FA) {
        return TransactRequest.builder()
                .channelSessionId("channelSessionId-0003")
                .timestamp(getCurrentIsoDateTime())
                .reserveFundsTxnRef("90b91ada-df60-4a03-b0c3-c6bbf8381d09-0003")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount(FA)
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }
    public static TransactRequest SetupBodyForTransactV3WithchannelIdisNull(String clientId, ChannelName channelName,
                                                                                        String channelId, String productId) {
        return TransactRequest.builder()
                .channelSessionId("channelSessionId-0003")
                .timestamp(getCurrentIsoDateTime())
                .reserveFundsTxnRef("90b91ada-df60-4a03-b0c3-c6bbf8381d09-0003")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .channelId(channelId)
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }
    public static TransactRequest SetupBodyForTransactV3WithchannelNameNull(String clientId, String channelName,
                                                                                        ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .channelSessionId("channelSessionId-0003")
                .timestamp(getCurrentIsoDateTime())
                .reserveFundsTxnRef("90b91ada-df60-4a03-b0c3-c6bbf8381d09-0003")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName(channelName)
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }
    public static TransactRequest SetupBodyForTransactV3WithsourceIdentifierNull(String clientId, ChannelName channelName,
                                                                                        ChannelId channelId, String productId, String SI) {
        return TransactRequest.builder()
                .channelSessionId("channelSessionId-0003")
                .timestamp(getCurrentIsoDateTime())
                .reserveFundsTxnRef("90b91ada-df60-4a03-b0c3-c6bbf8381d09-0003")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier(SI)
                .targetIdentifier("2348038382068")
                .build();
    }
    public static TransactRequest SetupBodyForTransactV3WithtargetIdentifierNull(String clientId, ChannelName channelName,
                                                                                        ChannelId channelId, String productId, String TI) {
        return TransactRequest.builder()
                .channelSessionId("channelSessionId-0003")
                .timestamp(getCurrentIsoDateTime())
                .reserveFundsTxnRef("90b91ada-df60-4a03-b0c3-c6bbf8381d09-0003")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier(null)
                .build();
    }

    //Max Limit Methods
    //V3
    public static TransactRequest setUpTransactV3DataReserveFundTxRefMaxLimit(String clientId, ChannelName channelName,
                                                      ChannelId channelId, String productId, String reserveFundTxn) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier(null)
                .reserveFundsTxnRef(reserveFundTxn)
                .channelSessionId("channelSessionId-0003")
                .clientTxnRef("toxR9F-0003")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV3DataAccIdentifierMaxLimit(String clientId, ChannelName channelName,
                                                                              ChannelId channelId, String productId, String accountIdentifier) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier(accountIdentifier)
                .reserveFundsTxnRef("90b91ada-df60-4a03-b0c3-c6bbf8381d09-0003")
                .channelSessionId("channelSessionId-0003")
                .clientTxnRef("toxR9F-0003")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV3DataClientTxnRefMaxLimit(String clientId, ChannelName channelName,
                                                                           ChannelId channelId, String productId, String clientTxnRef) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier(null)
                .reserveFundsTxnRef("90b91ada-df60-4a03-b0c3-c6bbf8381d09-0003")
                .channelSessionId("channelSessionId-0003")
                .clientTxnRef(clientTxnRef)
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV3DataChannelSessionIdMaxLimit(String clientId, ChannelName channelName,
                                                                          ChannelId channelId, String productId, String channelSessionID) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier(null)
                .reserveFundsTxnRef("90b91ada-df60-4a03-b0c3-c6bbf8381d09-0003")
                .channelSessionId(channelSessionID)
                .clientTxnRef("toxR9F-0003")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV3DataAuthCodeMaxLimit(String clientId, ChannelName channelName,
                                                                              ChannelId channelId, String productId, String authCode) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier(null)
                .reserveFundsTxnRef("90b91ada-df60-4a03-b0c3-c6bbf8381d09-0003")
                .channelSessionId("channelSessionId-0003")
                .clientTxnRef("toxR9F-0003")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .authCode(authCode)
                .build();
    }

    public static TransactRequest setUpTransactV3DataTimeStampMaxLimit(String clientId, ChannelName channelName,
                                                                      ChannelId channelId, String productId, String timeStamp) {
        return TransactRequest.builder()
                .timestamp(timeStamp)
                .accountIdentifier(null)
                .reserveFundsTxnRef("90b91ada-df60-4a03-b0c3-c6bbf8381d09-0003")
                .channelSessionId("channelSessionId-0003")
                .clientTxnRef("toxR9F-0003")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV3DataPurchaseAmountMaxLimit(String clientId, ChannelName channelName,
                                                                       ChannelId channelId, String productId, String PurchaseAmount) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier(null)
                .reserveFundsTxnRef("90b91ada-df60-4a03-b0c3-c6bbf8381d09-0003")
                .channelSessionId("channelSessionId-0003")
                .clientTxnRef("toxR9F-0003")
                .clientId(clientId)
                .productId(productId)
                .amount(PurchaseAmount)
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV3DataFeeAmountMaxLimit(String clientId, ChannelName channelName,
                                                                            ChannelId channelId, String productId, String feeAmount) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier(null)
                .reserveFundsTxnRef("90b91ada-df60-4a03-b0c3-c6bbf8381d09-0003")
                .channelSessionId("channelSessionId-0003")
                .clientTxnRef("toxR9F-0003")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount(feeAmount)
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV3DataSourceIdentifierMaxLimit(String clientId, ChannelName channelName,
                                                                       ChannelId channelId, String productId, String sourceIdentifier) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier(null)
                .reserveFundsTxnRef("90b91ada-df60-4a03-b0c3-c6bbf8381d09-0003")
                .channelSessionId("channelSessionId-0003")
                .clientTxnRef("toxR9F-0003")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier(sourceIdentifier)
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV3DataTargetIdentifierMaxLimit(String clientId, ChannelName channelName,
                                                                       ChannelId channelId, String productId, String targetIdentifier) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier(null)
                .reserveFundsTxnRef("90b91ada-df60-4a03-b0c3-c6bbf8381d09-0003")
                .channelSessionId("channelSessionId-0003")
                .clientTxnRef("toxR9F-0003")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier(targetIdentifier)
                .build();
    }

    //Max Limit Methods
    //V1

    public static TransactRequest setUpTransactV1DataReserveFundsTxnRefMaxLimit(String clientId, ChannelName channelName, ChannelId channelId,
                                                      String productId, String reserveFundsTxnRef) {
        return TransactRequest.builder()
                .accountIdentifier("4000-xxxx-xxxx-01-0004")
                .authCode(null)
                .clientTxnRef(ReserveAndTransactClient.clientTxnRefV1)
                .channelSessionId(ReserveAndTransactClient.channelSessionIdV1)
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000) //could possibly add amount as a parameter as well?
                .channelId(channelId.getChannelId()) //7
                .channelName(channelName.getChannelName()) //USSD
                .sourceIdentifier(ReserveAndTransactClient.IdentifierV1)
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .reserveFundsTxnRef(reserveFundsTxnRef)
                .build();
    }

    public static TransactRequest setUpTransactV1DataClientTxnRefMaxLimit(String clientId, ChannelName channelName, ChannelId channelId,
                                                                                String productId, String clientTxnref) {
        return TransactRequest.builder()
                .accountIdentifier("4000-xxxx-xxxx-01-0004")
                .authCode(null)
                .clientTxnRef(clientTxnref)//ReserveAndTransactClient.clientTxnRefV1
                .channelSessionId(ReserveAndTransactClient.channelSessionIdV1)
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000) //could possibly add amount as a parameter as well?
                .channelId(channelId.getChannelId()) //7
                .channelName(channelName.getChannelName()) //USSD
                .sourceIdentifier(ReserveAndTransactClient.IdentifierV1)
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .build();
    }

    public static TransactRequest setUpTransactV1DataChannelSessionIdMaxLimit(String clientId, ChannelName channelName, ChannelId channelId,
                                                                          String productId, String channelSessionID) {
        return TransactRequest.builder()
                .accountIdentifier("4000-xxxx-xxxx-01-0004")
                .authCode(null)
                .clientTxnRef(ReserveAndTransactClient.clientTxnRefV1)
                .channelSessionId(channelSessionID)//ReserveAndTransactClient.channelSessionIdV1
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000) //could possibly add amount as a parameter as well?
                .channelId(channelId.getChannelId()) //7
                .channelName(channelName.getChannelName()) //USSD
                .sourceIdentifier(ReserveAndTransactClient.IdentifierV1)
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .build();
    }
    public static TransactRequest setUpTransactV1DataChannelSessionIdEmpty(String clientId, ChannelName channelName, ChannelId channelId,
                                                                              String productId, String channelSessionID) {
        return TransactRequest.builder()
                .accountIdentifier("4000-xxxx-xxxx-01-0004")
                .authCode(null)
                .clientTxnRef(ReserveAndTransactClient.clientTxnRefV1)
                .channelSessionId("")//ReserveAndTransactClient.channelSessionIdV1
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000) //could possibly add amount as a parameter as well?
                .channelId(channelId.getChannelId()) //7
                .channelName(channelName.getChannelName()) //USSD
                .sourceIdentifier(ReserveAndTransactClient.IdentifierV1)
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .build();
    }

    public static TransactRequest setUpTransactV1DataTimeStampIdMaxLimit(String clientId, ChannelName channelName, ChannelId channelId,
                                                                              String productId, String timeStamp) {
        return TransactRequest.builder()
                .accountIdentifier("4000-xxxx-xxxx-01-0004")
                .authCode(null)
                .clientTxnRef(ReserveAndTransactClient.clientTxnRefV1)
                .channelSessionId(ReserveAndTransactClient.channelSessionIdV1)
                .timestamp(timeStamp)
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000) //could possibly add amount as a parameter as well?
                .channelId(channelId.getChannelId()) //7
                .channelName(channelName.getChannelName()) //USSD
                .sourceIdentifier(ReserveAndTransactClient.IdentifierV1)
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .build();
    }

    public static TransactRequest setUpTransactV1DataPurchaseAmountMaxLimit(String clientId, ChannelName channelName, ChannelId channelId,
                                                                         String productId, String purchaseAmount) {
        return TransactRequest.builder()
                .accountIdentifier("4000-xxxx-xxxx-01-0004")
                .authCode(null)
                .clientTxnRef(ReserveAndTransactClient.clientTxnRefV1)
                .channelSessionId(ReserveAndTransactClient.channelSessionIdV1)
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(purchaseAmount) //ReserveAndTransactClient.PurchaseAmount10000
                .channelId(channelId.getChannelId()) //7
                .channelName(channelName.getChannelName()) //USSD
                .sourceIdentifier(ReserveAndTransactClient.IdentifierV1)
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .build();
    }

        public static TransactRequest setUpTransactV1DataSourceIdentifierMaxLimit(String clientId, ChannelName channelName, ChannelId channelId,
                                                                         String productId, String sourceIdentifier) {
        return TransactRequest.builder()
                .accountIdentifier("4000-xxxx-xxxx-01-0004")
                .authCode(null)
                .clientTxnRef(ReserveAndTransactClient.clientTxnRefV1)
                .channelSessionId(ReserveAndTransactClient.channelSessionIdV1)
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000)
                .channelId(channelId.getChannelId()) //7
                .channelName(channelName.getChannelName()) //USSD
                .sourceIdentifier(sourceIdentifier)
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .build();
    }
    public static TransactRequest setUpTransactV1DataSourceIdentifierEmpty(String clientId, ChannelName channelName, ChannelId channelId,
                                                                              String productId, String sourceIdentifier) {
        return TransactRequest.builder()
                .accountIdentifier("4000-xxxx-xxxx-01-0004")
                .authCode(null)
                .clientTxnRef(ReserveAndTransactClient.clientTxnRefV1)
                .channelSessionId(ReserveAndTransactClient.channelSessionIdV1)
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000)
                .channelId(channelId.getChannelId()) //7
                .channelName(channelName.getChannelName()) //USSD
                .sourceIdentifier("")
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .build();
    }

    public static TransactRequest setUpTransactV1DataTargetIdentifierMaxLimit(String clientId, ChannelName channelName, ChannelId channelId,
                                                                            String productId, String TargetIdentifier) {
        return TransactRequest.builder()
                .accountIdentifier("4000-xxxx-xxxx-01-0004")
                .authCode(null)
                .clientTxnRef(ReserveAndTransactClient.clientTxnRefV1)
                .channelSessionId(ReserveAndTransactClient.channelSessionIdV1)
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000)
                .channelId(channelId.getChannelId()) //7
                .channelName(channelName.getChannelName()) //USSD
                .sourceIdentifier(ReserveAndTransactClient.IdentifierV1)
                .targetIdentifier(TargetIdentifier)
                .build();
    }
    public static TransactRequest setUpTransactV1DataWithChannelNameNull(String clientId, ChannelName channelName, ChannelId channelId,
                                                                              String productId){
        return TransactRequest.builder()
                .accountIdentifier(null)
                .authCode(null)
                .clientTxnRef(ReserveAndTransactClient.clientTxnRefV1)
                .channelSessionId(ReserveAndTransactClient.channelSessionIdV1)
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000) //could possibly add amount as a parameter as well?
                .channelId(channelId.getChannelId()) //7
                .channelName(null) //USSD
                .sourceIdentifier(ReserveAndTransactClient.IdentifierV1)
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .build();
    }
    public static TransactRequest setUpTransactV1DataAuthCodeMaxLimit(String clientId, ChannelName channelName, ChannelId channelId,
                                                                              String productId, String TargetIdentifier, String AuthCode) {
        return TransactRequest.builder()
                .accountIdentifier("4000-xxxx-xxxx-01-0004")
                .authCode(AuthCode)
                .clientTxnRef(ReserveAndTransactClient.clientTxnRefV1)
                .channelSessionId(ReserveAndTransactClient.channelSessionIdV1)
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000)
                .channelId(channelId.getChannelId()) //7
                .channelName(channelName.getChannelName()) //USSD
                .sourceIdentifier(ReserveAndTransactClient.IdentifierV1)
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .build();
    }


    //V2
    public static TransactRequest setUpTransactV2Data(String clientId, ChannelName channelName,
                                                      ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .accountIdentifier("222222xxxxxx0003")
                .clientTxnRef("30234241786MgAUs-0002")
                .channelSessionId("d5d65725c1414446b8546c5fcd5-0002")
                .timestamp(getCurrentIsoDateTime())
                .reserveFundsTxnRef("601502164444630590-0002")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382069")
                .targetIdentifier("2348038382069")
                .build();
    }

    public static TransactRequest setUpTransactV2DataWithCSIDEmpty(String clientId,
                                                                   ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .accountIdentifier("222222xxxxxx0003")
                .clientTxnRef("30234241786MgAUs-0002")
                .channelSessionId("")
                .timestamp(getCurrentIsoDateTime())
                .reserveFundsTxnRef("601502164444630590-0002")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV2DataWithTSEmpty(String clientId,
                                                                 ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp("")
                .accountIdentifier("222222xxxxxx0003")
                .clientTxnRef("30234241786MgAUs-0002")
                .channelSessionId("d5d65725c1414446b8546c5fcd5-0002")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV2DataWithRFTREmpty(String clientId,
                                                                   ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("222222xxxxxx0003")
                .clientTxnRef("30234241786MgAUs-0002")
                .channelSessionId("d5d65725c1414446b8546c5fcd5-0002")
                .reserveFundsTxnRef("")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV2DataWithRCIDEmpty(String clientId,
                                                                   ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("222222xxxxxx0003")
                .clientTxnRef("30234241786MgAUs-0002")
                .channelSessionId("d5d65725c1414446b8546c5fcd5-0002")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId("")
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }


    public static TransactRequest setUpTransactV2DataWithPIDEmpty(String clientId,
                                                                  ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("222222xxxxxx0003")
                .clientTxnRef("30234241786MgAUs-0002")
                .channelSessionId("d5d65725c1414446b8546c5fcd5-0002")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId("")
                .amount("10000")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV2DataWithPAEmpty(String clientId,
                                                                 ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("222222xxxxxx0003")
                .clientTxnRef("30234241786MgAUs-0002")
                .channelSessionId("d5d65725c1414446b8546c5fcd5-0002")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }


    public static TransactRequest setUpTransactV2DataWithCNEmpty(String clientId,
                                                                 ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("222222xxxxxx0003")
                .clientTxnRef("30234241786MgAUs-0002")
                .channelSessionId("d5d65725c1414446b8546c5fcd5-0002")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .channelId(channelId.getChannelId())
                .channelName("")
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV2DataWithCIDEmpty(String clientId,
                                                                  ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("222222xxxxxx0003")
                .clientTxnRef("30234241786MgAUs-0002")
                .channelSessionId("d5d65725c1414446b8546c5fcd5-0002")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .channelId("")
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV2DataWithSIEmpty(String clientId,
                                                                 ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("222222xxxxxx0003")
                .clientTxnRef("30234241786MgAUs-0002")
                .channelSessionId("d5d65725c1414446b8546c5fcd5-0002")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV2DataWithCTIEmpty(String clientId,
                                                                  ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("222222xxxxxx0003")
                .clientTxnRef("30234241786MgAUs-0002")
                .channelSessionId("d5d65725c1414446b8546c5fcd5-0002")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("")
                .build();
    }

    //V1 Empty
    public static TransactRequest setUpTransactV1Data(String clientId, ChannelName channelName, ChannelId channelId,
                                                      String productId) {
        return TransactRequest.builder()
                .accountIdentifier("4000-xxxx-xxxx-01-0004")
                .authCode(null)
                .clientTxnRef(ReserveAndTransactClient.clientTxnRefV1)
                .channelSessionId(ReserveAndTransactClient.channelSessionIdV1)
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000) //could possibly add amount as a parameter as well?
                .channelId(channelId.getChannelId()) //7
                .channelName(channelName.getChannelName()) //USSD
                .sourceIdentifier(ReserveAndTransactClient.IdentifierV1)
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .build();
    }


    public static TransactRequest setUpTransactV1DataPurchaseAmount(String clientId, ChannelName channelName, ChannelId channelId,
                                                      String productId, String purchaseAmount){
        return TransactRequest.builder()
                .accountIdentifier(null)
                .authCode(null)
                .clientTxnRef(ReserveAndTransactClient.clientTxnRefV1)
                .channelSessionId(ReserveAndTransactClient.channelSessionIdV1)
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(purchaseAmount) //could possibly add amount as a parameter as well?
                .channelId(channelId.getChannelId()) //7
                .channelName(channelName.getChannelName()) //USSD
                .sourceIdentifier(ReserveAndTransactClient.IdentifierV1)
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .build();
    }

    public static TransactRequest setUpTransactV1DataSourceIdentifier(String clientId, ChannelName channelName, ChannelId channelId,
                                                                    String productId, String sourceIdentifier){
        return TransactRequest.builder()
                .accountIdentifier(null)
                .authCode(null)
                .clientTxnRef(ReserveAndTransactClient.clientTxnRefV1)
                .channelSessionId(ReserveAndTransactClient.channelSessionIdV1)
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000) //could possibly add amount as a parameter as well?
                .channelId(channelId.getChannelId()) //7
                .channelName(channelName.getChannelName()) //USSD
                .sourceIdentifier(sourceIdentifier)
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .build();
    }

    public static TransactRequest setUpTransactV1DataTargetIdentifier(String clientId, ChannelName channelName, ChannelId channelId,
                                                                      String productId, String targetIdentifier){
        return TransactRequest.builder()
                .accountIdentifier(null)
                .authCode(null)
                .clientTxnRef(ReserveAndTransactClient.clientTxnRefV1)
                .channelSessionId(ReserveAndTransactClient.channelSessionIdV1)
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000) //could possibly add amount as a parameter as well?
                .channelId(channelId.getChannelId()) //7
                .channelName(channelName.getChannelName()) //USSD
                .sourceIdentifier(ReserveAndTransactClient.IdentifierV1)
                .targetIdentifier(targetIdentifier)
                .build();
    }

    public static TransactRequest setUpTransactV1DataWithCIDEmpty(String clientId, ChannelId channelId,
                                                                  String productId) {
        return TransactRequest.builder()
                .reserveFundsTxnRef("601502164444630590-0002")
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000) //could possibly add amount as a parameter as well?
                .channelId(channelId.getChannelId()) //7
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .build();
    }

    public static TransactRequest setUpTransactV1DataWithPIDEmpty(String clientId, ChannelId channelId,
                                                                  String productId) {
        return TransactRequest.builder()
                .reserveFundsTxnRef("601502164444630590-0002")
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000) //could possibly add amount as a parameter as well?
                .channelId(channelId.getChannelId()) //7
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .build();
    }

    public static TransactRequest setUpTransactV1DataWithPAEmpty(String clientId, ChannelId channelId,
                                                                 String productId, String PurchaseAmount) {
        return TransactRequest.builder()
                .reserveFundsTxnRef("601502164444630590-0002")
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(PurchaseAmount) //could possibly add amount as a parameter as well?
                .channelId(channelId.getChannelId()) //7
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .build();
    }

    public static TransactRequest setUpTransactV1DataWithChannelIDEmpty(String clientId, String channelId,
                                                                        String productId) {
        return TransactRequest.builder()
                .reserveFundsTxnRef("601502164444630590-0002")
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000) //could possibly add amount as a parameter as well?
                .channelId(channelId) //7
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .build();
    }

    public static TransactRequest setUpTransactV1DataWithTargetIdentifierEmpty(String clientId, ChannelId channelId,
                                                                               String productId, String TargetIdentifier) {
        return TransactRequest.builder()
                .reserveFundsTxnRef("601502164444630590-0002")
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000) //could possibly add amount as a parameter as well?
                .channelId(channelId.getChannelId()) //7
                .targetIdentifier(TargetIdentifier)
                .build();
    }
    public static TransactRequest setUpTransactV1DataWithChannelNameEmpty(String clientId, String channelName, ChannelId channelId,
                                                      String productId) {
        return TransactRequest.builder()
                .accountIdentifier("4000-xxxx-xxxx-01-0004")
                .authCode(null)
                .clientTxnRef(ReserveAndTransactClient.clientTxnRefV1)
                .channelSessionId(ReserveAndTransactClient.channelSessionIdV1)
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000) //could possibly add amount as a parameter as well?
                .channelId(channelId.getChannelId()) //7
                .channelName("") //USSD
                .sourceIdentifier(ReserveAndTransactClient.IdentifierV1)
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .build();
    }
    public static TransactRequest setUpTransactV1DataWithAccIdentifierEmpty(String clientId, ChannelName channelName, ChannelId channelId,
                                                                          String productId, String accIdentifier) {
        return TransactRequest.builder()
                .accountIdentifier(accIdentifier)
                .authCode(null)
                .clientTxnRef(ReserveAndTransactClient.clientTxnRefV1)
                .channelSessionId(ReserveAndTransactClient.channelSessionIdV1)
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000) //could possibly add amount as a parameter as well?
                .channelId(channelId.getChannelId()) //7
                .sourceIdentifier(ReserveAndTransactClient.IdentifierV1)
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .build();
    }

    //Max Limit Methods
    //V2

    public static TransactRequest setUpTransactV2DataReserveFundsTxnRefMaxLimit(String clientId, ChannelName channelName,
                                                      ChannelId channelId, String productId, String reserveFundsTxnRef) {
        return TransactRequest.builder()
                .accountIdentifier("222222xxxxxx0003")
                .clientTxnRef("30234241786MgAUs-0002")
                .channelSessionId("d5d65725c1414446b8546c5fcd5-0002")
                .timestamp(getCurrentIsoDateTime())
                .reserveFundsTxnRef(reserveFundsTxnRef)//"601502164444630590-0002"
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382069")
                .targetIdentifier("2348038382069")
                .build();
    }

    public static TransactRequest setUpTransactV2DataAccountIdentifierMaxLimit(String clientId, ChannelName channelName,
                                                                                ChannelId channelId, String productId, String accountIdentifier) {
        return TransactRequest.builder()
                .accountIdentifier(accountIdentifier)//"222222xxxxxx0003"
                .clientTxnRef("30234241786MgAUs-0002")
                .channelSessionId("d5d65725c1414446b8546c5fcd5-0002")
                .timestamp(getCurrentIsoDateTime())
                .reserveFundsTxnRef("601502164444630590-0002")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382069")
                .targetIdentifier("2348038382069")
                .build();
    }

    public static TransactRequest setUpTransactV2DataClientTxnRefMaxLimit(String clientId, ChannelName channelName,
                                                                               ChannelId channelId, String productId, String clientTxnRef) {
        return TransactRequest.builder()
                .accountIdentifier("222222xxxxxx0003")//
                .clientTxnRef(clientTxnRef)//"30234241786MgAUs-0002"
                .channelSessionId("d5d65725c1414446b8546c5fcd5-0002")
                .timestamp(getCurrentIsoDateTime())
                .reserveFundsTxnRef("601502164444630590-0002")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382069")
                .targetIdentifier("2348038382069")
                .build();
    }

    public static TransactRequest setUpTransactV2DataChannelSessionIdMaxLimit(String clientId, ChannelName channelName,
                                                                          ChannelId channelId, String productId, String channelSessionId) {
        return TransactRequest.builder()
                .accountIdentifier("222222xxxxxx0003")//
                .clientTxnRef("30234241786MgAUs-0002")//
                .channelSessionId(channelSessionId)//"d5d65725c1414446b8546c5fcd5-0002"
                .timestamp(getCurrentIsoDateTime())
                .reserveFundsTxnRef("601502164444630590-0002")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382069")
                .targetIdentifier("2348038382069")
                .build();
    }

    public static TransactRequest setUpTransactV2DataAuthCodeIdMaxLimit(String clientId, ChannelName channelName,
                                                                              ChannelId channelId, String productId, String authCode) {
        return TransactRequest.builder()
                .accountIdentifier("222222xxxxxx0003")
                .clientTxnRef("30234241786MgAUs-0002")
                .channelSessionId("d5d65725c1414446b8546c5fcd5-0002")
                .timestamp(getCurrentIsoDateTime())
                .reserveFundsTxnRef("601502164444630590-0002")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382069")
                .targetIdentifier("2348038382069")
                .authCode(authCode)
                .build();
    }

    public static TransactRequest setUpTransactV2DataTimestampMaxLimit(String clientId, ChannelName channelName,
                                                                        ChannelId channelId, String productId, String timestamp) {
        return TransactRequest.builder()
                .accountIdentifier("222222xxxxxx0003")
                .clientTxnRef("30234241786MgAUs-0002")
                .channelSessionId("d5d65725c1414446b8546c5fcd5-0002")
                .timestamp(timestamp)//getCurrentIsoDateTime()
                .reserveFundsTxnRef("601502164444630590-0002")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382069")
                .targetIdentifier("2348038382069")
                .build();
    }

    public static TransactRequest setUpTransactV2DataPurchaseAmountMaxLimit(String clientId, ChannelName channelName,
                                                                            ChannelId channelId, String productId, String purchaseAmount) {
        return TransactRequest.builder()
                .accountIdentifier("222222xxxxxx0003")
                .clientTxnRef("30234241786MgAUs-0002")
                .channelSessionId("d5d65725c1414446b8546c5fcd5-0002")
                .timestamp(getCurrentIsoDateTime())
                .reserveFundsTxnRef("601502164444630590-0002")
                .clientId(clientId)
                .productId(productId)
                .amount(purchaseAmount)//"10000"
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382069")
                .targetIdentifier("2348038382069")
                .build();
    }

    public static TransactRequest setUpTransactV2DataSourceIdentifierMaxLimit(String clientId, ChannelName channelName,
                                                                            ChannelId channelId, String productId, String sourceIdentifier) {
        return TransactRequest.builder()
                .accountIdentifier("222222xxxxxx0003")
                .clientTxnRef("30234241786MgAUs-0002")
                .channelSessionId("d5d65725c1414446b8546c5fcd5-0002")
                .timestamp(getCurrentIsoDateTime())
                .reserveFundsTxnRef("601502164444630590-0002")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier(sourceIdentifier)
                .targetIdentifier("2348038382069")
                .build();
    }

    public static TransactRequest setUpTransactV2DataTargetIdentifierMaxLimit(String clientId, ChannelName channelName,
                                                                            ChannelId channelId, String productId, String targetIdentifier) {
        return TransactRequest.builder()
                .accountIdentifier("222222xxxxxx0003")
                .clientTxnRef("30234241786MgAUs-0002")
                .channelSessionId("d5d65725c1414446b8546c5fcd5-0002")
                .timestamp(getCurrentIsoDateTime())
                .reserveFundsTxnRef("601502164444630590-0002")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382069")
                .targetIdentifier(targetIdentifier)
                .build();
    }

    //V1 Null
    public static TransactRequest setUpTransactV1DataWithCIDNull(String clientId, ChannelName channelName, ChannelId channelId,
                                                                 String productId) {
        return TransactRequest.builder()
                .accountIdentifier(null)
                .authCode(null)
                .clientTxnRef(ReserveAndTransactClient.clientTxnRefV1)
                .channelSessionId(ReserveAndTransactClient.channelSessionIdV1)
                .timestamp(getCurrentIsoDateTime())
                .clientId(null) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000) //could possibly add amount as a parameter as well?
                .channelId(channelId.getChannelId()) //7
                .channelName(channelName.getChannelName()) //USSD
                .sourceIdentifier(ReserveAndTransactClient.IdentifierV1)
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .build();
    }

    public static TransactRequest setUpTransactV1DataWithPIDNull(String clientId, ChannelName channelName, ChannelId channelId,
                                                                 String productId) {
        return TransactRequest.builder()
                .accountIdentifier(null)
                .authCode(null)
                .clientTxnRef(ReserveAndTransactClient.clientTxnRefV1)
                .channelSessionId(ReserveAndTransactClient.channelSessionIdV1)
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId) //3
                .productId(null) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000) //could possibly add amount as a parameter as well?
                .channelId(channelId.getChannelId()) //7
                .channelName(channelName.getChannelName()) //USSD
                .sourceIdentifier(ReserveAndTransactClient.IdentifierV1)
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .build();
    }

    public static TransactRequest setUpTransactV1DataWithPANull(String clientId, ChannelName channelName, ChannelId channelId,
                                                                String productId) {
        return TransactRequest.builder()
                .accountIdentifier(null)
                .authCode(null)
                .clientTxnRef(ReserveAndTransactClient.clientTxnRefV1)
                .channelSessionId(ReserveAndTransactClient.channelSessionIdV1)
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(null) //could possibly add amount as a parameter as well?
                .channelId(channelId.getChannelId()) //7
                .channelName(channelName.getChannelName()) //USSD
                .sourceIdentifier(ReserveAndTransactClient.IdentifierV1)
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .build();
    }

    public static TransactRequest setUpTransactV1DataWithChannelIDNull(String clientId, ChannelName channelName, ChannelId channelId,
                                                                       String productId) {
        return TransactRequest.builder()
                .accountIdentifier(null)
                .authCode(null)
                .clientTxnRef(ReserveAndTransactClient.clientTxnRefV1)
                .channelSessionId(ReserveAndTransactClient.channelSessionIdV1)
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000) //could possibly add amount as a parameter as well?
                .channelId(null) //7
                .channelName(channelName.getChannelName()) //USSD
                .sourceIdentifier(ReserveAndTransactClient.IdentifierV1)
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .build();
    }

    public static TransactRequest setUpTransactV1DataAccIdentifier(String clientId, ChannelName channelName, ChannelId channelId,
                                                                               String productId, String accIdentifier){
        return TransactRequest.builder()
                .accountIdentifier(accIdentifier)
                .authCode(null)
                .clientTxnRef(ReserveAndTransactClient.clientTxnRefV1)
                .channelSessionId(ReserveAndTransactClient.channelSessionIdV1)
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000) //could possibly add amount as a parameter as well?
                .channelId(channelId.getChannelId()) //7
                .channelName(channelName.getChannelName()) //USSD
                .sourceIdentifier(ReserveAndTransactClient.IdentifierV1)
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .build();
    }



    public static TransactRequest setUpTransactV1DataClientTxnRefId(String clientId, ChannelName channelName, ChannelId channelId,
                                                                   String productId, String clientTxnRef){
        return TransactRequest.builder()
                .accountIdentifier(null)
                .authCode(null)
                .clientTxnRef(clientTxnRef)
                .channelSessionId(ReserveAndTransactClient.channelSessionIdV1)
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000) //could possibly add amount as a parameter as well?
                .channelId(channelId.getChannelId()) //7
                .channelName(channelName.getChannelName()) //USSD
                .sourceIdentifier(ReserveAndTransactClient.IdentifierV1)
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .build();
    }
    public static TransactRequest setUpTransactV1DataChannelSessionId(String clientId, ChannelName channelName, ChannelId channelId,
                                                                    String productId, String channelSessionID){
        return TransactRequest.builder()
                .accountIdentifier(null)
                .authCode(null)
                .clientTxnRef(ReserveAndTransactClient.clientTxnRefV1)
                .channelSessionId(channelSessionID)
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000) //could possibly add amount as a parameter as well?
                .channelId(channelId.getChannelId()) //7
                .channelName(channelName.getChannelName()) //USSD
                .sourceIdentifier(ReserveAndTransactClient.IdentifierV1)
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .build();
    }

    public static TransactRequest setUpTransactV1DataTimeStamp(String clientId, ChannelName channelName, ChannelId channelId,
                                                                      String productId, String timeStamp){
        return TransactRequest.builder()
                .accountIdentifier(null)
                .authCode(null)
                .clientTxnRef(ReserveAndTransactClient.clientTxnRefV1)
                .channelSessionId(ReserveAndTransactClient.channelSessionIdV1)
                .timestamp(timeStamp)
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000) //could possibly add amount as a parameter as well?
                .channelId(channelId.getChannelId()) //7
                .channelName(channelName.getChannelName()) //USSD
                .sourceIdentifier(ReserveAndTransactClient.IdentifierV1)
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .build();
    }

    public static TransactRequest setUpTransactV1DataWithTargetIdentifierNull(String clientId, ChannelName channelName, ChannelId channelId,
                                                                              String productId){
        return TransactRequest.builder()
                .accountIdentifier(null)
                .authCode(null)
                .clientTxnRef(ReserveAndTransactClient.clientTxnRefV1)
                .channelSessionId(ReserveAndTransactClient.channelSessionIdV1)
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000) //could possibly add amount as a parameter as well?
                .channelId(channelId.getChannelId()) //7
                .channelName(channelName.getChannelName()) //USSD
                .sourceIdentifier(ReserveAndTransactClient.IdentifierV1)
                .targetIdentifier(null)
                .build();
    }
    public static TransactRequest setUpTransactV1DataWithSourceIdentifierNull(String clientId, ChannelName channelName, ChannelId channelId,
                                                                              String productId){
        return TransactRequest.builder()
                .accountIdentifier(null)
                .authCode(null)
                .clientTxnRef(ReserveAndTransactClient.clientTxnRefV1)
                .channelSessionId(ReserveAndTransactClient.channelSessionIdV1)
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000) //could possibly add amount as a parameter as well?
                .channelId(channelId.getChannelId()) //7
                .channelName(channelName.getChannelName()) //USSD
                .sourceIdentifier(null)
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .build();
    }
    public static TransactRequest setUpTransactV1DataWithAuthCodeEmpty(String clientId, ChannelName channelName, ChannelId channelId,
                                                                              String productId, String authCode){
        return TransactRequest.builder()
                .accountIdentifier(null)
                .authCode(authCode)
                .clientTxnRef(ReserveAndTransactClient.clientTxnRefV1)
                .channelSessionId(ReserveAndTransactClient.channelSessionIdV1)
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000) //could possibly add amount as a parameter as well?
                .channelId(channelId.getChannelId()) //7
                .channelName(channelName.getChannelName()) //USSD
                .sourceIdentifier(ReserveAndTransactClient.IdentifierV1)
                .targetIdentifier(ReserveAndTransactClient.IdentifierV1)
                .build();
    }

    //V4 Null
    public static TransactRequest setUpTransactV4DataWithCSIDNull(String clientId, CurrencyCode currencyCode,
                                                                  ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId(null)
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataWithTSNull(String clientId, CurrencyCode currencyCode,
                                                                ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(null)
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataWithRFTRNull(String clientId, CurrencyCode currencyCode,
                                                                  ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef(null)
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataWithCINull(String clientId, CurrencyCode currencyCode,
                                                                ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(null)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }
    public static TransactRequest setUpTransactV4DataWithAuthCodeNull(String clientId, CurrencyCode currencyCode,
                                                                ChannelName channelName, ChannelId channelId, String productId, String AuthCode) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .authCode(null)
                .build();
    }

    public static TransactRequest setUpTransactV4DataWithFSNull(String clientId, CurrencyCode currencyCode,
                                                                ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId(null)
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataWithPIDNull(String clientId, CurrencyCode currencyCode,
                                                                 ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(null)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataWithPANull(String clientId, CurrencyCode currencyCode,
                                                                ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount(null)
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataWithFANull(String clientId, CurrencyCode currencyCode,
                                                                ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount(null)
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataWithCCNull(String clientId, CurrencyCode currencyCode,
                                                                ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(null)
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataWithCNNull(String clientId, CurrencyCode currencyCode,
                                                                ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(null)
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataWithCIDnull(String clientId, CurrencyCode currencyCode,
                                                                 ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId("")
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataWithSINull(String clientId, CurrencyCode currencyCode,
                                                                ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier(null)
                .targetIdentifier("2348038382068")
                .build();
    }

    public static TransactRequest setUpTransactV4DataWithCTINull(String clientId, CurrencyCode currencyCode,
                                                                 ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2348038382068")
                .targetIdentifier(null)
                .build();
    }

    //// V4 Target Identifier expected to be an MSISDN based on the product type
    public static TransactRequest setUpTransactV4DataMSISDNProducType(String clientId, CurrencyCode currencyCode,
                                                                      ChannelName channelName, ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier("0000XXXX0004")
                .clientTxnRef("1598914290004")
                .channelSessionId("20200831230000004")
                .reserveFundsTxnRef("200831235610408740004")
                .clientId(clientId)
                .fundingSourceId("3")
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2341000000000")
                .targetIdentifier("a1")
                .build();
    }

    // V3 Target Identifier expected to be an MSISDN based on the product type
    public static TransactRequest setUpTransactV3DataMSISDNProducType(String clientId, ChannelName channelName,
                                                                      ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier(null)
                .reserveFundsTxnRef("90b91ada-df60-4a03-b0c3-c6bbf8381d09-0003")
                .channelSessionId("channelSessionId-0003")
                .clientTxnRef("toxR9F-0003")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .feeAmount("0")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2341000000000")
                .targetIdentifier("a1")
                .build();
    }

    // V2 Target Identifier expected to be an MSISDN based on the product type
    public static TransactRequest setUpTransactV2DataMSISDNProducType(String clientId, ChannelName channelName,
                                                                      ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .accountIdentifier("222222xxxxxx0003")
                .clientTxnRef("30234241786MgAUs-0002")
                .channelSessionId("d5d65725c1414446b8546c5fcd5-0002")
                .timestamp(getCurrentIsoDateTime())
                .reserveFundsTxnRef("601502164444630590-0002")
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2341000000000")
                .targetIdentifier("a1")
                .build();
    }

    // V2 TRANSACT NULL CASES
    public static TransactRequest setUpTransactV2DataWithChannelSessionIDEmpty(String clientId, ChannelName channelName,
                                                                      ChannelId channelId, String productId, String CSA) {
        return TransactRequest.builder()
                .channelSessionId(CSA)
                .reserveFundsTxnRef("601502164444630590-0002")
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2341000000000")
                .targetIdentifier("a1")
                .build();
    }

    public static TransactRequest setUpTransactV2DataWithtimestampEmpty(String clientId, ChannelName channelName,
                                                                      ChannelId channelId, String productId,String TS) {
        return TransactRequest.builder()
                .channelSessionId("d5d65725c1414446b8546c5fcd5-0002")
                .reserveFundsTxnRef("601502164444630590-0002")
                .timestamp(TS)
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2341000000000")
                .targetIdentifier("a1")
                .build();
    }
    public static TransactRequest setUpTransactV2DataWithreserveFundsTxnRefEmpty(String clientId, ChannelName channelName,
                                                                      ChannelId channelId, String productId, String RFTSR) {
        return TransactRequest.builder()
                .channelSessionId("d5d65725c1414446b8546c5fcd5-0002")
                .reserveFundsTxnRef(RFTSR)
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2341000000000")
                .targetIdentifier("a1")
                .build();
    }
    public static TransactRequest setUpTransactV2DataWithclientIdEmpty(String clientId, ChannelName channelName,
                                                                      ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .channelSessionId("d5d65725c1414446b8546c5fcd5-0002")
                .reserveFundsTxnRef("601502164444630590-0002")
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2341000000000")
                .targetIdentifier("a1")
                .build();
    }
    public static TransactRequest setUpTransactV2DataWithproductIdEmpty(String clientId, ChannelName channelName,
                                                                      ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .channelSessionId("d5d65725c1414446b8546c5fcd5-0002")
                .reserveFundsTxnRef("601502164444630590-0002")
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2341000000000")
                .targetIdentifier("a1")
                .build();
    }
    public static TransactRequest setUpTransactV2DatapurchaseAmountEmpty(String clientId, ChannelName channelName,
                                                                      ChannelId channelId, String productId, String PA) {
        return TransactRequest.builder()
                .channelSessionId("d5d65725c1414446b8546c5fcd5-0002")
                .reserveFundsTxnRef("601502164444630590-0002")
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId)
                .productId(productId)
                .amount(PA)
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2341000000000")
                .targetIdentifier("a1")
                .build();
    }
    public static TransactRequest setUpTransactV2DataWithchannelIdisEmpty(String clientId, ChannelName channelName,
                                                                      String channelId, String productId) {
        return TransactRequest.builder()
                .channelSessionId("d5d65725c1414446b8546c5fcd5-0002")
                .reserveFundsTxnRef("601502164444630590-0002")
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .channelId(channelId)
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2341000000000")
                .targetIdentifier("a1")
                .build();
    }
    public static TransactRequest setUpTransactV2DataWithchannelNameEmpty(String clientId, String channelName,
                                                                      ChannelId channelId, String productId) {
        return TransactRequest.builder()
                .channelSessionId("d5d65725c1414446b8546c5fcd5-0002")
                .reserveFundsTxnRef("601502164444630590-0002")
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .channelId(channelId.getChannelId())
                .channelName(channelName)
                .sourceIdentifier("2341000000000")
                .targetIdentifier("a1")
                .build();
    }
    public static TransactRequest setUpTransactV2DataWithsourceIdentifierEmpty(String clientId, ChannelName channelName,
                                                                      ChannelId channelId, String productId, String SI) {
        return TransactRequest.builder()
                .channelSessionId("d5d65725c1414446b8546c5fcd5-0002")
                .reserveFundsTxnRef("601502164444630590-0002")
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier(SI)
                .targetIdentifier("a1")
                .build();
    }
    public static TransactRequest setUpTransactV2DataWithTargetIdentifierEmpty(String clientId, ChannelName channelName,
                                                                      ChannelId channelId, String productId, String TI) {
        return TransactRequest.builder()
                .channelSessionId("d5d65725c1414446b8546c5fcd5-0002")
                .reserveFundsTxnRef("601502164444630590-0002")
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId)
                .productId(productId)
                .amount("10000")
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier("2341000000000")
                .targetIdentifier(TI)
                .build();
    }


    // V2 Target Identifier expected to be an MSISDN based on the product type
    public static TransactRequest setUpTransactV1DataMSISDNProducType(String clientId, ChannelName channelName, ChannelId channelId,
                                                                      String productId) {
        return TransactRequest.builder()
                .accountIdentifier(null)
                .authCode(null)
                .clientTxnRef(ReserveAndTransactClient.clientTxnRefV1)
                .channelSessionId(ReserveAndTransactClient.channelSessionIdV1)
                .timestamp(getCurrentIsoDateTime())
                .clientId(clientId) //3
                .productId(productId) //917
                .amount(ReserveAndTransactClient.PurchaseAmount10000) //could possibly add amount as a parameter as well?
                .channelId(channelId.getChannelId()) //7
                .channelName(channelName.getChannelName()) //USSD
                .sourceIdentifier("2341000000000")
                .targetIdentifier("a1")
                .build();
    }
    public static TransactRequest setUpTransactV4DataWithAdditionalDataWithProductFundSourceClientID(String AuthCode,String AccountIdentifier, String clientTxnRef, String FundingSourceId, String ChannelSessionId, String clientId, CurrencyCode currencyCode, ChannelName channelName,
                                                                                                     ChannelId channelId, String productId, String purchaseAmount, String feeAmount, String identifier, String ReserveFundsTxnRef) {
        Map<String,Object> ProductId = new LinkedHashMap<>();
        ProductId.put("productId", ReserveAndTransactClient.ProductAirtel_917);

        Map<String,Object> FundingsourceId = new LinkedHashMap<>();
        FundingsourceId.put("fundingSourceId", FundingSourceId);

        Map<String,Object> ClientId = new LinkedHashMap<>();
        ClientId.put("clientId", clientId);

        Map<String,Object> AdditionalData = new LinkedHashMap<>();
        AdditionalData.put("product", ProductId);
        AdditionalData.put("fundingSource", FundingsourceId);
        AdditionalData.put("client", ClientId);

        return TransactRequest.builder()
                .timestamp(getCurrentIsoDateTime())
                .accountIdentifier(AccountIdentifier)
                .authCode(AuthCode)
                .clientTxnRef(clientTxnRef)
                .channelSessionId(ChannelSessionId)
                .reserveFundsTxnRef(ReserveFundsTxnRef) //"200831235610408740004"
                .clientId(clientId)
                .fundingSourceId(FundingSourceId)
                .productId(productId)
                .amount(purchaseAmount)
                .feeAmount(feeAmount)
                .currencyCode(currencyCode.getCurrencyCode())
                .channelId(channelId.getChannelId())
                .channelName(channelName.getChannelName())
                .sourceIdentifier(identifier)
                .targetIdentifier(identifier)
                .additionalData(AdditionalData)
                .build();
    }

}
