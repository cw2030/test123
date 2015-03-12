package test123;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
        }
}