package com.example.sayantani.listenit;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sayantani.listenit.libs.Global;
import com.famoussoft.libs.JSON.JSONArray;
import com.famoussoft.libs.JSON.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class songdetails extends AppCompatActivity implements View.OnTouchListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener {

    private SeekBar seekBarProgress;
    private final Handler handler = new Handler();
    JSONArray songs=null;
    int total=0;
    int current=0;
    int length;
    int mvalue=3000;
    static MediaPlayer mPlayer;
    Button btnPlay,btnPause,btnStop,btnforward,btnbackward;
    TextView txtSongTitle;
    ImageView imgAlbum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songdetails);
        btnPlay=(Button)findViewById(R.id.btnPlay);
        btnPause=(Button)findViewById(R.id.btnPause);
        btnStop=(Button)findViewById(R.id.btnStop);
        btnbackward=(Button)findViewById(R.id.btnbackward);
        btnforward=(Button)findViewById(R.id.btnforward);
        txtSongTitle=(TextView)findViewById(R.id.txtSongTitle);
        imgAlbum=(ImageView) findViewById(R.id.imgAlbum);
        initMedia();
        // loadList();
        init();
/*
        try{
            URL newurl = new URL("https://upload.wikimedia.org/wikipedia/en/5/54/Public_image_ltd_album_cover.jpg");
            Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            imgAlbum.setImageBitmap(mIcon_val);
        }
        catch (Exception e){

        }


*/
        btnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(total>0){
                    JSONObject jobj=new JSONObject(songs.getJSONObject(current).toString());
                    String title = jobj.getString("title").toString();
                    String url = jobj.getString("url").toString();
                    play(url,title);
                    //btnPause.setText("Pause");
                    btnPause.setBackgroundResource(R.drawable.pause);
                }
            }
        });

        btnforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayer != null && mPlayer.isLooping()) {
                    mPlayer.seekTo(mPlayer.getCurrentPosition()+mvalue);
                    btnforward.setBackgroundResource(R.drawable.forward);
                }

            }
        });
        btnbackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayer != null && mPlayer.isLooping()) {
                    mPlayer.seekTo(mPlayer.getCurrentPosition()-mvalue);
                    btnbackward.setBackgroundResource(R.drawable.backward);
                }
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mPlayer != null && mPlayer.isPlaying()) {
                    mPlayer.pause();
                    length = mPlayer.getCurrentPosition();
                    //btnPause.setText("Resume");
                    btnPause.setBackgroundResource(R.drawable.play);
                } else if (mPlayer != null) {
                    mPlayer.seekTo(length);
                    mPlayer.start();
                    //btnPause.setText("Pause");
                    btnPause.setBackgroundResource(R.drawable.pause);
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mPlayer != null && mPlayer.isPlaying()) {
                    mPlayer.stop();
                    //btnPause.setText("Pause");
                    btnPause.setBackgroundResource(R.drawable.pause);
                }
            }
        });

    }

    private void initMedia(){
        seekBarProgress = (SeekBar)findViewById(R.id.seekBar1);
        seekBarProgress.setMax(99); // It means 100% .0-99
        seekBarProgress.setOnTouchListener(this);
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //mPlayer = new MediaPlayer();
        mPlayer.setOnBufferingUpdateListener(this);
        mPlayer.setOnCompletionListener(this);
    }

    private void next(){
        if(total>0){
            current++;
            if(current<total){

                JSONObject jobj=new JSONObject(songs.getJSONObject(current).toString());
                String title = jobj.getString("title").toString();
                String url = jobj.getString("url").toString();
                if(mPlayer.isPlaying()){
                    mPlayer.reset();
                }
                initMedia();
                play(url,title);
                //btnPause.setText("Pause");
                btnPause.setBackgroundResource(R.drawable.pause);
            }
            else{
                current=0;
            }
        }
    }

    private void play(String url, String title){

        try {
            mPlayer.setDataSource(url);
            txtSongTitle.setText(title);
            mPlayer.prepare();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        length = mPlayer.getDuration();
        mPlayer.start();
        primarySeekBarProgressUpdater();
    }

    private void init() {
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        TextView tv = new TextView(this);
        tv.setText("Loading...");
        tv.setPadding(0, 5, 0, 0);
        tv.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        tv.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        tv.setTextSize(30);
        mainLayout.addView(tv);
        downloadList();

    }

    private void downloadList(){
        RequestQueue queue = Volley.newRequestQueue(songdetails.this);  // this = context
        //String url = Global.getHostUrl()+"getsongs.php";
        String url = "http://api.mrasif.in/demo/mp/getsongs.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jobj = new JSONObject(response);
                            loadList(jobj);
                            //tvResponse.setText(Html.fromHtml(jobj.getString("status")+"<br/>"+jobj.getString("name")));
                        }catch (Exception e){
                            System.out.println(e.getMessage().toString());
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                return params;
            }
        };
        queue.add(postRequest);
    }

    private void loadList(JSONObject data) {
        // TODO Auto-generated method stub
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        mainLayout.removeAllViews();
        int total=Integer.parseInt(data.getString("total").toString());
        this.total=total;
        JSONArray jarray=new JSONArray(data.getJSONArray("songs").toString());
        songs=jarray;
        for (int i=0; i<total; i++) {
            JSONObject jobj=new JSONObject(jarray.getJSONObject(i).toString());
            String title = jobj.getString("title").toString();
            String url = jobj.getString("url").toString();

            LinearLayout ll = new LinearLayout(this);
            ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.setPadding(0, 0, 0, 2);

            LinearLayout sll = new LinearLayout(this);
            sll.setId(i);
            sll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            sll.setPadding(5, 5, 5, 5);
            sll.setOrientation(LinearLayout.VERTICAL);
            sll.setBackgroundColor(Color.BLACK);

            TextView tvTitle = new TextView(this);
            tvTitle.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            tvTitle.setText(title);
            tvTitle.setTextColor(Color.WHITE);

            TextView tvDate = new TextView(this);
            tvDate.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            tvDate.setText("");//url
            tvDate.setTextColor(Color.WHITE);

            sll.addView(tvTitle);
            sll.addView(tvDate);

            sll.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int id=Integer.parseInt(String.valueOf(v.getId()));
                    current=id;
                    JSONObject jobj=new JSONObject(songs.getJSONObject(id).toString());
                    int btnid = Integer.parseInt(jobj.getString("id").toString());
                    String title = jobj.getString("title").toString();
                    String url = jobj.getString("url").toString();
                    //Toast.makeText(getApplicationContext(),url,Toast.LENGTH_SHORT).show();
                    if(mPlayer.isPlaying()){
                        mPlayer.reset();
                    }
                    initMedia();
                    play(url,title);

                }
            });

            ll.addView(sll);
            mainLayout.addView(ll);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    protected void onDestroy() {
        super.onDestroy();
        // TODO Auto-generated method stub
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void primarySeekBarProgressUpdater() {
        seekBarProgress.setProgress((int)(((float)mPlayer.getCurrentPosition()/length)*100)); // This math construction give a percentage of "was playing"/"song length"
        if (mPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater();
                }
            };
            handler.postDelayed(notification,1000);
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        // TODO Auto-generated method stub
        seekBarProgress.setSecondaryProgress(percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // TODO Auto-generated method stub
        //buttonPlayPause.setImageResource(R.drawable.button_play);
        next();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        if(v.getId() == R.id.seekBar1){
            if(mPlayer.isPlaying()){
                SeekBar sb = (SeekBar)v;
                int playPositionInMillisecconds = (length / 100) * sb.getProgress();
                mPlayer.seekTo(playPositionInMillisecconds);
            }
        }
        return false;
    }
}

