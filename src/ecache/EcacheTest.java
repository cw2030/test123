package ecache;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;

import org.junit.Before;
import org.junit.Test;

public class EcacheTest {

    @Before
    public void setUp() throws Exception {}

    @Test
    public void test() throws Exception{
        CacheManager cm = CacheManager.create();
        System.out.println(cm.getCacheNames());
        
        Cache mc = new Cache("testCache", 5000, false, false, 5, 2);
        cm.addCache(mc);
        mc.put(new Element("abcd", BigDecimal.TEN));
        
        Element el = mc.get("abcd");
        BigDecimal bd = (BigDecimal)el.getObjectValue();
        System.out.println(bd.intValue());
        
        mc.put(new Element("abcd", "test00001"));
        System.out.println(mc.get("abcd").getObjectValue());
    }

}
