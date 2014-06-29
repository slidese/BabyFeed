package se.slide.babyfeed;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class CustomDialog extends DialogFragment {
    
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_MESSAGE = "message";
    
    private String mTitle = "About";
    private String mMessage = "";

    public CustomDialog() {
        
    }
    
    public static final CustomDialog newInstance(String title, String message)
    {
        CustomDialog fragment = new CustomDialog();
        Bundle bdl = new Bundle(2);
        bdl.putString(EXTRA_TITLE, title);
        bdl.putString(EXTRA_MESSAGE, message);
        fragment.setArguments(bdl);
        
        return fragment;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_dialog, container);
        
        Button btnOk = (Button) view.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                dismiss();
                
            }
        });
        
        mTitle = getArguments().getString(EXTRA_TITLE);
        mMessage = getArguments().getString(EXTRA_MESSAGE);
        
        getDialog().setTitle(mTitle);
        
        TextView textMessage = (TextView) view.findViewById(R.id.textMessage);
        textMessage.setText(Html.fromHtml(mMessage));

        return view;
    }

}
