package Traffic;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.Set;

public class BeforeBusDBtable {
    java.util.Map map = new java.util.HashMap();
    int size; //DB에 저장된 bus객체의 size
    Bus[] bus; // 버스객체들의 배열

    //생성한 버스객체들을 인자로 받아 hashmap에 넣기 위한 생성자
    public BeforeBusDBtable(Bus[] Busarray) {
        bus = Busarray;
        size = bus.length;
        for(int i = 0; i<size; i++) {
            map.put(bus[i].get_busNumber()+bus[i].get_finalstop().stopname, bus[i]);
        }
    }

    /*버스노선 정보 출력
     * 버스번호와 노선방향의 정보를 인자로 받아 hashmap에 주어진 정보와 일치하는 버스를 찾아 버스의 정보를 출력하는 메소드
     * */
    public void search_businformation(String busNumber, String finalstop) {
        Bus search_bus = (Bus) map.get(busNumber+finalstop);
        System.out.println("---------------------------------------------------------------------------"+busNumber+" bus information-------------------------------------------------------------------------------");
        System.out.printf("[%s]: %s\n","노선번호",search_bus.get_busNumber());
        System.out.printf("[%s]: %s\n","버스차번호", search_bus.get_carNumber());
        System.out.printf("[%s]: %s\n", "현재버스위치", search_bus.get_presentstop());
        System.out.printf("[%s]: %s\n","노선방향", search_bus.get_finalstop());
        System.out.printf("[%s]: %s\n","경유지", search_bus.stoplist);
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    /*버스정류장 정보를 검색하기 위한 메소드
     * hasmap에 저장되어 있는 버스들의 set을 검색하여 그버스들의 노선정보들을 iterator를 통해 해당 정류장과 일치하는 곳을 검색하는 메소드
     */
    public void search_busstop(String busstop) {
            Set<Bus> keyset_1 = map.keySet();
            Iterator<Bus> it_1 = keyset_1.iterator();

            String findbusstop = null;
            String[] stopnumber = new String[20];
            for(int i = 0; i<stopnumber.length; i++) {
                stopnumber[i] = null;
            }
            int check=0;
            while(it_1.hasNext()){
                Bus temp_1 = (Bus) map.get(it_1.next());
                ListIterator<Busstop> find = temp_1.stoplist.listIterator();
                while(find.hasNext()){
                    String str = find.next().stopname; //현재 탐색하고 있는 정류장의 이름을 str 변수에 저장.
                    String[] array = str.split(" "); //ex) 탄방역종점 31-080 -> 탄방역종점과 31-080 부분으로 나눔
                    /*동일한 일므을 갖고 있는 정류장들의 고유번호를 저장하는 배열 stopnumber에 저장하는 부분
                     * array[0]이 탐색 정류장 이름과 동일하다면 array[1]에 중복되지 않게 고유번호를 저장.
                     * 고유번호가 없는 정류장은 NIL로 표기하도록 구현함.
                     * */
                    if(array[0].equals(busstop)) {
                        String search;
                        if(array.length==1)
                            search = "NIL";
                        else {
                            search = array[1];
                        }

                        int k=0;
                        for(int j = 0; j<=check; j++) {
                            if(stopnumber[j] != null && stopnumber[j].equals(search)) {
                                k++;
                            }
                        }
                        if(k == 0) {
                            stopnumber[check] = search;
                            check++;
                        }
                    }
                }
            }
            /*버스정류장이 탐색되지 않을 시 예외처리*/
            if(check<1) {
                System.out.println("찾으시는 정류장의 정보는 없습니다.");
                return;
            }
        /*사용자로부터 탐색한 정류장 고유번호를 보여주고 검색하고자하는 정류장을 선택*/
        System.out.print("찾고자하는 정류장 번호");
        for(int g = 0; g<check; g++) {
            System.out.print("["+g+"]"+stopnumber[g]+" ");
        }System.out.print(":");
        Scanner input = new Scanner(System.in);
        int number =  Integer.parseInt(input.nextLine());
        if(stopnumber[number].equals("NIL")) {
            findbusstop = busstop;
        }else {
            findbusstop = busstop+" "+stopnumber[number];
        }
        searchBus(findbusstop);
    }

    public void searchBus(String findbusstop) {

        int check_count = 0;
        /*버스 정류장의 정보를 탐색하기 위한 부분*/
        Set<Bus> keyset_2 = map.keySet();
        Iterator<Bus> it_2 = keyset_2.iterator();
        while(it_2.hasNext()){
            Bus temp = (Bus) map.get(it_2.next());
            ListIterator<Busstop> li = temp.stoplist.listIterator();
            int count = 0;//검색하고자하는 정류장과 현재 버스의 위치의 offset을 계산하기 위한 변수
            while(li.hasNext()){
                if(li.next().stopname.equals(findbusstop)) {
                    check_count++;
                    Busstop presentstop = temp.get_presentstop();
                    Busstop nextstop = temp.get_nextstop();
                    int presentstoplistnumber = temp.get_presentstoplistnumber();
                    int offset = count-presentstoplistnumber-1; //현재 검색하고자하는 정류장과 현재 버스의 위치의 offset
                    int k = (int)(temp.get_time_2()-temp.get_time_1())/1000;  //현재 버스의 위치를 가져올 때 시간과 버스의 정류장위치를 갱신한 시간의 차이(이 가상 시뮬레이션에서는 변화를 빠르게 확인하기 위해 정류장간의 도착 시간단위를 초단위로 수행함)
                    int r_time_1 = (temp.get_timecount() - k); //다음 정류장까지 남은 시간(초단위)
                    int r_time_2 = r_time_1+(offset*temp.get_timecount());//현재 검색하고자하는 정류장이 버스의 현재위치와 인접하지 않다면 남은 시간(초단위)


                    if(presentstoplistnumber == 0) {
                        System.out.printf("출고지에서 출발중입니다.\n");//현재 버스의 위치가 출고지일 때
                    }else{
                        if(offset <= 0) {
                            System.out.printf("준비중입니다...\n");//현재 검색하고자하는 정류장을 지나쳤을 때
                        }
                        //현재 버스의 위치가 검색하고자하는 정류장 바로 전 정류장을 지나쳤을 때
                        else if(offset == 1){
                            if(r_time_1<=1)//전정류장에서 출발한지 1정도 지났을 때
                                System.out.printf("%s[종점:%s] : 현재 %s 정류장에서 출발했습니다.\n", temp.get_busNumber(), temp.get_finalstop(), presentstop);
                            else if(r_time_1<=2)//현재 검색한 정류장의 위치에 도착하기 2정도 남았을 때
                                System.out.printf("%s[종점:%s] : 현재 정류장에 진입중입니다.\n", temp.get_busNumber(), temp.get_finalstop());
                            else//전 정류장에서 출발했지만 현재 정류장까지 아직 2보다 많이 남았을 때
                                System.out.printf("%s[종점:%s] : 현재 정류장까지 %d 남았습니다.\n",temp.get_busNumber(), temp.get_finalstop(), r_time_1);
                        }else {// 현재 정류장까지 아직 많이 남았을 때
                            System.out.printf("%s[%s방향] : 현재 정류장까지 %d 남았습니다.\n",temp.get_busNumber(), temp.get_finalstop(), r_time_2);
                        }
                    }
                }
                count++;
            }
        }

        if(check_count == 0) {
            System.out.println("찾으시는 정류장의 정보는 없습니다.");
            return;
        }
        return;
    }


    //현재 버스 DB에 저장되어 있는 버스들의 노선 번호를 보여주기 위한 메소드
    public void show() {
        System.out.println("****************************************************************************************************************************************************************");
        for(int i = 0; i<size; i++) {
            if(i%2==0)
                continue;
            System.out.printf("%-10s",bus[i].get_busNumber());
            if((i+1)%15 == 0) {
                System.out.println();
            }
        }
        System.out.println();
        System.out.println("****************************************************************************************************************************************************************");
    }


}
