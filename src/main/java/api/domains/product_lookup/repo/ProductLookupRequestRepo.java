package api.domains.product_lookup.repo;

import api.clients.BasedAPIClient;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static api.clients.BasedAPIClient.*;
import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.http.ContentType.JSON;

public class ProductLookupRequestRepo {

    public static Map<String, Object> setUpPostNewClientData(boolean active, List clickatellSystem,
                                                             String clientId, String clientName, String countryCode,
                                                             String timezoneId, String clickatellAccountId,
                                                             String ctxLimitTotal, boolean signatureRequired) {

        Map<String,Object> jsonObjectPayload = new LinkedHashMap<>();
        jsonObjectPayload.put("active", active);
        jsonObjectPayload.put("clickatellSystem", clickatellSystem);
        jsonObjectPayload.put("clientId", clientId);
        jsonObjectPayload.put("clientName", clientName);
        jsonObjectPayload.put("countryCode", countryCode);
        jsonObjectPayload.put("properties", "");
        jsonObjectPayload.put("timezoneId", timezoneId);
        jsonObjectPayload.put("clickatellAccountId", clickatellAccountId);
        jsonObjectPayload.put("ctxLimitTotal", ctxLimitTotal);
        jsonObjectPayload.put("signatureRequired", signatureRequired);

        Map<String,Object> properties = new LinkedHashMap<>();

        jsonObjectPayload.put("properties", properties);

        return jsonObjectPayload;
    }
    public static Map<String, Object> setUpPostNewClientData(boolean active, List clickatellSystem,
                                                             String clientId, String clientName, String countryCode,
                                                             String timezoneId, String clickatellAccountId,
                                                             String ctxLimitTotal, boolean signatureRequired, String timestamp) {

        Map<String,Object> jsonObjectPayload = new LinkedHashMap<>();
        jsonObjectPayload.put("active", active);
        jsonObjectPayload.put("clickatellSystemIds", clickatellSystem);
        jsonObjectPayload.put("clientId", clientId);
        jsonObjectPayload.put("clientName", clientName);
        jsonObjectPayload.put("countryCode", countryCode);
        jsonObjectPayload.put("properties", "");
        jsonObjectPayload.put("timezoneId", timezoneId);
        jsonObjectPayload.put("clickatellAccountId", clickatellAccountId);
        jsonObjectPayload.put("ctxLimitTotal", ctxLimitTotal);
        jsonObjectPayload.put("signatureRequired", signatureRequired);
        jsonObjectPayload.put("timestamp", timestamp);

        Map<String,Object> properties = new LinkedHashMap<>();

        jsonObjectPayload.put("properties", properties);

        return jsonObjectPayload;
    }
    public static Map<String, Object> setUpPostClientTouchFLow(String channelID, String ClientName, String Timezone, String CountryCode, String ClickaTellAccountId) {

        Map<String,Object> jsonObjectPayload = new LinkedHashMap<>();
        jsonObjectPayload.put("channelIds", Arrays.asList(channelID));
        jsonObjectPayload.put("clientName", ClientName);
        jsonObjectPayload.put("timezone", Timezone);
        jsonObjectPayload.put("countryCode", CountryCode);
        jsonObjectPayload.put("clickatellAccountId", ClickaTellAccountId);
        return jsonObjectPayload;
    }
    public static Map<String, Object> setUpPostClientTouchFLowNullClientName(String channelID, String Timezone, String CountryCode, String ClickaTellAccountId) {

        Map<String,Object> jsonObjectPayload = new LinkedHashMap<>();
        jsonObjectPayload.put("channelIds", Arrays.asList(channelID));
        jsonObjectPayload.put("timezone", Timezone);
        jsonObjectPayload.put("countryCode", CountryCode);
        jsonObjectPayload.put("clickatellAccountId", ClickaTellAccountId);
        return jsonObjectPayload;
    }
    public static Map<String, Object> setUpPostClientTouchFLowNullTimeZone(String channelID, String ClientName, String CountryCode, String ClickaTellAccountId) {

        Map<String,Object> jsonObjectPayload = new LinkedHashMap<>();
        jsonObjectPayload.put("channelIds", Arrays.asList(channelID));
        jsonObjectPayload.put("clientName", ClientName);
        jsonObjectPayload.put("countryCode", CountryCode);
        jsonObjectPayload.put("clickatellAccountId", ClickaTellAccountId);
        return jsonObjectPayload;
    }
    public static Map<String, Object> setUpPostClientTouchFLowNullCountryCode(String channelID, String ClientName, String Timezone,String ClickaTellAccountId) {

        Map<String,Object> jsonObjectPayload = new LinkedHashMap<>();
        jsonObjectPayload.put("channelIds", Arrays.asList(channelID));
        jsonObjectPayload.put("clientName", ClientName);
        jsonObjectPayload.put("timezone", Timezone);
        jsonObjectPayload.put("clickatellAccountId", ClickaTellAccountId);
        return jsonObjectPayload;
    }
    public static Map<String, Object> setUpPostClientTouchFLowNullChannelId(String ClientName, String Timezone, String CountryCode, String ClickaTellAccountId) {

        Map<String,Object> jsonObjectPayload = new LinkedHashMap<>();
        jsonObjectPayload.put("clientName", ClientName);
        jsonObjectPayload.put("timezone", Timezone);
        jsonObjectPayload.put("countryCode", CountryCode);
        jsonObjectPayload.put("clickatellAccountId", ClickaTellAccountId);
        return jsonObjectPayload;
    }
    public static Map<String, Object> setUpPostChannelsForClientTouchFLow(String channelID, String clientId) {

        Map<String,Object> jsonObjectPayload = new LinkedHashMap<>();
        jsonObjectPayload.put("channelIds", Arrays.asList(channelID));
        jsonObjectPayload.put("clientId", clientId);
        return jsonObjectPayload;
    }
    public static Map<String, Object> setUpPostChannelsForClientTouchFLow(List channelID, String clientId) {

        Map<String,Object> jsonObjectPayload = new LinkedHashMap<>();
        jsonObjectPayload.put("channelIds", channelID);
        jsonObjectPayload.put("clientId", clientId);
        return jsonObjectPayload;
    }
    public static Map<String, Object> setUpPutProductData(boolean active,
                                                             String countryCode, String description, String shortDescription,
                                                             String externalId, String id, Map pricing,
                                                             String productTypeId, String productTypeName, boolean supportToken, boolean isTopSeller,
                                                          String timestamp, String vendorId, String originalVendorId) {

        Map<String,Object> jsonObjectPayload = new LinkedHashMap<>();
        jsonObjectPayload.put("active", active);
        jsonObjectPayload.put("attributes", Arrays.asList());
        jsonObjectPayload.put("countryCode", countryCode);
        jsonObjectPayload.put("description", description);
        jsonObjectPayload.put("shortDescription", shortDescription);
        jsonObjectPayload.put("externalId", externalId);
        jsonObjectPayload.put("id", id);
        jsonObjectPayload.put("pricing", pricing);
        jsonObjectPayload.put("productTypeId", productTypeId);
        jsonObjectPayload.put("productTypeName", productTypeName);
        jsonObjectPayload.put("supportToken", supportToken);
        jsonObjectPayload.put("isTopSeller", isTopSeller);
        jsonObjectPayload.put("timestamp", timestamp);
        jsonObjectPayload.put("vendorId", vendorId);
        jsonObjectPayload.put("purchaseMediumIds", Arrays.asList());
        jsonObjectPayload.put("subscriberTypeIds", Arrays.asList());
        jsonObjectPayload.put("topSellerPlatformIds", Arrays.asList());
        jsonObjectPayload.put("originalVendorId", originalVendorId);

        return jsonObjectPayload;
    }
    public static Map<String, Object> setUpPutProductData(boolean active,
                                                          String countryCode, String description, String shortDescription,
                                                          String externalId, String id, Map pricing,
                                                          String productTypeId, String productTypeName, boolean supportToken, boolean isTopSeller,
                                                          String timestamp, String vendorId, String purchaseMediumIds, String subscriberTypeIds, String topSellerPlatformIds,
                                                          String originalVendorId) {

        Map<String,Object> jsonObjectPayload = new LinkedHashMap<>();
        jsonObjectPayload.put("active", active);
        jsonObjectPayload.put("attributes", Arrays.asList());
        jsonObjectPayload.put("countryCode", countryCode);
        jsonObjectPayload.put("description", description);
        jsonObjectPayload.put("shortDescription", shortDescription);
        jsonObjectPayload.put("externalId", externalId);
        jsonObjectPayload.put("id", id);
        jsonObjectPayload.put("pricing", pricing);
        jsonObjectPayload.put("productTypeId", productTypeId);
        jsonObjectPayload.put("productTypeName", productTypeName);
        jsonObjectPayload.put("supportToken", supportToken);
        jsonObjectPayload.put("isTopSeller", isTopSeller);
        jsonObjectPayload.put("timestamp", timestamp);
        jsonObjectPayload.put("vendorId", vendorId);
        jsonObjectPayload.put("purchaseMediumIds", Arrays.asList(purchaseMediumIds));
        jsonObjectPayload.put("subscriberTypeIds", Arrays.asList(subscriberTypeIds));
        jsonObjectPayload.put("topSellerPlatformIds", Arrays.asList(topSellerPlatformIds));
        jsonObjectPayload.put("originalVendorId", originalVendorId);

        return jsonObjectPayload;
    }
    public static Map<String, Object> setUpPutProductData(boolean active,
                                                          String countryCode, String description, String shortDescription,
                                                          String externalId, String id, Map pricing,
                                                          String productTypeId, String productTypeName, boolean supportToken, boolean isTopSeller,
                                                          String timestamp, String vendorId, List purchaseMediumIds, List subscriberTypeIds, List topSellerPlatformIds,
                                                          String originalVendorId) {

        Map<String,Object> jsonObjectPayload = new LinkedHashMap<>();
        jsonObjectPayload.put("active", active);
        jsonObjectPayload.put("attributes", Arrays.asList());
        jsonObjectPayload.put("countryCode", countryCode);
        jsonObjectPayload.put("description", description);
        jsonObjectPayload.put("shortDescription", shortDescription);
        jsonObjectPayload.put("externalId", externalId);
        jsonObjectPayload.put("id", id);
        jsonObjectPayload.put("pricing", pricing);
        jsonObjectPayload.put("productTypeId", productTypeId);
        jsonObjectPayload.put("productTypeName", productTypeName);
        jsonObjectPayload.put("supportToken", supportToken);
        jsonObjectPayload.put("isTopSeller", isTopSeller);
        jsonObjectPayload.put("timestamp", timestamp);
        jsonObjectPayload.put("vendorId", vendorId);
        jsonObjectPayload.put("purchaseMediumIds", purchaseMediumIds);
        jsonObjectPayload.put("subscriberTypeIds", subscriberTypeIds);
        jsonObjectPayload.put("topSellerPlatformIds", topSellerPlatformIds);
        jsonObjectPayload.put("originalVendorId", originalVendorId);

        return jsonObjectPayload;
    }
    public static Map<String, Object> setUpPutProductDataWithAttributes(boolean active, List attributes,
                                                          String countryCode, String description, String shortDescription,
                                                          String externalId, String id, Map pricing,
                                                          String productTypeId, String productTypeName, boolean supportToken, boolean isTopSeller,
                                                          String timestamp, String vendorId, String purchaseMediumIds, String subscriberTypeIds, String topSellerPlatformIds,
                                                          String originalVendorId) {

        Map<String,Object> jsonObjectPayload = new LinkedHashMap<>();
        jsonObjectPayload.put("active", active);
        jsonObjectPayload.put("attributes", attributes);
        jsonObjectPayload.put("countryCode", countryCode);
        jsonObjectPayload.put("description", description);
        jsonObjectPayload.put("shortDescription", shortDescription);
        jsonObjectPayload.put("externalId", externalId);
        jsonObjectPayload.put("id", id);
        jsonObjectPayload.put("pricing", pricing);
        jsonObjectPayload.put("productTypeId", productTypeId);
        jsonObjectPayload.put("productTypeName", productTypeName);
        jsonObjectPayload.put("supportToken", supportToken);
        jsonObjectPayload.put("isTopSeller", isTopSeller);
        jsonObjectPayload.put("timestamp", timestamp);
        jsonObjectPayload.put("vendorId", vendorId);
        jsonObjectPayload.put("purchaseMediumIds", Arrays.asList(purchaseMediumIds));
        jsonObjectPayload.put("subscriberTypeIds", Arrays.asList(subscriberTypeIds));
        jsonObjectPayload.put("topSellerPlatformIds", Arrays.asList(topSellerPlatformIds));
        jsonObjectPayload.put("originalVendorId", originalVendorId);

        return jsonObjectPayload;
    }
    public static Map<String, Object> setUpPutProductDataWithAttributesWithoutPurchaseMedium(boolean active, List attributes,
                                                                        String countryCode, String description, String shortDescription,
                                                                        String externalId, String id, Map pricing,
                                                                        String productTypeId, String productTypeName, boolean supportToken, boolean isTopSeller,
                                                                        String timestamp, String vendorId,
                                                                        String originalVendorId) {

        Map<String,Object> jsonObjectPayload = new LinkedHashMap<>();
        jsonObjectPayload.put("active", active);
        jsonObjectPayload.put("attributes", attributes);
        jsonObjectPayload.put("countryCode", countryCode);
        jsonObjectPayload.put("description", description);
        jsonObjectPayload.put("shortDescription", shortDescription);
        jsonObjectPayload.put("externalId", externalId);
        jsonObjectPayload.put("id", id);
        jsonObjectPayload.put("pricing", pricing);
        jsonObjectPayload.put("productTypeId", productTypeId);
        jsonObjectPayload.put("productTypeName", productTypeName);
        jsonObjectPayload.put("supportToken", supportToken);
        jsonObjectPayload.put("isTopSeller", isTopSeller);
        jsonObjectPayload.put("timestamp", timestamp);
        jsonObjectPayload.put("vendorId", vendorId);
        jsonObjectPayload.put("purchaseMediumIds", Arrays.asList());
        jsonObjectPayload.put("subscriberTypeIds", Arrays.asList());
        jsonObjectPayload.put("topSellerPlatformIds", Arrays.asList());
        jsonObjectPayload.put("originalVendorId", originalVendorId);

        return jsonObjectPayload;
    }
    public static Map<String, Object> setUpPutProductDataWithAttributesWithPurchaseMedium(boolean active, List attributes,
                                                                                             String countryCode, String description, String shortDescription,
                                                                                             String externalId, String id, Map pricing,
                                                                                             String productTypeId, String productTypeName, boolean supportToken, boolean isTopSeller,
                                                                                             String timestamp, String vendorId,String purchaseMediumIds, String subscriberTypeIds, String topSellerPlatformIds,
                                                                                             String originalVendorId) {

        Map<String,Object> jsonObjectPayload = new LinkedHashMap<>();
        jsonObjectPayload.put("active", active);
        jsonObjectPayload.put("attributes", attributes);
        jsonObjectPayload.put("countryCode", countryCode);
        jsonObjectPayload.put("description", description);
        jsonObjectPayload.put("shortDescription", shortDescription);
        jsonObjectPayload.put("externalId", externalId);
        jsonObjectPayload.put("id", id);
        jsonObjectPayload.put("pricing", pricing);
        jsonObjectPayload.put("productTypeId", productTypeId);
        jsonObjectPayload.put("productTypeName", productTypeName);
        jsonObjectPayload.put("supportToken", supportToken);
        jsonObjectPayload.put("isTopSeller", isTopSeller);
        jsonObjectPayload.put("timestamp", timestamp);
        jsonObjectPayload.put("vendorId", vendorId);
        jsonObjectPayload.put("purchaseMediumIds", Arrays.asList(purchaseMediumIds));
        jsonObjectPayload.put("subscriberTypeIds", Arrays.asList(subscriberTypeIds));
        jsonObjectPayload.put("topSellerPlatformIds", Arrays.asList(topSellerPlatformIds));
        jsonObjectPayload.put("originalVendorId", originalVendorId);

        return jsonObjectPayload;
    }
    public static Map<String, Object> setUpPostCreateFundingSource(boolean active, String authenticationType,
                                                                                             String confirmationWindowSizeSeconds, List countryCodes, String fundingSourceName,
                                                                                             String id, String initialRetryDelay, String maxRetries,
                                                                                             String password, boolean raasTransactionHistoryEnabled, String reservationTimeout, String reserveFundsEndpoint,
                                                                                             String retryDelay, String serviceWindowSizeSeconds,
                                                                   boolean supportToken, String transactionEndpoint, String username, String statusLookupEndpoint, boolean signatureRequired) {

        Map<String,Object> jsonObjectPayload = new LinkedHashMap<>();
        jsonObjectPayload.put("active", active);
        jsonObjectPayload.put("authenticationType", authenticationType);
        jsonObjectPayload.put("confirmationWindowSizeSeconds", confirmationWindowSizeSeconds);
        jsonObjectPayload.put("countryCodes", countryCodes);
        jsonObjectPayload.put("fundingSourceName", fundingSourceName);
        jsonObjectPayload.put("id", id);
        jsonObjectPayload.put("initialRetryDelay", initialRetryDelay);
        jsonObjectPayload.put("maxRetries", maxRetries);
        jsonObjectPayload.put("password", password);
        jsonObjectPayload.put("raasTransactionHistoryEnabled", raasTransactionHistoryEnabled);
        jsonObjectPayload.put("reservationTimeout", reservationTimeout);
        jsonObjectPayload.put("reserveFundsEndpoint", reserveFundsEndpoint);
        jsonObjectPayload.put("retryDelay", retryDelay);
        jsonObjectPayload.put("serviceWindowSizeSeconds", serviceWindowSizeSeconds);
        jsonObjectPayload.put("supportToken", supportToken);
        jsonObjectPayload.put("transactionEndpoint", transactionEndpoint);
        jsonObjectPayload.put("username", username);
        jsonObjectPayload.put("statusLookupEndpoint", statusLookupEndpoint);
        jsonObjectPayload.put("signatureRequired", signatureRequired);

        return jsonObjectPayload;
    }
    public static Map<String, Object> setUpPostUpdateFundingSource(boolean active, String authenticationType,
                                                                   String confirmationWindowSizeSeconds, List countryCodes, String fundingSourceName,
                                                                   String id, String initialRetryDelay, String maxRetries,
                                                                   String password, boolean raasTransactionHistoryEnabled, String reservationTimeout, String reserveFundsEndpoint,
                                                                   String retryDelay, String serviceWindowSizeSeconds,
                                                                   boolean supportToken, String transactionEndpoint, String username, String statusLookupEndpoint, boolean signatureRequired, String timestamp) {

        Map<String,Object> jsonObjectPayload = new LinkedHashMap<>();
        jsonObjectPayload.put("active", active);
        jsonObjectPayload.put("authenticationType", authenticationType);
        jsonObjectPayload.put("confirmationWindowSizeSeconds", confirmationWindowSizeSeconds);
        jsonObjectPayload.put("countryCodes", countryCodes);
        jsonObjectPayload.put("fundingSourceName", fundingSourceName);
        jsonObjectPayload.put("id", id);
        jsonObjectPayload.put("initialRetryDelay", initialRetryDelay);
        jsonObjectPayload.put("maxRetries", maxRetries);
        jsonObjectPayload.put("password", password);
        jsonObjectPayload.put("raasTransactionHistoryEnabled", raasTransactionHistoryEnabled);
        jsonObjectPayload.put("reservationTimeout", reservationTimeout);
        jsonObjectPayload.put("reserveFundsEndpoint", reserveFundsEndpoint);
        jsonObjectPayload.put("retryDelay", retryDelay);
        jsonObjectPayload.put("serviceWindowSizeSeconds", serviceWindowSizeSeconds);
        jsonObjectPayload.put("supportToken", supportToken);
        jsonObjectPayload.put("transactionEndpoint", transactionEndpoint);
        jsonObjectPayload.put("username", username);
        jsonObjectPayload.put("statusLookupEndpoint", statusLookupEndpoint);
        jsonObjectPayload.put("signatureRequired", signatureRequired);
        jsonObjectPayload.put("timestamp", timestamp);

        return jsonObjectPayload;
    }



}
