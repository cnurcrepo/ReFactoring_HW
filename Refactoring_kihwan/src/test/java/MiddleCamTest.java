import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class MiddleCamTest {

    MiddleCam middleCam;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void createMiddleCamTestObj() {
        middleCam = new MiddleCam();
        System.setOut(new PrintStream(outContent));
        //upStream
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testReceiveFrameDataInBoard() {
        byte[] boardData = new byte[28];
        boardData[12] = (byte) 0x00;
        boardData[13] = (byte) 0x01;

        for (int index = 0; index < 6; index++) {
            boardData[index] = (byte) 0xFF;
        }
        byte[] boardDataNext = boardData.clone();
        assertThat(this.middleCam.receiveFrameData(boardData), is(false));


        byte[] boardSrc = new byte[6];
        for (int index = 0; index < 6; index++) {
            boardSrc[index] = (byte) index;
        }
        this.middleCam.setSrcAddr(boardSrc);
        assertThat(this.middleCam.receiveFrameData(boardDataNext), is(true));
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
        assertEquals("send Ack", this.outContent.toString());
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
        assertEquals("receive ack", this.outContent.toString());
    }

}