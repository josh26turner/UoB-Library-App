package spe.uoblibraryapp.api.ncip;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import spe.uoblibraryapp.Constants;

import static org.junit.Assert.assertEquals;


public class CheckoutBookRequestTests {

    private String testPID = "abcdef-12345678-90ghi-jkl";
    private String testUserBarcode = "barcode";
    private String testBookBarcode = "123456789";
    private String testBranchId = "159911";
    private String testAccessToken = "tk_1234567890";

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();


    @Test
    public void testRequestBodyIsCorrectOnInitialisation(){
        CheckoutBookRequest checkoutBookRequest = getExampleRequestPreParams();
        assertEquals(Constants.RequestTemplates.checkoutBook, checkoutBookRequest.requestBody);
    }


    @Test
    public void testRequestBodyIsCorrectAfterParams(){
        CheckoutBookRequest checkoutBookRequest = getExampleRequestAfterParams();
        assertEquals(String.format(
                Constants.RequestTemplates.checkoutBook,
                testPID,
                testUserBarcode,
                testAccessToken,
                testBookBarcode,
                testBranchId), checkoutBookRequest.requestBody);
    }


    @Test
    public void testRequestHasCorrectURL() throws ParamsNotSetException{
        CheckoutBookRequest checkoutBookRequest = getExampleRequestAfterParams();
        StringRequest request = checkoutBookRequest.createRequest(null);
        assertEquals(Constants.APIUrls.checkoutBook, request.getUrl());
    }


    @Test
    public void testRequestHasCorrectMethod() throws ParamsNotSetException{
        CheckoutBookRequest checkoutBookRequest = getExampleRequestAfterParams();
        StringRequest request = checkoutBookRequest.createRequest(null);
        assertEquals(Request.Method.POST, request.getMethod());
    }


    @Test
    public void testRequestHasCorrectContentType() throws ParamsNotSetException{
        CheckoutBookRequest checkoutBookRequest = getExampleRequestAfterParams();
        StringRequest request = checkoutBookRequest.createRequest(null);
        assertEquals("application/xml", request.getBodyContentType());
    }


    @Test
    public void testRequestCannotBeCreatedBeforeParamsAdded() throws ParamsNotSetException{
        CheckoutBookRequest checkoutBookRequest = getExampleRequestPreParams();
        exceptionRule.expect(ParamsNotSetException.class);
        exceptionRule.expectMessage("Cannot create request before params are added");
        checkoutBookRequest.createRequest(null);
    }


    private CheckoutBookRequest getExampleRequestAfterParams(){
        CheckoutBookRequest request = getExampleRequestPreParams();
        request.setRequestBodyParams(
                testPID,
                testUserBarcode,
                testAccessToken,
                testBookBarcode,
                testBranchId);
        return request;
    }


    private CheckoutBookRequest getExampleRequestPreParams(){
        return new CheckoutBookRequest();
    }
}
