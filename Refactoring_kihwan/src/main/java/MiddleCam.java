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
    private class EtherNetAddr {
        private byte[] dstAddr = new byte[6];
        private byte[] srcAddr = new byte[6];

        private EtherNetAddr() {
            for (int indexOfAddr = 0; indexOfAddr < 6; ++indexOfAddr) {
                this.dstAddr[indexOfAddr] = (byte) 0x00;
                this.srcAddr[indexOfAddr] = (byte) 0x00;
            }
        }
    }

    public boolean Receive(byte[] input) {
        boolean isBroad = true;
        for (int indextemp = 0; indextemp < 6; indextemp++) {
            if (input[indextemp] != (byte) 0xFF) {
                isBroad = false;
            }
        }
        if (isBroad && input[12] == (byte) 0x00) {
            if (input[13] == 0x01) {
                byte[] removeCapHeader = new byte[input.length - 14];
                System.arraycopy(input, 14, removeCapHeader, 0, removeCapHeader.length);
                System.out.println("send broad casting Ack");
                this.sendEhternetAck();
                return true;
            } else if (input[13] == 0x02) {
                System.out.println("receive ack so I send other frame broadcast");
                return false;//recive 안함 -> ack 받음 -> 다음 frame을 날림
            }
        }

        int index = 0;
        while (index < 6) {
            if (input[index] != this.ethernetHeader.enet_srcaddr.getAddrData(index)) {
                System.out.println("fail At EtherNet");
                return false;
            }
            index += 1;
        }
        while (index < 12) {
            if (input[index] != this.ethernetHeader.enetAddr.getAddrData(index - 6)) {
                System.out.println("fail At EtherNet");
                return false;
            }
            index += 1;
        }

        if (input[index] == 0x00) {//처음 값이 동일
            if (input[index + 1] == 0x01) {//data -> ack 생성 및 보내기
                byte[] removeCapHeader = new byte[input.length - 14];//header 제거
                System.arraycopy(input, 14, removeCapHeader, 0, removeCapHeader.length);
                this.sendEhternetAck();
                System.out.println("send ack and accept data from other");
                return true;
            } else if (input[index + 1] == 0x02) {
                System.out.println("receive ack so I send other frame");
                return false;// ack이므로 상위 계층에 다음 frame 보내도록 준비만 해주게 한다.
            }
        }
        return false;
    }

    private boolean sendEhternetAck() {//ack 만들어서 ack receive에 넣어준다
        byte[] headerAddedArray = new byte[14];
        int index = 0;
        while (index < 6) {
            headerAddedArray[index] = this.ethernetHeader.enetAddr.getAddrData(index);
            index += 1;
        }
        while (index < 12) {
            headerAddedArray[index] = this.ethernetHeader.enet_srcaddr.getAddrData(index - 6);
            index += 1;
        }
        headerAddedArray[index] = 0x00;
        headerAddedArray[index + 1] = 0x02;
        return true;
    }


}