package com.mmkj.util;

import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.*;

/**
 * @author by jicai on 2019/7/29.
 */
public class WindowsUtil {

    public static  void setWindows(Frame frame){
        int windowWidth = frame.getWidth();
        int windowHeight = frame.getHeight();
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        frame.setLocation(screenWidth/2-windowWidth/2, screenHeight/2-windowHeight/2);
    }

    public static void addPic(Frame frame){
        try {
            ClassPathResource resource = new ClassPathResource("jarpic.png");
            Image image = ImageIO.read(resource.getInputStream());
            frame.setIconImage(image);
        }catch (Exception e){
            if(SwitchUtil.LOG){
                e.printStackTrace();
            }
        }
    }

    public static final Font FONT = new Font("宋体", Font.PLAIN, 16);
}
