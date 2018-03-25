package com.example.sayantani.listenit;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sayantani.listenit.libs.Global;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity implements View.OnClickListener{

    Button btnLogin,btnSign;
    TextView fPsd;
    EditText uId,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        uId=(EditText)findViewById(R.id.uId);
        pass=(EditText)findViewById(R.id.pass);
        btnSign=(Button)findViewById(R.id.btnSign);
        fPsd=(TextView)findViewById(R.id.fPsd);
        fPsd.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnSign.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnLogin:
               intent = new Intent(login.this, songdetails.class);
               startActivity(intent);
                break;
            case R.id.fPsd:
                intent=new Intent(login.this,forgot_password.class);
                startActivity(intent);
                break;
            case R.id.btnSign:
                intent=new Intent(login.this,registration.class);
                startActivity(intent);
                break;

        }
        if (!uId.getText().toString().equals("")  && !pass.getText().toString().equals("") ) {
            login(uId.getText().toString(),pass.getText().toString());
        }
        else {

        }

    }

    public void login( final String userid, final String password) {
        RequestQueue queue = Volley.newRequestQueue(login.this);
        // this = context
        String url = Global.getHostUrl()+"login.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jobj = new JSONObject(response);
                            if(jobj.getString("status").toString().equals("true")){
                                Toast.makeText(getApplicationContext(),"Welcome", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                            }
                            //   tvResponse.setText(Html.fromHtml(data));
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

                params.put("userid",userid);
                params.put("pass", password);



                return params;

            }
        };
        queue.add(postRequest);

    }

}

