package ru.itsrv23.terminal.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.itsrv23.terminal.dto.PaymentDto;
import ru.itsrv23.terminal.model.Payment;

import java.io.File;
import java.nio.file.Path;

@Slf4j
@Service
public class ProcessPayment {
    @Value("${dir.file.source}")
    private String sourcePath;

    @SneakyThrows
    public Payment parseFile() {
        return parseFile(sourcePath);
    }

    @SneakyThrows
    public Payment parseFile(String path) {
        XmlMapper mapper = new XmlMapper();
        File dir = new File(path);
        File[] files = dir.listFiles();
        log.info("Count files :: {}", files.length);
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".pkt")) {
                PaymentDto paymentDto = mapper.readValue(file, PaymentDto.class);
                Payment payment = Payment.toPayment(paymentDto);
                payment.setPath(Path.of(file.getPath()));
                log.info("Payment created :: {}", payment);
                return payment;
            }
        }
        return null;
    }

    public void SendPayment(Payment payment) {

    }

    public void delete(Path path) {
        //Удаление в случае успеха
    }

    private void writeOnSuccess() {
        //Директория с csv файлами, по датам
    }

    private void replaceOnFail() {
        //Директория с фейлами, котоые не смогли обоработать, будем пробовать снова, с большим таймаутом? или продумать логику
    }

}
