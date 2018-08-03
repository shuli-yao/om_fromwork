package com.megvii.utlis;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @ClassName IdcardUtils
 * @Description 身份证工具类
 * <p>
 * <pre>
 * --15位身份证号码：第7、8位为出生年份(两位数)，第9、10位为出生月份，第11、12位代表出生日期，第15位代表性别，奇数为男，偶数为女。
 * --18位身份证号码：第7、8、9、10位为出生年份(四位数)，第11、第12位为出生月份，第13、14位代表出生日期，第17位代表性别，奇数为男，偶数为女。
 *    最后一位为校验位
 * </pre>
 * @Author shuliyao
 * @CreateTime 2018/6/7 下午3:45
 */
public class IdcardUtils {
    private static final Logger log = LoggerFactory.getLogger(IdcardUtils.class);

    /**
     * <pre>
     * 省、直辖市代码表：
     *     11 : 北京  12 : 天津  13 : 河北       14 : 山西  15 : 内蒙古
     *     21 : 辽宁  22 : 吉林  23 : 黑龙江  31 : 上海  32 : 江苏
     *     33 : 浙江  34 : 安徽  35 : 福建       36 : 江西  37 : 山东
     *     41 : 河南  42 : 湖北  43 : 湖南       44 : 广东  45 : 广西      46 : 海南
     *     50 : 重庆  51 : 四川  52 : 贵州       53 : 云南  54 : 西藏
     *     61 : 陕西  62 : 甘肃  63 : 青海       64 : 宁夏  65 : 新疆
     *     71 : 台湾
     *     81 : 香港  82 : 澳门
     *     91 : 国外
     * </pre>
     */
    private static String cityCode[] = {"11", "12", "13", "14", "15", "21",
            "22", "23", "31", "32", "33", "34", "35", "36", "37", "41", "42",
            "43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62",
            "63", "64", "65", "71", "81", "82", "91"};

    /**
     * 每位加权因子
     */
    private static int power[] = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5,
            8, 4, 2};

    /**
     * 验证所有的身份证的合法性
     *
     * @param idcard 身份证
     * @return 合法返回true，否则返回false
     */
    public static boolean isValidatedAllIdcard(String idcard) {
        if (idcard == null || "".equals(idcard)) {
            return false;
        }
        if (idcard.length() == 15) {
            return validate15IDCard(idcard);
        }
        return validate18Idcard(idcard);
    }

    /**
     * <p>
     * 判断18位身份证的合法性
     * </p>
     * 根据〖中华人民共和国国家标准GB11643-1999〗中有关公民身份号码的规定，公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。
     * 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
     * <p>
     * 顺序码: 表示在同一地址码所标识的区域范围内，对同年、同月、同 日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配 给女性。
     * </p>
     * <p>
     * 1.前1、2位数字表示：所在省份的代码； 2.第3、4位数字表示：所在城市的代码； 3.第5、6位数字表示：所在区县的代码；
     * 4.第7~14位数字表示：出生年、月、日； 5.第15、16位数字表示：所在地的派出所的代码；
     * 6.第17位数字表示性别：奇数表示男性，偶数表示女性；
     * 7.第18位数字是校检码：也有的说是个人信息码，一般是随计算机的随机产生，用来检验身份证的正确性。校检码可以是0~9的数字，有时也用x表示。
     * </p>
     * <p>
     * 第十八位数字(校验码)的计算方法为： 1.将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4
     * 2 1 6 3 7 9 10 5 8 4 2
     * </p>
     * <p>
     * 2.将这17位数字和系数相乘的结果相加。
     * </p>
     * <p>
     * 3.用加出来和除以11，看余数是多少
     * </p>
     * 4.余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3
     * 2。
     * <p>
     * 5.通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2。
     * </p>
     *
     * @param idcard
     * @return
     */
    public static boolean validate18Idcard(String idcard) {
        if (idcard == null) {
            return false;
        }

        // 非18位为假
        if (idcard.length() != 18) {
            return false;
        }
        // 获取前17位
        String idcard17 = idcard.substring(0, 17);

        // 前17位全部为数字
        if (!isDigital(idcard17)) {
            return false;
        }

        String provinceid = idcard.substring(0, 2);
        // 校验省份
        if (!checkProvinceid(provinceid)) {
            return false;
        }

        // 校验出生日期
        String birthday = idcard.substring(6, 14);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        try {
            Date birthDate = sdf.parse(birthday);
            String tmpDate = sdf.format(birthDate);
            if (!tmpDate.equals(birthday)) {
                return false;
            }
        } catch (ParseException e1) {
            return false;
        }

        // 获取第18位
        String idcard18Code = idcard.substring(17, 18);

        char c[] = idcard17.toCharArray();

        int bit[] = converCharToInt(c);

        int sum17 = 0;

        sum17 = getPowerSum(bit);

        // 将和值与11取模得到余数进行校验码判断
        String checkCode = getCheckCodeBySum(sum17);
        if (null == checkCode) {
            return false;
        }
        // 将身份证的第18位与算出来的校码进行匹配，不相等就为假
        if (!idcard18Code.equalsIgnoreCase(checkCode)) {
            return false;
        }

        return true;
    }

    /**
     * 校验15位身份证
     * <p>
     * <pre>
     * 只校验省份和出生年月日
     * </pre>
     *
     * @param idcard
     * @return
     */
    public static boolean validate15IDCard(String idcard) {
        if (idcard == null) {
            return false;
        }
        // 非15位为假
        if (idcard.length() != 15) {
            return false;
        }

        // 15全部为数字
        if (!isDigital(idcard)) {
            return false;
        }

        String provinceid = idcard.substring(0, 2);
        // 校验省份
        if (!checkProvinceid(provinceid)) {
            return false;
        }

        String birthday = idcard.substring(6, 12);

        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");

        try {
            Date birthDate = sdf.parse(birthday);
            String tmpDate = sdf.format(birthDate);
            if (!tmpDate.equals(birthday)) {
                return false;
            }
        } catch (ParseException e1) {
            return false;
        }

        return true;
    }

    /**
     * 将15位的身份证转成18位身份证
     *
     * @param idcard
     * @return
     */
    public static String convertIdcarBy15bit(String idcard) {
        if (idcard == null) {
            return null;
        }

        // 非15位身份证
        if (idcard.length() != 15) {
            return null;
        }

        // 15全部为数字
        if (!isDigital(idcard)) {
            return null;
        }

        String provinceid = idcard.substring(0, 2);
        // 校验省份
        if (!checkProvinceid(provinceid)) {
            return null;
        }

        String birthday = idcard.substring(6, 12);

        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");

        Date birthdate = null;
        try {
            birthdate = sdf.parse(birthday);
            String tmpDate = sdf.format(birthdate);
            if (!tmpDate.equals(birthday)) {// 身份证日期错误
                return null;
            }

        } catch (ParseException e1) {
            return null;
        }

        Calendar cday = Calendar.getInstance();
        cday.setTime(birthdate);
        String year = String.valueOf(cday.get(Calendar.YEAR));

        String idcard17 = idcard.substring(0, 6) + year + idcard.substring(8);

        char c[] = idcard17.toCharArray();
        String checkCode = "";

        // 将字符数组转为整型数组
        int bit[] = converCharToInt(c);

        int sum17 = 0;
        sum17 = getPowerSum(bit);

        // 获取和值与11取模得到余数进行校验码
        checkCode = getCheckCodeBySum(sum17);

        // 获取不到校验位
        if (null == checkCode) {
            return null;
        }
        // 将前17位与第18位校验码拼接
        idcard17 += checkCode;
        return idcard17;
    }

    /**
     * 校验省份
     *
     * @param provinceid
     * @return 合法返回TRUE，否则返回FALSE
     */
    private static boolean checkProvinceid(String provinceid) {
        for (String id : cityCode) {
            if (id.equals(provinceid)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 数字验证
     *
     * @param str
     * @return
     */
    private static boolean isDigital(String str) {
        return str.matches("^[0-9]*$");
    }

    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
     *
     * @param bit
     * @return
     */
    private static int getPowerSum(int[] bit) {

        int sum = 0;

        if (power.length != bit.length) {
            return sum;
        }

        for (int i = 0; i < bit.length; i++) {
            for (int j = 0; j < power.length; j++) {
                if (i == j) {
                    sum = sum + bit[i] * power[j];
                }
            }
        }
        return sum;
    }

    /**
     * 将和值与11取模得到余数进行校验码判断
     *
     * @param sum17
     * @return 校验位
     */
    private static String getCheckCodeBySum(int sum17) {
        String checkCode = null;
        switch (sum17 % 11) {
            case 10:
                checkCode = "2";
                break;
            case 9:
                checkCode = "3";
                break;
            case 8:
                checkCode = "4";
                break;
            case 7:
                checkCode = "5";
                break;
            case 6:
                checkCode = "6";
                break;
            case 5:
                checkCode = "7";
                break;
            case 4:
                checkCode = "8";
                break;
            case 3:
                checkCode = "9";
                break;
            case 2:
                checkCode = "x";
                break;
            case 1:
                checkCode = "0";
                break;
            case 0:
                checkCode = "1";
                break;
        }
        return checkCode;
    }

    /**
     * 将字符数组转为整型数组
     *
     * @param c
     * @return
     * @throws NumberFormatException
     */
    private static int[] converCharToInt(char[] c) throws NumberFormatException {
        int[] a = new int[c.length];
        int k = 0;
        for (char temp : c) {
            a[k++] = Integer.parseInt(String.valueOf(temp));
        }
        return a;
    }

    /**
     * 格式化省份证号
     *
     * @param idcard
     * @return
     */
    public static String formatIdcard(String idcard) {
        String resCard = "none";
        if (StringUtils.isBlank(idcard) || "none".equalsIgnoreCase(idcard)) {
            return "none";
        }
        if (idcard.length() == 15) {
            resCard = convertIdcarBy15bit(idcard);
        } else if (idcard.length() == 18) {
            resCard = idcard;
        } else {
            return resCard;
        }
        if (StringUtils.isBlank(resCard)) {
            return "none";
        }
        return resCard;
    }

    /**
     * 获取生日
     *
     * @param idcard
     * @return
     */
    public static String getBirthday(String idcard) {
        String birthday = "";
        if (idcard == null || "".equals(idcard)) {
            return "none";
        }
        birthday = idcard.substring(6, 10) + "-" + idcard.substring(10, 12) + "-" + idcard.substring(12, 14);
        return birthday;
    }

    /**
     * 获取生日，截止到月份
     *
     * @param idcard
     * @return
     */
    public static String getBirthdayByMonth(String idcard) {
        String birthday = "";
        if (idcard == null || "".equals(idcard)) {
            return "unknown";
        }
        birthday = idcard.substring(6, 10)+idcard.substring(10, 12);
        return birthday;
    }

    /**
     * 获取生日，只截取天
     *
     * @param idcard
     * @return
     */
    public static String getBirthdayByDay(String idcard) {
        String birthday = "";
        if (idcard == null || "".equals(idcard)) {
            return "unknown";
        }
        birthday = idcard.substring(12, 14);
        return birthday;
    }



    /**
     * 获取性别
     *
     * @param idcard
     * @return
     */
    public static String getGender(String idcard) {
        String gender = "";
        if (idcard == null || "".equals(idcard)) {
            return "none";
        }
        String sCardNum = idcard.substring(16, 17);
        try {
            if (Integer.parseInt(sCardNum) % 2 != 0) {
                gender = "male";//男
            } else {
                gender = "female";//女
            }
        } catch (Exception e) {
            log.error("获取性别异常 身份证号码=" + sCardNum, e);
            return "none";
        }
        return gender;
    }

    /**
     * 获取名族名称
     *
     * @param num
     * @return
     */
    public static String getNation(String num) {
        switch (num) {
            case "01":
                return "汉族";
            case "02":
                return "蒙古族";
            case "03":
                return "回族";
            case "04":
                return "藏族";
            case "05":
                return "维吾尔族";
            case "06":
                return "苗族";
            case "07":
                return "彝族";
            case "08":
                return "壮族";
            case "09":
                return "布依族";
            case "10":
                return "朝鲜族";
            case "11":
                return "满族";
            case "12":
                return "侗族";
            case "13":
                return "瑶族";
            case "14":
                return "白族";
            case "15":
                return "土家族";
            case "16":
                return "哈尼族";
            case "17":
                return "哈萨克族";
            case "18":
                return "傣族";
            case "19":
                return "黎族";
            case "20":
                return "僳僳族";
            case "21":
                return "佤族";
            case "22":
                return "畲族";
            case "23":
                return "高山族";
            case "24":
                return "拉祜族";
            case "25":
                return "水族";
            case "26":
                return "东乡族";
            case "27":
                return "纳西族";
            case "28":
                return "景颇族";
            case "29":
                return "柯尔克孜族";
            case "30":
                return "土族";
            case "31":
                return "达斡尔族";
            case "32":
                return "仫佬族";
            case "33":
                return "羌族";
            case "34":
                return "布朗族";
            case "35":
                return "撒拉族";
            case "36":
                return "毛南族";
            case "37":
                return "仡佬族";
            case "38":
                return "锡伯族";
            case "39":
                return "阿昌族";
            case "40":
                return "普米族";
            case "41":
                return "塔吉克族";
            case "42":
                return "怒族";
            case "43":
                return "乌孜别克族";
            case "44":
                return "俄罗斯族";
            case "45":
                return "鄂温克族";
            case "46":
                return "德昂族";
            case "47":
                return "保安族";
            case "48":
                return "裕固族";
            case "49":
                return "京族";
            case "50":
                return "塔塔尔族";
            case "51":
                return "独龙族";
            case "52":
                return "鄂伦春族";
            case "53":
                return "赫哲族";
            case "54":
                return "门巴族";
            case "55":
                return "珞巴族";
            case "56":
                return "基诺族";
            default:
                return num;
        }
    }

    public static void main(String[] args) throws Exception {
        String idcard15 = "130321860311519";
        String idcard18 = "21010219861708373x";
        // 15位身份证
        System.out.println(isValidatedAllIdcard(idcard15));
        // 18位身份证
        System.out.println(isValidatedAllIdcard(idcard18));
        // 15位身份证转18位身份证
        System.out.println(convertIdcarBy15bit(idcard15));
    }

}
