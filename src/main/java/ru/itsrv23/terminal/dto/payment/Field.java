package ru.itsrv23.terminal.dto.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class Field {
    @JacksonXmlProperty(localName = "field100")
    private Integer auto;
    @JacksonXmlProperty(localName = "AMOUNT")
    private BigDecimal amount;
}
