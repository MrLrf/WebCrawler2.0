//package github.mrlrf.test;
//
//import github.mrlrf.crawlercore.Spider;
//import github.mrlrf.model.Zhihu;
//import github.mrlrf.mybatis.mapper.ZhihuAnswerMapper;
//import github.mrlrf.mybatis.mapper.ZhihuQuestionMapper;
//import github.mrlrf.model.ZhihuAnswer;
//import github.mrlrf.model.ZhihuQuestion;
//import org.apache.ibatis.session.SqlSession;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.session.SqlSessionFactoryBuilder;
//
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.UUID;
//
///**抓取知乎多条问题和对应问题的链接的测试类
// * Created by codingBoy on 16/10/20.
// */
//public class Main4 {
//    public static void main(String[] args) throws Exception {
//        String url="https://www.zhihu.com/explore/recommendations";
//        String source= Spider.sendGet(url);
//
//        System.out.println(source);
//
//        ArrayList<Zhihu> results;
//
//        results=Spider.regexString(source);
//
//        //mybatis的配置文件
//        String resource = "dbconfig.xml";
//        InputStream is = MybatisTest.class.getClassLoader().getResourceAsStream(resource);
//        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
//        SqlSession session = sessionFactory.openSession();
//        ZhihuQuestionMapper zhihuQuestionMapper = session.getMapper(ZhihuQuestionMapper.class);
//        ZhihuAnswerMapper zhihuAnswerMapper = session.getMapper(ZhihuAnswerMapper.class);
//
//        for (Zhihu zhihu:results) {
//            String question_id = UUID.randomUUID().toString();
//            ZhihuQuestion zhihuQuestion = new ZhihuQuestion();
//            zhihuQuestion.setQuestion_id(question_id);
//            zhihuQuestion.setQuestion_title(zhihu.getTitle());
//            zhihuQuestion.setQuestion_description(zhihu.getTitledescription());
//            zhihuQuestion.setQuestion_url(zhihu.getZhihuUrl());
//
//            for (String answer : zhihu.getAnwsers()) {
//                ZhihuAnswer zhihuAnswer = new ZhihuAnswer(question_id,answer);
//                zhihuAnswerMapper.insertAnswer(zhihuAnswer);
//            }
//
//            int result = zhihuQuestionMapper.insertQuestion(zhihuQuestion);
//            if (result <= 0) {
//                session.close();
//                return;
//            }
//        }
//        session.commit();
//        session.close();
//    }
//}
