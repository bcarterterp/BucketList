package carter.brandon.bucket;

import java.util.ArrayList;
import java.util.Date;

public class Bucket {
	
	private String name,desc;
	private Date created, completed;
	private int totalItems, itemsCompleted;
	private boolean complete;
	private ArrayList<BucketItem> bucketList;
	
	public Bucket(String name, String desc,Date created) {
		
		this.name = name;
		this.desc = desc;
		this.created = created;
		complete = false;
		bucketList = new ArrayList<BucketItem>();

	}
	
	public Bucket(String name, String desc,Date created, Date completed, int totalItems,
			int itemsCompleted,boolean complete, ArrayList<BucketItem> bucketList) {
		
		this.name = name;
		this.desc = desc;
		this.created = created;
		this.completed = completed;
		this.totalItems = totalItems;
		this.itemsCompleted = itemsCompleted;
		this.complete = complete;
		this.bucketList = bucketList;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public int getTotalItems() {
		return totalItems;
	}
	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}
	public int getItemsCompleted() {
		return itemsCompleted;
	}
	public void setItemsCompleted(int itemsCompleted) {
		this.itemsCompleted = itemsCompleted;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public ArrayList<BucketItem> getBucketList() {
		return bucketList;
	}

	public void setBucketList(ArrayList<BucketItem> bucketList) {
		this.bucketList = bucketList;
	}
	
	

}
