package test123;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.google.gson.Gson;

public class Test{

        public static void main(String[] args) {
                try {
                    String match = "/(portal|fund|user)/.*";
                    String uri = "/user/addTopicApproval.do";
                    System.out.println(uri.matches(match));
                        InetAddress host = InetAddress.getLocalHost();
                        System.out.println(host.getHostName() + "::" + host.getHostAddress());
                } catch (UnknownHostException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                }
                String str = "/cabc";
                String regex = "/[(iandex)|(ab)].*";
                System.out.println(str.matches(regex));
                System.out.println("--------------------");
                Method[] ms = Test.class.getMethods();
                Method m = null;
                for (Method method : ms) {
                    if(method.getName().equals("t")){
                        m = method;
//                        break;
                    }
                    Type[] tps = method.getGenericParameterTypes();
                    System.out.println("method=" + method.getName());
                    for (Type type : tps) {
                        System.out.println("type=" + type.toString());
                        System.out.println(type.getClass().getName());
                        System.out.println("++++++++++++++++++++++");
                    }
                }
                Test test = new Test();
                Type[] tps = m.getGenericParameterTypes();
                Object[] params = new Object[5];
                byte b = 32;
                Gson g = new Gson();
                params[0] = g.fromJson("1", tps[0]);
                params[1] = g.fromJson("string2", tps[1]);
                params[2] = g.fromJson("false", tps[2]);
                params[3] = g.fromJson("9", tps[3]);
                params[4] = g.fromJson("" + b, tps[4]);
                /*try {
                    m.invoke(test,params);
                }
                catch (Exception e) { 
                    e.printStackTrace();
                }*/
                System.out.println(String.class.toString());
                System.out.println(BigDecimal.class.toString());
                
                Gson gson = new Gson();
                String json = gson.toJson(new BigDecimal("231"));
                System.out.println(json);
                System.out.println(gson.fromJson(json, BigDecimal.class));
        }
        
        
        public void t(int a,String b,boolean c,Integer d,byte e,byte[] bs,String[] df,BigDecimal h){
            System.out.println(a+b+c+d+e);
        }
}