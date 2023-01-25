package ru.itsrv23.terminal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JacksonXmlRootElement(namespace = "response")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseDto {
    @JacksonXmlProperty(localName = "account")
    private Integer account;

    @JacksonXmlProperty(localName = "osmp_txn_id")
    private String session;

    @JacksonXmlProperty(localName = "result")
    private Integer result;

    @JacksonXmlProperty(localName = "comment")
    private String comment;
}
