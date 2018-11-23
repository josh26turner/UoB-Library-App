package spe.uoblibraryapp.api.wmsobjects;


import org.junit.jupiter.api.Test;

import spe.uoblibraryapp.api.WMSException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;


class WMSCheckoutTests extends WMSUserProfileTests {

    @Test
    void testDoubleAcceptThrows(){
        WMSCheckout checkout = createCheckout();
        try {
            checkout.accept();
        } catch (WMSException e){
            fail("Should not fail after first accept");
        }
    }


    @Test
    void testDoubleRejectThrows(){

    }

    @Test
    void testRejectThenAcceptThrows(){

    }

    @Test
    void testAcceptThenRejectThrows(){

    }



    // Helper methods to create WMSCheckouts.



    private WMSCheckout createCheckout(){
        return new WMSCheckout("123456789", getUserProfile(), getServices().staffService);
    }


    private WMSUserProfile getUserProfile(){
        return createUserProfile("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<ns1:NCIPMessage xmlns:ns1=\"http://www.niso.org/2008/ncip\" xmlns=\"http://oclc.org/WCL/ncip/2011/extensions\" xmlns:ns3=\"http://www.oclc.org/ncip/usernote/2012\">\n" +
                "    <ns1:LookupUserResponse>\n" +
                "        <ns1:UserId>\n" +
                "            <ns1:UserIdentifierType>EIDM</ns1:UserIdentifierType>\n" +
                "            <ns1:UserIdentifierValue>998c3f4b-765e-48b2-asdf-8bd2cf8acee9</ns1:UserIdentifierValue>\n" +
                "        </ns1:UserId>\n" +
                "        <ns1:LoanedItemsCount>\n" +
                "            <ns1:CirculationStatus ns1:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/circulationstatus.scm\">On Loan</ns1:CirculationStatus>\n" +
                "            <ns1:LoanedItemCountValue>0</ns1:LoanedItemCountValue>\n" +
                "        </ns1:LoanedItemsCount>\n" +
                "        <ns1:RequestedItemsCount>\n" +
                "            <ns1:CirculationStatus ns1:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/circulationstatus.scm\">Recently Received</ns1:CirculationStatus>\n" +
                "            <ns1:RequestedItemCountValue>0</ns1:RequestedItemCountValue>\n" +
                "        </ns1:RequestedItemsCount>\n" +
                "        <ns1:RequestedItemsCount>\n" +
                "            <ns1:CirculationStatus ns1:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/circulationstatus.scm\">On Hold</ns1:CirculationStatus>\n" +
                "            <ns1:RequestedItemCountValue>0</ns1:RequestedItemCountValue>\n" +
                "        </ns1:RequestedItemsCount>\n" +
                "        <ns1:Ext>\n" +
                "            <UserFiscalAccountSummary>\n" +
                "                <ChargesCount>0</ChargesCount>\n" +
                "                <ns1:AccountBalance>\n" +
                "                    <ns1:CurrencyCode ns1:Scheme=\"http://www.bsi-global.com/Technical+Information/Publications/_Publications/tig90x.doc\">USD</ns1:CurrencyCode>\n" +
                "                    <ns1:MonetaryValue>0</ns1:MonetaryValue>\n" +
                "                </ns1:AccountBalance>\n" +
                "            </UserFiscalAccountSummary>\n" +
                "            <SubsequentElementControl>\n" +
                "                <ElementType ns1:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/elementtype.scm\">Requested Item</ElementType>\n" +
                "                <NextElement>1</NextElement>\n" +
                "            </SubsequentElementControl>\n" +
                "        </ns1:Ext>\n" +
                "    </ns1:LookupUserResponse>\n" +
                "</ns1:NCIPMessage>\n");
    }
}
