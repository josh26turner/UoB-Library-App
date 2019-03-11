package spe.uoblibraryapp;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Constants {
    private Constants(){ }


    public static final class LibraryDetails {
        public static final String institutionId = "132607";
        public static final Map<String, Integer> borrowerCategories = Stream.of(new Object[][] {
                {"Binding", 40 },
                {"Branch Transfers", 40 },
                {"Bristol Graduate", 40 },
                {"Bristol Law Society", 40 },
                {"Bristol Society of Architects", 40 },
                {"Cataloguing Dept.", 40 },
                {"Display of New Books", 40 },
                {"External Subscribers", 40 },
                {"ILL", 40 },
                {"ILL Borrower (External Libraries)", 40 },
                {"Individual Subscriber", 40 },
                {"Large Firm", 40 },
                {"Library Admin (28 days)", 40 },
                {"Library Admin (3 months)", 40 },
                {"Library Admin (364 days)", 40 },
                {"Medical Branches/Hospitals", 40 },
                {"Medics (MD)", 40 },
                {"Postgraduate Research FT", 75 },
                {"Postgraduate Research FT (EL)", 75 },
                {"Postgraduate Research PT", 75 },
                {"Postgraduate Taught FT", 40 },
                {"Postgraduate Taught FT (EL)", 40 },
                {"Postgraduate Taught PT", 40 },
                {"Recognised Local Users", 40 },
                {"Retired Staff", 75 },
                {"SCONUL Access Band A", 40 },
                {"SCONUL Access Band B", 40 },
                {"SCONUL Access Band C", 40 },
                {"SETSquared Firms", 40 },
                {"Small Firm", 40 },
                {"Staff", 75 },
                {"Student/Staff (PGT FT)", 40 },
                {"Student/Staff (PGT UG)", 40 },
                {"Student/Staff (UG FT)", 40 },
                {"Student/Staff (UG PT)", 40 },
                {"Suspended Account", 40 },
                {"Undergraduate FT", 40 },
                {"Undergraduate FT (EL)", 40 },
                {"Undergraduate PT", 40 },
                {"Visitor Non-Borrowing", 40 }
        }).collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));
        private LibraryDetails() { }
    }

    public static final class UserAuth {
        // Key Details for WMS NCIP and Availability
        public static final String clientId = "hNzXT2bmWYLwmWCfMDC2bAC9U1xJWBQytemHHKwzCF2YsJFnRw3isuML5E8PrK0F48OU8ENiIVzwcDWA";

        // OAuth2 flow
        public static final String redirectUrl = "uoblibrary://authenticate";
        public static final String authFailureUrl = "uoblibrary://authenticate#error";

        public static final String[] scopes = {"WMS_NCIP", "WMS_Availability", "refresh_token"};

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
        public static final String LOOKUP_USER = "spe.uoblibraryapp.api.ncip.LOOKUP_USER";
        public static final String LOOKUP_USER_ERROR = "spe.uoblibraryapp.api.ncip.LOOKUP_USER";
        public static final String CHECKOUT_BOOK = "spe.uoblibraryapp.api.ncip.CHECKOUT_BOOK";
        public static final String BOOK_CHECK_OUT_RESPONSE = "spe.uoblibraryapp.api.BOOK_CHECK_OUT_RESPONSE";
        public static final String CANCEL_RESERVATION = "spe.uoblibraryapp.api.ncip.CANCEL_RESERVATION";
        public static final String CANCEL_RESERVATION_RESPONSE = "spe.uoblibraryapp.api.ncip.CANCEL_RESERVATION_RESPONSE";
        public static final String CANCEL_RESERVATION_ERROR = "spe.uoblibraryapp.api.ncip.CANCEL_RESERVATION_ERROR";
        public static final String ACCESS_TOKEN_GENERATED = "spe.uoblibraryapp.api.auth.ACCESS_TOKEN_GENERATED";
        public static final String ACCESS_TOKEN_NEEDED = "spe.uoblibraryapp.api.auth.ACCESS_TOKEN_NEEDED";
        public static final String AUTH_ERROR = "spe.uoblibraryapp.api.auth.AUTH_ERROR";
        public static final String AUTH_LOGOUT = "spe.uoblibraryapp.api.auth.AUTH_LOGOUT";
        public static final String USER_PROFILE_RESPONSE = "spe.uoblibraryapp.api.USER_PROFILE_RESPONSE";
        public final static String LOOKUP_USER_ACCOUNT = "spe.uoblibraryapp.api.LOOKUP_USER_ACCOUNT";
        public final static String LOOKUP_USER_ACCOUNT_RESPONSE = "spe.uoblibraryapp.api.LOOKUP_USER_ACCOUNT_RESPONSE";
        private IntentActions() { }
    }


    public static final class Cache{
        // Cache Expiry
        public static final Integer cacheExpiryTime = 10; // in minutes
        private Cache() { }
    }


    public static final class APIUrls{
        public final static String patronProfile = "https://bub.share.worldcat.org/ncip/circ-patron";
        public final static String lookupUserAccount = "http://132.145.54.223:8080/auth/%s";
        public final static String checkoutBook = "http://132.145.54.223:8080/checkout";
        public final static String bookAvailability = "https://worldcat.org/circ/availability/sru/service?x-registryId=" + LibraryDetails.institutionId + "&query=no:ocm%s";
        private APIUrls() { }
    }


    public static final class RequestTemplates {
        public static final String lookupUser = "<NCIPMessage xmlns=\"http://www.niso.org/2008/ncip\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ncip=\"http://www.niso.org/2008/ncip\" xmlns:ns2=\"http://oclc.org/WCL/ncip/2011/extensions\" xsi:schemaLocation=\"http://www.niso.org/2008/ncip http://www.niso.org/schemas/ncip/v2_01/ncip_v2_01.xsd\" ncip:version=\"http://www.niso.org/schemas/ncip/v2_01/ncip_v2_01.xsd\">\n" +
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

        public static final String cancelReservation = "<NCIPMessage xmlns=\"http://www.niso.org/2008/ncip\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ncip=\"http://www.niso.org/2008/ncip\" xsi:schemaLocation=\"http://www.niso.org/2008/ncip http://www.niso.org/schemas/ncip/v2_01/ncip_v2_01.xsd\" ncip:version=\"http://www.niso.org/schemas/ncip/v2_01/ncip_v2_01.xsd\">\n" +
                "<CancelRequestItem>\n" +
                "<InitiationHeader>\n" +
                "<FromAgencyId>\n" +
                "<AgencyId ncip:Scheme=\"http://oclc.org/ncip/schemes/agencyid.scm\">" + LibraryDetails.institutionId + "</AgencyId>\n" +
                "</FromAgencyId>\n" +
                "<ToAgencyId>\n" +
                "<AgencyId>%s</AgencyId>\n" +
                "</ToAgencyId>\n" +
                "<ApplicationProfileType ncip:Scheme=\"http://oclc.org/ncip/schemes/application-profile/wcl.scm\">Version 2011</ApplicationProfileType>\n" +
                "</InitiationHeader>\n" +
                "<UserId>\n" +
                "<AgencyId>" + LibraryDetails.institutionId + "</AgencyId>\n" +
                "<UserIdentifierValue>%s</UserIdentifierValue>\n" +
                "</UserId>\n" +
                "<RequestId>\n" +
                "<AgencyId>" + LibraryDetails.institutionId + "</AgencyId>\n" +
                "<RequestIdentifierValue>%s</RequestIdentifierValue>\n" +
                "</RequestId>\n" +
                "<RequestType ncip:Scheme=\"http://www.niso.org/ncip/v1_0/imp1/schemes/requesttype/requesttype.scm\">Hold</RequestType>\n" +
                "<RequestScopeType ncip:Scheme=\"http://www.niso.org/ncip/v1_0/imp1/schemes/requestscopetype/requestscopetype.scm\">Bibliographic Item</RequestScopeType>\n" +
                "</CancelRequestItem>\n" +
                "</NCIPMessage>";

        public static final String checkoutBook = "<CheckoutBookRequest>" +
                "<userId>%s</userId>" +
                "<accessToken>%s</accessToken>" +
                "<itemId>%s</itemId>" +
                "</CheckoutBookRequest>";

        private RequestTemplates(){ }
    }
}
