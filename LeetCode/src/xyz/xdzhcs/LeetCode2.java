package xyz.xdzhcs;

public class LeetCode2 {
	public class ListNode {
	      int val;
	      ListNode next;
	      ListNode(int x) { val = x; }
	  }
	public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        int flag=0;
        int val=l1.val+l2.val+flag;
        ListNode l3=new ListNode(val%10);
        ListNode res=l3;
    	flag=val/10;
    	l1=l1.next;
    	l2=l2.next;
    	//ListNode next=l3.next;
        while(l1!=null&&l2!=null){
        	int value=l1.val+l2.val+flag;
        	//next=new ListNode(val%10);
        	l3.next=new ListNode(value%10);
        	flag=value/10;
        	l1=l1.next;
        	l2=l2.next;
        	//next=next.next;
        	l3=l3.next;
        }
        while(l1!=null){
        	int value=l1.val+flag;
        	l3.next=new ListNode(value%10);
        	flag=value/10;
        	l1=l1.next;
        	l3=l3.next;
        }
        while(l2!=null){
        	int value=l2.val+flag;
        	l3.next=new ListNode(value%10);
        	flag=value/10;
        	l2=l2.next;
        	l3=l3.next;
        }
        if(flag!=0){
            l3.next=new ListNode(flag);
        }
        return res;
    }
}
