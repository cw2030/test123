package captcha;

import java.io.FileOutputStream;

public class CaptchaTest {

    public static void main(String[] args) throws Exception{
        Captcha captcha = new SpecCaptcha(150, 40,5);
        captcha.out(new FileOutputStream("d:/captchpng.png"));
        System.out.println(captcha.text());
        
        
        captcha = new GifCaptcha(150, 40, 5);
        captcha.out(new FileOutputStream("d:/catchaGif.gif"));
        System.out.println(captcha.text());

    }

}
