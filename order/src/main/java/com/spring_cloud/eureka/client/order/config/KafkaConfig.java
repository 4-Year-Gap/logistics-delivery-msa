package com.spring_cloud.eureka.client.order.config;



import com.spring_cloud.eureka.client.order.application.IdentityIntegrationCommand;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.OrderCreateEvent;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.OrderStatusEvent;
import com.spring_cloud.eureka.client.order.infrastructure.client.dto.ProductUpdateEvent;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;


import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {



    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.password}")
    private String password;

    @Value("${spring.kafka.user-name}")
    private String username;


    private Map<String, Object> commonConfig() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);// 타입 정보 생략 가능
        return configProps;
    }
    @Bean
    public ProducerFactory<String, OrderCreateEvent> orderProducerFactory() {

        Map<String, Object> configProps = new HashMap<>(commonConfig());
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, OrderCreateEvent.class);
        configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        configProps.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        String jaasConfig = String.format(
                "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";",
                username, password);
        configProps.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, OrderCreateEvent> createKafkaTemplate() {
        return new KafkaTemplate<>(orderProducerFactory());
    }

    @Bean
    public ProducerFactory<String, IdentityIntegrationCommand> userProducerFactory() {

        Map<String, Object> configProps = new HashMap<>(commonConfig());
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, IdentityIntegrationCommand.class);
        configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        configProps.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        String jaasConfig = String.format(
                "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";",
                username, password);
        configProps.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, IdentityIntegrationCommand> updateKafkaTemplate() {
        return new KafkaTemplate<>(userProducerFactory());
    }

    @Bean
    public ProducerFactory<String, ProductUpdateEvent> ProductproducerFactory() {

        Map<String, Object> configProps = new HashMap<>(commonConfig());
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ProductUpdateEvent.class);
        configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        configProps.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        String jaasConfig = String.format(
                "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";",
                username, password);
        configProps.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, ProductUpdateEvent> productUpdateKafkaTemplate() {
        return new KafkaTemplate<>(ProductproducerFactory());
    }


    @Bean
    public ProducerFactory<String, OrderStatusEvent> statusproducerFactory() {

        Map<String, Object> configProps = new HashMap<>(commonConfig());
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, OrderStatusEvent.class);
        configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        configProps.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        String jaasConfig = String.format(
                "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";",
                username, password);
        configProps.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, OrderStatusEvent> statusKafkaTemplate() {
        return new KafkaTemplate<>(statusproducerFactory());
    }
}
