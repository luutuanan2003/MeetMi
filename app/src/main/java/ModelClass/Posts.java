package ModelClass;

public class Posts {
    private String nickname;
    private String avatar; // Assuming this is a path or URL to an avatar image
    private String photo;
    private String video; // Optional, could be a path or URL
    private String caption;
    private String dateTime; // Typically, this would be a Date or LocalDateTime object
    private String location; // Optional
    private String comments; // Assuming this is aggregated or serialized comments
    private String reaction; // Default null, could be an enum or string

    public Posts(String nickname, String caption) {
        this.nickname = nickname;
        this.caption = caption;
        // Initialize other fields as needed
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }


}