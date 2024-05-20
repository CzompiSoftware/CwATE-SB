package hu.czsoft.cwatesb.persistent;

import com.github.zafarkhaja.semver.Version;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ApplicationPersistentData {

    @Getter
    private static String name = "CwATE/SB";

    @Getter
    private static String fullName = "Czompi's WebApp Templating Engine powered by Spring Boot";

    @Getter
    private static Version version = Version.parse("1.0.0+build.1");

    @Getter
    private static UUID build = UUID.randomUUID();

    @Getter
    private static String compileTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

}
