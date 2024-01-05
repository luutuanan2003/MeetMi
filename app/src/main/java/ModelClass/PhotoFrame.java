package ModelClass;
import java.util.ArrayList;
import java.util.List;


    public class PhotoFrame {
        private String id;
        private List<String> photoFrameId;

        public PhotoFrame(String id) {
            this.id = id;
            this.photoFrameId = new ArrayList<>();
        }

        // Method stub for database interaction
        public List<String> getDPhotoFrame() {
            // Database logic to retrieve photo frames would go here
            return new ArrayList<>(); // Placeholder return
        }

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<String> getPhotoFrameId() {
            return photoFrameId;
        }

        public void setPhotoFrameId(List<String> photoFrameId) {
            this.photoFrameId = photoFrameId;
        }
    }


