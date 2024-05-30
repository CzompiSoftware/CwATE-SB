package hu.czsoft.cwatesb.page.navbar;

public class NavbarRouteParse {

    private static final String SEPARATOR = "->";
    private static final String DATA_TAG_START = "[";
    private static final String DATA_TAG_END = "]";

    private static NavbarNodeElement routeBuilder(String data, int startPos, NavbarNodeElement parent) {

        //if (list == null || list.isEmpty()) List<NavbarNodeElement> list = List.of();
        if(startPos == -1) startPos = 0;

        if(data.equals(DATA_TAG_START+DATA_TAG_END)) { // []
            return parent;
        }

        if (data.substring(startPos).contains("->")  && data.startsWith(DATA_TAG_START, startPos)) {
            System.out.println(data.substring(startPos));

            var separatorPos = data.indexOf(SEPARATOR, startPos) + SEPARATOR.length();
            var dataTag = data.substring(startPos, separatorPos - SEPARATOR.length());
            //System.out.println(data.substring(startPos, separatorPos));

            if(dataTag.startsWith(DATA_TAG_START)) dataTag = dataTag.substring(1);
            if(dataTag.endsWith(DATA_TAG_END)) dataTag = dataTag.substring(0, dataTag.length() - 1);

            var elem = NavbarNodeElementImpl.of(dataTag, parent);

            if(parent == null) {
                return elem;
            } else {
                parent.setChild(elem);
                return routeBuilder(data, separatorPos, parent);
            }

        }
        return parent;
    }
}
