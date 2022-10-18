package ca.homedepot.preference.model;

import lombok.Data;
import org.springframework.batch.item.ResourceAware;
import org.springframework.core.io.Resource;

/*
* LayoutB fields
* This class is resourceAware which means
* It knows which file it comes from
* */
@Data
public class EmailOptOuts implements ResourceAware
{
	/*
	 * Field emailAddress on LayoutB
	 *
	 */
	private String emailAddress;

	/*
	 * Field status on LayoutB Unsubscribed or Held
	 */
	private String status;

	/*
	 * Field status on LayoutB
	 * 
	 * @Nullable
	 */
	private String reason;

	/*
	 * Field dateUnsubscribed on LayoutB
	 *
	 */
	private String dateUnsubscribed;

	/*
	 * Field fileName not on LayoutB
	 *
	 */
	private String fileName;

	/*
	 * Method: setResource
	 * 
	 * @param resource update of which file the obj comes from
	 */
	@Override
	public void setResource(Resource resource)
	{
		this.fileName = resource.getFilename();
	}

}
