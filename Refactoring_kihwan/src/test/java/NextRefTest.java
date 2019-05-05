import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class NextRefTest {
    private NextRef nextRef;

    @Before
    public void createCaculator() {
        this.nextRef = new NextRef();
    }

    @Test
    public void canReceiveBoardCast() {
        byte[] data = new byte[28];
        data[13] = 0x01;
        assertThat(this.nextRef.Receive(data), is(true));
    }
}