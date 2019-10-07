package com.yuant.demo.wechatdemo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import java.lang.ref.WeakReference;

public class WeChatHandler extends Handler {
  private WeakReference<MainActivity> weakReference = null;
  Context mContext;

  public WeChatHandler(Context context) {
    mContext = context;
    weakReference = new WeakReference(mContext);
  }

  @Override
  public void handleMessage(Message msg) {
    switch (msg.what) {
      case MessageAction.ADD_TEXT:
        break;
      case MessageAction.ADD_IMAGE:
        break;
      case MessageAction.ADD_SYSTEMINFO:
        break;
      default:
        break;
    }
  }

}
