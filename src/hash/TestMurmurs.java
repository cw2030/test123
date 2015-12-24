package hash;

public class TestMurmurs {

    public static void main(String[] args) throws Exception{
        /*assertEquals(Murmurs.hashUnsigned("chenshuo").toString(),"5016608279269930526");
        assertEquals(Murmurs.hashUnsigned("shaoguoqing").toString(),"17121371936686143062");
        assertEquals(Murmurs.hashUnsigned("baozenghui").toString(),"5451996895512824982");
        assertEquals(Murmurs.hashUnsigned("05ff62ff6f7749ffff43ffff6673ff65").toString(),"10652549470333968609");
        assertEquals(Murmurs.hashUnsigned("hahahaha").toString(),"15134676900169534748");
        assertEquals(Murmurs.hashUnsigned("hahah1369531321aha5466sfdfaerttedddd56da").toString(),"6439159232526071817");
        assertEquals(Murmurs.hashUnsigned("测试汉字").toString(),"1146745369200541601");*/

        System.out.println(Murmurs.hashUnsigned("sessions/1").longValue()%5);
        System.out.println(Murmurs.hash("sessions/1".getBytes("utf-8")));
        System.out.println(Murmurs.hashUnsigned("sessions/2").longValue()%1024l);
        System.out.println(Murmurs.hashUnsigned("sessions/3").longValue()%1024l);
        System.out.println(Murmurs.hashUnsigned("sessions/4").longValue()%1024l);
    }

}
