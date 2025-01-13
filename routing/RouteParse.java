package hu.czsoft.data.search;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class RouteParse {
    protected static final Logger LOGGER = LogManager.getLogger(RouteParse.class);

    private static final String SEPARATOR = "->";
    private static final String DATA_TAG_START = "[";
    private static final String DATA_TAG_END = "]";

    private static boolean recursiveSearch(String data, int startPos, List<String> list) {
        //if (list == null || list.isEmpty()) list = List.of();
        if(startPos == -1) startPos = 0;

        if(data.equals(DATA_TAG_START + DATA_TAG_END)) { // []
            return false;
        }

        if (data.substring(startPos).contains("->")  && data.startsWith(DATA_TAG_START, startPos)) {
            System.out.println(data.substring(startPos));

            var separatorPos = data.indexOf(SEPARATOR, startPos) + SEPARATOR.length();
            var dataTag = data.substring(startPos, separatorPos - SEPARATOR.length());
            //System.out.println(data.substring(startPos, separatorPos));

            if(dataTag.startsWith(DATA_TAG_START)) dataTag = dataTag.substring(1);
            if(dataTag.endsWith(DATA_TAG_END)) dataTag = dataTag.substring(0, dataTag.length() - 1);

            boolean add = list.add(dataTag);

            return recursiveSearch(data, separatorPos, list);
        }
        return false;
    }

    public static RouteElement route(String data, RouteElement child) {
        List<String> list = new ArrayList<>();
        var val = recursiveSearch(data, 0, list);
        list = list.reversed();
        return recursiveRoute(list, 0, child);
    }

    private static RouteElement recursiveRoute(List<String> data, int startIndex, RouteElement child) {
        if(startIndex == data.size()) return child;

        var elem = RouteElementImpl.of(data.get(startIndex));

        if(child != null) {
            elem = RouteElementImpl.of(data.get(startIndex), child);
        }

        return recursiveRoute(data, startIndex + 1, elem);
    }
}
