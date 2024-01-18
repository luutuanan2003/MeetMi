package ModelClass;

import java.util.List;
import java.util.Map;

public class Posts {
    private String nickname ;
    private String keyID ;
    private String avatar; // Assuming this is a path or URL to an avatar image
    private List <String> photo;
    private String video; // Optional, could be a path or URL
    private String caption;
    private String dateTime; // Typically, this would be a Date or LocalDateTime object
    private Map<String, String> comments; // Assuming this is aggregated or serialized comments
    private int reaction; // Default null, could be an enum or string

    public Posts() {
    }

    public String getKeyID() {
        return keyID;
    }

    public void setKeyID(String keyID) {
        this.keyID = keyID;
    }

    public Posts(String nickname, String keyID, String avatar, List<String> photo, String video, String caption, String dateTime, Map<String, String> comments, int reaction) {
        this.nickname = nickname;
        this.keyID = keyID;
        this.avatar = avatar;
        this.photo = photo;
        this.video = video;
        this.caption = caption.toString();
        this.dateTime = dateTime;
        this.comments = comments;
        this.reaction = reaction;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<String> getPhoto() {
        return photo;
    }


    public void setPhoto(List<String> photo) {
        this.photo = photo;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }


    public Map<String, String> getComments() {
        return comments;
    }

    public void setComments(Map<String, String> comments) {
        this.comments = comments;
    }

    public int getReaction() {
        return reaction;
    }

    public void setReaction(int reaction) {
        this.reaction = reaction;
    }
}