package cn.e3mall.search.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.dao.SearchDao;
@Repository
public class SearchDaoImpl implements SearchDao{

	@Autowired
	private SolrServer solrServer;
	@Override
	public SearchResult search(SolrQuery solrQuery) throws Exception {
		QueryResponse queryResponse = solrServer.query(solrQuery);
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		long numFound = solrDocumentList.getNumFound();
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		List<SearchItem> list1 = new ArrayList<>();
		for (SolrDocument solrDocument : solrDocumentList) {
			SearchItem searchItem =new SearchItem();
			searchItem.setCategory_name((String) solrDocument.get("item_category_name"));
			searchItem.setId((String) solrDocument.get("id"));
			searchItem.setImage((String) solrDocument.get("item_image"));
			searchItem.setPrice((long) solrDocument.get("item_price"));
			searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
			String itemTitle = "";
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			if(list!=null && list.size()>0){
				itemTitle=list.get(0);
			}else{
				itemTitle = (String) solrDocument.get("item_title");
			}
			searchItem.setTitle(itemTitle);
			list1.add(searchItem);
		}
		SearchResult searchResult = new SearchResult();
		searchResult.setRecourdCount(numFound);
		searchResult.setItemList(list1);
		return searchResult;
	}
}
