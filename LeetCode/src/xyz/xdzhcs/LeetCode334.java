package xyz.xdzhcs;

public class LeetCode334 {
	public String reverseString(String s) {
        char[] str=s.toCharArray();
        for(int i=0;i<str.length/2;i++){
        	char ch=str[i];
        	str[i]=str[str.length-i-1];
        	str[str.length-i-1]=ch;
        }
        return new String(str);
    }
}
