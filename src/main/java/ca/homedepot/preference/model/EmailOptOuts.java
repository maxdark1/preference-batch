package ca.homedepot.preference.model;

import lombok.Data;
import org.springframework.batch.item.ResourceAware;
import org.springframework.core.io.Resource;


@Data
public class EmailOptOuts implements ResourceAware
{
	private String emailAddress;
	private String status;
	private String reason;
	private String dateUnsubscribed;

	private String fileName;

	@Override
	public void setResource(Resource resource) {
		this.fileName = resource.getFilename();
	}

}
