package hu.czsoft.cwatesb.model;

import com.github.zafarkhaja.semver.Version;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public class Engine {
    private final String nodeId;

    private final String name;

    private final String fullName;

    private final Version version;

    private final int versionBuild;

    private final UUID build;

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
