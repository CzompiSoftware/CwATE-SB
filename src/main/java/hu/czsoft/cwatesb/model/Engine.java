package hu.czsoft.cwatesb.model;

import com.github.zafarkhaja.semver.Version;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@ToString
public class Engine {
    @Getter
    private final String nodeId;

    @Getter
    private final String name;

    @Getter
    private final String fullName;

    @Getter
    private final Version version;

    @Getter
    private final int versionBuild;

    @Getter
    private final UUID build;

    @Getter
    private final String compileTime;

    public Engine(String nodeId, String name, String fullName, Version version, int versionBuild, UUID build, String compileTime) {
        this.nodeId = nodeId;
        this.name = name;
        this.fullName = fullName;
        this.version = version;
        this.build = build;
        this.versionBuild = versionBuild;
        this.compileTime = compileTime;
    }
}
