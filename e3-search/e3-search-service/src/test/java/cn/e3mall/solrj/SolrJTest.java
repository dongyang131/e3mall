package cn.e3mall.solrj;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import com.sun.tools.internal.xjc.model.SymbolSpace;

public class SolrJTest {
	@Test
	public void addDocument() throws Exception{
		//创建一个SolrServer对象
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.155:8080/solr/collection1");
		//创建一个文档对象
		SolrInputDocument document = new SolrInputDocument();
		//向文档对象中添加域
		document.addField("id", "1");
		document.addField("item_title", "测试商品标题");
		document.addField("item_price", 1000);
		//把文档对象写入索引库
		solrServer.add(document);
		//提交
		solrServer.commit();
	}
	@Test
	public void test11() throws Exception{
		SolrServer server = new HttpSolrServer("http://192.168.25.155:8080/solr/collection1");
		SolrInputDocument document= new SolrInputDocument();
		document.addField("id", "2");
		document.addField("item_title", "laiya");
		document.addField("item_price", 2000);
		server.add(document);
		server.commit();
	}
	@Test
	public void deleteDocumentById() throws Exception{
		//创建一个SolrServer对象
		SolrServer server = new HttpSolrServer("http://192.168.25.155:8080/solr/collection1");
		//根据Id删除
		server.deleteById("1");	
		//提交
		server.commit();
	}
	@Test
	public void deleteDocumentByQuery() throws Exception{
		//创建一个SolrServer对象
		SolrServer server = new HttpSolrServer("http://192.168.25.155:8080/solr/collection1");
		//根据查询删除
		server.deleteByQuery("*:*");
		//提交
		server.commit();
	}
	@Test
	public void searcheIndex() throws Exception{
		//创建一个SolrServer对象
		SolrServer server = new HttpSolrServer("http://192.168.25.155:8080/solr/collection1");
		//创建一个solrQury对象
		SolrQuery query = new SolrQuery();
		query.setQuery("*:*");
		//执行查询得到一个QueryResponse对象
		//query.set("q", "*:*");
		QueryResponse queryResponse = server.query(query);
		//取查询结果
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		//查询结果的总记录数
		long numFound = solrDocumentList.getNumFound();
		System.out.println(numFound);
		//查询结果列表
		for (SolrDocument solrDocument : solrDocumentList) {
			//打印
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_sell_point"));
		}
	}
	@Test
	public void searchIndex2() throws Exception{
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.155:8080/solr/collection1");
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.set("q", "*:*");
		QueryResponse queryResponse = solrServer.query(solrQuery);
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		System.out.println("总记录数:"+solrDocumentList.getNumFound());
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_sell_point"));
		}
	}
	@Test
	public void searchIndexWithHighLight() throws Exception{
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.155:8080/solr/collection1");
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery("手机");
		solrQuery.set("df", "item_title");
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("item_title");
		solrQuery.setHighlightSimplePre("<em>");
		solrQuery.setHighlightSimplePost("</em>");
		QueryResponse queryResponse = solrServer.query(solrQuery);
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		System.out.println(solrDocumentList.getNumFound());
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			String title = "";
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			if(list !=null && list.size()>0){
				title = list.get(0);
			}else{
				title = solrDocument.get("item_title").toString();
			}
			System.out.println(title);
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_sell_point"));
		}
	}
	@Test
	public void indexSearcheHighTest() throws Exception{
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.155:8080/solr/collection1");
		SolrQuery solrQuery = new  SolrQuery();
		solrQuery.setQuery("手机");
		solrQuery.set("df", "item_title");
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("item_title");
		solrQuery.setHighlightSimplePre("<em>");
		solrQuery.setHighlightSimplePost("</em>");
		QueryResponse queryResponse = solrServer.query(solrQuery);
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		System.out.println("总共:"+solrDocumentList.getNumFound());
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String title = "";
			if(list!=null && list.size()>0){
				title = list.get(0);
			}else{
				title = solrDocument.get("item_title").toString();
			}
			System.out.println(title);
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_sell_point"));
		}
		
				
	}
}
