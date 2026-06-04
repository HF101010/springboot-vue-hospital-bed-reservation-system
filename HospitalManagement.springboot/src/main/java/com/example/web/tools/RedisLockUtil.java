package com.example.web.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁工具类
 * 用于解决并发问题，如床位预约时的并发控制
 */
@Slf4j
@Component
public class RedisLockUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 默认锁过期时间（秒）
     */
    private static final long DEFAULT_EXPIRE_TIME = 30;

    /**
     * 释放锁的Lua脚本
     * 确保只有持有锁的线程才能释放锁
     */
    private static final String UNLOCK_SCRIPT = 
        "if redis.call('get', KEYS[1]) == ARGV[1] then " +
        "return redis.call('del', KEYS[1]) " +
        "else " +
        "return 0 " +
        "end";

    /**
     * 尝试获取分布式锁
     * 
     * @param lockKey 锁的key
     * @param lockValue 锁的值（用于标识持有锁的线程）
     * @param expireTime 锁的过期时间（秒）
     * @return 是否成功获取锁
     */
    public boolean tryLock(String lockKey, String lockValue, long expireTime) {
        try {
            Boolean result = redisTemplate.opsForValue()
                    .setIfAbsent(lockKey, lockValue, expireTime, TimeUnit.SECONDS);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.error("获取分布式锁失败，lockKey: {}", lockKey, e);
            return false;
        }
    }

    /**
     * 尝试获取分布式锁（使用默认过期时间）
     * 
     * @param lockKey 锁的key
     * @param lockValue 锁的值
     * @return 是否成功获取锁
     */
    public boolean tryLock(String lockKey, String lockValue) {
        return tryLock(lockKey, lockValue, DEFAULT_EXPIRE_TIME);
    }

    /**
     * 尝试获取分布式锁（自动生成lockValue）
     * 
     * @param lockKey 锁的key
     * @param expireTime 锁的过期时间（秒）
     * @return 锁的值，如果获取失败返回null
     */
    public String tryLockWithValue(String lockKey, long expireTime) {
        String lockValue = Thread.currentThread().getId() + "-" + System.currentTimeMillis();
        if (tryLock(lockKey, lockValue, expireTime)) {
            return lockValue;
        }
        return null;
    }

    /**
     * 释放分布式锁
     * 使用Lua脚本确保原子性，只有持有锁的线程才能释放
     * 
     * @param lockKey 锁的key
     * @param lockValue 锁的值
     * @return 是否成功释放锁
     */
    public boolean unlock(String lockKey, String lockValue) {
        try {
            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setScriptText(UNLOCK_SCRIPT);
            script.setResultType(Long.class);
            Long result = redisTemplate.execute(script, 
                    Collections.singletonList(lockKey), lockValue);
            return result != null && result > 0;
        } catch (Exception e) {
            log.error("释放分布式锁失败，lockKey: {}", lockKey, e);
            return false;
        }
    }

    /**
     * 执行带锁的操作
     * 自动获取和释放锁，确保操作的原子性
     * 
     * @param lockKey 锁的key
     * @param expireTime 锁的过期时间（秒）
     * @param action 要执行的操作
     * @param <T> 返回值类型
     * @return 操作结果
     * @throws Exception 操作异常或获取锁失败
     */
    public <T> T executeWithLock(String lockKey, long expireTime, LockAction<T> action) throws Exception {
        String lockValue = tryLockWithValue(lockKey, expireTime);
        if (lockValue == null) {
            throw new RuntimeException("获取分布式锁失败，请稍后重试");
        }
        
        try {
            return action.execute();
        } finally {
            unlock(lockKey, lockValue);
        }
    }

    /**
     * 执行带锁的操作（使用默认过期时间）
     * 
     * @param lockKey 锁的key
     * @param action 要执行的操作
     * @param <T> 返回值类型
     * @return 操作结果
     * @throws Exception 操作异常或获取锁失败
     */
    public <T> T executeWithLock(String lockKey, LockAction<T> action) throws Exception {
        return executeWithLock(lockKey, DEFAULT_EXPIRE_TIME, action);
    }

    /**
     * 锁操作的函数式接口
     */
    @FunctionalInterface
    public interface LockAction<T> {
        T execute() throws Exception;
    }
}
