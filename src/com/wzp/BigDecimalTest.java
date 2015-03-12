package com.wzp;

import java.math.BigDecimal;
import java.math.MathContext;

import org.junit.Test;

public class BigDecimalTest {

    @Test
    public void test(){
        MathContext mc = new MathContext(2);
        BigDecimal one = new BigDecimal(0.12f,mc);
        BigDecimal two = new BigDecimal(0.167f,mc);
        System.out.println(one + " : " + two);
        System.out.println(one.subtract(two,mc));
        
    }
}
