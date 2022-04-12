package api.reserve_and_transact;
import api.clients.*;
import api.domains.reserve_and_transact.model.ReserveAndTransactResponse;
import api.domains.transact.model.TransactResponse;
import api.enums.*;
import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import lombok.val;
import org.hamcrest.Matchers;
import org.hamcrest.core.CombinableMatcher;
import org.testng.annotations.Test;
//import sun.invoke.empty.Empty;
import util.base_test.BaseApiTest;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;
import static api.clients.ReserveAndTransactClient.*;
import static api.clients.SimulatorClient.*;
import static api.clients.SimulatorClient.addMtnTestCases;
import static api.clients.SupportUiClient.getRaasFlow;
import static api.clients.SupportUiClient.getRaasInteractions;
import static api.clients.TransactClient.executeTransact;
import static api.clients.TransactionLookupClient.findTransaction;
import static api.controls.TransactControl.getTransactionStatus;
import static api.domains.simulator.repo.SimulatorRequestRepo.setUpAirtelSimData;
import static api.domains.reserve_and_transact.repo.ReserveAndTransactRequestRepo.*;
import static api.domains.simulator.repo.SimulatorRequestRepo.setUpMtnSimData;
import static api.domains.transact.repo.TransactRequestRepo.*;
import static api.enums.ChannelName.*;
import static api.enums.CurrencyCode.*;
import static org.apache.http.HttpStatus.*;
import static api.clients.ProductLookupClient.*;
import static util.DateProvider.getCurrentIsoDateTime;

public class ReserveAndTransactTest extends BaseApiTest {

    //SUCCESS :: v1-v4
    @Test(groups = {"smokeTest"})
    @Description("30100 :: payd-raas-gateway :: POST v4/reserveAndTransact :: SUCCESS :: Reserve and Transact API (4.0)")
    @TmsLink("TECH-68538")
    public void testReserveAndTransactV4Success() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
//        //raas db check replaced with API check (TransactionLookup)
//        getTransactionStatus(raasTxnRef);

//        assertThat(getTransactionStatus(raasTxnRef))
//                .as("Postgres SQL query : Transaction Status incorrect")
//                .isTrue();

        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        Thread.sleep(20000);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Success));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"ctx_request" parameter isn't empty
                .body("ctx_request.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //Verify funds were successfully reserved (response_code equals to 0000)
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND ctx response code is SUCCESSFUL (0)
                //"responseCode" in the "ctx_response" equals to "0"
                .body("ctx_response[0].responseCode", Matchers.is(Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                //AND successful transaction result is sent (0000)
                //"transaction_result_request" parameter isn't empty
                .body("transaction_result_request.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND success response code is received from the funding source (202)
                //"responseCode" in the "transaction_result_response" equals to "202"
                .body("transaction_result_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode202))
                //AND transaction wasn't retried (no records found in the db)
                .body("ctx_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND transaction wasn't pending (no records found in the db)
                .body("ctx_lookup_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.responseCode0000)));
    }


    @Test
    @Description("30100 :: payd-raas-gateway :: POST v3/reserveAndTransact :: SUCCESS")
    @TmsLink("TECH-54334")
    public void testReserveAndTransactV3Success() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV3Data(ReserveAndTransactClient.TestClient3, ChannelName.MOBILE, ChannelId.MOBILE, ReserveAndTransactClient.ProductAirtel_917);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V3)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        Thread.sleep(20000);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Success));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"ctx_request" parameter isn't empty
                .body("ctx_request.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //Verify funds were successfully reserved (response_code equals to 0000)
                // "responseCode" in the "reserve_fund_response" equals to "0000"
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"reserve_fund_request" parameter isn't empty
                .body("reserve_fund_request.raasTxnRef", Matchers.is(raasTxnRef))
                //AND ctx response code is SUCCESSFUL (0)
                //"responseCode" in the "ctx_response" equals to "0"
                .body("ctx_response[0].responseCode", Matchers.is(Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                //AND successful transaction result is sent (0000)
                //"transaction_result_request" parameter isn't empty
                .body("transaction_result_request.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND success response code is received from the funding source (202)
                //"responseCode" in the "transaction_result_response" equals to "202"
                .body("transaction_result_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode202))
                //AND transaction wasn't retried (no records found in the db)
                .body("ctx_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND transaction wasn't pending (no records found in the db)
                .body("ctx_lookup_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.ZeroTransactionCode)));

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: POST v2/reserveAndTransact :: SUCCESS")
    @TmsLink("TECH-54335")
    public void testReserveAndTransactV2Success() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV2Data(ReserveAndTransactClient.TestClient3, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageProcessingRequest))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        Thread.sleep(30000);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Success));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"reserve_fund_request" parameter isn't empty
                .body("reserve_fund_request.raasTxnRef", Matchers.is(raasTxnRef))
                //Verify funds were successfully reserved (response_code equals to 0000)
                // "responseCode" in the "reserve_fund_response" equals to "0000"
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"ctx_request" parameter isn't empty
                .body("ctx_request.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND ctx response code is SUCCESSFUL (0)
                //"responseCode" in the "ctx_response" equals to "0"
                .body("ctx_response[0].responseCode", Matchers.is(Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                //AND successful transaction result is sent (0000)
                //"transaction_result_request" parameter isn't empty
                .body("transaction_result_request.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND success response code is received from the funding source (202)
                //"responseCode" in the "transaction_result_response" equals to "202"
                .body("transaction_result_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode202))
                //AND transaction wasn't retried (no records found in the db)
                .body("ctx_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND transaction wasn't pending (no records found in the db)
                .body("ctx_lookup_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.ZeroTransactionCode)));
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: POST v1/reserveAndTransact :: SUCCESS")
    @TmsLink("TECH-58612")
    public void testReserveAndTransactV1Success() throws InterruptedException {
        val jsonBody = setUpTransactV1Data(ReserveAndTransactClient.TestClient3, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917);

        val raasTxnRef = executeTransact(jsonBody, Port.TRANSACTIONS, Version.V1)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageProcessingRequest))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(TransactResponse.class).getRaasTxnRef();

        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        Thread.sleep(10000);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Success));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"reserve_fund_request" parameter isn't empty
                .body("reserve_fund_request.raasTxnRef", Matchers.is(raasTxnRef))
                //Verify funds were successfully reserved (response_code equals to 0000)
                // "responseCode" in the "reserve_fund_response" equals to "0000"
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"ctx_request" parameter isn't empty
                .body("ctx_request.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND ctx response code is SUCCESSFUL (0)
                //"responseCode" in the "ctx_response" equals to "0"
                .body("ctx_response[0].responseCode", Matchers.is(Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                //AND successful transaction result is sent (0000)
                //"transaction_result_request" parameter isn't empty
                .body("transaction_result_request.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND success response code is received from the funding source (202)
                //"responseCode" in the "transaction_result_response" equals to "202"
                .body("transaction_result_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode202))
                //AND transaction wasn't retried (no records found in the db)
                .body("ctx_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND transaction wasn't pending (no records found in the db)
                .body("ctx_lookup_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)));
    }


    //SUCCESS :: VENDORS & CLIENTS
    @Test(groups = {"smokeTest"})
    @Description("30100 :: payd-raas-gateway :: vendor 2 (CellC) SUCCESS")
    @TmsLink("TECH-69577")
    public void testReserveAndTransactVendor2CellCSuccess() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.Clickatell_Test_ZA_2_PaydWhitelistFundingSource_2, ZAR, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductCellC_60, ReserveAndTransactClient.PurchaseAmount1000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier_3);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

//        raas db check - being replaced by API transaction lookup check
//        val status = executeCustomQueryAndReturnValue(POSTGRES_SQL, format(GET_TRANSACTION_STATUS, raasTxnRef));
//        assertThat(status)
//                .as("Postgres SQL query result incorrect")
//                .contains("SUCCESS");

        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        Thread.sleep(20000);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.Clickatell_Test_ZA_2_PaydWhitelistFundingSource_2), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString("SUCCESS"));
    }


    @Test(groups = {"smokeTest"})
    @Description("30100 :: payd-raas-gateway :: vendor 3 (MTN_ZA) SUCCESS")
    @TmsLink("TECH-68400")
    public void testReserveAndTransactVendor3MtnZaSuccess() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.Clickatell_Test_ZA_2_PaydWhitelistFundingSource_2, ZAR, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductMTN_ZA_400, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier_4);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        Thread.sleep(30000);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.Clickatell_Test_ZA_2_PaydWhitelistFundingSource_2), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Success));
    }


    @Test(groups = {"smokeTest"})
    @Description("30100 :: payd-raas-gateway :: vendor 4 (Vodacom) SUCCESS")
    @TmsLink("TECH-69575")
    public void testReserveAndTransactVendor4VodacomSuccess() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.Clickatell_Test_ZA_2_PaydWhitelistFundingSource_2, ZAR, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductVodacom_ZA_40, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier_5);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageProcessingRequest))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
        Thread.sleep(30000);
        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.Clickatell_Test_ZA_2_PaydWhitelistFundingSource_2), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Success));
    }


    @Test(groups = {"smokeTest"})
    @Description("30100 :: payd-raas-gateway :: vendor 5 (Telkom) SUCCESS")
    @TmsLink("TECH-69580")
    public void testReserveAndTransactVendor5TelkomSuccess() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.Clickatell_Test_ZA_2_PaydWhitelistFundingSource_2, ZAR, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductTelkom_5_50, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier_3);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
        Thread.sleep(30000);
        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.Clickatell_Test_ZA_2_PaydWhitelistFundingSource_2), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Success));
    }


    @Test(groups = {"smokeTest"})
    @Description("30100 :: payd-raas-gateway :: vendor 15 (3line, threeline, ClickatellBiller3) SUCCESS")
    @TmsLink("TECH-75095")
    public void testReserveAndTransactVendor15ThreelineSuccess() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.Product_3line_15_505, ReserveAndTransactClient.PurchaseAmount50000, ReserveAndTransactClient.FeeAmount0, NotificationClient.Identifier_6);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        Thread.sleep(10000);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Success));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"reserve_fund_request" parameter isn't empty
                .body("reserve_fund_request.raasTxnRef", Matchers.is(raasTxnRef))
                //Verify funds were successfully reserved (response_code equals to 0000)
                // "responseCode" in the "reserve_fund_response" equals to "0000"
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"ctx_request" parameter isn't empty
                .body("ctx_request.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND ctx response code is SUCCESSFUL (0)
                //"responseCode" in the "ctx_response" equals to "0"
                .body("ctx_response[0].responseCode", Matchers.is(Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                //AND successful transaction result is sent (0000)
                //"transaction_result_request" parameter isn't empty
                .body("transaction_result_request.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND success response code is received from the funding source (202)
                //"responseCode" in the "transaction_result_response" equals to "202"
                .body("transaction_result_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode202))
                //AND transaction wasn't retried (no records found in the db)
                .body("ctx_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND transaction wasn't pending (no records found in the db)
                .body("ctx_lookup_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)));
    }


    @Test(groups = {"smokeTest"})
    @Description("30100 :: payd-raas-gateway :: vendor 21 (mwm) SUCCESS")
    @TmsLink("TECH-68398")
    public void testReserveAndTransactVendor21MwmSuccess() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.PurchaseAmount200, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier_9);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        Thread.sleep(20000);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Success));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"reserve_fund_request" parameter isn't empty
                .body("reserve_fund_request.raasTxnRef", Matchers.is(raasTxnRef))
                //Verify funds were successfully reserved (response_code equals to 0000)
                // "responseCode" in the "reserve_fund_response" equals to "0000"
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"ctx_request" parameter isn't empty
                .body("ctx_request.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND ctx response code is SUCCESSFUL (0)
                //"responseCode" in the "ctx_response" equals to "0"
                .body("ctx_response[0].responseCode", Matchers.is(Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                //AND successful transaction result is sent (0000)
                //"transaction_result_request" parameter isn't empty
                .body("transaction_result_request.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND success response code is received from the funding source (202)
                //"responseCode" in the "transaction_result_response" equals to "202"
                .body("transaction_result_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode202))
                //AND transaction wasn't retried (no records found in the db)
                .body("ctx_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND transaction wasn't pending (no records found in the db)
                .body("ctx_lookup_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)));
    }


    @Test(groups = {"smokeTest"})
    @Description("30100 :: payd-raas-gateway :: vendor 23 (MTN_ZA_clickatell) SUCCESS")
    @TmsLink("TECH-68536")
    public void testReserveAndTransactVendor23MtnZaSuccess() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.Clickatell_Test_ZA_2_PaydWhitelistFundingSource_2, ZAR, USSD, ChannelId.USSD, ReserveAndTransactClient.Product_MTN_ZA_Clickatell_30, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier_4);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        Thread.sleep(10000);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.Clickatell_Test_ZA_2_PaydWhitelistFundingSource_2), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Success));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"reserve_fund_request" parameter isn't empty
                .body("reserve_fund_request.raasTxnRef", Matchers.is(raasTxnRef))
                //Verify funds were successfully reserved (response_code equals to 0000)
                // "responseCode" in the "reserve_fund_response" equals to "0000"
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"ctx_request" parameter isn't empty
                .body("ctx_request.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND ctx response code is SUCCESSFUL (0)
                //"responseCode" in the "ctx_response" equals to "0"
                .body("ctx_response[0].responseCode", Matchers.is(Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                //AND successful transaction result is sent (0000)
                //"transaction_result_request" parameter isn't empty
                .body("transaction_result_request.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND success response code is received from the funding source (202)
                //"responseCode" in the "transaction_result_response" equals to "202"
                .body("transaction_result_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode202))
                //AND transaction wasn't retried (no records found in the db)
                .body("ctx_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND transaction wasn't pending (no records found in the db)
                .body("ctx_lookup_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)));
    }


    @Test(groups = {"smokeTest"})
    @Description("30100 :: payd-raas-gateway :: vendor 100 (MTN_NG) SUCCESS")
    @TmsLink("TECH-63683")
    public void testReserveAndTransactVendor100MtnNgSuccess() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_100, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        Thread.sleep(10000);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Success));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"reserve_fund_request" parameter isn't empty
                .body("reserve_fund_request.raasTxnRef", Matchers.is(raasTxnRef))
                //Verify funds were successfully reserved (response_code equals to 0000)
                // "responseCode" in the "reserve_fund_response" equals to "0000"
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"ctx_request" parameter isn't empty
                .body("ctx_request.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND ctx response code is SUCCESSFUL (0)
                //"responseCode" in the "ctx_response" equals to "0"
                .body("ctx_response[0].responseCode", Matchers.is(Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                //AND successful transaction result is sent (0000)
                //AND successful transaction result is sent (0000)
                //"transaction_result_request" parameter isn't empty
                .body("transaction_result_request.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND success response code is received from the funding source (202)
                //"responseCode" in the "transaction_result_response" equals to "202"
                .body("transaction_result_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode202))
                //AND transaction wasn't retried (no records found in the db)
                .body("ctx_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND transaction wasn't pending (no records found in the db)
                .body("ctx_lookup_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)));
    }


    @Test(groups = {"smokeTest"})
    @Description("30100 :: payd-raas-gateway :: vendor 101 (glo) SUCCESS")
    @TmsLink("TECH-68396")
    public void testReserveAndTransactVendor101GloSuccess() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.Product_Glo_110, ReserveAndTransactClient.PurchaseAmount50000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.IdentifierV2);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        Thread.sleep(10000);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Success));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"reserve_fund_request" parameter isn't empty
                .body("reserve_fund_request.raasTxnRef", Matchers.is(raasTxnRef))
                //Verify funds were successfully reserved (response_code equals to 0000)
                // "responseCode" in the "reserve_fund_response" equals to "0000"
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"ctx_request" parameter isn't empty
                .body("ctx_request.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND ctx response code is SUCCESSFUL (0)
                //"responseCode" in the "ctx_response" equals to "0"
                .body("ctx_response[0].responseCode", Matchers.is(Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                //AND successful transaction result is sent (0000)
                //"transaction_result_request" parameter isn't empty
                .body("transaction_result_request.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND success response code is received from the funding source (202)
                //"responseCode" in the "transaction_result_response" equals to "202"
                .body("transaction_result_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode202))
                //AND transaction wasn't retried (no records found in the db)
                .body("ctx_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND transaction wasn't pending (no records found in the db)
                .body("ctx_lookup_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)));
    }


    @Test(groups = {"smokeTest"})
    @Description("30100 :: payd-raas-gateway :: vendor 102 (9mobile/etisalat) SUCCESS")
    @TmsLink("TECH-68397")
    public void testReserveAndTransactVendor1029MobileEtisalatSuccess() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.Product_Etisalat_120, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.IdentifierV2);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        Thread.sleep(10000);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Success));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"reserve_fund_request" parameter isn't empty
                .body("reserve_fund_request.raasTxnRef", Matchers.is(raasTxnRef))
                //Verify funds were successfully reserved (response_code equals to 0000)
                // "responseCode" in the "reserve_fund_response" equals to "0000"
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"ctx_request" parameter isn't empty
                .body("ctx_request.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND ctx response code is SUCCESSFUL (0)
                //"responseCode" in the "ctx_response" equals to "0"
                .body("ctx_response[0].responseCode", Matchers.is(Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                //AND successful transaction result is sent (0000)
                //"transaction_result_request" parameter isn't empty
                .body("transaction_result_request.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND success response code is received from the funding source (202)
                //"responseCode" in the "transaction_result_response" equals to "202"
                .body("transaction_result_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode202))
                //AND transaction wasn't retried (no records found in the db)
                .body("ctx_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND transaction wasn't pending (no records found in the db)
                .body("ctx_lookup_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)));
    }


    @Test(groups = {"smokeTest"})
    @Description("30100 :: payd-raas-gateway :: vendor103 (Airtel) SUCCESS \"Airtime\" purchase")
    @TmsLink("TECH-57995")
    public void testReserveAndTransactVendor103AirtelAirtimeSuccess() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ProductLookupClient.ProductAirtel_130, "10000", "0", "2348038382067");

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        Thread.sleep(10000);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Success));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"reserve_fund_request" parameter isn't empty
                .body("reserve_fund_request.raasTxnRef", Matchers.is(raasTxnRef))
                //Verify funds were successfully reserved (response_code equals to 0000)
                // "responseCode" in the "reserve_fund_response" equals to "0000"
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"ctx_request" parameter isn't empty
                .body("ctx_request.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND ctx response code is SUCCESSFUL (0)
                //"responseCode" in the "ctx_response" equals to "0"
                .body("ctx_response[0].responseCode", Matchers.is(Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                //AND successful transaction result is sent (0000)
                //"transaction_result_request" parameter isn't empty
                .body("transaction_result_request.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND success response code is received from the funding source (202)
                //"responseCode" in the "transaction_result_response" equals to "202"
                .body("transaction_result_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode202))
                //AND transaction wasn't retried (no records found in the db)
                .body("ctx_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND transaction wasn't pending (no records found in the db)
                .body("ctx_lookup_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)));
    }


    @Test(groups = {"smokeTest"})
    @Description("30100 :: payd-raas-gateway :: vendor103 (Airtel) SUCCESS \"Data\" purchase")
    @TmsLink("TECH-57989")
    public void testReserveAndTransactVendor103AirtelDataSuccess() {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.Product_Airtel_189, PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Success));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"reserve_fund_request" parameter isn't empty
                .body("reserve_fund_request.raasTxnRef", Matchers.is(raasTxnRef))
                //Verify funds were successfully reserved (response_code equals to 0000)
                // "responseCode" in the "reserve_fund_response" equals to "0000"
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"ctx_request" parameter isn't empty
                .body("ctx_request.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND ctx response code is SUCCESSFUL (0)
                //"responseCode" in the "ctx_response" equals to "0"
                .body("ctx_response[0].responseCode", Matchers.is(Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                //AND successful transaction result is sent (0000)
                //"transaction_result_request" parameter isn't empty
                .body("transaction_result_request.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND success response code is received from the funding source (202)
                //"responseCode" in the "transaction_result_response" equals to "202"
                .body("transaction_result_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode202))
                //AND transaction wasn't retried (no records found in the db)
                .body("ctx_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND transaction wasn't pending (no records found in the db)
                .body("ctx_lookup_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)));
    }


    @Test(groups = {"smokeTest"})
    @Description("30100 :: payd-raas-gateway :: client 1003 (test client) SUCCESS (checksum)")
    @TmsLink("TECH-56890")
    public void testReserveAndTransactWithSignatureSuccess() throws InterruptedException {
        //Creating signature
        val jsonBody = setUpReserveAndTransactV3DataWtihSignature(ReserveAndTransactClient.TestClient1003, MOBILE,
                ChannelId.MOBILE, ProductAirtel_917);
        val signature = getProductInfoWithSecretValue(jsonBody)
                .then().assertThat().statusCode(SC_OK)
                .extract().body().as(ReserveAndTransactResponse.class).getSignature();

        //R&T with Signature call execution

        val raasTxnRef = executeReserveAndTransactWithSignature(jsonBody, Port.TRANSACTIONS, Version.V4, signature)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(TransactResponse.class).getRaasTxnRef();
    }
/*        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //Verify funds were successfully reserved (response_code equals to 0000)
                .body("reserve_fund_response.responseCode", Matchers.is("0000"))
                //AND ctx response code is SUCCESSFUL (0)
                .body("ctx_response[0].responseCode", Matchers.is(0))
                //AND successful transaction result is sent (0000)
                .body("transaction_result_request.responseCode", Matchers.is("0000"))
                //AND success response code is received from the funding source (202)
                .body("transaction_result_response.responseCode", Matchers.is("202"))
                //AND transaction wasn't retried (no records found in the db)
                .body("ctx_response.clientTransactionId", Matchers.not(raasTxnRef.concat("-0001")))
                //AND transaction wasn't pending (no records found in the db)
                .body("ctx_lookup_response.clientTransactionId", Matchers.not(raasTxnRef.concat("-0000")));

    }*/


    //PENDING :: RD :: NRD
    @Test
    @Description("30100 :: payd-raas-gateway :: NonRetryableDecline")
    @TmsLink("TECH-57167")
    public void testReserveAndTransactNonRetryableDecline() throws InterruptedException {
        //add test case to simulate a NON_RETRYABLE_DECLINE
        val addTestCase = setUpAirtelSimData(ReserveAndTransactClient.ResponseCode_17017, InFlightTransactionLookupClient.AirTel_purchase);

        addAirtelTestCases(Arrays.asList(addTestCase), Port.AIRTEL_SIMULATOR)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.contains(ReserveAndTransactClient.ResponseCode_17017))
                .body("id", Matchers.contains(""))
                .body("action", Matchers.contains(InFlightTransactionLookupClient.AirTel_purchase))
                .body("delay", Matchers.contains(Integer.parseInt(ReserveAndTransactClient.Airtel_delay_3000)))
                .body("httpStatusCode", Matchers.contains(Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                .body("fieldName", Matchers.contains(""));

        //perform R&T - purchase airtel product
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.Product_Airtel_130, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
        //Adding thread.sleep because after the execution of reserveAndTransact call the script execute so fast that it does not get time to execute the airtell simulator under the non-retriable decline state and in the next step it removes the cases.
        Thread.sleep(5000);

        //set simulator to the default state (delete simulator tests)
        removeAllAirtelTestCases(Port.AIRTEL_SIMULATOR)
                .then().assertThat().statusCode(SC_OK);

        //raas db check --- transaction status is "FAILED" - replaced with API transactionlookup check
//        val status = executeCustomQueryAndReturnValue(POSTGRES_SQL, format(GET_TRANSACTION_STATUS, raasTxnRef));
//        assertThat(status)
//                .as("Postgres SQL query result incorrect")
//                .contains("FAILED");

        //Verify transaction status is "FAILED"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(Failed));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //"raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"reserve_fund_request" parameter isn't empty
                .body("reserve_fund_request.raasTxnRef", Matchers.is(raasTxnRef))
                // "responseCode" in the "reserve_fund_response" equals to "0000"
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //only one value exist in the "ctx_request" array
                // "clientTransactionId" in "ctx_request" parameter is "rassTxnRef-0000"
                .body("ctx_response.clientTransactionId", Matchers.not(raasTxnRef.concat(FirstTransactionCode)))
                //"responseCode" in the "ctx_response" equals to "2213" for object with "clientTransactionId": "raasTxnRef-0000"
                .body("ctx_response[0].responseCode", Matchers.is(Integer.parseInt(ReserveAndTransactClient.responseCode2213)))
                .body("ctx_response.clientTransactionId", Matchers.not(raasTxnRef.concat(FirstTransactionCode)))
                //AND "ctx_lookup_request" array is empty
                .body("ctx_lookup_request", Matchers.empty())
                //AND "ctx_lookup_response" array is empty
                .body("ctx_lookup_response", Matchers.empty())
                //  "responseCode" in "transaction_result_request" parameter is "2213"
                .body("transaction_result_request.responseCode", Matchers.is(ReserveAndTransactClient.responseCode2213))
                // "responseCode" in the "transaction_result_response" equals to "202"
                .body("transaction_result_response.responseCode", Matchers.is(responseCode202));
    }


    @Test
    @Description("30100 :: payd-raas-gateway :: Pending To SUCCESS (airtel)")
    @TmsLink("TECH-46759")
    public void testReserveAndTransactPendingToSuccess() throws InterruptedException {
        //add test cases
        val addTestCase1 = setUpAirtelSimData(InFlightTransactionLookupClient.ResponseCode_500, InFlightTransactionLookupClient.AirTel_purchase);
        val addTestCase2 = setUpAirtelSimData(InFlightTransactionLookupClient.ResponseCode_200, InFlightTransactionLookupClient.AirTel_lookup);

        addAirtelTestCases(Arrays.asList(addTestCase1, addTestCase2), Port.AIRTEL_SIMULATOR)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.contains(InFlightTransactionLookupClient.ResponseCode_200, InFlightTransactionLookupClient.ResponseCode_500))
                .body("id", Matchers.contains("", ""))
                .body("action", Matchers.contains(InFlightTransactionLookupClient.AirTel_lookup, InFlightTransactionLookupClient.AirTel_purchase))
                .body("delay", Matchers.contains(Integer.parseInt(ReserveAndTransactClient.Airtel_delay_3000), Integer.parseInt(ReserveAndTransactClient.Airtel_delay_3000)))
                .body("httpStatusCode", Matchers.contains(Integer.parseInt(ReserveAndTransactClient.responseCode0), Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                .body("fieldName", Matchers.contains("", ""));
        //perform R&T - purchase airtel product
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.Product_Airtel_130, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
        Thread.sleep(180000);
        //set simulator to the default state (delete simulator tests)
        removeAllAirtelTestCases(Port.AIRTEL_SIMULATOR)
                .then().assertThat().statusCode(SC_OK);
        //Verify transaction status is "FAILED"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Success));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //"responseCode" in the "raas_response" equals to "0000" AND "reserve_fund_request" parameter isn't empty
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"reserve_fund_request" parameter isn't empty AND "responseCode" in the "reserve_fund_response" equals to "0000"
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"ctx_request" array with "clientTransactionId" is "{transactionId}-0000"
                .body("ctx_request[0].clientTransactionId", Matchers.is(raasTxnRef.concat(String.valueOf(ReserveAndTransactClient.StartTransactionCode))))
                ///AND "ctx_response" array AND "responseCode" for "clientTransactionId": "{transactionId}-0000" object equals to "2240"
                .body("ctx_response[0].responseCode", Matchers.is(Integer.parseInt(ReserveAndTransactClient.responseCode2240)))
                //more than one object exist in the "ctx_lookup_request" array with "clientTransactionId" is "{transactionId}-0000"
                .body("ctx_lookup_request.clientTransactionId[0]", Matchers.is(raasTxnRef.concat(String.valueOf((ReserveAndTransactClient.StartTransactionCode)))))
                .body("ctx_response[0].clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND "ctx_lookup_request" array is empty
                .body("ctx_lookup_request", Matchers.notNullValue())
                //AND "ctx_lookup_response" array is empty
                .body("ctx_lookup_response", Matchers.notNullValue())
                //AND "responseCode" in "transaction_result_request" parameter is "2213"
//                .body("transaction_result_request.responseCode", Matchers.is(ReserveAndTransactClient.responseCode2213))
                //AND success response code is received from the funding source
                .body("transaction_result_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode202))
                //AND more than one object exist in the "ctx_response" array AND "responseCode" for "clientTransactionId": "{transactionId}-0000" object equals to "2240"
                .body("ctx_response[0].responseCode", Matchers.is(Integer.parseInt(ReserveAndTransactClient.responseCode2240)))
                //AND "responseCode" for "clientTransactionId": "{transactionId}-0000" object equals to "0"
                .body("ctx_response.clientTransactionId", Matchers.not(raasTxnRef.concat(FirstTransactionCode)))
                // "responseCode" in the "transaction_result_response" equals to "202"
                .body("transaction_result_response.responseCode", Matchers.is(responseCode202));
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: Pending To NonRetryableDecline (mtn_za)")
    @TmsLink("TECH-57302")
    public void testReserveAndTransactPendingToNonRetryableDecline() throws InterruptedException {
        //add test cases
        val addTestCase1 = setUpMtnSimData("9318", "27837640171", "virtual_recharge", 200);
        val addTestCase2 = setUpMtnSimData("9318", "27837640171", "bundle_recharge", 200);
        val addTestCase3 = setUpMtnSimData("9313", "27837640171", "repeat_virtual_recharge", 200);
        val addTestCase4 = setUpMtnSimData("9313", "27837640171", "repeat_bundle_recharge", 200);

        addMtnTestCases(Arrays.asList(addTestCase1, addTestCase2, addTestCase3, addTestCase4), Port.MTN_SIMULATOR)
                .then().assertThat().statusCode(SC_OK);

        //perform R&T - purchase mtn product
        val jsonBody = setUpReserveAndTransactV4Data("2", ZAR, USSD, ChannelId.USSD, "400", "10000", "0", "27837640171");

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString("0000"))
                .body("responseMessage", Matchers.containsString("Processing request (funds reserved)"))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
        Thread.sleep(240000);

        //set simulator to the default state (delete simulator tests)
        removeAllMtnTestCases(Port.MTN_SIMULATOR)
                .then().assertThat().statusCode(SC_OK);

        //Verify transaction status is "FAILED"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, 2, queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString("FAILED"));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"reserve_fund_request" parameter isn't empty
                .body("reserve_fund_request.raasTxnRef", Matchers.is(raasTxnRef))
                //AND "responseCode" in the "reserve_fund_response" equals to "0000"
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND the only one objects exists in the "ctx_request" array with "clientTransactionId" is "{transactionId}-0000"
                .body("ctx_request.clientTransactionId", Matchers.hasItem(raasTxnRef.concat("-0000")))
                //AND the only one object exists in the "ctx_response" array
                //AND "responseCode" for "clientTransactionId": "{transactionId}-0000" object equals to "2236"
                .body("ctx_response.responseCode", Matchers.hasItem(2236))
                //AND more than one object exist in the "ctx_lookup_request" array with "clientTransactionId" equals to " {transactionId}-0000"
                .body("ctx_lookup_request.clientTransactionId[1]", Matchers.is(raasTxnRef.concat("-0000")))
                .body("ctx_lookup_request.clientTransactionId[0]", Matchers.is(raasTxnRef.concat("-0000")))
                //AND more than one object is in the "ctx_lookup_response"
                //AND "responseCode" for "clientTransactionId": " {transactionId}-0001" object equals to "2236" AND "responseCode" for "clientTransactionId": "{transactionId} -0001" object equals to "2213"
                .body("ctx_lookup_response.responseCode[0]", Matchers.is(2213))
                .body("ctx_lookup_response.responseCode[1]", Matchers.is(2236))
                //AND "responseCode" in "transaction_result_request" parameter is "2213"
                .body("transaction_result_request.responseCode", Matchers.is("2213"))
                //AND "responseCode" in the "transaction_result_response" equals to "202"
                .body("transaction_result_response.responseCode", Matchers.is("202"));
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: RetryableDecline to SUCCESS (airtel)")
    @TmsLink("TECH-57171")
    public void testReserveAndTransactRetryableDeclineToSuccess() throws InterruptedException {
        //add test cases
        val addTestCase1 = setUpAirtelSimData(ReserveAndTransactClient.ResponseCode_2238, InFlightTransactionLookupClient.AirTel_purchase);
        val addTestCase2 = setUpAirtelSimData(InFlightTransactionLookupClient.ResponseCode_200, InFlightTransactionLookupClient.AirTel_lookup);

        addAirtelTestCases(Arrays.asList(addTestCase1, addTestCase2), Port.AIRTEL_SIMULATOR)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.contains(InFlightTransactionLookupClient.ResponseCode_200, ReserveAndTransactClient.ResponseCode_2238))
                .body("id", Matchers.contains("", ""))
                .body("action", Matchers.contains(InFlightTransactionLookupClient.AirTel_lookup, InFlightTransactionLookupClient.AirTel_purchase))
                .body("delay", Matchers.contains(Integer.parseInt(ReserveAndTransactClient.Airtel_delay_3000), Integer.parseInt(ReserveAndTransactClient.Airtel_delay_3000)))
                .body("httpStatusCode", Matchers.contains(Integer.parseInt(ReserveAndTransactClient.responseCode0), Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                .body("fieldName", Matchers.contains("", ""));
        //perform R&T - purchase airtel product
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.Product_Airtel_130, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
        Thread.sleep(3000);
        //Set up testcase where action is purchase success
        val addTestCase3 = setUpAirtelSimData(InFlightTransactionLookupClient.ResponseCode_200, InFlightTransactionLookupClient.AirTel_purchase);
        addAirtelTestCases(Arrays.asList(addTestCase3), Port.AIRTEL_SIMULATOR)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.contains(InFlightTransactionLookupClient.ResponseCode_200, InFlightTransactionLookupClient.ResponseCode_200))
                .body("id", Matchers.contains("", ""))
                .body("action", Matchers.contains(InFlightTransactionLookupClient.AirTel_lookup, InFlightTransactionLookupClient.AirTel_purchase))
                .body("delay", Matchers.contains(Integer.parseInt(ReserveAndTransactClient.Airtel_delay_3000), Integer.parseInt(ReserveAndTransactClient.Airtel_delay_3000)))
                .body("httpStatusCode", Matchers.contains(Integer.parseInt(ReserveAndTransactClient.responseCode0), Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                .body("fieldName", Matchers.contains("", ""));
        Thread.sleep(180000);
        //set simulator to the default state (delete simulator tests)
        removeAllAirtelTestCases(Port.AIRTEL_SIMULATOR)
                .then().assertThat().statusCode(SC_OK);

        //Verify transaction status is "FAILED"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Success));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //"raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000" AND "reserve_fund_request" parameter isn't empty
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND "responseCode" in the "reserve_fund_response" equals to "0000"
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //only two objects exist in the "ctx_request" array with "clientTransactionId" is "{transactionId}-0000" and "{transactionId}-0001"
                .body("ctx_request[0].clientTransactionId", Matchers.is(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                .body("ctx_request[1].clientTransactionId", Matchers.is(raasTxnRef.concat(ReserveAndTransactClient.StartTransactionCode)))
                // "responseCode" for "clientTransactionId": "{transactionId}-0000" of ctx_response object equals to "2201"
                .body("ctx_response[1].responseCode", Matchers.is(Integer.parseInt(ReserveAndTransactClient.responseCode2201)))
                // "responseCode" for "clientTransactionId": "{transactionId}-0001" of ctx_response object equals to "0"
                .body("ctx_response[0].responseCode", Matchers.is(Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                //"ctx_lookup_request" array is empty
                .body("ctx_lookup_request", Matchers.empty())
                // "ctx_lookup_response" array  is empty
                .body("ctx_lookup_response", Matchers.empty())
                //"responseCode" in "transaction_result_request" parameter is "0000"
                .body("transaction_result_request.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"responseCode" in the "transaction_result_response" equals to "202"
                .body("transaction_result_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode202));
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: RetryableDecline to NonRetryableDecline (airtel)")
    @TmsLink("TECH-57170")
    public void testReserveAndTransactRetryableDeclineToNonRetryableDecline() throws InterruptedException {
        //add test cases
        val addTestCase1 = setUpAirtelSimData(ReserveAndTransactClient.ResponseCode_2238, InFlightTransactionLookupClient.AirTel_purchase);
        val addTestCase2 = setUpAirtelSimData(InFlightTransactionLookupClient.ResponseCode_200, InFlightTransactionLookupClient.AirTel_lookup);

        addAirtelTestCases(Arrays.asList(addTestCase1, addTestCase2), Port.AIRTEL_SIMULATOR)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.contains(InFlightTransactionLookupClient.ResponseCode_200, ReserveAndTransactClient.ResponseCode_2238))
                .body("id", Matchers.contains("", ""))
                .body("action", Matchers.contains(InFlightTransactionLookupClient.AirTel_lookup, InFlightTransactionLookupClient.AirTel_purchase))
                .body("delay", Matchers.contains(Integer.parseInt(ReserveAndTransactClient.Airtel_delay_3000), Integer.parseInt(ReserveAndTransactClient.Airtel_delay_3000)))
                .body("httpStatusCode", Matchers.contains(Integer.parseInt(ReserveAndTransactClient.responseCode0), Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                .body("fieldName", Matchers.contains("", ""));

        //perform R&T - purchase airtel product
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.Product_Airtel_130, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
        //Thread.sleep(24000);
        //Set up testcase where action is purchase non retryable decline
        val addTestCase3 = setUpAirtelSimData(ReserveAndTransactClient.ResponseCode_17017, InFlightTransactionLookupClient.AirTel_purchase);
        val addTestCase4 = setUpAirtelSimData(InFlightTransactionLookupClient.ResponseCode_200, InFlightTransactionLookupClient.AirTel_lookup);
        addAirtelTestCases(Arrays.asList(addTestCase3, addTestCase4), Port.AIRTEL_SIMULATOR)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.contains(InFlightTransactionLookupClient.ResponseCode_200, ReserveAndTransactClient.ResponseCode_17017))
                .body("id", Matchers.contains("", ""))
                .body("action", Matchers.contains(InFlightTransactionLookupClient.AirTel_lookup, InFlightTransactionLookupClient.AirTel_purchase))
                .body("delay", Matchers.contains(Integer.parseInt(ReserveAndTransactClient.Airtel_delay_3000), Integer.parseInt(ReserveAndTransactClient.Airtel_delay_3000)))
                .body("httpStatusCode", Matchers.contains(Integer.parseInt(ReserveAndTransactClient.responseCode0), Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                .body("fieldName", Matchers.contains("", ""));
        Thread.sleep(240000);
        System.out.println("Check here");

        //set simulator to the default state (delete simulator tests)
        removeAllAirtelTestCases(Port.AIRTEL_SIMULATOR)
                .then().assertThat().statusCode(SC_OK);

        //Verify transaction status is "FAILED"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Failed));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"reserve_fund_request" parameter isn't empty
                .body("reserve_fund_request.raasTxnRef", Matchers.is(raasTxnRef))
                //AND "responseCode" in the "reserve_fund_response" equals to "0000"
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND two objects exist in the "ctx_request" array with "clientTransactionId" is"{transactionId}-0000" and "{transactionId}-0001"
                .body("ctx_request.clientTransactionId[1]", Matchers.is(raasTxnRef.concat("-0000")))
                .body("ctx_request.clientTransactionId[0]", Matchers.is(raasTxnRef.concat("-0001")))
                //AND only two objects exist in the "ctx_response" array AND "responseCode" for "clientTransactionId": "{transactionId}-0000" object equals to "2201"AND "responseCode" for clientTransactionId": "{transactionId}-0001" object equals to "2213"
                .body("ctx_response[1].responseCode", Matchers.is(2201))
                .body("ctx_response[0].responseCode", Matchers.is(2213))
                //AND "ctx_lookup_request" array is empty
                .body("ctx_lookup_request", Matchers.empty())
                //AND "ctx_lookup_response" array is empty
                .body("ctx_lookup_response", Matchers.empty())
                //AND "responseCode" in "transaction_result_request" parameter is "2213"
                .body("transaction_result_request.responseCode", Matchers.is("2213"))
                //AND "responseCode" in the "transaction_result_response" equals to "202"
                .body("transaction_result_response.responseCode", Matchers.is("202"));
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: Pending To RetryableDecline To SUCCESS (airtel)")
    @TmsLink("TECH-57169")
    public void testReserveAndTransactPendingToRetryableDeclineToSuccess() throws InterruptedException {
        //add test cases
        val addTestCase1 = setUpAirtelSimData(InFlightTransactionLookupClient.ResponseCode_500, InFlightTransactionLookupClient.AirTel_purchase);
        val addTestCase4 = setUpAirtelSimData(ResponseCode_206, InFlightTransactionLookupClient.AirTel_lookup);

        addAirtelTestCases(Arrays.asList(addTestCase1, addTestCase4), Port.AIRTEL_SIMULATOR)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.contains(ResponseCode_206,InFlightTransactionLookupClient.ResponseCode_500))
                .body("id", Matchers.contains("",""))
                .body("action", Matchers.contains(InFlightTransactionLookupClient.AirTel_lookup,InFlightTransactionLookupClient.AirTel_purchase))
                .body("delay", Matchers.contains(Integer.parseInt(ReserveAndTransactClient.Airtel_delay_3000),Integer.parseInt(ReserveAndTransactClient.Airtel_delay_3000)))
                .body("httpStatusCode", Matchers.contains(Integer.parseInt(ReserveAndTransactClient.responseCode0),Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                .body("fieldName", Matchers.contains("",""));

        //perform R&T - purchase airtel product
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.Product_Airtel_130, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
        Thread.sleep(5000);

        //Set up testcase where action is purchase success
        val addTestCase3 = setUpAirtelSimData(InFlightTransactionLookupClient.ResponseCode_200, InFlightTransactionLookupClient.AirTel_purchase);
        val addTestCase2 = setUpAirtelSimData(ResponseCode_206, InFlightTransactionLookupClient.AirTel_lookup);
        addAirtelTestCases(Arrays.asList(addTestCase3, addTestCase2), Port.AIRTEL_SIMULATOR)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.contains(ResponseCode_206, InFlightTransactionLookupClient.ResponseCode_200))
                .body("id", Matchers.contains("", ""))
                .body("action", Matchers.contains(InFlightTransactionLookupClient.AirTel_lookup, InFlightTransactionLookupClient.AirTel_purchase))
                .body("delay", Matchers.contains(Integer.parseInt(ReserveAndTransactClient.Airtel_delay_3000), Integer.parseInt(ReserveAndTransactClient.Airtel_delay_3000)))
                .body("httpStatusCode", Matchers.contains(Integer.parseInt(ReserveAndTransactClient.responseCode0), Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                .body("fieldName", Matchers.contains("", ""));

        //Added 4 minutes wait because ctx requires time to iterate another cycle for transactions when airtel simulation is set to retryable decline.
        Thread.sleep(240000);
        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Success));

        //set simulator to the default state (delete simulator tests)
        removeAllAirtelTestCases(Port.AIRTEL_SIMULATOR)
                .then().assertThat().statusCode(SC_OK);


        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"reserve_fund_request" parameter isn't empty
                .body("reserve_fund_request.raasTxnRef", Matchers.is(raasTxnRef))
                //AND "responseCode" in the "reserve_fund_response" equals to "0000"
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND two objects exist in the "ctx_request" array with "clientTransactionId" is"{transactionId}-0000" and "{transactionId}-0001"
                .body("ctx_request.clientTransactionId[1]", Matchers.is(raasTxnRef.concat("-0000")))
                .body("ctx_request.clientTransactionId[0]", Matchers.is(raasTxnRef.concat("-0001")))
                //AND only two objects exist in the "ctx_response" array
                //AND "responseCode" for "clientTransactionId": "{transactionId}-0000" object equals to "0" AND "responseCode" for "clientTransactionId": "{transactionId}-0001" object equals to "2240"
                .body("ctx_response[1].responseCode", Matchers.is(2240))
                //AND transaction was pending (ctx lookup with response code 0)
                .body("ctx_response[0].responseCode", Matchers.is(0))
                //AND more than one object is in the "ctx_lookup_request"
                .body("ctx_lookup_request.clientTransactionId[1]", Matchers.is(raasTxnRef.concat("-0000")))
                .body("ctx_lookup_request.clientTransactionId[0]", Matchers.is(raasTxnRef.concat("-0000")))
                //AND more than one object is in the "ctx_lookup_response"
                //AND "responseCode" for "clientTransactionId": " {transactionId}-0000" object equals to "2240" AND "responseCode" for "clientTransactionId": "{transactionId}-0000" object equals to "2201"
                .body("ctx_lookup_response.responseCode[0]", Matchers.is(2201))
                .body("ctx_lookup_response.responseCode[1]", Matchers.is(2240))
                //AND "responseCode" in "transaction_result_request" parameter is "0000"
                .body("transaction_result_request.responseCode", Matchers.is("0000"))
                //AND "responseCode" in the "transaction_result_response" equals to "202"
                .body("transaction_result_response.responseCode", Matchers.is("202"));
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: Pending To RetryableDecline To NonRetryableDecline (airtel)")
    @TmsLink("TECH-46769")
    public void testReserveAndTransactPendingToRetryableDeclineToNonRetryableDecline() throws InterruptedException {
        //add test cases
        val addTestCase1 = setUpAirtelSimData(InFlightTransactionLookupClient.ResponseCode_500, InFlightTransactionLookupClient.AirTel_purchase);
        val addTestCase2 = setUpAirtelSimData(ReserveAndTransactClient.ResponseCode_206, InFlightTransactionLookupClient.AirTel_lookup);

        addAirtelTestCases(Arrays.asList(addTestCase1, addTestCase2), Port.AIRTEL_SIMULATOR)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.contains(ReserveAndTransactClient.ResponseCode_206, InFlightTransactionLookupClient.ResponseCode_500))
                .body("id", Matchers.contains("", ""))
                .body("action", Matchers.contains(InFlightTransactionLookupClient.AirTel_lookup, InFlightTransactionLookupClient.AirTel_purchase))
                .body("delay", Matchers.contains(Integer.parseInt(ReserveAndTransactClient.Airtel_delay_3000), Integer.parseInt(ReserveAndTransactClient.Airtel_delay_3000)))
                .body("httpStatusCode", Matchers.contains(Integer.parseInt(ReserveAndTransactClient.responseCode0), Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                .body("fieldName", Matchers.contains("", ""));

        //perform R&T - purchase airtel product
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.Product_Airtel_130, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
        Thread.sleep(3000);

        //Set up testcase where action is purchase non retryable decline
        val addTestCase3 = setUpAirtelSimData(ReserveAndTransactClient.ResponseCode_17017, InFlightTransactionLookupClient.AirTel_purchase);
        val addTestCase4 = setUpAirtelSimData(ResponseCode_206, InFlightTransactionLookupClient.AirTel_lookup);
        addAirtelTestCases(Arrays.asList(addTestCase3, addTestCase4), Port.AIRTEL_SIMULATOR)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.contains(ReserveAndTransactClient.ResponseCode_206, ReserveAndTransactClient.ResponseCode_17017))
                .body("id", Matchers.contains("", ""))
                .body("action", Matchers.contains(InFlightTransactionLookupClient.AirTel_lookup, InFlightTransactionLookupClient.AirTel_purchase))
                .body("delay", Matchers.contains(Integer.parseInt(ReserveAndTransactClient.Airtel_delay_3000), Integer.parseInt(ReserveAndTransactClient.Airtel_delay_3000)))
                .body("httpStatusCode", Matchers.contains(Integer.parseInt(ReserveAndTransactClient.responseCode0), Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                .body("fieldName", Matchers.contains("", ""));

        Thread.sleep(240000);

        //set simulator to the default state (delete simulator tests)
        removeAllAirtelTestCases(Port.AIRTEL_SIMULATOR)
                .then().assertThat().statusCode(SC_OK);

        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Failed));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"reserve_fund_request" parameter isn't empty
                .body("reserve_fund_request.raasTxnRef", Matchers.is(raasTxnRef))
                //AND "responseCode" in the "reserve_fund_response" equals to "0000"
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND two objects exist in the "ctx_request" array with "clientTransactionId" is"{transactionId}-0000" and "{transactionId}-0001"
                .body("ctx_request.clientTransactionId[1]", Matchers.is(raasTxnRef.concat("-0000")))
                .body("ctx_request.clientTransactionId[0]", Matchers.is(raasTxnRef.concat("-0001")))
                //AND only two objects exist in the "ctx_response" array
                //AND "responseCode" for "clientTransactionId": "{transactionId}-0000" object equals to "2213" AND "responseCode" for "clientTransactionId": "{transactionId}-0001" object equals to "2240"
                .body("ctx_response[1].responseCode", Matchers.is(2240))
                //AND transaction was pending (ctx lookup with response code 2213)
                .body("ctx_response[0].responseCode", Matchers.is(2213))
                //AND more than one object is in the "ctx_lookup_request"
                .body("ctx_lookup_request.clientTransactionId[1]", Matchers.is(raasTxnRef.concat("-0000")))
                .body("ctx_lookup_request.clientTransactionId[0]", Matchers.is(raasTxnRef.concat("-0000")))
                //AND more than one object is in the "ctx_lookup_response"
                //AND "responseCode" for "clientTransactionId": " {transactionId}-0000" object equals to "2240" AND "responseCode" for "clientTransactionId": "{transactionId}-0000" object equals to "2201"
                .body("ctx_lookup_response.responseCode[0]", Matchers.is(2201))
                .body("ctx_lookup_response.responseCode[1]", Matchers.is(2240))
                //AND "responseCode" in "transaction_result_request" parameter is "2213"
                .body("transaction_result_request.responseCode", Matchers.is("2213"))
                //AND "responseCode" in the "transaction_result_response" equals to "202"
                .body("transaction_result_response.responseCode", Matchers.is("202"));
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: RetryableDecline To Pending To SUCCESS (airtel)")
    @TmsLink("TECH-57303")
    public void testReserveAndTransactRetryableDeclineToPendingToSuccess() throws InterruptedException {
        //add test cases
        val addTestCase1 = setUpAirtelSimData(ReserveAndTransactClient.ResponseCode_2238, InFlightTransactionLookupClient.AirTel_purchase);
        val addTestCase2 = setUpAirtelSimData(InFlightTransactionLookupClient.ResponseCode_200, InFlightTransactionLookupClient.AirTel_lookup);

        addAirtelTestCases(Arrays.asList(addTestCase1, addTestCase2), Port.AIRTEL_SIMULATOR)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.contains(InFlightTransactionLookupClient.ResponseCode_200, ReserveAndTransactClient.ResponseCode_2238))
                .body("id", Matchers.contains("", ""))
                .body("action", Matchers.contains(InFlightTransactionLookupClient.AirTel_lookup, InFlightTransactionLookupClient.AirTel_purchase))
                .body("delay", Matchers.contains(Integer.parseInt(ReserveAndTransactClient.Airtel_delay_3000), Integer.parseInt(ReserveAndTransactClient.Airtel_delay_3000)))
                .body("httpStatusCode", Matchers.contains(Integer.parseInt(ReserveAndTransactClient.responseCode0), Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                .body("fieldName", Matchers.contains("", ""));

        //perform R&T - purchase airtel product
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.Product_Airtel_130, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
        Thread.sleep(3000);
        //Set up testcase where action is purchase pending
        val addTestCase3 = setUpAirtelSimData(InFlightTransactionLookupClient.ResponseCode_500, InFlightTransactionLookupClient.AirTel_purchase);
        val addTestCase4 = setUpAirtelSimData(InFlightTransactionLookupClient.ResponseCode_200, InFlightTransactionLookupClient.AirTel_lookup);
        addAirtelTestCases(Arrays.asList(addTestCase3,addTestCase4), Port.AIRTEL_SIMULATOR)
                .then().assertThat().statusCode(SC_OK).body("responseCode", Matchers.contains(InFlightTransactionLookupClient.ResponseCode_200, InFlightTransactionLookupClient.ResponseCode_500))
                .body("id", Matchers.contains("", ""))
                .body("action", Matchers.contains(InFlightTransactionLookupClient.AirTel_lookup, InFlightTransactionLookupClient.AirTel_purchase))
                .body("delay", Matchers.contains(Integer.parseInt(ReserveAndTransactClient.Airtel_delay_3000), Integer.parseInt(ReserveAndTransactClient.Airtel_delay_3000)))
                .body("httpStatusCode", Matchers.contains(Integer.parseInt(ReserveAndTransactClient.responseCode0), Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                .body("fieldName", Matchers.contains("", ""));

        Thread.sleep(240000);
        //set simulator to the default state (delete simulator tests)
        removeAllAirtelTestCases(Port.AIRTEL_SIMULATOR)
                .then().assertThat().statusCode(SC_OK);

        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Success));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"reserve_fund_request" parameter isn't empty
                .body("reserve_fund_request.raasTxnRef", Matchers.is(raasTxnRef))
                //AND "responseCode" in the "reserve_fund_response" equals to "0000"
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND two objects exist in the "ctx_request" array with "clientTransactionId" is"{transactionId}-0000" and "{transactionId}-0001"
                .body("ctx_request.clientTransactionId[1]", Matchers.is(raasTxnRef.concat("-0000")))
                .body("ctx_request.clientTransactionId[0]", Matchers.is(raasTxnRef.concat("-0001")))
                //AND only two objects exist in the "ctx_response" array
                //AND "responseCode" for "clientTransactionId": "{transactionId}-0000" object equals to "2201" AND "responseCode" for "clientTransactionId": "{transactionId}-0001" object equals to "2240"
                .body("ctx_response[1].responseCode", Matchers.is(2201))
                //AND transaction was pending (ctx lookup with response code 2240)
                .body("ctx_response[0].responseCode", Matchers.is(2240))
                //AND more than one object is in the "ctx_lookup_request"
                .body("ctx_lookup_request.clientTransactionId[1]", Matchers.is(raasTxnRef.concat("-0001")))
                .body("ctx_lookup_request.clientTransactionId[0]", Matchers.is(raasTxnRef.concat("-0001")))
                //AND more than one object is in the "ctx_lookup_response"
                //AND "responseCode" for "clientTransactionId": " {transactionId}-0000" object equals to "2240" AND "responseCode" for "clientTransactionId": "{transactionId}-0000" object equals to "0"
                .body("ctx_lookup_response.responseCode[0]", Matchers.is(0))
                .body("ctx_lookup_response.responseCode[1]", Matchers.is(2240))
                //AND "responseCode" in "transaction_result_request" parameter is "0000"
                .body("transaction_result_request.responseCode", Matchers.is("0000"))
                //AND "responseCode" in the "transaction_result_response" equals to "202"
                .body("transaction_result_response.responseCode", Matchers.is("202"));


    }

    @Test
    @Description("30100 :: payd-raas-gateway :: RetryableDecline To Pending To NonRetryableDecline (mtn_za)")
    @TmsLink("TECH-57304")
    public void testReserveAndTransactRetryableDeclineToPendingToNonRetryableDecline() throws InterruptedException {
        //add test cases
        val addTestCase1 = setUpMtnSimData("3803", "27837640171", "virtual_recharge", 200);
        val addTestCase2 = setUpMtnSimData("3803", "27837640171", "bundle_recharge", 200);
        val addTestCase3 = setUpMtnSimData("3803", "27837640171", "repeat_virtual_recharge", 200);
        val addTestCase4 = setUpMtnSimData("3803", "27837640171", "repeat_bundle_recharge", 200);

        addMtnTestCases(Arrays.asList(addTestCase1,addTestCase2, addTestCase3, addTestCase4 ), Port.MTN_SIMULATOR)
                .then().assertThat().statusCode(SC_OK);

        //perform R&T - purchase mtn product
        val jsonBody = setUpReserveAndTransactV4Data("2", ZAR, USSD, ChannelId.USSD, "400", "10000", "0", "27837640171");

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString("0000"))
                .body("responseMessage", Matchers.containsString("Processing request (funds reserved)"))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

        //add first test: mapped to ctx PENDING response code (9318) "action" is "purchase" (virtual_recharge)
        val addTestCase5 = setUpMtnSimData("9318", "27837640171", "virtual_recharge", 200);
        //add second test: mapped to ctx NON_RETRYABLE_DECLINE response code (9313) "action" is "lookup" (repeat_virtual_recharge)
        val addTestCase6 = setUpMtnSimData("9313", "27837640171", "repeat_virtual_recharge", 200);
        val addTestCase7 = setUpMtnSimData("9318", "27837640171", "bundle_recharge", 200);
        val addTestCase8 = setUpMtnSimData("9313", "27837640171", "repeat_bundle_recharge", 200);

        addMtnTestCases(Arrays.asList(addTestCase5, addTestCase6, addTestCase7, addTestCase8), Port.MTN_SIMULATOR)
                .then().assertThat().statusCode(SC_OK);
        Thread.sleep(240000);

        //set simulator to the default state (delete simulator tests)
        removeAllMtnTestCases(Port.MTN_SIMULATOR)
                .then().assertThat().statusCode(SC_OK);

        //Verify transaction status is "FAILED"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, 2, queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString("FAILED"));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"reserve_fund_request" parameter isn't empty
                .body("reserve_fund_request.raasTxnRef", Matchers.is(raasTxnRef))
                //AND "responseCode" in the "reserve_fund_response" equals to "0000"
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND two objects exist in the "ctx_request" array with "clientTransactionId" is"{transactionId}-0000" and "{transactionId}-0001"
                .body("ctx_request.clientTransactionId[1]", Matchers.is(raasTxnRef.concat("-0000")))
                .body("ctx_request.clientTransactionId[0]", Matchers.is(raasTxnRef.concat("-0001")))
                //AND only two objects exist in the "ctx_response" array AND "responseCode" for "clientTransactionId": "{transactionId}-0000" object equals to "2236"AND "responseCode" for clientTransactionId": "{transactionId}-0001" object equals to "2201"
                .body("ctx_response[1].responseCode", Matchers.is(2201))
                //AND transaction was pending (ctx lookup with response code 2236)
                .body("ctx_response[0].responseCode", Matchers.is(2236))
                //AND more than one object is in the "ctx_lookup_request"
                .body("ctx_lookup_request.clientTransactionId[1]", Matchers.is(raasTxnRef.concat("-0001")))
                .body("ctx_lookup_request.clientTransactionId[0]", Matchers.is(raasTxnRef.concat("-0001")))
                //AND more than one object is in the "ctx_lookup_response"
                //AND "responseCode" for "clientTransactionId": " {transactionId}-0001" object equals to "2236" AND "responseCode" for "clientTransactionId": "{transactionId} -0001" object equals to "2213"
                .body("ctx_lookup_response.responseCode[0]", Matchers.is(2213))
                .body("ctx_lookup_response.responseCode[1]", Matchers.is(2236))
                //AND "responseCode" in "transaction_result_request" parameter is "2213"
                .body("transaction_result_request.responseCode", Matchers.is("2213"))
                //AND "responseCode" in the "transaction_result_response" equals to "202"
                .body("transaction_result_response.responseCode", Matchers.is("202"));

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: 2510 Purchase amount exceeds allowed maximum product price amount")
    @TmsLink("TECH-69564")
    public void testReserveAndTransactV4PurchaseAmountExceedsAllowedMaximumPrice() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.Product_1200, ReserveAndTransactClient.PurchaseAmount1000000001, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
//        //raas db check replaced with API check (TransactionLookup)
//        assertThat(getTransactionStatus(raasTxnRef))
//                .as("Postgres SQL query : Transaction Status incorrect")
//                .isTrue();

        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        Thread.sleep(10000);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Failed));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"ctx_request" parameter isn't empty
                .body("ctx_request.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //Verify funds were successfully reserved (response_code equals to 0000)
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND ctx response code is SUCCESSFUL (0)
                //"responseCode" in the "ctx_response" equals to "2510"
                .body("ctx_response[0].responseCode", Matchers.is(Integer.parseInt(ReserveAndTransactClient.ResponseCode_2510)));
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: 2509 Purchase amount below allowed minimum product price amount")
    @TmsLink("TECH-69567")
    public void testReserveAndTransactV4PurchaseAmountBelowAllowedMaximumPrice() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.PurchaseAmount99, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
//        //raas db check replaced with API check (TransactionLookup)
//        assertThat(getTransactionStatus(raasTxnRef))
//                .as("Postgres SQL query : Transaction Status incorrect")
//                .isTrue();

        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        Thread.sleep(10000);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Failed));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"ctx_request" parameter isn't empty
                .body("ctx_request.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //Verify funds were successfully reserved (response_code equals to 0000)
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND ctx response code is SUCCESSFUL (0)
                //"responseCode" in the "ctx_response" equals to "2509"
                .body("ctx_response[0].responseCode", Matchers.is(Integer.parseInt(ReserveAndTransactClient.ResponseCode_2509)));
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: 2511 Purchase amount does not use correct increment for product")
    @TmsLink("TECH-69569")
    public void testReserveAndTransactV4PurchaseAmountIncorrectIncrement() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_100, ReserveAndTransactClient.PurchaseAmount10001, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        Thread.sleep(10000);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Failed));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"reserve_fund_request" parameter isn't empty
                .body("reserve_fund_request.raasTxnRef", Matchers.is(raasTxnRef))
                //Verify funds were successfully reserved (response_code equals to 0000)
                // "responseCode" in the "reserve_fund_response" equals to "0000"
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"ctx_request" parameter isn't empty
                .body("ctx_request.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND ctx response code is SUCCESSFUL (0)
                //"responseCode" in the "ctx_response" equals to "2511"
                .body("ctx_response[0].responseCode", Matchers.is(Integer.parseInt(ReserveAndTransactClient.ResponseCode_2511)))
                //AND successful transaction result is sent (0000)
                //"transaction_result_request" parameter isn't empty
                .body("transaction_result_request.responseCode", Matchers.is(ReserveAndTransactClient.ResponseCode_2511))
                //AND success response code is received from the funding source (202)
                //"responseCode" in the "transaction_result_response" equals to "202"
                .body("transaction_result_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode202))
                //AND transaction wasn't retried (no records found in the db)
                .body("ctx_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND transaction wasn't pending (no records found in the db)
                .body("ctx_lookup_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)));
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: \"Transaction request not valid\" error")
    @TmsLink("TECH-93004")
    public void testReserveAndTransactInvalidJsonBody() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_100, ReserveAndTransactClient.PurchaseAmountInvalid, ReserveAndTransactClient.FeeAmount10, ReserveAndTransactClient.Identifier);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageInvalidJsonBody))
                .body("raasTxnRef", Matchers.blankOrNullString())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: 2506 Purchase amount does not correspond to product price amount")
    @TmsLink("TECH-69571")
    public void testReserveAndTransactV4PurchaseAmountNotCorrespondToPriceAmount() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.Product_919, ReserveAndTransactClient.PurchaseAmount200, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier_9);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        Thread.sleep(20000);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(Failed));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"reserve_fund_request" parameter isn't empty
                .body("reserve_fund_request.raasTxnRef", Matchers.is(raasTxnRef))
                //Verify funds were successfully reserved (response_code equals to 0000)
                // "responseCode" in the "reserve_fund_response" equals to "0000"
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"ctx_request" parameter isn't empty
                .body("ctx_request.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND ctx response code is SUCCESSFUL (0)
                //"responseCode" in the "ctx_response" equals to "2506"
                .body("ctx_response[0].responseCode", Matchers.is(Integer.parseInt(ReserveAndTransactClient.ResponseCode_2506)))
                //AND successful transaction result is sent (0000)
                //"transaction_result_request" parameter isn't empty
                .body("transaction_result_request.responseCode", Matchers.is(ReserveAndTransactClient.responseCode2201))
                //AND success response code is received from the funding source (202)
                //"responseCode" in the "transaction_result_response" equals to "202"
                .body("transaction_result_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode202))
                //AND transaction wasn't retried (no records found in the db)
                .body("ctx_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND transaction wasn't pending (no records found in the db)
                .body("ctx_lookup_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)));
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: Reserve Funds transaction Reference was not expected error")
    @TmsLink("TECH-92973")
    public void testReserveAndTransactV4ReserveFundsTransactionReferenceError() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.Product_919, ReserveAndTransactClient.PurchaseAmount200, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier_9, clientTxnRef);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageReserveFundsTransaction))
                .body("raasTxnRef", Matchers.blankOrNullString())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: \"Invalid Fee Amount\" error")
    @TmsLink("TECH-93001")
    public void testReserveAndTransactV4InvalidFeeAmount() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.Product_919, ReserveAndTransactClient.PurchaseAmount200, ReserveAndTransactClient.FeeAmount10, ReserveAndTransactClient.Identifier_9);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_2055))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageInvalidAmount))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        Thread.sleep(20000);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(Failed));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "2055"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.ResponseCode_2055));
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: Reserve Funds transaction Reference was not expected error")
    @TmsLink("TECH-92973")
    public void testReserveAndTransactV3ReserveFundsTransactionReferenceError() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV3Data(ReserveAndTransactClient.TestClient3, ChannelName.MOBILE, ChannelId.MOBILE, ReserveAndTransactClient.ProductAirtel_917, clientTxnRef);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V3)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageReserveFundsTransaction))
                .body("raasTxnRef", Matchers.blankOrNullString())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: Reserve Funds transaction Reference was not expected error")
    @TmsLink("TECH-92973")
    public void testReserveAndTransactV2ReserveFundsTransactionReferenceError() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV2Data(ReserveAndTransactClient.TestClient3, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, clientTxnRef);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V2)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageReserveFundsTransaction))
                .body("raasTxnRef", Matchers.blankOrNullString())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: Funding Source not linked to Client error")
    @TmsLink("TECH-92991")
    public void testReserveAndTransactV4FundingSourceNotLinked() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.Product_919, ReserveAndTransactClient.PurchaseAmount200, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier_9, clientTxnRef, fundingSourceId_4);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundingResourceNotLinked))
                .body("raasTxnRef", Matchers.blankOrNullString())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: error when financial terms are missed for vendor, client and product")
    @TmsLink("TECH-74564")
    public void testReserveAndTransactV4FinancialTermsMissed() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.Product_1201, ReserveAndTransactClient.PurchaseAmount200, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier_9);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(TransactClient.responseMessageServiceTUnavailable))
                .body("raasTxnRef", Matchers.blankOrNullString())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
    }


    @Test
    @Description("30100 :: payd-raas-gateway :: 4000 error when product doesn't exist in the system")
    @TmsLink("TECH-74597")
    public void testReserveAndTransactWhenProductIDdoesNotExists() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.Product_11, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessagePID11NotFound))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v4/reserveAndTransact :: 0001 Service temporarily unavailable error")
    @TmsLink("TECH-93007")
    public void testReserveAndTransactWithErrorServiceUnavailable() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4DataToTestServiceUnavailable(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, NotificationClient.Identifier_6);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(TransactClient.ResponseCode_0001))
                .body("responseMessage", Matchers.containsString(TransactClient.responseMessageServiceTUnavailable))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v2/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-93384")
    public void testReserveAndTransactV2AccountIdentifierMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV2DataAccIdentifierMaxLimit(ReserveAndTransactClient.TestClient3, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.AccountIdentifierV2MaxLimit);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V2)
                .then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(TransactClient.responseMessageServiceTUnavailable))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v2/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-140782")
    public void testReserveAndTransactV2ClientTxnRefMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV2DataClientTxnRefMaxLimit(ReserveAndTransactClient.TestClient3, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.clientTxnRefV2MaxLimit);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V2)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageClientTxnRef))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v2/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-140783")
    public void testReserveAndTransactV2ChannelSessionIdMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV2DataChannelSessionIDMaxLimit(ReserveAndTransactClient.TestClient3, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.channelSessionIdV2MaxLimit);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V2)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(TransactClient.responseMessageAlphaNumericCSID))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v2/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-140784")
    public void testReserveAndTransactV2AuthCodeMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV2DataAuthCodeMaxLimit(ReserveAndTransactClient.TestClient3, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.authCodeV2MaxLimit);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V2)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageAuthCode))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v2/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-140785")
    public void testReserveAndTransactV2TimeStampMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV2DataTimeStampMaxLimit(ReserveAndTransactClient.TestClient3, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.timeStampMaxLimit);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V2)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageInvalidJsonBody))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v2/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-140786")
    public void testReserveAndTransactV2ClientIdMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV2Data(ReserveAndTransactClient.ClientIdInvalid, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V2)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageInvalidJsonBody))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v2/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-140787")
    public void testReserveAndTransactV2ProductIDMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV2Data(TestClient3, USSD, ChannelId.USSD, ReserveAndTransactClient.Product_Invalid);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V2)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageInvalidJsonBody))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v2/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-140788")
    public void testReserveAndTransactV2PurchaseAmountMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV2DataPurchaseAmountMaxLimit(TestClient3, USSD, ChannelId.USSD, ProductAirtel_917, ReserveAndTransactClient.PurchaseAmountMaxLimit);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V2)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessagePurchaseAmountMaxLimit))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v2/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-140789")
    public void testReserveAndTransactV2ChannelIDMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV2Data(TestClient3, USSD, ChannelId.INVALID, ProductAirtel_917);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V2)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageChannelIDMaxLimit))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

    }

        @Test
    @Description("30100 :: payd-raas-gateway :: v2/reserveAndTransact :: char limit exceeded value for request parameters (SourceIdentifierMaxLimit)")
    @TmsLink("TECH-123386")
    public void testReserveAndTransactV2SourceIdentifierMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV2DataSourceIdentifier(TestClient3, USSD, ChannelId.USSD, ProductAirtel_917, SourceIdentifierMaxLimit);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V2)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageSourceIdentifierMaxLimit))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v2/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-140790")
    public void testReserveAndTransactV2TargetIdentifierMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV2DataTargetIdentifier(TestClient3, USSD, ChannelId.USSD, ProductAirtel_917, TargetIdentifierMaxLimit);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V2)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageTargetIdentifierMaxLimit))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

    }
    @Test
    @Description("30100 :: payd-raas-gateway :: v2/reserveAndTransact :: char limit exceeded value for request parameters (ChannelNameMaxLimit)")
    @TmsLink("TECH-123385")
    public void testReserveAndTransactV2ChannelNameMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV2Data(TestClient3, INVALID, ChannelId.USSD, ProductAirtel_917);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V2)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", CombinableMatcher.either(Matchers.containsString(TransactClient.responseMessageChannelName)).or(Matchers.containsString(ReserveAndTransactClient.responseMessageChannelNameAlphanumeric)))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v3/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-93383")
    public void testReserveAndTransactV3AccIdentifierMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV3DataAccIdentifier(ReserveAndTransactClient.TestClient3, ChannelName.MOBILE, ChannelId.MOBILE, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.AccountIdentifierV2MaxLimit);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V3)
                .then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR)
                .body("responseCode", Matchers.containsString(ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(TransactClient.responseMessageServiceTUnavailable))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v3/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-139906")
    public void testReserveAndTransactV3ClientTxnRefMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV3DataClientTxnRef(ReserveAndTransactClient.TestClient3, ChannelName.MOBILE, ChannelId.MOBILE, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.clientTxnRefV2MaxLimit);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V3)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageClientTxnRef))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v3/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-139907")
    public void testReserveAndTransactV3channelSessionIdMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV3DataChannelSessionId(ReserveAndTransactClient.TestClient3, ChannelName.MOBILE, ChannelId.MOBILE, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.channelSessionIdV2MaxLimit);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V3)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(TransactClient.responseMessageAlphaNumericCSID))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v3/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-139908")
    public void testReserveAndTransactV3authCodeMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV3DataAuthCode(ReserveAndTransactClient.TestClient3, ChannelName.MOBILE, ChannelId.MOBILE, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.authCodeV2MaxLimit);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V3)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageAuthCode))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v3/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-139909")
    public void testReserveAndTransactV3timestampMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV3DataTimestamp(ReserveAndTransactClient.TestClient3, ChannelName.MOBILE, ChannelId.MOBILE, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.timeStampMaxLimit);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V3)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageInvalidJsonBody))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v3/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-139911")
    public void testReserveAndTransactV3ClientIdMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV3Data(ReserveAndTransactClient.ClientIdInvalid, ChannelName.MOBILE, ChannelId.MOBILE, ReserveAndTransactClient.ProductAirtel_917);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V3)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageInvalidJsonBody))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v3/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-139912")
    public void testReserveAndTransactV3productIdMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV3Data(ReserveAndTransactClient.TestClient3, ChannelName.MOBILE, ChannelId.MOBILE, ReserveAndTransactClient.Product_Invalid);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V3)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageInvalidJsonBody))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v3/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-139913")
    public void testReserveAndTransactV3purchaseAmountMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV3DataPurchaseAmount(ReserveAndTransactClient.TestClient3, ChannelName.MOBILE, ChannelId.MOBILE, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.PurchaseAmountMaxLimit);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V3)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessagePurchaseAmountMaxLimit))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v3/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-139914")
    public void testReserveAndTransactV3feeAmountMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV3DataFeeAmount(ReserveAndTransactClient.TestClient3, ChannelName.MOBILE, ChannelId.MOBILE, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.clientTxnRefV2MaxLimit);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V3)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageInvalidJsonBody))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v3/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-139915")
    public void testReserveAndTransactV3channelIDMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV3Data(ReserveAndTransactClient.TestClient3, ChannelName.MOBILE, ChannelId.INVALID, ReserveAndTransactClient.ProductAirtel_917);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V3)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageChannelIDMaxLimit))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

    }
    @Test
    @Description("30100 :: payd-raas-gateway :: v3/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-139916")
    public void testReserveAndTransactV3sourceIdentifierMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV3DataSourceIdentifier(ReserveAndTransactClient.TestClient3, ChannelName.MOBILE, ChannelId.MOBILE, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.SourceIdentifierMaxLimit);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V3)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageSourceIdentifierMaxLimit))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v3/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-139917")
    public void testReserveAndTransactV3targetIdentifierMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV3DataTargetIdentifier(ReserveAndTransactClient.TestClient3, ChannelName.MOBILE, ChannelId.MOBILE, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.TargetIdentifierMaxLimit);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V3)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageTargetIdentifierMaxLimit))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

    }
    @Test
    @Description("30100 :: payd-raas-gateway :: v3/reserveAndTransact :: char limit exceeded value for request parameters (channelNameMaxLimit)")
    @TmsLink("TECH-123383")
    public void testReserveAndTransactV3channelNameMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV3Data(ReserveAndTransactClient.TestClient3, ChannelName.INVALID, ChannelId.MOBILE, ReserveAndTransactClient.ProductAirtel_917);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V3)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ResponseCode_4000))
                .body("responseMessage", CombinableMatcher.either(Matchers.containsString(TransactClient.responseMessageChannelName)).or(Matchers.containsString(ReserveAndTransactClient.responseMessageChannelNameAlphanumeric)))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v4/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-93370")
    public void testReserveAndTransactV4AccountIdentifierMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4DataAccountIdentifier(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier, ReserveAndTransactClient.AccountIdentifierV2MaxLimit);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(TransactClient.responseMessageServiceTUnavailable))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v4/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-139614")
    public void testReserveAndTransactV4ClientTxnRefMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4DataClientTxnRef(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier, ReserveAndTransactClient.clientTxnRefV2MaxLimit);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageClientTxnRef))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v4/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-139616")
    public void testReserveAndTransactV4channelSessionIdMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4DataChannelSessionId(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier, ReserveAndTransactClient.channelSessionIdV2MaxLimit);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(TransactClient.responseMessageAlphaNumericCSID))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v4/reserveAndTransact :: char limit exceeded value for request parameters (AuthCodeMaxLimit)")
    @TmsLink("TECH-123392")
    public void testReserveAndTransactV4AuthCodeMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4DataAuthCode(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier, ReserveAndTransactClient.authCodeV2MaxLimit);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageAuthCode))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v4/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-139617")
    public void testReserveAndTransactV4TimeStampMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4DataTimeStamp(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier, ReserveAndTransactClient.authCodeV2MaxLimit);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageInvalidJsonBody))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v4/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-139618")
    public void testReserveAndTransactV4ClientIdMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ClientIdInvalid, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageInvalidJsonBody))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v4/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-139619")
    public void testReserveAndTransactV4FundingSourceMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier_9, ReserveAndTransactClient.clientTxnRef, ReserveAndTransactClient.ClientIdInvalid);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageInvalidJsonBody))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v4/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-139622")
    public void testReserveAndTransactV4PurchaseAmountMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.Product_919, ReserveAndTransactClient.PurchaseAmountMaxLimit, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier_9);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessagePurchaseAmountMaxLimit))
                .body("raasTxnRef", Matchers.blankOrNullString())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v4/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-139623")
    public void testReserveAndTransactV4FeeAmountMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.Product_919, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.clientTxnRef, ReserveAndTransactClient.Identifier_9);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageInvalidJsonBody))
                .body("raasTxnRef", Matchers.blankOrNullString())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v4/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-139624")
    public void testReserveAndTransactV4CurrencyCodeMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, INVALIDCC, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageCurrencyCodeMaxLimitCurrency))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v4/reserveAndTransact :: char limit exceeded value for request parameters (ChannelIdMaxLimit)")
    @TmsLink("TECH-123393")
    public void testReserveAndTransactV4ChannelIdMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.INVALID, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageChannelIDMaxLimit))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
    }


    @Test
    @Description("30100 :: payd-raas-gateway :: v4/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-139625")
    public void testReserveAndTransactV4SourceIdentifierMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4DataSourceIdentifier(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.SourceIdentifierMaxLimit);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageSourceIdentifierMaxLimit))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v4/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-139626")
    public void testReserveAndTransactV4TargetIdentifierMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4DataTargetIdentifier(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.TargetIdentifierMaxLimit);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageTargetIdentifierMaxLimit))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
    }
    @Test
    @Description("30100 :: payd-raas-gateway :: v4/reserveAndTransact :: char limit exceeded value for request parameters (ChannelNameMaxLimit)")
    @TmsLink("TECH-123381")
    public void testReserveAndTransactV4ChannelNameMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, INVALID, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", CombinableMatcher.either(Matchers.containsString(TransactClient.responseMessageChannelName)).or(Matchers.containsString(ReserveAndTransactClient.responseMessageChannelNameAlphanumeric)))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
    }
    @Test
    @Description("30100 :: payd-raas-gateway :: v3/reserveAndTransact :: char limit exceeded value for request parameters")
    @TmsLink("TECH-139621")
    public void testReserveAndTransactV4productIdMaxLimit() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, INVALID, ChannelId.USSD, Product_Invalid, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V3)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageInvalidJsonBody))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: perform successful transaction when test client id matches funding source id")
    @TmsLink("TECH-57952")
    public void testReserveAndTransactV4ClientIdFundingSourceSame() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_100, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
//        //raas db check replaced with API check (TransactionLookup)
//        assertThat(getTransactionStatus(raasTxnRef))
//                .as("Postgres SQL query : Transaction Status incorrect")
//                .isTrue();

        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        Thread.sleep(10000);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Success));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"ctx_request" parameter isn't empty
                .body("ctx_request.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //Verify funds were successfully reserved (response_code equals to 0000)
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND ctx response code is SUCCESSFUL (0)
                //"responseCode" in the "ctx_response" equals to "0"
                .body("ctx_response[0].responseCode", Matchers.is(Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                //AND successful transaction result is sent (0000)
                //"transaction_result_request" parameter isn't empty
                .body("transaction_result_request.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND success response code is received from the funding source (202)
                //"responseCode" in the "transaction_result_response" equals to "202"
                .body("transaction_result_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode202))
                //AND transaction wasn't retried (no records found in the db)
                .body("ctx_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND transaction wasn't pending (no records found in the db)
                .body("ctx_lookup_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.responseCode0000)));
    }

    @Test
    @Description("30100 :: payd-raas-gateway :: Funding Source not linked to Client error")
    @TmsLink("TECH-57981")
    public void testReserveAndTransactV4FundingSourceClientIdDifferent() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4DataClientAndFundingDiff(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.PurchaseAmount200, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier, fundingSourceId_1500);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();

        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        Thread.sleep(10000);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Success));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"ctx_request" parameter isn't empty
                .body("ctx_request.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //Verify funds were successfully reserved (response_code equals to 0000)
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND ctx response code is SUCCESSFUL (0)
                //"responseCode" in the "ctx_response" equals to "0"
                .body("ctx_response[0].responseCode", Matchers.is(Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                //AND successful transaction result is sent (0000)
                //"transaction_result_request" parameter isn't empty
                .body("transaction_result_request.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND success response code is received from the funding source (202)
                //"responseCode" in the "transaction_result_response" equals to "202"
                .body("transaction_result_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode202))
                //AND transaction wasn't retried (no records found in the db)
                .body("ctx_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND transaction wasn't pending (no records found in the db)
                .body("ctx_lookup_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.responseCode0000)));
    }

    /*@Test
    @Description("30100 :: payd-raas-gateway :: v1/transact(reserveAndTransact) :: char limit exceeded value for request parameters")
    @TmsLink("TECH-93385")
    public void testReserveAndTransactV1accountIdentifierMaxLimit() throws InterruptedException {
        val jsonBody = setUpTransactV1DataAccIdentifier(ReserveAndTransactClient.TestClient3, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, AccountIdentifierV2MaxLimit);

        val raasTxnRef = executeTransact(jsonBody, Port.TRANSACTIONS, Version.V1)
                .then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR)
                .body("responseCode", Matchers.containsString(ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(TransactClient.responseMessageServiceTUnavailable))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(TransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v1/transact(reserveAndTransact) :: char limit exceeded value for request parameters")
    @TmsLink("TECH-93385")
    public void testReserveAndTransactV1ClientTxnRefMaxLimit() throws InterruptedException {
        val jsonBody = setUpTransactV1DataClientTxnRefId(ReserveAndTransactClient.TestClient3, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, clientTxnRefV2MaxLimit);

        val raasTxnRef = executeTransact(jsonBody, Port.TRANSACTIONS, Version.V1)
                .then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR)
                .body("responseCode", Matchers.containsString(ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(TransactClient.responseMessageServiceTUnavailable))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(TransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v1/transact(reserveAndTransact) :: char limit exceeded value for request parameters")
    @TmsLink("TECH-93385")
    public void testReserveAndTransactV1ChannelSessionIdMaxLimit() throws InterruptedException {
        val jsonBody = setUpTransactV1DataChannelSessionId(ReserveAndTransactClient.TestClient3, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, channelSessionIdV2MaxLimit);

        val raasTxnRef = executeTransact(jsonBody, Port.TRANSACTIONS, Version.V1)
                .then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR)
                .body("responseCode", Matchers.containsString(ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(TransactClient.responseMessageServiceTUnavailable))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(TransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v1/transact(reserveAndTransact) :: char limit exceeded value for request parameters")
    @TmsLink("TECH-93385")
    public void testReserveAndTransactV1TimeStampMaxLimit() throws InterruptedException {
        val jsonBody = setUpTransactV1DataTimeStamp(ReserveAndTransactClient.TestClient3, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, timeStampMaxLimit);

        val raasTxnRef = executeTransact(jsonBody, Port.TRANSACTIONS, Version.V1)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageInvalidJsonBody))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(TransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v1/transact(reserveAndTransact) :: char limit exceeded value for request parameters")
    @TmsLink("TECH-93385")
    public void testReserveAndTransactV1ProductIdMaxLimit() throws InterruptedException {
        val jsonBody = setUpTransactV1Data(ReserveAndTransactClient.TestClient3, USSD, ChannelId.USSD, ReserveAndTransactClient.Product_Invalid);

        val raasTxnRef = executeTransact(jsonBody, Port.TRANSACTIONS, Version.V1)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageInvalidJsonBody))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(TransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v1/transact(reserveAndTransact) :: char limit exceeded value for request parameters")
    @TmsLink("TECH-93385")
    public void testReserveAndTransactV1PurchaseAmountMaxLimit() throws InterruptedException {
        val jsonBody = setUpTransactV1DataPurchaseAmount(ReserveAndTransactClient.TestClient3, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.channelSessionIdV2MaxLimit);

        val raasTxnRef = executeTransact(jsonBody, Port.TRANSACTIONS, Version.V1)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageInvalidJsonBody))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(TransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v1/transact(reserveAndTransact) :: char limit exceeded value for request parameters")
    @TmsLink("TECH-93385")
    public void testReserveAndTransactV1ChannelIDMaxLimit() throws InterruptedException {
        val jsonBody = setUpTransactV1Data(ReserveAndTransactClient.TestClient3, USSD, ChannelId.MAXLIMIT, ReserveAndTransactClient.ProductAirtel_917);

        val raasTxnRef = executeTransact(jsonBody, Port.TRANSACTIONS, Version.V1)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageInvalidJsonBody))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(TransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v1/transact(reserveAndTransact) :: char limit exceeded value for request parameters")
    @TmsLink("TECH-93385")
    public void testReserveAndTransactV1ChannelNameMaxLimit() throws InterruptedException {
        val jsonBody = setUpTransactV1Data(ReserveAndTransactClient.TestClient3, INVALID, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917);

        val raasTxnRef = executeTransact(jsonBody, Port.TRANSACTIONS, Version.V1)
                .then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(TransactClient.responseMessageServiceTUnavailable))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(TransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v1/transact(reserveAndTransact) :: char limit exceeded value for request parameters")
    @TmsLink("TECH-93385")
    public void testReserveAndTransactV1SourceIdentifierMaxLimit() throws InterruptedException {
        val jsonBody = setUpTransactV1DataSourceIdentifier(ReserveAndTransactClient.TestClient3, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, SourceIdentifierMaxLimit);

        val raasTxnRef = executeTransact(jsonBody, Port.TRANSACTIONS, Version.V1)
                .then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(TransactClient.responseMessageServiceTUnavailable))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(TransactResponse.class).getRaasTxnRef();

    }

    @Test
    @Description("30100 :: payd-raas-gateway :: v1/transact(reserveAndTransact) :: char limit exceeded value for request parameters")
    @TmsLink("TECH-93385")
    public void testReserveAndTransactV1TargetIdentifierMaxLimit() throws InterruptedException {
        val jsonBody = setUpTransactV1DataTargetIdentifier(ReserveAndTransactClient.TestClient3, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, TargetIdentifierMaxLimit);

        val raasTxnRef = executeTransact(jsonBody, Port.TRANSACTIONS, Version.V1)
                .then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(TransactClient.responseMessageServiceTUnavailable))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(TransactResponse.class).getRaasTxnRef();
    }*/

    @Test
    @Description("30100 :: payd-raas-gateway :: ctx :: transaction with amount higher than \"topup_amount\" client in ctx should be successful")
    @TmsLink("TECH-79013")
    public void testReserveAndTransactV4AmountHigherThanTopUpAmount() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD,ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.PurchaseAmount20000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
//        //raas db check replaced with API check (TransactionLookup)
//        assertThat(getTransactionStatus(raasTxnRef))
//                .as("Postgres SQL query : Transaction Status incorrect")
//                .isTrue();

        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        Thread.sleep(10000);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Success));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"ctx_request" parameter isn't empty
                .body("ctx_request.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //Verify funds were successfully reserved (response_code equals to 0000)
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND ctx response code is SUCCESSFUL (0)
                //"responseCode" in the "ctx_response" equals to "0"
                .body("ctx_response[0].responseCode", Matchers.is(Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                //AND successful transaction result is sent (0000)
                //"transaction_result_request" parameter isn't empty
                .body("transaction_result_request.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND success response code is received from the funding source (202)
                //"responseCode" in the "transaction_result_response" equals to "202"
                .body("transaction_result_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode202))
                //AND transaction wasn't retried (no records found in the db)
                .body("ctx_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND transaction wasn't pending (no records found in the db)
                .body("ctx_lookup_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.responseCode0000)));
    }

    @Test
    @Description("POST /raas/v4/reserveAndTransact :: if body for the request is changed then the \"Header Signature invalid\" error should be returned in the response")
    @TmsLink("TECH-92994")
    public void testReserveAndTransactV3WithHeaderSignatureInvalidError() throws InterruptedException {
        //Creating signature
        val jsonBody = setUpReserveAndTransactV3DataWtihSignature(ReserveAndTransactClient.TestClient1003, MOBILE,
                ChannelId.MOBILE, ProductAirtel_917);
        val signature = getProductInfoWithSecretValue(jsonBody)
                .then().assertThat().statusCode(SC_OK)
                .extract().body().as(ReserveAndTransactResponse.class).getSignature();

        //R&T with Signature call execution success
        executeReserveAndTransactWithSignature(jsonBody, Port.TRANSACTIONS, Version.V4, signature)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(TransactResponse.class).getRaasTxnRef();


        //GIVEN valid signature is generated and provided as header in the v4/reserveAndTransact request
        //
        //AND any character is added/removed to the request body
        val jsonBody1 = setUpReserveAndTransactV3DataWtihSignaturetoExecuteWithInvalidBody(ReserveAndTransactClient.TestClient1003, MOBILE,
                ChannelId.MOBILE, ProductAirtel_917);
        executeReserveAndTransactWithSignature(jsonBody1, Port.TRANSACTIONS, Version.V4, signature)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageHeaderSignatureInvalid))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(TransactResponse.class).getRaasTxnRef();

    }
    @Test
    @Description("POST /raas/v4/reserveAndTransact :: if the \"Signature\" value is changed then the \"Header Signature invalid\" error should be returned in the response")
    @TmsLink("TECH-139599")
    public void testReserveAndTransactV3WithHeaderSignatureInvalidErrorInvalidSignature() throws InterruptedException {
        //Creating signature
        val jsonBody = setUpReserveAndTransactV3DataWtihSignature(ReserveAndTransactClient.TestClient1003, MOBILE,
                ChannelId.MOBILE, ProductAirtel_917);
        val signature = getProductInfoWithSecretValue(jsonBody)
                .then().assertThat().statusCode(SC_OK)
                .extract().body().as(ReserveAndTransactResponse.class).getSignature();

        //R&T with Signature call execution success
        executeReserveAndTransactWithSignature(jsonBody, Port.TRANSACTIONS, Version.V4, signature)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(TransactResponse.class).getRaasTxnRef();


        //GIVEN valid signature is generated and provided as header in the v4/reserveAndTransact request
        //AND any character is added/removed to the signature
        executeReserveAndTransactWithSignature(jsonBody, Port.TRANSACTIONS, Version.V4, signature + "a")
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageHeaderSignatureInvalid))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(TransactResponse.class).getRaasTxnRef();

    }
    @Test
    @Description("POST /raas/v4/reserveAndTransact :: if the \"Signature\" is not sent then the \"Header Signature invalid\" error should be returned in the response")
    @TmsLink("TECH-139600")
    public void testReserveAndTransactV3WithHeaderSignatureInvalidErrorNoSignature() throws InterruptedException {
        //Creating signature
        val jsonBody = setUpReserveAndTransactV3DataWtihSignature(ReserveAndTransactClient.TestClient1003, MOBILE,
                ChannelId.MOBILE, ProductAirtel_917);
        val signature = getProductInfoWithSecretValue(jsonBody)
                .then().assertThat().statusCode(SC_OK)
                .extract().body().as(ReserveAndTransactResponse.class).getSignature();

        //R&T with Signature call execution success
        executeReserveAndTransactWithSignature(jsonBody, Port.TRANSACTIONS, Version.V4, signature)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(TransactResponse.class).getRaasTxnRef();


        //GIVEN valid signature is generated and provided as header in the v3/reserveAndTransact request
        //
        //AND signature is not sent
        executeReserveAndTransactWithSignature(jsonBody, Port.TRANSACTIONS, Version.V4, null)
                .then().assertThat().statusCode(SC_BAD_REQUEST)
                .body("responseCode", Matchers.containsString(ResponseCode_4000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageHeaderSignatureInvalid))
                .body("raasTxnRef", Matchers.nullValue())
                .extract().body().as(TransactResponse.class).getRaasTxnRef();
    }
    @Test
    @Description("GET /getRaasFlow :: happy path")
    @TmsLink("TECH-146863")
    public void testGetRaasFlowHappyPath() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        Thread.sleep(20000);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Success));

        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"ctx_request" parameter isn't empty
                .body("ctx_request", Matchers.notNullValue())
                //Verify funds were successfully reserved (response_code equals to 0000)
                .body("reserve_fund_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND ctx response code is SUCCESSFUL (0)
                //"responseCode" in the "ctx_response" equals to "0"
                .body("ctx_response[0].responseCode", Matchers.is(Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                //AND successful transaction result is sent (0000)
                //"transaction_result_request" parameter isn't empty
                .body("transaction_result_request.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //AND success response code is received from the funding source (202)
                //"responseCode" in the "transaction_result_response" equals to "202"
                .body("transaction_result_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode202))
                //AND transaction wasn't retried (no records found in the db)
                .body("ctx_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND transaction wasn't pending (no records found in the db)
                .body("ctx_lookup_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.responseCode0000)));
    }
    @Test
    @Description("GET /getRaasInteractions :: happy path")
    @TmsLink("TECH-146861")
    public void testGetRaasInteractionsHappyPath() throws InterruptedException {
        val jsonBody = setUpReserveAndTransactV4Data(ReserveAndTransactClient.TestClient3, NGN, USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917, ReserveAndTransactClient.PurchaseAmount10000, ReserveAndTransactClient.FeeAmount0, ReserveAndTransactClient.Identifier);

        val raasTxnRef = executeReserveAndTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageFundsReserved))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(ReserveAndTransactResponse.class).getRaasTxnRef();
        //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        Thread.sleep(20000);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Success));

        //Verify against support tool API
        getRaasInteractions(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRefrence", Matchers.hasItem(raasTxnRef))
                .body ("channelSessionId", Matchers.hasItem(ReserveAndTransactClient.channelSessionId))
                .body("clientTxnRef", Matchers.notNullValue())
                .body("sourceIdentifier", Matchers.hasItem(ReserveAndTransactClient.Identifier))
                .body("responseMessage", Matchers.hasItem(responseMessageFundsReserved))
                .body("amount", Matchers.hasItem((Integer.parseInt(PurchaseAmount10000))));
    }
}