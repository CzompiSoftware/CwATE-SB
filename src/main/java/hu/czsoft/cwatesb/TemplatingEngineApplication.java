package hu.czsoft.cwatesb;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.base.MoreObjects;
import hu.czsoft.cwatesb.model.CdnAPI;
import hu.czsoft.cwatesb.model.Engine;
import hu.czsoft.cwatesb.model.Metadata;
import hu.czsoft.cwatesb.model.Site;
import hu.czsoft.cwatesb.persistent.ApplicationPersistentData;
import hu.czsoft.cwatesb.persistent.SiteConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class TemplatingEngineApplication {

	public static final List<Metadata> PAGE_LIST = new ArrayList<>();
	public static final String WORKING_DIRECTORY = "../data/";
	public static final String CONTENT_DIRECTORY = WORKING_DIRECTORY + "content/";
	public static final String CONTENT_LANG_DIRECTORY = WORKING_DIRECTORY + "content-{0}/";
	public static SiteConfig SITE_CONFIG;
	public static Engine ENGINE;
	public static Site SITE;
	public static XmlMapper XML_MAPPER;

	public static void main(String[] args) {
		XML_MAPPER = new XmlMapper();
		XML_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		XML_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		XML_MAPPER.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
		XML_MAPPER.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
		XML_MAPPER.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
		SITE_CONFIG = SiteConfig.getInstance();
		List<String> styles = new ArrayList<>();
		for (var style : SITE_CONFIG.getThemeStore()) {
			if (style.getUrl() != null && !style.getUrl().isBlank()){
				if (!style.getUrl().startsWith("//") && style.getUrl().startsWith("http://") && style.getUrl().startsWith("https://"))
				{
					styles.add(CdnAPI.renderUrl(style.getUrl()));
				}
				else
				{
					styles.add(style.getUrl());
				}
			}
			else
			{
				styles.add(CdnAPI.renderUrl("css/%s@v%s/%s".formatted(style.getName(), style.getVersion(), MoreObjects.firstNonNull(style.getFileName(), "master.css"))));
			}
		}
		PAGE_LIST.clear();
		PAGE_LIST.addAll(XmdHandler.getInstance().enumeratePageMetadata());
		SITE = new Site(SITE_CONFIG.getId(), SITE_CONFIG.getShortName(), SITE_CONFIG.getName(), SITE_CONFIG.getDefaultLang(), SITE_CONFIG.getThemeColor(), SITE_CONFIG.getCdnUrl(), SITE_CONFIG.getProducts(), PAGE_LIST, styles);

		var nodeId = "";
		var version = ApplicationPersistentData.getVersion().withoutBuildMetadata();
		var ver = ApplicationPersistentData.getVersion().buildMetadata().get();
		ver = ver.toLowerCase().replace("build.","");
		var versionBuild = Integer.parseInt(ver);
		ENGINE = new Engine(nodeId, ApplicationPersistentData.getName(), ApplicationPersistentData.getFullName(), version, versionBuild, ApplicationPersistentData.getBuild(), ApplicationPersistentData.getCompileTime());
		SpringApplication.run(TemplatingEngineApplication.class, args);
	}

}
