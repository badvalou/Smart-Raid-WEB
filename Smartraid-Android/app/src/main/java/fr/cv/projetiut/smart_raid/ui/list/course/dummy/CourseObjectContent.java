package fr.cv.projetiut.smart_raid.ui.list.course.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.cv.projetiut.smart_raid.data.Course;

public class CourseObjectContent {

    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    public static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
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
        private Course courseId;
        public final String id;
        public final String content;
        public final String details;

        public DummyItem(Course courseId, String id, String content, String details) {
            this.courseId = courseId;
            this.id = id;
            this.content = content;
            this.details = details;
        }

        public Course getCourseId() {
            return courseId;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
