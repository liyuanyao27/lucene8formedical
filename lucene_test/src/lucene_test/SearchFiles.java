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
 * ����jar��Lucene-core��lucene-analyzers-common��lucene-queryparser
 * ���ã�ʹ�����������ļ�
 */
public class SearchFiles {
    public static Version luceneVersion = Version.LATEST;
    /**
     * ��ѯ����
     */
    public static Vector indexSearch(String keywords){
        String res = "";
        DirectoryReader reader = null;
        try{
        	 String[] fields = { "text", "title" };
//            1������Directory
             Directory directory = FSDirectory.open(Paths.get("D:\\lucenetest\\neswindex"));//��Ӳ��������Directory
//            2������IndexReader
             reader = DirectoryReader.open(directory);
//            3������IndexWriter����IndexSearcher
             IndexSearcher searcher =  new IndexSearcher(reader);
             Map<String, Float> boosts = new HashMap<String, Float>();
             boosts.put("title", 1f);
             boosts.put("text", 2f);

             MultiFieldQueryParser parser = new MultiFieldQueryParser(fields,
            		 new IKAnalyzer(), boosts);
             
             // Query query = parser.parse("name:lucene OR description:lucene");
             //��ͬ��name:lucene OR description:lucene
             Query query = parser.parse(keywords);
             //System.out.println(parser.toString());
             TopDocs topDocs = searcher.search(query,1);
     		// ���ݲ�ѯ����ƥ����ļ�¼����
            
             
             
             //TotalHits count = topDocs.totalHits;
     		//System.out.println("ƥ����ļ�¼����:" + count);
     		// ���ݲ�ѯ����ƥ����ļ�¼
     		//String hit=count.toString();
     		//String regEx="[^0-9]";
     		//Pattern p=Pattern.compile(regEx);
     		//Matcher m=p.matcher(hit);
     		//String hits=m.replaceAll("").trim();
     		//String b = String.valueOf(hits);
     		//int c = Integer.parseInt(b);
     		//��count���б༭ʹֻ֮�������
     		
     		
     		
     		
     		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
     		for (ScoreDoc scoreDoc : scoreDocs) {
     			// ��ȡ�ĵ���ID
     			int docId = scoreDoc.doc;
     			// ͨ��ID��ȡ�ĵ�
     			Document doc = searcher.doc(docId);
     			
     			
     			
     			
     			
     			
     			String zhixin= searcher.explain(query,docId).toString();//�ѵ÷ֽ����ַ�����
     			int count_freq = searchCount("(freq=", zhixin);//freq�����г����˶��ٴ�
     			int x=0;
     			float textplus=0;
     			float titleplus=0;
     			float array_title[] = new float[count_freq];
     			float array_text[]=new float[count_freq];
     			for(int i=0;i<count_freq;i++){
     				
     				int zhixin_index1=zhixin.indexOf("(freq=",x);
     				x=x+zhixin_index1+6;
     				String o1=zhixin.substring(zhixin_index1-70,zhixin_index1-60);//����title����text���Ƕ�����

     				String o=zhixin.substring(zhixin_index1+6,zhixin_index1+9);//����ƥ�����
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
     			
     			
     			
     			
     			
     			
     			
     			//System.out.println("text��" + doc.get("text"));
     			//System.out.println("ʱ�䣺" + doc.get("time"));
     			//System.out.println("��Ʒ�۸�" + doc.get("price"));
     			//System.out.println("���⣺" + doc.get("title"));
     			//System.out.println("==========================");
     			Vector vec=new Vector();
         		vec.add(plusresult);
     			vec.add(doc.get("text"));
             /*//            4������������query
//            ����parse����ȷ�����������ݣ��ڶ���������ʾ��������
             QueryParser parser = new QueryParser("fields",new StandardAnalyzer());//content��ʾ�����������˵�ֶ�
             Query query = parser.parse(keywords);//������������
//            5������Searcher����TopDocs
             TopDocs topDocs = searcher.search(query, 1000);
     		// ���ݲ�ѯ����ƥ����ļ�¼����
     		TotalHits count = topDocs.totalHits;
     		System.out.println("ƥ����ļ�¼����:" + count);
     		// ���ݲ�ѯ����ƥ����ļ�¼
     		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
     		for (ScoreDoc scoreDoc : scoreDocs) {
     			// ��ȡ�ĵ���ID
     			int docId = scoreDoc.doc;
     			// ͨ��ID��ȡ�ĵ�
     			Document doc = searcher.doc(docId);
     			System.out.println("URL��" + doc.get("url"));
     			System.out.println("ʱ�䣺" + doc.get("time"));
     			//System.out.println("��Ʒ�۸�" + doc.get("price"));
     			System.out.println("���⣺" + doc.get("title"));
     			System.out.println("==========================");
     		}*/

     			return vec;
     		}    
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            //9���ر�reader
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
        public static int searchCount(String shortStr, String longStr) {
    	         // ����һ��count������ַ������ֵĴ���
    	         int count = 0;
    	         // ����String���indexOf(String str)���������ص�һ����ͬ�ַ������ֵ��±�
    	         while (longStr.indexOf(shortStr) != -1) {
    	             // ���������ͬ�ַ����������1
    	             count++;
    	             // ����String���substring(int beginIndex)��������õ�һ����ͬ�ַ����ֺ���ַ���
    	             longStr = longStr.substring(longStr.indexOf(shortStr)
    	                 + shortStr.length());
    	 
    	        }
    	         // ���ش���
    	         return count;
    	     }
    
    
    public static void putin(String a) {
    	indexSearch(a);
    }
    
    public static void main(String[] args) throws IOException
    {
        System.out.println(indexSearch("����")); //���������ݿ����޸�
    }
}
