package hu.czsoft.cwatesb.page;

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
@JsonFormat(with = {JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES})
@ToString
public class Metadata {
    @JsonFormat(with = {JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty(isAttribute = true)
    private String type;

    @JsonFormat(with = {JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty(isAttribute = true)
    private String lang = "en";

    /**
     * Use {@code navbar.isMember} instead
     */
    @JsonFormat(with = {JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty(isAttribute = true)@Deprecated
    private boolean isNavMenuItem = false;

    /**
     * Use {@code navbar.index} instead
     */
    @JsonFormat(with = {JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty(isAttribute = true)@Deprecated
    private short navMenuId = -1;

    @JsonFormat(with = {JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty(isAttribute = true)
    private boolean showModifiedAt;

    /**
     * Use {@code route} instead
     */
    @JsonFormat(with = {JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES})
    @JacksonXmlProperty @Deprecated
    private String id;

    /**
     * File location.<br>
     * It can be simply the file name or a virtual route, that the page will serve.
     */
    @JsonFormat(with = {JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES})
    @JacksonXmlProperty
    private String route;

    /**
     *
     */
    @JsonFormat(with = {JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES})
    @JacksonXmlProperty
    private String title;

    /**
     *
     */
    @JsonFormat(with = {JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty
    private String keywords;

    /**
     *
     */
    @JsonFormat(with = {JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty
    private String description;

    /**
     *
     */
    @JsonFormat(with = {JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty
    private String search;

    /**
     * Navigation bar related configuration
     */
    @JsonFormat(with = {JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty
    private Navbar navbar;

    /**
     *
     */
    @JsonFormat(with = {JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty
    private String author = "";

    /**
     *
     */
    @JsonFormat(with = {JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty
    @JsonProperty("image")
    private ImageData previewImage = new ImageData();

    /**
     *
     */
    @JsonFormat(with = {JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES}, shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty
    private Date releasedAt;

    /**
     *
     */
    @JsonFormat(with = {JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES}, shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JacksonXmlProperty
    private Date modifiedAt;

    @JsonIgnore@Getter
    private int length;

    /**
     * Use {@code route} instead
     */
    @JsonIgnore@Getter@Setter @Deprecated
    private String url;

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

