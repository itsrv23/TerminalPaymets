package ru.itsrv23.terminal.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import ru.itsrv23.terminal.service.ProcessPayment;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class Schedule {

    private final ProcessPayment processPayment;

    @PostConstruct
    private void postConstruct() {
        processPayment.createDirIfNotExists();
    }


    @Scheduled(fixedRate = 5_000)
    public void read() throws IOException {
        processPayment.process();
    }

    /**
     * Каждую минуту пытаемся провести платежи еще раз
     * Если папка не пуста, чекаем сервер, если ок, то обрабатываем
     * @throws IOException
     */
    @Scheduled(fixedRate = 60_000)
    public void tryAgain() throws IOException {
        processPayment.processOnFail();
    }
}
