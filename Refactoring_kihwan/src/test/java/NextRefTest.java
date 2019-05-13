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

        //given
        //객체 생성 및 자료구조에 테스트 할 경우를 추가 해 준다

        //when
        //값불러올 경우

        //then 나눠서 사용하기
        //assertJ를 사용하기 -> 더 쉽게 할 수 있다.
        //hasSize(), contains 가지고 있는거 확인하기
        //검증 하는 단계 결과를 보는 곳

    }
}