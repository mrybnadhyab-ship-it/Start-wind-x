package com.win7startmenu;

import android.app.Service;
import android.os.IBinder;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StartOverlayService extends Service {

    WindowManager wm;
    View menu;
    View outsideView;

    final int MENU_COLOR = Color.rgb(182, 215, 233);

    @Override
    public void onCreate() {
        super.onCreate();

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        // طبقة شفافة تلتقط الضغط خارج القائمة
        outsideView = new View(this);
        outsideView.setBackgroundColor(Color.TRANSPARENT);

        WindowManager.LayoutParams outsideParams =
                new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT
                );

        outsideParams.gravity = Gravity.TOP | Gravity.START;

        outsideView.setOnClickListener(v -> stopSelf());

        wm.addView(outsideView, outsideParams);

        // القائمة
        LinearLayout panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setPadding(8, 8, 8, 8);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(MENU_COLOR);
        bg.setStroke(2, Color.rgb(120, 155, 180));
        bg.setCornerRadius(8);
        panel.setBackground(bg);

        // عنوان القائمة
        TextView title = new TextView(this);
        title.setText("Windows 7");
        title.setTextSize(21);
        title.setTextColor(Color.rgb(30, 50, 65));
        title.setGravity(Gravity.CENTER_VERTICAL);
        title.setPadding(12, 8, 12, 10);

        panel.addView(title);

        // منطقة التطبيقات
        ScrollView scroll = new ScrollView(this);

        LinearLayout appsLayout = new LinearLayout(this);
        appsLayout.setOrientation(LinearLayout.VERTICAL);

        PackageManager pm = getPackageManager();

        List<ApplicationInfo> apps =
                new ArrayList<>(
                        pm.getInstalledApplications(
                                PackageManager.GET_META_DATA
                        )
                );

        // إظهار التطبيقات القابلة للتشغيل فقط
        List<ApplicationInfo> launchableApps = new ArrayList<>();

        for (ApplicationInfo info : apps) {
            Intent launch =
                    pm.getLaunchIntentForPackage(info.packageName);

            if (launch != null) {
                launchableApps.add(info);
            }
        }

        // ترتيب أبجدي
        Collections.sort(
                launchableApps,
                new Comparator<ApplicationInfo>() {
                    @Override
                    public int compare(
                            ApplicationInfo a,
                            ApplicationInfo b) {

                        String nameA =
                                pm.getApplicationLabel(a).toString();

                        String nameB =
                                pm.getApplicationLabel(b).toString();

                        return nameA.compareToIgnoreCase(nameB);
                    }
                }
        );

        // إنشاء عناصر التطبيقات
        for (ApplicationInfo info : launchableApps) {

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setPadding(8, 5, 8, 5);

            // أيقونة التطبيق
            ImageView icon = new ImageView(this);

            Drawable drawable =
                    pm.getApplicationIcon(info);

            icon.setImageDrawable(drawable);

            LinearLayout.LayoutParams iconParams =
                    new LinearLayout.LayoutParams(42, 42);

            iconParams.setMargins(4, 0, 10, 0);

            row.addView(icon, iconParams);

            // اسم التطبيق
            TextView name = new TextView(this);

            name.setText(
                    pm.getApplicationLabel(info)
            );

            name.setTextSize(16);
            name.setTextColor(Color.rgb(30, 45, 55));
            name.setGravity(Gravity.CENTER_VERTICAL);

            row.addView(
                    name,
                    new LinearLayout.LayoutParams(
                            0,
                            52,
                            1
                    )
            );

            // فتح التطبيق
            row.setOnClickListener(v -> {

                try {

                    Intent launch =
                            pm.getLaunchIntentForPackage(
                                    info.packageName
                            );

                    if (launch != null) {

                        launch.addFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK
                        );

                        startActivity(launch);

                        stopSelf();
                    }

                } catch (Exception ignored) {
                }
            });

            appsLayout.addView(row);
        }

        scroll.addView(appsLayout);

        panel.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        350,
                        560
                )
        );

        menu = panel;

        // القائمة في الجهة اليمنى
        WindowManager.LayoutParams menuParams =
                new WindowManager.LayoutParams(
                        370,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT
                );

        menuParams.gravity =
                Gravity.BOTTOM | Gravity.END;

        menuParams.x = 8;
        menuParams.y = 82;

        wm.addView(menu, menuParams);
    }

    @Override
    public void onDestroy() {

        if (menu != null) {
            try {
                wm.removeView(menu);
            } catch (Exception ignored) {
            }
        }

        if (outsideView != null) {
            try {
                wm.removeView(outsideView);
            } catch (Exception ignored) {
            }
        }

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
            }
