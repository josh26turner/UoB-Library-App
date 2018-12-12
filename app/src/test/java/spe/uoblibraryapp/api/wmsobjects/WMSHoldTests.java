package spe.uoblibraryapp.api.wmsobjects;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.GregorianCalendar;

import javax.xml.parsers.ParserConfigurationException;

import spe.uoblibraryapp.api.ncip.WMSNCIPElement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static spe.uoblibraryapp.api.XMLParser.parse;

public class WMSHoldTests {

    @Test
    public void testRequestIdParsed() throws WMSParseException{
        WMSHold hold = createParsedHold();
        assertEquals(
                "RequestId does not match",
                "be5aa3d8-e143-414f-a1af-5a6c88dc3b5d",
                hold.getRequestId()
        );
    }

    @Test
    public void testAgencyIdParsed() throws WMSParseException{
        WMSHold hold = createParsedHold();
        assertEquals(
                "AgencyId does not match",
                "128807",
                hold.getAgencyId()
        );
    }

    @Test
    public void testRequestTypeParsed() throws WMSParseException{
        WMSHold hold = createParsedHold();
        assertEquals(
                "RequestType does not match",
                "Hold",
                hold.getRequestType()
        );
    }

    @Test
    public void testRequestStatusTypeParsed() throws WMSParseException{
        WMSHold hold = createParsedHold();
        assertEquals(
                "RequestStatusType does not match",
                "In Process",
                hold.getRequestStatusType()
        );
    }

    @Test
    public void testDatePlacedParsed() throws WMSParseException{
        WMSHold hold = createParsedHold();
        assertEquals(
                "DatePlaced does not match",
                new GregorianCalendar(2014, 1, 21,16, 48, 14).getTime(),
                hold.getDatePlaced()
        );
    }

    @Test
    public void testPickupLocationParsed() throws WMSParseException{
        WMSHold hold = createParsedHold();
        assertEquals(
                "PickupLocation does not match",
                "129055",
                hold.getPickupLocation()
        );
    }

    @Test
    public void testMediumTypeParsed() throws WMSParseException{
        WMSHold hold = createParsedHold();
        assertEquals(
                "MediumType does not match",
                "Book" ,
                hold.getMediumType()
        );
    }

    @Test
    public void testHoldQueuePositionParsed() throws WMSParseException{
        WMSHold hold = createParsedHold();
        assertEquals(
                "HoldQueuePosition does not match",
                (Integer) 1,
                hold.getHoldQueuePosition()
        );
    }

    @Test
    public void testHoldQueueLengthParsed() throws WMSParseException{
        WMSHold hold = createParsedHold();
        assertEquals(
                "HoldQueueLength does not match",
                (Integer) 1,
                hold.getHoldQueueLength()
        );
    }

    @Test
    public void testEarliestDateNeededParsed() throws WMSParseException{
        WMSHold hold = createParsedHold();
        assertEquals(
                "EarliestDateNeeded does not match",
                new GregorianCalendar(2014, 1, 21, 16, 48, 40).getTime(),
                hold.getEarliestDateNeeded()
        );
    }

    @Test
    public void testNeedBeforeDateParsed() throws WMSParseException{
        WMSHold hold = createParsedHold();
        assertEquals(
                "NeedBeforeDate does not match",
                new GregorianCalendar(2034, 1, 21, 16, 48, 40).getTime(),
                hold.getNeedBeforeDate()
        );
    }







    private WMSHold createParsedHold() throws WMSParseException{
        Document doc = null;
        try {
            doc = parse(
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
                            "        </ns1:RequestedItem>\n" );
        } catch (ParserConfigurationException | IOException | SAXException e){
            fail(e.getMessage());
        }
        NodeList nodes = doc.getChildNodes();

        return new WMSHold(new WMSNCIPElement(nodes.item(0)));
    }
}
