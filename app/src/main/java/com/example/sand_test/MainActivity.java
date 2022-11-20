package com.example.sand_test;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {


    EditText edit;
    TextView text;

    String key="EGxdWRMjxDcYICQJA8X%2B3XCCbUMPFTyGwIJ%2FMVfSGrT3YzJu70x%2B9Dwqgd0x0o9g9vlJgD7PeMJVNojKVA1rHA%3D%3D";

    String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit= (EditText)findViewById(R.id.edit);
        text= (TextView)findViewById(R.id.text);
    }

    // 검색 버튼 클릭
    public void mOnClick(View v){

        Toast.makeText(getApplicationContext(),"ONclick!!!",Toast.LENGTH_SHORT).show();

        // 감섹 버튼 클릭시 키보드 내리기
        InputMethodManager manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        switch( v.getId() ){
            case R.id.button:
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        data= getXmlData();//아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                text.setText(data); //TextView에 문자열  data 출력
                            }
                        });

                    }
                }).start();

                break;
        }

    }

    String getXmlData(){

        StringBuffer buffer=new StringBuffer();

        String str= edit.getText().toString();//EditText에 작성된 Text얻어오기
        String[] list = str.split(" "); //띄어쓰기로 구분

        String location =""; //서울특별시
        String detail_location =""; //강남구

        try {
            location = URLEncoder.encode(list[0],"utf-8");
            detail_location=URLEncoder.encode(list[1],"utf-8");
            //한글의 경우 인식이 안되기에 utf-8 방식으로 encoding     //지역 검색 위한 변수
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String queryUrl="http://apis.data.go.kr/B552657/ErmctInfoInqireService/getEmrrmRltmUsefulSckbdInfoInqire?"//요청 URL
                +"ServiceKey=" + key +"&STAGE1="+ location
                + "&STAGE2=" + detail_location
                +"&pageNo=1&numOfRows=30";
        // STAGE2가 없어도 가능하도록 조작해야함!

        System.out.println(queryUrl); //queryUrl확인

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            System.out.println("inTry");
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            System.out.println(url);

            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            System.out.println("try!");
            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기
            String tag;

            xpp.next();
            int eventType= xpp.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        System.out.println("파싱시작!!!");
                        break;

                    case XmlPullParser.START_TAG:
                        tag= xpp.getName();//태그 이름 얻어오기

                        if(tag.equals("item")) ;// 첫번째 검색결과
                        else if(tag.equals("dutyName")){
                            buffer.append("기관명 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//addr 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        else if(tag.equals("dutyTel3")){
                            buffer.append("응급실전화 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("hpid")){
                            buffer.append("기관코드 :");
                            xpp.next();
                            buffer.append(xpp.getText());//cpId
                            buffer.append("\n");
                        }
                        else if(tag.equals("hv1")){
                            buffer.append("응급실 당직의 직통연락처:");
                            xpp.next();
                            buffer.append(xpp.getText());//cpNm
                            buffer.append("\n");
                        }
                        else if(tag.equals("hv10")){
                            buffer.append("VENTI(소아):");
                            xpp.next();
                            buffer.append(xpp.getText());//
                            buffer.append("\n");
                        }
                        else if(tag.equals("hv11")){
                            buffer.append("인큐베이터:");
                            xpp.next();
                            buffer.append(xpp.getText());//
                            buffer.append("\n");
                        }
                        else if(tag.equals("hv12")){
                            buffer.append("소아당직의 직통연락처:");
                            xpp.next();
                            buffer.append(xpp.getText());//csId
                            buffer.append("\n");
                        }
                        else if(tag.equals("hv2")){
                            buffer.append("내과 중환자실:");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        else if(tag.equals("hv3")){
                            buffer.append("외과 중환자실:");
                            xpp.next();
                            buffer.append(xpp.getText());//
                            buffer.append("\n");
                        }
                        else if(tag.equals("hv4")){
                            buffer.append("외과입원실(정형외과):");
                            xpp.next();
                            buffer.append(xpp.getText());//
                            buffer.append("\n");
                        }
                        else if(tag.equals("hv5")){
                            buffer.append("신경과입원실 :");
                            xpp.next();
                            buffer.append(xpp.getText());//
                            buffer.append("\n");
                        }
                        else if(tag.equals("hv6")){
                            buffer.append("신경외과 중환자실 :");
                            xpp.next();
                            buffer.append(xpp.getText());//
                            buffer.append("\n");
                        }
                        else if(tag.equals("hv7")){
                            buffer.append("약물중환자 :");
                            xpp.next();
                            buffer.append(xpp.getText());//
                            buffer.append("\n");
                        }
                        else if(tag.equals("hv8")){
                            buffer.append("화상중환자 :");
                            xpp.next();
                            buffer.append(xpp.getText());//
                            buffer.append("\n");
                        }
                        else if(tag.equals("hv9")){
                            buffer.append("외상중환자 :");
                            xpp.next();
                            buffer.append(xpp.getText());//
                            buffer.append("\n");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //태그 이름 얻어오기

                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈

                        break;
                }
                eventType= xpp.next();
            }

        } catch (Exception e) {
            System.out.println("error");
            e.printStackTrace();
            // TODO Auto-generated catch blocke.printStackTrace();
        }

        buffer.append("파싱 끝\n");

        return buffer.toString();//StringBuffer 문자열 객체 반환

    }

    public void moveButton(View v){
        if(v.getId()==R.id.button4) {
            Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
            startActivity(intent);
        }
    }

    public void moveButton2(View v){
        if(v.getId()==R.id.button3) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

}