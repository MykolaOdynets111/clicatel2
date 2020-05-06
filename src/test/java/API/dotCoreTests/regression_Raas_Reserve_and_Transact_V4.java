/**
 * TestSuite: TransactV4 Regression test suite.
 * Includes all end to end api tests for Reserve and Transact V4
 * Author: Juan-Claude Botha
 */

package API.dotCoreTests;

import api.testUtilities.sqlDataAccessLayer.sqlDataAccess;
import api.testUtilities.testConfig;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import api.requestLibary.CORE.coreReserveAndTransactV4POJO;

import api.testUtilities.dataBuilders.testDataFactory;

import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

import api.testUtilities.propertyConfigWrapper.configWrapper;
import util.Listeners.allureApiTestListener;

@Listeners(allureApiTestListener.class)
public class regression_Raas_Reserve_and_Transact_V4 extends testConfig {

    // Create properties object in order to inject environment specific variables upon build
    Properties properties = configWrapper.loadPropertiesFile("config.properties");

    // Data staging for use in test
    @DataProvider(name = "ReserveAndTransactV4testcases", parallel = true)
    public Object[] createReserveAndTransactV4TestData() throws IOException, ParseException {

        return new String[][]{

                {testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","successcase1","accountIdentifier"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","successcase1","purchaseAmount"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","successcase1","channelId"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","successcase1","channelName"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","successcase1","channelSessionId"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","successcase1","clientId"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","successcase1","clientTxnRef"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","successcase1","productId"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","successcase1","sourceIdentifier"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","successcase1","targetIdentifier"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","successcase1","timestamp"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","successcase1","feeAmount"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","successcase1","currencyCode"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","successcase1","fundingSourceId"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","successcase1","expectedRaasResponseCode"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","successcase1","expectedMessage"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","successcase1","expectedHTTPResponseCode"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","successcase1","expectedRaasResultRequestResponseCode"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","successcase1","expectedRaasResultResponseResponseCode"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","successcase1","expectedCTXTransactionResponseCode")},

        };
    }

    @Step("Reserve and Transact V4 Success")
    @Test(dataProvider = "ReserveAndTransactV4testcases")
    public void basicReserveAndTransactV4(String accountIdentifier,
                                String purchaseAmount,
                                String channelId,
                                String channelName,
                                String channelSessionId,
                                String clientId,
                                String clientTxnRef,
                                String productId,
                                String sourceIdentifier,
                                String targetIdentifier,
                                String timeStamp,
                                String feeAmount,
                                String currencyCode,
                                String fundingSourceId,
                                String expectedRaasResponseCode,
                                String expectedMessage,
                                String expectedHTTPResponseCode,
                                String expectedRaasResultRequestResponseCode,
                                String expectedRaasResultResponseResponseCode,
                                String expectedCTXTransactionResponseCode) throws IOException, InterruptedException {

        // Financial Terms Calculate GET method call
        Response finTermsCalculateResponse =
                given(CORE_getEndPoints_FinancialTermsCalculate)
                .param("clientId",clientId)
                .param("productId", productId)
                .param("purchaseAmount", purchaseAmount)
                .when()
                .get()
                .then()
                .extract()
                .response();

        // Create ReserveAndtransactV4 payload object - contains transactV4 request body
        coreReserveAndTransactV4POJO ReserveAndTransactV4Payload = new coreReserveAndTransactV4POJO(
                accountIdentifier,
                purchaseAmount,
                channelId,
                channelName,
                channelSessionId,
                clientId,
                clientTxnRef,
                productId,
                sourceIdentifier,
                targetIdentifier,
                timeStamp,
                feeAmount,
                currencyCode,
                fundingSourceId);

        // Create transactV4 response body object - contains api response data for use in assertions or other calls
        Response ReserveAndTransactV4response =
                given(CORE_getEndPoints_ReserveAndTransactV4)
                .contentType(ContentType.JSON)
                .body(ReserveAndTransactV4Payload)
                .when()
                .post()
                .then()
                .extract()
                .response();

        // Finance Terms Calculate response assertions
        Assert.assertNotEquals(finTermsCalculateResponse.path("clientId"), "");
        Assert.assertNotEquals(finTermsCalculateResponse.path("clientId"), "null");
        Assert.assertEquals(finTermsCalculateResponse.path("clientId").toString(), clientId);
        Assert.assertEquals(finTermsCalculateResponse.path("productId").toString(), productId);
        Assert.assertEquals(finTermsCalculateResponse.path("purchaseAmount").toString(), purchaseAmount);

        // Transact V4 response assertions - purchase
        Assert.assertEquals(ReserveAndTransactV4response.path("responseCode"), expectedRaasResponseCode);
        Assert.assertEquals(ReserveAndTransactV4response.path("responseMessage"), expectedMessage);
        Assert.assertEquals(ReserveAndTransactV4response.statusCode(), Integer.parseInt(expectedHTTPResponseCode));

        // CTX DB assertions
        Thread.sleep(5000);
        Assert.assertEquals(sqlDataAccess.verifyMySQLCustomSql("SELECT * FROM cpgtx.tran_log WHERE clientTransactionId = " + "'" + ReserveAndTransactV4response.path("raasTxnRef") + "-0000'", "transactionResponseCode"), expectedCTXTransactionResponseCode);

        // raas db assertions
        Assert.assertEquals(sqlDataAccess.verifyPostgreDb("raas.transaction_log", "raas_txn_ref", "=", ReserveAndTransactV4response.path("raasTxnRef")), ReserveAndTransactV4response.path("raasTxnRef"));
        Assert.assertEquals(sqlDataAccess.verifyPostgreDb("raas.raas_request", "raas_txn_ref", "=", ReserveAndTransactV4response.path("raasTxnRef")), ReserveAndTransactV4response.path("raasTxnRef"));
        Assert.assertEquals(sqlDataAccess.verifyPostgreDb("raas.raas_response", "raas_txn_ref", "=", ReserveAndTransactV4response.path("raasTxnRef")), ReserveAndTransactV4response.path("raasTxnRef"));

    }

    // Data staging for use in test
    @DataProvider(name = "ReserveAndTransactV4MTNZASuccesstestcases", parallel = true)
    public Object[] ReserveAndTransactV4MTNZASuccesstestcases() throws IOException, ParseException {

        return new String[][]{

                {testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","MTNZASuccess","accountIdentifier"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","MTNZASuccess","purchaseAmount"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","MTNZASuccess","channelId"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","MTNZASuccess","channelName"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","MTNZASuccess","channelSessionId"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","MTNZASuccess","clientId"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","MTNZASuccess","clientTxnRef"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","MTNZASuccess","productId"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","MTNZASuccess","sourceIdentifier"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","MTNZASuccess","targetIdentifier"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","MTNZASuccess","timestamp"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","MTNZASuccess","feeAmount"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","MTNZASuccess","currencyCode"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","MTNZASuccess","fundingSourceId"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","MTNZASuccess","expectedRaasResponseCode"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","MTNZASuccess","expectedMessage"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","MTNZASuccess","expectedHTTPResponseCode"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","MTNZASuccess","expectedRaasResultRequestResponseCode"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","MTNZASuccess","expectedRaasResultResponseResponseCode"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4suite","MTNZASuccess","expectedCTXTransactionResponseCode")},

        };
    }

    @Step("Reserve and Transact V4 Success")
    @Test(dataProvider = "ReserveAndTransactV4MTNZASuccesstestcases")
    public void ReserveAndTransactV4MTNZASuccesstestcases(String accountIdentifier,
                                          String purchaseAmount,
                                          String channelId,
                                          String channelName,
                                          String channelSessionId,
                                          String clientId,
                                          String clientTxnRef,
                                          String productId,
                                          String sourceIdentifier,
                                          String targetIdentifier,
                                          String timeStamp,
                                          String feeAmount,
                                          String currencyCode,
                                          String fundingSourceId,
                                          String expectedRaasResponseCode,
                                          String expectedMessage,
                                          String expectedHTTPResponseCode,
                                          String expectedRaasResultRequestResponseCode,
                                          String expectedRaasResultResponseResponseCode,
                                          String expectedCTXTransactionResponseCode) throws IOException, InterruptedException {

        // Financial Terms Calculate GET method call
        Response finTermsCalculateResponse =
                given(CORE_getEndPoints_FinancialTermsCalculate)
                        .param("clientId",clientId)
                        .param("productId", productId)
                        .param("purchaseAmount", purchaseAmount)
                        .when()
                        .get()
                        .then()
                        .extract()
                        .response();

        // Create ReserveAndtransactV4 payload object - contains transactV4 request body
        coreReserveAndTransactV4POJO ReserveAndTransactV4Payload = new coreReserveAndTransactV4POJO(
                accountIdentifier,
                purchaseAmount,
                channelId,
                channelName,
                channelSessionId,
                clientId,
                clientTxnRef,
                productId,
                sourceIdentifier,
                targetIdentifier,
                timeStamp,
                feeAmount,
                currencyCode,
                fundingSourceId);

        // Create transactV4 response body object - contains api response data for use in assertions or other calls
        Response ReserveAndTransactV4response =
                given(CORE_getEndPoints_ReserveAndTransactV4)
                        .contentType(ContentType.JSON)
                        .body(ReserveAndTransactV4Payload)
                        .when()
                        .post()
                        .then()
                        .extract()
                        .response();

        // Finance Terms Calculate response assertions
        Assert.assertNotEquals(finTermsCalculateResponse.path("clientId"), "");
        Assert.assertNotEquals(finTermsCalculateResponse.path("clientId"), "null");
        Assert.assertEquals(finTermsCalculateResponse.path("clientId").toString(), clientId);
        Assert.assertEquals(finTermsCalculateResponse.path("productId").toString(), productId);
        Assert.assertEquals(finTermsCalculateResponse.path("purchaseAmount").toString(), purchaseAmount);

        // Transact V4 response assertions - purchase
        Assert.assertEquals(ReserveAndTransactV4response.path("responseCode"), expectedRaasResponseCode);
        Assert.assertEquals(ReserveAndTransactV4response.path("responseMessage"), expectedMessage);
        Assert.assertEquals(ReserveAndTransactV4response.statusCode(), Integer.parseInt(expectedHTTPResponseCode));

        // CTX DB assertions
        Thread.sleep(5000);
        Assert.assertEquals(sqlDataAccess.verifyMySQLCustomSql("SELECT * FROM cpgtx.tran_log WHERE clientTransactionId = " + "'" + ReserveAndTransactV4response.path("raasTxnRef") + "-0000'", "transactionResponseCode"), expectedCTXTransactionResponseCode);

        // raas db assertions
        Assert.assertEquals(sqlDataAccess.verifyPostgreDb("raas.transaction_log", "raas_txn_ref", "=", ReserveAndTransactV4response.path("raasTxnRef")), ReserveAndTransactV4response.path("raasTxnRef"));
        Assert.assertEquals(sqlDataAccess.verifyPostgreCustomSql("select * from raas.transaction_log where raas_txn_ref = " + "'" + ReserveAndTransactV4response.path("raasTxnRef") + "'", "raas_response_response_code"), expectedRaasResponseCode);
        Assert.assertEquals(sqlDataAccess.verifyPostgreDb("raas.raas_request", "raas_txn_ref", "=", ReserveAndTransactV4response.path("raasTxnRef")), ReserveAndTransactV4response.path("raasTxnRef"));
        Assert.assertEquals(sqlDataAccess.verifyPostgreDb("raas.raas_response", "raas_txn_ref", "=", ReserveAndTransactV4response.path("raasTxnRef")), ReserveAndTransactV4response.path("raasTxnRef"));

    }

    // Data staging for use in test
    @DataProvider(name = "NegativeTestCases", parallel = true)
    public Object[] NegativeTestCases() throws IOException, ParseException {

        return new String[][]{

                {testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4NegativeTesting","invalidLargeAmountTransaction","accountIdentifier"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4NegativeTesting","invalidLargeAmountTransaction","purchaseAmount"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4NegativeTesting","invalidLargeAmountTransaction","channelId"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4NegativeTesting","invalidLargeAmountTransaction","channelName"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4NegativeTesting","invalidLargeAmountTransaction","channelSessionId"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4NegativeTesting","invalidLargeAmountTransaction","clientId"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4NegativeTesting","invalidLargeAmountTransaction","clientTxnRef"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4NegativeTesting","invalidLargeAmountTransaction","productId"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4NegativeTesting","invalidLargeAmountTransaction","sourceIdentifier"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4NegativeTesting","invalidLargeAmountTransaction","targetIdentifier"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4NegativeTesting","invalidLargeAmountTransaction","timestamp"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4NegativeTesting","invalidLargeAmountTransaction","feeAmount"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4NegativeTesting","invalidLargeAmountTransaction","currencyCode"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4NegativeTesting","invalidLargeAmountTransaction","fundingSourceId"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4NegativeTesting","invalidLargeAmountTransaction","expectedRaasResponseCode"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4NegativeTesting","invalidLargeAmountTransaction","expectedMessage"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4NegativeTesting","invalidLargeAmountTransaction","expectedHTTPResponseCode"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4NegativeTesting","invalidLargeAmountTransaction","expectedRaasResultRequestResponseCode"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4NegativeTesting","invalidLargeAmountTransaction","expectedRaasResultResponseResponseCode"),
                        testDataFactory.getTestData("ReserveAndTransactV4datasource.json","reserveandtransactv4NegativeTesting","invalidLargeAmountTransaction","expectedCTXTransactionResponseCode")},

        };
    }

    @Step("Negative test cases")
    @Test(dataProvider = "NegativeTestCases")
    public void invalidLargeAmountTransaction(String accountIdentifier,
                                                          String purchaseAmount,
                                                          String channelId,
                                                          String channelName,
                                                          String channelSessionId,
                                                          String clientId,
                                                          String clientTxnRef,
                                                          String productId,
                                                          String sourceIdentifier,
                                                          String targetIdentifier,
                                                          String timeStamp,
                                                          String feeAmount,
                                                          String currencyCode,
                                                          String fundingSourceId,
                                                          String expectedRaasResponseCode,
                                                          String expectedMessage,
                                                          String expectedHTTPResponseCode,
                                                          String expectedRaasResultRequestResponseCode,
                                                          String expectedRaasResultResponseResponseCode,
                                                          String expectedCTXTransactionResponseCode) throws IOException, InterruptedException {

        // Financial Terms Calculate GET method call
        Response finTermsCalculateResponse =
                given(CORE_getEndPoints_FinancialTermsCalculate)
                        .param("clientId",clientId)
                        .param("productId", productId)
                        .param("purchaseAmount", purchaseAmount)
                        .when()
                        .get()
                        .then()
                        .extract()
                        .response();

        // Create ReserveAndtransactV4 payload object - contains transactV4 request body
        coreReserveAndTransactV4POJO ReserveAndTransactV4Payload = new coreReserveAndTransactV4POJO(
                accountIdentifier,
                purchaseAmount,
                channelId,
                channelName,
                channelSessionId,
                clientId,
                clientTxnRef,
                productId,
                sourceIdentifier,
                targetIdentifier,
                timeStamp,
                feeAmount,
                currencyCode,
                fundingSourceId);

        // Create transactV4 response body object - contains api response data for use in assertions or other calls
        Response ReserveAndTransactV4response =
                given(CORE_getEndPoints_ReserveAndTransactV4)
                        .contentType(ContentType.JSON)
                        .body(ReserveAndTransactV4Payload)
                        .when()
                        .post()
                        .then()
                        .extract()
                        .response();

        // Transact V4 response assertions - purchase
        Assert.assertEquals(ReserveAndTransactV4response.path("responseCode"), expectedRaasResponseCode);
        Assert.assertEquals(ReserveAndTransactV4response.path("responseMessage"), expectedMessage);
        Assert.assertEquals(ReserveAndTransactV4response.statusCode(), Integer.parseInt(expectedHTTPResponseCode));

    }

}
