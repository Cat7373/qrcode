package org.cat73.qrcode.util;

/**
 * 字符串工具类
 */
public class Strings {
    protected Strings() {
        throw new UnsupportedOperationException();
    }

    /**
     * 判断字符序列是否为 null 或长度为 0
     * @param chs 被判断的字符序列
     * @return 判断结果
     */
    public static boolean isEmpty(CharSequence chs) {
        return chs == null || chs.length() == 0;
    }
}
