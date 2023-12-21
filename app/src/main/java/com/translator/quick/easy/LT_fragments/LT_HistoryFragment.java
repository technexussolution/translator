package com.translator.quick.easy.LT_fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.translator.quick.easy.R;
import com.translator.quick.easy.LT_db.LT_HelperDB;
import com.translator.quick.easy.LT_model.LT_DBModel;
import com.translator.quick.easy.LT_utils.LT_TranslatorAdapter;

import java.util.ArrayList;
import java.util.Collections;


public class LT_HistoryFragment extends Fragment {
    Context context;
    LT_DBModel data_model;
    FloatingActionButton del;
    TextView empty;
    LT_HelperDB LTHelperDB;
    RecyclerView histortrev;
    LT_TranslatorAdapter LTTranslatorAdapter;
    ArrayList<LT_DBModel> data_models = new ArrayList<>();
    int count = 0;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_history, viewGroup, false);
        this.context = getContext();
        this.empty = (TextView) inflate.findViewById(R.id.emptytext);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.datarev);
        this.histortrev = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.LTHelperDB = new LT_HelperDB(this.context);
        FloatingActionButton floatingActionButton = (FloatingActionButton) inflate.findViewById(R.id.del);
        this.del = floatingActionButton;
        floatingActionButton.setOnClickListener(new View.OnClickListener() { // from class: com.translator.quick.easy.translator_fragments.HistoryFragment.1
            @Override 
            public void onClick(View view) {
                new AlertDialog.Builder(LT_HistoryFragment.this.getActivity()).setTitle("Delete History").setMessage("Are you sure you want to delete history?").setPositiveButton(17039379, new DialogInterface.OnClickListener() { // from class: com.translator.quick.easy.translator_fragments.HistoryFragment.1.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LT_HistoryFragment.this.LTHelperDB.delAll();
                        LT_HistoryFragment.this.histortrev.setVisibility(8);
                        LT_HistoryFragment.this.del.setVisibility(View.GONE);
                        LT_HistoryFragment.this.empty.setVisibility(View.VISIBLE);
                    }
                }).setNegativeButton(17039369, (DialogInterface.OnClickListener) null).show();
            }
        });
        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.data_models.clear();
        int checkData = checkData();
        this.count = checkData;
        if (checkData == 0) {
            this.empty.setVisibility(View.VISIBLE);
            this.histortrev.setVisibility(View.GONE);
            this.del.setVisibility(View.GONE);
            return;
        }
        this.empty.setVisibility(8);
        Cursor transData = this.LTHelperDB.getTransData();
        while (transData.moveToNext()) {
            LT_DBModel dBModelLT = new LT_DBModel();
            this.data_model = dBModelLT;
            dBModelLT.setId(transData.getInt(0));
            this.data_model.setSource_language(transData.getString(1));
            this.data_model.setSource_language_txt(transData.getString(2));
            this.data_model.setTarget_language(transData.getString(3));
            this.data_model.setTarget_language_txt(transData.getString(4));
            this.data_models.add(this.data_model);
        }
        transData.close();
        this.LTHelperDB.close();
        Collections.reverse(this.data_models);
        LT_TranslatorAdapter LTTranslatorAdapter = new LT_TranslatorAdapter(this.context, this.data_models);
        this.LTTranslatorAdapter = LTTranslatorAdapter;
        this.histortrev.setAdapter(LTTranslatorAdapter);
        this.histortrev.setVisibility(View.VISIBLE);
    }

    public int checkData() {
        SQLiteDatabase writableDatabase = new LT_HelperDB(getActivity()).getWritableDatabase();
        Cursor rawQuery = writableDatabase.rawQuery("SELECT * FROM Translation_Data", null);
        int count = rawQuery.getCount();
        rawQuery.close();
        writableDatabase.close();
        return count;
    }
}
