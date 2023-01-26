package ru.itsrv23.terminal.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.itsrv23.terminal.dto.PaymentDto;
import ru.itsrv23.terminal.dto.ResponseDto;
import ru.itsrv23.terminal.model.Payment;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.Objects;

@Slf4j
@Service
public class ProcessPayment {
    @Value("${dir.file.source}")
    private String sourcePath;

    @Value("${dir.file.fail}")
    private String sourceFail;

    @Value("${dir.file.csv}")
    private String sourceCsv;

    @Value("${lime.server.host}")
    private String host;

    @Value("${lime.server.port}")
    private String port;

    @Autowired
    private RestTemplate restTemplate;

    public void process() {
        Payment payment = parseFile();
        if (Objects.isNull(payment)) {
            return;
        }
        trySendPayment(payment);
    }

    private void trySendPayment(Payment payment) {
        ResponseDto responseDto = sendPayment(payment);
        if (Objects.nonNull(responseDto)) {
            //TODO если сервер отбил с не верным позывным, подумать о тиките для Администоратора
            if (responseDto.getResult().equals(0)) {
                log.info("Success :: {}", payment.toCsv());
            } else {
                log.info("Something wrong :: {}, {}", payment.toCsv(), responseDto);
            }
            writeOnSuccess(payment);
            delete(payment.getPath());
        } else {
            log.info("Something wrong, ResponseDto is null :: {}", payment.toCsv());
            replaceOnFail(payment.getPath());
        }
    }

    @SneakyThrows
    public void writeOnSuccess(Payment payment) {
        //Директория с csv файлами, по датам
        String fileCsv = sourceCsv + "\\" + LocalDate.now() + ".csv";
        Files.write(
                Paths.get(fileCsv)
                , payment.toCsv().getBytes()
                , StandardOpenOption.CREATE
                , StandardOpenOption.APPEND
        );
    }

    @SneakyThrows
    public void delete(Path path) {
        Files.deleteIfExists(path);
    }

    @SneakyThrows
    public void replaceOnFail(Path path) {
        Files.copy(path, Paths.get(sourceFail + "\\" + path.getFileName()));
        delete(path);
    }


    public void processOnFail() {
        Payment payment = parseFile(sourceFail);
        if (Objects.isNull(payment)) {
            return;
        }

        if (!testConnectIsOk()) {
            return;
        }

        ResponseDto responseDto = sendPayment(payment);
        if (Objects.nonNull(responseDto)) {
            writeOnSuccess(payment);
            delete(payment.getPath());
            log.info("Success :: {}", payment.toCsv());
        }
    }


    @SneakyThrows
    private boolean testConnectIsOk() {
        URI uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(host)
                .port(port)
                .build()
                .toUri();
        ResponseEntity<String> entity=null;
        try {
            entity = restTemplate.getForEntity(uri, String.class);
        } catch (ResourceAccessException e) {
            log.info("Connection error {}", uri);
        }
        if (Objects.nonNull(entity)){
            return entity.getStatusCode().is2xxSuccessful();
        }
        return false;
    }

    @SneakyThrows
    public Payment parseFile() {
        return parseFile(sourcePath);
    }

    @SneakyThrows
    public Payment parseFile(String path) {
        XmlMapper mapper = new XmlMapper();
        File dir = new File(path);
        File[] files = dir.listFiles();
        log.debug("Count files :: {}", files.length);
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".pkt")) {
                PaymentDto paymentDto = mapper.readValue(file, PaymentDto.class);
                Payment payment = Payment.toPayment(paymentDto);
                payment.setPath(file.toPath());
                log.info("Payment created :: {}", payment);
                return payment;
            }
        }
        return null;
    }

    @SneakyThrows
    private ResponseDto sendPayment(@NonNull Payment payment) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("pay_id", payment.getSession());
        queryParams.add("account", payment.getAccount().toString());
        queryParams.add("pay_amount", payment.getAmount().toString());

        URI uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(host)
                .port(port)
                .queryParams(queryParams)
                .build()
                .toUri();

        log.debug(uri.toString());

        XmlMapper mapper = new XmlMapper();
        ResponseDto responseDto = null;
        try {
            ResponseEntity<String> entity = restTemplate.getForEntity(uri, String.class);
            responseDto = mapper.readValue(entity.getBody(), ResponseDto.class);
            log.debug("responseDto :: {}", responseDto);
        } catch (ResourceAccessException e) {
            log.info("Connection error {}", uri);
        }
        return responseDto;
    }

    public void createDirIfNotExists() {
        try {
            log.info("sourcePath = {}", sourcePath);
            log.info("sourceFail = {}", sourceFail);
            log.info("sourceCsv = {}", sourceCsv);

            if (Files.notExists(Paths.get(sourcePath))){
                Files.createDirectory(Paths.get(sourcePath));
            }
            if (Files.notExists(Paths.get(sourceFail))){
                Files.createDirectory(Paths.get(sourceFail));
            }
            if (Files.notExists(Paths.get(sourceCsv))){
                Files.createDirectory(Paths.get(sourceCsv));
            }
        } catch (IOException e) {
            //
        }
    }

}
