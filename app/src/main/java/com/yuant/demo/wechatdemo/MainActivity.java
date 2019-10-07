package com.yuant.demo.wechatdemo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import com.sce.sdk.wechatlibrary.WeChatManager;
import com.sce.sdk.wechatlibrary.config.MessageConfig;
import com.sce.sdk.wechatlibrary.data.MessageInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yuant
 */
public class MainActivity extends Activity implements View.OnClickListener {

  private static final int READ_REQUEST_CODE = 10;
  private static final String TAG = "MainActivity";

  /** View  **/
  TextView usage;
  RadioGroup radioGroup;
  RadioButton user_host;
  RadioButton user_remote;
  EditText messageText;
  Button addText;
  EditText messageSystemInfo;
  Button addSystemInfo;
  Button addImage;
  Button startApply;

  /**  data  **/
  int userType;
  private List<MessageInfo> list;
  WeChatManager manager;
  WeChatHandler mHandler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initView();
    initData();
  }

  private void initView() {
    usage = findViewById(R.id.usage);
    usage.setMovementMethod(ScrollingMovementMethod.getInstance());
    usage.setText(R.string.usage);

    radioGroup = findViewById(R.id.radioGroup);
    user_host = findViewById(R.id.user_host);
    user_remote = findViewById(R.id.user_remote);
    radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
        if(checkId == R.id.user_host && user_host.isChecked()) {
          user_remote.setChecked(false);
          userType = MessageConfig.USER_SENT;
        }
        if(checkId == R.id.user_remote && user_remote.isChecked()) {
          user_host.setChecked(false);
          userType = MessageConfig.USER_RECV;
        }
      }
    });

    messageText = findViewById(R.id.messageText);
    addText = findViewById(R.id.addText);
    addText.setOnClickListener(this);

    messageSystemInfo = findViewById(R.id.messageSystemInfo);
    addSystemInfo = findViewById(R.id.addSystemInfo);
    addSystemInfo.setOnClickListener(this);

    addImage = findViewById(R.id.addImage);
    addImage.setOnClickListener(this);

    startApply = findViewById(R.id.startApply);
    startApply.setOnClickListener(this);
  }

  private void initData() {
    userType = MessageConfig.USER_SENT;
    manager = WeChatManager.with(getApplicationContext());
    list = new ArrayList<>();
    mHandler = new WeChatHandler(this);
  }

  @Override
  public void onClick(View view) {
    switch(view.getId()) {
      case R.id.addText:
        performTextPush();
        break;
      case R.id.addImage:
        performFileSearch();
        break;
      case R.id.addSystemInfo:
        performSystemInfoPush();
        break;
      case R.id.startApply:
        performApply();
        break;
      default:
        break;
    }
  }

  private void performApply() {
    if(WeChatManager.get() != null) {
      WeChatManager.setMessageQueue(list);
      WeChatManager.start();
    }
  }

  private void performTextPush() {
    String text = messageText.getText().toString();
    list.add(new MessageInfo(MessageConfig.MESSAGE_TEXT, userType, text, "Amy"));
  }

  private void performSystemInfoPush() {
    String systemInfo = messageSystemInfo.getText().toString();
    list.add(new MessageInfo(MessageConfig.MESSAGE_SYSTEMINFO, userType, systemInfo, "Mary"));
  }

  /**
   * Fires an intent to spin up the "file chooser" UI and select an image.
   * 视图检索文件
   */
  private void performFileSearch() {
    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
    intent.addCategory(Intent.CATEGORY_OPENABLE);
    intent.setType("image/*");
    startActivityForResult(intent, READ_REQUEST_CODE);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode,
      Intent resultData) {
    super.onActivityResult(requestCode, resultCode, resultData);
    if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
      Uri uri = null;
      if (resultData != null) {
        uri = resultData.getData();
        Log.i(TAG, "Uri: " + uri.toString());
        list.add(new MessageInfo(MessageConfig.MESSAGE_IMAGE, userType, uri, "Sally"));
      }
    }
  }
}
