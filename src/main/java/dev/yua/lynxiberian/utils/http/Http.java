package dev.yua.lynxiberian.utils.http;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;

public class Http {
    public File download(String source, String destination) throws IOException {
        File localFile = new File(destination);
        FileUtils.copyURLToFile(new URL(source), localFile, 10000, 10000);
        return localFile;
    }

    public String getContent(String url) throws IOException {
        StringBuilder content = new StringBuilder();
        URL myurl = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
        con.setRequestProperty ( "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0" );
        InputStream ins = con.getInputStream();
        InputStreamReader isr = new InputStreamReader(ins);
        try (BufferedReader in = new BufferedReader(isr)) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) content.append(inputLine);
        }
        ins.close();
        isr.close();
        con.disconnect();
        return content.toString();
    }

    public JSONObject getJsonObjectFromXml(String url) throws IOException {
        return XML.toJSONObject(this.getContent(url));
    }

    public JSONArray getJsonArray(String url) throws IOException {
        return new JSONArray(this.getContent(url));
    }

    public JSONObject getJsonObject(String url) throws IOException {
        return new JSONObject(this.getContent(url));
    }
}
