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


public class LookupUserRequestTests {

    private String testPID = "abcdef-12345678-90ghi-jkl";
    private String testAccessToken = "tk_1234567890";

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();




    @Test
    public void testRequestBodyIsCorrectOnInitialisation(){
        LookupUserRequest lookupUserRequest = getExampleRequestPreParams();
        assertEquals(Constants.RequestTemplates.lookupUser, lookupUserRequest.requestBody);
    }


    @Test
    public void testRequestBodyIsCorrectAfterParams(){
        LookupUserRequest lookupUserRequest = getExampleRequestAfterParams();
        assertEquals(String.format(Constants.RequestTemplates.lookupUser, testPID), lookupUserRequest.requestBody);
    }

    @Test
    public void testRequestHasCorrectURL() throws ParamsNotSetException{
        LookupUserRequest lookupUserRequest = getExampleRequestAfterParams();
        StringRequest request = lookupUserRequest.createRequest(null);
        assertEquals(Constants.APIUrls.patronProfile, request.getUrl());
    }

    @Test
    public void testRequestHasCorrectMethod() throws ParamsNotSetException{
        LookupUserRequest lookupUserRequest = getExampleRequestAfterParams();
        StringRequest request = lookupUserRequest.createRequest(null);
        assertEquals(Request.Method.POST, request.getMethod());
    }

    @Test
    public void testRequestHasCorrectAuthorizationHeader() throws ParamsNotSetException, AuthFailureError {
        LookupUserRequest lookupUserRequest = getExampleRequestAfterParams();
        StringRequest request = lookupUserRequest.createRequest(null);
        Map<String, String> headers = request.getHeaders();
        assertEquals("Bearer " + testAccessToken, headers.get("Authorization"));
    }

    @Test
    public void testRequestHasCorrectContentType() throws ParamsNotSetException{
        LookupUserRequest lookupUserRequest = getExampleRequestAfterParams();
        StringRequest request = lookupUserRequest.createRequest(null);
        assertEquals("application/xml", request.getBodyContentType());
    }

    @Test
    public void testRequestCannotBeCreatedBeforeParamsAdded() throws ParamsNotSetException{
        LookupUserRequest lookupUserRequest = getExampleRequestPreParams();
        exceptionRule.expect(ParamsNotSetException.class);
        exceptionRule.expectMessage("Cannot create request before params are added");
        lookupUserRequest.createRequest(null);
    }


    private LookupUserRequest getExampleRequestAfterParams(){
        LookupUserRequest request = getExampleRequestPreParams();
        request.setRequestBodyParams(testPID);
        return request;
    }


    private LookupUserRequest getExampleRequestPreParams(){
        return new LookupUserRequest(testAccessToken);
    }



}
