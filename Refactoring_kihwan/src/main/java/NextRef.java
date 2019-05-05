import lombok.Data;

public class NextRef {
    private EtherNetFrame ethernetHeader = new EtherNetFrame();

    //lombok으로 getter/setter 로 지정
    @Data
    private class EtherNetFrame {//객체명 변경
        EtherNetAddr enetDstAddr;//dst 정보
        EtherNetAddr enetSrcAddr;//src 정보
        byte[] enetType;
        byte[] enetData;

        public EtherNetFrame() {
            this.enetDstAddr = new EtherNetAddr();
            this.enetSrcAddr = new EtherNetAddr();
            this.enetType = new byte[2];
            this.enetData = null;
        }
    }

    @Data
    private class EtherNetAddr {
        private byte[] addr = new byte[6];

        public EtherNetAddr() {
            for (int indexOfAddr = 0; indexOfAddr < addr.length; ++indexOfAddr) {
                this.addr[indexOfAddr] = (byte) 0x00;
            }
        }
    }

    public boolean Receive(byte[] input) {//board cast랑 data check를 하는 것
        boolean checkIsBoard = this.isBoard(input);
        if ((checkIsBoard || this.checkMyConnection(input)) && input[12] == (byte) 0x00) {
            if (input[13] == (byte) 0x01) {
                byte[] removedHeaderData = this.removeCappHeaderData(input);
                if (checkIsBoard) {
                    System.out.println("sendAck");//다른 코드로 removedHeaderData를 써야 하지만 나머지 코드가 없어서 이렇습니다.
                } else {
                    this.sendEhternetAck();
                }
                return true;
            } else if (input[13] == (byte) 0x02) {
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
            headerAddedArray[index] = this.ethernetHeader.getEnetDstAddr().getAddr()[index];
            index += 1;
        }
        while (index < 12) {
            headerAddedArray[index] = this.ethernetHeader.getEnetSrcAddr().getAddr()[index - 6];
            index += 1;
        }
        headerAddedArray[index] = 0x00;
        headerAddedArray[index + 1] = 0x02;
        return true;
    }

    private boolean isBoard(byte[] input) {//브로드 케스팅인지 판별 -> 너무 길어져서 추가적을 빼서 작성한 코드
        for (int indexOfInputData = 0; indexOfInputData < 6; indexOfInputData++) {
            if (input[indexOfInputData] != (byte) 0xFF) {
                return false;
            }
        }
        return !this.checkMyEthernetBoardSrcAddress(6, input);
    }

    private boolean checkMyEthernetBoardSrcAddress(int startAddressIndex, byte[] message) { //src 정보를 board에 맞게 작성
        int indexOfMyAddressByteArrayIndex = 0;
        for (int indexOfStart = startAddressIndex; indexOfStart < startAddressIndex + 6; indexOfStart++) {
            if (message[indexOfStart] != this.ethernetHeader.getEnetSrcAddr().getAddr()[indexOfMyAddressByteArrayIndex]) {
                return false;
            }
            indexOfMyAddressByteArrayIndex++;
        }
        return true;
    }

    private boolean checkMyConnection(byte[] input) {//연결된 것과 동일한지 판별
        int index = 0;
        while (index < 6) {
            if (input[index] != this.ethernetHeader.getEnetSrcAddr().getAddr()[index]) {
                return false;
            }
            index += 1;
        }
        return this.checkMyEthernetDstAddress(6, input);
    }

    private boolean checkMyEthernetDstAddress(int startAddressIndex, byte[] message) {//Dst 확인하는코드 src랑 위치가 다른므로 따로 작성 비슷해 보이지만 index정보가 많이 다름
        int indexOfMyAddressByteArrayIndex = 0;
        for (int indexOfStart = startAddressIndex; indexOfStart < startAddressIndex + 6; indexOfStart++) {
            if (message[indexOfStart] != this.ethernetHeader.getEnetDstAddr().getAddr()[indexOfMyAddressByteArrayIndex]) {
                return false;
            }
            indexOfMyAddressByteArrayIndex++;
        }
        return true;
    }

    private byte[] removeCappHeaderData(byte[] input) {//header 제거 코드를 따로 빼서작성 receive에 너무 많은기능 구현을 피하기 위한 것
        byte[] removeCappHeader = new byte[14];
        System.arraycopy(input, 14, removeCappHeader, 0, 14);
        return removeCappHeader;
    }
}
