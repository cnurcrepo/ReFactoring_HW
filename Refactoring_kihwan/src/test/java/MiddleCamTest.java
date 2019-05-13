import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class MiddleCamTest {

    MiddleCam middleCam;

    @Before
    public void createMiddleCamTestObj() {
        middleCam = new MiddleCam();
    }

    @Test
    public void testReceiveFrameData() {
        //given
        byte[] frameData = new byte[28];
        //when
        frameData[12] = (byte) 0x00;
        frameData[13] = (byte) 0x01;

        //then
        assertThat(this.middleCam.receiveFrameData(frameData), is(true));
    }

    @Test
    public void testReceiveAckData() {
        //given
        byte[] frameData = new byte[14];
        //when
        frameData[12] = (byte) 0x00;
        frameData[13] = (byte) 0x02;

        //then
        assertThat(this.middleCam.receiveFrameData(frameData), is(false));
    }

}