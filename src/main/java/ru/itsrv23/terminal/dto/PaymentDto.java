package ru.itsrv23.terminal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itsrv23.terminal.dto.payment.Field;

@Data
@NoArgsConstructor
@JacksonXmlRootElement(namespace = "root")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentDto {

    @JacksonXmlProperty(localName = "fields")
    private Field field;
    @JacksonXmlProperty(localName = "initial_session_num")
    private String session;
    @JacksonXmlProperty(localName = "payment_create_dt")
    private String paymentCreateDt;
}
