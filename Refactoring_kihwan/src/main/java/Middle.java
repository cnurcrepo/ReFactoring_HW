import lombok.Data;

public class Middle {
    private EtherNetFrame _ethernetHeader = new EtherNetFrame();

    //lombok으로 getter/setter 로 지정
    @Data
    private class EtherNetFrame {//객체명 변경
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
    private class EtherNetAddr {
        private byte[] srcAddr = new byte[6];
        private byte[] dstAddr = new byte[6];

        public EtherNetAddr() {
            for (int indexOfAddr = 0; indexOfAddr < srcAddr.length; ++indexOfAddr) {
                this.srcAddr[indexOfAddr] = (byte) 0x00;
                this.dstAddr[indexOfAddr] = (byte) 0x00;
            }
        }
    }

    public boolean receiveFrameData(byte[] inputData) {//다름 class에서 호출해야함
        if (this.isBoardData(inputData) || this.isMyConnectionData(inputData)) {
            if (inputData[13] == (byte) 0x01) {
                this.sendEthernetAck(inputData);
                byte[] removeHeaderData = removeCappHeaderData(inputData);
                return true;// 원래 코드에서는 상위 layer에 data보내야 하는데 일부분이라서 생략헀습니다.
            } else if (inputData[13] == (byte) 0x02) {//ack이므로 상위 계층에 알리고 false return
                return false;
            }
        }
        return false;//내가 원한 frame이 아니다
    }

    private boolean sendEthernetAck(byte[] inputData) {
        byte[] ackFrame = new byte[14];
        int index = 0;
        while (index < 6) {
            ackFrame[index] = inputData[index + 6];
            ackFrame[index + 6] = inputData[index];
            index += 1;
        }
        ackFrame[12] = 0x00;
        ackFrame[13] = 0x02;
        return true;
    }

    private boolean checkTheFrameData(byte[] myAddressData, byte[] inputFrameData, int inputDataStartIndex) {
        for (int index = inputDataStartIndex; index < inputDataStartIndex + 6; index++) {
            if (inputFrameData[inputDataStartIndex] != myAddressData[index - inputDataStartIndex]) {
                return false;
            }
        }
        return true;
    }

    private boolean isBoardData(byte[] inputFrameData) {// broad인지 판별 해주는 메소드
        byte[] boradData = new byte[6];
        for (int index = 0; index < 6; index++) {
            boradData[index] = (byte) 0xFF;
        }
        byte[] srcAddr = this._ethernetHeader.getEnetAddr().getSrcAddr();
        return this.checkTheFrameData(boradData, inputFrameData, 0)
                && !this.checkTheFrameData(srcAddr, inputFrameData, 6);
    }

    private boolean isMyConnectionData(byte[] inputFrameData) {//연결된 data인지 판별 해주는 역할
        byte[] srcAddr = this._ethernetHeader.getEnetAddr().getSrcAddr();
        byte[] dstAddr = this._ethernetHeader.getEnetAddr().getDstAddr();
        return this.checkTheFrameData(srcAddr, inputFrameData, 0)
                && this.checkTheFrameData(dstAddr, inputFrameData, 6);
    }

    private byte[] removeCappHeaderData(byte[] input) {//header 제거 코드를 따로 빼서작성 receive에 너무 많은기능 구현을 피하기 위한 것
        byte[] removeCappHeader = new byte[14];
        System.arraycopy(input, 14, removeCappHeader, 0, 14);
        return removeCappHeader;
    }
}