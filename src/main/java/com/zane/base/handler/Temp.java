package com.zane.base.handler;

import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Temp {
    public String getCarCode() {
        StringBuilder a = new StringBuilder("甘A·");
        List<String> strings = List.of("1","2","3","4","5","6","7","8","9","0","A","B","C","D","E","F","G","H"
                ,"J","K","L","M","N","P","Q","R","S","T","U","V","W","X","Y","Z");
        List<String> gereratedList = new ArrayList<>();
        List<String> resultList = new ArrayList<>();
        for (int i = 0; i < 50;i++) {
            StringBuilder x = new StringBuilder();
            int nums = RandomUtils.nextInt(0,3);
//            System.out.println(nums);
            //生成字母
            for (int j = 0; j < nums;j++){
                x.append(strings.get(RandomUtils.nextInt(10, 34)));
            }
            //生成数字
            for (int k = 0;k < 5 - nums;k++){
                x.append(strings.get(RandomUtils.nextInt(0,10)));
            }
            if (gereratedList.contains(a.toString())){
                i --;
            }
            else {
                String stringA = x.toString();
                // 重新排序
                ArrayList<Character> characters = new ArrayList<>();
                for (char c: stringA.toCharArray()){
                    characters.add(c);
                }
                Collections.shuffle(characters);
                StringBuilder res = new StringBuilder();
                for (char c: characters){
                    res.append(c);
                }
//                System.out.println(characters);
                resultList.add(a.append(res).toString().toUpperCase());
                gereratedList.add(x.toString());
                a.delete(3,a.length());
            }
        }
        System.out.println(resultList);
        return resultList.toString();

    }
}
