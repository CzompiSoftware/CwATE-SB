package hu.czsoft.web.engine;

import com.github.zafarkhaja.semver.Version;
import hu.czsoft.data.manager.singleton.InMemorySingleton;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.jar.Manifest;

import static com.github.zafarkhaja.semver.Version.parse;

@Setter
@Getter
public class EngineManager extends InMemorySingleton<Engine> {
    private final Logger LOGGER = LogManager.getLogger(EngineManager.class);
    private Engine item;

    public void load() throws IOException {
        var name = "CwATE/SB";
        var fullName = "Czompi's WebApp Templating Engine powered by Spring Boot";
        Version version, cleanVersion;
        UUID build;
        int buildNumber;
        String nodeId = "", compileTime = "", nodeName = "node";
        Manifest manifest;
        try {
            manifest = new Manifest(new ClassPathResource("META-INF/MANIFEST.MF", EngineManager.class.getClassLoader()).getInputStream());
        } catch (IOException e) {
            manifest = new Manifest();
            LOGGER.warn(e);
        }

        try {
            name = manifest.getMainAttributes().getValue("Implementation-Name");
        } catch (Exception ex) {
            name = "Unknown";
            //LOGGER.warn(ex);
        }

        try {
            name = manifest.getMainAttributes().getValue("Implementation-FullName");
        } catch (Exception ex) {
            name = "Undefined application";
            //LOGGER.warn(ex);
        }

        try {
            version = parse(manifest.getMainAttributes().getValue("Implementation-Version"));
        } catch (Exception ex) {
            version = parse("0.0.0+build.0");
            //LOGGER.warn(ex);
        }

        try {
            build = UUID.fromString(manifest.getMainAttributes().getValue("Implementation-Build"));
        } catch (Exception ex) {
            build = UUID.randomUUID();
            //LOGGER.warn(ex);
        }

        try {
            compileTime = manifest.getMainAttributes().getValue("Implementation-Timestamp");
        } catch (Exception ex) {
            //LOGGER.warn(ex);
        }
        if(compileTime == null) {
            compileTime = "0000-00-00T00:00:00";
        }

        try {
            nodeName = java.net.InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            //LOGGER.warn(e);
        }

        try {
            buildNumber = Integer.parseInt(version.buildMetadata().orElseThrow().toLowerCase().replace("build.",""));
        } catch (NoSuchElementException e) {
            buildNumber = -1;
            //LOGGER.warn(e);
        }

        cleanVersion = version.withoutBuildMetadata();

        setItem(new Engine(
                nodeId,
                nodeName,
                name,
                fullName,
                cleanVersion,
                buildNumber,
                build,
                compileTime));
    }

    @Override @SuppressWarnings("unchecked")
    public Class<Engine> getItemClass() {
        return Engine.class;
    }
}
