//package github.mrlrf.test;
//
//import github.mrlrf.model.ZhihuAnswer;
//import github.mrlrf.model.ZhihuQuestion;
//import org.apache.ibatis.io.Resources;
//import org.apache.ibatis.session.SqlSession;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.session.SqlSessionFactoryBuilder;
//
//import java.io.InputStream;
//import java.io.Reader;
//import java.util.UUID;
//
///**
// * 类的描述
// *
// * @Author lirf
// * @Date 2017/5/27 10:43
// */
//public class MybatisTest {
//
//    public static void main(String[] args) throws Exception {
//        //mybatis的配置文件
//        String resource = "dbconfig.xml";
//        //使用类加载器加载mybatis的配置文件（它也加载关联的映射文件）
//        Reader reader = Resources.getResourceAsReader(resource);
//        InputStream is = MybatisTest.class.getClassLoader().getResourceAsStream(resource);
//        //构建sqlSession的工厂
//        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
//        //使用MyBatis提供的Resources类加载mybatis的配置文件（它也加载关联的映射文件）
//        //Reader reader = Resources.getResourceAsReader(resource);
//        //构建sqlSession的工厂
//        //SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
//        //创建能执行映射文件中sql的sqlSession
//        SqlSession session = sessionFactory.openSession();
//
//        ZhihuAnswerMapper zhihuQuestionMapper = session.getMapper(ZhihuAnswerMapper.class);
//
//        ZhihuQuestion zhihuQuestion = new ZhihuQuestion();
//        zhihuQuestion.setQuestion_id(UUID.randomUUID().toString());
//        zhihuQuestion.setQuestion_title("5");
//        zhihuQuestion.setQuestion_description("5");
//        zhihuQuestion.setQuestion_url("5");
//
//        ZhihuAnswer zhihuAnswer = new ZhihuAnswer("12344", "ceshiceskahfkasd");
//
//
//        int result = zhihuQuestionMapper.insertAnswer(zhihuAnswer);
//        //zhihuQuestion.setQuestion_id(UUID.randomUUID().toString());
//        //int r = zhihuQuestionMapper.insertQuestion(zhihuQuestion);
//        session.commit();
//        session.close();
//        /**
//         * 映射sql的标识字符串，
//         * me.gacl.mapping.userMapper是userMapper.xml文件中mapper标签的namespace属性的值，
//         * getUser是select标签的id属性值，通过select标签的id属性值就可以找到要执行的SQL
//         */
//        //String statement = "com.lirf.mybatis.mapping.ZhihuQuestionMapper.getUser";//映射sql的标识字符串
//        ////执行查询返回一个唯一user对象的sql
//        //ZhihuQuestion zq = session.selectOne(statement,"4");
//        //System.out.println(zq.getQuestion_title());
//    }
//}
