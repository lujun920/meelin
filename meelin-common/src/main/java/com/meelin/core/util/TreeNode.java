package com.meelin.core.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p/>类文件: com.meelin.core.util.TreeNode.java
 * <p/>类功能描述: 树形菜单封装类
 *
 * @作者: luj
 * @时间: 2016/12/20 15:14
 */
public class TreeNode implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String pId;
	private String name;
	private String uri;
	private boolean open;
	private boolean checked;
	private boolean isParent;
	private List<TreeNode>	children=new ArrayList<TreeNode>();

	public TreeNode(String id, String pId, String name, String uri, boolean open){
		this.id= id;
		this.pId= pId;
		this.name= name;
		this.uri= uri;
		this.open= open;
	}
	public TreeNode(String id, String pId, String name, String uri, boolean open, boolean checked){
		this.id= id;
		this.pId= pId;
		this.name= name;
		this.uri= uri;
		this.open= open;
		this.checked= checked;
	}
	
	/**
	 * @param id
	 * @param pId
	 * @param name
	 * @param uri
	 * @param open
	 * @param checked
	 * @param isParent
	 */
	public TreeNode(String id, String pId, String name, String uri, boolean open, boolean checked, boolean isParent) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.uri = uri;
		this.open = open;
		this.checked = checked;
		this.isParent = isParent;
	}
	public void add(TreeNode node){
		//递归添加节点
		if(String.valueOf("0").equals(String.valueOf(node.pId))){
			this.children.add(node);
		} else	if(node.pId.equals(this.id)){
			this.children.add(node);
		}else{
			for (TreeNode tmp_node : children){
				tmp_node.add(node);
			}
		}
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public List<TreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	public boolean getIsParent() {
		return isParent;
	}
	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}
}
