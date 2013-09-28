package jp.recruit.hps.movie.client.utils;

import java.util.List;
import com.appspot.hps_movie.selectionEndpoint.model.CompanyV1Dto;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class CompanyPriferences {
	private static final String KEYS_NAME[]={
		"comapanyname1","comapanyname2","comapanyname3"
	};
	private static final String KEYS_PHASE[]={
		"comapanyphase1","comapanyphase2","comapanyphase3"
	};
	private static final String KEYS_DATE[]={
		"comapanydate1","comapanydate2","comapanydate3"
	};

	SharedPreferences pref;
	SharedPreferences.Editor editor;
	private List<CompanyV1Dto> mList;
	private Context context;

	public CompanyPriferences(Context context,List<CompanyV1Dto> list){
		mList = list;
		this.context = context;
	}

	public void setCompanyData(){
		pref = context.getSharedPreferences(CommonUtils.STRING_PREF_KEY, Activity.MODE_PRIVATE);
		// Editor の設定
		editor = pref.edit();
		for(int i=0;i<mList.size();i++){
			if(mList.get(i)!=null){
				// Editor に値を代入
				if(mList.get(i).getName()!=null){
					editor.putString(KEYS_NAME[i], mList.get(i).getName());
				}
				if(mList.get(i).getPhase()!=null){
					editor.putString(KEYS_PHASE[i], mList.get(i).getPhase());
				}
				if(mList.get(i).getStartDate()!=null){
					editor.putLong(KEYS_DATE[i], mList.get(i).getStartDate());
				}
			}
		}
		// データの保存
		editor.commit();
	}



}
