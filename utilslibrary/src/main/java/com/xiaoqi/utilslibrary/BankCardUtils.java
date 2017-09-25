package com.xiaoqi.utilslibrary;

import android.content.Context;
import android.text.TextUtils;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yun.wang
 * Date :2017/6/30
 * Description: ***
 * Version: 1.0.0
 */
public class BankCardUtils {

     /*
    校验过程：
    1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
    2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，将个位十位数字相加，即将其减去9），再求和。
    3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
    */
    /**
     * 校验银行卡卡号
     */
    public static boolean checkBankCard(String bankCard) {
        if(TextUtils.isEmpty(bankCard) || bankCard.length() < 15 || bankCard.length() > 19) {
            return false;
        }
        char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));
        if(bit == 'N'){
            return false;
        }
        return bankCard.charAt(bankCard.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     * @param nonCheckCodeBankCard
     * @return
     */
    private static char getBankCardCheckCode(String nonCheckCodeBankCard){
        if(nonCheckCodeBankCard == null || nonCheckCodeBankCard.trim().length() == 0
                || !nonCheckCodeBankCard.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeBankCard.trim().toCharArray();
        int luhmSum = 0;
        for(int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if(j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
    }


    //had better in i/o thread
    public static String getBankNameByBankcardnumber(String number, Context context) {
        if (TextUtils.isEmpty(number)) {
            return "";
        }

        String bankNameString = "";

        try {
            InputStream is = context.getAssets().open("bankcardinfo/bankInfo.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String readString = new String(buffer, "UTF-8");

            String[] stringarray = readString.split("\r\n");
            List<String> readLines = Arrays.asList(stringarray);
            ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
            String cardNo = number;

            for (int i = 0; i < readLines.size(); i++) {
                String[] ss = readLines.get(i).split("=");
                map.put(ss[0], ss[1]);
            }
            cardNo = cardNo.replaceAll(" ", "");
            String temp = cardNo.substring(0, 6);
            String name6 = map.get(temp);
            if (name6 == null) {
                temp = cardNo.substring(0, 7);
                String name7 = map.get(temp);
                if (name7 == null) {
                    temp = cardNo.substring(0, 8);
                    String name8 = map.get(temp);
                    if (name8 == null) {
                        temp = cardNo.substring(0, 9);
                        String name9 = map.get(temp);
                        if (name9 == null) {
                            bankNameString = "";
                        } else {
                            bankNameString = name9;
                        }
                    } else {
                        bankNameString = name8;
                    }
                } else {
                    bankNameString = name7;
                }
            }
            bankNameString = name6;
        } catch (Exception ex) {
            ex.printStackTrace();
            bankNameString = "";
        }

        return bankNameString;

    }
}
