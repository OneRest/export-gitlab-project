
package com.demo.project.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtils {

    public static void main(String[] args) {
        List<String> list = java.util.Arrays.asList("a", "b", "c", "c", "d", "f", "a");

        Stream.iterate(0, i -> i + 1).limit(list.size()).forEach(i -> {

            System.out.println(String.valueOf(i) + list.get(i));
        });

    }

    /**
     * @Description:读取文件
     * @param: @param filePath 文件路径
     * @param: @return
     * @author: weijiashang
     * @date: 2018年12月11日 上午11:20:41
     */
    public static List<String> readFileIntoStringArrList(String filePath) {
        List<String> list = new ArrayList<String>();
        try {
            File file = new File(filePath);
            if(file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), "utf-8");
                BufferedReader bufferedReader = new BufferedReader(read);
                String line = null;
                while((line = bufferedReader.readLine()) != null) {
                    list.add(line);
                }
                bufferedReader.close();
                read.close();
            } else {
                log.debug("找不到指定的文件");
            }
        } catch(Exception e) {
            log.error("读取文件内容出错" + e);
        }
        return list;
    }

    /**
     * @Description:
     * @param: @param fileNamePath 文件名路径
     * @param: @param content 写入内容
     * @param: @param isAppend 是否在旧文件上追加内容
     * @author: weijiashang
     * @date: 2018年12月10日 下午4:42:15
     */
    public static void writeFile(String fileNamePath, String content, boolean isAppend) {
        File file = new File(fileNamePath);
        // 数据追加
        if(isAppend) {
            // 如果没有文件就创建
            if(!file.isFile()) {
                try {
                    file.createNewFile();
                } catch(IOException e) {
                    log.error("创建文件异常：" + e);
                }
            }
            FileWriter writer = null;
            try {
                writer = new FileWriter(fileNamePath, true);
                writer.write(content + "\r\n");
            } catch(IOException e) {
                log.error("数据写入文件异常：" + e);
            } finally {
                try {
                    writer.close();
                } catch(IOException e) {
                    log.error("close异常：" + e);
                }
            }
        } else {
            if(file.isFile()) {
                // 如果有文件存在，则删除
                file.delete();
            }
            try {
                file.createNewFile();
            } catch(IOException e) {
                log.error("创建文件异常：" + e);
            }

            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(fileNamePath));
                writer.write(content + "\r\n");
            } catch(IOException e) {
                log.error("写入文件异常" + e);
            } finally {
                try {
                    writer.close();
                } catch(IOException e) {
                    log.error("close异常：" + e);
                }
            }
        }
    }

    public static void writeFileCSV(String fileNamePath, String content, boolean isAppend) {
        File file = new File(fileNamePath);
        // 数据追加
        if(isAppend) {
            // 如果没有文件就创建
            if(!file.isFile()) {
                try {
                    file.createNewFile();
                } catch(IOException e) {
                    log.error("创建文件异常：" + e);
                }
            }
            FileWriter writer = null;
            try {
                writer = new FileWriter(fileNamePath, true);
                writer.write(content + "\r\n");
            } catch(IOException e) {
                log.error("数据写入文件异常：" + e);
            } finally {
                try {
                    writer.close();
                } catch(IOException e) {
                    log.error("close异常：" + e);
                }
            }
        } else {
            if(file.isFile()) {
                // 如果有文件存在，则删除
                file.delete();
            }
            try {
                file.createNewFile();
            } catch(IOException e) {
                log.error("创建文件异常：" + e);
            }

            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(fileNamePath));
                writer.write(content + "\r\n");
            } catch(IOException e) {
                log.error("写入文件异常" + e);
            } finally {
                try {
                    writer.close();
                } catch(IOException e) {
                    log.error("close异常：" + e);
                }
            }
        }
    }

}
