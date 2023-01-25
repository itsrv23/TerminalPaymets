package ru.itsrv23.terminal.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import ru.itsrv23.terminal.model.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ActiveProfiles("test")
@SpringBootTest
class ProcessPaymentTest {
    @SpyBean
    private ProcessPayment processPayment;

    static Payment expected(){
        return Payment.builder()
                .paymentCr(LocalDateTime.parse("24.01.2023 11:50:55", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")))
                .amount(BigDecimal.valueOf(10))
                .account(123456)
                .session("2401231150554071121B")
                .build();
    }

    @Test
    void readXml() throws Exception {
        Payment payment = processPayment.parseFile();

        Assertions.assertThat(payment.getAccount()).isEqualTo(expected().getAccount());
        Assertions.assertThat(payment.getAmount()).isEqualTo("10.00");
    }

    @Test
    void convertDt() {
        LocalDateTime dt = LocalDateTime.parse("24.01.2023 11:50:55", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        System.out.println("offsetDateTime = " + dt);
    }

    @Test
    void testWrite() throws Exception{
        processPayment.writeOnSuccess(expected());
    }

}