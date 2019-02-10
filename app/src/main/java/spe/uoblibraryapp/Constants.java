package spe.uoblibraryapp;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public final class Constants {
    private Constants(){ }


    public static final class LibraryDetails {
        public final static String institutionId = "132607";
        private LibraryDetails() { }
    }

    public static final class UserAuth {
        // Key Details for WMS NCIP and Availability
        public final static String clientId = "hNzXT2bmWYLwmWCfMDC2bAC9U1xJWBQytemHHKwzCF2YsJFnRw3isuML5E8PrK0F48OU8ENiIVzwcDWA";

        // OAuth2 flow
        public final static String redirectUrl = "uoblibrary://authenticate";
        public final static String authFailureUrl = "uoblibrary://authenticate#error";

        public final static String[] scopes = {"WMS_NCIP", "WMS_Availability", "refresh_token"};

        private UserAuth() { }

        public static String oAuthUrl() {
            String encodedRedirectUrl;
            try{
                encodedRedirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
            } catch (UnsupportedEncodingException ex){
                encodedRedirectUrl = "";
            }
            return "https://authn.sd00.worldcat.org/oauth2/authorizeCode" +
                    "?client_id=" + clientId +
                    "&authenticatingInstitutionId=" + LibraryDetails.institutionId +
                    "&contextInstitutionId=" + LibraryDetails.institutionId +
                    "&redirect_uri=" + encodedRedirectUrl +
                    "&response_type=token" +
                    "&scope=" + TextUtils.join("%20", scopes);
        }






    }

    public static final class IntentActions {
        public final static String LOOKUP_USER = "spe.uoblibraryapp.api.ncip.LOOKUP_USER";
        public final static String LOOKUP_USER_ERROR = "spe.uoblibraryapp.api.ncip.LOOKUP_USER";
        public final static String CHECKOUT_BOOK = "spe.uoblibraryapp.api.ncip.CHECKOUT_BOOK";
        public final static String ACCESS_TOKEN_GENERATED = "spe.uoblibraryapp.api.auth.ACCESS_TOKEN_GENERATED";
        public final static String ACCESS_TOKEN_NEEDED = "spe.uoblibraryapp.api.auth.ACCESS_TOKEN_NEEDED";
        public final static String AUTH_ERROR = "spe.uoblibraryapp.api.auth.AUTH_ERROR";
        public final static String AUTH_LOGOUT = "spe.uoblibraryapp.api.auth.AUTH_LOGOUT";
        public final static String USER_PROFILE_RESPONSE = "spe.uoblibraryapp.api.USER_PROFILE_RESPONSE";
        public final static String BOOK_CHECK_OUT_RESPONSE = "spe.uoblibraryapp.api.BOOK_CHECK_OUT_RESPONSE";
        private IntentActions() { }
    }


    public static final class Cache{
        // Cache Expiry
        public final static Integer cacheExpiryTime = 10; // in minutes
        private Cache() { }
    }


    public static final class APIUrls{
        public final static String lookupUser = "https://bub.share.worldcat.org/ncip/circ-patron";
        public final static String checkoutBook = "http://132.145.54.223/checkout";
        public final static String bookAvailability = "https://worldcat.org/circ/availability/sru/service?x-registryId=" + LibraryDetails.institutionId + "&query=no:ocm%s";
        private APIUrls() { }
    }


    public static final class RequestTemplates {
        public final static String lookupUser = "<NCIPMessage xmlns=\"http://www.niso.org/2008/ncip\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ncip=\"http://www.niso.org/2008/ncip\" xmlns:ns2=\"http://oclc.org/WCL/ncip/2011/extensions\" xsi:schemaLocation=\"http://www.niso.org/2008/ncip http://www.niso.org/schemas/ncip/v2_01/ncip_v2_01.xsd\" ncip:version=\"http://www.niso.org/schemas/ncip/v2_01/ncip_v2_01.xsd\">\n" +
                "<LookupUser>\n" +
                "<InitiationHeader>\n" +
                "<FromAgencyId>\n" +
                "<AgencyId ncip:Scheme=\"http://oclc.org/ncip/schemes/agencyid.scm\">" + LibraryDetails.institutionId + "</AgencyId>\n" +
                "</FromAgencyId>\n" +
                "<ToAgencyId>\n" +
                "<AgencyId ncip:Scheme=\"http://oclc.org/ncip/schemes/agencyid.scm\">"+ LibraryDetails.institutionId +"</AgencyId>\n" +
                "</ToAgencyId>\n" +
                "<ApplicationProfileType ncip:Scheme=\"http://oclc.org/ncip/schemes/application-profile/wcl.scm\">Version 2011</ApplicationProfileType>\n" +
                "</InitiationHeader>\n" +
                "<UserId>\n" +
                "<UserIdentifierValue>%s</UserIdentifierValue>\n" + // Insert userId here
                "</UserId>\n" +
                "<LoanedItemsDesired/>\n" +
                "<RequestedItemsDesired/>\n" +
                "<UserFiscalAccountDesired/>\n" +
                "<Ext>\n" +
                "<ns2:ResponseElementControl>\n" +
                "<ns2:ElementType ncip:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/elementtype.scm\">Account Details</ns2:ElementType>\n" +
                "<ns2:StartElement>1</ns2:StartElement>\n" +
                "<ns2:MaximumCount>10</ns2:MaximumCount>\n" +
                "<ns2:SortField ncip:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/accountdetailselementtype.scm\">Accrual Date</ns2:SortField>\n" +
                "<ns2:SortOrderType ncip:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/sortordertype.scm\">Ascending</ns2:SortOrderType>\n" +
                "</ns2:ResponseElementControl>\n" +
                "<ns2:ResponseElementControl>\n" +
                "<ns2:ElementType ncip:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/elementtype.scm\">Loaned Item</ns2:ElementType>\n" +
                "<ns2:StartElement>1</ns2:StartElement>\n" +
                "<ns2:MaximumCount>50</ns2:MaximumCount>\n" +
                "<ns2:SortField ncip:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/loaneditemelementtype.scm\">Date Due</ns2:SortField>\n" +
                "<ns2:SortOrderType ncip:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/sortordertype.scm\">Ascending</ns2:SortOrderType>\n" +
                "</ns2:ResponseElementControl>\n" +
                "<ns2:ResponseElementControl>\n" +
                "<ns2:ElementType ncip:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/elementtype.scm\">Requested Item</ns2:ElementType>\n" +
                "<ns2:StartElement>1</ns2:StartElement>\n" +
                "<ns2:MaximumCount>50</ns2:MaximumCount>\n" +
                "<ns2:SortField ncip:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/requesteditemelementtype.scm\">Date Placed</ns2:SortField>\n" +
                "<ns2:SortOrderType ncip:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/sortordertype.scm\">Ascending</ns2:SortOrderType>\n" +
                "</ns2:ResponseElementControl>\n" +
                "</Ext>\n" +
                "</LookupUser>\n" +
                "</NCIPMessage>";

        public final static String checkoutBook = "<CheckoutBookRequest>" +
                "<userId>%s</userId>" +
                "<accessToken>%s</accessToken>" +
                "<itemId>%s</itemId>" +
                "</CheckoutBookRequest>";

        private RequestTemplates(){ }
    }
}
