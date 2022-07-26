package ca.homedepot.preference.repositories.entities;

import lombok.Data;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * 
 * InventoryStatusMessagesEntity
 *
 */
@Entity
@Data
@Table(name = "inventory_status_messages")
@IdClass(NotificationStatusMessageID.class)
public class InventoryStatusMessagesEntity
{

	/**
	 * article id
	 */
	@Id
	@Column(name = "article_id")
	private String articleId;

	/**
	 * source id
	 */
	@Id
	@Column(name = "source_id")
	private String sourceId;

	/**
	 * status
	 */
	@Column(name = "status")
	private String status;
	/**
	 * content of data
	 */
	@Column(name = "message_json")
	private String messageJson;
	/**
	 * created Ts
	 */
	@Column(name = "createdts")
	private Date createdTs;
}
