# Server Maintenance Guide

The server is a Spring Boot Java HTTP server designed to be an API for the mobile application. It entails two main functions: checking out books and getting user profile information - such as if the account is blocked. In a sense it can be thought of as a librarian checking out the books on the library user's behalf, giving the check out proper authentication to the requests. The server also makes requests to get information on the user profile as the default information returned is not sufficient for our requirements. 

## Checkout Requests:  

[WMS API Documentation](https://www.oclc.org/developer/develop/web-services/wms-ncip-service/staff-profile.en.html)  

Here you can find the documentation for the API we use to make check out requests.  

Checkout requests are made to the path `/checkout` as a POST request. The body of the post request is XML, of the form:

```XML
<CheckoutBookRequest>
    <userId>{userID}</userId>
    <barcode>{user-barcode}</barcode>
    <accessToken>{accessToken}</accessToken>
    <itemId>{itemID}</itemId>
    <location>{location}</location>
</CheckoutBookRequest>
```

The XML is parsed into a `CheckOutRequest` object by the classes in the `xml` package. The user is authorised at this point, checking that the access token is valid. Then the request is authorised by forming the a HMAC signature. Then the checkout request is made. The response from the WMS is then directly returned to application. 

## User Auth

[WMS API Documentation](https://www.oclc.org/developer/develop/web-services/wms-ncip-service/patron-profile.en.html)  

Here you can find the documentation for the API we use to get the user information.  

Requests are made by a GET method to the `/user/{userID}` path. Where `{userID}` is the user ID being looked up. Along with this, the user's access token is sent in the Authorization HTTP Header. 