package spe.uoblibraryapp.api.wmsobjects;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import spe.uoblibraryapp.api.ncip.WMSNCIPElement;


/**
 * This is used to represent a loan, from WMS.
 */
public class WMSLoan {

    private String itemId;
    private String agencyId;
    private WMSBook book;
    private Date dueDate;
    private Date checkedOutDate;
    private Integer renewalCount;
    private Integer reminderLevel;
    private String mediumType;
    private Boolean isRenewable;

    /**
     * Constructor
     * @param elemHolder This contains the node information
     * @throws WMSParseException throws if the node fails to parse
     */
    WMSLoan(WMSNCIPElement elemHolder) throws WMSParseException{

        // Get Node.
        Node element = elemHolder.getElem();

        // Check correct node is passed to WMSLoan
        if (!element.getNodeName().equals("ns1:LoanedItem")){
            throw new WMSParseException("Node is not correct loan node");
        }

        // Parse the node :o
        try {
            parseNode(element);
        } catch (ParseException e){
            throw new WMSParseException(e.getMessage());
        }

        // TODO make WMS Availability request to check the status of the book.
        // TODO Can a standard user use the availability service
        // If so then can we join the two keys so the user only has to authenticate once? Ask David.
    }


    /**
     * To parse WMS dates into Java Dates
     * @param strDate this is a date from WMS
     * @return This returns the Java Date object from the parsed date
     * @throws ParseException Throws if the date fails to parse
     */
    private Date parseDate(String strDate) throws ParseException{
        strDate = strDate.replace("T", "-").replace("Z", "");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        return format.parse(strDate);
    }

    /**
     * This is for parsing the node during initialisation
     * @param node The top level node to be parsed
     * @throws ParseException thrown if a date fails to parse
     * @throws WMSParseException thrown if the WMSBook object fails to parse
     */
    private void parseNode(Node node) throws ParseException, WMSParseException{
        NodeList children = node.getChildNodes();
        for (int i=0; i<children.getLength(); i++){
            Node child = children.item(i);
            switch (child.getNodeName()){
                case "ns1:ItemId":
                    NodeList childsChildren = child.getChildNodes();
                    for (int x=0; x<childsChildren.getLength(); x++){
                        Node childsChild = childsChildren.item(x);
                        switch (childsChild.getNodeName()){
                            case "ns1:ItemIdentifierValue":
                                itemId = childsChild.getTextContent();
                                break;
                            case "ns1:AgencyId":
                                agencyId = childsChild.getTextContent();
                                break;
                        }
                    }
                    break;
                case "ns1:ReminderLevel":
                    reminderLevel = Integer.valueOf(child.getTextContent());
                    break;
                case "ns1:DateDue":
                    dueDate = parseDate(child.getTextContent());
                    break;
                case "ns1:MediumType":
                    mediumType = child.getTextContent();
                    break;
                case "ns1:Ext":
                    NodeList childsChildren2 = child.getChildNodes();
                    for (int x=0; x<childsChildren2.getLength(); x++){
                        Node childsChild = childsChildren2.item(x);
                        switch (childsChild.getNodeName()){
                            case "ns1:RenewalCount":
                                renewalCount = Integer.valueOf(childsChild.getTextContent());
                                break;
                            case "ns1:DateCheckedOut":
                                checkedOutDate = parseDate(childsChild.getTextContent());
                                break;
                            case "ns1:BibliographicDescription":
                                book = new WMSBook(new WMSNCIPElement(childsChild));
                                break;
                        }
                    }
                    break;
            }
        }
    }



    // Getters for UI people to use

    /**
     * This gets the WMSBook object for the book a loan is concerning
     * @return The WMSBook object
     */
    public WMSBook getBook(){
        return this.book;
    }

    /**
     * Gets the due date for the loan
     * @return the due date
     */
    public Date getDueDate(){
        return this.dueDate;
    }

    /**
     * Gets the date that the book was checked out
     * @return the checked out date
     */
    public Date getCheckedOutDate(){
        return this.checkedOutDate;
    }

    /**
     * Gets the renewal count for the loan
     * @return the renewal count
     */
    public Integer getRenewalCount(){
        return this.renewalCount;
    }

    /**
     * Gets the reminder level for a loan
     * @return the reminder level
     */
    public Integer getReminderLevel() {
        return this.reminderLevel;
    }

    /**
     * Gets the item id that the loan is for (identifies a specific book)
     * @return the item id
     */
    public String getItemId() {
        return this.itemId;
    }

    /**
     * Gets the agency id the loan was made from (should always be the same)
     * @return the agency id
     */
    public String getAgencyId() {
        return this.agencyId;
    }

    /**
     * Gets the medium type (book, ... etc)
     * @return the medium type
     */
    public String getMediumType() {
        return this.mediumType;
    }



    /**
     * Gets renewal status, if the book can be renewed
     * @return if can be renewed
     */
    public Boolean getRenewalStatus() {return this.isRenewable;}

    public Boolean isOverdue() {
        return this.dueDate.before(new Date());
    }
}
