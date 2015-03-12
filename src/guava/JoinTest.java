package guava;

import java.util.List;
import java.util.Objects;

import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public class JoinTest {

    @Test
    public void test(){
        List<String> list = Lists.newArrayList("1","b","c");
        System.out.println(Joiner.on(",").join(list));
        List<String> result = Splitter.on(",").splitToList(Joiner.on(",").join(list));
        
    }
}
