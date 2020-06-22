package com.ssm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * @description:Redis的配置Java类
 * @author: Cherry
 * @time: 2020/6/17 9:05
 */
@Configuration
@EnableCaching
public class RedisConfig {
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //最大连接数
        jedisPoolConfig.setMaxTotal(100);
        //最小空闲连接数
        jedisPoolConfig.setMinIdle(20);
        //当池内没有可用的连接时，最大等待时间
        jedisPoolConfig.setMaxWaitMillis(10000);
        //------其他属性根据需要自行添加-------------
        return jedisPoolConfig;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory(@Autowired JedisPoolConfig jedisPoolConfig) {
        //单机版jedis
        RedisStandaloneConfiguration redisStandaloneConfiguration =
                new RedisStandaloneConfiguration();
        //设置redis服务器的host或者ip地址
        redisStandaloneConfiguration.setHostName("localhost");
        //设置默认使用的数据库
        redisStandaloneConfiguration.setDatabase(0);
        //设置密码
        //redisStandaloneConfiguration.setPassword(RedisPassword.of("123456"));
        //设置redis的服务的端口号
        redisStandaloneConfiguration.setPort(6379);
        //获得默认的连接池构造器(怎么设计的，为什么不抽象出单独类，供用户使用呢)
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcb =
                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder)JedisClientConfiguration.builder();
        //指定jedisPoolConifig来修改默认的连接池构造器（真麻烦，滥用设计模式！）
        jpcb.poolConfig(jedisPoolConfig);
        //通过构造器来构造jedis客户端配置
        JedisClientConfiguration jedisClientConfiguration = jpcb.build();
        //单机配置 + 客户端配置 = jedis连接工厂
        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
    }

    @Bean
    public RedisTemplate initRedisTemplate(@Autowired RedisConnectionFactory RedisConnectionFactory){
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(RedisConnectionFactory);
        // 自定Redis序列化器
        RedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
        RedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // 设置序列化器
        redisTemplate.setDefaultSerializer(stringRedisSerializer);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jdkSerializationRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jdkSerializationRedisSerializer);
        return redisTemplate;
    }

    @Bean(name = "redisCacheManager")
    public CacheManager initRedisCacheManager(@Autowired RedisConnectionFactory redisConnectionFactory){
        // 设置缓存有效期
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMillis(10));
        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration).build();
    }
}
