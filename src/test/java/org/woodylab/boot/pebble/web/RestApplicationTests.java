package org.woodylab.boot.pebble.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Created by <a href="mailto:javaworld@qq.com">Woody</a> @ 15-12-12 下午10:03.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {RestApplicationTests.Application.class, CustomPebbleExtension.class})
@IntegrationTest("server.port:0")
@WebAppConfiguration
public class RestApplicationTests {

    @Autowired
    private EmbeddedWebApplicationContext context;
    private int port;

    @Before
    public void init() {
        port = context.getEmbeddedServletContainer().getPort();
    }

    @Test
    public void testHomePage() throws Exception {
        String body = new TestRestTemplate().getForObject("http://localhost:" + port,
                String.class);
        assertTrue(body.contains("Hello World"));
    }

    @Test
    public void testPartialPage() throws Exception {
        String body = new TestRestTemplate().getForObject("http://localhost:" + port
                + "/partial", String.class);
        assertTrue(body.contains("Hello App"));
    }


    @Test
    public void testExtension() throws Exception {
        String body = new TestRestTemplate().getForObject("http://localhost:" + port +
                "/foo", String.class);
        assertTrue(body.contains("success"));
    }

    @Configuration
    @EnableAutoConfiguration
    @Controller
    public static class Application {

        @RequestMapping("/")
        public String home(Map<String, Object> model) {
            model.put("time", new Date());
            model.put("message", "Hello World");
            model.put("title", "Hello App");
            return "home";
        }

        @RequestMapping("/partial")
        public String layout(Map<String, Object> model) {
            model.put("time", new Date());
            model.put("message", "Hello World");
            model.put("title", "Hello App");
            return "partial";
        }

        @RequestMapping("/foo")
        public String foo() {
            return "foo";
        }

        public static void main(String[] args) {
            SpringApplication.run(Application.class, args);
        }

    }

}
