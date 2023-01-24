package ru.itsrv23.terminal.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import ru.itsrv23.terminal.model.Payment;
import ru.itsrv23.terminal.service.ProcessPayment;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class Schedule {

    private final ProcessPayment processPayment;

    @Scheduled(fixedRate = 1_000_000)
    public void read() throws IOException {
        System.out.println("start = " + log);
        Payment payment = processPayment.parseFile();
//        boolean b = Files.deleteIfExists(payment.getPath());
//        System.out.println("b = " + b);
        System.out.println("end = " + log);
    }
}
