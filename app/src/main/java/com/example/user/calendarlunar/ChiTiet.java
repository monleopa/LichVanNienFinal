package com.example.user.calendarlunar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ChiTiet extends AppCompatActivity {


    private String DATABASE_NAME = "dbChiTiet.sqlite";
    private String DB_PATH_SUFFIX = "/databases/";
    private SQLiteDatabase database = null;
    TextView txtHoang, txtHac, txtTot, txtXau, txtNen, txtKnen, txtCTNgay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        init();
        docDB();

        Intent intent = getIntent();

        String ngay = intent.getStringExtra("ngay");
        String thangnam = intent.getStringExtra("thangnam");


        String s2 = "";
        String s = "";
        for (int i = 0; i < thangnam.length(); i++){
            if(i >= 6){
                if (thangnam.charAt(i) != '/'){
                    s += thangnam.charAt(i);
                }
                s2 += thangnam.charAt(i);
            }
        }
        String ctngay = ngay + s2;
        ctngay = ctngay.replaceAll("\\s+", "/");
        String ngay2 = ngay + s;
        ngay2 = ngay2.replaceAll("\\s+", "");
        System.out.println(ngay2);
        txtCTNgay.setText("Chi tiết ngày: "+ctngay);
        showDB(ngay2);
    }

    private void init() {
        txtHoang = findViewById(R.id.txtHoang);
        txtHac = findViewById(R.id.txtHac);
        txtTot = findViewById(R.id.txtTot);
        txtXau = findViewById(R.id.txtXau);
        txtNen = findViewById(R.id.txtNen);
        txtKnen = findViewById(R.id.txtKnen);
        txtCTNgay = findViewById(R.id.txtCTNgay);
    }

    private void showDB(String ngay){
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.query("chitietngay", null,null,null,null,null,null);
        while(cursor.moveToNext()){
            String maN = cursor.getString(0);

            if( maN.equals(ngay)){
                String hoang = cursor.getString(1);
                String hac = cursor.getString(2);
                String tot = cursor.getString(3);
                String xau = cursor.getString(4);
                String nen = cursor.getString(5);
                String knen = cursor.getString(6);

                txtHoang.setText(hoang);
                txtHac.setText(hac);
                txtTot.setText(tot);
                txtXau.setText(xau);
                txtNen.setText(nen);
                txtKnen.setText(knen);
            }
        }
        cursor.close();
    }

    private void docDB() {
        File dbfile = getDatabasePath(DATABASE_NAME);
        if(!dbfile.exists()){
            try {
                CopyDBFromAssets();
                Toast.makeText(this,"thanh cong", Toast.LENGTH_LONG).show();
            }
            catch (Exception e){
                Toast.makeText(this,"that bai", Toast.LENGTH_LONG).show();
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
