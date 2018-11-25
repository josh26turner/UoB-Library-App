package spe.uoblibraryapp.nfctesting;

import org.junit.Assert;
import org.junit.Test;

public class HexTesting {
    @Test
    public void HexTesting() {
        byte [] readMultipleBlocksCommandOutput = {0x00, 0x23, 0x01, 0x02};
        Assert.assertArrayEquals(readMultipleBlocksCommandOutput, spe.uoblibraryapp.nfc.Hex.readMultipleBlocksCommand(1, 2));

        readMultipleBlocksCommandOutput[2] = (byte) 0x7F;
        readMultipleBlocksCommandOutput[3] = (byte) 0x00;
        Assert.assertArrayEquals(readMultipleBlocksCommandOutput, spe.uoblibraryapp.nfc.Hex.readMultipleBlocksCommand(127, 0));

        readMultipleBlocksCommandOutput[2] = (byte) 0x00;
        readMultipleBlocksCommandOutput[3] = (byte) 0x7F;
        Assert.assertArrayEquals(readMultipleBlocksCommandOutput, spe.uoblibraryapp.nfc.Hex.readMultipleBlocksCommand(0, 127));

        readMultipleBlocksCommandOutput[2] = (byte) 0x80;
        readMultipleBlocksCommandOutput[3] = (byte) 0x00;
        Assert.assertArrayEquals(readMultipleBlocksCommandOutput, spe.uoblibraryapp.nfc.Hex.readMultipleBlocksCommand(-128, 0));

        readMultipleBlocksCommandOutput[2] = (byte) 0x00;
        readMultipleBlocksCommandOutput[3] = (byte) 0x80;
        Assert.assertArrayEquals(readMultipleBlocksCommandOutput, spe.uoblibraryapp.nfc.Hex.readMultipleBlocksCommand(0, -128));
    }

    @Test(expected = NumberFormatException.class)
    public void HexExceptionTesting() {
        spe.uoblibraryapp.nfc.Hex.readMultipleBlocksCommand(128, 0);
        spe.uoblibraryapp.nfc.Hex.readMultipleBlocksCommand(0, 128);
        spe.uoblibraryapp.nfc.Hex.readMultipleBlocksCommand(128, 128);
        spe.uoblibraryapp.nfc.Hex.readMultipleBlocksCommand(-129, 0);
        spe.uoblibraryapp.nfc.Hex.readMultipleBlocksCommand(0, -129);
        spe.uoblibraryapp.nfc.Hex.readMultipleBlocksCommand(-129, -129);
    }
}
