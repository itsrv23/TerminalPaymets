package ru.itsrv23.terminal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.itsrv23.terminal.dto.PaymentDto;
import ru.itsrv23.terminal.exception.PaymentMappingException;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Builder
@AllArgsConstructor
@Data
public class Payment {
    private Integer account;
    private BigDecimal amount;
    private LocalDateTime paymentCr;
    private LocalDateTime processCr;
    private String session;
    private Path path;

    public static Payment toPayment(PaymentDto paymentDto) {
        if (Objects.isNull(paymentDto)) {
            throw new PaymentMappingException();
        }
        return Payment.builder()
                .account(paymentDto.getField().getAccount())
                .amount(paymentDto.getField().getAmount())
                .session(paymentDto.getSession())
                .paymentCr(LocalDateTime.parse(paymentDto.getPaymentCreateDt(), DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")))
                .processCr(LocalDateTime.now())
                .build();
    }

    public String toCsv() {
        String d = ";";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(paymentCr).append(d)
                .append(account).append(d)
                .append(amount).append(d)
                .append(session).append(d)
                .append("\n");
        return stringBuilder.toString();
    }
}
