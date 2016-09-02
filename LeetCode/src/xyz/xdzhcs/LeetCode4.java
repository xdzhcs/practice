package xyz.xdzhcs;

public class LeetCode4 {
	public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int num=nums1.length+nums2.length;
        int i=0,j=0,cnt=0,n1=0,n2=0;
        while(cnt<num/2+1&&i<nums1.length&&j<nums2.length){
        	
        	if(nums1[i]<nums2[j]){
        		n1=n2;
        		n2=nums1[i];
        		i++;
        	}else {
        		n1=n2;
        		n2=nums2[j];
        		j++;
			}
        	cnt++;
        }
        while(cnt<num/2+1&&i<nums1.length){
        	
        	n1=n2;
    		n2=nums1[i];
        	i++;
        	cnt++;
        }
        while(cnt<num/2+1&&j<nums2.length){
        	
        	n1=n2;
    		n2=nums2[j];
        	j++;
        	cnt++;
        }
        //System.out.println("n1:"+n1+" n2:"+n2);
        if(num%2==0){
        	//如果两个数组的总数为偶数,则中位数为第n/2和第n/2+1个数的平均数,从1开始
        	return (n1+n2)/2.0;
        }else {
			//如果两个数组的总数为奇数,则中位数为第n/2+1个数,从1开始
        	return n2/1.0;
		}
    }
}
