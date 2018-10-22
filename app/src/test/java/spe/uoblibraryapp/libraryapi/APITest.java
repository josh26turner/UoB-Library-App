package spe.uoblibraryapp.libraryapi;

import org.junit.Test;
import static org.junit.Assert.*;

public class APITest {
    @Test
    public void add_two_numbers(){
        API api = new API();
        assertEquals(10, api.add(1,9));
    }
}
