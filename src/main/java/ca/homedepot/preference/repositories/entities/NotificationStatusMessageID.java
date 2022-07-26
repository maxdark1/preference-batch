package ca.homedepot.preference.repositories.entities;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * NotificationStatusMessageID
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationStatusMessageID implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * article id
	 */
	private String articleId;
	/**
	 * source id
	 */
	private String sourceId;


}
