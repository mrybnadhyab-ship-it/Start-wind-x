package com.win7startmenu;

import android.app.Service;
import android.os.IBinder;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StartOverlayService extends Service {

    WindowManager wm;
    View v;

    @Override
    public void onCreate() {
        super.onCreate();

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        LinearLayout p = new LinearLayout(this);
        p.setOrientation(LinearLayout.VERTICAL);
        p.setPadding(20, 15, 20, 15);

        GradientDrawable g = new GradientDrawable();
        g.setColor(Color.WHITE);
        g.setStroke(2, Color.rgb(100, 140, 190));
        g.setCornerRadius(12);
        p.setBackground(g);

        add(p, "Windows 7", 21);
        add(p, "📁  MiXplorer", 17);
        add(p, "🖼  الصور", 17);
        add(p, "⚙  الإعدادات", 17);

        v = p;

        WindowManager.LayoutParams q = new WindowManager.LayoutParams(
                330,
                -2,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        q.gravity = Gravity.BOTTOM | Gravity.START;
        q.x = 8;
        q.y = 82;

        wm.addView(v, q);
    }

    void add(LinearLayout p, String s, float z) {
        TextView t = new TextView(this);
        t.setText(s);
        t.setTextSize(z);
        t.setTextColor(Color.DKGRAY);
        t.setPadding(12, 10, 12, 10);
        p.addView(t);
    }

    @Override
    public void onDestroy() {
        if (v != null)
            wm.removeView(v);

        super.onDestroy();
    }

    @Override
    public IBinder onBind(android.content.Intent i) {
        return null;
    }
}
