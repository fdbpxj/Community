package life.majian.community.test;

public class StringTest {
    //  1、字符串转化（压缩） “aabbccdaa” -> “a2b2c2d1a2”/
    public String strTransform(String str) {
        char[] chars = str.toCharArray();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(chars[0]);
        //计数下标
        int count = 0;
        //字符串判断
        int temp = chars[0];
        for (int i = 0; i < chars.length; i++) {
            if (temp == chars[i]) {
                count++;
            } else {
                temp = chars[i];
                stringBuffer.append(count);
                stringBuffer.append(chars[i]);
                count = 1;
            }
        }
        stringBuffer.append(count);
        return stringBuffer.toString();
    }
     // 2.统计字符串个数 如“abc21b416u” ===> 输出5
}
