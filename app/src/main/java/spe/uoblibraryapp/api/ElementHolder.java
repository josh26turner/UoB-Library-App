package spe.uoblibraryapp.api;

import org.w3c.dom.Node;

/**
 * Use this when passing an element to a wmsobject.
 *
 * This allows the wmsobject to differentiate different API responses,
 * so it can parse them differently
 *
 */
public interface ElementHolder {

    void setElem(Node elem);
    Node getElem();
}
