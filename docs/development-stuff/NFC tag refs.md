Get system info - 0x2B:
    15 bytes (0 - 14)

    Byte 0:
        Status
        0x00 - OK

    Byte 1:
        Flags
        Bits: (all true: 1, false: 0)
            1 - DFID - how it is structured
            2 - AFI - family groups
            3 - Mem size
            4 - IC reference
            Rest:  ¯\_(ツ)_/¯
    Bytes 2 - 9:
        UID: bytes should be reversed but not the bits in the bytes (?)

    Byte 10:
        DSFID

    Byte 11:
        AFI

    Bytes 12 - 13:
        12 = number of blocks
        13 = blocksize

    Byte 14:
        IC reference

Inventory - 0x14:
    8 bytes:
        UID

Read multple bytes - 0x23, 0xXY, 0xWZ:
    0xXY: offset
    0xWZ: number of blocks