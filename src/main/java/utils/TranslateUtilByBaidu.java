package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import github.mrlrf.model.BaiduTranslateResult;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Google翻译工具
 *
 * @Author lirf
 * @Date 2017/6/10 18:29
 */
public class TranslateUtilByBaidu {
    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";

    private static final String APPID = "20170620000059595";
    private static final String SECURITYKEY = "WJxS1kn7HWkQmVGXKkul";

    private static final String ENGLISH = "en";
    private static final String CHINA = "zh";

    /**
     * 百度翻译
     *
     * @param query   翻译文本
     * @param from    翻译语系
     * @param to      目标语系
     * @return
     * @throws Exception
     */
    public static String getTransResult(String query, String from, String to) {
        Map<String, String> params = buildParams(query, from, to);
        return HttpGet.get(TRANS_API_HOST, params);
    }

    private static Map<String, String> buildParams(String query, String from, String to) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);

        params.put("appid", APPID);

        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);

        // 签名
        String src = APPID + query + salt + SECURITYKEY; // 加密前的原文
        params.put("sign", MD5.md5(src));

        return params;
    }

    /**
     * 百度翻译: 英文-->简中
     *
     * @param text   翻译文本
     * @return
     * @throws Exception
     */
    public static String en2chs(final String text) throws Exception {
        String transResult = getTransResult(text, ENGLISH, CHINA);

        ObjectMapper mapper = new ObjectMapper();
        BaiduTranslateResult baiduTranslateResult = mapper.readValue(transResult, BaiduTranslateResult.class);
        String result = baiduTranslateResult.getTrans_result().get(0).get("dst");
        return result;
    }

    @Test
    public void mainTest() throws Exception {
        String result = getTransResult("This is a question", ENGLISH, CHINA);
        System.out.println(result);
        ObjectMapper mapper = new ObjectMapper();
        BaiduTranslateResult baiduTranslateResult = mapper.readValue(result, BaiduTranslateResult.class);
        System.out.println(baiduTranslateResult.getTrans_result().get(0).get("dst"));
        //System.out.println(map.get("trans_result"));
    }
}
