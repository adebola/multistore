package io.factorialsystems.msscstore21authorization.service;

import io.factorialsystems.msscstore21authorization.dto.AuditRequestDTO;
import io.factorialsystems.msscstore21authorization.dto.MailRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MQService {
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.audit.key}")
    private String auditRoutingKey;

    @Value("${rabbitmq.routing.mail.key}")
    private String mailRoutingKey;

    private final RabbitTemplate rabbitTemplate;

    public void audit(String action, String message, String user, String tenantId) {
        AuditRequestDTO request = new AuditRequestDTO(
                action,
                message,
                user,
                tenantId
        );

        rabbitTemplate.convertAndSend(exchange, auditRoutingKey, request);
    }

    public void sendMail(MailRequestDTO dto) {
        rabbitTemplate.convertAndSend(exchange, mailRoutingKey, dto);
    }
}
