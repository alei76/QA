package com.tcm.util;

import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * Created by azurexsyl on 2015/12/14.
 */
public class JedisTools {

    private static Jedis jedis;

    static {
        jedis = new Jedis("10.15.62.238", 6379, 0);
    }

    public static Set<String> getURI(String word) {
        return jedis.zrange(word, 0, -1);
    }

    public static void delKey(String word) {
        jedis.del(word);
    }

    // JUST FORTEST-
    public static void main(String[] args) {
        /*delKey("药");
        delKey("片");
        delKey("散");
        delKey("膏");
        delKey("茶");
        delKey("10");*/
        Set<String> set = getURI("症状");
        for(String s : set) {
            System.out.println(s);
        }
    }
}
