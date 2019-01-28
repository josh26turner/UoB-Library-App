package spe.uoblibraryapp.api.wmsobjects;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import spe.uoblibraryapp.api.WMSException;
import spe.uoblibraryapp.api.ncip.WMSNCIPElement;

/**
 * This is generated by a user profile and has all the controls needed to checkout a book
 * @author rileyevans
 */
public class WMSCheckout {
    private String itemId;
    private WMSBook book;
    private String userId;
    private Date dueDate;

    // TODO: Change this class to represent a checkout response. Stores due date and a WMSBook.

    public WMSCheckout(
            WMSNCIPElement elementHolder,
            String userId
    ) throws WMSParseException {
        this.userId = userId;
        Node node = elementHolder.getElem();

        // Check the node contains the correct data, before parsing.

        if (!node.getNodeName().equals("ns1:CheckOutItemResponse")) {
            throw new WMSParseException("WMSUserProfile requires a <ns1:LookupUserResponse> Node");
        }

        // Parse the node :o
        try {
            parseNode(node);
        } catch (ParseException e) {
            throw new WMSParseException(e.getMessage());
        }

    }


    private void parseNode(Node node) throws WMSParseException, ParseException {
        NodeList childNodes = node.getChildNodes();
        for (int i=0; i<childNodes.getLength(); i++) {
            Node child = childNodes.item(i);

            switch (child.getNodeName()) {
                case "ns1:UserId":
                    parseUserId(child);
                    break;
                case "ns1:ItemId":
                    parseItemId(child);
                    break;
                case "ns1:DueDate":
                    // TODO: Add check if request is on hold or recently recieved.
                    // Can they be dealt with by the same list?
                    dueDate = parseDate(child.getTextContent());
                    break;
                case "ns1:ItemOptionalFields":
                    NodeList childsChildren = child.getChildNodes();
                    for (int j=0; j<childsChildren.getLength(); j++){
                        Node childsChild = childsChildren.item(j);
                        if (childsChild.getNodeName().equals("ns1:BibliographicDescription")){
                            book = new WMSBook(new WMSNCIPElement(childsChild));
                        }
                    }
                    break;
            }
        }
    }



    private void parseUserId(Node userIdNode) throws WMSParseException{
        NodeList children = userIdNode.getChildNodes();
        for (int i=0; i<children.getLength(); i++){
            Node child = children.item(i);
            if (child.getNodeName().equals("ns1:UserIdentifierValue")){
                if (!userId.equals(child.getTextContent())){
                    throw new WMSParseException("UserId of response does not match UserId of the profile given");
                }
                return;
            }
        }
    }

    private void parseItemId(Node itemIdNode) throws WMSParseException{
        NodeList children = itemIdNode.getChildNodes();
        for (int i=0; i<children.getLength(); i++){
            Node child = children.item(i);
            if (child.getNodeName().equals("ns1:ItemIdentifierValue")){
                itemId = child.getTextContent();
                return;
            }
        }
        if (this.itemId == null){
            throw new WMSParseException("No ItemId element can be found");
        }
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
     * Exists for the UI people to get book info for a checkout
     * @return
     */
    public WMSBook getBook(){
        return this.book;
    }


    public Date getDueDate() {
        return dueDate;
    }
}
