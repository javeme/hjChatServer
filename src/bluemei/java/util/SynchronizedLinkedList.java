package bluemei.java.util;

import java.util.Collection;
import java.util.LinkedList;

public class SynchronizedLinkedList<E> extends LinkedList<E>{
	private static final long serialVersionUID = 3771790989584313156L;
	private boolean isLock;
	public SynchronizedLinkedList()
	{
		isLock=false;
	}
	public synchronized boolean add(E e)
	{
		/*while(isLock)
		{
			try {
				Thread.sleep(0);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}*/
		return super.add(e);
	}
	public synchronized boolean remove(Object o)
	{
		/*while(isLock)
		{
			try {
				Thread.sleep(0);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}*/
		return super.remove(o);
	}
	public synchronized E remove(int index)
	{
		/*while(isLock)
		{
			try {
				Thread.sleep(0);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}*/
		return super.remove(index);
	}
	public synchronized void addLast(E e)
	{
		/*while(isLock)
		{
			try {
				Thread.sleep(0);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}*/
		super.addLast(e);
	}
	public synchronized E removeLast()
	{
		/*while(isLock)
		{
			try {
				Thread.sleep(0);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}*/
		return super.removeLast();
	}
	public synchronized int size()
	{
		return super.size();
	}
	public synchronized void addToLast(E e)
	{
		/*while(isLock)
		{
			try {
				Thread.sleep(0);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}*/
		super.addLast(e);
	}
	public synchronized E removeLastObject()
	{
		/*while(isLock)
		{
			try {
				Thread.sleep(0);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}*/
		return super.removeLast();
	}
	
	public synchronized boolean addByCollection(Collection<? extends E> c) 
	{
		/*while(isLock)
		{
			try {
				Thread.sleep(0);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}*/
		return super.addAll(c);
	}
	public synchronized void clear() 
	{
		/*while(isLock)
		{
			try {
				Thread.sleep(0);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}*/
		super.clear();
	}
	public void lock() {
		isLock=true;
	}
	public void unLock() {
		isLock=false;
	}
}
