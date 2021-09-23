package com.example.tcpmoverapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private Button btnColor;
    private Button btnLeft;
    private Button btnRight;
    private Button btnUp;
    private Button btnDown;

    private Socket socket;
    private BufferedWriter bw;
    private BufferedReader br;
    private int x,y;
    private int r,g,b;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        x=400;
        y=300;
        r=200;
        g=100;
        b=25;


        btnColor= findViewById(R.id.btnColor);
        btnLeft= findViewById(R.id.btnLeft);
        btnRight= findViewById(R.id.btnRight);
        btnUp= findViewById(R.id.btnUp);
        btnDown= findViewById(R.id.btnDown);
        btnColor.setBackgroundColor(Color.rgb(r,g,b));

        initClient();

        btnUp.setOnClickListener(
                (v)->{
                    if (y>50) {
                        y-= 25;
                    }
                    sendMessage("y:"+y);

        });

        btnDown.setOnClickListener(
                (v)->{
                    if (y<550) {
                        y+= 25;
                    }
                    sendMessage("y:"+y);

                });

        btnLeft.setOnClickListener(
                (v)->{
                    if (x>50) {
                        x -= 25;
                    }
                    sendMessage("x:"+x);

                });

        btnRight.setOnClickListener(
                (v)->{
                    if (x<750) {
                        x+= 25;
                    }
                    sendMessage("x:"+x);

                });

        btnColor.setOnClickListener(
                (v)->{
                   r=(int) Math.floor(Math.random() * 256);
                   g=(int) Math.floor(Math.random() * 256);
                   b=(int) Math.floor(Math.random() * 256);
                   sendMessage("c:"+r+":"+g+":"+b);
                   btnColor.setBackgroundColor(Color.rgb(r,g,b));
                });


    }

    public void initClient() {
        new Thread(
                ()->{
                    try {
                        //10.0.2.2 Emulator
                        //IP 192.168.0.5
                        socket=new Socket("192.168.0.5",5000);

                        InputStream is= socket.getInputStream();
                        InputStreamReader isr= new InputStreamReader(is);
                        br= new BufferedReader(isr);

                        OutputStream os = socket.getOutputStream();
                        OutputStreamWriter osw= new OutputStreamWriter(os);
                        bw= new BufferedWriter(osw);

                        while(true) {
                            System.out.println("Waiting");
                            String line = br.readLine();
                            System.out.println("Recieved");
                            System.out.println("Msg: "+ line);
                        }


                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }).start();

    }

    public void sendMessage(String msg) {

        new Thread(
                ()->{

                    try {

                        bw.write(msg+"\n");
                        bw.flush();
                        runOnUiThread(()->{
                            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                        });
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }




                }).start();
    }
}