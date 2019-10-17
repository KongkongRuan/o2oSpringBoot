package com.imooc.o2o.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PathUtil {

    /*
     * 根据不同的操作系统，设置储存图片文件不同的根目录
     */
    private static String seperator = System.getProperty("file.separator");
    
    private static String winPath;
    
	private static String linuxPath;
    
    private static String shopPath;
    @Value("${win.base.path}")
    public  void setWinPath(String winPath) {
		PathUtil.winPath = winPath;
	}
    @Value("${linux.base.path}")
	public  void setLinuxPath(String linuxPath) {
    	PathUtil.linuxPath = linuxPath;
	}
    @Value("${shop.relevant.path}")
	public  void setShopPath(String shopPath) {
		PathUtil.shopPath = shopPath;
	}
    public static String getImgBasePath() {

        String os =System.getProperty("os.name");
        String basePath = "";
        if(os.toLowerCase().startsWith("win")) {
          basePath = winPath;    //根据自己的实际路径进行设置
        }else {
            basePath = linuxPath;//根据自己的实际路径进行设置
        }
        basePath = basePath.replace("/", seperator);
        return basePath;
    }

    //根据不同的业务需求返回不同的子路径
    public static String getShopImagePath(long shopId) {
        String imagePath = shopPath+ shopId + seperator;
        return imagePath.replace("/", seperator);
    }
}