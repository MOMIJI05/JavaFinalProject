package com.example.service;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.image.BufferedImage;

public class PopupMessageBuilder {

    public static void showNotification(String title) {
        String caption = "你可能有興趣的電影即將上映...！";
        String text = "哇啥！？你可能會喜歡" + title + "\n而它即將上映！趕快去看看吧！";

        if (!SystemTray.isSupported()) {
            System.err.println("系統不支援桌面通知。");
            return;
        }

        try {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            TrayIcon trayIcon = new TrayIcon(image, "電影通知");
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("電影通知");
            tray.add(trayIcon);

            trayIcon.displayMessage(caption, text, MessageType.INFO);

            Thread.sleep(2000);
            tray.remove(trayIcon);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
