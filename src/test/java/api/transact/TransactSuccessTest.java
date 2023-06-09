package api.transact;

import api.clients.ReserveAndTransactClient;
import api.domains.transact.model.TransactResponse;
import api.enums.*;
import io.qameta.allure.Description;
import io.qameta.allure.TmsLink;
import lombok.val;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import util.base_test.BaseApiTest;
import java.util.Hashtable;
import java.util.Map;

import static api.clients.SupportUiClient.getRaasFlow;
import static api.clients.TransactClient.executeTransact;
import static api.clients.TransactionLookupClient.findTransaction;
import static api.domains.transact.repo.TransactRequestRepo.*;
import static api.enums.ChannelName.USSD;
import static org.apache.http.HttpStatus.SC_OK;

public class TransactSuccessTest extends BaseApiTest {
    @Test(groups = {"smokeTest"})
    @Description("30100 :: payd-raas-gateway :: POST v4/transact :: SUCCESS :: Transact API (4.0)")
    @TmsLink("MKP-1010")
    public void testTransactV4Success() throws InterruptedException {
        val jsonBody = setUpTransactV4Data(ReserveAndTransactClient.TestClient3, CurrencyCode.NGN, ChannelName.INTERNET, ChannelId.INTERNET, ReserveAndTransactClient.ProductAirtel_917);

        val raasTxnRef = executeTransact(jsonBody, Port.TRANSACTIONS, Version.V4)
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

        //TODO: Verify against support tool API
        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //AND ctx response code is SUCCESSFUL (0)
                //"responseCode" in the "ctx_response" equals to "0"
                .body("ctx_response[0].responseCode", Matchers.is(Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"ctx_request" parameter isn't empty
                .body("ctx_request.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
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
    @Description("30100 :: payd-raas-gateway :: POST v3/transact :: SUCCESS")
    @TmsLink("MKP-1012")
    public void testTransactV3Success() throws InterruptedException {
        val jsonBody = setUpTransactV3Data(ReserveAndTransactClient.TestClient3, ChannelName.INTERNET, ChannelId.INTERNET, ReserveAndTransactClient.ProductAirtel_917);

        val raasTxnRef = executeTransact(jsonBody, Port.TRANSACTIONS, Version.V3)
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

    //TODO: Verify against support tool API
        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //AND ctx response code is SUCCESSFUL (0)
                //"responseCode" in the "ctx_response" equals to "0"
                .body("ctx_response[0].responseCode", Matchers.is(Integer.parseInt(ReserveAndTransactClient.responseCode0)))
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //"ctx_request" parameter isn't empty
                .body("ctx_request.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
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
    @Description("30100 :: payd-raas-gateway :: POST v2/transact :: SUCCESS")
    @TmsLink("MKP-931")
    public void testTransactV2Success() throws InterruptedException {
        val jsonBody = setUpTransactV2Data(ReserveAndTransactClient.TestClient3, ChannelName.USSD, ChannelId.USSD, ReserveAndTransactClient.ProductAirtel_917);

        val raasTxnRef = executeTransact(jsonBody, Port.TRANSACTIONS, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("responseCode", Matchers.containsString(ReserveAndTransactClient.responseCode0000))
                .body("responseMessage", Matchers.containsString(ReserveAndTransactClient.responseMessageProcessingRequest))
                .body("raasTxnRef", Matchers.notNullValue())
                .extract().body().as(TransactResponse.class).getRaasTxnRef();

    //Verify transaction status is "SUCCESS"
        Map<String, String> queryParams = new Hashtable<>();
        queryParams.put("raasTxnRef", raasTxnRef);
        Thread.sleep(30000);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Success));

        //TODO: Verify against support tool API
        //Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
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
    @Description("30100 :: payd-raas-gateway :: POST v1/transact SUCCESS")
    @TmsLink("MKP-1107")
    public void testTransactV1Success() throws InterruptedException {
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
        Thread.sleep(30000);
        findTransaction(Port.TRANSACTION_LOOKUP_SERVICE, Integer.parseInt(ReserveAndTransactClient.TestClient3), queryParams, Version.V2)
                .then().assertThat().statusCode(SC_OK)
                .body("raasTxnRef", Matchers.containsString(raasTxnRef))
                .body("transactionStatus", Matchers.containsString(ReserveAndTransactClient.Success));

        //TODO: Verify against support tool API
//Verify against support tool API
        getRaasFlow(Port.RAAS_FLOW, raasTxnRef)
                .then().assertThat().statusCode(SC_OK)
                //"responseCode" in the "raas_response" equals to "0000"
                .body("raas_response.responseCode", Matchers.is(ReserveAndTransactClient.responseCode0000))
                //THEN "raas_request" parameter isn't empty
                .body("raas_request.raasTxnRef", Matchers.is(raasTxnRef))
                //Verify funds were successfully reserved (response_code equals to 0000)
                //"responseCode" in the "reserve_fund_response" equals to "0000"
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
                //"ctx_request" parameter isn't empty
                .body("ctx_request.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.FirstTransactionCode)))
                //AND transaction wasn't pending (no records found in the db)
                .body("ctx_lookup_response.clientTransactionId", Matchers.not(raasTxnRef.concat(ReserveAndTransactClient.ZeroTransactionCode)));
    }

}