package hu.czsoft.cwatesb.page.navbar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class NavbarRouteParse {
    protected static final Logger LOGGER = LogManager.getLogger(NavbarRouteParse.class);

    private static final String SEPARATOR_LTR = "->";
    private static final String SEPARATOR_RTL = "<-";
    private static final String DATA_TAG_START = "[";
    private static final String DATA_TAG_END = "]";

    /**
     *
     * @param data
     * @param child
     * @return
     * @throws UnsupportedOperationException occurs when both '->' and '<-' operators are present on the same data source.
     */
    public static NavbarNodeElement route(String data, NavbarNodeElement child) throws UnsupportedOperationException {
        if(data.contains(SEPARATOR_LTR) && data.contains(SEPARATOR_RTL)) throw new UnsupportedOperationException();
        List<String> list = new ArrayList<>();
        boolean ltr = true;
        ltr = data.contains(SEPARATOR_LTR);
        var val = recursiveSearch(data, 0, list, ltr);
        if(ltr) list = list.reversed();
        return recursiveRoute(list, 0, child);
    }

    private static NavbarNodeElement recursiveRoute(List<String> data, int startIndex, NavbarNodeElement child) {
        if(startIndex == data.size()) return child;

        var elem = NavbarNodeElementImpl.of(data.get(startIndex));

        if(child != null) {
            elem = NavbarNodeElementImpl.of(data.get(startIndex), child);
        }

        return recursiveRoute(data, startIndex + 1, elem);
    }

    private static boolean recursiveSearch(String data, int startPos, List<String> list, boolean ltr) {
        //if (list == null || list.isEmpty()) list = List.of();
        if(startPos == -1) startPos = 0;

        if(startPos == data.length()) return false;

        if(data.equals(DATA_TAG_START + DATA_TAG_END)) { // []
            return false;
        }

        if (data.substring(startPos).contains(SEPARATOR_LTR)  && data.startsWith(DATA_TAG_START, startPos)) {
            System.out.println(data.substring(startPos));

            var separatorPos = data.indexOf(SEPARATOR_LTR, startPos) + SEPARATOR_LTR.length();
            var dataTag = data.substring(startPos, separatorPos - SEPARATOR_LTR.length());
            //System.out.println(data.substring(startPos, separatorPos));

            if(dataTag.startsWith(DATA_TAG_START)) dataTag = dataTag.substring(1);
            if(dataTag.endsWith(DATA_TAG_END)) dataTag = dataTag.substring(0, dataTag.length() - 1);

            boolean add = list.add(dataTag);

            return recursiveSearch(data, separatorPos, list, true);
        } else if (data.substring(startPos).contains(SEPARATOR_RTL)  && data.startsWith(DATA_TAG_START, startPos)) {
            System.out.println(data.substring(startPos));

            var separatorPos = data.indexOf(SEPARATOR_RTL, startPos) + SEPARATOR_RTL.length();
            var dataTag = data.substring(startPos, separatorPos - SEPARATOR_RTL.length());
            //System.out.println(data.substring(startPos, separatorPos));

            if(dataTag.startsWith(DATA_TAG_START)) dataTag = dataTag.substring(1);
            if(dataTag.endsWith(DATA_TAG_END)) dataTag = dataTag.substring(0, dataTag.length() - 1);

            boolean add = list.add(dataTag);

            return recursiveSearch(data, separatorPos, list, false);
        }
        return false;
    }

}
