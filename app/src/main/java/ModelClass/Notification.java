package ModelClass;

public class Notification {
    private String dateTime, fromUser, fromUserAvatar, isComment, isReaction;

    public Notification(String dateTime, String fromUser, String fromUserAvatar, String isComment, String isReaction) {
        this.dateTime = dateTime;
        this.fromUser = fromUser;
        this.fromUserAvatar = fromUserAvatar;
        this.isComment = isComment;
        this.isReaction = isReaction;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getFromUserAvatar() {
        return fromUserAvatar;
    }

    public void setFromUserAvatar(String fromUserAvatar) {
        this.fromUserAvatar = fromUserAvatar;
    }

    public String getIsComment() {
        return isComment;
    }

    public void setIsComment(String isComment) {
        this.isComment = isComment;
    }

    public String getIsReaction() {
        return isReaction;
    }

    public void setIsReaction(String isReaction) {
        this.isReaction = isReaction;
    }
}
