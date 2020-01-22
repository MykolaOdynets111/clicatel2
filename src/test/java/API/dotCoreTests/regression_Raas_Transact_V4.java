/**
 * TestSuite: TransactV4 Regression test suite.
 * Includes all end to end api tests for Transact V4
 * Author: Juan-Claude Botha
 */

package API.dotCoreTests;

import api.testUtilities.testConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import api.requestLibary.CORE.coreTransactV4POJO;

import api.testUtilities.dataBuilders.testDataFactory;
import api.testUtilities.sqlDataAccessLayer.sqlDataAccess;

import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

import api.testUtilities.propertyConfigWrapper.configWrapper;

public class regression_Raas_Transact_V4 extends testConfig {

    // Create properties object in order to inject environment specific variables upon build
    Properties properties = configWrapper.loadPropertiesFile("config.properties");

    // Data staging for use in test
    @DataProvider(name = "transactV4testcases", parallel = true)
    public Object[] createTransactTestData() throws IOException, ParseException {

        return new String[][]{

                {testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase1","accountIdentifier"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase1","purchaseAmount"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase1","channelId"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase1","channelName"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase1","channelSessionId"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase1","clientId"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase1","clientTxnRef"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase1","productId"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase1","sourceIdentifier"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase1","targetIdentifier"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase1","timestamp"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase1","reserveFundsTxnRef"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase1","feeAmount"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase1","currencyCode"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase1","fundingSourceId"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase1","expectedRaasResponseCode"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase1","expectedMessage"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase1","expectedHTTPResponseCode"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase1","expectedRaasResultRequestResponseCode"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase1","expectedRaasResultResponseResponseCode"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase1","expectedCTXTransactionResponseCode")},

                {testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase2","accountIdentifier"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase2","purchaseAmount"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase2","channelId"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase2","channelName"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase2","channelSessionId"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase2","clientId"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase2","clientTxnRef"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase2","productId"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase2","sourceIdentifier"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase2","targetIdentifier"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase2","timestamp"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase2","reserveFundsTxnRef"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase2","feeAmount"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase2","currencyCode"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase2","fundingSourceId"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase2","expectedRaasResponseCode"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase2","expectedMessage"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase2","expectedHTTPResponseCode"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase2","expectedRaasResultRequestResponseCode"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase2","expectedRaasResultResponseResponseCode"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase2","expectedCTXTransactionResponseCode")},

                {testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","failurecase3ProductWithIncorrectFeeAmount","accountIdentifier"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","failurecase3ProductWithIncorrectFeeAmount","purchaseAmount"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","failurecase3ProductWithIncorrectFeeAmount","channelId"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","failurecase3ProductWithIncorrectFeeAmount","channelName"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","failurecase3ProductWithIncorrectFeeAmount","channelSessionId"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","failurecase3ProductWithIncorrectFeeAmount","clientId"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","failurecase3ProductWithIncorrectFeeAmount","clientTxnRef"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","failurecase3ProductWithIncorrectFeeAmount","productId"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","failurecase3ProductWithIncorrectFeeAmount","sourceIdentifier"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","failurecase3ProductWithIncorrectFeeAmount","targetIdentifier"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","failurecase3ProductWithIncorrectFeeAmount","timestamp"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","failurecase3ProductWithIncorrectFeeAmount","reserveFundsTxnRef"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","failurecase3ProductWithIncorrectFeeAmount","feeAmount"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","failurecase3ProductWithIncorrectFeeAmount","currencyCode"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","failurecase3ProductWithIncorrectFeeAmount","fundingSourceId"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","failurecase3ProductWithIncorrectFeeAmount","expectedRaasResponseCode"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","failurecase3ProductWithIncorrectFeeAmount","expectedMessage"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","failurecase3ProductWithIncorrectFeeAmount","expectedHTTPResponseCode"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","failurecase3ProductWithIncorrectFeeAmount","expectedRaasResultRequestResponseCode"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","failurecase3ProductWithIncorrectFeeAmount","expectedRaasResultResponseResponseCode"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","failurecase3ProductWithIncorrectFeeAmount","expectedCTXTransactionResponseCode")},

                {testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase4","accountIdentifier"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase4","purchaseAmount"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase4","channelId"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase4","channelName"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase4","channelSessionId"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase4","clientId"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase4","clientTxnRef"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase4","productId"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase4","sourceIdentifier"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase4","targetIdentifier"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase4","timestamp"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase4","reserveFundsTxnRef"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase4","feeAmount"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase4","currencyCode"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase4","fundingSourceId"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase4","expectedRaasResponseCode"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase4","expectedMessage"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase4","expectedHTTPResponseCode"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase4","expectedRaasResultRequestResponseCode"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase4","expectedRaasResultResponseResponseCode"),
                        testDataFactory.getTestData("TransactV4datasource.json","transactv4suite","successcase4","expectedCTXTransactionResponseCode")}


        };
    }

    // Action step
    @Test(dataProvider = "transactV4testcases")
    public void basicTransactV4(String accountIdentifier,
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
                                String reserveFundsTxnRef,
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
                given()
                .param("clientId",clientId)
                .param("productId", productId)
                .param("purchaseAmount", purchaseAmount)
                .when()
                .get(properties.getProperty("QA_MINION")+":"+properties.getProperty("CORE_FinTermsCalc_Port")+properties.getProperty("CORE_FinTermsCalc_BasePath"))
                .then()
                .extract()
                .response();

        // Create transactV4 payload object - contains transactV4 request body
        coreTransactV4POJO TransactV4payload = new coreTransactV4POJO(accountIdentifier,
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
                reserveFundsTxnRef,
                feeAmount,
                currencyCode,
                fundingSourceId);

        // Create transactV4 response body object - contains api response data for use in assertions or other calls
        Response TransactV4response =
        given()
        .contentType(ContentType.JSON)
        .body(TransactV4payload)
        .when()
        .post(properties.getProperty("QA_MINION")+":"+properties.getProperty("CORE_Transact_V4_RequestSpec_Port")+properties.getProperty("CORE_Transact_V4_RequestSpec_BasePath"))
        .then()
        .extract()
        .response();

        // Assertions

        // Finance Terms Calculate response assertions
        Assert.assertNotEquals(finTermsCalculateResponse.path("clientId"), "");
        Assert.assertNotEquals(finTermsCalculateResponse.path("clientId"), "null");
        Assert.assertEquals(finTermsCalculateResponse.path("clientId").toString(), clientId);
        Assert.assertEquals(finTermsCalculateResponse.path("productId").toString(), productId);
        Assert.assertEquals(finTermsCalculateResponse.path("purchaseAmount").toString(), purchaseAmount);

        // Transact V4 response assertions - purchase
        Assert.assertEquals(TransactV4response.path("responseCode"), expectedRaasResponseCode);
        Assert.assertEquals(TransactV4response.path("responseMessage"), expectedMessage);
        Assert.assertEquals(TransactV4response.statusCode(), Integer.parseInt(expectedHTTPResponseCode));

        // CTX DB assertions
        Assert.assertEquals(sqlDataAccess.verifyMySQLCustomSql("SELECT * FROM cpgtx.tran_log WHERE clientTransactionId = " + "'" + TransactV4response.path("raasTxnRef") + "-0000'", "transactionResponseCode"), expectedCTXTransactionResponseCode);
        //Assert.assertEquals(sqlDataAccess.verifyMySQLCustomSql("SELECT * FROM cpgtx.tran_log WHERE clientTransactionId = " + "'" + TransactV4response.path("raasTxnRef") + "-0000'", "clientTransactionId"), TransactV4response.path("raasTxnRef") + "-0000");
        //Assert.assertEquals(sqlDataAccess.verifyMySQLCustomSql("SELECT * FROM cpgtx.tran_log WHERE clientTransactionId = " + "'" + TransactV4response.path("raasTxnRef") + "-0000'" , "product_id"), productId);
        //Assert.assertEquals(clientId, sqlDataAccess.verifyMySQLCustomSql("SELECT * FROM cpgtx.tran_log WHERE clientTransactionId = '" + TransactV4response.path("raasTxnRef") + "-0000", "client_id"));

        // raas db assertions
        Assert.assertEquals(sqlDataAccess.verifyPostgreDb("raas.transaction_log","raas_txn_ref", "=", TransactV4response.path("raasTxnRef")), TransactV4response.path("raasTxnRef"));
        Assert.assertEquals(sqlDataAccess.verifyPostgreDb("raas.raas_request", "raas_txn_ref", "=", TransactV4response.path("raasTxnRef")), TransactV4response.path("raasTxnRef"));
        Assert.assertEquals(sqlDataAccess.verifyPostgreDb("raas.raas_response", "raas_txn_ref", "=", TransactV4response.path("raasTxnRef")), TransactV4response.path("raasTxnRef"));
        Assert.assertEquals(sqlDataAccess.verifyPostgreCustomSql("SELECT * FROM raas.transaction_result_request WHERE raas_txn_ref = '" + TransactV4response.path("raasTxnRef") + "'", "response_code"), expectedRaasResultRequestResponseCode);
        //Assert.assertEquals(sqlDataAccess.verifyPostgreCustomSql("SELECT * FROM raas.transaction_result_response WHERE raas_txn_ref = '" + TransactV4response.path("raasTxnRef") + "'", "response_code"), expectedRaasResultResponseResponseCode);

    }
}