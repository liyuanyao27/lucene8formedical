package lucene_test;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TotalHits;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * @author song
 * @description: 
 * 依赖jar：Lucene-core，lucene-analyzers-common，lucene-queryparser
 * 作用：使用索引搜索文件
 */
public class SearchFiles {
    public static Version luceneVersion = Version.LATEST;
    /**
     * 查询内容
     */
    public static Vector indexSearch(String keywords){
        String res = "";
        DirectoryReader reader = null;
        try{
        	 String[] fields = { "text", "title" };
//            1、创建Directory
             Directory directory = FSDirectory.open(Paths.get("D:\\lucenetest\\neswindex"));//在硬盘上生成Directory
//            2、创建IndexReader
             reader = DirectoryReader.open(directory);
//            3、根据IndexWriter创建IndexSearcher
             IndexSearcher searcher =  new IndexSearcher(reader);
             Map<String, Float> boosts = new HashMap<String, Float>();
             boosts.put("title", 1f);
             boosts.put("text", 2f);

             MultiFieldQueryParser parser = new MultiFieldQueryParser(fields,
            		 new IKAnalyzer(), boosts);
             
             // Query query = parser.parse("name:lucene OR description:lucene");
             //等同于name:lucene OR description:lucene
             Query query = parser.parse(keywords);
             //System.out.println(parser.toString());
             TopDocs topDocs = searcher.search(query,1);
     		// 根据查询条件匹配出的记录总数
            
             
             
             //TotalHits count = topDocs.totalHits;
     		//System.out.println("匹配出的记录总数:" + count);
     		// 根据查询条件匹配出的记录
     		//String hit=count.toString();
     		//String regEx="[^0-9]";
     		//Pattern p=Pattern.compile(regEx);
     		//Matcher m=p.matcher(hit);
     		//String hits=m.replaceAll("").trim();
     		//String b = String.valueOf(hits);
     		//int c = Integer.parseInt(b);
     		//对count进行编辑使之只输出数字
     		
     		
     		
     		
     		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
     		for (ScoreDoc scoreDoc : scoreDocs) {
     			// 获取文档的ID
     			int docId = scoreDoc.doc;
     			// 通过ID获取文档
     			Document doc = searcher.doc(docId);
     			
     			
     			
     			
     			
     			
     			String zhixin= searcher.explain(query,docId).toString();//把得分解释字符串化
     			int count_freq = searchCount("(freq=", zhixin);//freq在文中出现了多少次
     			int x=0;
     			float textplus=0;
     			float titleplus=0;
     			float array_title[] = new float[count_freq];
     			float array_text[]=new float[count_freq];
     			for(int i=0;i<count_freq;i++){
     				
     				int zhixin_index1=zhixin.indexOf("(freq=",x);
     				x=x+zhixin_index1+6;
     				String o1=zhixin.substring(zhixin_index1-70,zhixin_index1-60);//返回title或者text的那段文字

     				String o=zhixin.substring(zhixin_index1+6,zhixin_index1+9);//返回匹配次数
     				if (o1.indexOf("text")!=-1) {
     					
     					float   y2   =   Float.parseFloat(o);
     					array_text[i]=y2;
     					textplus=(y2+textplus)*5/100;
     					//System.out.println(y1+"text");
     				}
     				else if (o1.indexOf("title")!=-1) {
     					

     					float   y1   =   Float.parseFloat(o);
     					array_title[i]=y1;
     					titleplus=(y1*2+titleplus)*5/100;
     		     		//System.out.println(y1+"title");
     				}
     				
     				
     				
     				
     			}
     			
     			float plusresult=titleplus+textplus;
     			
     			
     			
     			
     			
     			//System.out.println(searcher.explain(query,docId));
     			
     			
     			
     			
     			
     			
     			
     			//System.out.println("text：" + doc.get("text"));
     			//System.out.println("时间：" + doc.get("time"));
     			//System.out.println("商品价格：" + doc.get("price"));
     			//System.out.println("标题：" + doc.get("title"));
     			//System.out.println("==========================");
     			Vector vec=new Vector();
         		vec.add(plusresult);
     			vec.add(doc.get("text"));
             /*//            4、创建搜索的query
//            创建parse用来确定搜索的内容，第二个参数表示搜索的域
             QueryParser parser = new QueryParser("fields",new StandardAnalyzer());//content表示搜索的域或者说字段
             Query query = parser.parse(keywords);//被搜索的内容
//            5、根据Searcher返回TopDocs
             TopDocs topDocs = searcher.search(query, 1000);
     		// 根据查询条件匹配出的记录总数
     		TotalHits count = topDocs.totalHits;
     		System.out.println("匹配出的记录总数:" + count);
     		// 根据查询条件匹配出的记录
     		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
     		for (ScoreDoc scoreDoc : scoreDocs) {
     			// 获取文档的ID
     			int docId = scoreDoc.doc;
     			// 通过ID获取文档
     			Document doc = searcher.doc(docId);
     			System.out.println("URL：" + doc.get("url"));
     			System.out.println("时间：" + doc.get("time"));
     			//System.out.println("商品价格：" + doc.get("price"));
     			System.out.println("标题：" + doc.get("title"));
     			System.out.println("==========================");
     		}*/

     			return vec;
     		}    
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            //9、关闭reader
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
        public static int searchCount(String shortStr, String longStr) {
    	         // 定义一个count来存放字符串出现的次数
    	         int count = 0;
    	         // 调用String类的indexOf(String str)方法，返回第一个相同字符串出现的下标
    	         while (longStr.indexOf(shortStr) != -1) {
    	             // 如果存在相同字符串则次数加1
    	             count++;
    	             // 调用String类的substring(int beginIndex)方法，获得第一个相同字符出现后的字符串
    	             longStr = longStr.substring(longStr.indexOf(shortStr)
    	                 + shortStr.length());
    	 
    	        }
    	         // 返回次数
    	         return count;
    	     }
    
    
    public static void putin(String a) {
    	indexSearch(a);
    }
    
    public static void main(String[] args) throws IOException
    {
        System.out.println(indexSearch("我网")); //搜索的内容可以修改
    }
}
