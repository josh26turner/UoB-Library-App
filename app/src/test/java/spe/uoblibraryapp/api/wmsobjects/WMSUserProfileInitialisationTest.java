package spe.uoblibraryapp.api.wmsobjects;

import org.junit.Test;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import spe.uoblibraryapp.api.ncip.WMSNCIPElement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class WMSUserProfileInitialisationTest extends WMSUserProfileTests{

    /**
     * A test to ensure that the userId is set correctly.
     */
    @Test
    public void testEmptyUserProfileUserIdSetCorrectly(){
        WMSUserProfile userProfile = createBlankUserProfile();
        assertEquals(
                "The userId does not match the expected value",
                "998c3f4b-765e-48b2-asdf-8bd2cf8acee9",
                userProfile.getUserId()
        );
    }

    @Test
    public void testEmptyUserProfileHasNoLoans() {
        WMSUserProfile userProfile = createBlankUserProfile();
        assertEquals("Loans are not empty for a blank UserProfile", 0, userProfile.getLoans().size());
    }

    @Test
    public void testEmptyUserProfileHasNoFines() {
        WMSUserProfile userProfile = createBlankUserProfile();
        assertEquals("Fines are not empty for a blank UserProfile", 0, userProfile.getFines().size());
    }

    @Test
    public void testEmptyUserProfileHasNoHolds() {
        WMSUserProfile userProfile = createBlankUserProfile();
        assertEquals("Holdss are not empty for a blank UserProfile", 0, userProfile.getOnHold().size());
    }








    // TODO: create tests to ensure that the blank UserProfile contains nothing in loans, fines etc.

    /**
     * This creates a blank user profile, that contains no fines, loans or requests.
     * @return WMSUserProfile, that is blank.
     */
    private WMSUserProfile createBlankUserProfile(){
        Node node = null;
        try {
            node = createUserNode(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
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
                            "</ns1:NCIPMessage>\n"
            );
        } catch (SAXException | IOException | ParserConfigurationException ex){
            fail(ex.getMessage());
        }

        WMSNCIPServices services = getServices();

        WMSUserProfile userProfile = null;
        try {
            userProfile = new WMSUserProfile(
                    new WMSNCIPElement(node),
                    services.patronService,
                    services.staffService
            );
        } catch (WMSParseException ex){
            fail(ex.getMessage());
        }

        return userProfile;
    }
}
