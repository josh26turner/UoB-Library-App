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

public class WMSLoanTests {

    @Test
    public void testParsedGetItemId() throws WMSParseException{
        WMSLoan loan = createParsedLoan();
        assertEquals(
                "Item id does not match",
                "eec21bc3-6af3-4b9f-ac1c-1066c95e7732",
                loan.getItemId()
        );
    }

    @Test
    public void testParsedGetAgencyId() throws WMSParseException{
        WMSLoan loan = createParsedLoan();
        assertEquals(
                "Agency id does not match",
                "128807",
                loan.getAgencyId()
        );
    }

    @Test
    public void testParsedGetMediumType() throws WMSParseException{
        WMSLoan loan = createParsedLoan();
        assertEquals(
                "Medium Type does not match",
                "Book",
                loan.getMediumType()
        );
    }

    @Test
    public void testParsedGetDueDate() throws WMSParseException{
        WMSLoan loan = createParsedLoan();
        assertEquals(
                "Due date does not match",
                new GregorianCalendar(2018, 11, 1, 16, 20, 53).getTime(),
                loan.getDueDate()
        );
    }

    @Test
    public void testParsedGetCheckedOutDate() throws WMSParseException{
        WMSLoan loan = createParsedLoan();
        assertEquals(
                "Checked out date does not match",
                new GregorianCalendar(2018, 10, 24, 16, 20, 53).getTime(),
                loan.getCheckedOutDate()
        );
    }

    @Test
    public void testParsedGetRenewalCount() throws WMSParseException{
        WMSLoan loan = createParsedLoan();
        Integer count = 1;
        assertEquals(
                "Renewal count does not match",
                count,
                loan.getRenewalCount()
        );
    }

    @Test
    public void testParsedGetReminderLevel() throws WMSParseException{
        WMSLoan loan = createParsedLoan();
        Integer level = 0;
        assertEquals(
                "Reminder level does not match",
                level,
                loan.getReminderLevel()
        );
    }



    private WMSLoan createParsedLoan() throws WMSParseException{
        Document doc = null;
        try {
            doc = parse(
                    "        <ns1:LoanedItem>\n" + // Done (Ethical python hacking)
                            "            <ns1:ItemId>\n" +
                            "                <ns1:AgencyId ns1:Scheme=\"http://oclc.org/ncip/schemes/agencyid.scm\">128807</ns1:AgencyId>\n" +
                            "                <ns1:ItemIdentifierValue>eec21bc3-6af3-4b9f-ac1c-1066c95e7732</ns1:ItemIdentifierValue>\n" +
                            "            </ns1:ItemId>\n" +
                            "            <ns1:ReminderLevel>0</ns1:ReminderLevel>\n" +
                            "            <ns1:DateDue>2018-12-01T16:20:53Z</ns1:DateDue>\n" +
                            "            <ns1:Amount>\n" +
                            "                <ns1:CurrencyCode ns1:Scheme=\"http://www.bsi-global.com/Technical+Information/Publications/_Publications/tig90x.doc\">USD</ns1:CurrencyCode>\n" +
                            "                <ns1:MonetaryValue>0</ns1:MonetaryValue>\n" +
                            "            </ns1:Amount>\n" +
                            "            <ns1:MediumType ns1:Scheme=\"http://worldcat.org/ncip/schemes/v2/extensions/mediumtype.scm\">Book</ns1:MediumType>\n" +
                            "            <ns1:Ext>\n" +
                            "                <ns1:RenewalCount>1</ns1:RenewalCount>\n" +
                            "                <ns1:DateCheckedOut>2018-11-24T16:20:53Z</ns1:DateCheckedOut>\n" +
                            "                <ns1:BibliographicDescription>\n" +
                            "                    <ns1:Author>Sanjib, Sinha</ns1:Author>\n" +
                            "                    <ns1:BibliographicRecordId>\n" +
                            "                        <ns1:BibliographicRecordIdentifier>967844934</ns1:BibliographicRecordIdentifier>\n" +
                            "                        <ns1:BibliographicRecordIdentifierCode ns1:Scheme=\"http://www.niso.org/ncip/v1_0/imp1/schemes/bibliographicrecordidentifiercode/bibliographicrecordidentifiercode.scm\">OCLC</ns1:BibliographicRecordIdentifierCode>\n" +
                            "                    </ns1:BibliographicRecordId>\n" +
                            "                    <ns1:PublicationDate>2017</ns1:PublicationDate>\n" +
                            "                    <ns1:Publisher>[United States] : Apress, 2017.</ns1:Publisher>\n" +
                            "                    <ns1:Title>Beginning ethical hacking with Python</ns1:Title>\n" +
                            "                    <ns1:Language ns1:Scheme=\"http://lcweb.loc.gov/standards/iso639-2/bibcodes.html\">eng</ns1:Language>\n" +
                            "                </ns1:BibliographicDescription>\n" +
                            "            </ns1:Ext>\n" +
                            "        </ns1:LoanedItem>\n");
        } catch (ParserConfigurationException | IOException | SAXException e){
            fail(e.getMessage());
        }
        NodeList nodes = doc.getChildNodes();

        return new WMSLoan(new WMSNCIPElement(nodes.item(0)));
    }

}
