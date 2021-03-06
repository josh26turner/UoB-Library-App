package spe.uoblibraryapp.api.wmsobjects;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import spe.uoblibraryapp.api.ncip.WMSNCIPElement;

/**
 * This is generated by a user profile and has all the controls needed to checkout a book
 * @author rileyevans
 */
public class WMSCheckout {
    private WMSBook book;


    public WMSCheckout(
            WMSNCIPElement elementHolder
    ) throws WMSParseException {
        Node node = elementHolder.getElem();

        // Check the node contains the correct data, before parsing.
//        System.out.print(node.getNodeName());
//        throw new WMSParseException(node.getNodeName());
        if (!node.getNodeName().equals("ns1:CheckOutItemResponse")) {
            throw new WMSParseException("WMSUserProfile requires a <ns1:CheckOutItemResponse> Node");
        }

        // Parse the node :o
        parseNode(node);
    }


    private void parseNode(Node node) throws WMSParseException {
        NodeList childNodes = node.getChildNodes();
        for (int i=0; i<childNodes.getLength(); i++) {
            Node child = childNodes.item(i);

            switch (child.getNodeName()) {
                case "ns1:ItemOptionalFields":
                    NodeList childsChildren = child.getChildNodes();
                    for (int j=0; j<childsChildren.getLength(); j++){
                        Node childsChild = childsChildren.item(j);
                        if (childsChild.getNodeName().equals("ns1:BibliographicDescription")){
                            book = new WMSBook(new WMSNCIPElement(childsChild));
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Exists for the UI people to get book info for a checkout
     * @return
     */
    public WMSBook getBook(){
        return this.book;
    }
}
