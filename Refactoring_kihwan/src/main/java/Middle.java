import lombok.Data;

public class Middle {
    private EtherNetFrame _ethernetHeader = new EtherNetFrame();

    //lombok으로 getter/setter 로 지정
    @Data
    private class EtherNetFrame {//객체명 변경
        EtherNetAddr enetAddr;//dst 정보
        byte[] enetType;
        byte[] enetData;

        public EtherNetFrame() {
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


}
