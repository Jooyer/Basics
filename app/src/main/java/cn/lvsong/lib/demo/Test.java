package cn.lvsong.lib.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Desc: https://www.cnblogs.com/gnivor/p/4509268.html
 * https://blog.csdn.net/qq_37514135/article/details/79512768?utm_medium=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.channel_param&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.channel_param
 * Author: Jooyer
 * Date: 2020-07-29
 * Time: 18:49
 */
public class Test {

    public static void main(String[] args) {
        String s = new String("　　Back in the fifteenth century, in a tiny village near Nuremberg, lived a family with eighteen children. Eighteen! In order merely to keep food on the table for this mob, the father and head of the household, a goldsmith by profession, worked almost eighteen hours a day at his trade and any other paying chore he could find in the neighborhood. Despite their seemingly hopeless condition, two of Albrecht Durer the Elder's children had a dream. They both wanted to pursue their talent for art, but they knew full well that their father would never be financially able to send either of them to Nuremberg to study at the Academy");
//        String s = "\n";
        StringTokenizer st = new StringTokenizer(s); // 拆分的单词带标点
//        StringTokenizer st = new StringTokenizer(s, " ,?.!:\"'\n\r#",true); // 没有计算标点
        System.out.println( "Token Total: " + st.countTokens() );
        List<String> words = new ArrayList<>();
        while( st.hasMoreElements() ){
            words.add(st.nextToken());
//            System.out.print(st.nextToken());
        }

        for (String str:words){
            System.out.println(str);
        }

    }

}
