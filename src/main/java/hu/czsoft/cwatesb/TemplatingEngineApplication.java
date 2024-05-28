package hu.czsoft.cwatesb;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.base.MoreObjects;
import hu.czsoft.cwatesb.model.*;
import hu.czsoft.cwatesb.persistent.ApplicationPersistentData;
import hu.czsoft.cwatesb.persistent.SiteConfig;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.logging.LoggingSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class TemplatingEngineApplication {
	// it's important to initialize the OpenTelemetry SDK as early in your applications lifecycle as
	// possible.
	private static final OpenTelemetry openTelemetry = initOpenTelemetry();

	private static OpenTelemetry initOpenTelemetry() {
		SdkTracerProvider sdkTracerProvider =
				SdkTracerProvider.builder()
						.addSpanProcessor(SimpleSpanProcessor.create(new LoggingSpanExporter()))
						.build();

		OpenTelemetrySdk sdk =
				OpenTelemetrySdk.builder()
						.setTracerProvider(sdkTracerProvider)
						.setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
						.build();

		Runtime.getRuntime().addShutdownHook(new Thread(sdkTracerProvider::close));
		return sdk;
	}

	private static final Tracer tracer = openTelemetry.getTracer("hu.czsoft.cwatesb.TemplatingEngineApplication");
	private static final Logger LOGGER = LogManager.getLogger(TemplatingEngineApplication.class);

	public static final List<Page> PAGE_LIST = new ArrayList<>();
	public static final String WORKING_DIRECTORY = "../data/";
	public static final String CONTENT_DIRECTORY = WORKING_DIRECTORY + "content/";
	public static final String CONTENT_LANG_DIRECTORY = WORKING_DIRECTORY + "content-{0}/";
	public static SiteConfig SITE_CONFIG;
	public static Engine ENGINE;
	public static Site SITE;
	public static XmlMapper XML_MAPPER;

	public TemplatingEngineApplication() {
	}

	public static void main(String[] args) {
		SpringApplication.run(TemplatingEngineApplication.class, args);
		preConfig();

		LOGGER.info("Name: {}\r\nNode: {}\r\nVersion: {}\r\nBuild: {}\r\nCompile time: {}", ENGINE.getFullName(), ENGINE.getNodeId(), ENGINE.getVersion(), ENGINE.getBuild(), ENGINE.getCompileTime());

	}

	private static void preConfig() {
		XML_MAPPER = new XmlMapper();
		XML_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		XML_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		XML_MAPPER.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
		XML_MAPPER.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
		XML_MAPPER.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		XML_MAPPER.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true);
		XML_MAPPER.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES, true);
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
		PAGE_LIST.addAll(XmdHandler.getInstance().enumeratePages());
		SITE = new Site(
				SITE_CONFIG.getId(),
				SITE_CONFIG.getShortName(),
				SITE_CONFIG.getName(),
				SITE_CONFIG.getDefaultLang(),
				SITE_CONFIG.getThemeColor(),
				SITE_CONFIG.getCdnUrl(),
				SITE_CONFIG.getCopyrightHolder(),
				SITE_CONFIG.getProducts(),
				//PAGE_LIST, //TODO: Remove this before stable release
				styles
		);

        String nodeId = "node";
        try {
            nodeId = java.net.InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
			LOGGER.error(e);
        }
        ENGINE = new Engine(
				nodeId,
				ApplicationPersistentData.getName(),
				ApplicationPersistentData.getFullName(),
				ApplicationPersistentData.getVersion().withoutBuildMetadata(),
				Integer.parseInt(ApplicationPersistentData.getVersion().buildMetadata().get().toLowerCase().replace("build.","")),
				ApplicationPersistentData.getBuild(),
				ApplicationPersistentData.getCompileTime());
	}

}
