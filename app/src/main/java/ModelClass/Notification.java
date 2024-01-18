package ModelClass;

public class Notification {
    private String dateTime, fromUser, fromUserAvatar,fromEmail, isComment, isReaction,commentData;

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getCommentData() {
        return commentData;
    }

    public void setCommentData(String commentData) {
        this.commentData = commentData;
    }

    public Notification(String dateTime, String fromUser, String fromEmail, String fromUserAvatar, String isComment, String isReaction, String commentData) {
        this.dateTime = dateTime;
        this.fromUser = fromUser;
        this.fromEmail = fromEmail;
        this.fromUserAvatar = fromUserAvatar;
        this.isComment = isComment;
        this.isReaction = isReaction;
        this.commentData = commentData;
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
