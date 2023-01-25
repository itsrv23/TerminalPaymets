package ru.itsrv23.terminal.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import ru.itsrv23.terminal.dto.ResponseDto;
import ru.itsrv23.terminal.model.Payment;
import ru.itsrv23.terminal.service.ProcessPayment;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class Schedule {

    private final ProcessPayment processPayment;

    @Scheduled(fixedRate = 5_000)
    public void read() throws IOException {
        processPayment.createDirIfNotExists();
        processPayment.process();
    }

    /**
     * Каждые 5 минут пытаемся провести платежи еще раз
     * Если папка не пуста, чекаем сервер, если ок, то обрабатываем
     * @throws IOException
     */
    @Scheduled(fixedRate = 300_000)
    public void tryAgain() throws IOException {
        processPayment.processOnFail();
    }
}
