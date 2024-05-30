package org.commonmark.ext.czsoft.xmd.lua.lre;

import net.sandius.rembulan.StateContext;
import net.sandius.rembulan.Table;
import net.sandius.rembulan.Variable;
import net.sandius.rembulan.compiler.CompilerChunkLoader;
import net.sandius.rembulan.env.RuntimeEnvironments;
import net.sandius.rembulan.exec.CallException;
import net.sandius.rembulan.exec.CallPausedException;
import net.sandius.rembulan.exec.DirectCallExecutor;
import net.sandius.rembulan.impl.StateContexts;
import net.sandius.rembulan.lib.StandardLibrary;
import net.sandius.rembulan.load.LoaderException;
import net.sandius.rembulan.runtime.LuaFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.commonmark.ext.czsoft.xmd.lua.LuaType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class LuaRuntime {
    private static Logger LOGGER = LogManager.getLogger(LuaRuntime.class);

    private final CompilerChunkLoader loader;
    private final Table env;
    private final StateContext state;

    private LuaRuntime() {

        state = StateContexts.newDefaultInstance();

        env = StandardLibrary.in(RuntimeEnvironments.system()).installInto(state);
        loader = CompilerChunkLoader.of("lua_runtime");


    }

    public String execute(String sourceCode, LuaType type) {
        LuaFunction main = null;
        List<Object> result = new ArrayList<>();
        try {
            main = loader.loadTextChunk(new Variable(env), "lre_" + UUID.randomUUID().toString().split("-")[0], sourceCode);
            result.addAll(List.of(DirectCallExecutor.newExecutor().call(state, main)));
            LOGGER.debug(main);

        } catch (LoaderException | CallException | CallPausedException | InterruptedException e) {
            if(type == LuaType.BLOCK) {
                var exceptionName = e.getClass().getCanonicalName();
                String[] splitExName = exceptionName.split("\\.");
                exceptionName = Arrays.stream(splitExName).skip(splitExName.length-1).toArray()[0].toString();
                return  "]>danger< ## " + exceptionName + "\r\n" +
                        "] " + e.getCause().getLocalizedMessage().replace("\n","\n] ");
            } else if (type == LuaType.INLINE) {
                return "???";
            }
            LOGGER.error(e);
        }
        return result.get(0).toString();
    }

    public static LuaRuntime create() {
        return new LuaRuntime();
    }
}
