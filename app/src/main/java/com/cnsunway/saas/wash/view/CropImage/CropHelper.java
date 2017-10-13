package com.cnsunway.saas.wash.view.CropImage;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.activity.CropImageActivity;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.dialog.LoadingDialog;
import com.cnsunway.saas.wash.framework.net.JsonVolley;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.model.LocationForService;
import com.cnsunway.saas.wash.model.UploadToken;
import com.cnsunway.saas.wash.model.User;
import com.cnsunway.saas.wash.resp.UpdateTokenResp;
import com.cnsunway.saas.wash.resp.UpdateUserResp;
import com.cnsunway.saas.wash.sharef.UserInfosPref;
import com.cnsunway.saas.wash.util.BitmapUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by peter on 16/2/19.
 */
public class CropHelper {

    public static final int HEAD_FROM_ALBUM = 2106;
    public static final int HEAD_FROM_CAMERA = 2107;
    public static final int HEAD_FROM_CROP = 2108;

    private Fragment fragment;
    private Activity activity;
    private Dialog uploadHeadPortraitDialog;
    private ImageLoadingListener headImageListener;

    public CropHelper(Fragment frag,ImageLoadingListener imageLoadListener){
        this.fragment = frag;
        if(frag != null){
            activity = frag.getActivity();
        }
        headImageListener = imageLoadListener;
    }



    public Activity getActivity(){
        if(fragment != null){
            return fragment.getActivity();
        }
        return activity;
    }

    public Fragment getFragment(){
        return fragment;
    }

    public void showChooseDialog() {
        if(getActivity() == null){
            return;
        }
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_photo_choose, null);
        final Dialog dialog = new Dialog(getActivity(), R.style.transparentStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        Button btnGallery = (Button)dialog.findViewById(R.id.btn_gallery);
        Button btnCamera = (Button)dialog.findViewById(R.id.btn_camera);
        Button btnCancel = (Button)dialog.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                if(getFragment() != null) {
                    getFragment().startActivityForResult(intent, HEAD_FROM_ALBUM);
                }else if(getActivity() != null){
                    getActivity().startActivityForResult(intent, HEAD_FROM_ALBUM);
                }

                dialog.cancel();
            }
        });
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 下面这句指定调用相机拍照后的照片存储的路径
                String tempPhotoPath = getPhotoPath();
                File temp = new File(tempPhotoPath);
                if (temp.exists()) {
                    temp.delete();
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(temp));
                if(getFragment() != null) {
                    getFragment().startActivityForResult(intent, HEAD_FROM_CAMERA);
                }else if(getActivity() != null){
                    getActivity().startActivityForResult(intent, HEAD_FROM_CAMERA);
                }

                dialog.cancel();
            }
        });

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    /** * 以CenterCrop方式resize图片 *
     *  @param src 原始图片 *
     *  @return */
    public Bitmap resizeBitmapByCenterCrop(Bitmap src) {
        if (src == null ) { return null; }
        // 图片宽度
        int w = src.getWidth();
        // 图片高度
        int h = src.getHeight();
        // Imageview宽度 int x = destWidth;
        // Imageview高度 int y = destHeight;
        // 高宽比之差
        int temp = 1 - (h / w);
        int x = w;
        int y = w;
        // /** * 判断高宽比例，如果目标高宽比例大于原图，则原图高度不变，宽度为(w1 = (h * x) / y)拉伸 * 画布宽高(w1,h),在原图的((w - w1) / 2, 0)位置进行切割 */
        if (temp > 0) { // 计算画布宽度
            int w1 = (h * x) / y;
            // 创建一个指定高宽的图片
            Bitmap newb = Bitmap.createBitmap(src, (w - w1) / 2, 0, w1, h);
            return newb;
        }
        else { /** * 如果目标高宽比小于原图，则原图宽度不变，高度为(h1 = (y * w) / x), * 画布宽高(w, h1), 原图切割点(0, (h - h1) / 2) */ // 计算画布高度
            int h1 = (y * w) / x; // 创建一个指定高宽的图片
            Bitmap newb = Bitmap.createBitmap(src, 0, (h - h1) / 2, w, h1);
            return newb;
        }
    }

    public String getPhotoPath(){
        File path = Environment.getExternalStorageDirectory();
        String tempPhotoPath = path.getAbsolutePath()+"/head.png";
        return tempPhotoPath;
    }

    public void getDataFromCamera(Intent data) {
        File temp = new File(getPhotoPath());
        if (!temp.exists()) {
            return;
        } else {
            Uri uri = null;
            try {
                uri = Uri.parse(getPhotoPath());
                Bitmap bitmap = getBitmapFromUri(uri);
                if(startCrop(uri)){
                    return;
                }
                if(bitmap != null) {
                    showUploadHeadDialog();

                    upload(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void getDataFromAlbum(Intent data) {
        if (data == null) {
        } else {
            Uri uri = null;
            uri = data.getData();
            if (uri != null) {
                if(startCrop(uri)){
                    return;
                }
                Bitmap bitmap = getBitmapFromUri(uri);
                if(bitmap != null) {
                    showUploadHeadDialog();
                    upload(bitmap);
                }
            }
        }
    }

    public Bitmap getBitmapFromUri(Uri target) {
        if(getActivity() == null){
            return null;
        }
        InputStream is = null;
        try {

            // ContentResolver cr = getContentResolver();
            // is = cr.openInputStream(target);
            // mBitmap = BitmapFactory.decodeStream(is);
            File file = new File(target.getPath());

            ContentResolver cr = getActivity().getContentResolver();
            Cursor cursor = cr.query(target, null, null, null, null);// 根据Uri从数据库中找
            if (cursor == null && file.isFile()) {
                String path = file.getAbsolutePath();
                BitmapUtils.setSize(500, 500);
                Bitmap bitmap = BitmapUtils.getBitmap(new FileInputStream(file));
                if(readPictureDegree(path)!=0){
                    bitmap=rotaingImageBitmap(readPictureDegree(path), bitmap);
                }
                BitmapUtils.reset();

                return bitmap;
            } else {
                BitmapUtils.setSize(500, 500);
                Bitmap bitmap = BitmapUtils.getBitmap(is);

                is = cr.openInputStream(target);
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

    private Bitmap uploadBitmap;
    private Uri uploadUnCropUri;
    private void upload(Bitmap bitmap){
        uploadBitmap = bitmap;

        JsonVolley uploadTokenVolley =  new JsonVolley(getActivity(),
                Const.Message.MSG_UPLOADTOKEN_SUCC,
                Const.Message.MSG_UPLOADTOKEN_FAIL);
        UserInfosPref userInfos = UserInfosPref.getInstance(getActivity());
        LocationForService locationForService = UserInfosPref.getInstance(getActivity()).getLocationServer();
        String accessToken = userInfos.getUser().getToken();
        uploadTokenVolley.requestGet(Const.Request.uploadPic,
                getHandler(), accessToken,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
    }

    public Handler getHandler(){
        return handler;
    }
    protected Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (getActivity() == null ||getActivity().isFinishing()) {
                return;
            }
            handlerMessage(msg);
        }
    };

    protected void handlerMessage(Message msg) {
        switch (msg.what) {
            case Const.Message.MSG_UPLOADTOKEN_SUCC:

                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    UpdateTokenResp respSucc = (UpdateTokenResp) JsonParser.jsonToObject(msg.obj + "", UpdateTokenResp.class);
                    UploadToken uploadToken = respSucc.getData();
                    if(uploadToken == null){
                        return;
                    }
                    String imageUrl = uploadToken.userOid;
                    Date date = new Date();
                    updateHeadInfo(uploadToken.picUrlPrefix,date.getTime() + "_" + imageUrl,uploadToken.uploadToken);
                }else{
                    hideUploadHeadDialog();
                }
                break;
            case Const.Message.MSG_UPLOADTOKEN_FAIL:
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                }
                hideUploadHeadDialog();
                break;
            case Const.Message.MSG_UPLOADHEAD_SUCC:
                hideUploadHeadDialog();
                if (msg.arg1 == Const.Request.REQUEST_SUCC){
                    UserInfosPref userInfos = UserInfosPref.getInstance(getActivity());
//{"data":{"promoterCode":null,"lastLoginDate":"2016-02-17 16:38:09","memo":null,"status":1,"password":null,"orderCount":0,"userOid":"df2152f1974011e5b1fa8038bc0b555d","wxOpenId":"ontm8tyxsBI0Ll_t2Crmcm_xjOgg","nickName":null,"token":"2e0963ca976d11e5b1fa8038bc0b555d","deviceToken":"AkyszT8wG-2iWAzYDfrZ5RlOoSQJ3k0VbuuBH_3Bj4Py","deviceType":1,"headPortraitUrl":"http:\/\/7xorpq.dl1.z0.glb.clouddn.com\/df2152f1974011e5b1fa8038bc0b555d.png","inviteCode":null,"gender":null,"channel":22,"mobile":"13817048334"},"now":"2016-02-17 16:52:30","responseCode":0,"responseMsg":null}
                    UpdateUserResp respSucc = (UpdateUserResp) JsonParser.jsonToObject(msg.obj + "", UpdateUserResp.class);
                    User updateUser = respSucc.getData();
                    if (updateUser != null) {
                        if (userInfos != null) {
                            userInfos.saveUser(updateUser);
                        }
                    }
                }
                break;
            case Const.Message.MSG_UPLOADHEAD_FAIL:
                hideUploadHeadDialog();
                break;
            case Const.Message.MSG_PROFILE_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC){
                    UpdateUserResp respSucc = (UpdateUserResp) JsonParser.jsonToObject(msg.obj + "", UpdateUserResp.class);

                    User updateUser = respSucc.getData();
                    if(updateUser != null ) {
                        UserInfosPref userInfos = UserInfosPref.getInstance(getActivity());
                        if (userInfos != null) {
                            userInfos.saveUser(updateUser);
                        }
                        if(!TextUtils.isEmpty(updateUser.getHeadPortraitUrl())){
                            ImageLoader imageLoader = ImageLoader.getInstance();
                            imageLoader.loadImage(updateUser.getHeadPortraitUrl(), headImageListener);
                        }


//                        VolleyLoadPicture vlp = new VolleyLoadPicture(getActivity(), headPortrait);
//                        vlp.getmImageLoader().get(updateUser.headPortraitUrl + "?_=" + System.currentTimeMillis(), headImageListener);
                    }
                }
                break;
            case Const.Message.MSG_PROFILE_FAIL:
                break;
            default:
                break;
        }
    }
    private void updateHeadInfo(final String prefixUrl,final String key,String token){

        UserInfosPref userInfos = UserInfosPref.getInstance(getActivity());
        if(userInfos == null || userInfos.getUser() == null){
            return;
        }
        String accessToken = userInfos.getUser().getToken();

        String oid = accessToken;
        if(key != null){
            oid = key;
        }
        oid += ".png";
        Configuration config = new Configuration.Builder()
                .chunkSize(256 * 1024)  //分片上传时，每片的大小。 默认 256K
                .putThreshhold(512 * 1024)  // 启用分片上传阀值。默认 512K
                .connectTimeout(10) // 链接超时。默认 10秒
                .responseTimeout(60) // 服务器响应超时。默认 60秒
                .build();
        // 重用 uploadManager。一般地，只需要创建一个 uploadManager 对象
        UploadManager uploadManager = new UploadManager(config);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        uploadBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        uploadManager.put(baos.toByteArray(), oid, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        if (info != null && info.statusCode != 200) {
                            if (uploadHeadPortraitDialog != null) {
                                uploadHeadPortraitDialog.cancel();
                                uploadHeadPortraitDialog = null;
                            }
                            return;
                        }
//                        headPortrait.setImageBitmap(uploadBitmap);
                        if(headImageListener != null){
                            headImageListener.onLoadingComplete("",null,uploadBitmap);
                        }
                        //  res 包含hash、key等信息，具体字段取决于上传策略的设置。
                        Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                        JsonVolley uploadTokenVolley = new JsonVolley(getActivity(),
                                Const.Message.MSG_UPLOADHEAD_SUCC,
                                Const.Message.MSG_UPLOADHEAD_FAIL);
                        UserInfosPref userInfos = UserInfosPref.getInstance(getActivity());
                        String accessToken = userInfos.getUser().getToken();
                        uploadTokenVolley.addParams("headPortraitUrl", prefixUrl + key);
                        LocationForService locationForService = UserInfosPref.getInstance(getActivity()).getLocationServer();

                        uploadTokenVolley.requestPost(Const.Request.operatoresSave,
                                getHandler(), accessToken,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
                        );
                    }
                }, null);

    }

    private void showUploadHeadDialog(){
        if(uploadHeadPortraitDialog == null){
            uploadHeadPortraitDialog = new LoadingDialog(getActivity(), getActivity().getString(R.string.operating));
            uploadHeadPortraitDialog.setCancelable(false);
            uploadHeadPortraitDialog.show();
        }
    }
    private void hideUploadHeadDialog(){
        if(uploadHeadPortraitDialog != null) {
            uploadHeadPortraitDialog.cancel();
            uploadHeadPortraitDialog = null;
        }
    }

    public boolean startCrop(Uri uri) {
        boolean canCrop = false;
        try {
            Intent intent = new Intent(getActivity(), CropImageActivity.class);
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");// 进行修剪
            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("return-data", true);
            if(getFragment() != null) {
                getFragment().startActivityForResult(intent, HEAD_FROM_CROP);
            }else if(getActivity() != null) {
                getActivity().startActivityForResult(intent, HEAD_FROM_CROP);
            }
            canCrop = true;
            return canCrop;
        }catch (Exception e){

        }catch (Error err){

        }
        return canCrop;
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_CANCELED) {
            if(requestCode == HEAD_FROM_CROP){

            }
            return;
        } else {
            switch (requestCode) {
                case HEAD_FROM_ALBUM:
                    Log.e("onActivityResult", "接收到图库图片");
                    getDataFromAlbum(data);
                    break;
                case HEAD_FROM_CAMERA:
                    Log.e("onActivityResult", "接收到拍照图片");
                    getDataFromCamera(data);
                    break;
                case HEAD_FROM_CROP:
//                    Bitmap bitmap = (Bitmap)data.getParcelableExtra("data");
                    String path = data.getStringExtra("path");

                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapUtils.getBitmap(new FileInputStream(path));
                    }catch (Exception e){

                    }
                    if(bitmap != null) {
                        showUploadHeadDialog();
                        upload(bitmap);
                    }
                    break;
            }
        }

    }

}
