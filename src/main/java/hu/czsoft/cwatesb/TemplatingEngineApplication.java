package hu.czsoft.cwatesb;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import hu.czsoft.web.engine.EngineManager;
import hu.czsoft.cwatesb.page.PageCollectionManager;
import hu.czsoft.cwatesb.site.SiteManager;
import hu.czsoft.xmdl.XmdlDocument;
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
import org.springframework.core.io.ClassPathResource;

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


	public static final XmlMapper XML_MAPPER = configureXmlMapper();
	public static final XmdlDocument XMDL_DOCUMENT = new XmdlDocument(XML_MAPPER);
	public static final EngineManager ENGINE_MANAGER = new EngineManager();
	public static final SiteManager SITE_MANAGER = new SiteManager(ENGINE_MANAGER);
	public static final PageCollectionManager PAGE_MANAGER = new PageCollectionManager(SITE_MANAGER, XMDL_DOCUMENT);

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
		// Load engine manager
		try {
			ENGINE_MANAGER.load(new ClassPathResource("META-INF/MANIFEST.MF", TemplatingEngineApplication.class.getClassLoader()).getInputStream());
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
		try {
			PAGE_MANAGER.load(CONTENT_DIRECTORY);
		} catch (IOException e) {
			LOGGER.warn(e);
		}
	}

	private static XmlMapper configureXmlMapper() {
		return XmlMapper.builder()
				.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
				.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
				.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
				.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)
				.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES, true)
				.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false).build();
	}

}
