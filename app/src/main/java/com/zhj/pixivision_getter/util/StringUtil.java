package com.zhj.pixivision_getter.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class StringUtil {
	public static List<String> getImgUrl(String msgs2) {
		String regularExpression = "<img src=\"";
		Pattern pattern=Pattern.compile(regularExpression);
		Matcher matcher =pattern.matcher(msgs2);
		String[] list = msgs2.split(regularExpression) ;
		List<String> list3 = new ArrayList<String>();
		for(int i = 0 ; i <list.length;i++){
			if(list[i].contains("jpg\"></a>")){
				String[] list2=list[i].split("jpg\"></a>");
				Log.d("items", list2[0]);
				list3.add(list2[0]);
			}
		}
		String[] list4= new String[list3.size()];
		for(int i = 0 ; i <list3.size();i++){
			list4[i] = list3.get(i);
		}
		return list3;
	}
}
