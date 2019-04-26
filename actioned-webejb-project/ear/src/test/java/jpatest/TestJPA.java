package jpatest;

import static org.junit.Assert.*;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class TestJPA {
    String port = System.getProperty("liberty.test.port");
    String war = "jpatest";
    String urlBase = "http://localhost:" + port + "/" + war + "/";

    @org.junit.Test
    public void testIndexPage() throws Exception {
        String url = this.urlBase;
        HttpURLConnection con = testRequestHelper(url, "GET");
        assertEquals("Incorrect response code from " + url, 200, con.getResponseCode());

    }

    private HttpURLConnection testRequestHelper(String url, String method) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod(method);
        return con;
    }
}
