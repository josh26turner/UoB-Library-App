package spe.uoblibraryapp.api.wmsobjects;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import spe.uoblibraryapp.api.ncip.WMSNCIPElement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static spe.uoblibraryapp.api.XMLParser.parse;

public class WMSBookTests {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    // Simple Getter Tests

    @Test
    public void testGetBookId(){
        WMSBook book = createSimpleBook();
        assertEquals(
                "The book id does not match",
                "123456789",
                book.getBookId()
        );
    }

    @Test
    public void testGetTitle(){
        WMSBook book = createSimpleBook();
        assertEquals(
                "The title does not match",
                "Diary of a wimpy kid : dog days",
                book.getTitle()
        );
    }

    @Test
    public void testGetPublisher(){
        WMSBook book = createSimpleBook();
        assertEquals(
                "The publisher does not match",
                "New York : Amulet Books",
                book.getPublisher()
        );
    }

    @Test
    public void testGetPublicationDate(){
        WMSBook book = createSimpleBook();
        assertEquals(
                "The publication date does not match",
                (Integer) 2007,
                book.getPublicationDate()
        );
    }

    @Test
    public void testGetAuthor(){
        WMSBook book = createSimpleBook();
        assertEquals(
                "The author ddes not match",
                "Shanske, Darien,",
                book.getAuthor()
        );
    }


    // Test parser works correctly
    @Test
    public void testParserGetBookID() throws WMSParseException{
        WMSBook book = createParsedBook();
        assertEquals("Book id does not match", "317923541" , book.getBookId());
    }

    @Test
    public void testParserGetTitle() throws WMSParseException{
        WMSBook book = createParsedBook();
        assertEquals("Title does not match", "Diary of a wimpy kid : dog days " , book.getTitle());
    }

    @Test
    public void testParserGetPublisher() throws WMSParseException{
        WMSBook book = createParsedBook();
        assertEquals("Publisher does not match", "New York : Amulet Books," , book.getPublisher());
    }

    @Test
    public void testParserGetPublicationDate() throws WMSParseException{
        WMSBook book = createParsedBook();
        assertEquals("Published date does not match", (Integer) 2009 , book.getPublicationDate());
    }

    @Test
    public void testParserGetAuthor() throws WMSParseException{
        WMSBook book = createParsedBook();
        assertEquals("Author does not match", "Kinney, Jeff." , book.getAuthor());
    }


    @Test
    public void testThrowsIfNodeIsNotBiliographicDescription() throws WMSParseException{
        Document doc = null;
        try {
            doc = parse(
                    "  <ns1:TestThis>\n" +
                            "    <ns1:Author>Kinney, Jeff.</ns1:Author>\n" +
                            "    <ns1:BibliographicRecordId>\n" +
                            "      <ns1:BibliographicRecordIdentifier>317923541</ns1:BibliographicRecordIdentifier>\n" +
                            "      <ns1:BibliographicRecordIdentifierCode ns1:Scheme=\"http://www.niso.org/ncip/v1_0/imp1/schemes/bibliographicrecordidentifiercode/bibliographicrecordidentifiercode.scm\">OCLC</ns1:BibliographicRecordIdentifierCode>\n" +
                            "    </ns1:BibliographicRecordId>\n" +
                            "    <ns1:PublicationDate>2009</ns1:PublicationDate>\n" +
                            "    <ns1:Publisher>New York : Amulet Books,</ns1:Publisher>\n" +
                            "    <ns1:Title>Diary of a wimpy kid : dog days /</ns1:Title>\n" +
                            "    <ns1:Language ns1:Scheme=\"http://lcweb.loc.gov/standards/iso639-2/bibcodes.html\">eng</ns1:Language>\n" +
                            "  </ns1:TestThis>\n");
        } catch (ParserConfigurationException | IOException | SAXException e){
            fail(e.getMessage());
        }
        NodeList nodes = doc.getChildNodes();

        exceptionRule.expect(WMSParseException.class);
        exceptionRule.expectMessage("WMSBook needs a <ns1:BibliographicDescription> Node");

        new WMSBook(new WMSNCIPElement(nodes.item(0)));
    }




    /**
     * Creates simple book, using empty WMSBook constructor
     * @return the book object
     */
    private WMSBook createSimpleBook(){
        return new WMSBook();
    }

    private WMSBook createParsedBook() throws WMSParseException{
        Document doc = null;
        try {
            doc = parse(
                         "  <ns1:BibliographicDescription>\n" +
                         "    <ns1:Author>Kinney, Jeff.</ns1:Author>\n" +
                         "    <ns1:BibliographicRecordId>\n" +
                         "      <ns1:BibliographicRecordIdentifier>317923541</ns1:BibliographicRecordIdentifier>\n" +
                         "      <ns1:BibliographicRecordIdentifierCode ns1:Scheme=\"http://www.niso.org/ncip/v1_0/imp1/schemes/bibliographicrecordidentifiercode/bibliographicrecordidentifiercode.scm\">OCLC</ns1:BibliographicRecordIdentifierCode>\n" +
                         "    </ns1:BibliographicRecordId>\n" +
                         "    <ns1:PublicationDate>2009</ns1:PublicationDate>\n" +
                         "    <ns1:Publisher>New York : Amulet Books,</ns1:Publisher>\n" +
                         "    <ns1:Title>Diary of a wimpy kid : dog days /</ns1:Title>\n" +
                         "    <ns1:Language ns1:Scheme=\"http://lcweb.loc.gov/standards/iso639-2/bibcodes.html\">eng</ns1:Language>\n" +
                         "  </ns1:BibliographicDescription>\n");
        } catch (ParserConfigurationException | IOException | SAXException e){
            fail(e.getMessage());
        }
        NodeList nodes = doc.getChildNodes();

        return new WMSBook(new WMSNCIPElement(nodes.item(0)));
    }
}
