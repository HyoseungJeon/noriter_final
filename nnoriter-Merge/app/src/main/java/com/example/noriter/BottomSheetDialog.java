package com.example.noriter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by charlie on 2017. 11. 22
 */
public class BottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    public static BottomSheetDialog getInstance() {
        return new BottomSheetDialog();
    }
    private LinearLayout lname;
    private LinearLayout Address;
    private LinearLayout Number;
    public TextView NameText;
    public TextView AddText;
    public TextView distText;
    public TextView numberText;
    int pid;
    String nname;
    String addr;
    String numb;
    int dist;
    pcinfo_item pcc = new pcinfo_item();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_dialog, container, false);
        NameText = (TextView) view.findViewById(R.id.Nametext);
        AddText = (TextView) view.findViewById(R.id.Addtext);
        numberText = (TextView) view.findViewById(R.id.numberText);
        lname = (LinearLayout) view.findViewById(R.id.name);
        Address = (LinearLayout) view.findViewById(R.id.Address);

        distText = (TextView) view.findViewById(R.id.distText);
        lname.setOnClickListener(this);
        Address.setOnClickListener(this);
        maketext(pcc.getName(), addr, numb,dist);
        return view;
    }
    @Override
    public void onClick(View view) {
        Intent pcinfoActivity = new Intent(getContext(), pcinfoActivity.class);
        pcinfoActivity.putExtra("name", pcc.getName());
        pcinfoActivity.putExtra("number", pcc.getNumber());
        pcinfoActivity.putExtra("address", addr);
        pcinfoActivity.putExtra("Card", pcc.isCard());
        pcinfoActivity.putExtra("Printer", pcc.isPrinter());
        pcinfoActivity.putExtra("Office", pcc.isOffice());
        pcinfoActivity.putExtra("Charger", pcc.isCharger());
        pcinfoActivity.putExtra("CPU", pcc.getCPU_C());
        pcinfoActivity.putExtra("GPU", pcc.getGPU());
        pcinfoActivity.putExtra("RAM", pcc.getRAM());
        pcinfoActivity.putExtra("Pc_main",pcc.getPc_main());
        pcinfoActivity.putExtra("PC_menu",pcc.getPc_menu());
        pcinfoActivity.putExtra("lo_x",pcc.getLo_x());
        pcinfoActivity.putExtra("lo_y",pcc.getLo_y());
        startActivity(pcinfoActivity);
        dismiss();
    }
    public void maketext(String a, String b, String c,int d) {
        NameText.setText("  "+a);
        AddText.setText("  "+b);
        numberText.setText("  "+c);
        distText.setText("     " +d+"m");
    }

    public void setName(String a) {
        nname = a;
    }
    public void setAddr(String a) {
        addr = a;
    }
    public void setNumb(String a) {
        numb = a;
    }
    public void setPid(int a) {pid = a;    }
    public void setDist(int a){dist=a;}
}
