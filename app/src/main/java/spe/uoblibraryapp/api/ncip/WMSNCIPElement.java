package spe.uoblibraryapp.api.ncip;

import org.w3c.dom.Element;

import spe.uoblibraryapp.api.ElementHolder;

public class WMSNCIPElement implements ElementHolder {
    private Element elem;
    public WMSNCIPElement(Element elem) {
        this.elem = elem;
    }

    @Override
    public void setElem(Element elem) {
        this.elem = elem;
    }

    @Override
    public Element getElem(){
        return this.elem;
    }
}
