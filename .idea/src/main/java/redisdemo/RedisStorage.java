package com.skillbox.redisdemo;

import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisConnectionException;
import org.redisson.config.Config;
import redis.clients.jedis.Jedis;

import java.util.Date;

import static java.lang.System.out;

public class RedisStorage {
    private Jedis jedis;

    // Объект для работы с Redis
    private RedissonClient redisson;

    // Объект для работы с ключами
    private RKeys rKeys;

    // Объект для работы с Sorted Set'ом
    private RScoredSortedSet<String> onlineUsers;

    private final static String KEY = "ONLINE_USERS";

    private double getTs() {
        return new Date().getTime() / 1000;
    }

    // Пример вывода всех ключей
    public void listKeys() {
        Iterable<String> keys = rKeys.getKeys();
        for (String key : keys) {
            out.println("KEY: " + key + ", type:" + rKeys.getType(key));
        }
    }

    void init() {
        jedis = new Jedis("localhost", 6379);
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        try {
            redisson = Redisson.create(config);
        } catch (RedisConnectionException Exc) {
            out.println("Не удалось подключиться к Redis");
            out.println(Exc.getMessage());
        }
        rKeys = redisson.getKeys();
        onlineUsers = redisson.getScoredSortedSet(KEY);
        rKeys.delete(KEY);
    }

    public void addUser(int user) {
        jedis.zadd(KEY, getTs(), String.valueOf(user));
    }

   // void shutdown() {
   //     redisson.shutdown();
   // }

    // Фиксирует посещение пользователем страницы
 //   void logPageVisit(int user_id) {
  //      //ZADD ONLINE_USERS
  //      onlineUsers.add(getTs(), String.valueOf(user_id));
   // }

    // Удаляет
    //  void deleteOldEntries(int secondsAgo)
    // {
    //ZREVRANGEBYSCORE ONLINE_USERS 0 <time_5_seconds_ago>
    //    onlineUsers.removeRangeByScore(0, true, getTs() - secondsAgo, true);


    //  }
    // int calculateUsersNumber()
    // {
    //ZCOUNT ONLINE_USERS
    //     return onlineUsers.count(Double.NEGATIVE_INFINITY, true, Double.POSITIVE_INFINITY, true);
    // }
    public void deleteAll() {
        jedis.flushDB();
    }

  //  public void showQueue() {
    //    System.out.println(jedis.zrange(KEY, 0, -1));
 //   }
}
