package com.example.sayantani.listenit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sayantani.listenit.libs.Global;
import com.example.sayantani.listenit.libs.MyDBMS;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class registration extends AppCompatActivity implements View.OnClickListener {

    EditText name,passWord,eMail,userid,cpassWord;
    Button btnRegstr;
    MyDBMS dbms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        name=(EditText)findViewById(R.id.name);
        passWord=(EditText)findViewById(R.id.passWord);
        eMail=(EditText)findViewById(R.id.eMail);
        userid=(EditText)findViewById(R.id.userid);
        cpassWord=(EditText)findViewById(R.id.cpassWord);
        btnRegstr=(Button)findViewById(R.id.btnRegstr);
        btnRegstr.setOnClickListener(this);
        dbms = new MyDBMS(this);

    }

    @Override
    public void onClick(View v) {
        /**Intent intent = new Intent(registration.this, login.class);
         startActivity(intent);*/
        if (!userid.getText().toString().equals("") && !name.getText().toString().equals("") && !eMail.getText().toString().equals("") && !passWord.getText().toString().equals("") && passWord.getText().toString().equals(cpassWord.getText().toString())) {
            registration(name.getText().toString(),userid.getText().toString(),passWord.getText().toString(),eMail.getText().toString());
        }
        else {

        }

    }

    public void registration(final String name, final String userid, final String password, final String email) {
        //RequestQueue queue = Volley.newRequestQueue(registration.this);
        // this = context
        //String url = Global.getHostUrl()+"registration.php";
        String url = "http://api.mrasif.in/demo/gchat/registration.php";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jobj = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();

                            if(jobj.getString("status").toString().equals("true")){
                                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Registration Failed",Toast.LENGTH_SHORT).show();
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

                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", userid);
                params.put("pass", password);
                params.put("name", name);
                params.put("email", email);

                return params;
            }
        };
        //queue.add(postRequest);

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        requestQueue.add(postRequest);

    }
}
