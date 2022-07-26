package ca.homedepot.preference.repositories.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Entity
@Data
@Table(name = "notification_subscription")
public class NotificationSubscriptionEntity
{

	@Id
	@Column(name = "reg_id")
	private String regId;

	/**
	 * The article id.
	 */
	@Column(name = "article_id")
	private String articleId;

	/**
	 * The email Id.
	 */
	@Column(name = "email_id")
	private String emailId;

	/**
	 * The Phone number.
	 */
	@Column(name = "phone_no")
	private String phoneNo;

	/**
	 * The notification type.
	 */
	@Column(name = "notification_type")
	private String notificationType;

	/**
	 * The serviceId.
	 */
	@Column(name = "subscription_type")
	private String subscriptionType;

	/**
	 * language.
	 */
	@Column(name = "langcode")
	private String langcode;


	/**
	 * uom.
	 */
	@Column(name = "uom")
	private String uom;

	@CreationTimestamp
	@Column(name = "createdts", updatable = false)
	private Date createdOn;
}