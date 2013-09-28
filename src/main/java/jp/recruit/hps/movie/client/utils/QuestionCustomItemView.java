package jp.recruit.hps.movie.client.utils;

import jp.recruit.hps.movie.client.R;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.FrameLayout;
 
public class QuestionCustomItemView extends FrameLayout implements Checkable {
 
    private CheckBox mCheckBox;
     
    public QuestionCustomItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }
 
    public QuestionCustomItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }
 
    public QuestionCustomItemView(Context context) {
        super(context);
        initialize();
    }
     
    private void initialize() {
        // レイアウトを追加する
        addView(inflate(getContext(), R.layout.question_custom_item_view, null));
        mCheckBox = (CheckBox) findViewById(R.id.check_box);
    }
 
    @Override
    public boolean isChecked() {
        return mCheckBox.isChecked();
    }
 
    @Override
    public void setChecked(boolean checked) {
        // RadioButton の表示を切り替える
        mCheckBox.setChecked(checked);
        
    }
 
    @Override
    public void toggle() {
    }
  
    
}