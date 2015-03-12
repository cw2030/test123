package guava;

import java.nio.charset.Charset;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder.SetMultimapBuilder;
import com.google.common.collect.Multimaps;

public class PreConditionsTest {

    @Test
    public void test(){
        String uname = "abc";
        
        Preconditions.checkNotNull(uname,"uname can't be null");
        System.out.println("ok");
        System.out.println(Strings.isNullOrEmpty(null));
        Charset utf8 = Charsets.UTF_8;
        Multimap<String, String> m = SetMultimapBuilder.linkedHashKeys().arrayListValues().build();
        
    }
}
