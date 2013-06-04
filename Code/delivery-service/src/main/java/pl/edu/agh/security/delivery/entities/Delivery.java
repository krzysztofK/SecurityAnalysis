package pl.edu.agh.security.delivery.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DELIVERIES")
public class Delivery implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "DELIVERYID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "REC_NAME")
    private String recipientName;

    @Column(name = "REC_ADDRESS")
    private String recipientAddress;

    @Column(name = "SENDER_NAME")
    private String senderName;

    @Column(name = "SENDER_ADDRESS")
    private String senderAddress;

    @Column(name = "COMPL_DTM")
    private Date completionDateTime;

    @Column(name = "PRIORITY")
    private String priority;

    @Column(name = "WEIGHT")
    private Integer weight;

    @Column(name = "STATUS")
    private String status;

    public Delivery() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRecipientAddress() {
        return this.recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public Date getCompletionDateTime() {
        return this.completionDateTime;
    }

    public void setCompletionDateTime(Date completionDateTime) {
        this.completionDateTime = completionDateTime;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
