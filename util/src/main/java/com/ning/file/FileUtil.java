package com.ning.file;

import java.io.*;

public class FileUtil {
    public static void splitFile(String filePath, String outputDirectory, int chunks,String suffix) throws IOException {
        File inputFile = new File(filePath);
        long inputFileSize = inputFile.length();
        long chunkSize = inputFileSize / chunks;

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile))) {
            byte[] buffer = new byte[(int) chunkSize];
            int bytesRead;
            int index = 1;

            while ((bytesRead = bis.read(buffer)) > 0 && index <= chunks) {
                String chunkFileName = outputDirectory + File.separator + "part" + index + "." + suffix;
                try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(chunkFileName))) {
                    bos.write(buffer, 0, bytesRead);
                }
                index++;
            }
        }
    }

    public static void main(String[] args) {
        String inputFilePath = "E:\\temp\\log\\catalina.out"; // 替换为你的文件路径
        String outputDirectory = "E:\\temp\\log"; // 替换为你的输出目录
        int chunks = 30; // 分割成10个文件

        try {
            splitFile(inputFilePath, outputDirectory, chunks,"out");
            System.out.println("文件分割成功。");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
