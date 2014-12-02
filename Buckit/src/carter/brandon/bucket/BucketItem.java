package carter.brandon.bucket;

import java.util.Date;

public class BucketItem {
	
	
	private String name, desc;
	private Date created, completed;
	private boolean complete;
	
	public BucketItem(String name, String desc, Date created){
		this.name = name;
		this.desc = desc;
		this.created = created;
	}
	
	public BucketItem(String name, String desc, Date created, Date completed,
			boolean complete) {
		super();
		this.name = name;
		this.desc = desc;
		this.created = created;
		this.completed = completed;
		this.complete = complete;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getCompleted() {
		return completed;
	}

	public void setCompleted(Date completed) {
		this.completed = completed;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	
	
}
