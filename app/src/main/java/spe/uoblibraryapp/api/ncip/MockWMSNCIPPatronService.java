package spe.uoblibraryapp.api.ncip;

import spe.uoblibraryapp.api.WMSResponse;

class MockWMSNCIPPatronService implements WMSNCIPPatronService {

    @Override
    public WMSResponse lookup_user(String user_id){
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<ns1:NCIPMessage xmlns:ns1=\"http://www.niso.org/2008/ncip\" xmlns=\"http://oclc.org/WCL/ncip/2011/extensions\" xmlns:ns3=\"http://www.oclc.org/ncip/usernote/2012\">\n" +
                "    <ns1:LookupUserResponse>\n" +
                "        <ns1:UserId>\n" +
                "            <ns1:UserIdentifierType>EIDM</ns1:UserIdentifierType>\n" +
                "            <ns1:UserIdentifierValue>" + user_id + "</ns1:UserIdentifierValue>\n" +
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
                "            <ns1:RequestedItemCountValue>1</ns1:RequestedItemCountValue>\n" +
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
                "</ns1:NCIPMessage>\n";
        return create_response(xml);
    }

    @Override
    public WMSResponse renew_item(String book_id) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<ns1:NCIPMessage xmlns:ns1=\"http://www.niso.org/2008/ncip\" xmlns=\"http://oclc.org/WCL/ncip/2011/extensions\" xmlns:ns3=\"http://www.oclc.org/ncip/usernote/2012\">\n"
                + "<ns1:RenewItemResponse>\n"
                + "<ns1:ItemId>\n"
                + "<ns1:ItemIdentifierValue>eec21bc3-6af3-4b9f-ac1c-1066c95e7734</ns1:ItemIdentifierValue>\n"
                + "</ns1:ItemId>" + "<ns1:DateDue>2014-03-22T03:59:59Z</ns1:DateDue>\n"
                + "<ns1:RenewalCount>1</ns1:RenewalCount>" + "</ns1:RenewItemResponse>" + "</ns1:NCIPMessage>\n";
        return create_response(xml);
    }

    @Override
    public WMSResponse renew_all() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                    "<ns2:NCIPMessage xmlns=\"http://oclc.org/WCL/ncip/2011/extensions\"\n" +
                        "xmlns:ns2=\"http://www.niso.org/2008/ncip\" xmlns:ns3=\"http://www.oclc.org/ncip/renewall/2014\"\n" +
                        "xmlns:ns4=\"http://www.oclc.org/ncip/usernote/2012\"\n" +
                        "ns2:version=\"http://www.niso.org/schemas/ncip/v2_0/imp1/xsd/ncip_v2_01.xsd\">\n" +
                        "<ns2:Ext>\n" +
                            "<ns3:RenewAllItemResponse>\n" +
                                "<ns2:UserId>\n" +
                                    "<ns2:AgencyId ns2:Scheme=\"http://oclc.org/ncip/schemes/agencyid/institutionid.scm\"\n" +
                                        ">128807</ns2:AgencyId>\n" +
                                    "<ns2:UserIdentifierValue>26484a07-89z4-44y6-81b1-3147i629995m</ns2:UserIdentifierValue>\n" +
                                "</ns2:UserId>\n" +
                                "<ns3:RenewInformation>\n" +
                                    "<ns2:ItemId>\n" +
                                        "<ns2:AgencyId ns2:Scheme=\"http://oclc.org/ncip/schemes/agencyid.scm\"\n" +
                                            ">128807</ns2:AgencyId>\n" +
                                        "<ns2:ItemIdentifierValue>684b2138-8b7a-473a-bc46-175990a1092c</ns2:ItemIdentifierValue>\n" +
                                    "</ns2:ItemId>\n" +
                                    "<ns2:DateDue>2015-01-20T04:59:59Z</ns2:DateDue>\n" +
                                    "<ns2:RenewalCount>7</ns2:RenewalCount>\n" +
                                "</ns3:RenewInformation>\n" +
                                "<ns3:RenewInformation>\n" +
                                    "<ns2:ItemId>\n" +
                                        "<ns2:AgencyId ns2:Scheme=\"http://oclc.org/ncip/schemes/agencyid.scm\"\n" +
                                            ">128807</ns2:AgencyId>\n" +
                                        "<ns2:ItemIdentifierValue>6bb56325-1f14-4dc4-8561-a8fa35d52853</ns2:ItemIdentifierValue>\n" +
                                    "</ns2:ItemId>\n" +
                                    "<ns2:DateDue>2015-01-20T04:59:59Z</ns2:DateDue>\n" +
                                    "<ns2:RenewalCount>7</ns2:RenewalCount>\n" +
                                "</ns3:RenewInformation>\n" +
                                "<ns3:RenewInformation>\n" +
                                    "<ns2:ItemId>\n" +
                                        "<ns2:AgencyId ns2:Scheme=\"http://oclc.org/ncip/schemes/agencyid.scm\"\n" +
                                            ">128807</ns2:AgencyId>\n" +
                                        "<ns2:ItemIdentifierValue>17825536-0b23-4838-b1da-1eb990ce71bc</ns2:ItemIdentifierValue>\n" +
                                    "</ns2:ItemId>\n" +
                                    "<ns2:DateDue>2015-01-20T04:59:59Z</ns2:DateDue>\n" +
                                    "<ns2:RenewalCount>5</ns2:RenewalCount>\n" +
                                "</ns3:RenewInformation>\n" +
                                "<ns3:RenewInformation>\n" +
                                    "<ns2:ItemId>\n" +
                                        "<ns2:AgencyId ns2:Scheme=\"http://oclc.org/ncip/schemes/agencyid.scm\"\n" +
                                            ">128807</ns2:AgencyId>\n" +
                                        "<ns2:ItemIdentifierValue>62c4902b-f583-491d-923e-00eca8a8b54b</ns2:ItemIdentifierValue>\n" +
                                    "</ns2:ItemId>\n" +
                                    "<ns2:DateDue>2015-01-20T04:59:59Z</ns2:DateDue>\n" +
                                    "<ns2:RenewalCount>8</ns2:RenewalCount>\n" +
                                "</ns3:RenewInformation>\n" +
                                "<ns3:RenewInformation>\n" +
                                    "<ns2:ItemId>\n" +
                                        "<ns2:AgencyId ns2:Scheme=\"http://oclc.org/ncip/schemes/agencyid.scm\"\n" +
                                            ">128807</ns2:AgencyId>\n" +
                                        "<ns2:ItemIdentifierValue>aff043b3-405b-4e18-975b-9f08b96686a7</ns2:ItemIdentifierValue>\n" +
                                    "</ns2:ItemId>\n" +
                                    "<ns2:DateDue>2015-02-12T04:59:59Z</ns2:DateDue>\n" +
                                    "<ns2:RenewalCount>1</ns2:RenewalCount>\n" +
                                "</ns3:RenewInformation>\n" +
                            "</ns3:RenewAllItemResponse>\n" +
                        "</ns2:Ext>\n" +
                    "</ns2:NCIPMessage>\n";
        return create_response(xml);
    }

    @Override
    public WMSResponse request_item() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<ns1:NCIPMessage xmlns:ns1=\"http://www.niso.org/2008/ncip\" xmlns=\"http://oclc.org/WCL/ncip/2011/extensions\" xmlns:ns3=\"http://www.oclc.org/ncip/usernote/2012\">\n" +
                "<ns1:RequestItemResponse>\n" +
                        "<ns1:RequestId>" +
                            "<ns1:AgencyId>128807</ns1:AgencyId>\n" +
                            "<ns1:RequestIdentifierValue>bc219183-b3a8-4d5b-878d-797a8dff95ec</ns1:RequestIdentifierValue>\n" +
                        "</ns1:RequestId>\n" +
                        "<ns1:UserId>\n" +
                            "<ns1:UserIdentifierType>EIDM</ns1:UserIdentifierType>\n" +
                            "<ns1:UserIdentifierValue>998c3f4b-765e-48b2-asdf-8bd2cf8acee9</ns1:UserIdentifierValue>\n" +
                        "</ns1:UserId>\n" +
                        "<ns1:RequestType ns1:Scheme=\"http://www.niso.org/ncip/v1_0/imp1/schemes/requesttype/requesttype.scm\">Hold</ns1:RequestType>\n" +
                        "<ns1:RequestScopeType ns1:Scheme=\"http://www.niso.org/ncip/v1_0/imp1/schemes/requestscopetype/requestscopetype.scm\">Bibliographic Item</ns1:RequestScopeType>\n" +
                        "<ns1:ItemOptionalFields>\n" +
                            "<ns1:BibliographicDescription>\n" +
                                "<ns1:Author>Staikos, K.</ns1:Author>\n" +
                                "<ns1:BibliographicRecordId>\n" +
                                    "<ns1:BibliographicRecordIdentifier>43286419</ns1:BibliographicRecordIdentifier>\n" +
                                    "<ns1:BibliographicRecordIdentifierCode ns1:Scheme=\"http://www.niso.org/ncip/v1_0/imp1/schemes/bibliographicrecordidentifiercode/bibliographicrecordidentifiercode.scm\">OCLC</ns1:BibliographicRecordIdentifierCode>\n" +
                                "</ns1:BibliographicRecordId>\n" +
                                "<ns1:Edition>1st English ed.</ns1:Edition>\n" +
                                "<ns1:PublicationDate>2000</ns1:PublicationDate>\n" +
                                "<ns1:Publisher>London : The British Library,</ns1:Publisher>\n" +
                                "<ns1:Title>The great libraries : from antiquity to the Renaissance (3000 B.C. to A.D. 1600) /</ns1:Title>\n" +
                                "<ns1:Language ns1:Scheme=\"http://lcweb.loc.gov/standards/iso639-2/bibcodes.html\">eng</ns1:Language>\n" +
                                "<ns1:MediumType ns1:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/mediumtype.scm\">Book</ns1:MediumType>\n" +
                            "</ns1:BibliographicDescription>\n" +
                            "<ns1:HoldQueueLength>1</ns1:HoldQueueLength>\n" +
                        "</ns1:ItemOptionalFields>\n" +
                        "<ns1:Ext>\n" +
                            "<ns1:HoldQueuePosition>1</ns1:HoldQueuePosition>\n" +
                        "</ns1:Ext>\n" +
                    "</ns1:RequestItemResponse>\n" +
                "</ns1:NCIPMessage>\n";
        return create_response(xml);
    }

    @Override
    public WMSResponse request_bibliographic() {
        // TODO: Add mock api response
        return null;
    }

    @Override
    public WMSResponse update_request() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                    "<ns1:NCIPMessage xmlns:ns1=\"http://www.niso.org/2008/ncip\" xmlns=\"http://oclc.org/WCL/ncip/2011/extensions\" xmlns:ns3=\"http://www.oclc.org/ncip/usernote/2012\">\n" +
                        "<ns1:UpdateRequestItemResponse>\n" +
                            "<ns1:ItemId>\n" +
                                "<ns1:ItemIdentifierValue/>\n" +
                            "</ns1:ItemId>\n" +
                            "<ns1:UserId>\n" +
                                "<ns1:UserIdentifierType>EIDM</ns1:UserIdentifierType>\n" +
                                "<ns1:UserIdentifierValue>998c3f4b-765e-48b2-asdf-8bd2cf8acee9</ns1:UserIdentifierValue>\n" +
                            "</ns1:UserId>\n" +
                            "<ns1:RequestType ns1:Scheme=\"http://www.niso.org/ncip/v1_0/imp1/schemes/requesttype/requesttype.scm\">Hold</ns1:RequestType>\n" +
                            "<ns1:RequestScopeType ns1:Scheme=\"http://www.niso.org/ncip/v1_0/imp1/schemes/requestscopetype/requestscopetype.scm\">Bibliographic Item</ns1:RequestScopeType>\n" +
                        "</ns1:UpdateRequestItemResponse>\n" +
                    "</ns1:NCIPMessage>\n";
        return create_response(xml);
    }

    @Override
    public WMSResponse cancel_request() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                    "<ns1:NCIPMessage xmlns:ns1=\"http://www.niso.org/2008/ncip\" xmlns=\"http://oclc.org/WCL/ncip/2011/extensions\" xmlns:ns3=\"http://www.oclc.org/ncip/usernote/2012\">\n" +
                        "<ns1:CancelRequestItemResponse>\n" +
                            "<ns1:RequestId>\n" +
                                "<ns1:AgencyId>128807</ns1:AgencyId>\n" +
                                "<ns1:RequestIdentifierValue>bc219183-b3a8-4d5b-878d-797a8dff95ec</ns1:RequestIdentifierValue>\n" +
                            "</ns1:RequestId>\n" +
                            "<ns1:UserId>\n" +
                                "<ns1:UserIdentifierType>EIDM</ns1:UserIdentifierType>\n" +
                                "<ns1:UserIdentifierValue>998c3f4b-765e-48b2-asdf-8bd2cf8acee9</ns1:UserIdentifierValue>\n" +
                            "</ns1:UserId>\n" +
                        "</ns1:CancelRequestItemResponse>\n" +
                    "</ns1:NCIPMessage>\n";


        return create_response(xml);
    }

    private WMSResponse create_response(String xml){
        return new WMSNCIPResponse(
                xml
        );
    }
}
