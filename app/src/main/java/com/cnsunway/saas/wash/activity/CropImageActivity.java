package com.cnsunway.saas.wash.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.util.BitmapUtils;
import com.cnsunway.saas.wash.view.CropImage.CropImageView;
import com.cnsunway.saas.wash.view.CropImage.CropView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CropImageActivity extends BaseActivity implements View.OnClickListener{

    private CropView imageBackground;
    private CropImageView imageCrop;
    private RelativeLayout root;
    private int widthOfScreen;
    private int heightOfScreen;
    private Button btnCancel;
    private Button btnPick;
    private TextView titleText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);
        root=(RelativeLayout) findViewById(R.id.root);
        imageCrop=(CropImageView) findViewById(R.id.image);
        imageBackground=(CropView) findViewById(R.id.image_background);
        imageBackground.setLineHeight(1);
        imageBackground.setLineWidth(1);
        widthOfScreen=getWindowManager().getDefaultDisplay().getWidth();
        heightOfScreen=getWindowManager().getDefaultDisplay().getHeight();

        Uri uri = getIntent().getData();
        imageCrop.setImageBitmap(getBitmapFromUri(uri));
        titleText = (TextView) findViewById(R.id.text_title);
        titleText.setText("头像选取");

        btnCancel = (Button)findViewById(R.id.btn_cancel);
        btnPick = (Button)findViewById(R.id.btn_pick);
        btnCancel.setOnClickListener(this);
        btnPick.setOnClickListener(this);
    }

    public Bitmap getBitmapFromUri(Uri target) {

        InputStream is = null;
        try {

            // ContentResolver cr = getContentResolver();
            // is = cr.openInputStream(target);
            // mBitmap = BitmapFactory.decodeStream(is);
            File file = new File(target.getPath());
            if (file.isFile()) {
                BitmapUtils.setSize(512, 512);//最大支持，否则会sample
                Bitmap bitmap = BitmapUtils.getBitmap(new FileInputStream(file));
                String path = file.getAbsolutePath();
                if(readPictureDegree(path)!=0){
                    bitmap=rotaingImageBitmap(readPictureDegree(path), bitmap);
                }
                BitmapUtils.reset();
                return bitmap;
            } else {
                BitmapUtils.setSize(512, 512);//最大支持，否则会sample
                ContentResolver cr = getContentResolver();
                is = cr.openInputStream(target);
                Bitmap bitmap = BitmapUtils.getBitmap(is);
                Cursor cursor = cr.query(target, null, null, null, null);// 根据Uri从数据库中找
                if (cursor != null) {
                    cursor.moveToFirst();// 把游标移动到首位，因为这里的Uri是包含ID的所以是唯一的不需要循环找指向第一个就是了
                    String filePath = cursor.getString(cursor.getColumnIndex("_data"));// 获取图片路
                    String orientation = cursor.getString(cursor
                            .getColumnIndex("orientation"));// 获取旋转的角度
                    cursor.close();
                    int angle = 0;
                    if (orientation != null && !"".equals(orientation)) {
                        angle = Integer.parseInt(orientation);
                    }
                    if (angle != 0) {
                        // 下面的方法主要作用是把图片转一个角度，也可以放大缩小等
                        Matrix m = new Matrix();
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        m.setRotate(angle); // 旋转angle度
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                                m, true);// 从新生成图片

                    }
                };

                BitmapUtils.reset();
                return bitmap;
            }
        } catch (Exception e) {
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public void back(View v){
        finish();
    }
    public String getSavedPath(){
        File path = Environment.getExternalStorageDirectory();
        String tempPhotoPath = path.getAbsolutePath()+"/saved.png";
        return tempPhotoPath;
    }
    @Override
    public void onClick(View v) {
        if(v == btnCancel){
            setResult(Activity.RESULT_CANCELED);
            finish();
        }else if(v == btnPick){
            Bitmap cropBitmap = imageCrop.clip();
            Intent data = new Intent();
            BitmapUtils.saveFile(cropBitmap, getSavedPath());
            data.putExtra("path", getSavedPath());
            setResult(Activity.RESULT_OK, data);
            finish();
        }
    }


    /**
     * 读取图片旋转角度
     * 三星手机会旋转图片
     * @param imagePath
     * @return
     */
    public static int readPictureDegree(String imagePath) {
        int imageDegree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(imagePath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    imageDegree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    imageDegree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    imageDegree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageDegree;
    }

    /**
     * 旋转图片
     * @param angle
     * @param mBitmap
     * @return
     */
    public static Bitmap rotaingImageBitmap(int angle, Bitmap mBitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap b = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
        if(mBitmap!=null&&!mBitmap.isRecycled()){
            mBitmap.recycle();
            mBitmap=null;
        }
        return b;
    }
}
