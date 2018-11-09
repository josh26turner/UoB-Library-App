package spe.uoblibraryapp.api;

import org.w3c.dom.Document;

public interface WMSResponse {
    String raw_response();
    Document parse_response();
    Boolean did_fail();
}
