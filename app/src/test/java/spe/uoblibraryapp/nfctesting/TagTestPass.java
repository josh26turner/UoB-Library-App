package spe.uoblibraryapp.nfctesting;

import android.nfc.Tag;
import android.os.Bundle;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TagTestPass {
    private static final int TECH_NFC_V = 1;
    private static final String EXTRA_NFC_V_RESP_FLAGS = "respflags";  // byte (Response Flag)
    private static final String EXTRA_NFC_V_DSFID = "dsfid";           // byte (DSF ID);

    public static Tag mockTag() {
        Class tagClass = Tag.class;

        Method createMock;

        try {
            createMock = tagClass.getMethod("createMockTag", byte[].class, int[].class, Bundle[].class);
            Bundle nfcvBundle = new Bundle();
            nfcvBundle.putByteArray(EXTRA_NFC_V_DSFID, new byte[]{(byte) 0x01, (byte) 0x02});
            nfcvBundle.putByteArray(EXTRA_NFC_V_RESP_FLAGS, new byte[]{(byte) 0x00});

            byte[] tagID = new byte[]{(byte) 0x00, (byte) 0x01};

            try {
                return (Tag) createMock.invoke(null, tagID,  new int[] {TECH_NFC_V}, new Bundle[]{ nfcvBundle});
            } catch (IllegalAccessException e) {
                System.out.println("Illegal access exception");
            } catch (InvocationTargetException e) {
                System.out.println("Invocation target exception");
            }
        } catch (NoSuchMethodException e) {
            System.out.println("No method");
            System.exit(0);
        }
        return null;
    }
}
