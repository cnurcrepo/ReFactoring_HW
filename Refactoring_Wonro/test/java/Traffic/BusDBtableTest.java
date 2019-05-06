package Traffic;


import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class BusDBtableTest{
    BusDBtable DB;
    Bus[] bus = new Bus[2];
    String[] stopnumber = new String[20];
    @Before
    public void Insert(){
    Busstop busstop_1 = new Busstop("목원대학교 33-230");
    Busstop busstop_2 = new Busstop("흥도초등학교 41-810");
    Busstop busstop_3 = new Busstop("도안고등학교 45-520");
    Busstop busstop_4 = new Busstop("트리풀시티9단지 45-890");
    Busstop busstop_5 = new Busstop("도안센트럴시티 45-500");
    Busstop busstop_6 = new Busstop("도안고등학교 45-510");
        bus[0] = new Bus("106");
        bus[1] = new Bus("106");
        bus[0].stoplist.add(busstop_1);
        bus[0].stoplist.add(busstop_2);
        bus[0].stoplist.add(busstop_3);
        bus[1].stoplist.add(busstop_4);
        bus[1].stoplist.add(busstop_5);
        bus[1].stoplist.add(busstop_6);
        bus[0].set_finalstop(busstop_3);
        bus[1].set_finalstop(busstop_6);
        DB = new BusDBtable(bus);

    }

    @Test
    public void testInsert() {
        assertThat(bus[0].stoplist.get(0).stopname, is("목원대학교 33-230"));
        assertThat(bus[0].stoplist.get(1).stopname, is("흥도초등학교 41-810"));
        assertThat(bus[0].stoplist.get(2).stopname, is("도안고등학교 45-520"));
        assertThat(bus[1].stoplist.get(0).stopname, is("트리풀시티9단지 45-890"));
        assertThat(bus[1].stoplist.get(1).stopname, is("도안센트럴시티 45-500"));
        assertThat(bus[1].stoplist.get(2).stopname, is("도안고등학교 45-510"));
    }


    @Test
    public void testInitStopNumber(){
        DB.initStopNumber(stopnumber);
        for(int i = 0; i< stopnumber.length; i++)
         assertThat(stopnumber[i], is(nullValue()));
    }

    @Test
    public void testHashmapSearchingBusstopInfo(){
        DB.hashmapSearchingBusstopInfo("도안고등학교", stopnumber);
        assertTrue(stopnumber[0].equals("45-520") || stopnumber[0].equals("45-510"));
        assertTrue(stopnumber[1].equals("45-520") || stopnumber[1].equals("45-510"));
    }

    @Test
    public void testSelectBusstopIndex(){
        DB.hashmapSearchingBusstopInfo("도안고등학교", stopnumber);
        String selectbusstop = DB.selectBusStopInfo("도안고등학교",1, stopnumber);
        assertTrue(selectbusstop.equals("도안고등학교 45-520") || selectbusstop.equals("도안고등학교 45-510"));
    }










}
