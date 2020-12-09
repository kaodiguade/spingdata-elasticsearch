import com.itheima.es.entity.Article;
import com.itheima.es.repositories.ArticleRepository;
import org.elasticsearch.ElasticsearchTimeoutException;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class SpringDataEsTest {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Test
    public void createIndex() throws Exception{
        elasticsearchTemplate.createIndex(Article.class);
    }
    @Test
    public void addDocument() throws Exception{
        for (int i = 3; i <20 ; i++) {
            Article article = new Article();
            article.setId(i);
            article.setTitle("习近平视察海军陆战队"+i);
            article.setContent("习近平在视察海军陆战队时强调 加快推进转型建设 加快提升作战能力 努力锻造一支合成多能快速反应全域运用的精兵劲旅"+i);
            articleRepository.save(article);
        }
    }
    @Test
    public void deleteDocument() throws Exception{
        articleRepository.deleteById(1l);
    }
    @Test
    public void updateDocument() throws Exception{
        //先删除，后添加
        addDocument();//更改内容就行
    }
    @Test
    public void findAll() throws Exception{
        Iterable<Article> iterable = articleRepository.findAll();
        iterable.forEach(a -> System.out.println(a.getContent()));
    }
    @Test
    public void findById() throws Exception{
        Optional<Article> optionalArticle = articleRepository.findById(2l);
        Article article = optionalArticle.get();
        System.out.println(article.getContent());
    }
    @Test
    public void findByTitle() throws Exception{
        List<Article> listByTitle = articleRepository.findByTitle("习近平");
        listByTitle.stream().forEach(a -> System.out.println(a));
    }
    @Test
    public void findByTitleOrContent() throws Exception{
        Pageable pageable = PageRequest.of(0,15);
        List<Article> list = articleRepository.findByTitleOrContent("习近平","3",pageable);
        list.stream().forEach(a -> System.out.println(a.getContent()));
    }
    @Test
    public void nativeSearchQuery() throws Exception{
        Query query = new NativeSearchQueryBuilder().withQuery(QueryBuilders.queryStringQuery("习近平").defaultField("title")).withPageable(PageRequest.of(0,15)).build();
        //elasticsearchTemplate.queryForList(query,Article.class);
        //list.stream().forEach(a -> System.out.println(a.getContent()));

    }
}
