package lucene_test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.alibaba.fastjson.*;

/**
 * @author song
 * @description: 
 * 依赖jar：Lucene-core，lucene-analyzers-common，lucene-queryparser
 * 作用：简单的索引建立
 */
public class IndexFiles {
    public static Version luceneVersion = Version.LATEST;
    /**
     * 建立索引
     
    public static void createIndex(){
        IndexWriter writer = null;
        try{
            //1、创建Directory
            //Directory directory = new RAMDirectory();//创建内存directory
            Directory directory = FSDirectory.open(Paths.get("D:/lucenetest/neswindex"));//在硬盘上生成Directory00
            //2、创建IndexWriter
            IKAnalyzer analyzer = new IKAnalyzer();
            IndexWriterConfig iwConfig = new IndexWriterConfig( analyzer);
            writer = new IndexWriter(directory, iwConfig);
            //3、创建document对象
            Document document = null;
            //4、为document添加field对象
            File f = new File("D:/lucenetest/files");//索引源文件位置
            for (File file:f.listFiles()){
                    document = new Document();
                    document.add(new StringField("path", f.getPath(),Field.Store.YES));
                    //System.out.println(file.getName());
                    document.add(new StringField("name", file.getName(),Field.Store.YES));
                    //System.out.println(file.getPath());
                    String txt = readFile(file.getPath());
                    document.add(new TextField("content", txt, Field.Store.YES)); //textField内容会进行分词
                    //document.add(new TextField("content", new FileReader(file)));  如果不用utf-8编码的话直接用这个就可以了
                    writer.addDocument(document);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            //6、使用完成后需要将writer进行关闭
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
    
    public static void createNewsIndex(String newsFile, String indexPath){
        IndexWriter writer = null;
        try{
            //1、创建Directory
            //Directory directory = new RAMDirectory();//创建内存directory
        	
        	Directory directory = FSDirectory.open(Paths.get(indexPath));//在硬盘上生成Directory00
            //2、创建IndexWriter
        	IKAnalyzer analyzer = new IKAnalyzer();
        	IndexWriterConfig iwConfig = new IndexWriterConfig( analyzer);
            writer = new IndexWriter(directory, iwConfig);
            //3、创建document对象
            Document document = null;
            //4、读取数据，逐一加入到索引文件中
            try{
            	   BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(newsFile),"UTF-8"));
            	   
            	   //网友推荐更加简洁的写法
        		   String line = "";
                   while ((line = buff.readLine()) != null) {
                	   //System.out.println(line);
                	   // 一次读入一行数据
                       //System.out.println(line);
                       JSONObject jobj = JSONObject.parseObject(line);
                       //String url = (String)jobj.get("url");
                       
                       String title = (String)jobj.get("title"); //optim
                       //System.out.println(title);
                       String text = (String)jobj.get("text"); //optim
                       System.out.println(text);                
                       
                       //String time = (String)jobj.get("time");
                       //System.out.println(time);
                       
                       document = new Document();
                       //document.add(new StringField("url", url, Field.Store.YES));
                       //document.add(new StringField("time", time, Field.Store.YES));
                       document.add(new TextField("title", title ,Field.Store.YES));
                       document.add(new TextField("text", text, Field.Store.YES)); //textField内容会进行分词
                       //document.add(new TextField("content", new FileReader(file)));  如果不用utf-8编码的话直接用这个就可以了
                       
                       
                       
                       writer.addDocument(document);
                   }
                   //System.out.println(document.toString());
               } catch (IOException e) {
                   e.printStackTrace();
               }       
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            //6、使用完成后需要将writer进行关闭
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 读取json文件，返回json串
     * @param fileName
     * @return
     
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);

            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }*/
   /**
    public static String readFile(String pathname) {
    	String txt = "";
    	
    	try (FileReader reader = new FileReader(pathname);
                BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
           ) {
               //网友推荐更加简洁的写法
    		   String line = "";
               while ((line = br.readLine()) != null) {
                   // 一次读入一行数据
                   System.out.println(line);
                   txt  += line + "\n" ;
               }
           } catch (IOException e) {
               e.printStackTrace();
           }

    	return txt; 		
    }
    */
    public static void main(String[] args) throws IOException
    {
        //createIndex();
    	createNewsIndex("D:\\lucenetest\\news\\text2_1.txt", "D:\\lucenetest\\neswindex");
    }
}