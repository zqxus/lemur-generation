package cn.afterturn.gen.core.parse.impl;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.afterturn.gen.core.parse.IParse;
import cn.afterturn.gen.model.base.GenBeanEntity;
import cn.afterturn.gen.model.base.GenerationEntity;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * FREEMARK 解析
 * @author JueYue
 * @date 2014年12月25日
 */
public class FreemakParseImpl implements IParse {

    private static final Logger               LOGGER         = LoggerFactory.getLogger(FreemakParseImpl.class);

    private static final StringTemplateLoader resourceLoader = new StringTemplateLoader();

    private static Configuration              cfg;

    static {
        try {
            cfg = new Configuration();
            cfg.setTemplateLoader(resourceLoader);
            cfg.setLocale(Locale.CHINA);
            cfg.setDefaultEncoding("UTF-8");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public List<String> parse(GenerationEntity generationEntity, GenBeanEntity tableEntity, List<String> fileList) {
        List<String> renderList = new ArrayList<>();
        Template t;
        try {
            for (String file : fileList) {
                resourceLoader.putTemplate(file, file);
                t = cfg.getTemplate(file);
                Writer write = new StringWriter();
                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put(IParse.GEN_PARAMS, generationEntity);
                paramsMap.put(IParse.TABLE_DETAIL, tableEntity);
                t.process(paramsMap, write);
                renderList.add(write.toString());
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return renderList;
    }

}
