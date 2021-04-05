package com.example.noriter;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class detailSearchActivity extends AppCompatActivity{
    private int pindex;
    private int cpubrend;
    private int gen;
    private String core;
    private int ram;
    private String gpu;
    private boolean printer;
    private boolean card;
    private boolean office;
    private boolean charger;

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        getSupportActionBar().setTitle("상세검색");
        setContentView(R.layout.detailsearch);

        //초기 상태 업로드
        Intent searchintent = getIntent();
        cpubrend = searchintent.getIntExtra("cpubrend",0);
        gen = searchintent.getIntExtra("gen",0);
        core = searchintent.getStringExtra("core");
        ram = searchintent.getIntExtra("ram",0);
        gpu = searchintent.getStringExtra("gpu");
        printer = searchintent.getBooleanExtra("printer",false);
        card = searchintent.getBooleanExtra("card",false);
        office = searchintent.getBooleanExtra("office",false);
        charger = searchintent.getBooleanExtra("charger",false);

        RadioGroup cpubrendrg = (RadioGroup) findViewById(R.id.search_detail_cpuradio);
        if(cpubrend==1)
            cpubrendrg.check(R.id.intel);
        else if (cpubrend==2)
            cpubrendrg.check(R.id.amd);

        final ArrayAdapter genAdpater = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.genary));
        ArrayAdapter coreAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.coreary));
        ArrayAdapter ramAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.ramary));
        ArrayAdapter gpuAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.gpuary));
        final Spinner genSpinner = (Spinner) findViewById(R.id.search_detail_gen);
        genSpinner.setAdapter(genAdpater);
        final Spinner coreSpinner = (Spinner) findViewById(R.id.search_detail_core);
        coreSpinner.setAdapter(coreAdapter);
        final Spinner ramSpinner = (Spinner) findViewById(R.id.search_detail_ram);
        ramSpinner.setAdapter(ramAdapter);
        final Spinner gpuSpinner = (Spinner) findViewById(R.id.search_detail_gpu);
        gpuSpinner.setAdapter(gpuAdapter);

        if(gen == 1 || gen == 2)
            genSpinner.setVisibility(View.VISIBLE);
        if(core != null)
            coreSpinner.setVisibility(View.VISIBLE);
        genSpinner.setSelection(gen);
        if(core != null){
            for(int i = 0 ; i < coreAdapter.getCount() ; i++){
                if(core.matches(coreSpinner.getItemAtPosition(i).toString())){
                    coreSpinner.setSelection(i);
                    break;
                }
            }
        }
        ramSpinner.setSelection(ram/4);
        if(gpu != null){
            for(int i = 0 ; i < gpuAdapter.getCount() ; i++){
                if(gpu.matches(gpuSpinner.getItemAtPosition(i).toString())){
                    gpuSpinner.setSelection(i);
                    break;
                }
            }
        }

        final CheckBox printerCB = (CheckBox) findViewById(R.id.search_detail_printer);
        final CheckBox cardCB = (CheckBox) findViewById(R.id.search_detail_card);
        final CheckBox officeCB = (CheckBox) findViewById(R.id.search_detail_office);
        final CheckBox chargerCB = (CheckBox) findViewById(R.id.search_detail_charger);

        printerCB.setChecked(printer);
        cardCB.setChecked(card);
        officeCB.setChecked(office);
        chargerCB.setChecked(charger);

        //CPU 브랜드 선택 라디오버튼 세팅
        cpubrendrg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                genSpinner.setVisibility(View.VISIBLE);
                coreSpinner.setVisibility(View.VISIBLE);
                if(checkedId == R.id.intel)
                    cpubrend = 1;
                else if(checkedId == R.id.amd)
                    cpubrend = 2;
            }
        });

        //스피너 세팅

        genSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0)
                    gen = position+3;
                else
                    gen = 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                gen = 0;
            }
        });
        coreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0)
                    core = coreSpinner.getItemAtPosition(position).toString();
                //Toast.makeText(getApplicationContext(), core, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        ramSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ram= position*4;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ram = 0;
            }
        });
        gpuSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0)
                    gpu = gpuSpinner.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //체크박스 세팅
        printerCB.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(printerCB.isChecked())
                    printer = true;
                else
                    printer = false;
            }
        });
        cardCB.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cardCB.isChecked())
                    card = true;
                else
                    card = false;
            }
        });
        officeCB.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(officeCB.isChecked())
                    office = true;
                else
                    office = false;
            }
        });
        chargerCB.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chargerCB.isChecked())
                    charger = true;
                else
                    charger = false;
            }
        });

        //적용 버튼 리스너
        Button search_detail_apply = (Button) findViewById(R.id.search_detail_apply);
        search_detail_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra("cpubrend",cpubrend);
                intent.putExtra("gen", gen);
                intent.putExtra("core",core);
                intent.putExtra("ram",ram);
                intent.putExtra("gpu",gpu);
                intent.putExtra("printer",printer);
                intent.putExtra("card",card);
                intent.putExtra("office",office);
                intent.putExtra("charger",charger);
                startActivity(intent);
            }
        });
    }
}