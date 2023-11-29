package com.aurora.consumer;

import com.alibaba.fastjson.JSON;
import com.aurora.model.dto.ArticleSearchDTO;
import com.aurora.model.dto.MaxwellDataDTO;
import com.aurora.entity.Article;
import com.aurora.mapper.ElasticsearchMapper;
import com.aurora.util.BeanCopyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.aurora.constant.RabbitMQConstant.MAXWELL_QUEUE;

@Component
@RabbitListener(queues = MAXWELL_QUEUE)
@Slf4j
public class MaxWellConsumer {

    @Autowired
    private ElasticsearchMapper elasticsearchMapper;

    @RabbitHandler
    public void process(byte[] data) {
        try{

            // 添加日志输出语句
            System.out.println("Received message: " + new String(data));
            MaxwellDataDTO maxwellDataDTO = JSON.parseObject(new String(data), MaxwellDataDTO.class);
            System.out.println("============maxwellDataDTO:" + maxwellDataDTO);
            Article article = JSON.parseObject(JSON.toJSONString(maxwellDataDTO.getData()), Article.class);
            System.out.println("==========acticle:"+ article);
            switch (maxwellDataDTO.getType()) {
                case "insert":
                case "update":
                    elasticsearchMapper.save(BeanCopyUtil.copyObject(article, ArticleSearchDTO.class));
                    break;
                case "delete":
                    elasticsearchMapper.deleteById(article.getId());
                    break;
                default:
                    break;
            }

        }catch (Exception e){
            log.error("MaxWellConsumer.process方法异常："+e.getMessage());
        }

    }
}
