package spe.uoblibraryapp.api.ncip;

import spe.uoblibraryapp.api.WMSResponse;

class MockWMSNCIPStaffService implements WMSNCIPStaffService {

    @Override
    public WMSResponse checkOut(String userId, String itemId){
        // TODO: Change item identifier value to item id, user identifiable value to user id
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                    "<ns1:NCIPMessage xmlns:ns2=\"http://oclc.org/WCL/ncip/2011/extensions\" xmlns:ns1=\"http://www.niso.org/2008/ncip\" ns1:version=\"2.0\">\n" +
                        "<ns1:CheckOutItemResponse>\n" +
                            "<ns1:ItemId>\n" +
                                "<ns1:AgencyId>128807</ns1:AgencyId>\n" +
                                "<ns1:ItemIdentifierValue>10176</ns1:ItemIdentifierValue>\n" +
                            "</ns1:ItemId>\n" +
                            "<ns1:UserId>\n" +
                                "<ns1:AgencyId>128807</ns1:AgencyId>\n" +
                                "<ns1:UserIdentifierValue>1234567890</ns1:UserIdentifierValue>\n" +
                            "</ns1:UserId>\n" +
                            "<ns1:DateDue>2014-03-24T23:59:59.000-04:00</ns1:DateDue>\n" +
                            "<ns1:RenewalCount>1</ns1:RenewalCount>\n" +
                            "<ns1:ItemOptionalFields>\n" +
                                "<ns1:BibliographicDescription>\n" +
                                    "<ns1:Author>Clancy, Tom,</ns1:Author>\n" +
                                    "<ns1:BibliographicRecordId>\n" +
                                        "<ns1:BibliographicRecordIdentifier>606774536</ns1:BibliographicRecordIdentifier\n>" +
                                        "<ns1:BibliographicRecordIdentifierCode ns1:Scheme=\"http://www.niso.org/ncip/v2_0/schemes/bibliographicrecordidentifiercode/bibliographicrecordidentifiercode.scm\">OCLC</ns1:BibliographicRecordIdentifierCode>\n" +
                                    "</ns1:BibliographicRecordId>\n" +
                                    "<ns1:PublicationDate>2010</ns1:PublicationDate>\n" +
                                    "<ns1:Publisher>New York : G.P. Putnam's Sons,</ns1:Publisher>\n" +
                                    "<ns1:Title>Dead or alive /</ns1:Title>\n" +
                                    "<ns1:Language ns1:Scheme=\"http://lcweb.loc.gov/standards/iso639-2/bibcodes.html\">eng</ns1:Language>\n" +
                                    "<ns1:MediumType>Book</ns1:MediumType>\n" +
                                "</ns1:BibliographicDescription>\n" +
                                "<ns1:ItemUseRestrictionType>DEFAULT_4BOOK</ns1:ItemUseRestrictionType>\n" +
                                "<ns1:CirculationStatus ns1:Scheme=\"http://www.niso.org/ncip/v2_0/schemes/circulationstatus/circulationstatus.scm\">On Loan</ns1:CirculationStatus>\n" +
                                "<ns1:ItemDescription>\n" +
                                    "<ns1:CallNumber>PS3553.L245 D425 2010</ns1:CallNumber>\n" +
                                    "<ns1:ItemDescriptionLevel ns1:Scheme=\"http://www.niso.org/ncip/v2_0/schemes/itemdescriptionlevel/itemdescriptionlevel.scm\">Item</ns1:ItemDescriptionLevel>\n" +
                                    "<ns1:HoldingsInformation>\n" +
                                        "<ns1:StructuredHoldingsData>\n" +
                                            "<ns1:HoldingsEnumeration/>\n" +
                                            "<ns1:HoldingsChronology/>\n" +
                                        "</ns1:StructuredHoldingsData>\n" +
                                    "</ns1:HoldingsInformation>\n" +
                                    "<ns1:NumberOfPieces>1</ns1:NumberOfPieces>\n" +
                                "</ns1:ItemDescription>\n" +
                                "<ns1:Location>\n" +
                                    "<ns1:LocationType ns1:Scheme=\"http://www.niso.org/ncip/v2_0/schemes/locationtype/locationtype.scm\">Permanent Location</ns1:LocationType>\n" +
                                    "<ns1:LocationName>\n" +
                                        "<ns1:LocationNameInstance>\n" +
                                            "<ns1:LocationNameLevel>1</ns1:LocationNameLevel>\n" +
                                            "<ns1:LocationNameValue>MAIN</ns1:LocationNameValue>\n" +
                                        "</ns1:LocationNameInstance>\n" +
                                        "<ns1:LocationNameInstance>\n" +
                                        "<ns1:LocationNameLevel>2</ns1:LocationNameLevel>\n" +
                                            "<ns1:LocationNameValue>MAIN-STACKS</ns1:LocationNameValue>\n" +
                                        "</ns1:LocationNameInstance>\n" +
                                    "</ns1:LocationName>\n" +
                                "</ns1:Location>\n" +
                            "</ns1:ItemOptionalFields>\n" +
                        "</ns1:CheckOutItemResponse>\n" +
                    "</ns1:NCIPMessage>\n";

        return createResponse(xml);
    }

    @Override
    public WMSResponse checkIn(String item_id){
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                    "<ns1:NCIPMessage xmlns:ns2=\"http://oclc.org/WCL/ncip/2011/extensions\" xmlns:ns1=\"http://www.niso.org/2008/ncip\" ns1:version=\"2.0\">\n" +
                        "<ns1:CheckInItemResponse>\n" +
                            "<ns1:ItemId>\n" +
                                "<ns1:AgencyId>128807</ns1:AgencyId>\n" +
                                "<ns1:ItemIdentifierValue>10176</ns1:ItemIdentifierValue>\n" +
                            "</ns1:ItemId>\n" +
                            "<ns1:UserId>\n" +
                                "<ns1:AgencyId>128807</ns1:AgencyId>\n" +
                                "<ns1:UserIdentifierValue>1234567890</ns1:UserIdentifierValue>\n" +
                            "</ns1:UserId>\n" +
                            "<ns1:RoutingInformation>\n" +
                                "<ns1:RoutingInstructions>Re-shelve</ns1:RoutingInstructions>\n" +
                                "<ns1:Destination/>\n" +
                            "</ns1:RoutingInformation>\n" +
                        "</ns1:CheckInItemResponse>\n" +
                    "</ns1:NCIPMessage>\n";

        return createResponse(xml);
    }
    
    @Override
    public WMSResponse requestItem(){
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                    "<ns1:NCIPMessage xmlns:ns2=\"http://oclc.org/WCL/ncip/2011/extensions\" xmlns:ns1=\"http://www.niso.org/2008/ncip\" ns1:version=\"2.0\">\n" +
                        "<ns1:RequestItemResponse>\n" +
                            "<ns1:RequestId>\n" +
                                "<ns1:AgencyId ns1:Scheme=\"http://oclc.org/ncip/schemes/agencyid.scm\">128807</ns1:AgencyId>\n" +
                                "<ns1:RequestIdentifierValue>1789ec18-a68f-4ed9-a796-18c59c983aa5</ns1:RequestIdentifierValue>\n" +
                            "</ns1:RequestId>\n" +
                            "<ns1:UserId>\n" +
                                "<ns1:AgencyId>128807</ns1:AgencyId>\n" +
                                "<ns1:UserIdentifierValue>1234567890</ns1:UserIdentifierValue>\n" +
                            "</ns1:UserId>\n" +
                            "<ns1:RequestType ns1:Scheme=\"http://www.niso.org/ncip/v1_0/imp1/schemes/requesttype/requesttype.scm\">Hold</ns1:RequestType>\n" +
                            "<ns1:RequestScopeType ns1:Scheme=\"http://www.niso.org/ncip/v1_0/imp1/schemes/requestscopetype/requestscopetype.scm\">Item</ns1:RequestScopeType>\n" +
                        "</ns1:RequestItemResponse>\n" +
                    "</ns1:NCIPMessage>\n";

        return createResponse(xml);
    }

    @Override
    public WMSResponse requestBibliographic(){
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
        "<ns1:NCIPMessage xmlns:ns2=\"http://oclc.org/WCL/ncip/2011/extensions\" xmlns:ns1=\"http://www.niso.org/2008/ncip\" ns1:version=\"2.0\">\n" +
            "<ns1:RequestItemResponse>\n" +
                "<ns1:RequestId>\n" +
                    "<ns1:AgencyId ns1:Scheme=\"http://oclc.org/ncip/schemes/agencyid.scm\">128807</ns1:AgencyId>\n" +
                    "<ns1:RequestIdentifierValue>8bf42f54-4fb6-471c-a35f-6c2e4b6fc9ce</ns1:RequestIdentifierValue>\n" +
                "</ns1:RequestId>\n" +
                "<ns1:UserId>\n" +
                    "<ns1:AgencyId>128807</ns1:AgencyId>\n" +
                    "<ns1:UserIdentifierValue>1234567890</ns1:UserIdentifierValue>\n" +
                "</ns1:UserId>\n" +
                "<ns1:RequestType ns1:Scheme=\"http://www.niso.org/ncip/v1_0/imp1/schemes/requesttype/requesttype.scm\">Hold</ns1:RequestType>\n" +
                "<ns1:RequestScopeType ns1:Scheme=\"http://www.niso.org/ncip/v1_0/imp1/schemes/requestscopetype/requestscopetype.scm\">Bibliographic Item</ns1:RequestScopeType>\n" +
            "</ns1:RequestItemResponse>\n" +
        "</ns1:NCIPMessage>\n";

        return createResponse(xml);
    }

    @Override
    public WMSResponse cancelRequest(){
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                    "<ns1:NCIPMessage xmlns:ns2=\"http://oclc.org/WCL/ncip/2011/extensions\" xmlns:ns1=\"http://www.niso.org/2008/ncip\" ns1:version=\"2.0\">\n" +
                        "<ns1:CancelRequestItemResponse>\n" +
                            "<ns1:ItemId>\n" +
                                "<ns1:AgencyId>128807</ns1:AgencyId>\n" +
                                "<ns1:ItemIdentifierValue>10176</ns1:ItemIdentifierValue>\n" +
                            "</ns1:ItemId>\n" +
                            "<ns1:UserId>\n" +
                                "<ns1:AgencyId>128807</ns1:AgencyId>\n" +
                                "<ns1:UserIdentifierValue>1234567890</ns1:UserIdentifierValue>\n" +
                            "</ns1:UserId>\n" +
                        "</ns1:CancelRequestItemResponse>\n" +
                    "</ns1:NCIPMessage>\n";

        return createResponse(xml);
    }

    @Override
    public WMSResponse cancelBibliographic(){
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                    "<ns1:NCIPMessage xmlns:ns2=\"http://oclc.org/WCL/ncip/2011/extensions\" xmlns:ns1=\"http://www.niso.org/2008/ncip\" ns1:version=\"2.0\">\n" +
                        "<ns1:CancelRequestItemResponse>\n" +
                            "<ns1:RequestId>\n" +
                                "<ns1:AgencyId>128807</ns1:AgencyId>\n" +
                                "<ns1:RequestIdentifierValue>8bf42f54-4fb6-471c-a35f-6c2e4b6fc9ce</ns1:RequestIdentifierValue>\n" +
                            "</ns1:RequestId>\n" +
                            "<ns1:UserId>\n" +
                                "<ns1:AgencyId>128807</ns1:AgencyId>\n" +
                                "<ns1:UserIdentifierValue>1234567890</ns1:UserIdentifierValue>\n" +
                            "</ns1:UserId>\n" +
                        "</ns1:CancelRequestItemResponse>\n" +
                    "</ns1:NCIPMessage>\n";

        return createResponse(xml);
    }

    private WMSResponse createResponse(String xml){
        return new WMSNCIPResponse(xml);
    }
}