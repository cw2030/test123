package encrypt;

import java.util.Random;
import java.util.UUID;

import com.google.common.base.Strings;

/**
 * 根据browserId生成唯一标识
 * @author user
 *
 */
public class CommonTokens {
    public static char[] chars = new char[]{'H','b','E','c','A','d','F','g','K','m'};

    /**
     * 生成token
     * @param browserId
     * @return
     */
    public static String generate(String browserId){
        try{
            Random random = new Random();
            String[] bs = browserId.split("-");
            String content = browserId;
            if(bs.length > 4){
                content = bs[3] + bs[0];
            }
            int key = random.nextInt(10);
            char cc = chars[key];
            return cc + (key + AES.encrypt(content, key));
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * 检查token有效性
     * @param browserId
     * @param token
     * @return
     */
    public static boolean check(String browserId,String token){
        if(Strings.isNullOrEmpty(browserId) || Strings.isNullOrEmpty(token)){
            return false;
        }
        String keyStr = token.substring(1, 2);
        String _token = token.substring(2);
        String[] bs = browserId.split("-");
        String content = browserId;
        if(bs.length > 4){
            content = bs[3] + bs[0];
        }
        try{
            String temp = AES.encrypt(content, Integer.valueOf(keyStr));
            if(_token.equals(temp)){
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        
        return false;
    }
    
    public static void main(String[] args){
        String browserId = UUID.randomUUID().toString();
        System.out.println("browserId:" +browserId);
        String token = generate(browserId);
        System.out.println("token: " + token);
        System.out.println("check result: " + check(browserId, token));
    }
}
