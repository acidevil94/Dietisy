package com.example.andrea.dietisy.fragment.secondaryFrags;

import android.support.v4.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.andrea.dietisy.R;

/**
 * Created by andrea on 04/12/2016.
 */

public class MsgBoxFragment extends DialogFragment {

        private boolean answerRequested;    //è richiesta la scelta da parte dell'utente?
        private String message;

        public interface MsgBoxListener {
            public void onYes(DialogFragment dialog);
            public void onNo(DialogFragment dialog);
        }

        private MsgBoxListener mListener;

        public void onCreate(Bundle state) {
            super.onCreate(state);
            setRetainInstance(true);
        }

        public static MsgBoxFragment newInstance(MsgBoxListener listener,boolean b,String m) {
            MsgBoxFragment fragment = new MsgBoxFragment();
            fragment.mListener = listener;
            fragment.answerRequested = b;
            fragment.message = m;
            return fragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            setStyle(STYLE_NORMAL,0);
            LayoutInflater i =  getActivity().getLayoutInflater();
            View v  = i.inflate(R.layout.fragment_msg_box,null);

            TextView txtMessage =(TextView) v.findViewById(R.id.txtMsgBoxMessage);
            Button btnYes = (Button) v.findViewById(R.id.btnMsgBoxYes);
            Button btnNo = (Button) v.findViewById(R.id.btnMsgBoxNo);



            txtMessage.setText(message);
            String yesMessage = "OK";
            btnNo.setVisibility(View.INVISIBLE);

            if(answerRequested) {
                yesMessage = "SI";
                btnNo.setVisibility(View.VISIBLE);
                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onNo(MsgBoxFragment.this);
                        }
                    }
                });
            }
            btnYes.setText(yesMessage);

            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onYes(MsgBoxFragment.this);
                    }
                }
            });

            builder.setView(v);
           // builder.setTitle("Warning!")
               //     .setIcon(android.R.drawable.ic_dialog_alert);
            return builder.create();
        }
    }


