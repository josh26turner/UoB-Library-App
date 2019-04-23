package spe.uoblibraryapp.api.wmsobjects;


import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import spe.uoblibraryapp.api.XMLParser;
import spe.uoblibraryapp.api.ncip.WMSNCIPElement;

import static org.junit.Assert.assertEquals;

public class WMSCheckoutTests extends WMSUserProfileTests {

    @Test
    public void testBookParse(){
        WMSCheckout wmsCheckout = getCheckout();
        assertEquals(wmsCheckout.getBook().getBookId(), "606774536");

    }

    private WMSCheckout getCheckout(){
        try{
            Document doc = XMLParser.parse("<ns1:NCIPMessage xmlns:ns2=\"http://oclc.org/WCL/ncip/2011/extensions\" xmlns:ns1=\"http://www.niso.org/2008/ncip\" ns1:version=\"2.0\">\n" +
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
                    "<ns1:BibliographicRecordIdentifier>606774536</ns1:BibliographicRecordIdentifier>\n" +
                    "<ns1:BibliographicRecordIdentifierCode ns1:Scheme=\"http://www.niso.org/ncip/v2_0/schemes/bibliographicrecordidentifiercode/bibliographicrecordidentifiercode.scm\">OCLC</ns1:BibliographicRecordIdentifierCode>\n" +
                    "</ns1:BibliographicRecordId>\n" +
                    "<ns1:PublicationDate>2010</ns1:PublicationDate>\n" +
                    "<ns1:Publisher>New York : G.P. Putnam's Sons,</ns1:Publisher>\n" +
                    "<ns1:Title>Dead or alive /</ns1:Title>\n" +
                    "<ns1:Language ns1:Scheme=\"http://lcweb.loc.gov/standards/iso639-2/bibcodes.html\">eng</ns1:Language>\n" +
                    "<ns1:MediumType>Book</ns1:MediumType>\n" +
                    "</ns1:BibliographicDescription>\n" +
                    "</ns1:ItemOptionalFields>\n" +
                    "</ns1:CheckOutItemResponse>\n" +
                    "</ns1:NCIPMessage>");
            Node node = doc.getElementsByTagName("ns1:CheckOutItemResponse").item(0);
            return new WMSCheckout(new WMSNCIPElement(node));
        } catch (Exception ex){
            return null;
        }

    }

}
