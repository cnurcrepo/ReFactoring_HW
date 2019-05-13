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

    public boolean receiveFrameData(byte[] inputData) {

        return false;
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
}
