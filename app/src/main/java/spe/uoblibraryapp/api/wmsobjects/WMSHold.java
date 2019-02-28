package spe.uoblibraryapp.api.wmsobjects;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import spe.uoblibraryapp.api.ncip.WMSNCIPElement;

public class WMSHold {

    private static final String TAG = "WMSHold";

    private String requestId;
    private String agencyId;
    private String requestType;
    private String requestStatusType;
    private Date datePlaced;
    private String pickupLocation;
    private String mediumType;
    private Integer holdQueuePosition;
    private Integer holdQueueLength;
    private Date earliestDateNeeded;
    private Date needBeforeDate;
    private WMSBook book;


    WMSHold(WMSNCIPElement elemHolder) throws WMSParseException {

        Node element = elemHolder.getElem();

        if (!element.getNodeName().equals("ns1:RequestedItem")) {
            throw new WMSParseException("Node is not correct loan node");
        }

        // Parse the node
        try {
            parseNode(element);
        } catch (ParseException e) {
            throw new WMSParseException(e.getMessage());
        }
    }


    public WMSHold() {
        requestId = "1234567890";
        agencyId = "132607";
        requestType = "Hold";
        requestStatusType = "In Process";
        datePlaced = new Date();
        pickupLocation = "119061";
        mediumType = "Book";
        holdQueuePosition = 1;
        holdQueueLength = 3;
        earliestDateNeeded = new Date();
        needBeforeDate = new Date();
        book = new WMSBook("1234567890");
    }

    private Date parseDate(String strDate) throws ParseException {
        String replacedStrDate = strDate.replace("T", "-").replace("Z", "");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        return format.parse(replacedStrDate);
    }


    private void parseNode(Node node) throws ParseException, WMSParseException {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            switch (child.getNodeName()) {
                case "ns1:RequestId":
                    NodeList childsChildren = child.getChildNodes();
                    for (int j = 0; j < childsChildren.getLength(); j++) {
                        Node childsChild = childsChildren.item(j);
                        switch (childsChild.getNodeName()) {
                            case "ns1:AgencyId":
                                agencyId = childsChild.getTextContent();
                                break;
                            case "ns1:RequestIdentifierValue":
                                requestId = childsChild.getTextContent();
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                case "ns1:RequestType":
                    requestType = child.getTextContent();
                    break;
                case "ns1:RequestStatusType":
                    requestStatusType = child.getTextContent();
                    break;
                case "ns1:DatePlaced":
                    datePlaced = parseDate(child.getTextContent());
                    break;
                case "ns1:PickupLocation":
                    pickupLocation = child.getTextContent();
                    break;
                case "ns1:HoldQueuePosition":
                    holdQueuePosition = Integer.valueOf(child.getTextContent());
                    break;
                case "ns1:MediumType":
                    mediumType = child.getTextContent();
                    break;
                case "ns1:Ext":
                    NodeList childsChildren2 = child.getChildNodes();
                    for (int j = 0; j < childsChildren2.getLength(); j++) {
                        Node childsChild = childsChildren2.item(j);
                        switch (childsChild.getNodeName()) {
                            case "ns1:BibliographicDescription":
                                book = new WMSBook(new WMSNCIPElement(childsChild));
                                break;
                            case "ns1:EarliestDateNeeded":
                                earliestDateNeeded = parseDate(childsChild.getTextContent());
                                break;
                            case "ns1:HoldQueueLength":
                                holdQueueLength = Integer.valueOf(childsChild.getTextContent());
                                break;
                            case "ns1:NeedBeforeDate":
                                needBeforeDate = parseDate(childsChild.getTextContent());
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }


    // Getters for UI people

    public String getRequestId() {
        return this.requestId;
    }
    public String getAgencyId() {
        return this.agencyId;
    }
    public String getRequestType() {
        return this.requestType;
    }
    public String getRequestStatusType() {
        return this.requestStatusType;
    }
    public Date getDatePlaced() {
        return this.datePlaced;
    }

    public String getPickupLocation() {
        switch (this.pickupLocation) {
            case "119059":
                return "Queens Building Library";
            case "119036":
                return "Arts and Social Sciences Library";
            case "119038":
                return "Biological Sciences Library";
            case "119045":
                return "Chemistry Library";
            case "119049":
                return "Education Library";
            case "119054":
                return "Medical Library";
            case "119058":
                return "Physics Library";
            case "119060":
                return "Veterinary Sciences Library";
            case "119061":
                return "Wills Memorial Library";
            default:
                return this.pickupLocation;
        }
    }

    public String getBranchId(){
        return this.pickupLocation;
    }

    public Integer getHoldQueuePosition() {
        return this.holdQueuePosition;
    }
    public String getMediumType() {
        return this.mediumType;
    }
    public WMSBook getBook() {
        return this.book;
    }
    public Date getEarliestDateNeeded() {
        return this.earliestDateNeeded;
    }
    public Integer getHoldQueueLength() {
        return this.holdQueueLength;
    }
    public Date getNeedBeforeDate() {
        return this.needBeforeDate;
    }
    public Boolean isReadyToCollect() {
        return "Available For Pickup".equals(this.requestStatusType);
    }
}
