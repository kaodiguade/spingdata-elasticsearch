import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.es.entity.Article;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.elasticsearch.xpack.core.XPackClient;
import org.elasticsearch.xpack.core.watcher.client.WatcherClient;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;


public class ElasticSearchClientTest {

    private TransportClient client;
    @Before
    public void init() throws Exception{
        client = new PreBuiltXPackTransportClient(Settings.builder()
                .put("cluster.name", "my-elasticsearch").build());
        client.addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9301));
        client.addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9302));
        client.addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9303));
    }
    @Test
    public void createIndex() throws Exception{
        client.admin().indices().prepareCreate("hello").get();

//        XPackClient xpackClient = new XPackClient(client);
//        WatcherClient watcherClient = xpackClient.watcher();
        client.close();

    }

    @Test
    public void setMappings() throws Exception{
        XContentBuilder builder = XContentFactory.jsonBuilder().startObject().startObject("properties")
                .startObject("id").field("type","long").field("store",true).endObject()
                .startObject("title").field("type","text").field("store",true).field("analyzer","ik_smart").endObject()
                .startObject("content").field("type","text").field("store",true).field("analyzer","ik_smart").endObject()
                .endObject().endObject();
        client.admin().indices().preparePutMapping("hello").setType("_doc").setSource(builder).get();

        client.close();
    }

    @Test
    public void testAddDocument() throws Exception{
        XContentBuilder builder = XContentFactory.jsonBuilder().startObject()
                .field("id",1l)
                .field("title","标题")
                .field("content","内容啊")
                .endObject();
        client.prepareIndex().setIndex("hello").setType("_doc").setId("1").setSource(builder).get();
        client.close();
    }

    @Test
    public void testAddDocument2() throws Exception{
        Article article = new Article();
        article.setId(2);
        article.setTitle("标题2");
        article.setContent("内容啊2");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonDocument = objectMapper.writeValueAsString(article);
        System.out.println(jsonDocument);

        client.prepareIndex("hello","_doc","2").setSource(jsonDocument, XContentType.JSON).get();
        client.close();
    }

    @Test
    public void testAddDocument3() throws Exception{
        for (int i = 3; i < 100; i++) {
            Article article = new Article();
            article.setId(i);
            article.setTitle("在7.3版本中"+i);
            article.setContent("已经不推荐使用TransportClient这个client，官网说在"+i);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonDocument = objectMapper.writeValueAsString(article);
            System.out.println(jsonDocument);

            client.prepareIndex("hello","_doc",i+"").setSource(jsonDocument, XContentType.JSON).get();
        }

        client.close();
    }
}
