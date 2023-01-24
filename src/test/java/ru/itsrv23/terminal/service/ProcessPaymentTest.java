package ru.itsrv23.terminal.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import ru.itsrv23.terminal.model.Payment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ActiveProfiles("test")
@SpringBootTest
class ProcessPaymentTest {
    @SpyBean
    private ProcessPayment processPayment;

    @Test
    void readXml() throws Exception {
        Payment payment = processPayment.parseFile();

        Assertions.assertThat(payment.getAuto()).isEqualTo(982);
        Assertions.assertThat(payment.getAmount()).isEqualTo("800.00");
    }

    @Test
    void convertDt() {
        LocalDateTime dt = LocalDateTime.parse("24.01.2023 11:50:55", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        System.out.println("offsetDateTime = " + dt);
    }

}