package Testing;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

//ran out of time, but this is the gist of some test cases that can be made

public class RedisDataExporterTest {
    private JedisPool jedisPool;
    private Jedis jedis;

    @BeforeEach
    public void setUp() {
        // Initialize Jedis pool and Jedis for testing
        jedisPool = new JedisPool(new JedisPoolConfig(), "localhost", 6379);
        jedis = jedisPool.getResource();
    }

    @AfterEach
    public void tearDown() {
        // Clean up resources
        jedis.close();
        jedisPool.close();
    }

    @Test
    public void testSubdomainsExport() {
        // Instantiate your RedisDataExporter class
        RedisDataExporter exporter = new RedisDataExporter();

        // You can use a Jedis instance to interact with Redis for testing
        Jedis jedis = new Jedis("localhost", 6379);

        // Define test subdomains
        List<String> testSubdomains = Arrays.asList("http://secureline.tools.avast.com", "http://gf.tools.avast.com");

        // Call the method you want to test
        exporter.exportSubdomains(jedis, testSubdomains);

        // Retrieve the subdomains from Redis
        String subdomainsKey = "subdomains";
        String redisSubdomains = jedis.get(subdomainsKey);

        // Assert that the Redis value matches the expected value
        assertEquals(String.join(",", testSubdomains), redisSubdomains);

    }

    @Test
    public void testCookiesExport() {
        // Instantiate your RedisDataExporter class
        RedisDataExporter exporter = new RedisDataExporter();
    
        // You can use a Jedis instance to interact with Redis for testing
        Jedis jedis = new Jedis("localhost", 6379);
    
        
        // Define test cookies 
        Map<String, String> testCookies = new HashMap<>();
        testCookies.put("cookie1", "value1");
        testCookies.put("cookie2", "value2");
    
        // Call the method you want to test 
        exporter.exportCookies(jedis, testCookies);
    
        // Retrieve and assert the values of individual cookies in Redis
        for (Map.Entry<String, String> entry : testCookies.entrySet()) {
            String cookieKey = "cookie:" + entry.getKey();
            String redisValue = jedis.get(cookieKey);
            assertEquals(entry.getValue(), redisValue);
    }
}
