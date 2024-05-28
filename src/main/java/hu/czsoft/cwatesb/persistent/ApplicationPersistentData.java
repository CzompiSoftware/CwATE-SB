package hu.czsoft.cwatesb.persistent;

import com.github.zafarkhaja.semver.Version;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.jar.Manifest;

import static com.github.zafarkhaja.semver.Version.parse;

public class ApplicationPersistentData {
    private static final Logger LOGGER = LogManager.getLogger(ApplicationPersistentData.class);

    private static final Manifest manifest;

    static {
        Manifest man;
        try {
            man = new Manifest(new ClassPathResource("META-INF/MANIFEST.MF", ApplicationPersistentData.class.getClassLoader()).getInputStream());
        } catch (IOException e) {
            man = new Manifest();
            LOGGER.error(e);
        }
        manifest = man;
    }

    @Getter
    private static String name = "CwATE/SB";

    @Getter
    private static String fullName = "Czompi's WebApp Templating Engine powered by Spring Boot";

    @Getter
    private static Version version;
    static {
        try {
            version = parse(manifest.getMainAttributes().getValue("Implementation-Version"));
        } catch (Exception ex) {
            version = Version.parse("0.0.0+build.0");
            LOGGER.error(ex);
        }
    }

    @Getter
    private static UUID build;

    static {
        try {
            build = UUID.fromString(manifest.getMainAttributes().getValue("Implementation-Build"));
        } catch (Exception ex) {
            build = UUID.randomUUID();
            LOGGER.error(ex);
        }
    }

    @Getter
    private static String compileTime;

    static {
        try {
            compileTime = manifest.getMainAttributes().getValue("Implementation-Timestamp");
        } catch (Exception ex) {
            compileTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            LOGGER.error(ex);
        }
    }

    ;

}
