import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.TransportAddress;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.junit.Before;
import org.junit.Test;

import javax.swing.text.Highlighter;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 查询
 */
public class SearchIndexTest {
    private TransportClient client;
    @Before
    public void init() throws Exception{
        client = new PreBuiltXPackTransportClient(Settings.builder()
                .put("cluster.name", "my-elasticsearch").build());
        client.addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9301));
        client.addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9302));
        client.addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9303));
    }

    /**
     * 分页查询
     * @param queryBuilder
     * @throws Exception
     */
    private void search(QueryBuilder queryBuilder) throws  Exception{
        SearchResponse searchResponse = client.prepareSearch("hello").setQuery(queryBuilder)
                .setFrom(0).setSize(5).get();

        SearchHits searchHits = searchResponse.getHits();
        System.out.println("count:"+searchHits.getTotalHits());
        System.out.println("");
        Iterator<SearchHit> iterable = searchHits.iterator();
        while (iterable.hasNext()){
            SearchHit searchHit = iterable.next();
            System.out.println(searchHit.getSourceAsString());

            Map<String,Object> document = searchHit.getSourceAsMap();
            //System.out.println(document.get("id"));
            //System.out.println(document.get("title"));
            //System.out.println(document.get("content"));
            System.out.println("");
        }
        client.close();
    }
    /**
     * 分页查询高亮显示
     * @param queryBuilder
     * @throws Exception
     */
    private void search(QueryBuilder queryBuilder,String highlightFiled) throws  Exception{
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field(highlightFiled);
        highlightBuilder.preTags("<em>");
        highlightBuilder.postTags("</em>");

        SearchResponse searchResponse = client.prepareSearch("hello").setQuery(queryBuilder)
                .setFrom(0).setSize(5).highlighter(highlightBuilder).get();

        SearchHits searchHits = searchResponse.getHits();
        System.out.println("count:"+searchHits.getTotalHits());
        System.out.println("");
        Iterator<SearchHit> iterable = searchHits.iterator();
        while (iterable.hasNext()){
            SearchHit searchHit = iterable.next();
            System.out.println(searchHit.getSourceAsString());

            Map<String,Object> document = searchHit.getSourceAsMap();
            //System.out.println(document.get("id"));
            //System.out.println(document.get("title"));
            //System.out.println(document.get("content"));
            System.out.println("");
            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
            System.out.println("highlightFields:"+highlightFields);

            HighlightField highlightField = highlightFields.get(highlightFiled);
            Text[] fragments = highlightField.getFragments();
            if (fragments!=null){
                String title = fragments[0].toString();
                System.out.println(title);
            }
        }
        client.close();
    }
    /**
     * 由id查询
     * @throws Exception
     */
    @Test
    public void queryById() throws Exception{
        QueryBuilder queryBuilder = QueryBuilders.idsQuery().addIds("1","2");
        search(queryBuilder);
    }

    /**
     * 根据term查询
     * @throws Exception
     */
    @Test
    public void queryByTerm() throws Exception{
        QueryBuilder queryBuilder = QueryBuilders.termQuery("title","2");
        search(queryBuilder);
    }
    /**
     * 根据queryString查询
     * @throws Exception
     */
    @Test
    public void queryByQueryString()throws Exception{
        QueryBuilder queryBuilder = QueryBuilders.queryStringQuery("版本").defaultField("title");
        //search(queryBuilder);
        search(queryBuilder,"title");
    }
}
