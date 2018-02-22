package cn.et;
import java.io.IOException;  
import java.io.StringReader;  
  
import org.apache.lucene.analysis.TokenStream;  
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;  
import org.wltea.analyzer.core.IKSegmenter;  
import org.wltea.analyzer.core.Lexeme;  
import org.wltea.analyzer.lucene.IKAnalyzer;  
  
@SuppressWarnings("resource")  
public class IKAnalyzerTest {  
    public static void main(String[] args) throws Exception {
        demo();
    }
    // 分词测试  
    public static void demo() throws Exception {
    	//定义分词器 当参数为true时，分词器进行智能切分    默认细粒度切分算法
        IKAnalyzer ika = new IKAnalyzer();
        System.out.println("当前使用的分词器：" + ika.getClass().getSimpleName());
        TokenStream tokenStream = ika.tokenStream("content", "The participle is intelligent syncopation");
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();// 必须先调用reset方法
        while (tokenStream.incrementToken()) {
            System.out.println(new String(charTermAttribute.buffer()));
        }
        tokenStream.close();
    }
    // 单独的使用IKAnalyzer，可以直接使用IK分词器的核心类，真正分词的实现类IKSegmenter分词器进行分词
    public static void demo1() throws Exception {
        StringReader reader = new StringReader("The participle is intelligent syncopation");
        IKSegmenter ik = new IKSegmenter(reader, true);// 当为true时，分词器进行最大词长切分
        Lexeme lexeme = null;
        try {
            while ((lexeme = ik.next()) != null)
                System.out.println(lexeme.getLexemeText());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            reader.close();
        }
    }
}
