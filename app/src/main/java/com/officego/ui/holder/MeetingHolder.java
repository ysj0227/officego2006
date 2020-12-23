package com.officego.ui.holder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.R;

/**
 * Created by shijie
 * Date 2020/12/23
 **/
public class MeetingHolder extends RecyclerView.ViewHolder {
    public TextView tvMeetingTitle, tvMeetingMore;
    public RecyclerView rvMeeting;

    public MeetingHolder(View itemView) {
        super(itemView);
        tvMeetingTitle = itemView.findViewById(R.id.tv_meeting_title);
        tvMeetingMore = itemView.findViewById(R.id.tv_meeting_more);
        rvMeeting = itemView.findViewById(R.id.rv_meeting);
    }
}
