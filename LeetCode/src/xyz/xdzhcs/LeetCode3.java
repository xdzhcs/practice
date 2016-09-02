package xyz.xdzhcs;

public class LeetCode3 {
	public int lengthOfLongestSubstring(String s) {
        boolean[] flag=new boolean[80];
        int begin=0;
        int max=0;
        for(int i=0;i<s.length();){
        	if(flag[s.charAt(i)-48]==false){
        		//如果字串中没有出现该字母，则标记为已出现
        		flag[s.charAt(i)-48]=true;
        		i++;
        	}else {
				//如果子串种已经出现该字母，则算当前子串长度，并与最大值进行比较
        		int length=i-begin;
        		if(length>max){
        			max=length;
        		}
        		//begin自加，并且把对应的字符标记为没出现
        		while(true){
        			flag[s.charAt(begin)-48]=false;
        			begin++;
        			System.out.print(begin+":"+s.charAt(begin)+":");
        			System.out.println((int)(s.charAt(begin)-48)==(int)(s.charAt(i)-48));
        			if(((int)(s.charAt(begin)-48)==(int)(s.charAt(i)-48))==true){
        				System.out.println("hi");
        				//begin++;
        				break;
        			}
        			
        		}
			}
        }
        int length=s.length()-begin;
        
		if(length>max){
			max=length;
		}
		return max;
    }
}
