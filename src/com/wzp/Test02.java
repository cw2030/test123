package com.wzp;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Test02 {

    @Before
    public void setUp() throws Exception {}

    @Test
    public void test() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
        Bean u = new Bean();
        u.name = "wwww";
        u.d = new Date();
        
        String json = gson.toJson(u);
        
        System.out.println(json);
        System.out.println(json.startsWith("{"));
        Bean uu = gson.fromJson(json, Bean.class);
        System.out.println(Integer.class.toString());
        Date dd = gson.fromJson("\"2015-04-17 15:58:58.023\"", Date.class);
        String ss = "2015-04-17 15:58:58.023";
        System.out.println(gson.fromJson("\"" + ss +"\"", Date.class));
        System.out.println(dd);
        
        System.out.println(uu);
        
        Date d = new Date();
        
        String j = gson.toJson(d);
        System.out.println(gson.toJson(d));
        System.out.println(gson.fromJson(j, Date.class));
        
        String exp = "EXP:java.text.ParseException\u0001Unparseable date: \"2015-04-17 16:43:35.224\"";
        System.out.println(exp.startsWith("EXP:"));
    }
    
    class Bean{
        String name;
        
        Date d;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getD() {
            return d;
        }

        public void setD(Date d) {
            this.d = d;
        }

        @Override
        public String toString() {
            return "Bean [name=" + name + ", d=" + d + "]";
        }
        
        
    }

}
