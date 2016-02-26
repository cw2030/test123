package encrypt.md5;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MD5Test {
    public static String content = "QT330001_thdsDesjm21;a01404227523a640400c34c926db703811e19b6239943796_ff88924d58a8af33cc0dc5466d3ab772_2;a01404227523a6405793afee53710233fa625748624c8eee_00f8f9de36e97d21_3;a01404227523a6405793afee53710233fa625748624c8eee_18a1999dfc35b9df_3;a01404227523a6405793afee53710233fa625748624c8eee_67eaff70db7df2da_2;a01404227523a6405793afee53710233fa625748624c8eee_8435b7cedee54423_3;a01404227523a6405793afee53710233fa625748624c8eee_f191872bfc9c0471_3;a01404227523a640d95fb3da631617c4a9adaf7e54907d37_763cfbd2b5d1a7f6_3;a01404227523a640d95fb3da631617c4a9adaf7e54907d37_a0719140473f4498_3;a01404227523a640d95fb3da631617c4a9adaf7e54907d37_c6d25483d17ef128_3";

    @Before
    public void setUp() throws Exception {
//        content = "QT330001_thdsDesjm21;a01404227523a640400c34c926db703811e19b6239943796_ff88924d58a8af33cc0dc5466d3ab772;a01404227523a6405793afee53710233fa625748624c8eee_00f8f9de36e97d21;a01404227523a6405793afee53710233fa625748624c8eee_18a1999dfc35b9df;a01404227523a6405793afee53710233fa625748624c8eee_67eaff70db7df2da;a01404227523a6405793afee53710233fa625748624c8eee_8435b7cedee54423;a01404227523a6405793afee53710233fa625748624c8eee_f191872bfc9c0471;a01404227523a640d95fb3da631617c4a9adaf7e54907d37_763cfbd2b5d1a7f6;a01404227523a640d95fb3da631617c4a9adaf7e54907d37_a0719140473f4498;a01404227523a640d95fb3da631617c4a9adaf7e54907d37_c6d25483d17ef128";
    }

    @Test
    public void test() throws Exception{
        MD5 md5 = new MD5();
        System.out.println(md5.md5("content", "GBK"));
        System.out.println(md5.md5toHex(content, "GBK"));
    }

}