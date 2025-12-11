package com.gm.wj.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "feedback")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    // 多对一关联用户
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "type")
    private String type = "suggestion";  // suggestion, bug, other

    @Column(name = "status")
    private String status = "pending"; // pending, processing, resolved

    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(name = "reply_content")
    private String replyContent;

    @Column(name = "reply_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date replyTime;

    // 构造方法
    public Feedback() {
        this.createTime = new Date();
    }

    public Feedback(User user, String title, String content, String type) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.type = type;
        this.status = "pending";
        this.createTime = new Date();
    }

    // Getter 和 Setter 方法
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getReplyContent() { return replyContent; }
    public void setReplyContent(String replyContent) { this.replyContent = replyContent; }

    public Date getReplyTime() { return replyTime; }
    public void setReplyTime(Date replyTime) { this.replyTime = replyTime; }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", user=" + (user != null ? user.getUsername() : "null") +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
