package com.cnbot.irobotvoice.utils;


import java.io.File;
import java.io.FileOutputStream;

/**
 * 通用工具类
 *
 * @author PYZ
 */

/**
 * 通用工具类
 * @author PYZ
 */
public class ByteUtils {


    /**
     * 连续保存文件
     * @param path 文件路径
     * @return 数据是否结束
     */
    private static FileOutputStream fileOutputStream = null;
    public static boolean saveFileToByte(String path,String name,byte[] data, boolean isEnd){

        boolean ret = false;
        try{
            //创建路径
            File file = new File(path);
            file.mkdirs();
            file = new File(path,name);

            if (fileOutputStream == null) {
                fileOutputStream = new FileOutputStream(file);
            }
            if (data != null && data.length > 0) {
                fileOutputStream.write(data);
                fileOutputStream.flush();
            }
            if (data == null || isEnd){
                // 如果数据长度小于8k，则任务数据已传输完毕，存在一个文件的最后一帧数据刚好8k ,则继续获取下一帧，通过下一帧的getData是否为null作为最有一帧的依据
                fileOutputStream.close();
                fileOutputStream = null;
                ret = true;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }

}