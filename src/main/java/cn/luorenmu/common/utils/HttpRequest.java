package cn.luorenmu.common.utils;

import cn.hutool.http.HttpResponse;
import cn.luorenmu.common.convert.RequestContentConvert;
import cn.luorenmu.common.file.FileManager;
import cn.luorenmu.entiy.config.Request;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author LoMu
 * Date 2023.12.02 20:32
 */

//进一步封装Http请求

@Slf4j
public class HttpRequest {

    public static HttpResponse execute(Request.RequestDetailed requestDetailed) {
        String requestDetailedMethodmethod = requestDetailed.getMethod();


        try {
            cn.hutool.http.HttpRequest httpRequest = (cn.hutool.http.HttpRequest) RequestContentConvert.class
                    .getMethod("requestTo" + StringUtils.firstCharacterUpperCase(requestDetailedMethodmethod),
                            Request.RequestDetailed.class)
                    .invoke(null, requestDetailed);

            httpRequest.headerMap(Map.of("DS", StringUtils.getDS(), "x-rpc-client_type", " 2", "x-rpc-app_version", " 2.61.1"), true);
            HttpResponse execute = httpRequest.execute();
            log.debug(execute.toString());

            return execute;
        } catch (NoSuchMethodException e) {
            log.error("not support request method : {}", requestDetailed);
            throw new RuntimeException(e);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }


    public static void main(String[] args) {
        Request config = FileManager.getConfig(Request.class);
        Request.RequestDetailed articleCollect = config.getMihoyo().getArticleCollect();
        HttpResponse execute = execute(articleCollect);
        System.out.println(execute.body());


    }

}