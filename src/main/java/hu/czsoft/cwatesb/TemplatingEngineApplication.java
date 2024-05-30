package hu.czsoft.cwatesb;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import hu.czsoft.cwatesb.engine.EngineManager;
import hu.czsoft.cwatesb.page.PageCollectionManager;
import hu.czsoft.cwatesb.site.SiteManager;
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

import java.io.IOException;

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

	public static final String WORKING_DIRECTORY = "../data/";
	public static final String CONTENT_DIRECTORY = WORKING_DIRECTORY + "content/";
	public static final String CONTENT_LANG_DIRECTORY = WORKING_DIRECTORY + "content-{}/";
	public static final String TRANSLATION_DIRECTORY = WORKING_DIRECTORY + "translation/";


	public static XmlMapper XML_MAPPER;

	public static final PageCollectionManager PAGE_MANAGER = new PageCollectionManager();
	public static final EngineManager ENGINE_MANAGER = new EngineManager();
	public static final SiteManager SITE_MANAGER = new SiteManager(ENGINE_MANAGER);

	public TemplatingEngineApplication() {
	}

	public static void main(String[] args) {
		preConfig();
		SpringApplication.run(TemplatingEngineApplication.class, args);

		LOGGER.info("---------------------- CwATE/SB ----------------------");
		LOGGER.info("  Node: {}", ENGINE_MANAGER.get().getNodeName());
		LOGGER.info("  Application id: {}", ENGINE_MANAGER.get().getNodeId());
		LOGGER.info("  Build: {}", ENGINE_MANAGER.get().getBuild());
		LOGGER.info("  Version: {}", ENGINE_MANAGER.get().getVersion());
		LOGGER.info("  Telemetry provider: {}", "NotImplemented");
		LOGGER.info("  Environment: {}", "NotImplemented");
		LOGGER.info("  Compile time: {}", ENGINE_MANAGER.get().getCompileTime());
		LOGGER.info("-------------------------------------------------------");
	}

	private static void preConfig() {
		// Configure Xml mapper
		configureXmlMapper();

		// Load engine manager
		try {
			ENGINE_MANAGER.load();
		} catch (IOException e) {
			LOGGER.warn(e);
		}

		// Load site manager
		try {
			SITE_MANAGER.load();
		} catch (IOException e) {
			LOGGER.warn(e);
		}

		// Load page manager
		PAGE_MANAGER.clear();
		PAGE_MANAGER.addRange(XmdlParser.getInstance().enumeratePages(CONTENT_DIRECTORY));

	}

	private static void configureXmlMapper() {
		XML_MAPPER = new XmlMapper();
		XML_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		XML_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		XML_MAPPER.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
		XML_MAPPER.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
		XML_MAPPER.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		XML_MAPPER.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true);
		XML_MAPPER.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES, true);
		XML_MAPPER.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
	}

}
