package cn.et;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class Test {
	//指定索引库的位置
	private static String dir = "E:\\index";
	//定义分词器 当参数为true时，分词器进行智能切分    默认细粒度切分算法
	private static IKAnalyzer ika = new IKAnalyzer(true);
	public static void main(String[] args) throws IOException, ParseException {
		search();
		System.out.println("OK");
	}
	public static void search() throws IOException, ParseException{
		//指定索引库的存储目录
		Directory directory = FSDirectory.open(new File(dir));
		//读取索引库的存储目录
		DirectoryReader ireader = DirectoryReader.open(directory);
		//搜索类
	    IndexSearcher isearcher = new IndexSearcher(ireader);
	    //lucene的查询解析器   用于指定查询的属性名和分词器
	    QueryParser parser = new QueryParser(Version.LUCENE_47, "userAddress", ika);
	    //搜索
	    Query query = parser.parse("省");
	    //获取搜索结果    可以指定返回的doucment个数   根据得分排序
	    ScoreDoc[] hits = isearcher.search(query, null, 10).scoreDocs;
	    for (int i = 0; i < hits.length; i++) {
	    	Document hitDoc = isearcher.doc(hits[i].doc);
	    	System.out.println(hitDoc.get("userAddress"));//hitDoc.getField(fieldName).stringValue()
	    }
	    ireader.close();
	    directory.close();
	}
	/**
	 * 创建索引库
	 * @throws IOException 
	 */
	public static void write() throws IOException{
		//指定索引库的存储目录
		Directory directory = FSDirectory.open(new File(dir));
		//索引创建器配置    将分词器与lucene版本关联
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, ika);
		//新建索引创建器   传入索引库的存储目录和分词器
		IndexWriter iwriter = new IndexWriter(directory, config);
		//新建   document 对象
		Document doc = new Document();
		//添加field 属性                     属性名                   属性值    [写入索引库(TYPE_NOT_STORED 不写入)] 属性值传入byte数组为默认写入
		doc.add(new Field("userName", "张三", TextField.TYPE_STORED));
		doc.add(new Field("userAddress", "广东省  深圳市  罗湖区", TextField.TYPE_STORED));
		Document doc1 = new Document();
		doc1.add(new Field("userName", "李四", TextField.TYPE_STORED));
		doc1.add(new Field("userAddress", "广西省  桂林市  七星区", TextField.TYPE_STORED));
		//索引创建器中添加doucment
		iwriter.addDocument(doc);
		iwriter.addDocument(doc1);
		//事务提交   索引创建
		iwriter.commit();
		iwriter.close();
	}
}
