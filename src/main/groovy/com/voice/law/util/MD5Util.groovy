package com.voice.law.util

import org.springframework.util.DigestUtils

import java.text.SimpleDateFormat

/**
 * 描述 密码 md5 加密
 * @author zsd* @date 2020/1/9 3:09 下午
 */
class MD5Util {

    /**
     * md5 加密
     * @param str 待加密的字符串
     * @param slat 盐值
     * @return
     */
    static String getMD5(String str, String slat) {
        String base = str + slat
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes())
        return md5
    }

    /**
     * 明文密码与密文比较是否相等
     * @param plainText 明文
     * @param slat 盐值
     * @param cipherText 密文
     */
    static boolean compare(String plainText, String slat, String cipherText) {
        if (plainText == null && cipherText == null)
            return true
        if ((plainText == null && cipherText != null) || (plainText != null && cipherText == null))
            return false
        return cipherText == getMD5(plainText, slat)
    }

    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new FileReader("/Users/zsd/Downloads/shen_qin_chen.txt"))
        Scanner scanner = new Scanner(System.in)
        int i = 0
        String line
        while (true) {
            i++
            if (i >= 8117) {
                scanner.next()
            }
            line = br.readLine()
//            line = new String(line.getBytes("gb2312"), "utf-8")
            println(i + ":" + line)
            br.readLine()
        }
    }
}
