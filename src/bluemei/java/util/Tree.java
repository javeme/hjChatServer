package bluemei.java.util;

import java.util.LinkedList;

public class Tree {
	private TreeNode root=null;
	
	public Tree(Object rootElement)
	{
		TreeNode node=new TreeNode();
		node.selfValue=rootElement;
		//node.parent=null;
		//node.firstChild=null;
		//node.nextSibling=null;
		this.root=node;
	}
	public boolean addNode(Object self,Object parent)
	{
		TreeNode parentNode=findNode(parent);
		if(parentNode==null)
			return false;
		TreeNode selfNode=new TreeNode();
		selfNode.selfValue=self;
		selfNode.parent=parentNode;
		return true;
	}
	public boolean addNode(Object self,Object parent,Object leftSibling)
	{
		TreeNode parentNode=findNode(parent);
		if(parentNode==null)
			return false;
		TreeNode leftSiblingNode=findNode(leftSibling);
		if(leftSiblingNode==null)
			return false;
		TreeNode selfNode=new TreeNode();
		selfNode.selfValue=self;
		selfNode.parent=parentNode;
		selfNode.nextSibling=leftSiblingNode.nextSibling;
		leftSiblingNode.nextSibling=selfNode;
		return true;
	}

	public boolean isParentOf(Object self,Object other)
	{
		TreeNode selfNode=findNode(self);
		TreeNode otherNode=findNode(other);
		if(selfNode==null || otherNode==null)
		{
			return false;
		}
		else
		{
			TreeNode childNode=selfNode.firstChild;
			TreeNode nextSiblingNode=null;
			LinkedList<TreeNode> queue=new LinkedList<TreeNode>();
			while(childNode!=null)
			{
				if(childNode==otherNode)
					return true;
				
				nextSiblingNode=childNode.nextSibling;
				while(nextSiblingNode!=null)
				{
					if(nextSiblingNode==otherNode)
						return true;
					if(nextSiblingNode.firstChild != null)
						queue.add(nextSiblingNode.firstChild);
					nextSiblingNode=nextSiblingNode.nextSibling;
				}
				childNode=queue.removeFirst();
			}
			return false;
		}
	}
	public Object getParent(Object value)
	{
		TreeNode selfNode=findNode(value);
		if(selfNode==null)
			return null;
		return selfNode.parent.selfValue;
	}
	public Object getNextSibling(Object value)
	{
		TreeNode selfNode=findNode(value);
		if(selfNode==null)
			return null;
		return selfNode.nextSibling.selfValue;
	}
	public Object getChild(Object value,int oderNum)
	{
		TreeNode selfNode=findNode(value);
		if(selfNode==null)
			return null;
		TreeNode childNode=selfNode.firstChild;
		if(childNode==null)
			return null;
		if(oderNum<=0)
			return null;
		for(int i=1; childNode!=null;i++)
		{
			if(i==oderNum)
				return childNode.selfValue;
			childNode=childNode.nextSibling;
		}
		return null;
	}
	private TreeNode findNode(Object value)
	{
		TreeNode childNode=root;
		TreeNode nextSiblingNode=null;
		LinkedList<TreeNode> queue=new LinkedList<TreeNode>();
		while(childNode!=null)
		{
			if(childNode.selfValue.equals(value))
				return childNode;
			
			nextSiblingNode=childNode.nextSibling;
			while(nextSiblingNode!=null)
			{
				if(nextSiblingNode.selfValue.equals(value))
					return nextSiblingNode;
				if(nextSiblingNode.firstChild != null)
					queue.add(nextSiblingNode.firstChild);
				nextSiblingNode=nextSiblingNode.nextSibling;
			}
			childNode=queue.removeFirst();
		}
		return null;
	}
}
