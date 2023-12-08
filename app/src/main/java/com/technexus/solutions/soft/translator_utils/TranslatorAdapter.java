package com.technexus.solutions.soft.translator_utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.technexus.solutions.soft.R;
import com.technexus.solutions.soft.ads.AdClass;
import com.technexus.solutions.soft.translator_activities.DetailsHistory;
import com.technexus.solutions.soft.translator_model.DBModel;

import java.util.List;


public class TranslatorAdapter extends RecyclerView.Adapter<TranslatorAdapter.ViewHolder> {
    Context context;
    ViewHolder holder;
    private final List<DBModel> list;
    DBModel model;

    public TranslatorAdapter(Context context, List<DBModel> list) {
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
                    TranslatorAdapter.this.model = (DBModel) TranslatorAdapter.this.list.get(ViewHolder.this.getAdapterPosition());
                    TranslatorConstants.source_l = TranslatorAdapter.this.model.getSource_language();
                    TranslatorConstants.source_t = TranslatorAdapter.this.model.getSource_language_txt();
                    TranslatorConstants.target_l = TranslatorAdapter.this.model.getTarget_language();
                    TranslatorConstants.target_t = TranslatorAdapter.this.model.getTarget_language_txt();
                    TranslatorConstants.position = TranslatorAdapter.this.model.getId();
                    AdClass.showInterAd((Activity) context, new AdClass.OnInterClose() {
                        @Override
                        public void onInterClose() {
                            Intent intent = new Intent(TranslatorAdapter.this.context, DetailsHistory.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    });

                }
            });
        }
    }
}
