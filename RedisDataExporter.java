
import org.w3c.dom.NodeList;

import redis.clients.jedis.*;

public class RedisDataExporter {

    public static void main(String[] args) {

        String redisHost = "localhost";
        int redisPort = 6379;

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        JedisPool jedisPool = new JedisPool(poolConfig, redisHost, redisPort);

        try (Jedis jedis = jedisPool.getResource()) {

            // load and parse the XML file
            File xmlFile = new File("config.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // extract subdomains
            List<String> subdomains = new ArrayList<>();
            NodeList subdomainList = doc.getElementsByTagName("subdomain");
            for (int i = 0; i < subdomainList.getLength(); i++) {
                Element subdomainElement = (Element) subdomainList.item(i);
                String subdomain = subdomainElement.getTextContent();
                subdomains.add(subdomain);
            }

            // export subdomains to a Redis key
            String subdomainsKey = "subdomains";
            jedis.set(subdomainsKey, String.join(",", subdomains));

            // extract and export cookies
            NodeList cookieList = doc.getElementsByTagName("cookie");
            for (int i = 0; i < cookieList.getLength(); i++) {
                Element cookieElement = (Element) cookieList.item(i);
                String name = cookieElement.getAttribute("name");
                String host = cookieElement.getAttribute("host");
                String value = cookieElement.getTextContent();
                String cookieKey = "cookie:" + name + ":" + host;
                jedis.set(cookieKey, value);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // close jedis connection pool when finished
            jedisPool.close();
        }

    }
}