package com.example.user.calendarlunar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class CongViec extends AppCompatActivity {

    private String DATABASE_NAME = "dbChiTiet.sqlite";
    private String DB_PATH_SUFFIX = "/databases/";
    private SQLiteDatabase database2 = null;

    ListView lvCongViec;
    CustomAdapterCV customAdapterCV;
    ArrayList<CongViecCustom> mangcv;

    Button btnThem;
    TextView txtNgaycv;
    Button btnXoa, btnSua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cong_viec);
        init();
        docDB();
        showCVonlist();
        addControl();
        addEvent();
    }

    private void addControl() {

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(CongViec.this);
                dialog.setTitle("Công việc");
                dialog.setCancelable(false); // không cho click ra ngoài hộp thoại
                dialog.setContentView(R.layout.themcongviec);
                dialog.show();

                final EditText edtCV = dialog.findViewById(R.id.edtCV);
                final EditText edtCV2 = dialog.findViewById(R.id.edtCV2);
                Button btnHuyCV = dialog.findViewById(R.id.btnHuyCV);
                final Button btnHenGio = dialog.findViewById(R.id.btnHenGio);
                final TimePicker timePicker = dialog.findViewById(R.id.timePicker);
                final DatePicker datePicker = dialog.findViewById(R.id.datePicker);
                Calendar calendar = Calendar.getInstance();

                btnHuyCV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

                btnHenGio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String s6 = edtCV.getText().toString();
                        String s7 = edtCV2.getText().toString();

                        int month = datePicker.getMonth();
                        int day = datePicker.getDayOfMonth();
                        int year = datePicker.getYear();

                        timePicker.setIs24HourView(true);
                        int gio = timePicker.getCurrentHour();
                        int phut = timePicker.getCurrentMinute();

                        String gio2 = String.valueOf(gio);
                        String phut2 = String.valueOf(phut);
                        if (phut<10) phut2 = "0"+ String.valueOf(phut);
                        if (gio<10) gio2 = "0" + String.valueOf(gio);

                        String s2 = String.valueOf(day);
                        String s3 = String.valueOf(month);
                        String s4 = String.valueOf(year);
                        String s5 = gio2 + ":" + phut2;
                        s5 = s5.replaceAll("\\s+", "");
                        System.out.println(s2+" " +s3+" " +s4+" "+s5+ " "+s6);
                        themDB(s2,s3,s4,s5,s6,s7);
                        dialog.cancel();
                    }
                });
            }
        });



//        btnXoa.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                database2.delete("congviec", "mangay = ?", new String[]{"2"}));
//                showCVonlist();
//            }
//        });

    }

    private void themDB(String s2, String s3, String s4, String s5, String s6, String s7) {
        ContentValues row = new ContentValues();

        row.put("ngay",s2);
        row.put("thang",s3);
        row.put("nam",s4);
        row.put("time",s5);
        row.put("tittle",s6);
        row.put("content",s7);

        long r = database2.insert("congviec",null,row);

        if(r > 0){
            Toast.makeText(CongViec.this,"Đã thêm công việc",Toast.LENGTH_LONG).show();
        }

        showCVonlist();
    }

    private void addEvent() {

    }

    private void showCVonlist() {
        database2 = openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE, null);
        Cursor cursor2 = database2.query("congviec",null,null,null,null,null,null);
        mangcv.clear();

        while(cursor2.moveToNext()){
            int id = cursor2.getInt(0);
            String ngay = cursor2.getString(1);
            String thang = cursor2.getString(2);
            String nam = cursor2.getString(3);
            String time = cursor2.getString(4);
            String tittle = cursor2.getString(5);
            String content = cursor2.getString(6);
            CongViecCustom congViecCustom = new CongViecCustom(id,ngay,thang,nam,time,tittle,content);
            mangcv.add(congViecCustom);
        }
        cursor2.close();
        customAdapterCV.notifyDataSetChanged();
    }

   // @SuppressLint("WrongViewCast")
    private void init() {
        lvCongViec = findViewById(R.id.lvCongViec);
        txtNgaycv = findViewById(R.id.txtNgaycv);
        mangcv = new ArrayList<CongViecCustom>();
        customAdapterCV = new CustomAdapterCV(CongViec.this,mangcv);
        lvCongViec.setAdapter(customAdapterCV);
        btnThem = findViewById(R.id.btnThem);
     //   btnXoa = findViewById(R.id.btnXoa);
     //   btnSua = findViewById(R.id.btnSua);
    }

    private void docDB() {
        File dbfile = getDatabasePath(DATABASE_NAME);
        if(!dbfile.exists()){
            try {
                CopyDBFromAssets();
               // Toast.makeText(this,"thanh cong", Toast.LENGTH_LONG).show();
            }
            catch (Exception e){
              //  Toast.makeText(this,"that bai", Toast.LENGTH_LONG).show();
            }
        }

    }
    private void CopyDBFromAssets() {
        try {
            InputStream myInput = getAssets().open(DATABASE_NAME);
            String outFileName = layLink();
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);

            if(!f.exists()){
                f.mkdir();
            }

            OutputStream myOutput =  new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;

            while((length = myInput.read(buffer)) > 0){
                myOutput.write(buffer, 0 , length);
            }

            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
        catch (Exception e){
            e.printStackTrace();
            Log.d("Loi",e.toString());
        }
    }
    private String layLink(){
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }
}
