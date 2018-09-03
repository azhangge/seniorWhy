package com.nd.auxo.recommend.core.util;

import lombok.Data;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文字工具类
 * Created by xzy on 15-1-19.
 */
public class WordUtils {

    private static Pattern PATTERN_PHONE = Pattern.compile("^(1)\\d{10}$");
    private static Pattern PATTERN_EMAIL = Pattern.compile("^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$");
    private static Pattern PATTERN_IDCARD = Pattern.compile("^((1[1-5])|(2[1-3])|(3[1-7])|(4[1-6])|(5[0-4])|(6[1-5])|71|(8[12])|91)\\d{15}(\\d|X|x)$");
    private static Pattern PATTERN_HOME_IDCARD = Pattern.compile("^([MmHh])(\\d{8})(\\d{2})?$");
    private static Pattern PATTERN_ARMY_IDCARD = Pattern.compile("^([\\u4E00-\\u9FA5]|[\\u4E00-\\u9FA5]{2})(\\u5B57)?(\\u7B2C)?(\\d{2})(-)?(\\d{4,6})(\\u53F7)?$");
    private static final Logger LOGGER = LoggerFactory.getLogger(WordUtils.class);
    /**
     * 字数限制(一个中文字算2个字节)
     *
     * @param value
     * @param maxLength
     * @return true:超过最大字数
     */
    public static boolean isOutOfMaxLength(String value, Long maxLength) {
        if (value == null) {
            value = "";
        }
        String str = StringUtils.trimWhitespace(value);
        int strLength = str.length();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) > 127) {
                strLength++;
            }
        }
        return strLength > maxLength;
    }

    /**
     * 对html格式的字符串做转义(img标签除外)
     *
     * @param str
     * @return
     */
    public static String escapeHtmlWithImg(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        Pattern p = Pattern.compile("<img.*?>");
        Matcher m = p.matcher(str);
        StringBuffer resultStr = new StringBuffer();
        int lastStartPos = 0;
        //遍历所有匹配的img标签
        while (m.find()) {
            if (m.start() != 0) {
                //将上一次img标签和这次img标签之间的字符串做转义,保存到结果字符串
                resultStr.append(escapeHtml(str.substring(lastStartPos, m.start())));
            }
            //将img标签保存到结果字符串
            resultStr.append(m.group());
            lastStartPos = m.end();
        }
        if (lastStartPos != str.length()) {
            //将最后一个img标签之后的字符串做转义,保存到结果字符串
            resultStr.append(escapeHtml(str.substring(lastStartPos, str.length())));
        }
        return resultStr.toString();
    }

    public static String escapeHtml(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        String tempStr = str;
        tempStr = tempStr.replaceAll("&", "&amp;");
        tempStr = tempStr.replaceAll("<", "&lt;");
        tempStr = tempStr.replaceAll(">", "&gt;");
        tempStr = tempStr.replaceAll(" ", "&nbsp;");
        tempStr = tempStr.replaceAll("\"", "&quot;");
        return tempStr;
//        return StringEscapeUtils.escapeHtml(str);
    }

    /**
     * 对转义后的html格式的字符串做恢复
     *
     * @param str
     * @return
     */
    public static String unEscapeHtml(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
//        str = str.replaceAll("&amp;", "&");
//        str = str.replaceAll("&lt;", "<");
//        str = str.replaceAll("&gt;", ">");
//        str = str.replaceAll("&nbsp;", " ");
//        str = str.replaceAll("&quot;", "\"");
        return StringEscapeUtils.unescapeHtml(str);
    }

    /**
     * 去除字符串中的html标签
     *
     * @param str
     * @return
     */
    public static String removeHtml(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        str = str.replaceAll("</?[^<]+>", "");
        return str;
    }

    /**
     * 将公式和图片替换为 [公式] [图片]字样
     *
     * @param src
     * @return
     */
    public static String replaceImageAndFormula(String src) {
        return unEscapeHtml(removeHtml(src.replaceAll("<img*[^>]*imgtype=\"2\".*?/>", "[ 公式 ]").replaceAll("<img*[^>]*src=.*?/>", "[ 图片 ]")));
    }

    /**
     * 去除字符串中的html标签(保留img标签)
     *
     * @param str
     * @return
     */
    public static String removeHtmlIgnoreImg(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        str = str.replaceAll("<(?!/?img)/?[^<]+>", "");
        return str;
    }

    /**
     * 校验证件格式是否符合规范
     *
     * @param card
     * @return
     */
    public static boolean validCardFormat(String card) {
        if (PATTERN_IDCARD.matcher(card).find()) {
            //身份证
            return validIdCardFormat(card);
        } else if (PATTERN_ARMY_IDCARD.matcher(card).find()) {
            //军官证
            return true;
        } else if (PATTERN_HOME_IDCARD.matcher(card).find()) {
            //回乡证
            return true;
        }
        return false;
    }

    /**
     * 校验手机格式是否符合规范
     *
     * @param phone
     * @return
     */
    public static boolean validPhone(String phone) {
        return PATTERN_PHONE.matcher(phone).find();
    }

    /**
     * 校验邮箱格式是否符合规范
     *
     * @param email
     * @return
     */
    public static boolean validEmail(String email) {
        return PATTERN_EMAIL.matcher(email).find();
    }

    /**
     * 校验账号格式是否符合规范
     *
     * @param account
     * @return
     */
    public static boolean validAccount(String account) {
        Boolean result = true;
        if (PATTERN_IDCARD.matcher(account).find()) {
            //身份证
            result = false;
        } else if (PATTERN_ARMY_IDCARD.matcher(account).find()) {
            //军官证
            result = false;
        } else if (PATTERN_HOME_IDCARD.matcher(account).find()) {
            //回乡证
            result = false;
        } else if (validEmail(account)) {
            //邮箱
            result = false;
        } else if (validPhone(account)) {
            //手机
            result = false;
        }

        return result;
    }

    /**
     * 校验身份证格式是否符合规范
     *
     * @param idCard
     * @return
     */
    public static boolean validIdCardFormat(String idCard) {

        if (idCard == null) {
            return false;
        }
        if (idCard.length() != 18) {
            return false;
        }
        String dateStr = idCard.substring(6, 14);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        String sDate = sdf.format(date);
        if (!sDate.equals(dateStr)) {
            return false;
        }

        String idCard17 = idCard.substring(0, 17);
        String idCardLast = idCard.substring(17, 18).toLowerCase();
        long sum = 0;
        for (int i = idCard17.length() - 1, j = 0; i >= 0; i--, j++) {
            long temp = Long.parseLong(idCard17.substring(i, i + 1)) * idCardWeight(j);
            sum += temp;
        }
        int checkVal = (int) (12 - (sum % 11)) % 11;
        String ckValStr = checkVal == 10 ? "x" : String.valueOf(checkVal);
        return ckValStr.equals(idCardLast);
    }

    private static long idCardWeight(long w) {
        return ((long) Math.pow(2, w + 1)) % 11;
    }

    /**
     * 获取证件号存储的字符串
     */
    public static String getIDCardStoreStr(String idCard) {
        if (PATTERN_IDCARD.matcher(idCard).find()) {
            //身份证
            return idCard.toUpperCase();
        } else if (PATTERN_ARMY_IDCARD.matcher(idCard).find()) {
            //军官证
            return PATTERN_ARMY_IDCARD.matcher(idCard).replaceAll("$1$4$6").toUpperCase();
        } else if (PATTERN_HOME_IDCARD.matcher(idCard).find()) {
            //回乡证
            return PATTERN_HOME_IDCARD.matcher(idCard).replaceAll("$1$2").toUpperCase();
        }
        return idCard;
    }

    /*按特定的编码格式获取长度*/
    public static int getWordCountByCode(String str, String code) {
        int length = 0;
        try {
            length = str.getBytes(code).length;
        } catch (IOException e) {
            LOGGER.debug(" ", e);
//            e.printStackTrace();
        }
        return length;
    }

    /**
     * 获取指定字节数的字符串 汉字算3个字符（UTF-8）
     *
     * @param string
     * @param j
     * @return
     */
    public static String getWordInLength(String string, int j) {
        String encode = "UTF-8";
        byte[] buf;
        String s = null;
        if (getWordCountByCode(string, encode) <= j) return string;
        try {
            buf = string.getBytes(encode);
            int count = 0;
            int i;
            for (i = j - 1; i >= 0; i--) {
                if (buf[i] < 0)
                    count++;
                else
                    break;
            }
            if (count % 3 == 0)
                s = new String(buf, 0, j, encode);
            else if (count % 3 == 1)
                s = new String(buf, 0, j - 1, encode);
            else if (count % 3 == 2)
                s = new String(buf, 0, j - 2, encode);
        } catch (IOException e) {
            LOGGER.debug(" ", e);
//            e.printStackTrace();
        }
        return s;
    }
}
