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

Requests are made by a GET method to the `/user/{userID}` path. Where `{userID}` is the user ID being looked up. Along with this, the user's access token is sent in the Authorization HTTP Header. First the access token is checked. An OAuth token is then generated using an HMAC signature, then the token is used to make a patron profile request. The response from this request is then returned to the application.  

## Keys  

There are 2 different types of key pairs used in this application. These are stored in the `Keys.java` file. There are 2 sets of methods and variables deploying the keys here. Each key type has a `get` method, an index and an array of arrays that store the different key pairs. The arrays should be in the form `{{secret0, public0}, {secret1, public1}, ...}`. Currently only one request can be completed per key pair per second. This method allows for multiple key pairs to allow for multiple transactions per second. The two different key types are the keys for checkout requests and the keys for OAuth. The only thing that needs changing in this file will be the key pairs, removing the current ones and adding new ones, everything else should work if they added in the correct form.  

## Library Data  

Most of the university specific data is stored in the `UoBLibrary.java` file, should it need updating.  

