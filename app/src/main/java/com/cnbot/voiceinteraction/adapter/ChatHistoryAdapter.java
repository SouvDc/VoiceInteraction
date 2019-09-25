package com.cnbot.voiceinteraction.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnbot.voiceinteraction.R;
import com.cnbot.voiceinteraction.bean.Msg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deng jia on 2018/7/25.
 */

public class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryAdapter.ChatHolder>{
    //定义变量
    private Context mContext;
    private List<Msg> mHistory = new ArrayList<>();

    public ChatHistoryAdapter (Context context, List<Msg> history) {
        mContext = context;
        mHistory = history;
    }
    @Override
    public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //需要传入parent，不然item不能居中
        if (viewType == Msg.INPUT_TYPE) {
            return new UserHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ai_user_chat, parent, false));
        } else if (viewType == Msg.OUTPUT_TYPE) {
            return new ChatHistoryItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ai_speech_chat_text, parent, false));
        } else {
            return null;
        }


    }

    @Override
    public void onBindViewHolder(ChatHolder holder, int position) {
        holder.tvContent.setText(Html.fromHtml(mHistory.get(position).getMessage()));
    }

    @Override
    public int getItemViewType(int position) {
        return mHistory.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mHistory.size();
    }



    public class ChatHolder extends RecyclerView.ViewHolder {

        public TextView tvTime;
        public TextView tvContent;

        public ChatHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time_chat);

        }
    }


    class ChatHistoryItem extends ChatHolder{
        public ChatHistoryItem(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_content_output_text);
        }
    }

    public class UserHolder extends ChatHolder{

        public UserHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_content_user_input);
        }
    }
}
