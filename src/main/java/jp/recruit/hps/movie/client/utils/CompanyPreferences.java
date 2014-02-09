package jp.recruit.hps.movie.client.utils;

import java.util.List;

import com.appspot.hps_movie.companyEndpoint.model.CompanyV1Dto;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class CompanyPreferences {
	private static final String KEYS_KEY[] = { "comapanyKey1",
			"comapanyKey2", "comapanyKey3" };
	private static final String KEYS_INTERVIEW_KEY[] = { "interviewKey1",
		"interviewKey2", "interviewKey3" };
	private static final String KEYS_DATE[] = { "comapanyDate1",
			"comapanyDate2", "comapanyDate3" };
	private static final String KEYS_NAME[] = { "comapanyName1",
		"comapanyName2", "comapanyName3" };
	
	private static final int COUNT = 3;

	static SharedPreferences pref;
	static SharedPreferences.Editor editor;

	public static void setCompanyData(Context context, List<CompanyV1Dto> list) {
		pref = context.getSharedPreferences(CommonUtils.STRING_PREF_KEY,
				Activity.MODE_PRIVATE);
		editor = pref.edit();
		int count = COUNT;
		if (list.size() < 3) {
			count = list.size();
		}

		for (int i = 0; i < count; i++) {
			if (list.get(i) != null) {
				// Editor に値を代入
				if (list.get(i).getInterviewKey() != null) {
					editor.putString(KEYS_INTERVIEW_KEY[i], list.get(i).getInterviewKey());
				}
				if (list.get(i).getKey() != null) {
					editor.putString(KEYS_KEY[i], list.get(i).getKey());
				}
				if (list.get(i).getStartDate() != null) {
					editor.putLong(KEYS_DATE[i], list.get(i).getStartDate());
				}
				if (list.get(i).getName() != null) {
					editor.putString(KEYS_NAME[i], list.get(i).getName());
				}
				
			}
		}
		// データの保存
		editor.commit();
	}

	public static String getcompanyKey(int i) {
		if (pref != null && i < 3) {
			return pref.getString(KEYS_KEY[i], null);
		}
		return null;
	}
	
	public static String getinterviewKey(int i) {
		if (pref != null && i < 3) {
			return pref.getString(KEYS_INTERVIEW_KEY[i], null);
		}
		return null;
	}
	

	public static Long getTime(int i) {
		if (pref != null && i < 3) {
			return pref.getLong(KEYS_DATE[i], -1l);
		}
		return -1l;
	}
	
	public static String getName(int i) {
		if (pref != null && i < 3) {
			return pref.getString(KEYS_NAME[i], null);
		}
		return null;
	}
	
	public static void cleanPref(int i){
		if(pref !=null&& i<3){
			
			pref.edit().remove(KEYS_DATE[i]).commit();
			pref.edit().remove(KEYS_KEY[i]).commit();
			pref.edit().remove(KEYS_NAME[i]).commit();
		}
	}
}
