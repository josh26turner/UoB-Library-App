package spe.uoblibraryapp.api.wmsobjects;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WMSUserProfileInitialisationTest extends WMSUserProfileTests{

    // Tests for empty user profile

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

    /**
     * A test to ensure that an empty user profile has no loans.
     */
    @Test
    public void testEmptyUserProfileHasNoLoans() {
        WMSUserProfile userProfile = createBlankUserProfile();
        assertEquals( "Loans are not empty for a blank UserProfile",0, userProfile.getLoans().size());
    }

    /**
     * A test to ensure that an empty user profile has no fines.
     */
    @Test
    public void testEmptyUserProfileHasNoFines() {
        WMSUserProfile userProfile = createBlankUserProfile();
        assertEquals("Fines are not empty for a blank UserProfile",0, userProfile.getFines().size());
    }

    /**
     * A test to ensure that an empty user profile has no holds.
     */
    @Test
    public void testEmptyUserProfileHasNoHolds() {
        WMSUserProfile userProfile = createBlankUserProfile();
        assertEquals("Holds are not empty for a blank UserProfile",0, userProfile.getOnHold().size());
    }


    // Tests for user profile with 1 loan, no fines and no holds

    /**
     * A test to ensure that the userId is set correctly.
     */
    @Test
    public void test1LoanNoFinesNoHoldsUserProfileUserIdSetCorrectly(){
        WMSUserProfile userProfile = createUserProfile1LoanNoFinesNoHolds();
        assertEquals(
                "The userId does not match the expected value",
                "998c3f4b-765e-48b2-asdf-8bd2cf8acee9",
                userProfile.getUserId()
        );
    }

    /**
     * A test to ensure that the user profile has 1 loans.
     */
    @Test
    public void test1LoanNoFinesNoHoldsUserProfileHas1Loan() {
        WMSUserProfile userProfile = createUserProfile1LoanNoFinesNoHolds();
        assertEquals("Number of loans do not match",1, userProfile.getLoans().size());
    }

    /**
     * A test to ensure that the user profile has no fines.
     */
    @Test
    public void test1LoanNoFinesNoHoldsUserProfileHasNoFines() {
        WMSUserProfile userProfile = createUserProfile1LoanNoFinesNoHolds();
        assertEquals("Fines are not empty for a blank UserProfile",0, userProfile.getFines().size());
    }

    /**
     * A test to ensure that the user profile has no holds.
     */
    @Test
    public void test1LoanNoFinesNoHoldsUserProfileHasNoHolds() {
        WMSUserProfile userProfile = createUserProfile1LoanNoFinesNoHolds();
        assertEquals("Holds are not empty for a blank UserProfile",0, userProfile.getOnHold().size());
    }


    // Tests for user profile with 1 loan, no fines and no holds

    /**
     * A test to ensure that the userId is set correctly.
     */
    @Test
    public void test2LoansNoFines1HoldUserProfileUserIdSetCorrectly(){
        WMSUserProfile userProfile = createUserProfile2LoansNoFines1Hold();
        assertEquals(
                "The userId does not match the expected value",
                "998c3f4b-765e-48b2-asdf-8bd2cf8acee9",
                userProfile.getUserId()
        );
    }

    /**
     * A test to ensure that the user profile has 1 loans.
     */
    @Test
    public void test2LoansNoFines1HoldUserProfileHas2Loans() {
        WMSUserProfile userProfile = createUserProfile2LoansNoFines1Hold();
        assertEquals("Number of loans do not match",2, userProfile.getLoans().size());
    }

    /**
     * A test to ensure that the user profile has no fines.
     */
    @Test
    public void test2LoansNoFines1HoldUserProfileHasNoFines() {
        WMSUserProfile userProfile = createUserProfile2LoansNoFines1Hold();
        assertEquals("Fines are not empty for a blank UserProfile",0, userProfile.getFines().size());
    }

    /**
     * A test to ensure that the user profile has no holds.
     */
    @Test
    public void test2LoansNoFines1HoldUserProfileHas1Hold() {
        WMSUserProfile userProfile = createUserProfile2LoansNoFines1Hold();
        assertEquals( "Number of holds do not match",1, userProfile.getOnHold().size());
    }



    // TODO: Add tests for Fines when we know how to parse them.






    // Methods to create test WMSUserProfiles




    private WMSUserProfile createUserProfile1LoanNoFinesNoHolds(){
        return createUserProfile("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<ns1:NCIPMessage xmlns:ns1=\"http://www.niso.org/2008/ncip\" xmlns=\"http://oclc.org/WCL/ncip/2011/extensions\" xmlns:ns3=\"http://www.oclc.org/ncip/usernote/2012\">\n" +
                "    <ns1:LookupUserResponse>\n" +
                "        <ns1:UserId>\n" +
                "            <ns1:UserIdentifierType>EIDM</ns1:UserIdentifierType>\n" +
                "            <ns1:UserIdentifierValue>998c3f4b-765e-48b2-asdf-8bd2cf8acee9</ns1:UserIdentifierValue>\n" +
                "        </ns1:UserId>\n" +
                "        <ns1:LoanedItemsCount>\n" +
                "            <ns1:CirculationStatus ns1:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/circulationstatus.scm\">On Loan</ns1:CirculationStatus>\n" +
                "            <ns1:LoanedItemCountValue>1</ns1:LoanedItemCountValue>\n" +
                "        </ns1:LoanedItemsCount>\n" +
                "        <ns1:LoanedItem>\n" +
                "            <ns1:ItemId>\n" +
                "                <ns1:AgencyId ns1:Scheme=\"http://oclc.org/ncip/schemes/agencyid.scm\">128807</ns1:AgencyId>\n" +
                "                <ns1:ItemIdentifierValue>eec21bc3-6af3-4b9f-ac1c-1066c95e7734</ns1:ItemIdentifierValue>\n" +
                "            </ns1:ItemId>\n" +
                "            <ns1:ReminderLevel>0</ns1:ReminderLevel>\n" +
                "            <ns1:DateDue>2014-03-22T03:59:59Z</ns1:DateDue>\n" +
                "            <ns1:Amount>\n" +
                "                <ns1:CurrencyCode ns1:Scheme=\"http://www.bsi-global.com/Technical+Information/Publications/_Publications/tig90x.doc\">USD</ns1:CurrencyCode>\n" +
                "                <ns1:MonetaryValue>0</ns1:MonetaryValue>\n" +
                "            </ns1:Amount>\n" +
                "            <ns1:MediumType ns1:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/mediumtype.scm\">Book</ns1:MediumType>\n" +
                "            <ns1:Ext>\n" +
                "                <ns1:RenewalCount>1</ns1:RenewalCount>\n" +
                "                <ns1:DateCheckedOut>2014-02-21T16:20:53Z</ns1:DateCheckedOut>\n" +
                "                <ns1:BibliographicDescription>\n" +
                "                    <ns1:Author>Shanske, Darien,</ns1:Author>\n" +
                "                    <ns1:BibliographicRecordId>\n" +
                "                        <ns1:BibliographicRecordIdentifier>62789926</ns1:BibliographicRecordIdentifier>\n" +
                "                        <ns1:BibliographicRecordIdentifierCode ns1:Scheme=\"http://www.niso.org/ncip/v1_0/imp1/schemes/bibliographicrecordidentifiercode/bibliographicrecordidentifiercode.scm\">OCLC</ns1:BibliographicRecordIdentifierCode>\n" +
                "                    </ns1:BibliographicRecordId>\n" +
                "                    <ns1:PublicationDate>2007</ns1:PublicationDate>\n" +
                "                    <ns1:Publisher>New York : Cambridge University Press,</ns1:Publisher>\n" +
                "                    <ns1:Title>Thucydides and the philosophical origins of history /</ns1:Title>\n" +
                "                    <ns1:Language ns1:Scheme=\"http://lcweb.loc.gov/standards/iso639-2/bibcodes.html\">eng</ns1:Language>\n" +
                "                </ns1:BibliographicDescription>\n" +
                "            </ns1:Ext>\n" +
                "        </ns1:LoanedItem>\n" +
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


    private WMSUserProfile createUserProfile2LoansNoFines1Hold(){
        return createUserProfile("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<ns1:NCIPMessage xmlns:ns1=\"http://www.niso.org/2008/ncip\" xmlns=\"http://oclc.org/WCL/ncip/2011/extensions\" xmlns:ns3=\"http://www.oclc.org/ncip/usernote/2012\">\n" +
                "    <ns1:LookupUserResponse>\n" +
                "        <ns1:UserId>\n" +
                "            <ns1:UserIdentifierType>EIDM</ns1:UserIdentifierType>\n" +
                "            <ns1:UserIdentifierValue>998c3f4b-765e-48b2-asdf-8bd2cf8acee9</ns1:UserIdentifierValue>\n" +
                "        </ns1:UserId>\n" +
                "        <ns1:LoanedItemsCount>\n" +
                "            <ns1:CirculationStatus ns1:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/circulationstatus.scm\">On Loan</ns1:CirculationStatus>\n" +
                "            <ns1:LoanedItemCountValue>1</ns1:LoanedItemCountValue>\n" +
                "        </ns1:LoanedItemsCount>\n" +
                "        <ns1:LoanedItem>\n" +
                "            <ns1:ItemId>\n" +
                "                <ns1:AgencyId ns1:Scheme=\"http://oclc.org/ncip/schemes/agencyid.scm\">128807</ns1:AgencyId>\n" +
                "                <ns1:ItemIdentifierValue>eec21bc3-6af3-4b9f-ac1c-1066c95e7734</ns1:ItemIdentifierValue>\n" +
                "            </ns1:ItemId>\n" +
                "            <ns1:ReminderLevel>0</ns1:ReminderLevel>\n" +
                "            <ns1:DateDue>2014-03-22T03:59:59Z</ns1:DateDue>\n" +
                "            <ns1:Amount>\n" +
                "                <ns1:CurrencyCode ns1:Scheme=\"http://www.bsi-global.com/Technical+Information/Publications/_Publications/tig90x.doc\">USD</ns1:CurrencyCode>\n" +
                "                <ns1:MonetaryValue>0</ns1:MonetaryValue>\n" +
                "            </ns1:Amount>\n" +
                "            <ns1:MediumType ns1:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/mediumtype.scm\">Book</ns1:MediumType>\n" +
                "            <ns1:Ext>\n" +
                "                <ns1:RenewalCount>1</ns1:RenewalCount>\n" +
                "                <ns1:DateCheckedOut>2014-02-21T16:20:53Z</ns1:DateCheckedOut>\n" +
                "                <ns1:BibliographicDescription>\n" +
                "                    <ns1:Author>Shanske, Darien,</ns1:Author>\n" +
                "                    <ns1:BibliographicRecordId>\n" +
                "                        <ns1:BibliographicRecordIdentifier>62789926</ns1:BibliographicRecordIdentifier>\n" +
                "                        <ns1:BibliographicRecordIdentifierCode ns1:Scheme=\"http://www.niso.org/ncip/v1_0/imp1/schemes/bibliographicrecordidentifiercode/bibliographicrecordidentifiercode.scm\">OCLC</ns1:BibliographicRecordIdentifierCode>\n" +
                "                    </ns1:BibliographicRecordId>\n" +
                "                    <ns1:PublicationDate>2007</ns1:PublicationDate>\n" +
                "                    <ns1:Publisher>New York : Cambridge University Press,</ns1:Publisher>\n" +
                "                    <ns1:Title>Thucydides and the philosophical origins of history /</ns1:Title>\n" +
                "                    <ns1:Language ns1:Scheme=\"http://lcweb.loc.gov/standards/iso639-2/bibcodes.html\">eng</ns1:Language>\n" +
                "                </ns1:BibliographicDescription>\n" +
                "            </ns1:Ext>\n" +
                "        </ns1:LoanedItem>\n" +
                "        <ns1:LoanedItem>\n" +
                "            <ns1:ItemId>\n" +
                "                <ns1:AgencyId ns1:Scheme=\"http://oclc.org/ncip/schemes/agencyid.scm\">128807</ns1:AgencyId>\n" +
                "                <ns1:ItemIdentifierValue>eec21bc3-6af3-4b9f-ac1c-1066c95e7734</ns1:ItemIdentifierValue>\n" +
                "            </ns1:ItemId>\n" +
                "            <ns1:ReminderLevel>0</ns1:ReminderLevel>\n" +
                "            <ns1:DateDue>2014-03-22T03:59:59Z</ns1:DateDue>\n" +
                "            <ns1:Amount>\n" +
                "                <ns1:CurrencyCode ns1:Scheme=\"http://www.bsi-global.com/Technical+Information/Publications/_Publications/tig90x.doc\">USD</ns1:CurrencyCode>\n" +
                "                <ns1:MonetaryValue>0</ns1:MonetaryValue>\n" +
                "            </ns1:Amount>\n" +
                "            <ns1:MediumType ns1:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/mediumtype.scm\">Book</ns1:MediumType>\n" +
                "            <ns1:Ext>\n" +
                "                <ns1:RenewalCount>1</ns1:RenewalCount>\n" +
                "                <ns1:DateCheckedOut>2014-02-21T16:20:53Z</ns1:DateCheckedOut>\n" +
                "                <ns1:BibliographicDescription>\n" +
                "                    <ns1:Author>Shanske, Darien,</ns1:Author>\n" +
                "                    <ns1:BibliographicRecordId>\n" +
                "                        <ns1:BibliographicRecordIdentifier>62789926</ns1:BibliographicRecordIdentifier>\n" +
                "                        <ns1:BibliographicRecordIdentifierCode ns1:Scheme=\"http://www.niso.org/ncip/v1_0/imp1/schemes/bibliographicrecordidentifiercode/bibliographicrecordidentifiercode.scm\">OCLC</ns1:BibliographicRecordIdentifierCode>\n" +
                "                    </ns1:BibliographicRecordId>\n" +
                "                    <ns1:PublicationDate>2007</ns1:PublicationDate>\n" +
                "                    <ns1:Publisher>New York : Cambridge University Press,</ns1:Publisher>\n" +
                "                    <ns1:Title>Thucydides and the philosophical origins of history /</ns1:Title>\n" +
                "                    <ns1:Language ns1:Scheme=\"http://lcweb.loc.gov/standards/iso639-2/bibcodes.html\">eng</ns1:Language>\n" +
                "                </ns1:BibliographicDescription>\n" +
                "            </ns1:Ext>\n" +
                "        </ns1:LoanedItem>\n" +
                "        <ns1:RequestedItemsCount>\n" +
                "            <ns1:CirculationStatus ns1:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/circulationstatus.scm\">Recently Received</ns1:CirculationStatus>\n" +
                "            <ns1:RequestedItemCountValue>0</ns1:RequestedItemCountValue>\n" +
                "        </ns1:RequestedItemsCount>\n" +
                "        <ns1:RequestedItemsCount>\n" +
                "            <ns1:CirculationStatus ns1:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/circulationstatus.scm\">On Hold</ns1:CirculationStatus>\n" +
                "            <ns1:RequestedItemCountValue>0</ns1:RequestedItemCountValue>\n" +
                "        </ns1:RequestedItemsCount>\n" +
                "        <ns1:RequestedItem>\n" +
                "            <ns1:RequestId>\n" +
                "                <ns1:AgencyId ns1:Scheme=\"http://oclc.org/ncip/schemes/agencyid.scm\">128807</ns1:AgencyId>\n" +
                "                <ns1:RequestIdentifierValue>be5aa3d8-e143-414f-a1af-5a6c88dc3b5d</ns1:RequestIdentifierValue>\n" +
                "            </ns1:RequestId>\n" +
                "            <ns1:RequestType ns1:Scheme=\"http://www.niso.org/ncip/v1_0/imp1/schemes/requesttype/requesttype.scm\">Hold</ns1:RequestType>\n" +
                "            <ns1:RequestStatusType ns1:Scheme=\"http://www.niso.org/ncip/v1_0/imp1/schemes/requeststatustype/requeststatustype.scm\">In Process</ns1:RequestStatusType>\n" +
                "            <ns1:DatePlaced>2014-02-21T16:48:14Z</ns1:DatePlaced>\n" +
                "            <ns1:PickupLocation>129055</ns1:PickupLocation>\n" +
                "            <ns1:HoldQueuePosition>1</ns1:HoldQueuePosition>\n" +
                "            <ns1:Title>Diary of a wimpy kid : dog days /</ns1:Title>\n" +
                "            <ns1:MediumType ns1:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/mediumtype.scm\">Book</ns1:MediumType>\n" +
                "            <ns1:Ext>\n" +
                "                <ns1:BibliographicDescription>\n" +
                "                    <ns1:Author>Kinney, Jeff.</ns1:Author>\n" +
                "                    <ns1:BibliographicRecordId>\n" +
                "                        <ns1:BibliographicRecordIdentifier>317923541</ns1:BibliographicRecordIdentifier>\n" +
                "                        <ns1:BibliographicRecordIdentifierCode ns1:Scheme=\"http://www.niso.org/ncip/v1_0/imp1/schemes/bibliographicrecordidentifiercode/bibliographicrecordidentifiercode.scm\">OCLC</ns1:BibliographicRecordIdentifierCode>\n" +
                "                    </ns1:BibliographicRecordId>\n" +
                "                    <ns1:PublicationDate>2009</ns1:PublicationDate>\n" +
                "                    <ns1:Publisher>New York : Amulet Books,</ns1:Publisher>\n" +
                "                    <ns1:Title>Diary of a wimpy kid : dog days /</ns1:Title>\n" +
                "                    <ns1:Language ns1:Scheme=\"http://lcweb.loc.gov/standards/iso639-2/bibcodes.html\">eng</ns1:Language>\n" +
                "                </ns1:BibliographicDescription>\n" +
                "                <ns1:EarliestDateNeeded>2014-02-21T16:48:40.230Z</ns1:EarliestDateNeeded>\n" +
                "                <ns1:HoldQueueLength>1</ns1:HoldQueueLength>\n" +
                "                <ns1:NeedBeforeDate>2034-02-21T16:48:40.230Z</ns1:NeedBeforeDate>\n" +
                "            </ns1:Ext>\n" +
                "        </ns1:RequestedItem>\n" +
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

    /**
     * This creates a blank user profile, that contains no fines, loans or requests.
     * @return WMSUserProfile, that is blank.
     */
    private WMSUserProfile createBlankUserProfile(){
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
