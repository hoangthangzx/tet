package com.metalsensor.gold.detector.finder.databinding;
import com.metalsensor.gold.detector.finder.R;
import com.metalsensor.gold.detector.finder.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivitySettingBindingImpl extends ActivitySettingBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.lnStatus_2, 1);
        sViewsWithIds.put(R.id.imvBack, 2);
        sViewsWithIds.put(R.id.tvLanguage2, 3);
        sViewsWithIds.put(R.id.lnTriggerValue, 4);
        sViewsWithIds.put(R.id.linearLayout3, 5);
        sViewsWithIds.put(R.id.tvSetAlarm, 6);
        sViewsWithIds.put(R.id.tvTrigger, 7);
        sViewsWithIds.put(R.id.chose, 8);
        sViewsWithIds.put(R.id.layoutproces, 9);
        sViewsWithIds.put(R.id.relativeLayout, 10);
        sViewsWithIds.put(R.id.tex2, 11);
        sViewsWithIds.put(R.id.seekBar, 12);
        sViewsWithIds.put(R.id.ln1, 13);
        sViewsWithIds.put(R.id.lnAlarmSound, 14);
        sViewsWithIds.put(R.id.tvSound, 15);
        sViewsWithIds.put(R.id.sound, 16);
        sViewsWithIds.put(R.id.valumeon, 17);
        sViewsWithIds.put(R.id.valumeoff, 18);
        sViewsWithIds.put(R.id.texttoaudio, 19);
        sViewsWithIds.put(R.id.flasson, 20);
        sViewsWithIds.put(R.id.flassoff, 21);
        sViewsWithIds.put(R.id.mylbry, 22);
        sViewsWithIds.put(R.id.vibration, 23);
        sViewsWithIds.put(R.id.vibrationoff, 24);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivitySettingBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 25, sIncludes, sViewsWithIds));
    }
    private ActivitySettingBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.ImageView) bindings[8]
            , (android.widget.RelativeLayout) bindings[21]
            , (android.widget.RelativeLayout) bindings[20]
            , (android.widget.ImageView) bindings[2]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[9]
            , (android.widget.LinearLayout) bindings[5]
            , (android.widget.LinearLayout) bindings[13]
            , (android.widget.LinearLayout) bindings[14]
            , (android.widget.LinearLayout) bindings[1]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[4]
            , (android.widget.LinearLayout) bindings[22]
            , (android.widget.RelativeLayout) bindings[10]
            , (com.metalsensor.gold.detector.finder.customview.SeekBarSetSizeThumb2) bindings[12]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[16]
            , (android.widget.TextView) bindings[11]
            , (android.widget.LinearLayout) bindings[19]
            , (android.widget.TextView) bindings[3]
            , (android.widget.LinearLayout) bindings[6]
            , (android.widget.TextView) bindings[15]
            , (android.widget.TextView) bindings[7]
            , (android.widget.RelativeLayout) bindings[18]
            , (android.widget.RelativeLayout) bindings[17]
            , (android.widget.RelativeLayout) bindings[23]
            , (android.widget.RelativeLayout) bindings[24]
            );
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x1L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
            return variableSet;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        // batch finished
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}