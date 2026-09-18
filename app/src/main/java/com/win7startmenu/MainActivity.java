package com.win7startmenu;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.content.Intent;
import android.net.Uri;
import android.widget.*;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setPadding(30, 40, 30, 30);

        TextView t = new TextView(this);
        t.setText("Windows 7 Start Menu\n\nفعّل الظهور فوق التطبيقات ثم شغّل القائمة.");
        t.setTextSize(20);
        l.addView(t);

        Button p = new Button(this);
        p.setText("السماح بالظهور فوق التطبيقات");

        p.setOnClickListener(v -> startActivity(new Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()))));

        l.addView(p);

        Button s = new Button(this);
        s.setText("تشغيل Start Menu");

        s.setOnClickListener(v -> {
            if (Settings.canDrawOverlays(this))
                startService(new Intent(this, StartOverlayService.class));
            else
                p.performClick();
        });

        l.addView(s);

        setContentView(l);
    }
}
