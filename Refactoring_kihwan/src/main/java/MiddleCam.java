import lombok.Data;

public class MiddleCam {
    private EtherNetFrame ethernetHeader = new EtherNetFrame();

    @Data
    private class EtherNetFrame {//src, dst 정보 하나의 객체 안에 넣기
        EtherNetAddr enetAddr;//dst 정보
        byte[] enetType;
        byte[] enetData;

        private EtherNetFrame() {
            this.enetAddr = new EtherNetAddr();
            this.enetType = new byte[2];
            this.enetData = null;
        }
    }

    @Data
    private class EtherNetAddr { //extract subclass를 사용해서 1개의 클래스에 많은 filed변수를 저장
        private byte[] dstAddr = new byte[6];
        private byte[] srcAddr = new byte[6];

        private EtherNetAddr() {
            for (int indexOfAddr = 0; indexOfAddr < 6; ++indexOfAddr) {
                this.dstAddr[indexOfAddr] = (byte) 0x00;
                this.srcAddr[indexOfAddr] = (byte) 0x00;
            }
        }
    }

    public boolean receiveFrameData(byte[] inputData) {
        if ((this.isBoardData(inputData) || this.isMyConnectionData(inputData)) && inputData[12] == (byte) 0x00) {
            if (inputData[13] == 0x01) {
                byte[] removeCapHeader = new byte[inputData.length - 14];
                System.arraycopy(inputData, 14, removeCapHeader, 0, removeCapHeader.length);
                System.out.println("send Ack");
                this.sendEhternetAck(inputData);
                return true;
            } else if (inputData[13] == 0x02) {
                System.out.println("receive ack");
                return false;//recive 안함 -> ack 받음 -> 다음 frame을 날림
            }
        }
        return false;
    }

    private boolean checkTheFrameData(byte[] myAddressData, byte[] inputFrameData, int inputDataStartIndex) {// add prarmeter 사용,
        for (int index = inputDataStartIndex; index < inputDataStartIndex + 6; index++) {
            if (inputFrameData[index] != myAddressData[index - inputDataStartIndex]) {
                return false;
            }
        }
        return true;
    }

    private boolean isBoardData(byte[] inputFrameData) {
        byte[] boardData = new byte[6];
        for (int index = 0; index < 6; index++) {
            boardData[index] = (byte) 0xFF;
        }

        byte[] srcAddr = this.ethernetHeader.getEnetAddr().getSrcAddr();
        return this.checkTheFrameData(boardData, inputFrameData, 0)
                && !this.checkTheFrameData(srcAddr, inputFrameData, 6);
    }

    private boolean isMyConnectionData(byte[] inputFrameData) {
        byte[] srcAddr = this.ethernetHeader.getEnetAddr().getSrcAddr();
        byte[] dstAddr = this.ethernetHeader.getEnetAddr().getDstAddr();
        return this.checkTheFrameData(dstAddr, inputFrameData, 6)
                && this.checkTheFrameData(srcAddr, inputFrameData, 0);
    }

    //위에 3개 메소드로 Extract Method로 receive안에 있는 모든 기능을 함수로 뺴서 더 보기 편하게 하였다

    private boolean sendEhternetAck(byte[] inputData) {//ack 만들어서 ack receive에 넣어준다
        byte[] ackFrame = new byte[14];
        int index = 0;
        while (index < 6) {
            ackFrame[index] = inputData[index + 6];
            ackFrame[index + 6] = inputData[index];
            index += 1;
        }
        ackFrame[12] = 0x00;
        ackFrame[13] = 0x02;
        //원래 코드에서는 하위 계층으로 호출해서 보내야함
        return true;
    }
}