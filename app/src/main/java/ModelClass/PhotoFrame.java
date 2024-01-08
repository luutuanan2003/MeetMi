package ModelClass;
import java.util.ArrayList;
import java.util.List;


    public class PhotoFrame {
        private String id;
        private String styleName;
        private String resource;

        public PhotoFrame(String id,String styleName, String resource) {
            this.id = id;
            this.styleName = styleName;
            this.resource = resource;
        }

        public String getStyleName() {
            return styleName;
        }

        public void setStyleName(String styleName) {
            this.styleName = styleName;
        }

        public String getResource() {
            return resource;
        }

        public void setResource(String resource) {
            this.resource = resource;
        }

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }


    }


