package com.cnsunway.saas.wash.adapter;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.model.Comment;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;


public class CommentsAdapter extends BaseAdapter{

    List<Comment> comments;
    Context context;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    public void clear(){
        if(comments != null){
            comments.clear();
        }
    }
    public CommentsAdapter(List<Comment> comments, Application context ){
        this.comments = comments;
        this.context = context;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int i) {
        return comments.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Comment comment = (Comment) getItem(i);
        Holder holder = null;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.activity_user_comment_item,null);
            holder = new Holder();
            holder.comment = (TextView) view.findViewById(R.id.tv_comment);   //用户评论

            holder.nickName = (TextView) view.findViewById(R.id.tv_nickName);//昵称 nickName
            holder.score1 = (ImageView) view.findViewById(R.id.image_score1);//星级 storeScore
            holder.score2 = (ImageView) view.findViewById(R.id.image_score2);
            holder.score3 = (ImageView) view.findViewById(R.id.image_score3);
            holder.score4 = (ImageView) view.findViewById(R.id.image_score4);
            holder.score5 = (ImageView) view.findViewById(R.id.image_score5);

            holder.createdDate = (TextView) view.findViewById(R.id.tv_createdDate); //createdDate 评论时间

            holder.headPortrait = (RoundedImageView) view.findViewById(R.id.image_headPortrait);   // headPortraitUrl 头像图片URL
            view.setTag(holder);
        }else {
            holder = (Holder) view.getTag();
        }

//        holder.storeName.setText(store.getStoreName());
//

        holder.comment.setText(comment.getComment());
        holder.createdDate.setText(comment.getCreatedDate());
        holder.nickName.setText(comment.getNickName());
        switch (comment.getStoreScore()){
            case 0:
                holder.score1.setImageResource(R.mipmap.star_gray);
                holder.score2.setImageResource(R.mipmap.star_gray);
                holder.score3.setImageResource(R.mipmap.star_gray);
                holder.score4.setImageResource(R.mipmap.star_gray);
                holder.score5.setImageResource(R.mipmap.star_gray);
                break;
            case 1:
                holder.score1.setImageResource(R.mipmap.star_orange);
                holder.score2.setImageResource(R.mipmap.star_gray);
                holder.score3.setImageResource(R.mipmap.star_gray);
                holder.score4.setImageResource(R.mipmap.star_gray);
                holder.score5.setImageResource(R.mipmap.star_gray);
                break;
            case 2:
                holder.score1.setImageResource(R.mipmap.star_orange);
                holder.score2.setImageResource(R.mipmap.star_orange);
                holder.score3.setImageResource(R.mipmap.star_gray);
                holder.score4.setImageResource(R.mipmap.star_gray);
                holder.score5.setImageResource(R.mipmap.star_gray);
                break;
            case 3:
                holder.score1.setImageResource(R.mipmap.star_orange);
                holder.score2.setImageResource(R.mipmap.star_orange);
                holder.score3.setImageResource(R.mipmap.star_orange);
                holder.score4.setImageResource(R.mipmap.star_gray);
                holder.score5.setImageResource(R.mipmap.star_gray);
                break;
            case 4:
                holder.score1.setImageResource(R.mipmap.star_orange);
                holder.score2.setImageResource(R.mipmap.star_orange);
                holder.score3.setImageResource(R.mipmap.star_orange);
                holder.score4.setImageResource(R.mipmap.star_orange);
                holder.score5.setImageResource(R.mipmap.star_gray);
                break;
            case 5:
                holder.score1.setImageResource(R.mipmap.star_orange);
                holder.score2.setImageResource(R.mipmap.star_orange);
                holder.score3.setImageResource(R.mipmap.star_orange);
                holder.score4.setImageResource(R.mipmap.star_orange);
                holder.score5.setImageResource(R.mipmap.star_orange);
                break;


        }
        Glide.with(context).load(comment.getHeadPortraitUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.headPortrait);
        return view;
    }

    class Holder{

        public  TextView comment;
        public TextView nickName;
        public ImageView score1,score2,score3,score4,score5;
        public TextView createdDate;
        public RoundedImageView headPortrait;


    }
}
