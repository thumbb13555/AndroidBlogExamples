package com.noahliu.autofillexample.Service;

import android.app.assist.AssistStructure;
import android.content.Context;
import android.os.CancellationSignal;
import android.service.autofill.AutofillService;
import android.service.autofill.Dataset;
import android.service.autofill.FillCallback;
import android.service.autofill.FillContext;
import android.service.autofill.FillRequest;
import android.service.autofill.FillResponse;
import android.service.autofill.SaveCallback;
import android.service.autofill.SaveInfo;
import android.service.autofill.SaveRequest;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;
import android.widget.RemoteViews;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.noahliu.autofillexample.R;
import com.noahliu.autofillexample.SaveData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MyAutofill extends AutofillService {

    private final static String TAG = AutofillService.class.getSimpleName();

    @Override
    public void onConnected() {
        super.onConnected();
       //可以在這裡做初始設定

    }

    //當介面被觸發時回調這裡
    @Override
    public void onFillRequest(FillRequest request, CancellationSignal cancellationSignal,
                              FillCallback callback) {

        // 取得介面上所有的元件，並以陣列輸出有設定"hint"的元件之id
        List<FillContext> fillContexts = request.getFillContexts();
        AssistStructure structure = fillContexts.get(fillContexts.size() - 1).getStructure();
        //過濾出有設定"hint"屬性的元件，並輸出成陣列
        ArrayMap<String, AutofillId> fields = getAutofillableFields(structure);
        Log.e(TAG, "在目前介面中有被設定的元件列表:\n" + fields);

        // 新增回調
        FillResponse response;
        response = createResponse(this, fields);

        callback.onSuccess(response);
    }

    /**↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 此處為過濾整個畫面，並取得有設定autofillHints屬性的元件 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓*/
    @NonNull
    private ArrayMap<String, AutofillId> getAutofillableFields(@NonNull AssistStructure structure) {
        ArrayMap<String, AutofillId> fields = new ArrayMap<>();
        //取得該畫面中的節點數量
        int nodes = structure.getWindowNodeCount();
        for (int i = 0; i < nodes; i++) {
            AssistStructure.ViewNode node = structure.getWindowNodeAt(i).getRootViewNode();
            addAutofillableFields(fields, node);
        }
        return fields;
    }

    private void addAutofillableFields(@NonNull Map<String, AutofillId> fields,
                                       @NonNull AssistStructure.ViewNode node) {
        String hint = getHint(node);
        if (hint != null) {
            AutofillId id = node.getAutofillId();
            if (!fields.containsKey(hint)) {
                Log.e(TAG, "已找到有設定autofillHints的元件，hint為 '" + hint + "' 設定ID為 " + id);
                fields.put(hint, id);
            } else {
                //如果有重複設定的元件的話將會到這邊
                //比方說已經有檢測到某元件在前面被設定過的話，後面被偵測到的元件將會被無視掉
                Log.d(TAG, "忽視掉有重複設定autofillHints的元件，hint為 '" + hint + "' 設定ID為 " + id);
            }
        }
        //利用遞歸法持續遍歷整個介面
        int childrenSize = node.getChildCount();
        for (int i = 0; i < childrenSize; i++) {
            addAutofillableFields(fields, node.getChildAt(i));
        }
    }

    @Nullable
    protected String getHint(@NonNull AssistStructure.ViewNode node) {

        String viewHint = node.getHint();
        String hint = inferHint(node, viewHint);

        if (hint != null) {
            Log.e(TAG, "找到元件， hint(" + viewHint + "): " + hint);
            return hint;
        } else if (!TextUtils.isEmpty(viewHint)) {
            Log.d(TAG, "無提示元件: " + viewHint);
        }
        String resourceId = node.getIdEntry();
        hint = inferHint(node, resourceId);
        if (hint != null) {
            Log.e(TAG, "找到有提示的元件ID: (" + resourceId + "): " + hint);
            return hint;
        } else if (!TextUtils.isEmpty(resourceId)) {
            Log.d(TAG, "無使用提示的元件ID: " + resourceId);
        }

        //這邊是官方給的示範，意味著你可以僅過濾某個Class、某個字元或者只去過濾EditText之類的
        CharSequence text = node.getText();
        CharSequence className = node.getClassName();
        if (text != null && className != null && className.toString().contains("EditText")) {
            hint = inferHint(node, text.toString());
            if (hint != null) {
                Log.d(TAG, "Found hint using text(" + text + "): " + hint);
                return hint;
            }
        } else if (!TextUtils.isEmpty(text)) {
            Log.v(TAG, "No hint using text: " + text + " and class " + className);
        }
        return null;
    }

    /**比對有設定autofillHints的元件，有時候可能其他開發者會隨便亂設定的話必須也能夠抓取到*/
    @Nullable
    protected String inferHint(AssistStructure.ViewNode node, @Nullable String actualHint) {
        if (actualHint == null) return null;

        String hint = actualHint.toLowerCase();
        //忽視掉 'label/container' 的提示
        if (hint.contains("label") || hint.contains("container")) {
            return null;
        }
        //抓取提示為password
        if (hint.contains("password")) return View.AUTOFILL_HINT_PASSWORD;
        //抓取提示為username或者login+id的組合
        if (hint.contains("username")
                || (hint.contains("login") && hint.contains("id")))
            return View.AUTOFILL_HINT_USERNAME;
        //以下類推
        if (hint.contains("email")) return View.AUTOFILL_HINT_EMAIL_ADDRESS;
        if (hint.contains("name")) return View.AUTOFILL_HINT_NAME;
        if (hint.contains("phone")) return View.AUTOFILL_HINT_PHONE;
        //若元件狀態為啟用，且設定的Type不是NONE（意味著可能設定了不一樣的）
        if (node.isEnabled() && node.getAutofillType() != View.AUTOFILL_TYPE_NONE) {
            return actualHint;
        }
        return null;
    }

    /**↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 此處為過濾整個畫面，並取得有設定autofillHints屬性的元件 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑*/

    /**新增一個對於觸發物件後的回調*/
    static FillResponse createResponse(@NonNull Context context,
                                       @NonNull ArrayMap<String, AutofillId> fields) {
        String packageName = context.getPackageName();
        FillResponse.Builder response = new FillResponse.Builder();
        //TODO 可以在這邊做取出資料的操作，無論你是用Database或者sharePreference揭示在這邊做取出資料並操作

        //本範例在這邊做假資料
        ArrayList<SaveData> arrayList = new ArrayList<>();
        arrayList.add(new SaveData("Noah","123456"));
        arrayList.add(new SaveData("Edis","654321"));

        for (int i = 0; i < arrayList.size(); i++) {
            Dataset unlockedDataset = newUnlockedDataset(fields, packageName, arrayList.get(i));
            response.addDataset(unlockedDataset);
        }

        //如果使用者輸入了沒被儲存過的密碼，則彈出視窗主動詢問要不要儲存
        Collection<AutofillId> ids = fields.values();
        AutofillId[] requiredIds = new AutofillId[ids.size()];
        ids.toArray(requiredIds);
        response.setSaveInfo(
                new SaveInfo.Builder(SaveInfo.SAVE_DATA_TYPE_GENERIC, requiredIds).build());

        return response.build();
    }

    /**建立下拉式介面的內容與要填入的設定*/
    static Dataset newUnlockedDataset(@NonNull Map<String, AutofillId> fields,
                                      @NonNull String packageName, SaveData data) {
        Dataset.Builder dataset = new Dataset.Builder();
        //遍歷元件
        for (Map.Entry<String, AutofillId> field : fields.entrySet()) {
            String hint = field.getKey();
            AutofillId id = field.getValue();
            //如果介面的hint是username，則填入Name，否則填入Psw（通常這裡是非username則一定是password）
            String value = hint.contains("username")? data.getName() : data.getPsw();
            //這裡是下拉的提示介面，一般來說只會顯示帳號不會顯示密碼
            String displayValue = hint.contains("password") ? data.getName() : "無使用者名稱";

            //下拉提示的介面
            RemoteViews presentation =
                    new RemoteViews(packageName, R.layout.user_suggestion_item);
            presentation.setTextViewText(R.id.user_suggestion_item, displayValue);
            presentation.setImageViewResource(R.id.icon, R.drawable.ic_baseline_key_24);

            //將Autofill的要輸入的元件ID、要輸入的字以及下拉提示的介面
            dataset.setValue(id, AutofillValue.forText(value), presentation);
        }

        return dataset.build();
    }
    /**↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 在詢問建議「是否儲存密碼」後的回調 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓*/
    /**當在完成登入後的填出視窗，若使用者選擇儲存的話便會來到這個回調*/
    @Override
    public void onSaveRequest(SaveRequest request, SaveCallback callback) {
        SaveData data = null;
        List<FillContext> fillContexts = request.getFillContexts();
        for (FillContext context:fillContexts) {
            ArrayMap<String, String> fields = getInputWord(context.getStructure());
            data = new SaveData(fields.get("username"),fields.get("password"));
        }
        if (data != null){
            //TODO 將介面目前的輸入儲存起來
            Log.d(TAG, "username: "+data.getName()+", password: "+data.getPsw());
        }

        callback.onSuccess();
    }
    /**遍歷畫面中的元件，取得hint元件中所輸入的內容*/
    private ArrayMap<String, String> getInputWord(@NonNull AssistStructure structure) {
        ArrayMap<String, String> fields = new ArrayMap<>();
        //取得該畫面中的節點數量
        int nodes = structure.getWindowNodeCount();
        for (int i = 0; i < nodes; i++) {
            AssistStructure.ViewNode node = structure.getWindowNodeAt(i).getRootViewNode();
            addInputArray(fields, node);

        }
        return fields;
    }
    private void addInputArray(@NonNull Map<String, String> fields,
                                       @NonNull AssistStructure.ViewNode node) {
        String hint = getHint(node);
        if (hint != null) {
            AutofillId id = node.getAutofillId();
            if (!fields.containsKey(hint)) {
                Log.e(TAG, "已找到有設定autofillHints的元件，hint為 '" + hint + "' 設定ID為 " + id);
                fields.put(hint, node.getText().toString());
            }
        }
        //利用遞歸法持續遍歷整個介面
        int childrenSize = node.getChildCount();
        for (int i = 0; i < childrenSize; i++) {
            addInputArray(fields, node.getChildAt(i));
        }
    }
}
