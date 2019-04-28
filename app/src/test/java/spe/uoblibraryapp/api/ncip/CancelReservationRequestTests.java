package spe.uoblibraryapp.api.ncip;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Map;

import spe.uoblibraryapp.Constants;

import static org.junit.Assert.assertEquals;


public class CancelReservationRequestTests {

    private String testPID = "abcdef-12345678-90ghi-jkl";
    private String testReservationID = "123456789";
    private String testBranchId = "159911";
    private String testAccessToken = "tk_1234567890";

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();


    @Test
    public void testRequestBodyIsCorrectOnInitialisation(){
        CancelReservationRequest cancelReservationRequest = getExampleRequestPreParams();
        assertEquals(Constants.RequestTemplates.cancelReservation, cancelReservationRequest.requestBody);
    }


    @Test
    public void testRequestBodyIsCorrectAfterParams(){
        CancelReservationRequest cancelReservationRequest = getExampleRequestAfterParams();
        assertEquals(String.format(Constants.RequestTemplates.cancelReservation, testBranchId, testPID, testReservationID), cancelReservationRequest.requestBody);
    }


    @Test
    public void testRequestHasCorrectURL() throws ParamsNotSetException{
        CancelReservationRequest cancelReservationRequest = getExampleRequestAfterParams();
        StringRequest request = cancelReservationRequest.createRequest(null);
        assertEquals(Constants.APIUrls.patronProfile, request.getUrl());
    }


    @Test
    public void testRequestHasCorrectMethod() throws ParamsNotSetException{
        CancelReservationRequest cancelReservationRequest = getExampleRequestAfterParams();
        StringRequest request = cancelReservationRequest.createRequest(null);
        assertEquals(Request.Method.POST, request.getMethod());
    }


    @Test
    public void testRequestHasCorrectAuthorizationHeader() throws ParamsNotSetException, AuthFailureError {
        CancelReservationRequest cancelReservationRequest = getExampleRequestAfterParams();
        StringRequest request = cancelReservationRequest.createRequest(null);
        Map<String, String> headers = request.getHeaders();
        assertEquals("Bearer " + testAccessToken, headers.get("Authorization"));
    }


    @Test
    public void testRequestHasCorrectContentType() throws ParamsNotSetException{
        CancelReservationRequest cancelReservationRequest = getExampleRequestAfterParams();
        StringRequest request = cancelReservationRequest.createRequest(null);
        assertEquals("application/xml", request.getBodyContentType());
    }


    @Test
    public void testRequestCannotBeCreatedBeforeParamsAdded() throws ParamsNotSetException{
        CancelReservationRequest cancelReservationRequest = getExampleRequestPreParams();
        exceptionRule.expect(ParamsNotSetException.class);
        exceptionRule.expectMessage("Cannot create request before params are added");
        cancelReservationRequest.createRequest(null);
    }


    private CancelReservationRequest getExampleRequestAfterParams(){
        CancelReservationRequest request = getExampleRequestPreParams();
        request.setRequestBodyParams(testBranchId, testPID, testReservationID);
        return request;
    }


    private CancelReservationRequest getExampleRequestPreParams(){
        return new CancelReservationRequest(testAccessToken);
    }
}
