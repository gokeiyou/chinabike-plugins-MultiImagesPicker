package com.chinabike.plugins;

import android.app.Activity;
import android.content.Intent;

import com.chinabike.plugins.mip.AppContext;
import com.chinabike.plugins.mip.activity.LocalAlbum;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/3.
 */
public class MultiImagesPicker extends CordovaPlugin {

    private  CallbackContext callbackContext;
    private JSONObject params;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        this.callbackContext = callbackContext;
        this.params = args.getJSONObject(0);

        if (action.equals("getPictures")) {

            Intent intent = new Intent(cordova.getActivity(), LocalAlbum.class);

            int max = 9;
            int desiredWidth = 0;
            int desiredHeight = 0;
            int quality = 100;
            if (this.params.has("max")) {
                max = this.params.getInt("max");
            }
            if (this.params.has("width")) {
                desiredWidth = this.params.getInt("width");
            }
            if (this.params.has("height")) {
                desiredWidth = this.params.getInt("height");
            }
            if (this.params.has("quality")) {
                quality = this.params.getInt("quality");
            }

            intent.putExtra("MAX_IMAGES", max);
            intent.putExtra("WIDTH", desiredWidth);
            intent.putExtra("HEIGHT", desiredHeight);
            intent.putExtra("QUALITY", quality);
            cordova.startActivityForResult(this, intent, 200);

            return true;
        }

        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        AppContext app = AppContext.getInstance();
        if (app.getResultCode() != 0) {
            resultCode = app.getResultCode();
        }
        ArrayList<String> fileNames = app.getFileNames();
        String errMsg = app.getErrMsg();
        if (resultCode == Activity.RESULT_OK && fileNames != null) {
//            ArrayList<String> fileNames = data.getStringArrayListExtra("MULTIPLEFILENAMES");
            JSONArray res = new JSONArray(fileNames);
            this.callbackContext.success(res);
        } else if (resultCode == Activity.RESULT_CANCELED && errMsg != null) {
//            String error = data.getStringExtra("ERRORMESSAGE");
            this.callbackContext.error(errMsg);
        } else if (resultCode == Activity.RESULT_CANCELED) {
            JSONArray res = new JSONArray();
            this.callbackContext.success(res);
        } else {
//            this.callbackContext.error("No images selected");
            this.callbackContext.error("没有选中照片");
        }
    }
}
