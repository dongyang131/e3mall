package cn.e3mall.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.dao.SearchDao;
import cn.e3mall.search.service.SearchService;
@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchDao searchDao;
	@Override
	public SearchResult search(String keyWord, int page, int rows) throws Exception {
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery(keyWord);
		solrQuery.setStart((page-1)*rows);
		solrQuery.setRows(rows);
		solrQuery.set("df", "item_title");
		solrQuery.setHighlight(true);
		solrQuery.setHighlightSimplePre("<em style=\"color:red\">");
		solrQuery.setHighlightSimplePost("</em>");
		SearchResult searchResult = searchDao.search(solrQuery);
		long recourdCount = searchResult.getRecourdCount();
		long pageCount = recourdCount/rows;
		if(recourdCount % rows != 0){
			pageCount++;
		}
		searchResult.setTotalPages(pageCount);
		
		return searchResult;
	}

}
