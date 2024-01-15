package ModelClass;

import java.util.List;

public class Posts {
    private String nickname;
    private String avatar; // Assuming this is a path or URL to an avatar image
    private List <String> photo;
    private String video; // Optional, could be a path or URL
    private String caption;
    private String dateTime; // Typically, this would be a Date or LocalDateTime object
    private List<String> comments; // Assuming this is aggregated or serialized comments
    private int reaction; // Default null, could be an enum or string

    public Posts(String nickname, String avatar, List<String> photo, String video, String caption, String dateTime, List<String> comments, int reaction) {
        this.nickname = nickname;
        this.avatar = avatar;
        this.photo = photo;
        this.video = video;
        this.caption = caption;
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


    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public int getReaction() {
        return reaction;
    }

    public void setReaction(int reaction) {
        this.reaction = reaction;
    }
}