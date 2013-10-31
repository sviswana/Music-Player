package player;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class LCMTest {
    @Test
    //Testing 4 simple integers
    public void testLCM1() {
        List<Integer> l = new ArrayList<Integer>();
        l.add(2);
        l.add(3);
        l.add(4);
        l.add(5);
        LCM lcm = new LCM(l);
        assertEquals(lcm.getLCM(), 60);
    }

    @Test
    //testing 5 double-digit integers
    public void testLCM2() {
        List<Integer> l = new ArrayList<Integer>();
        l.add(17);
        l.add(29);
        l.add(13);
        l.add(24);
        l.add(43);
        LCM lcm = new LCM(l);
        assertEquals(lcm.getLCM(), 6614088);

    }
    
    @Test
    //Testing LCM with 0.
    public void testLCM3() {
        List<Integer> l = new ArrayList<Integer>();
        l.add(0);
        l.add(3);
        l.add(32);
        l.add(5);
        LCM lcm = new LCM(l);
        assertEquals(lcm.getLCM(), 0);
    }
    
    @Test
    //Testing all as 1.
    public void testLCM4() {
        List<Integer> l = new ArrayList<Integer>();
        l.add(1);
        l.add(1);
        l.add(1);
        l.add(1);
        LCM lcm = new LCM(l);
        assertEquals(lcm.getLCM(), 1);
    }
}
