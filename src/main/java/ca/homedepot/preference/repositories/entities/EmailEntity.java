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
@Table(name = "email_analytics")
public class EmailEntity
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
	 * The inventory.
	 */
	@Column(name = "inventory")
	private Integer inventory;

	/**
	 * The email Id.
	 */
	@Column(name = "email_id")
	private String emailId;

	/**
	 * The email Type.
	 */

	@Column(name = "email_type")
	private String emailType;

	@CreationTimestamp
	@Column(name = "createdts", updatable = false)
	private Date createdOn;
}