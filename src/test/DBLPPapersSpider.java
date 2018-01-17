//package github.mrlrf.test;
//
//import github.mrlrf.crawlercore.Spider;
//import github.mrlrf.model.DblpConference;
//import github.mrlrf.mybatis.mapper.DblpConferenceMapper;
//import org.apache.ibatis.session.SqlSession;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.session.SqlSessionFactoryBuilder;
//
//import java.io.InputStream;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 类的描述
// *
// * @Author lirf
// * @Date 2017/5/27 14:11
// */
//public class DBLPPapersSpider {
//    public static boolean conferenceSpider (Map<String, String> input) throws Exception {
//        //mybatis的配置文件
//        String resource = "dbconfig.xml";
//        InputStream is = MybatisTest.class.getClassLoader().getResourceAsStream(resource);
//        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
//        SqlSession session = sessionFactory.openSession();
//        DblpConferenceMapper dblpConferenceMapper = session.getMapper(DblpConferenceMapper.class);
//        DblpPaperMapper dblpPaperMapper = session.getMapper(DblpPaperMapper.class);
//
//        for (String key : input.keySet()) {
//            String source= Spider.sendGet(input.get(key));
//            System.out.println("Begin---------");
//            List<DblpConference> conferences = Spider.regexConference(key, source);
//
//            int i = 0;
//            for (DblpConference dblpConference : conferences) {
//                int result = dblpConferenceMapper.insertConference(dblpConference);
//                System.out.println("conference:" + i++);
//                int j = 0;
//                for (Paper paper : dblpConference.getPapers()) {
//                    int result1 = dblpPaperMapper.insertPaper(paper);
//                    System.out.println("paper:" + j++);
//                    if (result1 < 1) {
//                        session.close();
//                        return false;
//                    }
//                }
//                if (result < 1) {
//                    session.close();
//                    return false;
//                }
//            }
//        }
//        session.commit();
//        session.close();
//        return true;
//    }
//
//    public static void main(String[] args) throws Exception {
//        Map<String, String > input = new HashMap<String, String>();
//        input.put("recsys", "http://dblp.uni-trier.de/db/conf/recsys/");
//        input.put("sigmod", "http://dblp.uni-trier.de/db/conf/sigmod/");
//
//        System.out.println(conferenceSpider(input));
//    }
//}
