package spe.uoblibraryapp.api.ncip;

import org.w3c.dom.Node;

import spe.uoblibraryapp.api.ElementHolder;

/**
 * Stores the document element for an WMS NCIP response
 *
 */
public class WMSNCIPElement implements ElementHolder {
    private Node elem;
    public WMSNCIPElement(Node elem) {
        this.elem = elem;
    }

    @Override
    public void setElem(Node elem) {
        this.elem = elem;
    }

    @Override
    public Node getElem(){
        return this.elem;
    }
}
