package fr.cv.projetiut.smart_raid.ui.list.message.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.cv.projetiut.smart_raid.data.Course;

public class MessageObjectContent {

    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    public static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.content, item);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    public static class DummyItem {
        public final String content;
        public final String time;

        public DummyItem(String content, String time) {
            this.content = content;
            this.time = time;
        }

        @Override
        public String toString() {
            return content + " " + time;
        }
    }
}
