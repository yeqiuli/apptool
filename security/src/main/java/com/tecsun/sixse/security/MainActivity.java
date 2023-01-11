package com.tecsun.sixse.security;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tecsun.jni.SSCardBean;
import com.tecsun.jni.TtIDCardBean;
import com.tecsun.jni.ZJReader;


public class MainActivity extends Activity {
	private String TAG = "MainActivity";
	private static UsbDevPermission usbPermission;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		usbPermission = new UsbDevPermission(this, new UsbDevPermission.onDevOpenListener() {
			@Override
			public void onListener(boolean isOpen) {
				if (isOpen){
					Toast.makeText(MainActivity.this, "设备初始化成功", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(MainActivity.this, "设备初始化失败", Toast.LENGTH_LONG).show();
				}
			}
		});
		initView();
		Log.d("MainActivity", ZJReader.GetLibVer());
	}

	@Override
	protected void onDestroy() {
		usbPermission.unUsbDevPermission();
		super.onDestroy();
	}

	private void initView() {
		final TextView txtView = (TextView) findViewById(R.id.textView);
		final ImageView imageView = (ImageView) findViewById(R.id.ivphoto) ;
		Button btn01 = (Button) findViewById(R.id.button01);
		btn01.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String strRcv;
				//strRcv = ZJReader.GetLibVer();
				TtIDCardBean idcardBean = new TtIDCardBean();
				idcardBean = ZJReader.iReadSFZ(idcardBean);
				if(idcardBean.getRetCode() < 0){
					strRcv = "读证件失败！返回码："+idcardBean.getRetCode() + "，错误信息："+idcardBean.getErrMsg();
				}else if(idcardBean.getRetCode() == 0){
					strRcv = "读身份证成功！"
							+ "姓名：" + idcardBean.getName() + "\n"
							+ "身份证号："+ idcardBean.getIdCardNo() + "\n"
							+ "性别："+ idcardBean.getSex() + "\n"
							+ "民族："+ idcardBean.getNation() + "\n"
							+ "出生日期："+ idcardBean.getBirthday() + "\n"
							+ "地址："+ idcardBean.getAddress() + "\n"
							+ "签发机构："+ idcardBean.getGrantdept() + "\n"
					;
					imageView.setImageBitmap(idcardBean.getPhoto());
				}else{
					strRcv = "读证件成功！证件类型：" + idcardBean.getRetCode();
				}
				Log.d("MainActivity", strRcv);
				txtView.setText(strRcv);
			}
		});

		Button btn02 = (Button) findViewById(R.id.button02);
		btn02.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String strRcv;
				if (!usbPermission.IsOpenDev()) {
					strRcv = "初始化F4MDS读卡器失败";
					Log.d("MainActivity", strRcv);
					txtView.setText(strRcv);
					return;
				}
				SSCardBean sscardBean = new SSCardBean();
				sscardBean = ZJReader.iReadSSCard(4, sscardBean);
				if(sscardBean.getRetCode() < 0){
					strRcv = "读社保卡失败！返回码："+sscardBean.getRetCode() + "，错误信息："+sscardBean.getErrMsg();
				}else{
					strRcv = "读社保卡成功！社会保障号：" + sscardBean.getCardId() + "\n" +
							"姓名：" + sscardBean.getCardName() + "\n" +
							"性别："+ sscardBean.getSex() + "\n";
				}
				Log.d("MainActivity", strRcv);
				txtView.setText(strRcv);


			}
		});

	}
}
