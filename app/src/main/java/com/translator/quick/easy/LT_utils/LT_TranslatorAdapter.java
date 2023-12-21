package com.translator.quick.easy.LT_utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.translator.quick.easy.R;
import com.translator.quick.easy.LT_ads.LT_AdClass;
import com.translator.quick.easy.LT_activities.LT_DetailsHistory;
import com.translator.quick.easy.LT_model.LT_DBModel;

import java.util.List;


public class LT_TranslatorAdapter extends RecyclerView.Adapter<LT_TranslatorAdapter.ViewHolder> {
    Context context;
    ViewHolder holder;
    private final List<LT_DBModel> list;
    LT_DBModel model;

    public LT_TranslatorAdapter(Context context, List<LT_DBModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.row_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        this.holder = viewHolder;
        this.model = this.list.get(i);
        viewHolder.source_lang.setText(this.model.getSource_language());
        viewHolder.source_text.setText(this.model.getSource_language_txt());
        viewHolder.target_lang.setText(this.model.getTarget_language());
        viewHolder.target_text.setText(this.model.getTarget_language_txt());
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView source_lang;
        TextView source_text;
        TextView target_lang;
        TextView target_text;

        ViewHolder(View view) {
            super(view);
            this.source_lang = (TextView) view.findViewById(R.id.source_lang);
            this.source_text = (TextView) view.findViewById(R.id.source_text);
            this.target_lang = (TextView) view.findViewById(R.id.target_lang);
            this.target_text = (TextView) view.findViewById(R.id.target_text);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    LT_TranslatorAdapter.this.model = (LT_DBModel) LT_TranslatorAdapter.this.list.get(ViewHolder.this.getAdapterPosition());
                    LT_TranslatorConstants.source_l = LT_TranslatorAdapter.this.model.getSource_language();
                    LT_TranslatorConstants.source_t = LT_TranslatorAdapter.this.model.getSource_language_txt();
                    LT_TranslatorConstants.target_l = LT_TranslatorAdapter.this.model.getTarget_language();
                    LT_TranslatorConstants.target_t = LT_TranslatorAdapter.this.model.getTarget_language_txt();
                    LT_TranslatorConstants.position = LT_TranslatorAdapter.this.model.getId();
                    LT_AdClass.showInterAd((Activity) context, new LT_AdClass.OnInterClose() {
                        @Override
                        public void onInterClose() {
                            Intent intent = new Intent(LT_TranslatorAdapter.this.context, LT_DetailsHistory.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    });

                }
            });
        }
    }
}
