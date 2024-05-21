package hu.czsoft.cwatesb.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@JacksonXmlRootElement
@ToString
public class Metadata {
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    @JsonInclude(JsonInclude.Include.NON_NULL)@JacksonXmlProperty(isAttribute = true)
    private String type;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    @JsonInclude(JsonInclude.Include.NON_NULL)@JacksonXmlProperty(isAttribute = true)
    private String lang;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    @JsonInclude(JsonInclude.Include.NON_NULL)@JacksonXmlProperty(isAttribute = true)
    private boolean isNavMenuItem;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    @JsonInclude(JsonInclude.Include.NON_NULL)@JacksonXmlProperty(isAttribute = true)
    private short navMenuId;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    @JsonInclude(JsonInclude.Include.NON_NULL)@JacksonXmlProperty(isAttribute = true)
    private boolean showModifiedAt;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    @JacksonXmlProperty
    private String id;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    @JacksonXmlProperty
    private String title;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    @JsonInclude(JsonInclude.Include.NON_NULL)@JacksonXmlProperty
    private String keywords;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    @JsonInclude(JsonInclude.Include.NON_NULL)@JacksonXmlProperty
    private String description;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    @JsonInclude(JsonInclude.Include.NON_NULL)@JacksonXmlProperty
    private String search;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    @JsonInclude(JsonInclude.Include.NON_NULL)@JacksonXmlProperty
    private String author = "";

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    @JsonInclude(JsonInclude.Include.NON_NULL)@JacksonXmlProperty@JsonProperty("image")
    private ImageData previewImage = new ImageData();

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonInclude(JsonInclude.Include.NON_NULL)@JacksonXmlProperty
    private Date releasedAt;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonInclude(JsonInclude.Include.NON_NULL)@JacksonXmlProperty
    private Date modifiedAt;

    @JsonIgnore@Getter
    private int length;

    public Metadata() {
        this(-1);
    }
    public Metadata(int length) {
        this.length = length;
    }

    public static Metadata parse(Metadata data, int length){
        data.length = length;
        return data;
    }
}

