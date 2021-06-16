package com.example.showcountry;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Server {
    private RequestQueue  requestQueue;
    private static Server mInstance;

    private Server(Context context){
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static  synchronized Server getmInstance(Context context){
        if (mInstance == null){
            mInstance = new Server(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
