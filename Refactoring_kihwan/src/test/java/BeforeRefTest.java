import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class BeforeRefTest {
    private BeforeRef before;

    @Before
    public void createCaculator() {
        before = new BeforeRef();
    }

    @Test
    public void canReceiveBoardCast() {
        byte[] data = new byte[24];
        data[13] = 0x01;
        assertThat(this.before.Receive(data), is(true));
    }
}