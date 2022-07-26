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
@Table(name = "registration_analytics")
public class RegistrationEntity
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
	 * The action.
	 */
	@Column(name = "action_type")
	private String actionType;

	/**
	 * The email Id.
	 */
	@Column(name = "email_id")
	private String emailId;

	@CreationTimestamp
	@Column(name = "createdts", updatable = false)
	private Date createdOn;
}